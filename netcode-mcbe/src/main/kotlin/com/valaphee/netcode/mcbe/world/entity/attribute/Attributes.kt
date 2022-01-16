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

package com.valaphee.netcode.mcbe.world.entity.attribute

import com.valaphee.netcode.mcbe.network.PacketBuffer
import java.util.EnumMap

/**
 * @author Kevin Ludwig
 */
class Attributes(
    val attributes: MutableMap<AttributeField, AttributeValue> = EnumMap(AttributeField::class.java)
) {
    operator fun get(field: AttributeField) = attributes[field]

    fun getValue(field: AttributeField) = attributes[field]?.value ?: field.defaultValue

    fun add(field: AttributeField) = field.attributeValue().also { attributes[field] = it }

    operator fun set(field: AttributeField, value: Float) = attributes.getOrPut(field, field::attributeValue).apply { this.value = value }

    val modified get() = attributes.values.any { it.modified }

    fun readFromBuffer(buffer: PacketBuffer, withDefault: Boolean) {
        repeat(buffer.readVarUInt()) {
            if (withDefault) {
                val minimum = buffer.readFloatLE()
                val maximum = buffer.readFloatLE()
                val value = buffer.readFloatLE()
                val defaultValue = buffer.readFloatLE()
                val field = AttributeField.byKey(buffer.readString())
                attributes[field] = AttributeValue(field, minimum, maximum, defaultValue, value)
            } else {
                val field = AttributeField.byKey(buffer.readString())
                val minimum = buffer.readFloatLE()
                val value = buffer.readFloatLE()
                val maximum = buffer.readFloatLE()
                attributes[field] = AttributeValue(field, minimum, maximum, value)
            }
        }
    }

    fun writeToBuffer(buffer: PacketBuffer, withDefault: Boolean) {
        val modifiedAttributes = attributes.filter { it.value.modified }
        buffer.writeVarUInt(modifiedAttributes.count())
        modifiedAttributes.forEach { (field, value) ->
            if (withDefault) {
                buffer.writeFloatLE(value.minimum)
                buffer.writeFloatLE(value.maximum)
                buffer.writeFloatLE(value.value)
                buffer.writeFloatLE(value.defaultValue)
                buffer.writeString(field.key)
            } else {
                buffer.writeString(field.key)
                buffer.writeFloatLE(value.minimum)
                buffer.writeFloatLE(value.value)
                buffer.writeFloatLE(value.maximum)
            }
            value.flagAsSaved()
        }
    }

    override fun toString() = "Attributes(${attributes.entries.joinToString { "${it.key}=${it.value.value}" }})"
}
