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

package com.valaphee.netcode.mcbe.entity.attribute

import com.valaphee.netcode.mcbe.PacketBuffer
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

    override fun toString() = StringBuilder("Attributes(").apply {
        attributes.forEach { (field, value) -> append(field).append('=').append(value.value).append(',') }
        if (attributes.isEmpty()) append(')') else setCharAt(length - 1, ')')
    }.toString()
}
