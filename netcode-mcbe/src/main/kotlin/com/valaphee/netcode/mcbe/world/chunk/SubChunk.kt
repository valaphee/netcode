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

import com.valaphee.netcode.mc.util.NibbleArray
import com.valaphee.netcode.mc.util.nibbleArray
import com.valaphee.netcode.mcbe.network.PacketBuffer

/**
 * @author Kevin Ludwig
 */
interface SubChunk {
    val empty: Boolean

    operator fun get(x: Int, y: Int, z: Int): Int

    operator fun get(x: Int, y: Int, z: Int, layer: Int) = get(x, y, z)

    operator fun set(x: Int, y: Int, z: Int, value: Int)

    operator fun set(x: Int, y: Int, z: Int, value: Int, layer: Int) = set(x, y, z, value)

    fun writeToBuffer(buffer: PacketBuffer)

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
    var blockSubIds: NibbleArray = nibbleArray(BlockStorage.XZSize * SubChunk.YSize * BlockStorage.XZSize)
) : SubChunk {
    override fun get(x: Int, y: Int, z: Int): Int {
        val index = (x shl SubChunk.XShift) or (z shl SubChunk.ZShift) or y
        return (blockIds[index].toInt() and (blockIdMask shl blockIdShift)) or blockSubIds[index]
    }

    override fun set(x: Int, y: Int, z: Int, value: Int) {
        val index = (x shl SubChunk.XShift) or (z shl SubChunk.ZShift) or y
        blockIds[index] = ((value shr blockIdShift) and blockIdMask).toByte()
        blockSubIds[index] = (value and blockSubIdMask)
    }

    override val empty get() = blockIds.all { it.toInt() == 0 }

    override fun writeToBuffer(buffer: PacketBuffer) {
        buffer.writeByte(0)
        buffer.writeBytes(blockIds)
        buffer.writeBytes(blockSubIds.data)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LegacySubChunk

        if (!blockIds.contentEquals(other.blockIds)) return false
        if (blockSubIds != other.blockSubIds) return false

        return true
    }

    override fun hashCode(): Int {
        var result = blockIds.contentHashCode()
        result = 31 * result + blockSubIds.hashCode()
        return result
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
    var layers: Array<Layer>
) : SubChunk {
    constructor(default: Int, version: BitArray.Version) : this(arrayOf(Layer(default, version), Layer(default, version)))

    override fun get(x: Int, y: Int, z: Int) = get(x, y, z, 0)

    override fun get(x: Int, y: Int, z: Int, layer: Int) = layers[layer].get((x shl SubChunk.XShift) or (z shl SubChunk.ZShift) or y)

    override fun set(x: Int, y: Int, z: Int, value: Int) = set(x, y, z, value, 0)

    override fun set(x: Int, y: Int, z: Int, value: Int, layer: Int) = layers[layer].set((x shl SubChunk.XShift) or (z shl SubChunk.ZShift) or y, value)

    override val empty get() = layers.all { it.empty }

    override fun writeToBuffer(buffer: PacketBuffer) = writeToBuffer(buffer, true)

    fun writeToBuffer(buffer: PacketBuffer, runtime: Boolean) {
        if (layers.size == 1) buffer.writeByte(1) else {
            buffer.writeByte(8)
            buffer.writeByte(layers.size)
        }
        layers.forEach { it.writeToBuffer(buffer, runtime) }
    }
}

fun PacketBuffer.readSubChunk(default: Int) = when (val version = readUnsignedByte().toInt()) {
    0, 2, 3, 4, 5, 6 -> LegacySubChunk(ByteArray(BlockStorage.XZSize * SubChunk.YSize * BlockStorage.XZSize).apply { readBytes(this) }, nibbleArray(ByteArray((BlockStorage.XZSize * SubChunk.YSize * BlockStorage.XZSize) / 2).apply { readBytes(this) })).also { if (isReadable(4096)) skipBytes(4096) }
    1 -> CompactSubChunk(arrayOf(readLayer(default)))
    8 -> CompactSubChunk(Array(readUnsignedByte().toInt()) { readLayer(default) })
    9 -> {
        val layerCount = readUnsignedByte().toInt()
        readByte() // absolute index for data-driven dimension heights
        CompactSubChunk(Array(layerCount) { readLayer(default) })
    }
    else -> TODO(version.toString())
}
