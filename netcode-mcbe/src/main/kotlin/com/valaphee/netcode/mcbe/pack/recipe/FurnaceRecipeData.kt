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
import com.valaphee.netcode.mcbe.world.item.craft.furnaceRecipe

/**
 * @author Kevin Ludwig
 */
@DataType("minecraft:recipe_furnace")
class FurnaceRecipeData(
    @get:JsonProperty("description") val description: Description,
    @get:JsonProperty("tags") val tags: Array<String>,
    @get:JsonProperty("input") val input: ItemStack,
    @get:JsonProperty("output") val output: ItemStack
) : RecipeData {
    class Description(
        @get:JsonProperty("identifier") val key: String
    )

    override fun toRecipe(netId: Int) = furnaceRecipe(input, output, tags.first())
}
