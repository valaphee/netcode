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

package com.valaphee.netcode.mcbe.network.packet

import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.PacketReader
import com.valaphee.netcode.mcbe.network.Restrict
import com.valaphee.netcode.mcbe.network.Restriction
import com.valaphee.netcode.mcbe.world.item.ItemStack
import com.valaphee.netcode.mcbe.world.item.crafting.BrewingContainerRecipe
import com.valaphee.netcode.mcbe.world.item.crafting.BrewingMixRecipe
import com.valaphee.netcode.mcbe.world.item.crafting.FurnaceRecipe
import com.valaphee.netcode.mcbe.world.item.crafting.MaterialReductionRecipe
import com.valaphee.netcode.mcbe.world.item.crafting.MultiRecipe
import com.valaphee.netcode.mcbe.world.item.crafting.Recipe
import com.valaphee.netcode.mcbe.world.item.crafting.ShapedRecipe
import com.valaphee.netcode.mcbe.world.item.crafting.ShapelessRecipe
import com.valaphee.netcode.mcbe.world.item.readIngredient
import com.valaphee.netcode.mcbe.world.item.readItemStackInstance
import com.valaphee.netcode.mcbe.world.item.readItemStackPre431
import com.valaphee.netcode.mcbe.world.item.writeIngredient
import com.valaphee.netcode.mcbe.world.item.writeItemStackInstance
import com.valaphee.netcode.mcbe.world.item.writeItemStackPre431
import com.valaphee.netcode.util.safeList

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class RecipesPacket(
    val recipes: List<Recipe>,
    val brewingMixRecipes: List<BrewingMixRecipe>,
    val brewingContainerRecipes: List<BrewingContainerRecipe>,
    val materialReductionRecipes: List<MaterialReductionRecipe>,
    val cleanRecipes: Boolean
) : Packet() {
    override val id get() = 0x34

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarUInt(recipes.size)
        recipes.forEach {
            when (it) {
                is ShapelessRecipe -> {
                    buffer.writeVarInt(0)
                    buffer.writeString(it.description.key)
                    buffer.writeVarUInt(it.input.size)
                    it.input.forEach(buffer::writeIngredient)
                    buffer.writeVarUInt(1)
                    if (version >= 431) buffer.writeItemStackInstance(it.output) else buffer.writeItemStackPre431(it.output)
                    buffer.writeUuid(it.id)
                    buffer.writeString(it.tags.single())
                    buffer.writeVarInt(it.priority)
                    if (version >= 407) buffer.writeVarUInt(it.netId)
                }
                is ShapedRecipe -> {
                    buffer.writeVarInt(1)
                    buffer.writeString(it.description.key)
                    buffer.writeVarInt(it.width)
                    buffer.writeVarInt(it.height)
                    it.input.forEach(buffer::writeIngredient)
                    buffer.writeVarUInt(it.output.size)
                    it.output.forEach { if (version >= 431) buffer.writeItemStackInstance(it) else buffer.writeItemStackPre431(it) }
                    buffer.writeUuid(it.id)
                    buffer.writeString(it.tags.single())
                    buffer.writeVarInt(it.priority)
                    if (version >= 407) buffer.writeVarUInt(it.netId)
                }
                is FurnaceRecipe -> {
                    if (it.input.subId == -1) {
                        buffer.writeVarInt(2)
                        buffer.writeVarInt(buffer.registries.items.getId(it.input.item))
                    } else {
                        buffer.writeVarInt(3)
                        buffer.writeVarInt(buffer.registries.items.getId(it.input.item))
                        buffer.writeVarInt(it.input.subId)
                    }
                    if (version >= 431) buffer.writeItemStackInstance(it.output) else buffer.writeItemStackPre431(it.output)
                    buffer.writeString(it.tags.single())
                }
                is MultiRecipe -> {
                    buffer.writeVarInt(4)
                    buffer.writeUuid(it.id)
                    buffer.writeVarUInt(it.netId)
                }
                else -> TODO("$it")
            }
        }

        buffer.writeVarUInt(brewingMixRecipes.size)
        brewingMixRecipes.forEach {
            buffer.writeVarInt(buffer.registries.items.getId(it.input.item))
            if (version >= 407) buffer.writeVarInt(it.input.subId)
            buffer.writeVarInt(buffer.registries.items.getId(it.reagent.item))
            if (version >= 407) buffer.writeVarInt(it.reagent.subId)
            buffer.writeVarInt(buffer.registries.items.getId(it.output.item))
            if (version >= 407) buffer.writeVarInt(it.output.subId)
        }
        buffer.writeVarUInt(brewingContainerRecipes.size)
        brewingContainerRecipes.forEach {
            buffer.writeVarInt(buffer.registries.items.getId(it.input))
            buffer.writeVarInt(buffer.registries.items.getId(it.reagent))
            buffer.writeVarInt(buffer.registries.items.getId(it.output))
        }
        if (version >= 465) {
            buffer.writeVarUInt(materialReductionRecipes.size)
            materialReductionRecipes.forEach {
                buffer.writeVarInt(buffer.registries.items.getId(it.input.item))
                buffer.writeVarUInt(it.output.size)
                it.output.forEach {
                    buffer.writeVarInt(buffer.registries.items.getId(it.item))
                    buffer.writeVarInt(it.count)
                }
            }
        }
        buffer.writeBoolean(cleanRecipes)
    }

    override fun handle(handler: PacketHandler) = handler.recipes(this)

    override fun toString() = "RecipesPacket(recipes=$recipes, brewingMixRecipes=$brewingMixRecipes, brewingContainerRecipes=$brewingContainerRecipes, materialReductionRecipes=$materialReductionRecipes, cleanRecipes=$cleanRecipes)"
}

