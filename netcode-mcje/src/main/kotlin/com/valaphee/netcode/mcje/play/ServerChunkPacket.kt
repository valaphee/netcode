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

import com.valaphee.netcode.mc.util.nbt.CompoundTag
import com.valaphee.netcode.mc.util.nbt.NbtInputStream
import com.valaphee.netcode.mc.util.nbt.NbtOutputStream
import com.valaphee.netcode.mcje.ServerPlayPacketHandler
import com.valaphee.netcode.mcje.Packet
import com.valaphee.netcode.mcje.PacketBuffer
import net.griefergames.breakdown.util.ByteBufInputStream
import net.griefergames.breakdown.util.ByteBufOutputStream
import net.griefergames.breakdown.world.chunk.Chunk
import net.griefergames.breakdown.world.chunk.storage.readSubChunk

/**
 * @author Kevin Ludwig
 */
class ServerChunkPacket(
    val chunk: Chunk? = null
) : Packet<ServerPlayPacketHandler> {
    override fun read(buffer: PacketBuffer, version: Int) {
        chunk = Chunk(buffer.readInt(), buffer.readInt()).also {
            val withBiomes = if (version >= Int.MAX_VALUE) true else buffer.readBoolean()
            val withSubChunks = buffer.readVarInt()
            it.heightMaps = NbtInputStream(ByteBufInputStream(buffer)).use { it.readTag() }?.asCompoundTag()!!
            if (version < 578) buffer.readVarInt()
            if (withBiomes) it.biomes = if (version >= 754) IntArray(buffer.readVarInt()) { buffer.readVarInt() } else IntArray(1024) { buffer.readInt() }
            if (version >= 578) buffer.readVarInt()
            val maximumSubChunks = Integer.highestOneBit(withSubChunks)
            it.subChunks = Array(if (maximumSubChunks == 0) 0 else Integer.numberOfLeadingZeros(maximumSubChunks)) { if (withSubChunks and (1 shl it) != 0) buffer.readSubChunk() else null }
            val blockEntities: MutableList<CompoundTag> = ArrayList()
            NbtInputStream(ByteBufInputStream(buffer)).use { repeat(buffer.readVarInt()) { _ -> blockEntities.add(it.readTag()?.asCompoundTag()!!) } }
            it.blockEntities = blockEntities
        }
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        chunk!!.let {
            buffer.writeInt(it.x)
            buffer.writeInt(it.z)
            buffer.writeBoolean(true)
            buffer.writeVarInt(0)
            NbtOutputStream(ByteBufOutputStream(buffer)).use { stream -> stream.writeTag(it.heightMaps) }
            var dataLengthIndex = 0
            if (version < 578) {
                dataLengthIndex = buffer.writerIndex()
                buffer.writeZero(PacketBuffer.MaximumVarIntLength)
            }
            if (version >= 754) {
                buffer.writeVarInt(it.biomes!!.size)
                it.biomes!!.forEach { buffer.writeVarInt(it) }
            } else it.biomes!!.forEach { buffer.writeInt(it) }
            if (version >= 578) {
                dataLengthIndex = buffer.writerIndex()
                buffer.writeZero(PacketBuffer.MaximumVarIntLength)
            }
            NbtOutputStream(ByteBufOutputStream(buffer)).use { stream -> it.blockEntities.forEach { stream.writeTag(it) } }
            buffer.setMaximumLengthVarInt(dataLengthIndex, buffer.writerIndex() - (dataLengthIndex + PacketBuffer.MaximumVarIntLength))
        }
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.chunk(this)
}
