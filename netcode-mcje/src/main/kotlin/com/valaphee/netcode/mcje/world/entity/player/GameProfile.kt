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

package com.valaphee.netcode.mcje.world.entity.player

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.valaphee.jackson.dataformat.nbt.NbtGenerator
import com.valaphee.netcode.mcje.util.jackson.UuidDeserializer
import com.valaphee.netcode.mcje.util.jackson.UuidSerializer
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
@JsonSerialize(using = GameProfile.Serializer::class)
/*@JsonDeserialize(using = GameProfile.Deserializer::class)*/
data class GameProfile(
    @JsonProperty("id") @JsonSerialize(using = UuidSerializer::class) @JsonDeserialize(using = UuidDeserializer::class) val userId: UUID?,
    @JsonProperty("name") val userName: String?,
    @JsonProperty("properties") val properties: List<Property>? = null
) {
    data class Property(
        @JsonProperty("name") val name: String,
        @JsonProperty("value") val value: String,
        @JsonProperty("signature") val signature: String? = null
    ) {
        data class Textures(
            @JsonProperty("timestamp") val timestamp: Long,
            @JsonProperty("profileId") @JsonSerialize(using = UuidSerializer::class) @JsonDeserialize(using = UuidDeserializer::class) val userId: UUID,
            @JsonProperty("profileName") val userName: String,
            @JsonProperty("textures") val textures: Map<String, Texture>
        ) {
            data class Texture(
                @JsonProperty("url") val url: String,
                @JsonProperty("metadata") val metadata: Map<String, String>?
            )
        }
    }

    object Serializer : JsonSerializer<GameProfile>() {
        override fun serialize(value: GameProfile, generator: JsonGenerator, provider: SerializerProvider) {
            when (generator) {
                is NbtGenerator -> {
                    generator.writeStartObject()
                    value.userName?.let { generator.writeStringField("Name", it) }
                    generator.writeEndObject()
                }
                else -> provider.defaultSerializeValue(value, generator)
            }
        }
    }

    /*object Deserializer : JsonDeserializer<GameProfile>() {
        override fun deserialize(parser: JsonParser, context: DeserializationContext): GameProfile = when (parser) {
            is NbtParser -> {
                val node = parser.readValueAsTree<JsonNode>()
                if (node.isTextual) GameProfile(null, node.asText())
                else GameProfile(node["Id"]?.let {
                    val value = context.readTreeAsValue(it, IntArray::class.java)
                    UUID((value[0].toLong() shl 32) or (value[1].toLong() and 0xFFFFFFFF), (value[2].toLong() shl 32) or (value[3].toLong() and 0xFFFFFFFF))
                }, node["Name"]?.textValue(), node["Properties"]?.fields()?.asSequence()?.flatMap {
                    val key = it.key
                    it.value.map { Property(key, it["Value"].textValue(), it["Signature"].textValue()) }
                }?.toList())
            }
            else -> parser.readValueAs(GameProfile::class.java)
        }
    }*/
}
