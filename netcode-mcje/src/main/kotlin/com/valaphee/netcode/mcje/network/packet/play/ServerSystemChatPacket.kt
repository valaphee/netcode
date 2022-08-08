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
import com.valaphee.netcode.mcje.network.V1_19_0
import net.kyori.adventure.text.Component
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
class ServerSystemChatPacket(
    val message: Component,
    val type: Type,
    val userId: UUID? = null
) : Packet<ServerPlayPacketHandler>() {
    enum class Type {
        Chat, System, Tip, SayCommand, MsgCommand, TeamMsgCommand, EmoteCommand, TellrawCommand
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeComponent(message)
        buffer.writeByte(type.ordinal)
        if (version < V1_19_0) buffer.writeUuid(userId ?: UUID(0, 0))
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.systemChat(this)

    override fun toString() = "ServerSystemChatPacket(message=$message, type=$type, userId=$userId)"
}

/**
 * @author Kevin Ludwig
 */
object ServerSystemChatPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerSystemChatPacket(buffer.readComponent(), ServerSystemChatPacket.Type.values()[buffer.readByte().toInt()], if (version < V1_19_0) buffer.readUuid() else null)
}
