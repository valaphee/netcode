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
import com.valaphee.netcode.mcbe.network.V1_16_010

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class SpawnPositionPacket(
    val type: Type,
    val blockPosition: Int3,
    val dimensionId: Int,
    val position: Int3,
    val forced: Boolean
) : Packet() {
    enum class Type {
        PlayerSpawn, WorldSpawn
    }

    override val id get() = 0x2B

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(type.ordinal)
        if (version >= V1_16_010) {
            buffer.writeInt3UnsignedY(blockPosition)
            buffer.writeVarUInt(dimensionId)
        }
        buffer.writeInt3UnsignedY(position)
        if (version < V1_16_010) buffer.writeBoolean(forced)
    }

    override fun handle(handler: PacketHandler) = handler.spawnPosition(this)

    override fun toString() = "SpawnPositionPacket(type=$type, blockPosition=$blockPosition, dimensionId=$dimensionId, position=$position, forced=$forced)"
}

/**
 * @author Kevin Ludwig
 */
object SpawnPositionPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): SpawnPositionPacket {
        val type = SpawnPositionPacket.Type.values()[buffer.readVarInt()]
        val blockPosition: Int3
        val dimensionId: Int
        if (version >= V1_16_010) {
            blockPosition = buffer.readInt3UnsignedY()
            dimensionId = buffer.readVarUInt()
        } else {
            blockPosition = Int3.Zero
            dimensionId = 0
        }
        val position = buffer.readInt3UnsignedY()
        val forced = if (version < V1_16_010) buffer.readBoolean() else false
        return SpawnPositionPacket(type, blockPosition, dimensionId, position, forced)
    }
}
