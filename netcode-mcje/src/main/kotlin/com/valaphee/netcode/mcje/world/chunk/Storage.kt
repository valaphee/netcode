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
sealed class Storage {
    abstract val bitsPerEntry: Int

    abstract operator fun get(index: Int): Int

    abstract operator fun set(index: Int, value: Int)
}

/**
 * @author Kevin Ludwig
 */
class SingleStorage(
    val value: Int
) : Storage() {
    override val bitsPerEntry get() = 0

    override fun get(index: Int) = value

    override fun set(index: Int, value: Int) = TODO()
}

/**
 * @author Kevin Ludwig
 */
class PalettedStorage(
    val palette: IntList,
    val data: BitArray
) : Storage() {
    override val bitsPerEntry get() = data.bitsPerEntry

    override fun get(index: Int) = palette.getInt(data[index])

    override fun set(index: Int, value: Int) {
        var paletteIndex = palette.indexOf(value)
        if (paletteIndex == -1) {
            paletteIndex = palette.size
            palette.add(value)
        }
        data[index] = paletteIndex
    }
}

/**
 * @author Kevin Ludwig
 */
class GlobalStorage(
    val data: BitArray
) : Storage() {
    override val bitsPerEntry get() = data.bitsPerEntry

    override fun get(index: Int) = data[index]

    override fun set(index: Int, value: Int) {
        data[index] = value
    }
}

fun PacketBuffer.readStorage(): Storage {
    val bitsPerEntry = readUnsignedByte().toInt()
    return if (bitsPerEntry == 0) SingleStorage(readVarInt()).also { LongArray(readVarInt()) { readLong() } }
    else if (bitsPerEntry <= 8) {
        val paletteSize = readVarInt()
        PalettedStorage(IntArrayList(paletteSize).apply { repeat(paletteSize) { add(readVarInt()) } }, BitArray(bitsPerEntry, LongArray(readVarInt()) { readLong() }))
    } else GlobalStorage(BitArray(bitsPerEntry, LongArray(readVarInt()) { readLong() }))
}

fun PacketBuffer.writeStorage(value: Storage) {
    writeByte(value.bitsPerEntry)
    when (value) {
        is SingleStorage -> {
            writeVarInt(value.value)
            writeVarInt(0)
        }
        is PalettedStorage -> {
            writeVarInt(value.palette.size)
            value.palette.forEach { writeVarInt(it) }
            writeVarInt(value.data.data.size)
            value.data.data.forEach { buffer.writeLong(it) }
        }
        is GlobalStorage -> {
            writeVarInt(value.data.data.size)
            value.data.data.forEach { buffer.writeLong(it) }
        }
    }
}
