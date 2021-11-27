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

@file:Suppress("NOTHING_TO_INLINE")

package com.valaphee.netcode.mcje

import com.fasterxml.jackson.databind.ObjectMapper
import com.valaphee.foundry.math.Double3
import com.valaphee.foundry.math.Float2
import com.valaphee.foundry.math.Float3
import com.valaphee.foundry.math.Int2
import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mc.util.Direction
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
    objectMapper: ObjectMapper? = null
) : ByteBufWrapper(buffer) {
    lateinit var objectMapper: ObjectMapper

    init {
        objectMapper?.let { this.objectMapper = it }
    }

    inline fun <reified T : Enum<T>> readByteFlags(): Collection<T> {
        val flagsValue = readByte().toInt()
        return EnumSet.noneOf(T::class.java).apply { enumValues<T>().filter { (flagsValue and (1 shl it.ordinal)) != 0 }.forEach { add(it) } }
    }

    inline fun <T : Enum<T>> writeByteFlags(flags: Collection<T>) {
        writeByte(flags.map { 1 shl it.ordinal }.fold(0) { flagsValue, flagValue -> flagsValue or flagValue })
    }

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

    fun setMaximumLengthVarInt(index: Int, value: Int) {
        setBytes(
            index, byteArrayOf(
                (value and 0x7F or 0x80).toByte(),
                (value ushr 7 and 0x7F or 0x80).toByte(),
                (value ushr 14 and 0x7F or 0x80).toByte(),
                (value ushr 21 and 0x7F or 0x80).toByte(),
                (value ushr 28 and 0x7F).toByte()
            )
        )
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
        check(length <= maximumLength) { "Maximum length of $maximumLength exceeded" }
        val bytes = ByteArray(length)
        readBytes(bytes)
        return bytes
    }

    fun writeByteArray(value: ByteArray) {
        writeVarInt(value.size)
        writeBytes(value)
    }

    @JvmOverloads
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

    companion object {
        const val MaximumVarIntLength = 5
    }
}