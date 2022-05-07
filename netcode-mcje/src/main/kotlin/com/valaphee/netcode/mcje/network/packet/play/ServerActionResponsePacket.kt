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

import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.PacketReader
import com.valaphee.netcode.mcje.network.ServerPlayPacketHandler

/**
 * @author Kevin Ludwig
 */
class ServerActionResponsePacket(
    val position: Int3,
    val blockStateId: Int,
    val action: Action,
    val succeed: Boolean
) : Packet<ServerPlayPacketHandler>() {
    enum class Action {
        StartBreak, AbortBreak, StopBreak
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeInt3UnsignedY(position)
        buffer.writeVarInt(blockStateId)
        buffer.writeVarInt(action.ordinal)
        buffer.writeBoolean(succeed)
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.actionResponse(this)

    override fun toString() = "ServerActionResponsePacket(position=$position, blockStateId=$blockStateId, action=$action, succeed=$succeed)"
}

/**
 * @author Kevin Ludwig
 */
object ServerActionResponsePacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerActionResponsePacket(buffer.readInt3UnsignedY(), buffer.readVarInt(), ServerActionResponsePacket.Action.values()[buffer.readVarInt()], buffer.readBoolean())
}
