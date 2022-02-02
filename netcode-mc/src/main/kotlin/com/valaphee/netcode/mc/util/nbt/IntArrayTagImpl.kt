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

package com.valaphee.netcode.mc.util.nbt

import java.io.DataInput
import java.io.DataOutput
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

/**
 * @author Kevin Ludwig
 */
internal class IntArrayTagImpl(
    override var name: String,
    private var value: IntArray? = null
) : ArrayTag {
    override val type get() = TagType.IntArray

    override fun toByteArray() = ByteBuffer.allocate(value!!.size * Int.SIZE_BYTES).apply { asIntBuffer().put(value) }.array()

    override fun toIntArray() = value!!.clone()

    override fun toLongArray() = ByteBuffer.allocate(value!!.size * Int.SIZE_BYTES).apply { asIntBuffer().put(value) }.asLongBuffer().array()

    override fun valueToString() = String(toByteArray(), StandardCharsets.UTF_8)

    override fun read(input: DataInput, depth: Int, remainingBytes: IntArray) {
        remainingBytes[0] -= Int.SIZE_BYTES
        if (remainingBytes[0] < 0) throw NbtException("Reached maximum allowed size")

        val valueLength = input.readInt()
        remainingBytes[0] -= valueLength * Int.SIZE_BYTES
        if (remainingBytes[0] < 0) throw NbtException("Reached maximum allowed size")

        value = IntArray(valueLength) { input.readInt() }
    }

    override fun write(output: DataOutput) {
        value?.let {
            output.writeInt(it.size)
            it.forEach { output.writeInt(it) }
        } ?: output.writeInt(0)
    }

    override fun print(string: StringBuilder) {
        string.append("[I;")
        value?.let {
            it.forEach { string.append(it).append(',') }
            if (it.isEmpty()) string.append(']') else string.setCharAt(string.length - 1, ']')
        } ?: string.append(']')
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IntArrayTagImpl

        if (value != null) {
            if (other.value == null) return false
            if (!value.contentEquals(other.value)) return false
        } else if (other.value != null) return false

        return true
    }

    override fun hashCode() = value?.contentHashCode() ?: 0

    override fun toString() = StringBuilder().apply(this::print).toString()
}
