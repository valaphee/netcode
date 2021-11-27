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

package com.valaphee.netcode.mcje.world.chunk.storage

import com.valaphee.netcode.mcje.PacketBuffer
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
