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
