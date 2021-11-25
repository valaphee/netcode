/*
 * MIT License
 *
 * Copyright (c) 2021, Valaphee.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
