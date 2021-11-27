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

package com.valaphee.netcode.mcje.util

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize

fun minecraftKey(string: String): NamespacedKey {
    val delimiter = string.indexOf(':')
    return when {
        delimiter >= 1 -> NamespacedKey(string.substring(0, delimiter), string.substring(delimiter + 1))
        delimiter >= 0 -> NamespacedKey("minecraft", string.substring(delimiter + 1))
        else -> NamespacedKey("minecraft", string)
    }
}

/**
 * @author Kevin Ludwig
 */
@JsonSerialize(using = NamespacedKey.Serializer::class)
@JsonDeserialize(using = NamespacedKey.Deserializer::class)
data class NamespacedKey(
    val namespace: String,
    val key: String
) : LinkOrNamespacedKey, Comparable<NamespacedKey> {
    override fun resolve(resolve: (String) -> NamespacedKey?) = this

    override fun compareTo(other: NamespacedKey) = when {
        key != other.key -> key.compareTo(other.key)
        else -> namespace.compareTo(other.namespace)
    }

    override fun toString() = if (namespace.isEmpty()) key else "$namespace:$key"

    class Serializer : JsonSerializer<NamespacedKey>() {
        override fun serialize(value: NamespacedKey, generator: JsonGenerator, provider: SerializerProvider) {
            generator.writeString(value.toString())
        }
    }

    class Deserializer : JsonDeserializer<NamespacedKey>() {
        override fun deserialize(parser: JsonParser, context: DeserializationContext) = minecraftKey(parser.codec.readValue(parser, String::class.java))
    }
}
