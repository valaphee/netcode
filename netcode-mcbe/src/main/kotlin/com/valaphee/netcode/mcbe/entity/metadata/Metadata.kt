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

package com.valaphee.netcode.mcbe.entity.metadata

import com.valaphee.netcode.mcbe.PacketBuffer
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

/**
 * @author Kevin Ludwig
 */
class Metadata {
    private val values = Int2ObjectOpenHashMap<MetadataValue<Any?>>()

    fun has(fieldId: Int) = values.containsKey(fieldId)

    fun has(fieldId: Int, type: MetadataType<*>) = values[fieldId]?.type == type

    fun has(field: MetadataField<*>) = values[field.id]?.type == field.type

    @Suppress("UNCHECKED_CAST")
    operator fun <T> get(fieldId: Int) = values[fieldId]?.value as T?

    @Suppress("UNCHECKED_CAST")
    operator fun <T> get(field: MetadataField<T>) = values[field.id]?.value as T?

    @Suppress("UNCHECKED_CAST")
    operator fun <T> set(fieldId: Int, type: MetadataType<T>, value: T) {
        values[fieldId]?.let { it.value = value } ?: run { values[fieldId] = MetadataValue(type, value) as MetadataValue<Any?> }
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <T> set(field: MetadataField<T>, value: T) {
        values[field.id]?.let { it.value = value } ?: run { values[field.id] = MetadataValue(field.type, value) as MetadataValue<Any?> }
    }

    val modified get() = values.values.any { it.modified }

    fun readFromBuffer(buffer: PacketBuffer) {
        repeat(buffer.readVarUInt()) {
            val fieldId = buffer.readVarUInt()
            val type = checkNotNull(MetadataType.registry[buffer.readVarUInt()])
            @Suppress("UNCHECKED_CAST")
            values[fieldId] = MetadataValue(type, type.read(buffer)) as MetadataValue<Any?>?
        }
    }

    fun writeToBuffer(buffer: PacketBuffer) {
        val modifiedValues = values.filterValues { it.modified }.onEach { it.value.modified = false }
        buffer.writeVarUInt(modifiedValues.size)
        modifiedValues.forEach { (fieldId, value) ->
            buffer.writeVarUInt(fieldId)
            val type = value.type
            buffer.writeVarUInt(MetadataType.registry.getId(type))
            type.write(buffer, value.value)
        }
    }

    override fun toString() = StringBuilder("Metadata(").apply {
        values.forEach { (fieldId, value) -> append(fieldId).append('=').append(value.value).append(',') }
        if (values.isEmpty()) append(')') else setCharAt(length - 1, ')')
    }.toString()
}
