/*
 * Copyright (c) 2021-2022, Valaphee.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.valaphee.netcode.mcje.network.packet.play

import com.valaphee.netcode.mcje.network.ClientPlayPacketHandler
import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.V1_19_0
import com.valaphee.netcode.mcje.network.V1_19_1
import com.valaphee.netcode.mcje.network.V1_19_3
import com.valaphee.netcode.util.LazyList
import java.util.BitSet
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
class ClientChatPacket(
    val message: String,
    val time: Long,
    val salt: Long,
    val signature: ByteArray?,
    val preview: Boolean,
    val lastSeenMessages: List<Pair<UUID, ByteArray>>?,
    val lastReceivedMessage: Pair<UUID, ByteArray>?,
    val offset: Int,
    val acknowledgedMessages: BitSet?
) : Packet<ClientPlayPacketHandler>() {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeString(message)
        if (version >= V1_19_3) {
            buffer.writeLong(time)
            buffer.writeLong(salt)
            signature?.let {
                buffer.writeBoolean(true)
                buffer.writeBytes(it)
            } ?: buffer.writeBoolean(false)
            buffer.writeVarInt(offset)
            // TODO acknowledgedMessages
        } else if (version >= V1_19_0) {
            buffer.writeLong(time)
            buffer.writeLong(salt)
            buffer.writeByteArray(signature!!)
            buffer.writeBoolean(preview)
            if (version >= V1_19_1) {
                buffer.writeVarInt(lastSeenMessages!!.size)
                lastSeenMessages.forEach {
                    buffer.writeUuid(it.first)
                    buffer.writeByteArray(it.second)
                }
                lastReceivedMessage?.let {
                    buffer.writeBoolean(true)
                    buffer.writeUuid(it.first)
                    buffer.writeByteArray(it.second)
                } ?: buffer.writeBoolean(false)
            }
        }
    }

    override fun handle(handler: ClientPlayPacketHandler) = handler.chat(this)

    override fun toString() = "ClientChatPacket(message='$message', time=$time, salt=$salt, signature=${signature?.contentToString()}, preview=$preview, lastSeenMessages=$lastSeenMessages, lastReceivedMessage=$lastReceivedMessage)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int): ClientChatPacket {
            val message = buffer.readString(256)
            val time: Long
            val salt: Long
            val signature: ByteArray?
            val preview: Boolean
            val lastSeenMessages: List<Pair<UUID, ByteArray>>?
            val lastReceivedMessage: Pair<UUID, ByteArray>?
            val offset: Int
            val acknowledgedMessages: BitSet?
            if (version >= V1_19_3) {
                time = buffer.readLong()
                salt = buffer.readLong()
                signature = if (buffer.readBoolean()) ByteArray(256).also(buffer::readBytes) else null
                preview = false
                lastSeenMessages = null
                lastReceivedMessage = null
                offset = buffer.readVarInt()
                acknowledgedMessages = null // TODO acknowledgedMessages
            } else if (version >= V1_19_0) {
                time = buffer.readLong()
                salt = buffer.readLong()
                signature = buffer.readByteArray(256)
                preview = buffer.readBoolean()
                if (version >= V1_19_1) {
                    lastSeenMessages = LazyList(buffer.readVarInt()) { buffer.readUuid() to buffer.readByteArray(256) }
                    lastReceivedMessage = if (buffer.readBoolean()) buffer.readUuid() to buffer.readByteArray(256) else null
                } else {
                    lastSeenMessages = emptyList()
                    lastReceivedMessage = null
                }
                offset = 0
                acknowledgedMessages = null
            } else {
                time = 0
                salt = 0
                signature = null
                preview = false
                lastSeenMessages = emptyList()
                lastReceivedMessage = null
                offset = 0
                acknowledgedMessages = null
            }
            return ClientChatPacket(message, time, salt, signature, preview, lastSeenMessages, lastReceivedMessage, offset, acknowledgedMessages)
        }
    }
}