/**
 * @author Kevin Ludwig
 */
object RecipesPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = RecipesPacket(
        safeList(buffer.readVarUInt()) {
            when (val type = buffer.readVarInt()) {
                0, 5, 6 -> ShapelessRecipe(ShapelessRecipe.Description(buffer.readString()), safeList(buffer.readVarUInt()) { checkNotNull(buffer.readIngredient()) }, safeList(buffer.readVarUInt()) { checkNotNull(if (version >= 431) buffer.readItemStackInstance() else buffer.readItemStackPre431()) }.single(), buffer.readUuid(), listOf(buffer.readString()), buffer.readVarInt(), if (version >= 407) buffer.readVarUInt() else 0)
                1, 7 -> {
                    val name = buffer.readString()
                    val width = buffer.readVarInt()
                    val height = buffer.readVarInt()
                    val input = safeList(width * height) { buffer.readIngredient() }
                    val output = safeList(buffer.readVarUInt()) { checkNotNull(if (version >= 431) buffer.readItemStackInstance() else buffer.readItemStackPre431()) }
                    val id = buffer.readUuid()
                    val tag = buffer.readString()
                    val priority = buffer.readVarInt()
                    val netId = if (version >= 407) buffer.readVarUInt() else 0
                    ShapedRecipe(ShapedRecipe.Description(name), emptyMap(), emptyList(), output, id, listOf(tag), priority, netId)
                }
                2 -> FurnaceRecipe(FurnaceRecipe.Description(""), ItemStack(checkNotNull(buffer.registries.items[buffer.readVarInt()]), -1), checkNotNull(if (version >= 431) buffer.readItemStackInstance() else buffer.readItemStackPre431()), listOf(buffer.readString()))
                3 -> FurnaceRecipe(FurnaceRecipe.Description(""), ItemStack(checkNotNull(buffer.registries.items[buffer.readVarInt()]), buffer.readVarInt()), checkNotNull(if (version >= 431) buffer.readItemStackInstance() else buffer.readItemStackPre431()), listOf(buffer.readString()))
                4 -> MultiRecipe(buffer.readUuid(), buffer.readVarUInt())
                else -> TODO("$type")
            }
        },
        safeList(buffer.readVarUInt()) { BrewingMixRecipe(BrewingMixRecipe.Description(""), emptyList(), ItemStack(buffer.registries.items[buffer.readVarInt()] ?: "minecraft:unknown", buffer.readVarInt()), ItemStack(buffer.registries.items[buffer.readVarInt()] ?: "minecraft:unknown", buffer.readVarInt()), ItemStack(buffer.registries.items[buffer.readVarInt()] ?: "minecraft:unknown", buffer.readVarInt())) },
        safeList(buffer.readVarUInt()) { BrewingContainerRecipe(BrewingContainerRecipe.Description(""), emptyList(), checkNotNull(buffer.registries.items[buffer.readVarInt()]), checkNotNull(buffer.registries.items[buffer.readVarInt()]), checkNotNull(buffer.registries.items[buffer.readVarInt()])) },
        if (version >= 465) safeList(buffer.readVarUInt()) { MaterialReductionRecipe(MaterialReductionRecipe.Description(""), emptyList(), ItemStack(checkNotNull(buffer.registries.items[buffer.readVarInt()])), safeList(buffer.readVarUInt()) { ItemStack(checkNotNull(buffer.registries.items[buffer.readVarInt()]), buffer.readVarInt()) }) } else emptyList(),
        buffer.readBoolean()
    )
}
