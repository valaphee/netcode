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

package com.valaphee.netcode.mcje.util.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.math.BigInteger
import java.util.UUID

fun UUID.toUndashedString(): String {
    val value = toString()
    return StringBuilder(32)
        .append(value, 0, 8)
        .append(value, 9, 13)
        .append(value, 14, 18)
        .append(value, 19, 23)
        .append(value, 24, 36)
        .toString()
}

/**
 * @author Kevin Ludwig
 */
object UuidSerializer : JsonSerializer<UUID>() {
    override fun serialize(value: UUID, generator: JsonGenerator, serializer: SerializerProvider) {
        generator.writeString(value.toUndashedString())
    }
}

fun String.asUndashedStringToUuid() = UUID(BigInteger(substring(0, 16), 16).toLong(), BigInteger(substring(16, 32), 16).toLong())

/**
 * @author Kevin Ludwig
 */
object UuidDeserializer : JsonDeserializer<UUID>() {
    override fun deserialize(parser: JsonParser, context: DeserializationContext) = parser.readValueAsTree<JsonNode>().textValue().asUndashedStringToUuid()
}
