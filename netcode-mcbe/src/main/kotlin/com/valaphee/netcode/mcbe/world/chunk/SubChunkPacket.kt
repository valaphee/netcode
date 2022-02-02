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

package com.valaphee.netcode.mcbe.world.chunk

import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mcbe.Packet
import com.valaphee.netcode.mcbe.PacketBuffer
import com.valaphee.netcode.mcbe.PacketHandler
import com.valaphee.netcode.mcbe.PacketReader
import com.valaphee.netcode.mcbe.Restrict
import com.valaphee.netcode.mcbe.Restriction

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class SubChunkPacket(
    val dimension: Int,
    val position: Int3,
    val data: ByteArray,
    val result: Result,
    val heightMapStatus: HeightMapStatus,
    val heightMap: ByteArray?,
    val blobId: Long? = null,
) : Packet() {
    enum class Result {
        Undefined, Success, NotFound, InvalidDimension, PlayerNotFound, OutOfBounds
    }

    enum class HeightMapStatus {
        Unavailable, Available, TooHigh, TooLow
    }

    override val id get() = 0xAE

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(dimension)
        buffer.writeInt3(position)
        buffer.writeByteArray(data)
        buffer.writeVarInt(result.ordinal)
        buffer.writeByte(heightMapStatus.ordinal)
        if (heightMapStatus == HeightMapStatus.Available) buffer.writeBytes(heightMap!!)
        blobId?.let {
            buffer.writeBoolean(true)
            buffer.writeLongLE(it)
        } ?: buffer.writeBoolean(false)
    }

    override fun handle(handler: PacketHandler) = handler.subChunk(this)

    override fun toString() = "SubChunkPacket(dimension=$dimension, position=$position, result=$result, heightMapStatus=$heightMapStatus, blobId=$blobId)"
}

/**
 * @author Kevin Ludwig
 */
object SubChunkPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): SubChunkPacket {
        val dimension = buffer.readVarInt()
        val position = buffer.readInt3()
        val data = buffer.readByteArray()
        val result = SubChunkPacket.Result.values()[buffer.readVarInt()]
        val heightMapStatus = SubChunkPacket.HeightMapStatus.values()[buffer.readByte().toInt()]
        val heightMap = if (heightMapStatus == SubChunkPacket.HeightMapStatus.Available) buffer.readByteArray() else null
        val blobId = if (buffer.readBoolean()) buffer.readLongLE() else null
        return SubChunkPacket(dimension, position, data, result, heightMapStatus, heightMap, blobId)
    }
}
