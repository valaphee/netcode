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
import com.valaphee.netcode.mcbe.item.stack.Stack
import com.valaphee.netcode.mcbe.item.stack.readIngredient
import com.valaphee.netcode.mcbe.item.stack.readStackInstance
import com.valaphee.netcode.mcbe.item.stack.readStackPre431
import com.valaphee.netcode.mcbe.item.stack.writeIngredient
import com.valaphee.netcode.mcbe.item.stack.writeStackInstance
import com.valaphee.netcode.mcbe.item.stack.writeStackPre431
import com.valaphee.netcode.mcbe.util.safeList
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
            val type = it.type
            buffer.writeVarInt(type.ordinal)
            when (type) {
                Recipe.Type.Shapeless, Recipe.Type.ShulkerBox, Recipe.Type.ShapelessChemistry -> {
                    buffer.writeString(it.name!!)
                    it.inputs!!.let {
                        buffer.writeVarUInt(it.size)
                        it.forEach { buffer.writeIngredient(it) }
                    }
                    it.outputs!!.let {
                        buffer.writeVarUInt(it.size)
                        it.forEach { if (version >= 431) buffer.writeStackInstance(it) else buffer.writeStackPre431(it) }
                    }
                    buffer.writeUuid(it.id!!)
                    buffer.writeString(it.tag!!)
                    buffer.writeVarInt(it.priority)
                    if (version >= 407) buffer.writeVarUInt(it.netId)
                }
                Recipe.Type.Shaped, Recipe.Type.ShapedChemistry -> {
                    buffer.writeString(it.name!!)
                    buffer.writeVarInt(it.width)
                    buffer.writeVarInt(it.height)
                    it.inputs!!.forEach { buffer.writeIngredient(it) }
                    it.outputs!!.let {
                        buffer.writeVarUInt(it.size)
                        it.forEach { if (version >= 431) buffer.writeStackInstance(it) else buffer.writeStackPre431(it) }
                    }
                    buffer.writeUuid(it.id!!)
                    buffer.writeString(it.tag!!)
                    buffer.writeVarInt(it.priority)
                    if (version >= 407) buffer.writeVarUInt(it.netId)
                }
                Recipe.Type.Furnace, Recipe.Type.FurnaceData -> {
                    val input = it.inputs!!.first()!!
                    buffer.writeVarInt(buffer.registrySet.items.getId(input.itemKey))
                    if (type == Recipe.Type.FurnaceData) buffer.writeVarInt(input.subId)
                    if (version >= 431) buffer.writeStackInstance(it.outputs!![0]) else buffer.writeStackPre431(it.outputs!![0])
                    buffer.writeString(it.tag!!)
                }
                Recipe.Type.Multi -> {
                    buffer.writeUuid(it.id!!)
                    buffer.writeVarUInt(it.netId)
                }
            }
        }
        buffer.writeVarUInt(potionMixRecipes.size)
        potionMixRecipes.forEach {
            buffer.writeVarInt(buffer.registrySet.items.getId(it.inputKey))
            if (version >= 407) buffer.writeVarInt(it.inputSubId)
            buffer.writeVarInt(buffer.registrySet.items.getId(it.reagentKey))
            if (version >= 407) buffer.writeVarInt(it.reagentSubId)
            buffer.writeVarInt(buffer.registrySet.items.getId(it.outputKey))
            if (version >= 407) buffer.writeVarInt(it.outputSubId)
        }
        buffer.writeVarUInt(containerMixRecipes.size)
        containerMixRecipes.forEach {
            buffer.writeVarInt(buffer.registrySet.items.getId(it.inputKey))
            buffer.writeVarInt(buffer.registrySet.items.getId(it.reagentKey))
            buffer.writeVarInt(buffer.registrySet.items.getId(it.outputKey))
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
            when (val type = Recipe.Type.values()[buffer.readVarInt()]) {
                Recipe.Type.Shapeless, Recipe.Type.ShulkerBox, Recipe.Type.ShapelessChemistry -> {
                    val name = buffer.readString()
                    val inputs = safeList(buffer.readVarUInt()) { buffer.readIngredient() }
                    val outputs = safeList(buffer.readVarUInt()) { if (version >= 431) buffer.readStackInstance() else buffer.readStackPre431() }
                    Recipe(buffer.readUuid(), name, type, 0, 0, inputs, outputs, buffer.readString(), buffer.readVarInt(), if (version >= 407) buffer.readVarUInt() else 0)
                }
                Recipe.Type.Shaped, Recipe.Type.ShapedChemistry -> {
                    val name = buffer.readString()
                    val width = buffer.readVarInt()
                    val height = buffer.readVarInt()
                    val inputs = safeList(width * height) { buffer.readIngredient() }
                    val outputs = safeList(buffer.readVarUInt()) { if (version >= 431) buffer.readStackInstance() else buffer.readStackPre431() }
                    Recipe(buffer.readUuid(), name, type, width, height, inputs, outputs, buffer.readString(), buffer.readVarInt(), if (version >= 407) buffer.readVarUInt() else 0)
                }
                Recipe.Type.Furnace, Recipe.Type.FurnaceData -> furnaceRecipe(Stack(buffer.registrySet.items[buffer.readVarInt()]!!, if (type == Recipe.Type.FurnaceData) buffer.readVarInt() else -1), if (version >= 431) buffer.readStackInstance() else buffer.readStackPre431(), buffer.readString())
                Recipe.Type.Multi -> multiRecipe(buffer.readUuid(), if (version >= 407) buffer.readVarUInt() else 0)
            }
        },
        safeList(buffer.readVarUInt()) { PotionMixRecipe(buffer.registrySet.items[buffer.readVarInt()] ?: "minecraft:unknown", if (version >= 407) buffer.readVarInt() else 0, buffer.registrySet.items[buffer.readVarInt()] ?: "minecraft:unknown", if (version >= 407) buffer.readVarInt() else 0, buffer.registrySet.items[buffer.readVarInt()] ?: "minecraft:unknown", if (version >= 407) buffer.readVarInt() else 0) },
        safeList(buffer.readVarUInt()) { ContainerMixRecipe(buffer.registrySet.items[buffer.readVarInt()] ?: "minecraft:unknown", buffer.registrySet.items[buffer.readVarInt()] ?: "minecraft:unknown", buffer.registrySet.items[buffer.readVarInt()] ?: "minecraft:unknown") },
        if (version >= 465) safeList(buffer.readVarUInt()) { MaterialReducer(buffer.readVarInt(), Int2IntOpenHashMap().apply { repeat(buffer.readVarUInt()) { this[buffer.readVarInt()] = buffer.readVarInt() } }) } else emptyList(),
        buffer.readBoolean()
    )
}
