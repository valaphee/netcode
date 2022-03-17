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

package com.valaphee.netcode.mcbe.network

import com.fasterxml.jackson.databind.ObjectMapper
import com.valaphee.foundry.math.Float2
import com.valaphee.foundry.math.Float3
import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mcbe.util.Registries
import com.valaphee.netcode.network.ByteBufWrapper
import io.netty.buffer.ByteBuf
import io.netty.util.AsciiString
import java.nio.charset.StandardCharsets
import java.util.EnumSet
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
class PacketBuffer(
    buffer: ByteBuf,
    val jsonObjectMapper: ObjectMapper,
    val nbtObjectMapper: ObjectMapper,
    val nbtVarIntObjectMapper: ObjectMapper,
    val nbtVarIntNoWrapObjectMapper: ObjectMapper,
    registries: Registries? = null
) : ByteBufWrapper(buffer) {
    lateinit var registries: Registries

    init {
        registries?.let { this.registries = it }
    }

    inline fun <reified T : Enum<T>> readByteFlags(): Set<T> {
        val flagsValue = readByte().toInt()
        return EnumSet.noneOf(T::class.java).apply { enumValues<T>().filter { (flagsValue and (1 shl it.ordinal)) != 0 }.forEach { add(it) } }
    }

    fun <T : Enum<T>> writeByteFlags(flags: Set<T>) = writeByte(flags.map { 1 shl it.ordinal }.fold(0) { flagsValue, flagValue -> flagsValue or flagValue })

    inline fun <reified T : Enum<T>> readShortLEFlags(): Set<T> {
        val flagsValue = readUnsignedShortLE()
        return EnumSet.noneOf(T::class.java).apply { enumValues<T>().filter { (flagsValue and (1 shl it.ordinal)) != 0 }.forEach { add(it) } }
    }

    fun <T : Enum<T>> writeShortLEFlags(flags: Set<T>) = writeShortLE(flags.map { 1 shl it.ordinal }.fold(0) { flagsValue, flagValue -> flagsValue or flagValue })

    fun readUuid() = UUID(readLongLE(), readLongLE())

    fun writeUuid(value: UUID) {
        writeLongLE(value.mostSignificantBits)
        writeLongLE(value.leastSignificantBits)
    }

    fun readString16(): String {
        val length = readUnsignedShortLE()
        check(length <= readableBytes()) { "Length of $length exceeds ${readableBytes()}" }
        val bytes = ByteArray(length)
        readBytes(bytes)
        return String(bytes, StandardCharsets.UTF_8)
    }

    fun writeString16(value: String) {
        val bytes = value.toByteArray(StandardCharsets.UTF_8)
        writeShortLE(bytes.size)
        writeBytes(bytes)
    }

    fun readAsciiStringLe(): AsciiString {
        val length = readIntLE()
        check(length <= readableBytes()) { "Length of $length exceeds ${readableBytes()}" }
        val bytes = ByteArray(length)
        readBytes(bytes)
        return AsciiString(bytes)
    }

    fun writeAsciiStringLe(value: AsciiString) {
        writeIntLE(value.length)
        writeBytes(value.toByteArray())
    }

    fun readVarUInt(): Int {
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

    fun writeVarUInt(value: Int) {
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

    fun setMaximumLengthVarUInt(index: Int, value: Int) {
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

    inline fun <reified T : Enum<T>> readVarUIntFlags(): Set<T> {
        val flagsValue = readVarUInt()
        return EnumSet.noneOf(T::class.java).apply { enumValues<T>().filter { (flagsValue and (1 shl it.ordinal)) != 0 }.forEach { add(it) } }
    }

    fun <T : Enum<T>> writeVarUIntFlags(flags: Set<T>) = writeVarUInt(flags.map { 1 shl it.ordinal }.fold(0) { flagsValue, flagValue -> flagsValue or flagValue })

    fun readVarInt(): Int {
        val value = readVarUInt()
        return (value ushr 1) xor -(value and 1)
    }

    fun writeVarInt(value: Int) {
        writeVarUInt((value shl 1) xor (value shr 31))
    }

    fun readVarULong(): Long {
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

    fun writeVarULong(value: Long) {
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

    inline fun <reified T : Enum<T>> readVarULongFlags(): Set<T> {
        val flagsValue = readVarULong()
        return EnumSet.noneOf(T::class.java).apply { enumValues<T>().filter { (flagsValue and (1L shl it.ordinal)) != 0L }.forEach { add(it) } }
    }

    fun <T : Enum<T>> writeVarULongFlags(flags: Set<T>) = writeVarULong(flags.map { 1L shl it.ordinal }.fold(0) { flagsValue, flagValue -> flagsValue or flagValue })

    fun readVarLong(): Long {
        val value = readVarULong()
        return (value ushr 1) xor -(value and 1)
    }

    fun writeVarLong(value: Long) {
        writeVarULong((value shl 1) xor (value shr 63))
    }

    inline fun <reified T : Enum<T>> readVarLongFlags(): Set<T> {
        val flagsValue = readVarLong()
        return EnumSet.noneOf(T::class.java).apply { enumValues<T>().filter { (flagsValue and (1L shl it.ordinal)) != 0L }.forEach { add(it) } }
    }

    fun <T : Enum<T>> writeVarLongFlags(flags: Set<T>) = writeVarLong(flags.map { 1L shl it.ordinal }.fold(0) { flagsValue, flagValue -> flagsValue or flagValue })

    fun readByteArray(): ByteArray {
        val length = readVarUInt()
        check(length <= readableBytes()) { "Length of $length exceeds ${readableBytes()}" }
        val bytes = ByteArray(length)
        readBytes(bytes)
        return bytes
    }

    fun readByteArrayOfExpectedLength(expectedLength: Int): ByteArray {
        val length = readVarUInt()
        check(length == expectedLength) { "Expected length of $expectedLength, was $length" }
        val bytes = ByteArray(length)
        readBytes(bytes)
        return bytes
    }

    fun writeByteArray(value: ByteArray) {
        writeVarUInt(value.size)
        writeBytes(value)
    }

    fun readString() = String(readByteArray(), StandardCharsets.UTF_8)

    fun writeString(value: String) {
        writeByteArray(value.toByteArray(StandardCharsets.UTF_8))
    }

    fun readInt3() = Int3(readVarInt(), readVarInt(), readVarInt())

    fun writeInt3(value: Int3) {
        writeVarInt(value.x)
        writeVarInt(value.y)
        writeVarInt(value.z)
    }

    fun readInt3UnsignedY() = Int3(readVarInt(), readVarUInt(), readVarInt())

    fun writeInt3UnsignedY(value: Int3) {
        writeVarInt(value.x)
        writeVarUInt(value.y)
        writeVarInt(value.z)
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

    fun readFloat2() = Float2(readFloatLE(), readFloatLE())

    fun writeFloat2(value: Float2) {
        writeFloatLE(value.x)
        writeFloatLE(value.y)
    }

    fun readFloat3() = Float3(readFloatLE(), readFloatLE(), readFloatLE())

    fun writeFloat3(value: Float3) {
        writeFloatLE(value.x)
        writeFloatLE(value.y)
        writeFloatLE(value.z)
    }

    companion object {
        const val MaximumVarUIntLength = 5
    }
}
