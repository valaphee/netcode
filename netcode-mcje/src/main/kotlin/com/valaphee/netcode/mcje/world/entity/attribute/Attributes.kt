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

package com.valaphee.netcode.mcje.world.entity.attribute

import com.valaphee.netcode.mcje.network.PacketBuffer
import java.util.EnumMap

/**
 * @author Kevin Ludwig
 */
class Attributes(
    val attributes: MutableMap<AttributeField, AttributeValue> = EnumMap(AttributeField::class.java)
) {
    operator fun get(field: AttributeField) = attributes[field]

    @Suppress("TYPE_INFERENCE_ONLY_INPUT_TYPES_WARNING")
    fun getValue(field: AttributeField) = attributes[field]?.modifiedValue ?: field.defaultValue

    fun add(field: AttributeField) = field.attributeValue().also { attributes[field] = it }

    operator fun set(field: AttributeField, value: Double) = attributes.getOrPut(field, field::attributeValue).apply { this.value = value }

    val modified get() = attributes.values.any { it.modified }

    fun readFromBufferPre758(buffer: PacketBuffer) {
        repeat(buffer.readInt()) {
            val field = AttributeField.byKey(buffer.readNamespacedKey())
            val value = AttributeValue(field, buffer.readDouble())
            repeat(buffer.readVarInt()) { value.applyModifier(AttributeValueModifier(buffer.readUuid(), buffer.readDouble(), AttributeValueModifier.Operation.values()[buffer.readByte().toInt()])) }
            attributes[field] = value
        }
    }

    fun readFromBuffer(buffer: PacketBuffer) {
        repeat(buffer.readVarInt()) {
            val field = AttributeField.byKey(buffer.readNamespacedKey())
            val value = AttributeValue(field, buffer.readDouble())
            repeat(buffer.readVarInt()) { value.applyModifier(AttributeValueModifier(buffer.readUuid(), buffer.readDouble(), AttributeValueModifier.Operation.values()[buffer.readByte().toInt()])) }
            attributes[field] = value
        }
    }

    fun writeToBufferPre758(buffer: PacketBuffer) {
        val modifiedAttributes = attributes.filter { it.value.modified }
        buffer.writeInt(modifiedAttributes.count())
        modifiedAttributes.forEach { (field, value) ->
            if (value.modified) {
                buffer.writeNamespacedKey(field.key)
                buffer.writeDouble(value.value)
                val modifiers = value.modifiers
                buffer.writeVarInt(modifiers.size)
                modifiers.forEach {
                    buffer.writeUuid(it.id)
                    buffer.writeDouble(it.value)
                    buffer.writeByte(it.operation.ordinal)
                }
                value.flagAsSaved()
            }
        }
    }

    fun writeToBuffer(buffer: PacketBuffer) {
        val modifiedAttributes = attributes.filter { it.value.modified }
        buffer.writeVarInt(modifiedAttributes.count())
        modifiedAttributes.forEach { (field, value) ->
            if (value.modified) {
                buffer.writeNamespacedKey(field.key)
                buffer.writeDouble(value.value)
                val modifiers = value.modifiers
                buffer.writeVarInt(modifiers.size)
                modifiers.forEach {
                    buffer.writeUuid(it.id)
                    buffer.writeDouble(it.value)
                    buffer.writeByte(it.operation.ordinal)
                }
                value.flagAsSaved()
            }
        }
    }

    override fun toString() = "Attributes(${attributes.entries.joinToString { "${it.key}=${it.value.value}" }})"
}
