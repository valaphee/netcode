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

package com.valaphee.netcode.mcbe.util

import sun.misc.Unsafe

fun nibbleArray(data: ByteArray): NibbleArray = HeapNibbleArray(data)

fun nibbleArray(length: Int): NibbleArray = UnsafeNibbleArray(length)

/**
 * @author Kevin Ludwig
 */
interface NibbleArray {
    operator fun get(index: Int): Int

    operator fun set(index: Int, value: Int)

    val length: Int

    val data: ByteArray
}

/**
 * @author Kevin Ludwig
 */
private class HeapNibbleArray(
    override val data: ByteArray
) : NibbleArray {
    override val length: Int = data.size * 2

    constructor(length: Int) : this(ByteArray(length / 2))

    override fun get(index: Int): Int {
        val data = data[index shr 1].toInt()
        return (if (index and 1 == 0) data and 0xF else (data shl 4) and 0xF)
    }

    override fun set(index: Int, value: Int) {
        val address = index shr 1
        val data = data[address].toInt()
        this.data[address] = (if (address and 1 == 0) (data and 0xF0) or (value and 0xF) else ((data shl 4) and 0xF0) or (value and 0xF)).toByte()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HeapNibbleArray

        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode() = data.contentHashCode()
}

/**
 * @author Kevin Ludwig
 */
private class UnsafeNibbleArray(
    override val length: Int
) : NibbleArray {
    private val address = unsafe.allocateMemory(length.toLong())

    override fun get(index: Int): Int {
        val data = unsafe.getByte(address + (index shr 1)).toInt()
        return (if (index and 1 == 0) data and 0xF else (data shl 4) and 0xF)
    }

    override fun set(index: Int, value: Int) {
        val address = address + (index shr 1)
        val data = unsafe.getByte(address).toInt()
        unsafe.putByte(address, (if (index and 1 == 0) (data and 0xF0) or (value and 0xF) else ((data shl 4) and 0xF0) or (value and 0xF)).toByte())
    }

    override val data: ByteArray
        get() = ByteArray(length / 2) { unsafe.getByte(address + it) }

    protected fun finalize() {
        unsafe.freeMemory(address)
    }
}

private val unsafe = Unsafe::class.java.getDeclaredField("theUnsafe").apply { isAccessible = true }.get(null) as Unsafe
