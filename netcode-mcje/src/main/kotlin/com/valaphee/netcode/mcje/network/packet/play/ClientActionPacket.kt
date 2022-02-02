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
import com.valaphee.netcode.mc.util.Direction
import com.valaphee.netcode.mcje.network.ClientPlayPacketHandler
import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.PacketReader

/**
 * @author Kevin Ludwig
 */
class ClientActionPacket(
    val action: Action,
    val blockPosition: Int3,
    val blockFace: Direction,
) : Packet<ClientPlayPacketHandler> {
    enum class Action {
        StartBreak, AbortBreak, StopBreak, DropStack, DropStack1, ReleaseItem, SwapHands
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(action.ordinal)
        buffer.writeInt3UnsignedY(blockPosition)
        buffer.writeDirection(blockFace)
    }

    override fun handle(handler: ClientPlayPacketHandler) = handler.action(this)

    override fun toString() = "ClientActionPacket(action=$action, blockPosition=$blockPosition, blockFace=$blockFace)"
}

/**
 * @author Kevin Ludwig
 */
object ClientActionPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ClientActionPacket(ClientActionPacket.Action.values()[buffer.readVarInt()], buffer.readInt3UnsignedY(), buffer.readDirection())
}
