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
import com.valaphee.netcode.mcbe.network.Restrict
import com.valaphee.netcode.mcbe.network.Restriction
import com.valaphee.netcode.util.LazyList

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
        val blockStateId: Int,
        val flags: Set<BlockUpdatePacket.Flag>,
        val runtimeEntityId: Long,
        val type: BlockUpdateSyncedPacket.Type
    )

    override val id get() = 0xAC

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeBlockPosition(basePosition)
        buffer.writeVarUInt(updates1.size)
        updates1.forEach {
            buffer.writeBlockPosition(it.position)
            buffer.writeVarUInt(it.blockStateId)
            buffer.writeVarUIntFlags(it.flags)
            buffer.writeVarULong(it.runtimeEntityId)
            buffer.writeVarULong(it.type.ordinal.toLong())
        }
        buffer.writeVarUInt(updates2.size)
        updates2.forEach {
            buffer.writeBlockPosition(it.position)
            buffer.writeVarUInt(it.blockStateId)
            buffer.writeVarUIntFlags(it.flags)
            buffer.writeVarULong(it.runtimeEntityId)
            buffer.writeVarULong(it.type.ordinal.toLong())
        }
    }

    override fun handle(handler: PacketHandler) = handler.blockUpdatesSynced(this)

    override fun toString() = "BlockUpdatesSyncedPacket(basePosition=$basePosition, updates1=$updates1, updates2=$updates2)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = BlockUpdatesSyncedPacket(
            buffer.readBlockPosition(),
            LazyList(buffer.readVarUInt()) { Update(buffer.readBlockPosition(), buffer.readVarUInt(), buffer.readVarUIntFlags(), buffer.readVarULong(), BlockUpdateSyncedPacket.Type.values()[buffer.readVarULong().toInt()]) },
            LazyList(buffer.readVarUInt()) { Update(buffer.readBlockPosition(), buffer.readVarUInt(), buffer.readVarUIntFlags(), buffer.readVarULong(), BlockUpdateSyncedPacket.Type.values()[buffer.readVarULong().toInt()]) }
        )
    }
}
