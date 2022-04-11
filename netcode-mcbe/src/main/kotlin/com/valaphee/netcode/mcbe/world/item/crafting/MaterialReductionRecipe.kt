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

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeName
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.valaphee.netcode.mcbe.pack.Data
import com.valaphee.netcode.mcbe.world.item.ItemStack

/**
 * @author Kevin Ludwig
 */
@JsonTypeName("minecraft:recipe_material_reduction")
data class MaterialReductionRecipe(
    @get:JsonProperty("description") val description: Description,
    @get:JsonProperty("tags") val tags: List<String>,
    @get:JsonProperty("input") @get:JsonSerialize(using = SingleItemSerializer::class) @get:JsonDeserialize(using = SingleItemDeserializer::class) val input: ItemStack,
    @get:JsonProperty("output") val output: List<ItemStack>,
) : Data {
    data class Description(
        @get:JsonProperty("identifier") val key: String
    )
}
