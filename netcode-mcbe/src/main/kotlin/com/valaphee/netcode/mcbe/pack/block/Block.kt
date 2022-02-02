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

package com.valaphee.netcode.mcbe.pack.block

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.valaphee.netcode.mc.util.nbt.Nbt
import com.valaphee.netcode.mc.util.nbt.compoundTag
import com.valaphee.netcode.mc.util.nbt.listTag
import com.valaphee.netcode.mc.util.nbt.ofBool
import com.valaphee.netcode.mc.util.nbt.toTag
import com.valaphee.netcode.mcbe.pack.ComponentsDeserializer
import com.valaphee.netcode.mcbe.pack.ComponentsSerializer
import com.valaphee.netcode.mcbe.pack.Data
import com.valaphee.netcode.mcbe.pack.DataType
import com.valaphee.netcode.mcbe.pack.DataTypeResolver

/**
 * @author Kevin Ludwig
 */
@DataType("minecraft:block")
class Block(
    @get:JsonProperty("description") val description: Description,
    @get:JsonProperty("events") val events: Map<String, Map<String, Any>>? = null,
    @get:JsonSerialize(using = ComponentsSerializer::class) @get:JsonDeserialize(using = ComponentsDeserializer::class) @get:JsonProperty("components") val components: List<BlockComponent>? = null,
    @get:JsonProperty("permutations") val permutations: List<Permutation>? = null
) : Data, Nbt {
    class Description(
        @get:JsonProperty("identifier") val key: String,
        @get:JsonProperty("properties") val properties: LinkedHashMap<String, LinkedHashSet<*>>? = null
    )

    class Permutation(
        @get:JsonProperty("condition") val condition: String,
        @get:JsonSerialize(using = ComponentsSerializer::class) @get:JsonDeserialize(using = ComponentsDeserializer::class) @get:JsonProperty("components") val components: List<BlockComponent>
    ) : Nbt {
        override fun toTag() = compoundTag().apply {
            setString("condition", condition)
            this["components"] = compoundTag().apply { components.forEach { this[DataTypeResolver.typeByClass(it::class)] = it.toTag() } }
        }
    }

    override fun toTag() = compoundTag().apply {
        setInt("molangVersion", 0)
        description.properties?.let {
            if (it.isNotEmpty()) this["properties"] = listTag().apply {
                description.properties.forEach {
                    put(compoundTag().apply {
                        setString("name", it.key)
                        this["enum"] = listTag().apply {
                            when (it.value.first()) {
                                is Boolean -> it.value.forEach { put(ofBool(it as Boolean)) }
                                is Int -> it.value.forEach { putInt(it as Int) }
                                is String -> it.value.forEach { putString(it as String) }
                                else -> TODO()
                            }
                        }
                    })
                }
            }
        }
        components?.let { this["components"] = compoundTag().apply { it.forEach { this[DataTypeResolver.typeByClass(it::class)] = it.toTag() } } }
        permutations?.let { if (permutations.isNotEmpty()) this["permutations"] = it.toTag() }
    }
}
