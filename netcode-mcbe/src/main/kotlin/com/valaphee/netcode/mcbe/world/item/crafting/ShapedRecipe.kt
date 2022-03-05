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

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeName
import com.valaphee.netcode.mcbe.world.item.ItemStack
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
@JsonTypeName("minecraft:recipe_shaped")
data class ShapedRecipe(
    @JsonProperty("description") val description: Description,
    @JsonProperty("tags") val tags: List<String>,
    @JsonProperty("key") val key: Map<Char, ItemStack>,
    @JsonProperty("pattern") val pattern: List<String>,
    @JsonProperty("priority") val priority: Int = 0,
    @JsonProperty("result") val result: ItemStack,
    @JsonIgnore val id: UUID = UUID.randomUUID(),
    @JsonIgnore var netId: Int = 0
) : Recipe {
    class Description(
        @JsonProperty("identifier") val key: String
    )

    val width: Int
    val height = pattern.size
    val input: Array<ItemStack?>

    init {
        var width = 0
        pattern.forEach {
            val patternRowWidth = it.length
            if (patternRowWidth > width) width = patternRowWidth
        }
        this.width = width
        input = arrayOfNulls<ItemStack?>(width * height).apply { pattern.forEachIndexed { i, patternRow -> patternRow.forEachIndexed { j, patternColumn -> this[i * width + j] = key[patternColumn] } } }
    }
}
