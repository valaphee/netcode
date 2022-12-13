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
import com.valaphee.netcode.mcje.network.V1_19_1
import com.valaphee.netcode.mcje.network.V1_19_3
import com.valaphee.netcode.util.LazyList
import java.util.BitSet
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
class ClientCommandPacket(
    val command: String,
    val time: Long,
    val salt: Long,
    val signatures: List<Pair<String, ByteArray>>,
    val preview: Boolean,
    val lastSeenMessages: List<Pair<UUID, ByteArray>>?,
    val lastReceivedMessage: Pair<UUID, ByteArray>?,
    val offset: Int,
    val acknowledgedMessages: BitSet?
) : Packet<ClientPlayPacketHandler>() {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeString(command)
        buffer.writeLong(time)
        buffer.writeLong(salt)
        if (version >= V1_19_3) {
            buffer.writeVarInt(signatures.size)
            signatures.forEach {
                buffer.writeString(it.first)
                buffer.writeBytes(it.second)
            }
            buffer.writeVarInt(offset)
            // TODO acknowledgedMessages
        } else {
            buffer.writeVarInt(signatures.size)
            signatures.forEach {
                buffer.writeString(it.first)
                buffer.writeByteArray(it.second)
            }
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

    override fun handle(handler: ClientPlayPacketHandler) = handler.command(this)

    override fun toString() = "ClientCommandPacket(command='$command', time=$time, salt=$salt, signatures=$signatures, preview=$preview, lastSeenMessages=$lastSeenMessages, lastReceivedMessage=$lastReceivedMessage)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int): ClientCommandPacket {
            val command = buffer.readString(256)
            val time = buffer.readLong()
            val salt = buffer.readLong()
            val signatures: List<Pair<String, ByteArray>>
            val preview: Boolean
            val lastSeenMessages: List<Pair<UUID, ByteArray>>?
            val lastReceivedMessage: Pair<UUID, ByteArray>?
            val offset: Int
            val acknowledgedMessages: BitSet?
            if (version >= V1_19_3) {
                signatures = LazyList(buffer.readVarInt()) { buffer.readString() to ByteArray(256).also(buffer::readBytes) }
                preview = false
                lastSeenMessages = null
                lastReceivedMessage = null
                offset = buffer.readVarInt()
                acknowledgedMessages = null // TODO acknowledgedMessages
            } else {
                signatures = LazyList(buffer.readVarInt()) { buffer.readString() to buffer.readByteArray() }
                preview = buffer.readBoolean()
                if (version >= V1_19_1) {
                    lastSeenMessages = LazyList(buffer.readVarInt()) { buffer.readUuid() to buffer.readByteArray() }
                    lastReceivedMessage = if (buffer.readBoolean()) buffer.readUuid() to buffer.readByteArray() else null
                } else {
                    lastSeenMessages = null
                    lastReceivedMessage = null
                }
                offset = 0
                acknowledgedMessages = null
            }
            return ClientCommandPacket(command, time, salt, signatures, preview, lastSeenMessages, lastReceivedMessage, offset, acknowledgedMessages)
        }
    }
}
