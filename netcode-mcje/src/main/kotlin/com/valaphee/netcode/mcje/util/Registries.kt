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

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

/**
 * @author Kevin Ludwig
 */
class Registries(
    @get:JsonProperty("minecraft:sound_event") val sounds: Registry,
    @get:JsonProperty("minecraft:mob_effect") val effects: Registry,
    @get:JsonProperty("minecraft:block") val blocks: Registry,
    @get:JsonProperty("minecraft:enchantment") val enchantments: Registry,
    @get:JsonProperty("minecraft:entity_type") val entityTypes: Registry,
    @get:JsonProperty("minecraft:item") val items: Registry,
    @get:JsonProperty("minecraft:particle_type") val particleTypes: Registry,
    @get:JsonProperty("minecraft:motive") val motive: Registry,
    @get:JsonProperty("minecraft:menu") val windowTypes: Registry,
    @get:JsonProperty("minecraft:recipe_serializer") val recipeTypes: Registry,
) {
    @get:JsonIgnore lateinit var blockStates: com.valaphee.netcode.mcje.util.Registry<NamespacedKey>

    @JsonDeserialize(using = Registry.Deserializer::class)
    class Registry : com.valaphee.netcode.mcje.util.Registry<NamespacedKey>() {
        object Deserializer : JsonDeserializer<Registry>() {
            override fun deserialize(parser: JsonParser, context: DeserializationContext) = Registry().apply {
                val map = parser.readValueAs(Map::class.java)
                map["default"]?.let { this.default = minecraftKey(it as String) }
                (map["entries"] as Map<*, *>).forEach { this[(it.value as Map<*, *>)["protocol_id"] as Int] = minecraftKey(it.key as String) }
            }
        }
    }

    class Block(
        @get:JsonProperty("properties") val properties: Map<String, List<String>> = emptyMap(),
        @get:JsonProperty("states") val states: List<State>
    ) {
        class State(
            @get:JsonProperty("id") val id: Int,
            @get:JsonProperty("default") val default: Boolean = false,
            @get:JsonProperty("properties") val properties: Map<String, String> = emptyMap()
        )
    }
}
