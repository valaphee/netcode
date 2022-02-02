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
