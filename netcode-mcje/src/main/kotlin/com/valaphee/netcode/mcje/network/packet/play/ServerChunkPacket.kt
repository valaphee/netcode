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

import com.valaphee.foundry.math.Int2
import com.valaphee.netcode.mc.util.nbt.Tag
import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.PacketReader
import com.valaphee.netcode.mcje.network.ServerPlayPacketHandler
import com.valaphee.netcode.util.safeList

/**
 * @author Kevin Ludwig
 */
class ServerChunkPacket(
    val position: Int2,
    val withSubChunks: Int,
    val heightMap: Tag?,
    val biomes: IntArray?,
    val data: ByteArray,
    val blockEntities: List<Tag?>
) : Packet<ServerPlayPacketHandler> {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeInt2(position)
        buffer.writeBoolean(biomes != null)
        buffer.writeVarInt(withSubChunks)
        buffer.toNbtOutputStream().use { it.writeTag(heightMap) }
        biomes?.let {
            buffer.writeVarInt(it.size)
            it.forEach(buffer::writeVarInt)
        }
        buffer.writeByteArray(data)
        buffer.writeVarInt(blockEntities.size)
        buffer.toNbtOutputStream().use { stream -> blockEntities.forEach(stream::writeTag) }
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.chunk(this)

    override fun toString() = "ServerChunkPacket(position=$position, withSubChunks=$withSubChunks, biomes=${biomes?.contentToString()}, data=${data.contentToString()})"
}

/**
 * @author Kevin Ludwig
 */
object ServerChunkPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): ServerChunkPacket {
        val position = buffer.readInt2()
        val withBiomes = buffer.readBoolean()
        val withSubChunks = buffer.readVarInt()
        val heightMap = buffer.toNbtInputStream().use { it.readTag() }
        val biomes = if (withBiomes) IntArray(buffer.readVarInt()) { buffer.readVarInt() } else null
        val data = buffer.readByteArray()
        val blockEntities = buffer.toNbtInputStream().use { stream -> safeList(buffer.readVarInt()) { stream.readTag() } }
        return ServerChunkPacket(position, withSubChunks, heightMap, biomes, data, blockEntities)
    }
}
