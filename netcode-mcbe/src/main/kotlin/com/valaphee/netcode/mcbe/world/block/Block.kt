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

package com.valaphee.netcode.mcbe.world.block

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeName
import com.valaphee.foundry.math.Float3
import com.valaphee.netcode.mcbe.pack.Data

/**
 * @author Kevin Ludwig
 */
@JsonTypeName("minecraft:block")
class Block : Data {
    @get:JsonProperty("description") val description: Description
    @get:JsonProperty("events") val events: Map<String, Map<String, Any>>?
    @get:JsonProperty("components") val components: Map<String, Any>?
    @get:JsonProperty("permutations") val permutations: List<Permutation>?
    @get:JsonIgnore val data: Data

    class Description(
        @get:JsonProperty("identifier") val key: String,
        @get:JsonProperty("properties") val properties: Map<String, List<*>>? = null
    )

    class Permutation(
        @get:JsonProperty("condition") val condition: String,
        @get:JsonProperty("components") val components: Map<String, Any>
    )

    class Data(
        @get:JsonProperty("molangVersion") val molangVersion: Int,
        @get:JsonProperty("properties") val properties: List<Property>? = null,
        @get:JsonProperty("components") val components: Map<String, Map<String, Any>>? = null,
        @get:JsonProperty("permutations") val permutations: List<Permutation>? = null
    ) {
        class Property(
            @get:JsonProperty("name") val name: String,
            @get:JsonProperty("enum") val enum: List<*>
        )
    }

    constructor(description: Description, events: Map<String, Map<String, Any>>? = null, components: Map<String, Any>? = null, permutations: List<Permutation>? = null) {
        this.description = description
        this.events = events
        this.components = components
        this.permutations = permutations
        this.data = Data(0, description.properties?.map { Data.Property(it.key, it.value) })
    }

    constructor(key: String, data: Data) {
        this.description = Description(key, data.properties?.associate { it.name to it.enum })
        events = null
        components = data.components?.mapNotNull {
            when (it.key) {
                "minecraft:block_light_absorption", "minecraft:block_light_emission", "minecraft:destroy_time", "minecraft:explosion_resistance", "minecraft:friction" -> it.key to it.value["value"] as Float
                "minecraft:creative_category" -> it.key to it.value
                "minecraft:entity_collision", "minecraft:pick_collision" -> it.key to it.value.let { mapOf("enabled" to true, "origin" to it["origin"], "size" to it["size"]) }
                "minecraft:geometry", "minecraft:map_color" -> it.key to it.value["value"] as String
                "minecraft:material_instances" -> it.key to mapOf("mappings" to emptyMap(), "materials" to it.value)
                "minecraft:on_player_placing" ->  it.key to mapOf("event" to it.value["triggerType"])
                "minecraft:rotation" -> it.key to Float3(it.value["x"] as Float, it.value["y"] as Float, it.value["z"]  as Float)
                else -> null
            }
        }?.toMap()
        permutations = null
        this.data = data
    }
}
