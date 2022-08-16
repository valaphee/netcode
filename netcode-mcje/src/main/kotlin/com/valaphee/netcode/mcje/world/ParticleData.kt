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

package com.valaphee.netcode.mcje.world

import com.valaphee.foundry.math.Float3
import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.util.NamespacedKey
import com.valaphee.netcode.mcje.util.minecraftKey
import com.valaphee.netcode.mcje.world.item.ItemStack
import com.valaphee.netcode.mcje.world.item.readItemStack
import com.valaphee.netcode.mcje.world.item.writeItemStack

/**
 * @author Kevin Ludwig
 */
open class ParticleData(
    val typeKey: NamespacedKey
) {
    fun getTypeId(version: Int) = ParticleType[version, typeKey]

    open fun writeToBuffer(buffer: PacketBuffer, version: Int) = Unit

    override fun toString() = "ParticleData(typeKey=$typeKey)"

    companion object {
        val blockTypeKey = minecraftKey("block")
        val blockMarkerTypeKey = minecraftKey("block_marker")
        val fallingDustTypeKey = minecraftKey("falling_dust")
        val dustTypeKey = minecraftKey("dust")
        val dustColorTransitionTypeKey = minecraftKey("dust_color_transition")
        val itemTypeKey = minecraftKey("item")
        val vibrationTypeKey = minecraftKey("vibration")
    }
}

fun PacketBuffer.readParticleData(typeKey: NamespacedKey, version: Int) = when (typeKey) {
    ParticleData.blockTypeKey, ParticleData.blockMarkerTypeKey, ParticleData.fallingDustTypeKey -> BlockParticleData(typeKey, readVarInt())
    ParticleData.dustTypeKey -> DustParticleData(typeKey, readFloat3(), readFloat())
    ParticleData.dustColorTransitionTypeKey -> DustColorTransitionParticleData(typeKey, readFloat3(), readFloat(), readFloat3())
    ParticleData.itemTypeKey -> ItemParticleData(typeKey, readItemStack(version))
    ParticleData.vibrationTypeKey -> {
        val origin = readInt3()
        val block = when (val positionType = readString()) {
            "minecraft:block" -> true
            "minecraft:entity" -> false
            else -> error("No such position type: $positionType")
        }
        val blockPosition: Int3?
        val entityId: Int
        if (block) {
            blockPosition = readBlockPosition()
            entityId = 0
        } else {
            blockPosition = null
            entityId = readVarInt()
        }
        val ticks = readVarInt()
        VibrationParticleData(typeKey, origin, block, blockPosition, entityId, ticks)
    }
    else -> ParticleData(typeKey)
}

/**
 * @author Kevin Ludwig
 */
class BlockParticleData(
    typeKey: NamespacedKey,
    val blockStateId: Int
) : ParticleData(typeKey) {
    override fun writeToBuffer(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(blockStateId)
    }

    override fun toString() = "BlockParticleData(typeKey=$typeKey, blockStateId=$blockStateId)"
}

/**
 * @author Kevin Ludwig
 */
class DustParticleData(
    typeKey: NamespacedKey,
    val color: Float3,
    val scale: Float
) : ParticleData(typeKey) {
    override fun writeToBuffer(buffer: PacketBuffer, version: Int) {
        buffer.writeFloat3(color)
        buffer.writeFloat(scale)
    }

    override fun toString() = "DustParticleData(typeId=$typeKey, color=$color, scale=$scale)"
}

/**
 * @author Kevin Ludwig
 */
class DustColorTransitionParticleData(
    typeKey: NamespacedKey,
    val fromColor: Float3,
    val scale: Float,
    val toColor: Float3
) : ParticleData(typeKey) {
    override fun writeToBuffer(buffer: PacketBuffer, version: Int) {
        buffer.writeFloat3(fromColor)
        buffer.writeFloat(scale)
        buffer.writeFloat3(toColor)
    }

    override fun toString() = "DustColorTransitionParticleData(typeId=$typeKey, fromColor=$fromColor, scale=$scale, toColor=$toColor)"
}

/**
 * @author Kevin Ludwig
 */
class ItemParticleData(
    typeKey: NamespacedKey,
    val itemStack: ItemStack?
) : ParticleData(typeKey) {
    override fun writeToBuffer(buffer: PacketBuffer, version: Int) {
        buffer.writeItemStack(itemStack, version)
    }

    override fun toString() = "ItemParticleData(typeId=$typeKey, itemStack=$itemStack)"
}

/**
 * @author Kevin Ludwig
 */
class VibrationParticleData(
    typeKey: NamespacedKey,
    val origin: Int3,
    val block: Boolean,
    val blockPosition: Int3?,
    val entityId: Int,
    val ticks: Int
) : ParticleData(typeKey) {
    override fun writeToBuffer(buffer: PacketBuffer, version: Int) {
        buffer.writeInt3(origin)
        buffer.writeString(if (block) "minecraft:block" else "minecraft:entity")
        if (block) buffer.writeInt3(blockPosition!!) else buffer.writeVarInt(entityId)
        buffer.writeVarInt(ticks)
    }

    override fun toString() = "VibrationParticleData(typeKey=$typeKey, origin=$origin, block=$block, blockPosition=$blockPosition, entityId=$entityId, ticks=$ticks)"
}
