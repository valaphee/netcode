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

package com.valaphee.netcode.mcje.play

import com.fasterxml.jackson.module.kotlin.readValue
import com.valaphee.netcode.mcje.Packet
import com.valaphee.netcode.mcje.PacketBuffer
import com.valaphee.netcode.mcje.PacketReader
import com.valaphee.netcode.mcje.util.text.Component
import com.valaphee.netcode.util.ByteBufStringReader
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
class ServerTextPacket(
    val message: Component,
    val type: Type,
    val userId: UUID = UserIdNone
) : Packet<ServerPlayPacketHandler> {
    enum class Type {
        Chat, System, Tip
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeString(buffer.objectMapper.writeValueAsString(message))
        buffer.writeByte(type.ordinal)
        if (version >= 754) buffer.writeUuid(userId)
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.text(this)

    override fun toString() = "ServerTextPacket(message=$message, type=$type, userId=$userId)"

    companion object {
        val UserIdNone = UUID(0, 0)
    }
}

/**
 * @author Kevin Ludwig
 */
object ServerTextPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerTextPacket(buffer.objectMapper.readValue(ByteBufStringReader(buffer, buffer.readVarInt())), ServerTextPacket.Type.values()[buffer.readByte().toInt()], if (version >= 754) buffer.readUuid() else ServerTextPacket.UserIdNone)
}
