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

import com.valaphee.netcode.mcbe.world.item.ItemStack
import java.util.UUID

fun recipeId(type: Recipe.Type, inputs: List<ItemStack?>, outputs: List<ItemStack?>): UUID = UUID.nameUUIDFromBytes("${type.ordinal}${if (type == Recipe.Type.Shapeless || type == Recipe.Type.ShapelessChemistry) inputs.filterNotNull().sortedBy { it.itemKey + it.subId + it.count }.joinToString { it.itemKey + it.subId + it.count } else inputs.joinToString { it?.let { it.itemKey + it.subId + it.count } ?: "" }}${outputs.joinToString { it?.let { it.itemKey + it.subId + it.count } ?: "" }}}".toByteArray())

fun shapelessRecipe(type: Recipe.Type, name: String, inputs: List<ItemStack?>, outputs: List<ItemStack?>, tag: String, priority: Int, netId: Int) = Recipe(recipeId(type, inputs, outputs), name, type, 0, 0, inputs, outputs, tag, priority, netId)

fun shapedRecipe(type: Recipe.Type, name: String, width: Int, height: Int, inputs: List<ItemStack?>, outputs: List<ItemStack?>, tag: String, priority: Int, netId: Int) = Recipe(recipeId(type, inputs, outputs), name, type, width, height, inputs, outputs, tag, priority, netId)

fun furnaceRecipe(input: ItemStack, output: ItemStack?, tag: String) = Recipe(null, null, if (input.subId != -1) Recipe.Type.FurnaceData else Recipe.Type.Furnace, 0, 0, listOf(input), listOf(output), tag, 0, 0)

fun multiRecipe(id: UUID, netId: Int) = Recipe(id, null, Recipe.Type.Multi, 0, 0, null, null, null, 0, netId)

/**
 * @author Kevin Ludwig
 */
data class Recipe(
    val id: UUID?,
    val name: String?,
    val type: Type,
    val width: Int,
    val height: Int,
    val inputs: List<ItemStack?>?,
    val outputs: List<ItemStack?>?,
    val tag: String?,
    val priority: Int,
    val netId: Int
) {

    enum class Type {
        Shapeless,
        Shaped,
        Furnace,
        FurnaceData,
        Multi,
        ShulkerBox,
        ShapelessChemistry,
        ShapedChemistry
    }
}
