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
import com.valaphee.netcode.util.safeList

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class SubChunkPacket(
    val cache: Boolean,
    val dimension: Int,
    val position: Int3,
    val responses: List<Response>
) : Packet() {
    data class Response(
        val position: Int3,
        val result: Result,
        val data: ByteArray?,
        val heightMapStatus: HeightMapStatus,
        val heightMap: ByteArray?,
        val blobId: Long
    ) {
        enum class Result {
            Undefined, Success, NotFound, InvalidDimension, PlayerNotFound, OutOfBounds, SuccessEmpty
        }

        enum class HeightMapStatus {
            Unavailable, Available, TooHigh, TooLow
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Response

            if (position != other.position) return false
            if (result != other.result) return false
            if (!data.contentEquals(other.data)) return false
            if (heightMapStatus != other.heightMapStatus) return false
            if (heightMap != null) {
                if (other.heightMap == null) return false
                if (!heightMap.contentEquals(other.heightMap)) return false
            } else if (other.heightMap != null) return false
            if (blobId != other.blobId) return false

            return true
        }

        override fun hashCode(): Int {
            var result1 = position.hashCode()
            result1 = 31 * result1 + result.hashCode()
            result1 = 31 * result1 + data.contentHashCode()
            result1 = 31 * result1 + heightMapStatus.hashCode()
            result1 = 31 * result1 + (heightMap?.contentHashCode() ?: 0)
            result1 = 31 * result1 + blobId.hashCode()
            return result1
        }

        override fun toString() = "Response(position=$position, result=$result, data=<omitted>, heightMapStatus=$heightMapStatus, heightMap=<omitted>, blobId=$blobId)"
    }

    override val id get() = 0xAE

    override fun write(buffer: PacketBuffer, version: Int) {
        if (version >= 486) {
            buffer.writeBoolean(cache)
            buffer.writeVarInt(dimension)
            buffer.writeInt3(position)
            buffer.writeIntLE(responses.size)
            responses.forEach {
                buffer.writeByte(it.position.x)
                buffer.writeByte(it.position.y)
                buffer.writeByte(it.position.z)
                buffer.writeByte(it.result.ordinal)
                if (it.result != Response.Result.SuccessEmpty || !cache) buffer.writeByteArray(it.data!!)
                buffer.writeByte(it.heightMapStatus.ordinal)
                if (it.heightMapStatus == Response.HeightMapStatus.Available) buffer.writeBytes(it.heightMap!!)
                if (cache) buffer.writeLongLE(it.blobId)
            }
        } else {
            buffer.writeVarInt(dimension)
            val it = responses.first()
            buffer.writeInt3(it.position)
            buffer.writeByteArray(it.data!!)
            buffer.writeVarInt(it.result.ordinal)
            buffer.writeByte(it.heightMapStatus.ordinal)
            if (it.heightMapStatus == Response.HeightMapStatus.Available || version < 475) buffer.writeBytes(it.heightMap!!)
            if (version >= 475) if (cache) {
                buffer.writeBoolean(true)
                buffer.writeLongLE(it.blobId)
            } else buffer.writeBoolean(false)
        }
    }

    override fun handle(handler: PacketHandler) = handler.subChunk(this)

    override fun toString() = "SubChunkPacket(cache=$cache, dimension=$dimension, position=$position, responses=$responses)"
}

/**
 * @author Kevin Ludwig
 */
object SubChunkPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = if (version >= 486) {
        val cache = buffer.readBoolean()
        val dimension = buffer.readVarInt()
        val position = buffer.readInt3()
        val responses = safeList(buffer.readIntLE()) {
            val position = Int3(buffer.readByte().toInt(), buffer.readByte().toInt(), buffer.readByte().toInt())
            val result = SubChunkPacket.Response.Result.values()[buffer.readVarInt()]
            val data = if (result != SubChunkPacket.Response.Result.SuccessEmpty || !cache) buffer.readByteArray() else null
            val heightMapStatus = SubChunkPacket.Response.HeightMapStatus.values()[buffer.readByte().toInt()]
            val heightMap = if (heightMapStatus == SubChunkPacket.Response.HeightMapStatus.Available) ByteArray(256).also { buffer.readBytes(it) } else null
            val blobId = if (cache) buffer.readLongLE() else 0L
            SubChunkPacket.Response(position, result, data, heightMapStatus, heightMap, blobId)
        }
        SubChunkPacket(cache, dimension, position, responses)
    } else {
        val dimension = buffer.readVarInt()
        val position = buffer.readInt3()
        val data = buffer.readByteArray()
        val result = SubChunkPacket.Response.Result.values()[buffer.readVarInt()]
        val heightMapStatus = SubChunkPacket.Response.HeightMapStatus.values()[buffer.readByte().toInt()]
        val heightMap = if (heightMapStatus == SubChunkPacket.Response.HeightMapStatus.Available || version < 475) ByteArray(256).also { buffer.readBytes(it) } else null
        val cache: Boolean
        val blobId: Long
        if (version >= 475) {
            cache = buffer.readBoolean()
            blobId = if (cache) buffer.readLongLE() else 0L
        } else {
            cache = false
            blobId = 0L
        }
        SubChunkPacket(cache, dimension, position, listOf(SubChunkPacket.Response(position, result, data, heightMapStatus, heightMap, blobId)))
    }
}
