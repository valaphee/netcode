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
import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.PacketReader
import com.valaphee.netcode.mcje.network.ServerPlayPacketHandler
import com.valaphee.netcode.mcje.network.V1_18_2
import com.valaphee.netcode.mcje.util.NibbleArray
import com.valaphee.netcode.mcje.world.chunk.BlockStorage
import com.valaphee.netcode.mcje.world.chunk.SubChunk
import java.util.BitSet

/**
 * @author Kevin Ludwig
 */
class ServerChunkLightPacket(
    val position: Int2,
    val trustEdges: Boolean,
    val withSkyLight: BitSet,
    val withBlockLight: BitSet,
    val emptySkyLight: BitSet,
    val emptyBlockLight: BitSet,
    val skyLight: Array<NibbleArray?>,
    val blockLight: Array<NibbleArray?>
) : Packet<ServerPlayPacketHandler>() {
    data class BlockEntity(
        val position: Int3,
        val type: Int,
        val data: Any?
    )

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(position.x)
        buffer.writeVarInt(position.y)
        buffer.writeBoolean(trustEdges)
        if (version >= V1_18_2) {
            val withSkyLight = withSkyLight.toLongArray()
            buffer.writeVarInt(withSkyLight.size)
            withSkyLight.forEach { buffer.writeLong(it) }
            val withBlockLight = withBlockLight.toLongArray()
            buffer.writeVarInt(withBlockLight.size)
            withBlockLight.forEach { buffer.writeLong(it) }
            val emptySkyLight = emptySkyLight.toLongArray()
            buffer.writeVarInt(emptySkyLight.size)
            emptySkyLight.forEach { buffer.writeLong(it) }
            val emptyBlockLight = emptyBlockLight.toLongArray()
            buffer.writeVarInt(emptyBlockLight.size)
            emptyBlockLight.forEach { buffer.writeLong(it) }
            buffer.writeVarInt(skyLight.size)
            skyLight.forEach { buffer.writeByteArray(it!!.bytes) }
            buffer.writeVarInt(blockLight.size)
            blockLight.forEach { buffer.writeByteArray(it!!.bytes) }
        } else {
            buffer.writeVarInt(withSkyLight.toLongArray().single().toInt())
            buffer.writeVarInt(withBlockLight.toLongArray().single().toInt())
            buffer.writeVarInt(emptySkyLight.toLongArray().single().toInt())
            buffer.writeVarInt(emptyBlockLight.toLongArray().single().toInt())
            skyLight.forEach { buffer.writeByteArray(it!!.bytes) }
            blockLight.forEach { buffer.writeByteArray(it!!.bytes) }
        }
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.chunkLight(this)

    override fun toString() = "ServerChunkLightPacket(position=$position, trustEdges=$trustEdges, withSkyLight=$withSkyLight, withBlockLight=$withBlockLight, emptySkyLight=$emptySkyLight, emptyBlockLight=$emptyBlockLight, skyLight=$skyLight, blockLight=$blockLight)"

    companion object {
        val EmptyLight = NibbleArray(BlockStorage.XZSize * SubChunk.YSize * BlockStorage.XZSize)
    }
}

/**
 * @author Kevin Ludwig
 */
object ServerChunkLightPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): ServerChunkLightPacket {
        val position = Int2(buffer.readInt(), buffer.readInt())
        val trustEdges = buffer.readBoolean()
        val withSkyLight: BitSet?
        val withBlockLight: BitSet?
        val emptySkyLight: BitSet?
        val emptyBlockLight: BitSet?
        val skyLight: Array<NibbleArray?>?
        val blockLight: Array<NibbleArray?>?
        if (version >= V1_18_2) {
            withSkyLight = BitSet.valueOf(LongArray(buffer.readVarInt()) { buffer.readLong() })
            withBlockLight = BitSet.valueOf(LongArray(buffer.readVarInt()) { buffer.readLong() })
            emptySkyLight = BitSet.valueOf(LongArray(buffer.readVarInt()) { buffer.readLong() })
            emptyBlockLight = BitSet.valueOf(LongArray(buffer.readVarInt()) { buffer.readLong() })
            skyLight = Array(buffer.readVarInt()) { NibbleArray(buffer.readByteArray((BlockStorage.XZSize * SubChunk.YSize * BlockStorage.XZSize) / 2)) }
            blockLight = Array(buffer.readVarInt()) { NibbleArray(buffer.readByteArray((BlockStorage.XZSize * SubChunk.YSize * BlockStorage.XZSize) / 2)) }
        } else {
            withSkyLight = BitSet.valueOf(longArrayOf(buffer.readVarInt().toUInt().toLong()))
            withBlockLight = BitSet.valueOf(longArrayOf(buffer.readVarInt().toUInt().toLong()))
            emptySkyLight = BitSet.valueOf(longArrayOf(buffer.readVarInt().toUInt().toLong()))
            emptyBlockLight = BitSet.valueOf(longArrayOf(buffer.readVarInt().toUInt().toLong()))
            skyLight = Array(18) {
                when {
                    withSkyLight[it] -> NibbleArray(buffer.readByteArray((BlockStorage.XZSize * SubChunk.YSize * BlockStorage.XZSize) / 2))
                    emptySkyLight[it] -> ServerChunkLightPacket.EmptyLight
                    else -> null
                }
            }
            blockLight = Array(18) {
                when {
                    withBlockLight[it] -> NibbleArray(buffer.readByteArray((BlockStorage.XZSize * SubChunk.YSize * BlockStorage.XZSize) / 2))
                    emptySkyLight[it] -> ServerChunkLightPacket.EmptyLight
                    else -> null
                }
            }
        }
        return ServerChunkLightPacket(position, trustEdges, withSkyLight, withBlockLight, emptySkyLight, emptyBlockLight, skyLight, blockLight)
    }
}
