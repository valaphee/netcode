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

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageDecoder
import io.netty.util.ReferenceCountUtil
import java.util.zip.Inflater

/**
 * @author Kevin Ludwig
 */
class Decompressor : MessageToMessageDecoder<ByteBuf>() {
    private val buffer = ByteArray(8192)

    private lateinit var inflater: Inflater

    override fun handlerAdded(context: ChannelHandlerContext) {
        super.handlerAdded(context)
        inflater = Inflater(true)
    }

    override fun handlerRemoved(context: ChannelHandlerContext) {
        inflater.end()
        super.handlerRemoved(context)
    }

    override fun decode(context: ChannelHandlerContext, `in`: ByteBuf, out: MutableList<Any>) {
        var out0: ByteBuf? = null
        try {
            out0 = context.alloc().ioBuffer(`in`.readableBytes() shl 2)
            inflater.setInput(`in`.nioBuffer())
            while (!inflater.finished() && inflater.totalIn < `in`.readableBytes()) out0.writeBytes(buffer, 0, inflater.inflate(buffer))
            inflater.reset()
            while (out0.isReadable) out.add(out0.readRetainedSlice(readVarInt(out0)))
        } finally {
            out0?.let { ReferenceCountUtil.safeRelease(out0) }
        }
    }

    private fun readVarInt(buffer: ByteBuf): Int {
        var value = 0
        var shift = 0
        while (35 >= shift) {
            val head = buffer.readByte().toInt()
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
