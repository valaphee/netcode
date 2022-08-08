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
import com.valaphee.netcode.mcje.network.PacketReader
import com.valaphee.netcode.mcje.network.V1_19_0

/**
 * @author Kevin Ludwig
 */
class ClientPlayerChatPacket(
    val message: String,
    val time: Long,
    val salt: Long,
    val signature: ByteArray?,
    val preview: Boolean
) : Packet<ClientPlayPacketHandler>() {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeString(message)
        if (version >= V1_19_0) {
            buffer.writeLong(time)
            buffer.writeLong(salt)
            buffer.writeByteArray(signature!!)
            buffer.writeBoolean(preview)
        }
    }

    override fun handle(handler: ClientPlayPacketHandler) = handler.playerChat(this)

    override fun toString() = "ClientPlayerChatPacket(message='$message', time=$time, salt=$salt, signature=${signature.contentToString()}, preview=$preview)"
}

/**
 * @author Kevin Ludwig
 */
object ClientPlayerChatPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): ClientPlayerChatPacket {
        val message = buffer.readString(256)
        val time: Long
        val salt: Long
        val signature: ByteArray?
        val preview: Boolean
        if (version >= V1_19_0) {
            time = buffer.readLong()
            salt = buffer.readLong()
            signature = buffer.readByteArray()
            preview = buffer.readBoolean()
        } else {
            time = 0
            salt = 0
            signature = null
            preview = false
        }
        return ClientPlayerChatPacket(message, time, salt, signature, preview)
    }
}
