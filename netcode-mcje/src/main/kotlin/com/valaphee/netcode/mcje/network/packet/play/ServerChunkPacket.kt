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

package com.valaphee.netcode.mcje.network.packet.play

import com.fasterxml.jackson.module.kotlin.readValue
import com.valaphee.foundry.math.Int2
import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.PacketReader
import com.valaphee.netcode.mcje.network.ServerPlayPacketHandler
import com.valaphee.netcode.mcje.network.V1_18_2
import com.valaphee.netcode.mcje.util.NibbleArray
import com.valaphee.netcode.util.safeList
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream
import java.io.OutputStream
import java.util.BitSet

/**
 * @author Kevin Ludwig
 */
class ServerChunkPacket(
    val position: Int2,
    val withSubChunks: Int?,
    val heightMap: Any?,
    val biomes: IntArray?,
    val data: ByteArray,
    val blockEntities: List<BlockEntity>,
    val trustEdges: Boolean,
    val withSkyLight: BitSet?,
    val withBlockLight: BitSet?,
    val emptySkyLight: BitSet?,
    val emptyBlockLight: BitSet?,
    val skyLight: Array<NibbleArray>?,
    val blockLight: Array<NibbleArray>?
) : Packet<ServerPlayPacketHandler>() {
    data class BlockEntity(
        val position: Int3,
        val type: Int,
        val data: Any?
    )

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeInt(position.x)
        buffer.writeInt(position.y)
        if (version >= V1_18_2) {
            buffer.nbtObjectMapper.writeValue(ByteBufOutputStream(buffer) as OutputStream, heightMap)
            buffer.writeByteArray(data)
            buffer.writeVarInt(blockEntities.size)
            blockEntities.forEach {
                val (x, y, z) = it.position
                buffer.writeByte(((x and 0xF) shl 4) or (z and 0xF))
                buffer.writeShort(y)
                buffer.writeVarInt(it.type)
                blockEntities.forEach { buffer.nbtObjectMapper.writeValue(ByteBufOutputStream(buffer) as OutputStream, it.data) }
            }
            buffer.writeBoolean(trustEdges)
            val withSkyLight = withSkyLight!!.toLongArray()
            buffer.writeVarInt(withSkyLight.size)
            withSkyLight.forEach { buffer.writeLong(it) }
            val withBlockLight = withBlockLight!!.toLongArray()
            buffer.writeVarInt(withBlockLight.size)
            withBlockLight.forEach { buffer.writeLong(it) }
            val emptySkyLight = emptySkyLight!!.toLongArray()
            buffer.writeVarInt(emptySkyLight.size)
            emptySkyLight.forEach { buffer.writeLong(it) }
            val emptyBlockLight = emptyBlockLight!!.toLongArray()
            buffer.writeVarInt(emptyBlockLight.size)
            emptyBlockLight.forEach { buffer.writeLong(it) }
            skyLight!!.let {
                buffer.writeVarInt(it.size)
                it.forEach { buffer.writeByteArray(it.bytes) }
            }
            blockLight!!.let {
                buffer.writeVarInt(it.size)
                it.forEach { buffer.writeByteArray(it.bytes) }
            }
        } else {
            buffer.writeBoolean(biomes != null)
            buffer.writeVarInt(withSubChunks!!)
            buffer.nbtObjectMapper.writeValue(ByteBufOutputStream(buffer) as OutputStream, heightMap)
            biomes?.let {
                buffer.writeVarInt(it.size)
                it.forEach(buffer::writeVarInt)
            }
            buffer.writeByteArray(data)
            buffer.writeVarInt(blockEntities.size)
            blockEntities.forEach { buffer.nbtObjectMapper.writeValue(ByteBufOutputStream(buffer) as OutputStream, it.data) }
        }
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.chunk(this)

    override fun toString() = "ServerChunkPacket(position=$position, withSubChunks=$withSubChunks, heightMap=<omitted>, biomes=<omitted>, data=<omitted>, blockEntities=$blockEntities, trustEdges=$trustEdges, withSkyLight=$withSkyLight, withBlockLight=$withBlockLight, emptySkyLight=$emptySkyLight, emptyBlockLight=$emptyBlockLight, skyLight=$skyLight, blockLight=$blockLight)"
}

/**
 * @author Kevin Ludwig
 */
object ServerChunkPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): ServerChunkPacket {
        val position = Int2(buffer.readInt(), buffer.readInt())
        val withSubChunks: Int?
        val heightMap: Any?
        val biomes: IntArray?
        val data: ByteArray
        val blockEntities: List<ServerChunkPacket.BlockEntity>
        val trustEdges: Boolean
        val withSkyLight: BitSet?
        val withBlockLight: BitSet?
        val emptySkyLight: BitSet?
        val emptyBlockLight: BitSet?
        val skyLight: Array<NibbleArray>?
        val blockLight: Array<NibbleArray>?
        if (version >= V1_18_2) {
            withSubChunks = null
            heightMap = buffer.nbtObjectMapper.readValue(ByteBufInputStream(buffer))
            biomes = null
            data = buffer.readByteArray()
            blockEntities = safeList(buffer.readVarInt()) {
                val xz = buffer.readByte().toInt()
                ServerChunkPacket.BlockEntity(Int3((xz shr 4) and 0xF, buffer.readShort().toInt(), xz and 0xF), buffer.readVarInt(), buffer.nbtObjectMapper.readValue(ByteBufInputStream(buffer)))
            }
            trustEdges = buffer.readBoolean()
            withSkyLight = BitSet.valueOf(LongArray(buffer.readVarInt()) { buffer.readLong() })
            withBlockLight = BitSet.valueOf(LongArray(buffer.readVarInt()) { buffer.readLong() })
            emptySkyLight = BitSet.valueOf(LongArray(buffer.readVarInt()) { buffer.readLong() })
            emptyBlockLight = BitSet.valueOf(LongArray(buffer.readVarInt()) { buffer.readLong() })
            skyLight = Array(buffer.readVarInt()) { NibbleArray(buffer.readByteArray()) }
            blockLight = Array(buffer.readVarInt()) { NibbleArray(buffer.readByteArray()) }
        } else {
            val withBiomes = buffer.readBoolean()
            withSubChunks = buffer.readVarInt()
            heightMap = buffer.nbtObjectMapper.readValue(ByteBufInputStream(buffer))
            biomes = if (withBiomes) IntArray(buffer.readVarInt()) { buffer.readVarInt() } else null
            data = buffer.readByteArray()
            blockEntities = safeList(buffer.readVarInt()) { ServerChunkPacket.BlockEntity(Int3.Zero, 0, buffer.nbtObjectMapper.readValue(ByteBufInputStream(buffer))) }
            trustEdges = false
            withSkyLight = null
            withBlockLight = null
            emptySkyLight = null
            emptyBlockLight = null
            skyLight = null
            blockLight = null
        }
        return ServerChunkPacket(position, withSubChunks, heightMap, biomes, data, blockEntities, trustEdges, withSkyLight, withBlockLight, emptySkyLight, emptyBlockLight, skyLight, blockLight)
    }
}
