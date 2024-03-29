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

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeName
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.jsontype.TypeSerializer
import com.valaphee.jackson.dataformat.nbt.NbtGenerator
import com.valaphee.netcode.mcbe.network.V1_18_010
import com.valaphee.netcode.mcbe.network.V1_19_010
import com.valaphee.netcode.mcbe.network.V1_19_020
import com.valaphee.netcode.mcbe.network.V1_19_030
import com.valaphee.netcode.mcbe.pack.Data

/**
 * @author Kevin Ludwig
 */
@JsonTypeName("minecraft:block")
@JsonSerialize(using = Block.Serializer::class)
class Block : Data {
    data class Description(
        @JsonProperty("identifier") val key: String,
        @JsonProperty("properties") val properties: Map<String, List<Any>>? = null
    )

    data class Permutation(
        @JsonProperty("condition") val condition: String,
        @JsonProperty("components") val components: Map<String, Any>
    )

    val description: Description
    val events: Map<String, Map<String, Any>>?
    val components: Map<String, Any>?
    val permutations: List<Permutation>?
    @get:JsonIgnore val states: List<BlockState>

    @JsonCreator
    constructor(
        @JsonProperty("description") description: Description,
        @JsonProperty("events") events: Map<String, Map<String, Any>>? = null,
        @JsonProperty("components") components: Map<String, Any>? = null,
        @JsonProperty("permutations") permutations: List<Permutation>? = null
    ) {
        this.description = description
        this.events = events
        this.components = components
        this.permutations = permutations
        this.states = description.properties?.values?.reversed()?.fold(listOf(listOf<Any>())) { acc, set -> acc.flatMap { list -> set.map { list + it } } }?.map { BlockState(description.key, description.properties.keys.zip(it.reversed()).toMap()) } ?: listOf(BlockState(description.key, emptyMap()))
    }

    constructor(key: String, states: List<Map<String, Any>>) {
        this.description = Description(key, emptyMap())
        events = null
        components = null
        permutations = null
        this.states = states.map { BlockState(description.key, it) }
    }

    object Serializer : JsonSerializer<Block>() {
        override fun serialize(value: Block, generator: JsonGenerator, provider: SerializerProvider) {
            when (generator) {
                is NbtGenerator -> {
                    generator.writeStartObject()
                    generator.writeNumberField("molangVersion", 0)
                    value.description.properties?.let { generator.writeObjectField("properties", it.map { mapOf("name" to it.key, "enum" to it.value) }) }
                    val version = provider.getAttribute("version") as Int? ?: V1_19_030
                    value.components?.let { generator.writeObjectField("components", serializeComponent(it, version)) }
                    value.permutations?.let { generator.writeObjectField("permutations", it.map { mapOf("condition" to it.condition, "components" to serializeComponent(it.components, version)) }) }
                    if (version >= V1_19_030) {
                        generator.writeObjectField("menu_category", value.components?.get("minecraft:creative_category")?.let {
                            mapOf("category" to (it as Map<*, *>)["category"] as String, "group" to it["group"] as String)
                        } ?: mapOf("category" to "", "group" to ""))
                    }
                    generator.writeEndObject()
                }
                else -> provider.defaultSerializeValue(value, generator)
            }
        }

        override fun serializeWithType(value: Block, generator: JsonGenerator, provider: SerializerProvider, typeSerializer: TypeSerializer) {
            when (generator) {
                is NbtGenerator -> serialize(value, generator, provider)
                else -> {
                    typeSerializer.writeTypePrefix(generator, typeSerializer.typeId(value, JsonToken.VALUE_STRING))
                    serialize(value, generator, provider)
                    typeSerializer.writeTypeSuffix(generator, typeSerializer.typeId(value, JsonToken.VALUE_STRING))
                }
            }
        }

        private fun serializeComponent(value: Map<String, Any?>, version: Int) = value.entries.mapNotNull { (componentKey, component) ->
            when (componentKey) {
                "minecraft:selection_box", "minecraft:aim_collision", "minecraft:pick_collision"     -> (if (version >= V1_19_020) "minecraft:selection_box" else if (version >= V1_18_010) "minecraft:aim_collision" else "minecraft:pick_collision") to mapOf("enabled" to true, "origin" to (component as Map<*, *>)["origin"].let { listOf(((it as List<*>)[0] as Number).toFloat(), (it[1] as Number).toFloat(), (it[2] as Number).toFloat()) }, "size" to component["size"].let { listOf(((it as List<*>)[0] as Number).toFloat(), (it[1] as Number).toFloat(), (it[2] as Number).toFloat()) })
                "minecraft:block_light_emission"                                                     -> "minecraft:block_light_emission" to mapOf("value" to (component as Number).toFloat())
                "minecraft:block_light_filter", "minecraft:block_light_absorption"                   -> if (version >= V1_18_010) "minecraft:block_light_filter" to mapOf("lightLevel" to (component as Number).toByte()) else "minecraft:block_light_absorption" to mapOf("value" to (component as Number).toFloat())
                "minecraft:collision_box", "minecraft:block_collision", "minecraft:entity_collision" -> (if (version >= V1_19_010) "minecraft:collision_box" else if (version >= V1_18_010) "minecraft:block_collision" else "minecraft:entity_collision") to mapOf("enabled" to true, "origin" to (component as Map<*, *>)["origin"].let { listOf(((it as List<*>)[0] as Number).toFloat(), (it[1] as Number).toFloat(), (it[2] as Number).toFloat()) }, "size" to component["size"].let { listOf(((it as List<*>)[0] as Number).toFloat(), (it[1] as Number).toFloat(), (it[2] as Number).toFloat()) })
                "minecraft:creative_category"                                                        -> if (version >= V1_19_030) null else "minecraft:creative_category" to mapOf("category" to (component as Map<*, *>)["category"] as String, "group" to component["group"] as String)
                "minecraft:destructible_by_mining", "minecraft:destroy_time"                         -> (if (version >= V1_19_020) "minecraft:destructible_by_mining" else "minecraft:destroy_time") to mapOf("value" to (component as Number).toFloat()) // destructible_by_mining
                "minecraft:destructible_by_explosion", "minecraft:explosion_resistance"              -> (if (version >= V1_19_020) "minecraft:destructible_by_explosion" else "minecraft:explosion_resistance") to mapOf("value" to (component as Number).toFloat())
                "minecraft:friction"                                                                 -> "minecraft:friction" to mapOf("value" to (component as Number).toFloat())
                "minecraft:geometry"                                                                 -> "minecraft:geometry" to mapOf("value" to component as String)
                "minecraft:map_color"                                                                -> "minecraft:map_color" to mapOf("value" to component as String)
                "minecraft:material_instances"                                                       -> "minecraft:material_instances" to mapOf("mappings" to mapOf<String, Any?>(), "materials" to (component as Map<*, *>).mapValues { (_, componentMaterial) -> mapOf("texture" to (componentMaterial as Map<*, *>)["texture"] as String, "render_method" to componentMaterial["render_method"] as String, "face_dimming" to componentMaterial["face_dimming"] as Boolean, "ambient_occlusion" to componentMaterial["ambient_occlusion"] as Boolean) })
                "minecraft:on_player_placing"                                                        -> "minecraft:on_player_placing" to mapOf("triggerType" to (component as Map<*, *>)["event"] as String)
                "minecraft:rotation"                                                                 -> "minecraft:rotation" to mapOf("x" to ((component as List<*>)[0] as Number).toFloat(), "y" to (component[1] as Number).toFloat(), "z" to (component[2] as Number).toFloat())
                else                                                                                 -> null
            }
        }.toMap()
    }
}
