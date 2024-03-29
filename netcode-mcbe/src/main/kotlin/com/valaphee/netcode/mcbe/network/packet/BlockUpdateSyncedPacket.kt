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

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class BlockUpdateSyncedPacket(
    val position: Int3,
    val blockStateId: Int,
    val flags: Set<BlockUpdatePacket.Flag>,
    val layer: Int,
    val runtimeEntityId: Long,
    val type: Type
) : Packet() {
    enum class Type {
        None, Create, Destroy
    }

    override val id get() = 0x6E

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeBlockPosition(position)
        buffer.writeVarUInt(blockStateId)
        buffer.writeVarUIntFlags(flags)
        buffer.writeVarUInt(layer)
        buffer.writeVarULong(runtimeEntityId)
        buffer.writeVarULong(type.ordinal.toLong())
    }

    override fun handle(handler: PacketHandler) = handler.blockUpdateSynced(this)

    override fun toString() = "BlockUpdateSyncedPacket(position=$position, blockStateId=$blockStateId, flags=$flags, layer=$layer, runtimeEntityId=$runtimeEntityId, type=$type)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = BlockUpdateSyncedPacket(buffer.readBlockPosition(), buffer.readVarUInt(), buffer.readVarUIntFlags(), buffer.readVarUInt(), buffer.readVarULong(), BlockUpdateSyncedPacket.Type.values()[buffer.readVarULong().toInt()])
    }
}
