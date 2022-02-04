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

package com.valaphee.netcode.mcje.world.chunk

import com.valaphee.netcode.mcje.network.PacketBuffer
import it.unimi.dsi.fastutil.ints.IntArrayList
import it.unimi.dsi.fastutil.ints.IntList

/**
 * @author Kevin Ludwig
 */
class SubChunk(
    val blockPalette: IntList?,
    val blocks: BitArray,
    var blockCount: Int
) {
    constructor(bitsPerBlock: Int) : this(IntArrayList(16).apply { add(0) }, BitArray(bitsPerBlock, BlockStorage.XZSize * YSize * BlockStorage.XZSize), 0)

    operator fun get(x: Int, y: Int, z: Int): Int {
        val indexOrValue = blocks[(y shl YShift) or (z shl ZShift) or x]
        return blockPalette?.getInt(indexOrValue) ?: indexOrValue
    }

    operator fun set(x: Int, y: Int, z: Int, value: Int) {
        var indexOrValue = value
        blockPalette?.let {
            indexOrValue = it.getInt(value)
            if (indexOrValue == -1) {
                indexOrValue = it.size
                it.add(indexOrValue)
            }
        }
        blocks[(y shl YShift) or (z shl ZShift) or x] = indexOrValue
    }

    companion object {
        const val YSize = 16
        const val YShift = 8
        const val ZShift = 4
    }
}

fun PacketBuffer.readSubChunk(): SubChunk {
    val blockCount = buffer.readUnsignedShort()
    val bitsPerBlock = buffer.readByte().toInt()
    var blockPalette: IntArrayList? = null
    if (bitsPerBlock <= 8) {
        val blockPaletteSize = readVarInt()
        blockPalette = IntArrayList(blockPaletteSize).apply { repeat(blockPaletteSize) { add(readVarInt()) } }
    }
    return SubChunk(blockPalette, BitArray(bitsPerBlock, LongArray(readVarInt()) { buffer.readLong() }), blockCount)
}
