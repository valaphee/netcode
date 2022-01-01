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

package com.valaphee.netcode.util

import io.netty.buffer.ByteBuf
import java.io.Closeable
import java.util.zip.Deflater
import java.util.zip.Inflater

/**
 * @author Kevin Ludwig
 */
interface Compressor : Closeable {
    fun process(`in`: ByteBuf, out: ByteBuf)
}

fun compressor(compress: Boolean, raw: Boolean, level: Int = 7): Compressor = JavaZipCompressor(compress, raw, level)

/**
 * @author Kevin Ludwig
 */
class JavaZipCompressor(
    private val compress: Boolean,
    raw: Boolean,
    level: Int
) : Compressor {
    private val buffer = ByteArray(8192)
    private val deflater: Deflater?
    private val inflater: Inflater?

    init {
        if (compress) {
            deflater = Deflater(level, raw)
            inflater = null
        } else {
            deflater = null
            inflater = Inflater(raw)
        }
    }

    override fun process(`in`: ByteBuf, out: ByteBuf) {
        val inData = ByteArray(`in`.readableBytes())
        `in`.readBytes(inData)
        if (compress) {
            deflater!!.setInput(inData)
            deflater.finish()
            while (!deflater.finished()) out.writeBytes(buffer, 0, deflater.deflate(buffer))
            deflater.reset()
        } else {
            inflater!!.setInput(inData)
            while (!inflater.finished() && inflater.totalIn < inData.size) out.writeBytes(buffer, 0, inflater.inflate(buffer))
            inflater.reset()
        }
    }

    override fun close() {
        if (compress) deflater!!.end() else inflater!!.end()
    }
}

class ZlibCompressor(
    private val compress: Boolean,
    raw: Boolean,
    level: Int
) : Compressor {
    private val impl = ZlibCompressorImpl()
    private var zStream = impl.init(compress, level, raw)

    override fun process(`in`: ByteBuf, out: ByteBuf) {
        while (!impl.finished && (compress || `in`.isReadable)) {
            out.ensureWritable(8192)
            val processed = impl.process(zStream, `in`.memoryAddress() + `in`.readerIndex(), `in`.readableBytes(), out.memoryAddress() + out.writerIndex(), out.writableBytes(), compress)
            `in`.readerIndex(`in`.readerIndex() + impl.consumed)
            out.writerIndex(out.writerIndex() + processed)
        }
        reset()
    }

    fun process(`in`: Long, inLength: Int, out: Long, outLength: Int) = impl.process(zStream, `in`, inLength, out, outLength, compress)

    val consumed get() = impl.consumed

    val isFinished get() = impl.finished

    fun reset() {
        impl.reset(zStream, compress)
        impl.consumed = 0
        impl.finished = false
    }

    override fun close() {
        impl.free(zStream, compress)
        zStream = 0
        impl.consumed = 0
        impl.finished = false
    }
}
