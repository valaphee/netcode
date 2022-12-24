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
import com.valaphee.netcode.mcje.network.ClientPlayPacketHandler
import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.V1_19_0
import com.valaphee.netcode.mcje.util.Direction

/**
 * @author Kevin Ludwig
 */
class ClientActionPacket(
    val action: Action,
    val blockPosition: Int3,
    val blockFace: Direction,
    val confirmId: Int
) : Packet<ClientPlayPacketHandler>() {
    enum class Action {
        StartBreak, AbortBreak, StopBreak, DropStack, DropStack1, ReleaseItem, SwapHands
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(action.ordinal)
        buffer.writeBlockPosition(blockPosition)
        buffer.writeDirection(blockFace)
        if (version >= V1_19_0) buffer.writeVarInt(confirmId)
    }

    override fun handle(handler: ClientPlayPacketHandler) = handler.action(this)

    override fun toString() = "ClientActionPacket(action=$action, blockPosition=$blockPosition, blockFace=$blockFace, confirmId=$confirmId)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = ClientActionPacket(Action.values()[buffer.readVarInt()], buffer.readBlockPosition(), buffer.readDirection(), if (version >= V1_19_0) buffer.readVarInt() else 0)
    }
}
