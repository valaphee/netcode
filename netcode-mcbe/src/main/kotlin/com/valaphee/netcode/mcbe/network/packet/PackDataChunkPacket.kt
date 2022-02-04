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

import com.valaphee.netcode.mcbe.Restrict
import com.valaphee.netcode.mcbe.Restriction
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.PacketReader
import io.netty.buffer.ByteBuf
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class PackDataChunkPacket(
    val packId: UUID,
    val packVersion: String?,
    val chunkIndex: Long,
    val progress: Long,
    val data: ByteBuf
) : Packet() {
    override val id get() = 0x53

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeString("$packId${packVersion?.let { "_$it" } ?: ""}")
        buffer.writeIntLE(chunkIndex.toInt())
        buffer.writeLongLE(progress)
        buffer.writeVarUInt(data.readableBytes())
        buffer.writeBytes(data)
    }

    override fun handle(handler: PacketHandler) = handler.packDataChunk(this)

    override fun toString() = "PackDataChunkPacket(packId=$packId, packVersion=$packVersion, chunkIndex=$chunkIndex, progress=$progress, data=$data)"
}

/**
 * @author Kevin Ludwig
 */
object PackDataChunkPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): PackDataChunkPacket {
        val pack = buffer.readString().split("_".toRegex(), 2).toTypedArray()
        val packId = UUID.fromString(pack[0])
        val packVersion = if (pack.size == 2) pack[1] else null
        val chunkIndex = buffer.readUnsignedIntLE()
        val progress = buffer.readLongLE()
        val data = buffer.readRetainedSlice(buffer.readVarUInt())
        return PackDataChunkPacket(packId, packVersion, chunkIndex, progress, data)
    }
}
