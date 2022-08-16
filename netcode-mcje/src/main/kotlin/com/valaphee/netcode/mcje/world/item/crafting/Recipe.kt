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

package com.valaphee.netcode.mcje.world.item.crafting

import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.util.NamespacedKey
import com.valaphee.netcode.mcje.util.minecraftKey
import com.valaphee.netcode.mcje.world.item.ItemStack
import com.valaphee.netcode.mcje.world.item.readItemStack
import com.valaphee.netcode.mcje.world.item.writeItemStack

/**
 * @author Kevin Ludwig
 */
open class Recipe(
    val type: NamespacedKey,
    val key: NamespacedKey
) {
    open fun writeToBuffer(buffer: PacketBuffer, version: Int) = Unit

    override fun toString() = "Recipe(type=$type, key=$key)"

    companion object {
        val craftingShapelessType = minecraftKey("crafting_shapeless")
        val craftingShapedType = minecraftKey("crafting_shaped")
        val smeltingType = minecraftKey("smelting")
        val blastingType = minecraftKey("blasting")
        val smokingType = minecraftKey("smoking")
        val campfireCookingType = minecraftKey("campfire_cooking")
        val stonecuttingType = minecraftKey("stonecutting")
        val smithingType = minecraftKey("smithing")
    }
}

fun PacketBuffer.readRecipe(version: Int) = when (val type = readNamespacedKey()) {
    Recipe.craftingShapelessType -> ShapelessRecipe(type, readNamespacedKey(), readString(), List(readVarInt()) { List(readVarInt()) { readItemStack(version) } }, readItemStack(version))
    Recipe.craftingShapedType -> {
        val width = readVarInt()
        val height = readVarInt()
        val group = readString()
        val input = List(width * height) { List(readVarInt()) { readItemStack(version) } }
        val output = readItemStack(version)
        ShapedRecipe(type, readNamespacedKey(), width, height, group, input, output)
    }
    Recipe.smeltingType, Recipe.blastingType, Recipe.smokingType, Recipe.campfireCookingType -> FurnaceRecipe(type, readNamespacedKey(), readString(), List(readVarInt()) { readItemStack(version) }, readItemStack(version), readFloat(), readVarInt())
    Recipe.stonecuttingType -> StonecuttingRecipe(type, readNamespacedKey(), readString(), List(readVarInt()) { readItemStack(version) }, readItemStack(version))
    Recipe.smithingType -> SmithingRecipe(type, readNamespacedKey(), List(readVarInt()) { readItemStack(version) }, List(readVarInt()) { readItemStack(version) }, readItemStack(version))
    else -> Recipe(type, readNamespacedKey())
}

/**
 * @author Kevin Ludwig
 */
class ShapelessRecipe(
    type: NamespacedKey,
    key: NamespacedKey,
    val group: String,
    val input: List<List<ItemStack?>>,
    val output: ItemStack?
) : Recipe(type, key) {
    override fun writeToBuffer(buffer: PacketBuffer, version: Int) {
        buffer.writeString(group)
        buffer.writeVarInt(input.size)
        input.forEach {
            buffer.writeVarInt(it.size)
            it.forEach { buffer.writeItemStack(it, version) }
        }
        buffer.writeItemStack(output, version)
    }

    override fun toString() = "ShapelessRecipe(type=$type, key=$key, group='$group', input=$input, output=$output)"
}

/**
 * @author Kevin Ludwig
 */
class ShapedRecipe(
    type: NamespacedKey,
    key: NamespacedKey,
    val width: Int,
    val height: Int,
    val group: String,
    val input: List<List<ItemStack?>>,
    val output: ItemStack?
) : Recipe(type, key) {
    override fun writeToBuffer(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(width)
        buffer.writeVarInt(height)
        buffer.writeString(group)
        input.forEach {
            buffer.writeVarInt(it.size)
            it.forEach { buffer.writeItemStack(it, version) }
        }
        buffer.writeItemStack(output, version)
    }

    override fun toString() = "ShapedRecipe(type=$type, key=$key, width=$width, height=$height, group='$group', input=$input, output=$output)"
}

/**
 * @author Kevin Ludwig
 */
class FurnaceRecipe(
    type: NamespacedKey,
    key: NamespacedKey,
    val group: String,
    val input: List<ItemStack?>,
    val output: ItemStack?,
    val experience: Float,
    val cookingTime: Int
) : Recipe(type, key) {
    override fun writeToBuffer(buffer: PacketBuffer, version: Int) {
        buffer.writeString(group)
        buffer.writeVarInt(input.size)
        input.forEach { buffer.writeItemStack(it, version) }
        buffer.writeItemStack(output, version)
        buffer.writeFloat(experience)
        buffer.writeVarInt(cookingTime)
    }

    override fun toString() = "FurnaceRecipe(type=$type, key=$key, group='$group', input=$input, output=$output, experience=$experience, cookingTime=$cookingTime)"
}

/**
 * @author Kevin Ludwig
 */
class StonecuttingRecipe(
    type: NamespacedKey,
    key: NamespacedKey,
    val group: String,
    val input: List<ItemStack?>,
    val output: ItemStack?
) : Recipe(type, key) {
    override fun writeToBuffer(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(input.size)
        input.forEach { buffer.writeItemStack(it, version) }
        buffer.writeItemStack(output, version)
    }

    override fun toString() = "StonecutterRecipe(type=$type, key=$key, input=$input, output=$output)"
}

/**
 * @author Kevin Ludwig
 */
class SmithingRecipe(
    type: NamespacedKey,
    key: NamespacedKey,
    val input: List<ItemStack?>,
    val material: List<ItemStack?>,
    val output: ItemStack?
) : Recipe(type, key) {
    override fun writeToBuffer(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(input.size)
        input.forEach { buffer.writeItemStack(it, version) }
        buffer.writeVarInt(material.size)
        material.forEach { buffer.writeItemStack(it, version) }
        buffer.writeItemStack(output, version)
    }

    override fun toString() = "SmithingRecipe(type=$type, key=$key, input=$input, material=$material, output=$output)"
}
