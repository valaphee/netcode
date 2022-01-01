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

/**
 * @author Kevin Ludwig
 */
internal class FloatTagImpl(
    override var name: String,
    private var value: Float = 0.0f
) : NumberTag {
    override val type get() = TagType.Float

    override val isNumber get() = true

    override fun toByte() = value.toInt().toByte()

    override fun toShort() = value.toInt().toShort()

    override fun toInt() = value.toInt()

    override fun toLong() = value.toLong()

    override fun toFloat() = value

    override fun toDouble() = value.toDouble()

    override fun read(input: DataInput, depth: Int, remainingBytes: IntArray) {
        remainingBytes[0] -= Float.SIZE_BYTES
        if (remainingBytes[0] < 0) throw NbtException("Reached maximum allowed size")

        value = input.readFloat()
    }

    override fun write(output: DataOutput) {
        output.writeFloat(value)
    }

    override fun print(string: StringBuilder) {
        string.append(value).append('f')
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FloatTagImpl

        if (value != other.value) return false

        return true
    }

    override fun hashCode() = value.hashCode()

    override fun toString() = StringBuilder().apply(this::print).toString()
}
