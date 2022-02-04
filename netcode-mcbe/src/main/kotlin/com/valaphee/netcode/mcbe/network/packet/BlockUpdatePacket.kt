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

package com.valaphee.netcode.mcbe.network.packet

import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.PacketReader
import com.valaphee.netcode.mcbe.network.Restrict
import com.valaphee.netcode.mcbe.network.Restriction
import java.util.EnumSet

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class BlockUpdatePacket(
    val position: Int3,
    val runtimeId: Int,
    val flags: Set<Flag>,
    val layer: Int
) : Packet() {
    enum class Flag {
        Neighbors, Network, NonVisual, Priority;

        companion object {
            val All: EnumSet<Flag> = EnumSet.of(Neighbors, Network)
            val AllPriority: EnumSet<Flag> = EnumSet.of(Neighbors, Network, Priority)
        }
    }

    override val id get() = 0x15

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeInt3UnsignedY(position)
        buffer.writeVarUInt(runtimeId)
        buffer.writeVarUIntFlags(flags)
        buffer.writeVarUInt(layer)
    }

    override fun handle(handler: PacketHandler) = handler.blockUpdate(this)

    override fun toString() = "BlockUpdatePacket(position=$position, runtimeId=$runtimeId, flags=$flags, layer=$layer)"
}

/**
 * @author Kevin Ludwig
 */
object BlockUpdatePacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = BlockUpdatePacket(buffer.readInt3UnsignedY(), buffer.readVarUInt(), buffer.readVarUIntFlags(), buffer.readVarUInt())
}
