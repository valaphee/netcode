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
import com.valaphee.netcode.mcje.network.PacketReader
import com.valaphee.netcode.mcje.network.ServerPlayPacketHandler
import net.kyori.adventure.text.Component
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
class ServerTextSignedPacket(
    val message: Component,
    val unsignedMessage: Component?,
    val type: ServerTextPacket.Type,
    val userId: UUID?,
    val userName: Component,
    val teamName: Component?,
    val time: Long,
    val salt: Long,
    val signature: ByteArray
) : Packet<ServerPlayPacketHandler>() {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeComponent(message)
        unsignedMessage?.let {
            buffer.writeBoolean(true)
            buffer.writeComponent(it)
        } ?: buffer.writeBoolean(false)
        buffer.writeVarInt(type.ordinal)
        buffer.writeUuid(userId ?: UUID(0, 0))
        buffer.writeComponent(userName)
        teamName?.let {
            buffer.writeBoolean(true)
            buffer.writeComponent(it)
        } ?: buffer.writeBoolean(false)
        buffer.writeLong(time)
        buffer.writeLong(salt)
        buffer.writeByteArray(signature)
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.textSigned(this)

    override fun toString() = "ServerTextSignedPacket(message=$message, unsignedMessage=$unsignedMessage, type=$type, userId=$userId, userName=$userName, teamName=$teamName, time=$time, salt=$salt, signature=${signature.contentToString()})"
}

/**
 * @author Kevin Ludwig
 */
object ServerTextSignedPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerTextSignedPacket(buffer.readComponent(), if (buffer.readBoolean()) buffer.readComponent() else null, ServerTextPacket.Type.values()[buffer.readVarInt()], buffer.readUuid(), buffer.readComponent(), if (buffer.readBoolean()) buffer.readComponent() else null, buffer.readLong(), buffer.readLong(), buffer.readByteArray())
}
