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

import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.util.NibbleArray

/**
 * @author Kevin Ludwig
 */
sealed class SubChunk {
    abstract val empty: Boolean

    abstract operator fun get(x: Int, y: Int, z: Int): Int

    abstract operator fun get(x: Int, y: Int, z: Int, layer: Int): Int

    abstract operator fun set(x: Int, y: Int, z: Int, value: Int)

    abstract operator fun set(x: Int, y: Int, z: Int, value: Int, layer: Int)

    abstract fun writeToBuffer(buffer: PacketBuffer, runtime: Boolean)

    companion object {
        const val YSize = 16
        const val XShift = 8
        const val ZShift = 4
    }
}

/**
 * @author Kevin Ludwig
 */
class LegacySubChunk(
    var blockIds: ByteArray = ByteArray(BlockStorage.XZSize * SubChunk.YSize * BlockStorage.XZSize),
    var blockSubIds: NibbleArray = NibbleArray(BlockStorage.XZSize * SubChunk.YSize * BlockStorage.XZSize)
) : SubChunk() {
    override fun get(x: Int, y: Int, z: Int): Int {
        val index = (x shl SubChunk.XShift) or (z shl SubChunk.ZShift) or y
        return (blockIds[index].toInt() and (blockIdMask shl blockIdShift)) or blockSubIds[index]
    }

    override fun get(x: Int, y: Int, z: Int, layer: Int) = get(x, y, z)

    override fun set(x: Int, y: Int, z: Int, value: Int) {
        val index = (x shl SubChunk.XShift) or (z shl SubChunk.ZShift) or y
        blockIds[index] = ((value shr blockIdShift) and blockIdMask).toByte()
        blockSubIds[index] = (value and blockSubIdMask)
    }

    override fun set(x: Int, y: Int, z: Int, layer: Int, value: Int) = set(x, y, z, value)

    override val empty get() = blockIds.all { it.toInt() == 0 }

    override fun writeToBuffer(buffer: PacketBuffer, runtime: Boolean) {
        buffer.writeByte(0)
        buffer.writeBytes(blockIds)
        buffer.writeBytes(blockSubIds.bytes)
    }

    companion object {
        private const val blockIdMask = 255
        private const val blockIdShift = 4
        private const val blockSubIdMask = 15
    }
}

/**
 * @author Kevin Ludwig
 */
class CompactSubChunk(
    val layers: List<Layer>,
    val index: Int? = null
) : SubChunk() {
    constructor(default: Int, version: BitArray.Version) : this(listOf(Layer(default, version), Layer(default, version)))

    override fun get(x: Int, y: Int, z: Int) = get(x, y, z, 0)

    override fun get(x: Int, y: Int, z: Int, layer: Int) = layers[layer][(x shl XShift) or (z shl ZShift) or y]

    override fun set(x: Int, y: Int, z: Int, value: Int) = set(x, y, z, value, 0)

    override fun set(x: Int, y: Int, z: Int, value: Int, layer: Int) {
        layers[layer][(x shl XShift) or (z shl ZShift) or y] = value
    }

    override val empty get() = layers.all { it.empty }

    override fun writeToBuffer(buffer: PacketBuffer, runtime: Boolean) {
        if (index != null) {
            buffer.writeByte(9)
            buffer.writeByte(layers.size)
            buffer.writeByte(index)
        } else if (layers.size == 1) buffer.writeByte(1) else {
            buffer.writeByte(8)
            buffer.writeByte(layers.size)
        }
        layers.forEach { it.writeToBuffer(buffer, runtime) }
    }
}

fun PacketBuffer.readSubChunk() = when (val version = readUnsignedByte().toInt()) {
    0, 2, 3, 4, 5, 6 -> LegacySubChunk(ByteArray(BlockStorage.XZSize * SubChunk.YSize * BlockStorage.XZSize).apply { readBytes(this) }, NibbleArray(ByteArray((BlockStorage.XZSize * SubChunk.YSize * BlockStorage.XZSize) / 2).apply { readBytes(this) })).also { if (isReadable(4096)) skipBytes(4096) }
    1 -> CompactSubChunk(listOf(readLayer()))
    8 -> CompactSubChunk(List(readUnsignedByte().toInt()) { readLayer() })
    9 -> {
        val layerCount = readUnsignedByte().toInt()
        val index = readByte().toInt()
        CompactSubChunk(List(layerCount) { readLayer() }, index)
    }
    else -> error("No such sub chunk version: $version")
}
