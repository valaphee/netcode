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

fun linkOrMinecraftKey(string: String) = if (string.startsWith('#')) Link(string.substring(1)) else minecraftKey(string)

/**
 * @author Kevin Ludwig
 */
@JsonSerialize(using = LinkOrNamespacedKey.Serializer::class)
@JsonDeserialize(using = LinkOrNamespacedKey.Deserializer::class)
interface LinkOrNamespacedKey {
    fun resolve(resolve: (String) -> NamespacedKey?): NamespacedKey?

    class Serializer : JsonSerializer<Link>() {
        override fun serialize(value: Link, generator: JsonGenerator, provider: SerializerProvider) {
            generator.writeString(value.toString())
        }
    }

    class Deserializer : JsonDeserializer<LinkOrNamespacedKey>() {
        override fun deserialize(parser: JsonParser, context: DeserializationContext) = linkOrMinecraftKey(parser.codec.readValue(parser, String::class.java))
    }
}
