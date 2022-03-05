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
    class Description(
        @JsonProperty("identifier") val key: String,
        @JsonProperty("properties") val properties: Map<String, List<Any>>? = null
    )

    class Permutation(
        @JsonProperty("condition") val condition: String,
        @JsonProperty("components") val components: Map<String, Any>
    )

    class Data(
        @JsonProperty("molangVersion") val molangVersion: Int,
        @JsonProperty("properties") val properties: List<Property>? = null,
        @JsonProperty("components") val components: Map<String, Map<String, Any>>? = null,
        @JsonProperty("permutations") val permutations: List<Permutation>? = null
    ) {
        class Property(
            @JsonProperty("name") val name: String,
            @JsonProperty("enum") val enum: List<Any>
        )
    }

    @JsonProperty("description") val description: Description
    @JsonProperty("events") val events: Map<String, Map<String, Any>>?
    @JsonProperty("components") val components: Map<String, Any>?
    @JsonProperty("permutations") val permutations: List<Permutation>?
    @JsonIgnore val states: List<BlockState>
    @JsonIgnore val data: Data

    constructor(description: Description, events: Map<String, Map<String, Any>>? = null, components: Map<String, Any>? = null, permutations: List<Permutation>? = null) {
        this.description = description
        this.events = events
        this.components = components
        this.permutations = permutations
        this.states = description.properties?.values?.reversed()?.fold(listOf(listOf<Any>())) { acc, set -> acc.flatMap { list -> set.map { list + it } } }?.map { BlockState(description.key, description.properties.keys.zip(it.reversed()).toMap()) } ?: listOf(BlockState(description.key, emptyMap()))
        this.data = Data(0, description.properties?.map { Data.Property(it.key, it.value) })
    }

    constructor(key: String, states: List<Map<String, Any>>) {
        this.description = Description(key, emptyMap())
        events = null
        components = null
        permutations = null
        this.states = states.map { BlockState(description.key, it) }
        this.data = Data(0, description.properties?.map { Data.Property(it.key, it.value) })
    }

    constructor(key: String, data: Data) {
        description = Description(key, data.properties?.associate { it.name to it.enum })
        events = null
        components = data.components?.mapNotNull {
            when (it.key) {
                "minecraft:block_light_absorption", "minecraft:block_light_emission", "minecraft:destroy_time", "minecraft:explosion_resistance", "minecraft:friction" -> it.key to it.value["value"] as Float
                "minecraft:creative_category" -> it.key to it.value
                "minecraft:entity_collision", "minecraft:pick_collision" -> it.key to it.value.let { mapOf("origin" to it["origin"], "size" to it["size"]) }
                "minecraft:geometry", "minecraft:map_color" -> it.key to it.value["value"] as String
                "minecraft:material_instances" -> it.key to mapOf("mappings" to emptyMap(), "materials" to it.value)
                "minecraft:on_player_placing" -> it.key to mapOf("event" to it.value["triggerType"])
                "minecraft:rotation" -> it.key to Float3(it.value["x"] as Float, it.value["y"] as Float, it.value["z"] as Float)
                else -> null
            }
        }?.toMap()
        permutations = null
        this.states = description.properties?.values?.reversed()?.fold(listOf(listOf<Any>())) { acc, set -> acc.flatMap { list -> set.map { list + it } } }?.map { BlockState(description.key, description.properties!!.keys.zip(it.reversed()).toMap()) } ?: listOf(BlockState(description.key, emptyMap()))
        this.data = data
    }
}
