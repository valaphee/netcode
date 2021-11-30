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

package com.valaphee.netcode.mcje.play

import com.valaphee.foundry.math.Int2
import com.valaphee.netcode.mc.util.nbt.Tag
import com.valaphee.netcode.mcje.Packet
import com.valaphee.netcode.mcje.PacketBuffer
import com.valaphee.netcode.mcje.PacketReader
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
