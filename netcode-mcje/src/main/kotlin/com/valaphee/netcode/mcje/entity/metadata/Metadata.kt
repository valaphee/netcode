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

package com.valaphee.netcode.mcje.entity.metadata

import com.valaphee.netcode.mcje.PacketBuffer
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

/**
 * @author Kevin Ludwig
 */
class Metadata {
    private val values = Int2ObjectOpenHashMap<MetadataValue<Any?>>()

    fun has(fieldId: Int) = values.containsKey(fieldId)

    fun has(fieldId: Int, type: MetadataType<*>) = values[fieldId]?.type == type

    @Suppress("UNCHECKED_CAST")
    operator fun <T> get(fieldId: Int) = values[fieldId]?.value as T?

    @Suppress("UNCHECKED_CAST")
    operator fun <T> set(fieldId: Int, type: MetadataType<T>, value: T) {
        values[fieldId]?.let { it.value = value as Any } ?: run { values[fieldId] = MetadataValue(type, value) as MetadataValue<Any?> }
    }

    val modified get() = values.values.any { it.modified }

    fun readFromBuffer(buffer: PacketBuffer) {
        var fieldId = buffer.readByte().toInt()
        while (fieldId != -1) {
            val type = MetadataType.registry.idToValue.get(buffer.readVarInt())
            values[fieldId] = MetadataValue(type, type.read(buffer))
            fieldId = buffer.readByte().toInt()
        }
    }

    fun writeToBuffer(buffer: PacketBuffer) {
        values.forEach { (fieldId, value) ->
            if (value.modified) {
                buffer.writeByte(fieldId)
                val type = value.type
                buffer.writeVarInt(MetadataType.registry.getId(type))
                type.write(buffer, value)
            }
        }
        buffer.writeByte(-1)
    }

    override fun toString() = StringBuilder("Metadata(").apply {
        values.forEach { (fieldId, value) -> append(fieldId).append('=').append(value.value).append(',') }
        if (values.isEmpty()) append(')') else setCharAt(length - 1, ')')
    }.toString()
}
