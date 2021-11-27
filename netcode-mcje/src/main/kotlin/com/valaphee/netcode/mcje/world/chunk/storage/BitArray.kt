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

/**
 * @author Kevin Ludwig
 */
class BitArray(
    val bitsPerEntry: Int,
    val data: LongArray,
) : Cloneable {
    val size = data.size shl (6 / bitsPerEntry)
    private val maximumEntryValue = (1L shl bitsPerEntry) - 1
    private val valuesPerLong = 64 / bitsPerEntry
    private val divideMultiply: Long
    private val divideAdd: Long
    private val divideShift: Int

    init {
        val magicIndex = 3 * (valuesPerLong - 1)
        divideMultiply = Integer.toUnsignedLong(magicValues[magicIndex])
        divideAdd = Integer.toUnsignedLong(magicValues[magicIndex + 1])
        divideShift = magicValues[magicIndex + 2]
    }

    constructor(bitsPerEntry: Int, size: Int) : this(bitsPerEntry, LongArray(roundToNearest(size * bitsPerEntry, Long.SIZE_BITS) / Long.SIZE_BITS))

    operator fun get(index: Int): Int {
        val cellIndex = cellIndex(index)
        val bitIndex = bitIndex(index, cellIndex)
        return ((data[cellIndex] shr bitIndex) and maximumEntryValue).toInt()
    }

    operator fun set(index: Int, value: Int) {
        val cellIndex = cellIndex(index)
        val bitIndex = bitIndex(index, cellIndex)
        data[cellIndex] = (data[cellIndex] and (maximumEntryValue shl bitIndex).inv()) or ((value.toLong() and maximumEntryValue) shl bitIndex)
    }

    private fun cellIndex(index: Int) = (index * divideMultiply + divideAdd shr 32 shr divideShift).toInt()

    private fun bitIndex(index: Int, cellIndex: Int) = (index - cellIndex * valuesPerLong) * bitsPerEntry

    val empty get() = data.all { it == 0L }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BitArray

        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode() = data.contentHashCode()

    companion object {
        internal fun roundToNearest(value: Int, roundTo: Int): Int {
            @Suppress("NAME_SHADOWING") var roundTo = roundTo
            if (roundTo == 0) return 0
            else if (value == 0) return roundTo
            if (value < 0) roundTo *= -1
            val remainder = value % roundTo
            return if (remainder != 0) value + roundTo - remainder else value
        }

        private val magicValues = intArrayOf(
            -1, -1, 0,
            Int.MIN_VALUE, 0, 0,
            1431655765, 1431655765, 0,
            Int.MIN_VALUE, 0, 1,
            858993459, 858993459, 0,
            715827882, 715827882, 0,
            613566756, 613566756, 0,
            Int.MIN_VALUE, 0, 2,
            477218588, 477218588, 0,
            429496729, 429496729, 0,
            390451572, 390451572, 0,
            357913941, 357913941, 0,
            330382099, 330382099, 0,
            306783378, 306783378, 0,
            286331153, 286331153, 0,
            Int.MIN_VALUE, 0, 3,
            252645135, 252645135, 0,
            238609294, 238609294, 0,
            226050910, 226050910, 0,
            214748364, 214748364, 0,
            204522252, 204522252, 0,
            195225786, 195225786, 0,
            186737708, 186737708, 0,
            178956970, 178956970, 0,
            171798691, 171798691, 0,
            165191049, 165191049, 0,
            159072862, 159072862, 0,
            153391689, 153391689, 0,
            148102320, 148102320, 0,
            143165576, 143165576, 0,
            138547332, 138547332, 0,
            Int.MIN_VALUE, 0, 4,
            130150524, 130150524, 0,
            126322567, 126322567, 0,
            122713351, 122713351, 0,
            119304647, 119304647, 0,
            116080197, 116080197, 0,
            113025455, 113025455, 0,
            110127366, 110127366, 0,
            107374182, 107374182, 0,
            104755299, 104755299, 0,
            102261126, 102261126, 0,
            99882960, 99882960, 0,
            97612893, 97612893, 0,
            95443717, 95443717, 0,
            93368854, 93368854, 0,
            91382282, 91382282, 0,
            89478485, 89478485, 0,
            87652393, 87652393, 0,
            85899345, 85899345, 0,
            84215045, 84215045, 0,
            82595524, 82595524, 0,
            81037118, 81037118, 0,
            79536431, 79536431, 0,
            78090314, 78090314, 0,
            76695844, 76695844, 0,
            75350303, 75350303, 0,
            74051160, 74051160, 0,
            72796055, 72796055, 0,
            71582788, 71582788, 0,
            70409299, 70409299, 0,
            69273666, 69273666, 0,
            68174084, 68174084, 0,
            Int.MIN_VALUE, 0, 5
        )
    }
}
