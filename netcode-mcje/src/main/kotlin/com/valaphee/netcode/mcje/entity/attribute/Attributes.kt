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

package com.valaphee.netcode.mcje.entity.attribute

import com.valaphee.netcode.mcje.PacketBuffer
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

    fun readFromBuffer(buffer: PacketBuffer) {
        repeat(buffer.readInt()) {
            val field = AttributeField.byKey(buffer.readNamespacedKey())
            val value = AttributeValue(field, buffer.readDouble())
            repeat(buffer.readVarInt()) { value.applyModifier(AttributeValueModifier(buffer.readUuid(), buffer.readDouble(), AttributeValueModifier.Operation.values()[buffer.readByte().toInt()])) }
            attributes[field] = value
        }
    }

    fun writeToBuffer(buffer: PacketBuffer) {
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

    override fun toString() = StringBuilder("Attributes(").apply {
        attributes.forEach { (field, value) -> append(field).append('=').append(value.value).append(',') }
        if (attributes.isEmpty()) append(')') else setCharAt(length - 1, ')')
    }.toString()
}
