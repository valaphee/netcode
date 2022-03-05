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
import com.valaphee.netcode.mcbe.world.item.crafting.ContainerMixRecipe
import com.valaphee.netcode.mcbe.world.item.crafting.MaterialReducer
import com.valaphee.netcode.mcbe.world.item.crafting.MultiRecipe
import com.valaphee.netcode.mcbe.world.item.crafting.PotionMixRecipe
import com.valaphee.netcode.mcbe.world.item.crafting.Recipe
import com.valaphee.netcode.mcbe.world.item.crafting.ShapedRecipe
import com.valaphee.netcode.mcbe.world.item.crafting.ShapelessRecipe
import com.valaphee.netcode.mcbe.world.item.readIngredient
import com.valaphee.netcode.mcbe.world.item.readStackInstance
import com.valaphee.netcode.mcbe.world.item.readStackPre431
import com.valaphee.netcode.mcbe.world.item.writeIngredient
import com.valaphee.netcode.mcbe.world.item.writeStackInstance
import com.valaphee.netcode.mcbe.world.item.writeStackPre431
import com.valaphee.netcode.util.safeList
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class RecipesPacket(
    val recipes: List<Recipe>,
    val potionMixRecipes: List<PotionMixRecipe>,
    val containerMixRecipes: List<ContainerMixRecipe>,
    val materialReducers: List<MaterialReducer>,
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
                    buffer.writeVarUInt(it.ingredients.size)
                    it.ingredients.forEach(buffer::writeIngredient)
                    buffer.writeVarUInt(1)
                    if (version >= 431) buffer.writeStackInstance(it.result) else buffer.writeStackPre431(it.result)
                    buffer.writeUuid(it.id)
                    buffer.writeString(it.tags.first())
                    buffer.writeVarInt(it.priority)
                    if (version >= 407) buffer.writeVarUInt(it.netId)
                }
                is ShapedRecipe -> {
                    buffer.writeVarInt(1)
                    buffer.writeString(it.description.key)
                    buffer.writeVarInt(it.width)
                    buffer.writeVarInt(it.height)
                    it.input.forEach(buffer::writeIngredient)
                    buffer.writeVarUInt(1)
                    if (version >= 431) buffer.writeStackInstance(it.result) else buffer.writeStackPre431(it.result)
                    buffer.writeUuid(it.id)
                    buffer.writeString(it.tags.first())
                    buffer.writeVarInt(it.priority)
                    if (version >= 407) buffer.writeVarUInt(it.netId)
                }
                is MultiRecipe -> {
                    buffer.writeVarInt(4)
                    buffer.writeUuid(it.id)
                    buffer.writeVarUInt(it.netId)
                }
                else -> TODO()
            }
        }
        buffer.writeVarUInt(potionMixRecipes.size)
        potionMixRecipes.forEach {
            buffer.writeVarInt(buffer.registries.items.getId(it.inputKey))
            if (version >= 407) buffer.writeVarInt(it.inputSubId)
            buffer.writeVarInt(buffer.registries.items.getId(it.reagentKey))
            if (version >= 407) buffer.writeVarInt(it.reagentSubId)
            buffer.writeVarInt(buffer.registries.items.getId(it.outputKey))
            if (version >= 407) buffer.writeVarInt(it.outputSubId)
        }
        buffer.writeVarUInt(containerMixRecipes.size)
        containerMixRecipes.forEach {
            buffer.writeVarInt(buffer.registries.items.getId(it.inputKey))
            buffer.writeVarInt(buffer.registries.items.getId(it.reagentKey))
            buffer.writeVarInt(buffer.registries.items.getId(it.outputKey))
        }
        if (version >= 465) {
            buffer.writeVarUInt(materialReducers.size)
            materialReducers.forEach {
                buffer.writeVarInt(it.inputId)
                buffer.writeVarUInt(it.itemCounts.size)
                it.itemCounts.forEach {
                    buffer.writeVarInt(it.key)
                    buffer.writeVarInt(it.value)
                }
            }
        }
        buffer.writeBoolean(cleanRecipes)
    }

    override fun handle(handler: PacketHandler) = handler.recipes(this)

    override fun toString() = "RecipesPacket(recipes=$recipes, potionMixRecipes=$potionMixRecipes, containerMixRecipes=$containerMixRecipes, materialReducers=$materialReducers, cleanRecipes=$cleanRecipes)"
}

/**
 * @author Kevin Ludwig
 */
object RecipesPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = RecipesPacket(
        safeList(buffer.readVarUInt()) {
            when (buffer.readVarInt()) {
                0 -> {
                    val name = buffer.readString()
                    val ingredients = safeList(buffer.readVarUInt()) { buffer.readIngredient()!! }
                    val result = safeList(buffer.readVarUInt()) { if (version >= 431) buffer.readStackInstance()!! else buffer.readStackPre431()!! }
                    val id = buffer.readUuid()
                    val tag = buffer.readString()
                    val priority = buffer.readVarInt()
                    val netId = if (version >= 407) buffer.readVarUInt() else 0
                    ShapelessRecipe(ShapelessRecipe.Description(name), listOf(tag), ingredients, priority, result.first(), id, netId)
                }
                1 -> {
                    val name = buffer.readString()
                    val width = buffer.readVarInt()
                    val height = buffer.readVarInt()
                    val ingredients = safeList(width * height) { buffer.readIngredient() }
                    val result = safeList(buffer.readVarUInt()) { if (version >= 431) buffer.readStackInstance()!! else buffer.readStackPre431()!! }
                    val id = buffer.readUuid()
                    val tag = buffer.readString()
                    val priority = buffer.readVarInt()
                    val netId = if (version >= 407) buffer.readVarUInt() else 0
                    ShapedRecipe(ShapedRecipe.Description(name), listOf(tag), emptyMap(), emptyList(), priority, result.first(), id, netId)
                }
                4 -> MultiRecipe(buffer.readUuid(), buffer.readVarUInt())
                else -> TODO()
            }
        },
        safeList(buffer.readVarUInt()) { PotionMixRecipe(buffer.registries.items[buffer.readVarInt()]!!, if (version >= 407) buffer.readVarInt() else 0, buffer.registries.items[buffer.readVarInt()]!!, if (version >= 407) buffer.readVarInt() else 0, buffer.registries.items[buffer.readVarInt()]!!, if (version >= 407) buffer.readVarInt() else 0) },
        safeList(buffer.readVarUInt()) { ContainerMixRecipe(buffer.registries.items[buffer.readVarInt()]!!, buffer.registries.items[buffer.readVarInt()]!!, buffer.registries.items[buffer.readVarInt()]!!) },
        if (version >= 465) safeList(buffer.readVarUInt()) { MaterialReducer(buffer.readVarInt(), Int2IntOpenHashMap().apply { repeat(buffer.readVarUInt()) { this[buffer.readVarInt()] = buffer.readVarInt() } }) } else emptyList(),
        buffer.readBoolean()
    )
}
