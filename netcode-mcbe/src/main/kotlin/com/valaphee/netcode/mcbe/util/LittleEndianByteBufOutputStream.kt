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

package com.valaphee.netcode.mcbe.util

import io.netty.buffer.ByteBuf
import java.io.DataOutput
import java.io.OutputStream
import java.nio.charset.StandardCharsets

/**
 * @author Kevin Ludwig
 */
open class LittleEndianByteBufOutputStream(
    private val buffer: ByteBuf
) : OutputStream(), DataOutput {
    private val startIndex = buffer.writerIndex()

    fun writtenBytes() = buffer.writerIndex() - startIndex

    override fun write(value: Int) {
        buffer.writeByte(value)
    }

    override fun write(value: ByteArray) {
        buffer.writeBytes(value)
    }

    override fun write(value: ByteArray, offset: Int, length: Int) {
        if (length != 0) buffer.writeBytes(value, offset, length)
    }

    override fun writeBoolean(value: Boolean) {
        buffer.writeBoolean(value)
    }

    override fun writeByte(value: Int) {
        buffer.writeByte(value)
    }

    override fun writeShort(value: Int) {
        buffer.writeShortLE(value)
    }

    override fun writeChar(value: Int) {
        buffer.writeShortLE(value)
    }

    override fun writeInt(value: Int) {
        buffer.writeIntLE(value)
    }

    override fun writeLong(value: Long) {
        buffer.writeLongLE(value)
    }

    override fun writeFloat(value: Float) {
        buffer.writeFloatLE(value)
    }

    override fun writeDouble(value: Double) {
        buffer.writeDoubleLE(value)
    }

    override fun writeBytes(value: String) {
        buffer.writeCharSequence(value, StandardCharsets.US_ASCII)
    }

    override fun writeChars(value: String) {
        value.indices.forEach { buffer.writeChar(value[it].code) }
    }

    override fun writeUTF(value: String) {
        value.toByteArray(StandardCharsets.UTF_8).run {
            writeShort(size)
            write(this)
        }
    }

    fun buffer() = buffer
}
