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
    val typeId: Int
) {
    open fun writeToBuffer(buffer: PacketBuffer) = Unit

    override fun toString() = "ParticleData(type=$typeId)"

    companion object {
        val blockType = minecraftKey("block")
        val blockMarkerType = minecraftKey("block_marker")
        val fallingDustType = minecraftKey("falling_dust")
        val dustType = minecraftKey("dust")
        val dustColorTransitionType = minecraftKey("dust_color_transition")
        val itemType = minecraftKey("item")
        val vibrationType = minecraftKey("vibration")
    }
}

fun PacketBuffer.readParticleData(typeId: Int) = when (typeId) {
    ParticleData.blockType, ParticleData.blockMarkerType, ParticleData.fallingDustType -> BlockParticleData(type, checkNotNull(registries.blockStates[readVarInt()]))
    ParticleData.dustType -> DustParticleData(type, readFloat3(), readFloat())
    ParticleData.dustColorTransitionType -> DustColorTransitionParticleData(type, readFloat3(), readFloat(), readFloat3())
    ParticleData.itemType -> ItemParticleData(type, readItemStack())
    ParticleData.vibrationType -> {
        val origin = readInt3()
        val block = when (val positionType = readString()) {
            "minecraft:block" -> true
            "minecraft:entity" -> false
            else -> error("No such position type: $positionType")
        }
        val blockPosition: Int3?
        val entityId: Int
        if (block) {
            blockPosition = readInt3UnsignedY()
            entityId = 0
        } else {
            blockPosition = null
            entityId = readVarInt()
        }
        val ticks = readVarInt()
        VibrationParticleData(type, origin, block, blockPosition, entityId, ticks)
    }
    else -> ParticleData(type)
}

/**
 * @author Kevin Ludwig
 */
class BlockParticleData(
    type: NamespacedKey,
    val blockState: NamespacedKey
) : ParticleData(type) {
    override fun writeToBuffer(buffer: PacketBuffer) {
        buffer.writeVarInt(buffer.registries.blockStates.getId(blockState))
    }

    override fun toString() = "BlockParticleData(type=$type, blockState=$blockState)"
}

/**
 * @author Kevin Ludwig
 */
class DustParticleData(
    type: NamespacedKey,
    val color: Float3,
    val scale: Float
) : ParticleData(type) {
    override fun writeToBuffer(buffer: PacketBuffer) {
        buffer.writeFloat3(color)
        buffer.writeFloat(scale)
    }

    override fun toString() = "DustParticleData(type=$type, color=$color, scale=$scale)"
}

/**
 * @author Kevin Ludwig
 */
class DustColorTransitionParticleData(
    type: NamespacedKey,
    val fromColor: Float3,
    val scale: Float,
    val toColor: Float3
) : ParticleData(type) {
    override fun writeToBuffer(buffer: PacketBuffer) {
        buffer.writeFloat3(fromColor)
        buffer.writeFloat(scale)
        buffer.writeFloat3(toColor)
    }

    override fun toString() = "DustColorTransitionParticleData(type=$type, fromColor=$fromColor, scale=$scale, toColor=$toColor)"
}

/**
 * @author Kevin Ludwig
 */
class ItemParticleData(
    type: NamespacedKey,
    val itemStack: ItemStack?
) : ParticleData(type) {
    override fun writeToBuffer(buffer: PacketBuffer) {
        buffer.writeItemStack(itemStack)
    }

    override fun toString() = "ItemParticleData(type=$type, itemStack=$itemStack)"
}

/**
 * @author Kevin Ludwig
 */
class VibrationParticleData(
    type: NamespacedKey,
    val origin: Int3,
    val block: Boolean,
    val blockPosition: Int3?,
    val entityId: Int,
    val ticks: Int
) : ParticleData(type) {
    override fun writeToBuffer(buffer: PacketBuffer) {
        buffer.writeInt3(origin)
        buffer.writeString(if (block) "minecraft:block" else "minecraft:entity")
        if (block) buffer.writeInt3(blockPosition!!) else buffer.writeVarInt(entityId)
        buffer.writeVarInt(ticks)
    }

    override fun toString() = "VibrationParticleData(type=$type, origin=$origin, block=$block, blockPosition=$blockPosition, entityId=$entityId, ticks=$ticks)"
}
