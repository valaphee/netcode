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

package com.valaphee.netcode.mcje.util

/**
 * @author Kevin Ludwig
 */
class NibbleArray(
    val bytes: ByteArray
) {
    val length: Int = bytes.size * 2

    constructor(length: Int) : this(ByteArray(length / 2))

    operator fun get(index: Int): Int {
        val value = bytes[index shr 1].toInt()
        return if (index and 1 == 0) value and 0xF else (value shr 4) and 0xF
    }

    operator fun set(index: Int, value: Int) {
        val _index = index shr 1
        bytes[_index] = (if (_index and 1 == 0) (bytes[_index].toInt() and 0xF0) or (value and 0xF) else (bytes[_index].toInt() and 0xF) or ((value and 0xF) shl 4)).toByte()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NibbleArray

        if (!bytes.contentEquals(other.bytes)) return false

        return true
    }

    override fun hashCode() = bytes.contentHashCode()
}
