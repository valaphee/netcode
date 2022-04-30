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

package com.valaphee.netcode.mcbe.world.item.crafting

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeName
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.valaphee.netcode.mcbe.world.item.ItemStack
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
@JsonTypeName("minecraft:recipe_shaped")
class ShapedRecipe : Recipe {
    data class Description(
        @get:JsonProperty("identifier") val key: String
    )

    val description: Description
    val key: Map<Char, ItemStack>
    val pattern: List<String>
    @get:JsonIgnore var width: Int
    @get:JsonIgnore val height: Int
    @get:JsonIgnore var input: Array<ItemStack?>
    val output: List<ItemStack>
    @get:JsonIgnore val id: UUID
    val tags: List<String>
    val priority: Int
    @get:JsonIgnore val netId: Int

    @JsonCreator
    constructor(
        @JsonProperty("description") description: Description,
        @JsonProperty("key") @JsonSerialize(contentUsing = IngredientSerializer::class) @JsonDeserialize(contentUsing = IngredientDeserializer::class) key: Map<Char, ItemStack>,
        @JsonProperty("pattern") pattern: List<String>,
        @JsonProperty("result") output: List<ItemStack>,
        @JsonProperty("tags") tags: List<String>,
        @JsonProperty("priority") priority: Int = 0,
    ) {
        this.description = description
        this.key = key
        this.pattern = pattern
        var width = 0
        pattern.forEach {
            val patternRowWidth = it.length
            if (patternRowWidth > width) width = patternRowWidth
        }
        this.width = width
        this.height = pattern.size
        input = arrayOfNulls<ItemStack?>(width * height).apply { pattern.forEachIndexed { i, patternRow -> patternRow.forEachIndexed { j, patternColumn -> this[i * width + j] = key[patternColumn] } } }
        this.output = output
        id = UUID.randomUUID()
        this.tags = tags
        this.priority = priority
        netId = 0
    }

    constructor(id: UUID, key: String, width: Int, height: Int, input: Array<ItemStack?>, output: List<ItemStack>, tag: String, priority: Int, netId: Int) {
        this.description = Description(key)
        this.key = emptyMap()
        this.pattern = emptyList()
        this.width = width
        this.height = height
        this.input = input
        this.output = output
        this.id = id
        this.tags = listOf(tag)
        this.priority = priority
        this.netId = netId
    }
}
