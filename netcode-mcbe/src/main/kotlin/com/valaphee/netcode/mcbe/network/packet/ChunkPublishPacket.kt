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

import com.valaphee.foundry.math.Int2
import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.Restrict
import com.valaphee.netcode.mcbe.network.Restriction
import com.valaphee.netcode.mcbe.network.V1_19_020
import com.valaphee.netcode.util.LazyList

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class ChunkPublishPacket(
    val position: Int3,
    val radius: Int,
    val savedChunks: List<Int2>
) : Packet() {
    override val id get() = 0x79

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeInt3(position)
        buffer.writeVarUInt(radius)
        if (version >= V1_19_020) {
            buffer.writeIntLE(savedChunks.size)
            savedChunks.forEach {
                buffer.writeVarInt(it.x)
                buffer.writeVarInt(it.y)
            }
        }
    }

    override fun handle(handler: PacketHandler) = handler.chunkPublish(this)

    override fun toString() = "ChunkPublishPacket(position=$position, radius=$radius, savedChunks=$savedChunks)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = ChunkPublishPacket(buffer.readInt3(), buffer.readVarUInt(), if (version >= V1_19_020) LazyList(buffer.readIntLE()) { Int2(buffer.readVarInt(), buffer.readVarInt()) } else emptyList())
    }
}
