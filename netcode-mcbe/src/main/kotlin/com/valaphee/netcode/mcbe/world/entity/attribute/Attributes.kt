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
import com.valaphee.netcode.mcbe.network.V1_19_020
import com.valaphee.netcode.util.LazyList
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

    fun readFromBuffer(buffer: PacketBuffer, version: Int, initial: Boolean) {
        repeat(buffer.readVarUInt()) {
            if (initial) {
                val field = AttributeField.byKey(buffer.readString())
                val minimum = buffer.readFloatLE()
                val value = buffer.readFloatLE()
                val maximum = buffer.readFloatLE()
                attributes[field] = AttributeValue(field, minimum, maximum, value, emptyList())
            } else {
                val minimum = buffer.readFloatLE()
                val maximum = buffer.readFloatLE()
                val value = buffer.readFloatLE()
                val defaultValue = buffer.readFloatLE()
                val field = AttributeField.byKey(buffer.readString())
                val modifiers = if (version >= V1_19_020) LazyList(buffer.readVarUInt()) { AttributeValueModifier(buffer.readString(), buffer.readString(), buffer.readFloatLE(), AttributeValueModifier.Operation.values()[buffer.readIntLE()], buffer.readIntLE(), buffer.readBoolean()) } else emptyList()
                attributes[field] = AttributeValue(field, minimum, maximum, defaultValue, modifiers, value)
            }
        }
    }

    fun writeToBuffer(buffer: PacketBuffer, version: Int, initial: Boolean) {
        val modifiedAttributes = attributes.filter { it.value.modified }
        buffer.writeVarUInt(modifiedAttributes.count())
        modifiedAttributes.forEach { (field, value) ->
            if (initial) {
                buffer.writeString(field.key)
                buffer.writeFloatLE(value.minimum)
                buffer.writeFloatLE(value.value)
                buffer.writeFloatLE(value.maximum)
            } else {
                buffer.writeFloatLE(value.minimum)
                buffer.writeFloatLE(value.maximum)
                buffer.writeFloatLE(value.value)
                buffer.writeFloatLE(value.defaultValue)
                buffer.writeString(field.key)
                if (version >= V1_19_020) {
                    buffer.writeVarUInt(value.modifiers.size)
                    value.modifiers.forEach {
                        buffer.writeString(it.id)
                        buffer.writeString(it.name)
                        buffer.writeFloatLE(it.value)
                        buffer.writeIntLE(it.operation.ordinal)
                        buffer.writeIntLE(it.operand)
                        buffer.writeBoolean(it.serializable)
                    }
                }
            }
            value.flagAsSaved()
        }
    }

    override fun toString() = "Attributes(${attributes.entries.joinToString { "${it.key}=${it.value.value}" }})"
}
