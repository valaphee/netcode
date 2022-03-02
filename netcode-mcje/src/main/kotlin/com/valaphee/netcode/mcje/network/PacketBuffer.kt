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

@file:Suppress("NOTHING_TO_INLINE")

package com.valaphee.netcode.mcje.network

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.valaphee.foundry.math.Double3
import com.valaphee.foundry.math.Float2
import com.valaphee.foundry.math.Float3
import com.valaphee.foundry.math.Int2
import com.valaphee.foundry.math.Int3
import com.valaphee.jackson.dataformat.nbt.NbtFactory
import com.valaphee.netcode.mc.util.Direction
import com.valaphee.netcode.mcje.RegistrySet
import com.valaphee.netcode.mcje.util.NamespacedKey
import com.valaphee.netcode.mcje.util.minecraftKey
import com.valaphee.netcode.util.ByteBufWrapper
import io.netty.buffer.ByteBuf
import java.nio.charset.StandardCharsets
import java.util.EnumSet
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
class PacketBuffer(
    buffer: ByteBuf,
    val jsonObjectMapper: ObjectMapper = jacksonObjectMapper(),
    val nbtObjectMapper: ObjectMapper = ObjectMapper(NbtFactory()),
    val registrySet: RegistrySet
) : ByteBufWrapper(buffer) {
    inline fun <reified T : Enum<T>> readByteFlags(): Set<T> {
        val flagsValue = readByte().toInt()
        return EnumSet.noneOf(T::class.java).apply { enumValues<T>().filter { (flagsValue and (1 shl it.ordinal)) != 0 }.forEach { add(it) } }
    }

    inline fun <T : Enum<T>> writeByteFlags(flags: Set<T>) = writeByte(flags.map { 1 shl it.ordinal }.fold(0) { flagsValue, flagValue -> flagsValue or flagValue })

    fun readUuid() = UUID(readLong(), readLong())

    fun writeUuid(value: UUID) {
        writeLong(value.mostSignificantBits)
        writeLong(value.leastSignificantBits)
    }

    fun readVarInt(): Int {
        var value = 0
        var shift = 0
        while (shift <= 35) {
            val head = readByte().toInt()
            value = value or ((head and 0x7F) shl shift)
            if (head and 0x80 == 0) return value
            shift += 7
        }
        throw ArithmeticException("VarInt wider than 35-bit")
    }

    fun writeVarInt(value: Int) {
        @Suppress("NAME_SHADOWING") var value = value
        while (true) {
            if (value and 0x7F.inv() == 0) {
                writeByte(value)
                return
            } else {
                writeByte((value and 0x7F) or 0x80)
                value = value ushr 7
            }
        }
    }

    fun readVarLong(): Long {
        var value: Long = 0
        var shift = 0
        while (shift <= 70) {
            val head = readByte().toInt()
            value = value or ((head and 0x7F).toLong() shl shift)
            if (head and 0x80 == 0) return value
            shift += 7
        }
        throw ArithmeticException("VarLong wider than 70-bit")
    }

    fun writeVarLong(value: Long) {
        @Suppress("NAME_SHADOWING") var value = value
        while (true) {
            if (value and 0x7FL.inv() == 0L) {
                writeByte(value.toInt())
                return
            } else {
                writeByte((value.toInt() and 0x7F) or 0x80)
                value = value ushr 7
            }
        }
    }

    fun readByteArray(maximumLength: Int = Short.MAX_VALUE.toInt()): ByteArray {
        val length = readVarInt()
        check(length <= readableBytes()) { "Length of $length exceeds ${readableBytes()}" }
        check(length <= maximumLength) { "Length of $length exceeds $maximumLength" }
        val bytes = ByteArray(length)
        readBytes(bytes)
        return bytes
    }

    fun writeByteArray(value: ByteArray) {
        writeVarInt(value.size)
        writeBytes(value)
    }

    fun readString(maximumLength: Int = Short.MAX_VALUE.toInt()) = String(readByteArray(maximumLength), StandardCharsets.UTF_8)

    fun writeString(value: String) {
        writeByteArray(value.toByteArray(StandardCharsets.UTF_8))
    }

    fun readNamespacedKey() = minecraftKey(readString())

    fun writeNamespacedKey(value: NamespacedKey) {
        writeString(value.toString())
    }

    fun readDirection() = Direction.values()[readVarInt() and 0x7]

    fun writeDirection(value: Direction) {
        writeVarInt(value.ordinal)
    }

    fun readInt2() = Int2(readVarInt(), readVarInt())

    fun writeInt2(value: Int2) {
        writeVarInt(value.x)
        writeVarInt(value.y)
    }

    fun readInt3() = Int3(readInt(), readInt(), readInt())

    fun writeInt3(value: Int3) {
        writeInt(value.x)
        writeInt(value.y)
        writeInt(value.z)
    }

    fun readInt3UnsignedY(): Int3 {
        val value = readLong()
        return Int3((value shr 38).toInt(), (value and 0xFFF).toInt(), (value shl 26 shr 38).toInt())
    }

    fun writeInt3UnsignedY(value: Int3) {
        writeLong(((value.x and 0x3FFFFFF).toLong() shl 38) or ((value.z and 0x3FFFFFF).toLong() shl 12) or (value.y and 0xFFF).toLong())
    }

    fun readAngle() = readByte() * 360 / 256f

    fun writeAngle(value: Float) {
        writeByte((value * 256 / 360).toInt())
    }

    fun readAngle2() = Float2(readAngle(), readAngle())

    fun writeAngle2(value: Float2) {
        writeAngle(value.x)
        writeAngle(value.y)
    }

    fun readFloat2() = Float2(readFloat(), readFloat())

    fun writeFloat2(value: Float2) {
        writeFloat(value.x)
        writeFloat(value.y)
    }

    fun readFloat3() = Float3(readFloat(), readFloat(), readFloat())

    fun writeFloat3(value: Float3) {
        writeFloat(value.x)
        writeFloat(value.y)
        writeFloat(value.z)
    }

    fun readDouble3() = Double3(readDouble(), readDouble(), readDouble())

    fun writeDouble3(value: Double3) {
        writeDouble(value.x)
        writeDouble(value.y)
        writeDouble(value.z)
    }
}
