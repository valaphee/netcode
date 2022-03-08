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
import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.PacketReader
import com.valaphee.netcode.mcje.network.ServerPlayPacketHandler
import com.valaphee.netcode.util.safeList
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream
import java.io.OutputStream

/**
 * @author Kevin Ludwig
 */
class ServerChunkPacket(
    val position: Int2,
    val withSubChunks: Int,
    val heightMap: Any?,
    val biomes: IntArray?,
    val data: ByteArray,
    val blockEntities: List<Any?>
) : Packet<ServerPlayPacketHandler> {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeInt(position.x)
        buffer.writeInt(position.y)
        buffer.writeBoolean(biomes != null)
        buffer.writeVarInt(withSubChunks)
        buffer.nbtObjectMapper.writeValue(ByteBufOutputStream(buffer) as OutputStream, heightMap)
        biomes?.let {
            buffer.writeVarInt(it.size)
            it.forEach(buffer::writeVarInt)
        }
        buffer.writeByteArray(data)
        buffer.writeVarInt(blockEntities.size)
        blockEntities.forEach { buffer.nbtObjectMapper.writeValue(ByteBufOutputStream(buffer) as OutputStream, it) }
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.chunk(this)

    override fun toString() = "ServerChunkPacket(position=$position, withSubChunks=$withSubChunks, heightMap=<omitted>, biomes=<omitted>, data=<omitted>, blockEntities=$blockEntities)"
}

/**
 * @author Kevin Ludwig
 */
object ServerChunkPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): ServerChunkPacket {
        val position = Int2(buffer.readInt(), buffer.readInt())
        val withBiomes = buffer.readBoolean()
        val withSubChunks = buffer.readVarInt()
        val heightMap = buffer.nbtObjectMapper.readValue<Any?>(ByteBufInputStream(buffer))
        val biomes = if (withBiomes) IntArray(buffer.readVarInt()) { buffer.readVarInt() } else null
        val data = buffer.readByteArray()
        val blockEntities = safeList(buffer.readVarInt()) { buffer.nbtObjectMapper.readValue<Any?>(ByteBufInputStream(buffer)) }
        return ServerChunkPacket(position, withSubChunks, heightMap, biomes, data, blockEntities)
    }
}
