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

package com.valaphee.netcode.mcbe.world.entity.metadata

import com.fasterxml.jackson.module.kotlin.readValue
import com.valaphee.foundry.math.Float3
import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.util.Int2ObjectOpenHashBiMap
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream
import java.io.OutputStream

/**
 * @author Kevin Ludwig
 */
abstract class MetadataType<T> {
    abstract fun read(buffer: PacketBuffer): T

    abstract fun write(buffer: PacketBuffer, value: T)

    companion object {
        val Byte = object : MetadataType<Byte>() {
            override fun read(buffer: PacketBuffer) = buffer.readByte()

            override fun write(buffer: PacketBuffer, value: Byte) {
                buffer.writeByte(value.toInt())
            }
        }

        val Short = object : MetadataType<Short>() {
            override fun read(buffer: PacketBuffer) = buffer.readShortLE()

            override fun write(buffer: PacketBuffer, value: Short) {
                buffer.writeShortLE(value.toInt())
            }
        }

        val Int = object : MetadataType<Int>() {
            override fun read(buffer: PacketBuffer) = buffer.readVarInt()

            override fun write(buffer: PacketBuffer, value: Int) {
                buffer.writeVarInt(value)
            }
        }

        val Float = object : MetadataType<Float>() {
            override fun read(buffer: PacketBuffer) = buffer.readFloatLE()

            override fun write(buffer: PacketBuffer, value: Float) {
                buffer.writeFloatLE(value)
            }
        }

        val String = object : MetadataType<String>() {
            override fun read(buffer: PacketBuffer) = buffer.readString()

            override fun write(buffer: PacketBuffer, value: String) {
                buffer.writeString(value)
            }
        }

        val Nbt = object : MetadataType<Any?>() {
            override fun read(buffer: PacketBuffer) = buffer.nbtVarIntObjectMapper.readValue<Any?>(ByteBufInputStream(buffer))

            override fun write(buffer: PacketBuffer, value: Any?) {
                buffer.nbtVarIntObjectMapper.writeValue(ByteBufOutputStream(buffer) as OutputStream, value)
            }
        }

        val Int3 = object : MetadataType<Int3>() {
            override fun read(buffer: PacketBuffer) = buffer.readInt3()

            override fun write(buffer: PacketBuffer, value: Int3) {
                buffer.writeInt3(value)
            }
        }

        val Long = object : MetadataType<Long>() {
            override fun read(buffer: PacketBuffer) = buffer.readVarLong()

            override fun write(buffer: PacketBuffer, value: Long) {
                buffer.writeVarLong(value)
            }
        }

        val Float3 = object : MetadataType<Float3>() {
            override fun read(buffer: PacketBuffer) = buffer.readFloat3()

            override fun write(buffer: PacketBuffer, value: Float3) {
                buffer.writeFloat3(value)
            }
        }

        val Flags = object : MetadataType<Set<Flag>>() {
            override fun read(buffer: PacketBuffer) = buffer.readVarLongFlags<Flag>()

            override fun write(buffer: PacketBuffer, value: Set<Flag>) {
                buffer.writeVarLongFlags(value)
            }
        }

        val Flags2 = object : MetadataType<Set<Flag2>>() {
            override fun read(buffer: PacketBuffer) = buffer.readVarLongFlags<Flag2>()

            override fun write(buffer: PacketBuffer, value: Set<Flag2>) {
                buffer.writeVarLongFlags(value)
            }
        }

        val registry = Int2ObjectOpenHashBiMap<MetadataType<*>>().apply {
            this[0] = Byte
            this[1] = Short
            this[2] = Int
            this[3] = Float
            this[4] = String
            this[5] = Nbt
            this[6] = Int3
            this[7] = Flags
            this[7] = Flags2
            this[7] = Long
            this[8] = Float3
        }
    }
}
