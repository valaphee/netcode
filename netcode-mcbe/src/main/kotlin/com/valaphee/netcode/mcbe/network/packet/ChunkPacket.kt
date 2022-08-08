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
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.PacketReader
import com.valaphee.netcode.mcbe.network.Restrict
import com.valaphee.netcode.mcbe.network.Restriction
import com.valaphee.netcode.mcbe.network.V1_17_041
import com.valaphee.netcode.mcbe.network.V1_18_010

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class ChunkPacket(
    val position: Int2,
    val subChunkCount: Int,
    val data: ByteArray,
    val blobIds: LongArray? = null,
    val request: Boolean = false,
) : Packet() {
    override val id get() = 0x3A

    override fun write(buffer: PacketBuffer, version: Int) {
        val (x, z) = position
        buffer.writeVarInt(x)
        buffer.writeVarInt(z)
        if (!request || version < V1_17_041) buffer.writeVarUInt(subChunkCount) else if (subChunkCount < 0 || version < V1_18_010) buffer.writeVarUInt(-1) else {
            buffer.writeVarUInt(-2)
            buffer.writeShortLE(subChunkCount)
        }
        blobIds?.let {
            buffer.writeBoolean(true)
            buffer.writeVarUInt(it.size)
            it.forEach(buffer::writeLongLE)
        } ?: buffer.writeBoolean(false)
        buffer.writeByteArray(data)
    }

    override fun handle(handler: PacketHandler) = handler.chunk(this)

    override fun toString() = "ChunkPacket(position=$position, subChunkCount=$subChunkCount, data=<omitted>, blobIds=${blobIds?.contentToString()}, request=$request)"
}

/**
 * @author Kevin Ludwig
 */
object ChunkPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): ChunkPacket {
        val position = Int2(buffer.readVarInt(), buffer.readVarInt())
        var subChunkCount = buffer.readVarUInt()
        val request = subChunkCount == -1 || subChunkCount == -2
        if (subChunkCount == -2) subChunkCount = buffer.readShortLE().toInt()
        val blobIds = if (buffer.readBoolean()) LongArray(buffer.readVarUInt()) { buffer.readLongLE() } else null
        val data = buffer.readByteArray()
        return ChunkPacket(position, subChunkCount, data, blobIds, request)
    }
}
