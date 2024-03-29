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

package com.valaphee.netcode.mcje.world.entity.metadata

import com.valaphee.netcode.mcje.network.PacketBuffer
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

    fun readFromBuffer(buffer: PacketBuffer, version: Int) {
        var fieldId = buffer.readByte().toInt()
        while (fieldId != -1) {
            val typeId = buffer.readVarInt()
            val type = MetadataType.registry[typeId] ?: error("No such metadata type: $typeId (field id: $fieldId)")
            values[fieldId] = MetadataValue(type, type.read(buffer, version))
            fieldId = buffer.readByte().toInt()
        }
    }

    fun writeToBuffer(buffer: PacketBuffer, version: Int) {
        values.forEach { (fieldId, value) ->
            if (value.modified) {
                buffer.writeByte(fieldId)
                val type = value.type
                buffer.writeVarInt(MetadataType.registry.getInt(type))
                type.write(buffer, value, version)
            }
        }
        buffer.writeByte(-1)
    }

    override fun toString() = "Metadata(${values.int2ObjectEntrySet().joinToString { "${it.intKey}=${it.value.value}" }})"
}
