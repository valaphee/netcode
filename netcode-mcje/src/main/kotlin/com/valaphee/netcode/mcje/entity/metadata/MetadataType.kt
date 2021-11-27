/*
 * MIT License
 *
 * Copyright (c) 2021, Valaphee.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.valaphee.netcode.mcje.entity.metadata

import com.fasterxml.jackson.module.kotlin.readValue
import com.valaphee.foundry.math.Float3
import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mc.util.Direction
import com.valaphee.netcode.mc.util.Registry
import com.valaphee.netcode.mc.util.nbt.NbtInputStream
import com.valaphee.netcode.mc.util.nbt.NbtOutputStream
import com.valaphee.netcode.mc.util.nbt.Tag
import com.valaphee.netcode.mcje.PacketBuffer
import com.valaphee.netcode.mcje.util.text.Component
import com.valaphee.netcode.util.ByteBufStringReader
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
interface MetadataType<T> {
    fun read(buffer: PacketBuffer): T

    fun write(buffer: PacketBuffer, value: Any?)

    companion object {
        val Byte = object : MetadataType<Number> {
            override fun read(buffer: PacketBuffer) = buffer.readByte()

            override fun write(buffer: PacketBuffer, value: Any?) {
                buffer.writeByte((value as Number).toInt())
            }
        }

        val Int = object : MetadataType<Number> {
            override fun read(buffer: PacketBuffer) = buffer.readVarInt()

            override fun write(buffer: PacketBuffer, value: Any?) {
                buffer.writeVarInt((value as Number).toInt())
            }
        }

        val Float = object : MetadataType<Number> {
            override fun read(buffer: PacketBuffer) = buffer.readFloatLE()

            override fun write(buffer: PacketBuffer, value: Any?) {
                buffer.writeFloatLE((value as Number).toFloat())
            }
        }

        val String = object : MetadataType<String> {
            override fun read(buffer: PacketBuffer) = buffer.readString()

            override fun write(buffer: PacketBuffer, value: Any?) {
                buffer.writeString(value as String)
            }
        }

        val Component = object : MetadataType<Component> {
            override fun read(buffer: PacketBuffer): Component = buffer.objectMapper.readValue(ByteBufStringReader(buffer, buffer.readVarInt()))

            override fun write(buffer: PacketBuffer, value: Any?) {
                buffer.writeString(buffer.objectMapper.writeValueAsString(value as Component))
            }
        }

        val OptionalComponent = object : MetadataType<Component?> {
            override fun read(buffer: PacketBuffer): Component? = if (buffer.readBoolean()) buffer.objectMapper.readValue(ByteBufStringReader(buffer, buffer.readVarInt())) else null

            override fun write(buffer: PacketBuffer, value: Any?) {
                value?.let {
                    buffer.writeBoolean(true)
                    buffer.writeString(buffer.objectMapper.writeValueAsString(value as Component))
                } ?: buffer.writeBoolean(false)
            }
        }

        /*val Stack = object : MetadataType<Stack<*>?> {
            override fun read(buffer: PacketBuffer) = buffer.readStack()

            override fun write(buffer: PacketBuffer, value: Any?) {
                buffer.writeStack(value as Stack<*>?)
            }
        }*/

        val Boolean = object : MetadataType<Boolean> {
            override fun read(buffer: PacketBuffer) = buffer.readBoolean()

            override fun write(buffer: PacketBuffer, value: Any?) {
                buffer.writeBoolean(value as Boolean)
            }
        }

        val Float3 = object : MetadataType<Float3> {
            override fun read(buffer: PacketBuffer) = buffer.readFloat3()

            override fun write(buffer: PacketBuffer, value: Any?) {
                buffer.writeFloat3(value as Float3)
            }
        }

        val Int3UnsignedY = object : MetadataType<Int3> {
            override fun read(buffer: PacketBuffer) = buffer.readInt3UnsignedY()

            override fun write(buffer: PacketBuffer, value: Any?) {
                buffer.writeInt3UnsignedY(value as Int3)
            }
        }

        val OptionalInt3UnsignedY = object : MetadataType<Int3?> {
            override fun read(buffer: PacketBuffer) = if (buffer.readBoolean()) buffer.readInt3UnsignedY() else null

            override fun write(buffer: PacketBuffer, value: Any?) {
                value?.let {
                    buffer.writeBoolean(true)
                    buffer.writeInt3UnsignedY(value as Int3)
                } ?: buffer.writeBoolean(false)
            }
        }

        val Direction = object : MetadataType<Direction> {
            override fun read(buffer: PacketBuffer) = buffer.readDirection()

            override fun write(buffer: PacketBuffer, value: Any?) {
                buffer.writeDirection(value as Direction)
            }
        }

        val OptionalUuid = object : MetadataType<UUID?> {
            override fun read(buffer: PacketBuffer) = if (buffer.readBoolean()) buffer.readUuid() else null

            override fun write(buffer: PacketBuffer, value: Any?) {
                value?.let {
                    buffer.writeBoolean(true)
                    buffer.writeUuid(it as UUID)
                } ?: buffer.writeBoolean(false)
            }
        }

        /*val OptionalBlockState = object : MetadataType<BlockState?> {
            override fun read(buffer: PacketBuffer): BlockState? {
                val blockStateId = buffer.readVarInt()
                return if (blockStateId == 0) null else BlockState.byId(blockStateId)
            }

            override fun write(buffer: PacketBuffer, value: Any?) {
                value?.let { buffer.writeVarInt((it as BlockState).id) } ?: buffer.writeVarInt(0)
            }
        }*/

        val Nbt = object : MetadataType<Tag?> {
            override fun read(buffer: PacketBuffer) = NbtInputStream(ByteBufInputStream(buffer)).use { it.readTag() }

            override fun write(buffer: PacketBuffer, value: Any?) {
                NbtOutputStream(ByteBufOutputStream(buffer)).use { it.writeTag(value as Tag) }
            }
        }

        /*val Particle = object : MetadataType<Particle> {
            override fun read(buffer: PacketBuffer) = buffer.readParticle(ParticleType.byId(buffer.readVarInt())!!)

            override fun write(buffer: PacketBuffer, value: Any?) {
                buffer.writeVarInt((value as Particle).type.id)
                buffer.writeParticle(value)
            }
        }*/

        val VillagerData = object : MetadataType<VillagerData> {
            override fun read(buffer: PacketBuffer) = buffer.readVillagerData()

            override fun write(buffer: PacketBuffer, value: Any?) {
                buffer.writeVillagerData(value as VillagerData)
            }
        }

        val OptionalInt = object : MetadataType<Number?> {
            override fun read(buffer: PacketBuffer): Number? {
                val value = buffer.readVarInt()
                return if (value == 0) null else value
            }

            override fun write(buffer: PacketBuffer, value: Any?) {
                buffer.writeVarInt(value?.let { (it as Number).toInt() } ?: 0)
            }
        }

        val Pose = object : MetadataType<Pose> {
            override fun read(buffer: PacketBuffer) = com.valaphee.netcode.mcje.entity.metadata.Pose.values()[buffer.readVarInt()]

            override fun write(buffer: PacketBuffer, value: Any?) {
                buffer.writeVarInt((value as Pose).ordinal)
            }
        }

        val Flags = object : MetadataType<Collection<Flag>> {
            override fun read(buffer: PacketBuffer) = buffer.readByteFlags<Flag>()

            override fun write(buffer: PacketBuffer, value: Any?) {
                @Suppress("UNCHECKED_CAST")
                buffer.writeByteFlags(value as Collection<Flag>)
            }
        }

        val registry = Registry<MetadataType<*>>().apply {
            this[0] = Flags
            this[0] = Byte
            this[1] = Int
            this[2] = Float
            this[3] = String
            this[4] = Component
            this[5] = OptionalComponent
            /*this[6] = Stack*/
            this[7] = Boolean
            this[8] = Float3
            this[9] = Int3UnsignedY
            this[10] = OptionalInt3UnsignedY
            this[11] = Direction
            this[12] = OptionalUuid
            /*this[13] = OptionalBlockState*/
            this[14] = Nbt
            /*this[15] = Particle*/
            this[16] = VillagerData
            this[17] = OptionalInt
            this[18] = Pose
        }
    }
}
