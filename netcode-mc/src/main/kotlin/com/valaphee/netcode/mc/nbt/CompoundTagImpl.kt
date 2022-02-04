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

package com.valaphee.netcode.mc.nbt

import java.io.DataInput
import java.io.DataOutput

/**
 * @author Kevin Ludwig
 */
internal class CompoundTagImpl(
    override var name: String,
    private val value: MutableMap<String, Tag> = LinkedHashMap()
) : CompoundTag {
    override val type get() = TagType.Compound

    override fun toMap() = value

    override fun has(name: String) = value.containsKey(name)

    override fun get(name: String) = value[name]

    override fun set(name: String, tag: Tag) {
        value[name] = tag
    }

    override fun setByte(name: String, value: Byte) {
        this.value[name] = ByteTagImpl(name, value)
    }

    override fun setShort(name: String, value: Short) {
        this.value[name] = ShortTagImpl(name, value)
    }

    override fun setInt(name: String, value: Int) {
        this.value[name] = IntTagImpl(name, value)
    }

    override fun setLong(name: String, value: Long) {
        this.value[name] = LongTagImpl(name, value)
    }

    override fun setFloat(name: String, value: Float) {
        this.value[name] = FloatTagImpl(name, value)
    }

    override fun setDouble(name: String, value: Double) {
        this.value[name] = DoubleTagImpl(name, value)
    }

    override fun setByteArray(name: String, value: ByteArray) {
        this.value[name] = com.valaphee.netcode.mc.nbt.ByteArrayTagImpl(name, value)
    }

    override fun setString(name: String, value: String) {
        this.value[name] = StringTagImpl(name, value)
    }

    override fun setIntArray(name: String, value: IntArray) {
        this.value[name] = IntArrayTagImpl(name, value)
    }

    override fun setLongArray(name: String, value: LongArray) {
        this.value[name] = LongArrayTagImpl(name, value)
    }

    override fun remove(name: String): Tag? = value.remove(name)

    override val size get() = value.size

    override val isEmpty get() = value.isEmpty()

    override fun read(input: DataInput, depth: Int, remainingBytes: IntArray) {
        if (depth == 0) throw NbtException("Reached maximum allowed depth")

        value.clear()

        remainingBytes[0] -= Byte.SIZE_BYTES
        if (remainingBytes[0] < 0) throw NbtException("Reached maximum allowed size")

        var type = TagType.values()[input.readUnsignedByte()]
        while (type != TagType.End) {
            val name = input.readUTF()
            value[name] = type.tag!!.invoke(name).apply { read(input, depth - 1, remainingBytes) }

            remainingBytes[0] -= Byte.SIZE_BYTES
            if (remainingBytes[0] < 0) throw NbtException("Reached maximum allowed size")

            type = TagType.values()[input.readUnsignedByte()]
        }
    }

    override fun write(output: DataOutput) {
        value.forEach { (key, tag) ->
            output.writeByte(tag.type.ordinal)
            output.writeUTF(key)
            tag.write(output)
        }
        output.writeByte(TagType.End.ordinal)
    }

    override fun print(string: StringBuilder) {
        string.append('{')
        value.forEach { (key, tag) ->
            string.append(key).append(':')
            tag.print(string)
            string.append(',')
        }
        if (value.isEmpty()) string.append('}') else string.setCharAt(string.length - 1, '}')
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CompoundTagImpl

        if (value != other.value) return false

        return true
    }

    override fun hashCode() = value.hashCode()

    override fun toString() = StringBuilder().apply(this::print).toString()
}