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

package com.valaphee.netcode.mc.util.nbt

import java.io.DataInput
import java.io.DataOutput

/**
 * @author Kevin Ludwig
 */
internal class ByteTagImpl(
    override var name: String,
    private var value: Byte = 0
) : NumberTag {
    override val type get() = TagType.Byte

    override fun toByte() = value

    override fun toShort() = value.toShort()

    override fun toInt() = value.toInt()

    override fun toLong() = value.toLong()

    override fun toFloat() = value.toFloat()

    override fun toDouble() = value.toDouble()

    override fun read(input: DataInput, depth: Int, remainingBytes: IntArray) {
        remainingBytes[0] -= Byte.SIZE_BYTES
        if (remainingBytes[0] < 0) throw NbtException("Reached maximum allowed size")

        value = input.readByte()
    }

    override fun write(output: DataOutput) {
        output.writeByte(value.toInt())
    }

    override fun print(string: StringBuilder) {
        string.append(value.toInt()).append('b')
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ByteTagImpl

        if (value != other.value) return false

        return true
    }

    override fun hashCode() = value.toInt()

    override fun toString() = StringBuilder().apply(this::print).toString()
}