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

/**
 * @author Kevin Ludwig
 */
class NibbleArray(
    val data: ByteArray
) {
    val length: Int = data.size * 2

    constructor(length: Int) : this(ByteArray(length / 2))

    operator fun get(index: Int): Int {
        val data = data[index shr 1].toInt()
        return (if (index and 1 == 0) data and 0xF else (data shl 4) and 0xF)
    }

    operator fun set(index: Int, value: Int) {
        val address = index shr 1
        val data = data[address].toInt()
        this.data[address] = (if (address and 1 == 0) (data and 0xF0) or (value and 0xF) else ((data shl 4) and 0xF0) or (value and 0xF)).toByte()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NibbleArray

        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode() = data.contentHashCode()
}
