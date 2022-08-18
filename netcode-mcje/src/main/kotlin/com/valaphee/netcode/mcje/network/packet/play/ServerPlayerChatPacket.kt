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

import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.ServerPlayPacketHandler
import com.valaphee.netcode.mcje.network.V1_19_0
import net.kyori.adventure.text.Component
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
class ServerPlayerChatPacket(
    val message: Component,
    val unsignedMessage: Component?,
    val chatTypeId: Int,
    val userId: UUID?,
    val userName: Component?,
    val teamName: Component?,
    val time: Long,
    val salt: Long,
    val signature: ByteArray
) : Packet<ServerPlayPacketHandler>() {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeComponent(message)
        if (version >= V1_19_0) unsignedMessage?.let {
            buffer.writeBoolean(true)
            buffer.writeComponent(it)
        } ?: buffer.writeBoolean(false)
        buffer.writeVarInt(chatTypeId)
        buffer.writeUuid(userId ?: UUID(0, 0))
        if (version >= V1_19_0) {
            buffer.writeComponent(userName!!)
            teamName?.let {
                buffer.writeBoolean(true)
                buffer.writeComponent(it)
            } ?: buffer.writeBoolean(false)
            buffer.writeLong(time)
            buffer.writeLong(salt)
            buffer.writeByteArray(signature)
        }
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.playerChat(this)

    override fun toString() = "ServerPlayerChatPacket(message=$message, unsignedMessage=$unsignedMessage, chatTypeId=$chatTypeId, userId=$userId, userName=$userName, teamName=$teamName, time=$time, salt=$salt, signature=${signature.contentToString()})"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int): ServerPlayerChatPacket {
            val message = buffer.readComponent()
            val unsignedMessage = if (version >= V1_19_0 && buffer.readBoolean()) buffer.readComponent() else null
            val chatTypeId = buffer.readVarInt()
            val userId = buffer.readUuid()
            val userName: Component?
            val teamName: Component?
            val time: Long
            val salt: Long
            val signature: ByteArray
            if (version >= V1_19_0) {
                userName = buffer.readComponent()
                teamName = if (buffer.readBoolean()) buffer.readComponent() else null
                time = buffer.readLong()
                salt = buffer.readLong()
                signature = buffer.readByteArray()
            } else {
                userName = null
                teamName = null
                time = System.currentTimeMillis()
                salt = 0L
                signature = byteArrayOf()
            }
            return ServerPlayerChatPacket(message, unsignedMessage, chatTypeId, userId, userName, teamName, time, salt, signature)
        }
    }
}
