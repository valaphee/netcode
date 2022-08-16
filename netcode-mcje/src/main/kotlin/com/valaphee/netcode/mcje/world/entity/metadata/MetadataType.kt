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

package com.valaphee.netcode.mcje.world.entity.metadata

import com.fasterxml.jackson.module.kotlin.readValue
import com.valaphee.foundry.math.Float3
import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.util.Direction
import com.valaphee.netcode.mcje.util.NamespacedKey
import com.valaphee.netcode.mcje.world.ParticleData
import com.valaphee.netcode.mcje.world.ParticleType
import com.valaphee.netcode.mcje.world.item.ItemStack
import com.valaphee.netcode.mcje.world.item.readItemStack
import com.valaphee.netcode.mcje.world.item.writeItemStack
import com.valaphee.netcode.mcje.world.readParticleData
import com.valaphee.netcode.util.Int2ObjectOpenHashBiMap
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream
import net.kyori.adventure.text.Component
import java.io.OutputStream
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
interface MetadataType<T> {
    fun read(buffer: PacketBuffer, version: Int): T

    fun write(buffer: PacketBuffer, value: Any?, version: Int)

    companion object {
        val Byte = object : MetadataType<Byte> {
            override fun read(buffer: PacketBuffer, version: Int) = buffer.readByte()

            override fun write(buffer: PacketBuffer, value: Any?, version: Int) {
                buffer.writeByte((value as Byte).toInt())
            }
        }

        val Int = object : MetadataType<Int> {
            override fun read(buffer: PacketBuffer, version: Int) = buffer.readVarInt()

            override fun write(buffer: PacketBuffer, value: Any?, version: Int) {
                buffer.writeVarInt(value as Int)
            }
        }

        val Float = object : MetadataType<Float> {
            override fun read(buffer: PacketBuffer, version: Int) = buffer.readFloatLE()

            override fun write(buffer: PacketBuffer, value: Any?, version: Int) {
                buffer.writeFloatLE(value as Float)
            }
        }

        val String = object : MetadataType<String> {
            override fun read(buffer: PacketBuffer, version: Int) = buffer.readString()

            override fun write(buffer: PacketBuffer, value: Any?, version: Int) {
                buffer.writeString(value as String)
            }
        }

        val Component = object : MetadataType<Component> {
            override fun read(buffer: PacketBuffer, version: Int) = buffer.readComponent()

            override fun write(buffer: PacketBuffer, value: Any?, version: Int) {
                buffer.writeComponent(value as Component)
            }
        }

        val OptionalComponent = object : MetadataType<Component?> {
            override fun read(buffer: PacketBuffer, version: Int): Component? = if (buffer.readBoolean()) buffer.readComponent() else null

            override fun write(buffer: PacketBuffer, value: Any?, version: Int) {
                value?.let {
                    buffer.writeBoolean(true)
                    buffer.writeComponent(value as Component)
                } ?: buffer.writeBoolean(false)
            }
        }

        val ItemStack = object : MetadataType<ItemStack?> {
            override fun read(buffer: PacketBuffer, version: Int) = buffer.readItemStack(version)

            override fun write(buffer: PacketBuffer, value: Any?, version: Int) {
                buffer.writeItemStack(value as ItemStack?, version)
            }
        }

        val Boolean = object : MetadataType<Boolean> {
            override fun read(buffer: PacketBuffer, version: Int) = buffer.readBoolean()

            override fun write(buffer: PacketBuffer, value: Any?, version: Int) {
                buffer.writeBoolean(value as Boolean)
            }
        }

        val Float3 = object : MetadataType<Float3> {
            override fun read(buffer: PacketBuffer, version: Int) = buffer.readFloat3()

            override fun write(buffer: PacketBuffer, value: Any?, version: Int) {
                buffer.writeFloat3(value as Float3)
            }
        }

        val BlockPosition = object : MetadataType<Int3> {
            override fun read(buffer: PacketBuffer, version: Int) = buffer.readBlockPosition()

            override fun write(buffer: PacketBuffer, value: Any?, version: Int) {
                buffer.writeBlockPosition(value as Int3)
            }
        }

        val OptionalInt3UnsignedY = object : MetadataType<Int3?> {
            override fun read(buffer: PacketBuffer, version: Int) = if (buffer.readBoolean()) buffer.readBlockPosition() else null

            override fun write(buffer: PacketBuffer, value: Any?, version: Int) {
                value?.let {
                    buffer.writeBoolean(true)
                    buffer.writeBlockPosition(value as Int3)
                } ?: buffer.writeBoolean(false)
            }
        }

        val Direction = object : MetadataType<Direction> {
            override fun read(buffer: PacketBuffer, version: Int) = buffer.readDirection()

            override fun write(buffer: PacketBuffer, value: Any?, version: Int) {
                buffer.writeDirection(value as Direction)
            }
        }

        val OptionalUuid = object : MetadataType<UUID?> {
            override fun read(buffer: PacketBuffer, version: Int) = if (buffer.readBoolean()) buffer.readUuid() else null

            override fun write(buffer: PacketBuffer, value: Any?, version: Int) {
                value?.let {
                    buffer.writeBoolean(true)
                    buffer.writeUuid(it as UUID)
                } ?: buffer.writeBoolean(false)
            }
        }

        val OptionalBlockState = object : MetadataType<Int?> {
            override fun read(buffer: PacketBuffer, version: Int): Int? {
                val blockStateId = buffer.readVarInt()
                return if (blockStateId == 0) null else blockStateId
            }

            override fun write(buffer: PacketBuffer, value: Any?, version: Int) {
                value?.let { buffer.writeVarInt(it as Int) } ?: buffer.writeVarInt(0)
            }
        }

        val Nbt = object : MetadataType<Any?> {
            override fun read(buffer: PacketBuffer, version: Int) = buffer.nbtObjectMapper.readValue<Any?>(ByteBufInputStream(buffer))

            override fun write(buffer: PacketBuffer, value: Any?, version: Int) {
                buffer.nbtObjectMapper.writeValue(ByteBufOutputStream(buffer) as OutputStream, value)
            }
        }

        val Particle = object : MetadataType<ParticleData> {
            override fun read(buffer: PacketBuffer, version: Int) = buffer.readParticleData(checkNotNull(ParticleType[version, buffer.readVarInt()]), version)

            override fun write(buffer: PacketBuffer, value: Any?, version: Int) {
                buffer.writeVarInt((value as ParticleData).getTypeId(version))
                value.writeToBuffer(buffer, version)
            }
        }

        val VillagerData = object : MetadataType<VillagerData> {
            override fun read(buffer: PacketBuffer, version: Int) = buffer.readVillagerData()

            override fun write(buffer: PacketBuffer, value: Any?, version: Int) {
                buffer.writeVillagerData(value as VillagerData)
            }
        }

        val OptionalInt = object : MetadataType<Int?> {
            override fun read(buffer: PacketBuffer, version: Int): Int? {
                val value = buffer.readVarInt()
                return if (value == 0) null else value
            }

            override fun write(buffer: PacketBuffer, value: Any?, version: Int) {
                buffer.writeVarInt(value?.let { it as Int } ?: 0)
            }
        }

        val Pose = object : MetadataType<Pose> {
            override fun read(buffer: PacketBuffer, version: Int) = com.valaphee.netcode.mcje.world.entity.metadata.Pose.values()[buffer.readVarInt()]

            override fun write(buffer: PacketBuffer, value: Any?, version: Int) {
                buffer.writeVarInt((value as Pose).ordinal)
            }
        }

        val CatVariant = object : MetadataType<Int> {
            override fun read(buffer: PacketBuffer, version: Int) = buffer.readVarInt()

            override fun write(buffer: PacketBuffer, value: Any?, version: Int) {
                buffer.writeVarInt(value as Int)
            }
        }

        val FrogVariant = object : MetadataType<Int> {
            override fun read(buffer: PacketBuffer, version: Int) = buffer.readVarInt()

            override fun write(buffer: PacketBuffer, value: Any?, version: Int) {
                buffer.writeVarInt(value as Int)
            }
        }

        val DimensionAndBlockPosition = object : MetadataType<Pair<NamespacedKey, Int3>> {
            override fun read(buffer: PacketBuffer, version: Int) = buffer.readNamespacedKey() to buffer.readBlockPosition()

            override fun write(buffer: PacketBuffer, value: Any?, version: Int) {
                val (dimension, position) = value as Pair<NamespacedKey, Int3>
                buffer.writeNamespacedKey(dimension)
                buffer.writeBlockPosition(position)
            }
        }

        val PaintingVariant = object : MetadataType<Int> {
            override fun read(buffer: PacketBuffer, version: Int) = buffer.readVarInt()

            override fun write(buffer: PacketBuffer, value: Any?, version: Int) {
                buffer.writeVarInt(value as Int)
            }
        }

        val Flags = object : MetadataType<Set<Flag>> {
            override fun read(buffer: PacketBuffer, version: Int) = buffer.readByteFlags<Flag>()

            override fun write(buffer: PacketBuffer, value: Any?, version: Int) {
                @Suppress("UNCHECKED_CAST")
                buffer.writeByteFlags(value as Set<Flag>)
            }
        }

        val registry = Int2ObjectOpenHashBiMap<MetadataType<*>>().apply {
            this[ 0] = Flags
            this[ 0] = Byte
            this[ 1] = Int
            this[ 2] = Float
            this[ 3] = String
            this[ 4] = Component
            this[ 5] = OptionalComponent
            this[ 6] = ItemStack
            this[ 7] = Boolean
            this[ 8] = Float3
            this[ 9] = BlockPosition
            this[10] = OptionalInt3UnsignedY
            this[11] = Direction
            this[12] = OptionalUuid
            this[13] = OptionalBlockState
            this[14] = Nbt
            this[15] = Particle
            this[16] = VillagerData
            this[17] = OptionalInt
            this[18] = Pose
            this[19] = CatVariant
            this[20] = FrogVariant
            this[21] = DimensionAndBlockPosition
            this[22] = PaintingVariant
        }
    }
}
