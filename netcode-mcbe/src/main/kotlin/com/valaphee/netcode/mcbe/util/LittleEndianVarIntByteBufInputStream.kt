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

import io.netty.buffer.ByteBuf
import java.nio.charset.StandardCharsets

/**
 * @author Kevin Ludwig
 */
class LittleEndianVarIntByteBufInputStream : LittleEndianByteBufInputStream {
    constructor(buffer: ByteBuf) : super(buffer)

    constructor(buffer: ByteBuf, length: Int) : super(buffer, length)

    constructor(buffer: ByteBuf, releaseOnClose: Boolean) : super(buffer, releaseOnClose)

    constructor(buffer: ByteBuf, length: Int, releaseOnClose: Boolean) : super(buffer, length, releaseOnClose)

    override fun readInt(): Int {
        val value = readVarUInt()
        return (value ushr 1) xor -(value and 1)
    }

    override fun readLong(): Long {
        var value: Long = 0
        var shift = 0
        while (shift <= 70) {
            val head = readByte().toInt()
            value = value or ((head and 0x7F).toLong() shl shift)
            if (head and 0x80 == 0) return (value ushr 1) xor -(value and 1)
            shift += 7
        }
        throw ArithmeticException("VarLong wider than 70-bit")
    }

    override fun readUTF(): String {
        val length = readVarUInt()
        check(Short.MAX_VALUE >= length) { "Maximum length of ${Short.MAX_VALUE} exceeded" }
        val bytes = ByteArray(length)
        readFully(bytes)
        return String(bytes, StandardCharsets.UTF_8)
    }

    private fun readVarUInt(): Int {
        var value = 0
        var shift = 0
        while (shift <= 35) {
            val head = readByte().toInt()
            value = value or ((head and 0x7F) shl shift)
            if (head and 0x80 == 0) return value
            shift += 7
        }
        throw ArithmeticException("VarInt wider than 35-bit")
    }
}
