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
import com.valaphee.netcode.mcbe.Restrict
import com.valaphee.netcode.mcbe.Restriction
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.PacketReader
import com.valaphee.netcode.util.safeList

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class BlockUpdatesSyncedPacket(
    val basePosition: Int3,
    val updates1: List<Update>,
    val updates2: List<Update>
) : Packet() {
    data class Update(
        val position: Int3,
        val runtimeId: Int,
        val flags: Set<BlockUpdatePacket.Flag>,
        val runtimeEntityId: Long,
        val type: BlockUpdateSyncedPacket.Type
    ) {
        constructor(position: Int3, runtimeId: Int, flags: Set<BlockUpdatePacket.Flag>) : this(position, runtimeId, flags, -1, BlockUpdateSyncedPacket.Type.None)
    }

    override val id get() = 0xAC

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeInt3UnsignedY(basePosition)
        buffer.writeVarUInt(updates1.size)
        updates1.forEach {
            buffer.writeInt3UnsignedY(it.position)
            buffer.writeVarUInt(it.runtimeId)
            buffer.writeVarUIntFlags(it.flags)
            buffer.writeVarULong(it.runtimeEntityId)
            buffer.writeVarULong(it.type.ordinal.toLong())
        }
        buffer.writeVarUInt(updates2.size)
        updates2.forEach {
            buffer.writeInt3UnsignedY(it.position)
            buffer.writeVarUInt(it.runtimeId)
            buffer.writeVarUIntFlags(it.flags)
            buffer.writeVarULong(it.runtimeEntityId)
            buffer.writeVarULong(it.type.ordinal.toLong())
        }
    }

    override fun handle(handler: PacketHandler) = handler.blockUpdatesSynced(this)

    override fun toString() = "BlockUpdatesSyncedPacket(basePosition=$basePosition, updates1=$updates1, updates2=$updates2)"
}

/**
 * @author Kevin Ludwig
 */
object BlockUpdatesSyncedPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = BlockUpdatesSyncedPacket(
        buffer.readInt3UnsignedY(),
        safeList(buffer.readVarUInt()) { BlockUpdatesSyncedPacket.Update(buffer.readInt3UnsignedY(), buffer.readVarUInt(), buffer.readVarUIntFlags(), buffer.readVarULong(), BlockUpdateSyncedPacket.Type.values()[buffer.readVarULong().toInt()]) },
        safeList(buffer.readVarUInt()) { BlockUpdatesSyncedPacket.Update(buffer.readInt3UnsignedY(), buffer.readVarUInt(), buffer.readVarUIntFlags(), buffer.readVarULong(), BlockUpdateSyncedPacket.Type.values()[buffer.readVarULong().toInt()]) }
    )
}
