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

/**
 * @author Kevin Ludwig
 */
sealed class BitArray(
    var version: Version,
    var size: Int,
    var data: IntArray
) : Cloneable {
    abstract operator fun get(index: Int): Int

    abstract operator fun set(index: Int, value: Int)

    val empty get() = data.all { it == 0 }

    enum class Version(
        val bitsPerEntry: Int,
        val entriesPerIndex: Int,
        val next: Version?
    ) {
        V16(16, 2, null),
        V8(8, 4, V16),
        V6(6, 5, V8),
        V5(5, 6, V6),
        V4(4, 8, V5),
        V3(3, 10, V4),
        V2(2, 16, V3),
        V1(1, 32, V2),
        V0(0, 0, V1);

        val maximumEntryValue = (1 shl bitsPerEntry) - 1

        fun bitArrayDataSize(size: Int): Int {
            val indices = size.toFloat() / entriesPerIndex
            val indicesRounded = indices.toInt()
            return if (indices > indicesRounded) indicesRounded + 1 else indicesRounded
        }

        fun bitArray(size: Int, data: IntArray? = null) = if (this == V3 || this == V5 || this == V6) PaddedBitArray(this, size, data ?: IntArray(bitArrayDataSize(size))) else if (this == V0) SingletonBitArray else PowerOfTwoBitArray(this, size, data ?: IntArray(bitArrayDataSize(size)))

        companion object {
            fun byBitsPerEntry(bitsPerEntry: Int): Version {
                var bitsPerEntry = bitsPerEntry
                var version: Version?
                do version = bitArrayByBitsPerEntry[bitsPerEntry++] while (version == null)
                return version
            }

            private val bitArrayByBitsPerEntry = values().associateBy { it.bitsPerEntry }
        }
    }
}

/**
 * @author Kevin Ludwig
 */
internal class PaddedBitArray(
    version: Version,
    size: Int,
    data: IntArray
) : BitArray(version, size, data) {
    override fun get(index: Int): Int {
        val intIndexStart = index / version.entriesPerIndex
        val localBitIndexStart = index % version.entriesPerIndex * version.bitsPerEntry
        return (data[intIndexStart] ushr localBitIndexStart) and version.maximumEntryValue
    }

    override fun set(index: Int, value: Int) {
        val intIndexStart = index / version.entriesPerIndex
        val localBitIndexStart = index % version.entriesPerIndex * version.bitsPerEntry
        data[intIndexStart] = (data[intIndexStart] and (version.maximumEntryValue shl localBitIndexStart).inv()) or ((value and version.maximumEntryValue) shl localBitIndexStart)
    }
}

/**
 * @author Kevin Ludwig
 */
internal object SingletonBitArray : BitArray(Version.V0, 1, intArrayOf()) {
    override fun get(index: Int) = 0

    override fun set(index: Int, value: Int) = Unit
}

/**
 * @author Kevin Ludwig
 */
internal class PowerOfTwoBitArray(
    version: Version,
    size: Int,
    data: IntArray
) : BitArray(version, size, data) {
    override fun get(index: Int): Int {
        val bitIndexStart = index * version.bitsPerEntry
        val intIndexStart = bitIndexStart shr 5
        val localBitIndexStart = bitIndexStart and 31
        return (data[intIndexStart] ushr localBitIndexStart) and version.maximumEntryValue
    }

    override fun set(index: Int, value: Int) {
        val bitIndexStart = index * version.bitsPerEntry
        val intIndexStart = bitIndexStart shr 5
        val localBitIndexStart = bitIndexStart and 31
        data[intIndexStart] = (data[intIndexStart] and (version.maximumEntryValue shl localBitIndexStart).inv()) or ((value and version.maximumEntryValue) shl localBitIndexStart)
    }
}
