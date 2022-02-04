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

package com.valaphee.netcode.mcbe.pack.recipe

import com.fasterxml.jackson.annotation.JsonProperty
import com.valaphee.netcode.mcbe.pack.DataType
import com.valaphee.netcode.mcbe.world.item.ItemStack
import com.valaphee.netcode.mcbe.world.item.craft.Recipe
import com.valaphee.netcode.mcbe.world.item.craft.shapedRecipe

/**
 * @author Kevin Ludwig
 */
@DataType("minecraft:recipe_shaped")
class ShapedRecipeData(
    @get:JsonProperty("description") val description: Description,
    @get:JsonProperty("tags") val tags: List<String>,
    @get:JsonProperty("key") val key: Map<Char, ItemStack>,
    @get:JsonProperty("pattern") val pattern: List<String>,
    @get:JsonProperty("priority") val priority: Int = 0,
    @get:JsonProperty("result") val result: ItemStack
) : RecipeData {
    class Description(
        @get:JsonProperty("identifier") val key: String
    )

    override fun toRecipe(netId: Int): Recipe {
        val height = pattern.size
        var width = 0
        pattern.forEach {
            val patternRowWidth = it.length
            if (patternRowWidth > width) width = patternRowWidth
        }
        val inputs = arrayOfNulls<ItemStack?>(width * height)
        pattern.forEachIndexed { i, patternRow -> patternRow.forEachIndexed { j, patternColumn -> inputs[i * width + j] = key[patternColumn] } }
        return shapedRecipe(Recipe.Type.Shaped, description.key, width, height, inputs.toList(), listOf(result), tags.first(), priority, netId)
    }
}
