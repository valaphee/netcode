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
import io.netty.buffer.CompositeByteBuf
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelOutboundHandlerAdapter
import io.netty.channel.ChannelPromise
import io.netty.util.ReferenceCountUtil
import network.ycc.raknet.pipeline.FlushTickHandler
import java.util.zip.Deflater

/**
 * @author Kevin Ludwig
 */
class Compressor(
    val level: Int = 7
) : ChannelOutboundHandlerAdapter() {
    private val buffer = ByteArray(8192)

    private lateinit var deflater: Deflater
    private lateinit var `in`: CompositeByteBuf
    private lateinit var out: CompositeByteBuf
    private var dirty = false

    override fun handlerAdded(context: ChannelHandlerContext) {
        super.handlerAdded(context)
        deflater = Deflater(level, true)
        val allocator = context.alloc()
        `in` = allocator.compositeDirectBuffer(componentMaximum)
        out = allocator.compositeDirectBuffer(componentMaximum)
    }

    override fun handlerRemoved(context: ChannelHandlerContext) {
        ReferenceCountUtil.safeRelease(out)
        ReferenceCountUtil.safeRelease(`in`)
        deflater.end()
        super.handlerRemoved(context)
    }

    override fun write(context: ChannelHandlerContext, message: Any, promise: ChannelPromise) {
        if (message is ByteBuf) {
            try {
                promise.trySuccess()
                if (!message.isReadable) return
                if (poolByteMaximum < `in`.readableBytes()) _flush(context)
                val headerBuffer = context.alloc().directBuffer(8, 8)
                writeVarInt(message.readableBytes(), headerBuffer)
                `in`.addComponent(true, headerBuffer)
                `in`.addComponent(true, message.retain())
                dirty = true
                if (poolByteMaximum < out.readableBytes()) _flush(context)
                FlushTickHandler.checkFlushTick(context.channel())
            } finally {
                ReferenceCountUtil.safeRelease(message)
            }
        } else super.write(context, message, promise)
    }

    override fun flush(context: ChannelHandlerContext) {
        if (dirty) _flush(context)
        super.flush(context)
    }

    private fun _flush(context: ChannelHandlerContext) {
        dirty = false
        val allocator = context.alloc()
        val out = allocator.directBuffer(`in`.readableBytes() / 4 + 16)
        deflater.setInput(`in`.nioBuffer())
        deflater.finish()
        while (!deflater.finished()) out.writeBytes(buffer, 0, deflater.deflate(buffer))
        deflater.reset()
        `in`.release()
        `in` = allocator.compositeDirectBuffer(componentMaximum)
        context.write(out).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE)
    }

    private fun writeVarInt(value: Int, buffer: ByteBuf) {
        @Suppress("NAME_SHADOWING") var value = value
        while (true) {
            if (value and 0x7F.inv() == 0) {
                buffer.writeByte(value)
                return
            } else {
                buffer.writeByte((value and 0x7F) or 0x80)
                value = value ushr 7
            }
        }
    }

    companion object {
        const val NAME = "mcbe-compressor"
        private const val componentMaximum = 512
        private const val poolByteMaximum = 128 * 1024
    }
}
