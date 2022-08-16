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
import com.valaphee.netcode.mcbe.network.Restrict
import com.valaphee.netcode.mcbe.network.Restriction
import com.valaphee.netcode.mcbe.network.V1_16_010
import com.valaphee.netcode.mcbe.network.V1_17_034
import com.valaphee.netcode.mcbe.world.item.Item
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
import com.valaphee.netcode.mcbe.world.item.readItemStack
import com.valaphee.netcode.mcbe.world.item.writeIngredient
import com.valaphee.netcode.mcbe.world.item.writeItemStack
import com.valaphee.netcode.util.LazyList
import kotlin.reflect.jvm.jvmName

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class RecipesPacket(
    val recipes: List<Recipe>,
    val brewingMixRecipes: List<BrewingMixRecipe>,
    val brewingContainerRecipes: List<BrewingContainerRecipe>,
    val materialReductionRecipes: List<MaterialReductionRecipe>,
    val reset: Boolean
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
                    it.input.forEach { buffer.writeIngredient(it, version) }
                    buffer.writeVarUInt(1)
                    buffer.writeItemStack(it.output, version, false)
                    buffer.writeUuid(it.id)
                    buffer.writeString(it.tags.single())
                    buffer.writeVarInt(it.priority)
                    if (version >= V1_16_010) buffer.writeVarUInt(it.netId)
                }
                is ShapedRecipe -> {
                    buffer.writeVarInt(1)
                    buffer.writeString(it.description.key)
                    buffer.writeVarInt(it.width)
                    buffer.writeVarInt(it.height)
                    it.input.forEach { buffer.writeIngredient(it, version) }
                    buffer.writeVarUInt(it.output.size)
                    it.output.forEach { buffer.writeItemStack(it, version, false) }
                    buffer.writeUuid(it.id)
                    buffer.writeString(it.tags.single())
                    buffer.writeVarInt(it.priority)
                    if (version >= V1_16_010) buffer.writeVarUInt(it.netId)
                }
                is FurnaceRecipe -> {
                    if (it.input.subId == -1) {
                        buffer.writeVarInt(2)
                        buffer.writeVarInt(it.input.itemId ?: Item[version, checkNotNull(it.input.itemKey) { "Neither item id nor key specified: ${it.input}" }])
                    } else {
                        buffer.writeVarInt(3)
                        buffer.writeVarInt(it.input.itemId ?: Item[version, checkNotNull(it.input.itemKey) { "Neither item id nor key specified: ${it.input}" }])
                        buffer.writeVarInt(it.input.subId)
                    }
                    buffer.writeItemStack(it.output, version, false)
                    buffer.writeString(it.tags.single())
                }
                is MultiRecipe -> {
                    buffer.writeVarInt(4)
                    buffer.writeUuid(it.id)
                    buffer.writeVarUInt(it.netId)
                }
                else -> error("Unsupported recipe type: ${it::class.jvmName}")
            }
        }

        buffer.writeVarUInt(brewingMixRecipes.size)
        brewingMixRecipes.forEach {
            buffer.writeVarInt(it.input.itemId ?: Item[version, checkNotNull(it.input.itemKey) { "Neither item id nor key specified: ${it.input}" }])
            if (version >= V1_16_010) buffer.writeVarInt(it.input.subId)
            buffer.writeVarInt(it.reagent.itemId ?: Item[version, checkNotNull(it.reagent.itemKey) { "Neither item id nor key specified: ${it.reagent}" }])
            if (version >= V1_16_010) buffer.writeVarInt(it.reagent.subId)
            buffer.writeVarInt(it.output.itemId ?: Item[version, checkNotNull(it.output.itemKey) { "Neither item id nor key specified: ${it.output}" }])
            if (version >= V1_16_010) buffer.writeVarInt(it.output.subId)
        }
        buffer.writeVarUInt(brewingContainerRecipes.size)
        brewingContainerRecipes.forEach {
            buffer.writeVarInt(Item[version, it.input])
            buffer.writeVarInt(Item[version, it.reagent])
            buffer.writeVarInt(Item[version, it.output])
        }
        if (version >= V1_17_034) {
            buffer.writeVarUInt(materialReductionRecipes.size)
            materialReductionRecipes.forEach {
                buffer.writeVarInt(it.input.itemId ?: Item[version, checkNotNull(it.input.itemKey) { "Neither item id nor key specified: ${it.input}" }])
                buffer.writeVarUInt(it.output.size)
                it.output.forEach {
                    buffer.writeVarInt(it.itemId ?: Item[version, checkNotNull(it.itemKey) { "Neither item id nor key specified: $it" }])
                    buffer.writeVarInt(it.count)
                }
            }
        }
        buffer.writeBoolean(reset)
    }

    override fun handle(handler: PacketHandler) = handler.recipes(this)

    override fun toString() = "RecipesPacket(recipes=$recipes, brewingMixRecipes=$brewingMixRecipes, brewingContainerRecipes=$brewingContainerRecipes, materialReductionRecipes=$materialReductionRecipes, reset=$reset)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = RecipesPacket(
            LazyList(buffer.readVarUInt()) {
                when (val type = buffer.readVarInt()) {
                    0, 5, 6 -> ShapelessRecipe(ShapelessRecipe.Description(buffer.readString()), LazyList(buffer.readVarUInt()) { buffer.readIngredient(version)!! }, LazyList(buffer.readVarUInt()) { buffer.readItemStack(version, false)!! }.single(), buffer.readUuid(), listOf(buffer.readString()), buffer.readVarInt(), if (version >= V1_16_010) buffer.readVarUInt() else 0)
                    1, 7 -> {
                        val key = buffer.readString()
                        val width = buffer.readVarInt()
                        val height = buffer.readVarInt()
                        val input = LazyList(width * height) { buffer.readIngredient(version) }
                        val output = LazyList(buffer.readVarUInt()) { buffer.readItemStack(version, false)!! }
                        val id = buffer.readUuid()
                        val tag = buffer.readString()
                        val priority = buffer.readVarInt()
                        val netId = if (version >= V1_16_010) buffer.readVarUInt() else 0
                        ShapedRecipe(id, key, width, height, input.toTypedArray(), output, tag, priority, netId)
                    }
                    2 -> {
                        val input = buffer.readVarInt()
                        val output = checkNotNull(buffer.readItemStack(version, false)) { "Furnace recipe has no output. ($input)" }
                        val tag = buffer.readString()
                        FurnaceRecipe(FurnaceRecipe.Description(""), ItemStack(input, Item[version, input]!!, -1), output, listOf(tag))
                    }
                    3 -> {
                        val inputId = buffer.readVarInt()
                        val inputSubId = buffer.readVarInt()
                        val output = checkNotNull(buffer.readItemStack(version, false)) { "Furnace recipe has no output. ($inputId:$inputSubId)" }
                        val tag = buffer.readString()
                        FurnaceRecipe(FurnaceRecipe.Description(""), ItemStack(inputId, Item[version, inputId]!!, inputSubId), output, listOf(tag))
                    }
                    4 -> MultiRecipe(buffer.readUuid(), buffer.readVarUInt())
                    else -> error("No such recipe type: $type")
                }
            },
            LazyList(buffer.readVarUInt()) {
                val inputId = buffer.readVarInt()
                val inputSubId = if (version >= V1_16_010) buffer.readVarInt() else -1
                val reagentId = buffer.readVarInt()
                val reagentSubId = if (version >= V1_16_010) buffer.readVarInt() else -1
                val outputId = buffer.readVarInt()
                val outputSubId = if (version >= V1_16_010) buffer.readVarInt() else -1
                BrewingMixRecipe(BrewingMixRecipe.Description(""), emptyList(), ItemStack(inputId, Item[version, inputId]!!, inputSubId), ItemStack(reagentId, Item[version, reagentId]!!, reagentSubId), ItemStack(outputId, Item[version, outputId]!!, outputSubId))
            },
            LazyList(buffer.readVarUInt()) { BrewingContainerRecipe(BrewingContainerRecipe.Description(""), emptyList(), Item[version, buffer.readVarInt()]!!, Item[version, buffer.readVarInt()]!!, Item[version, buffer.readVarInt()]!!) },
            if (version >= V1_17_034) LazyList(buffer.readVarUInt()) {
                val input = buffer.readVarInt()
                MaterialReductionRecipe(MaterialReductionRecipe.Description(""), emptyList(), ItemStack(input, Item[version, input]!!, -1), LazyList(buffer.readVarUInt()) {
                    val outputId = buffer.readVarInt()
                    val outputSubId = buffer.readVarInt()
                    ItemStack(outputId, Item[version, outputId]!!, outputSubId)
                })
            } else emptyList(),
            buffer.readBoolean()
        )
    }
}
