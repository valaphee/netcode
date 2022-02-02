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

package com.valaphee.netcode.mcbe

import com.valaphee.netcode.util.Compressor
import com.valaphee.netcode.util.ZlibCompressor
import com.valaphee.netcode.util.compressor
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.unix.Buffer
import io.netty.handler.codec.MessageToMessageDecoder
import io.netty.util.ReferenceCountUtil

/**
 * @author Kevin Ludwig
 */
class Decompressor : MessageToMessageDecoder<ByteBuf>() {
    private lateinit var compressor: Compressor

    override fun handlerAdded(context: ChannelHandlerContext) {
        super.handlerAdded(context)
        compressor = compressor(false, true)
    }

    override fun handlerRemoved(context: ChannelHandlerContext) {
        compressor.close()
        super.handlerRemoved(context)
    }

    override fun decode(context: ChannelHandlerContext, `in`: ByteBuf, out: MutableList<Any>) {
        var out0: ByteBuf? = null
        try {
            if (compressor is ZlibCompressor) {
                val zlibCompressor: ZlibCompressor = compressor as ZlibCompressor
                val allocator = context.alloc()
                val out1 = allocator.compositeDirectBuffer(128).also { out0 = it }
                val buffers = `in`.nioBuffers()
                var bufferIndex = 0
                var temporaryOut = allocator.directBuffer(chunkSize, chunkSize)
                while (!zlibCompressor.isFinished && bufferIndex != buffers.size) {
                    if (chunkFloor > temporaryOut.writableBytes()) {
                        out1.addComponent(true, temporaryOut)
                        temporaryOut = allocator.directBuffer(chunkSize, chunkSize)
                    }
                    val buffer = buffers[bufferIndex.coerceAtMost(buffers.size - 1)]
                    temporaryOut.writerIndex(temporaryOut.writerIndex() + zlibCompressor.process(Buffer.memoryAddress(buffer) + buffer.position(), buffer.remaining(), temporaryOut.memoryAddress() + temporaryOut.writerIndex(), temporaryOut.writableBytes()))
                    buffer.position(buffer.position() + zlibCompressor.consumed)
                    if (!buffer.hasRemaining()) bufferIndex++
                }
                if (temporaryOut.isReadable) out1.addComponent(true, temporaryOut) else temporaryOut.release()
                zlibCompressor.reset()
            } else {
                out0 = context.alloc().ioBuffer(`in`.readableBytes() shl 2)
                compressor.process(`in`, out0!!)
            }
            while (out0!!.isReadable) out.add(out0!!.readRetainedSlice(readVarInt(out0)))
        } finally {
            out0?.let { ReferenceCountUtil.safeRelease(out0) }
        }
    }

    private fun readVarInt(buffer: ByteBuf?): Int {
        var value = 0
        var shift = 0
        while (35 >= shift) {
            val head = buffer!!.readByte().toInt()
            value = value or ((head and 0x7F) shl shift)
            if (0 == head and 0x80) return value
            shift += 7
        }
        throw ArithmeticException("VarInt wider than 35-bit")
    }

    companion object {
        const val NAME = "mcbe-decompressor"
        private const val chunkSize = 8192
        private const val chunkFloor = 512
    }
}
