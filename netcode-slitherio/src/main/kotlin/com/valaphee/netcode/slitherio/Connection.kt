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

package com.valaphee.netcode.slitherio

import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.timeout.ReadTimeoutException
import org.apache.logging.log4j.LogManager
import java.net.InetSocketAddress

/**
 * @author Kevin Ludwig
 */
class Connection : SimpleChannelInboundHandler<Packet>() {
    lateinit var context: ChannelHandlerContext
        private set
    lateinit var handler: PacketHandler
        private set
    private var notClosed = true

    override fun channelActive(context: ChannelHandlerContext) {
        super.channelActive(context)
        this.context = context
        handler.initialize()
    }

    override fun channelInactive(context: ChannelHandlerContext) {
        handler.destroy()
        super.channelInactive(context)
    }

    override fun channelWritabilityChanged(context: ChannelHandlerContext) {
        handler.writabilityChanged()
        super.channelWritabilityChanged(context)
    }

    override fun exceptionCaught(context: ChannelHandlerContext, cause: Throwable) {
        if (context.channel().isActive) {
            when (cause) {
                is ReadTimeoutException -> {
                    log.warn("{}: Read timed out", handler)
                    context.close()
                }
                else -> log.error("{}: Unhandled exception caught", handler, cause)
            }
            try {
                handler.exceptionCaught(cause)
            } catch (thrown: Throwable) {
                log.error("{}: Exception processing exception", handler, cause)
            }
        }
    }

    override fun channelRead0(context: ChannelHandlerContext, packet: Packet) {
        if (notClosed) {
            println("In: ${packet}")
            packet.handle(handler)
        }
    }

    val address get() = context.channel().remoteAddress() as InetSocketAddress

    fun setHandler(handler: PacketHandler) {
        if (this::handler.isInitialized) {
            this.handler.destroy()
            this.handler = handler
            this.handler.initialize()
        } else this.handler = handler
    }

    fun write(packet: Packet) {
        if (notClosed) {
            println("Out: ${packet}")
            context.writeAndFlush(packet, context.voidPromise())
        }
    }

    fun close(packet: Packet? = null) {
        if (notClosed) {
            notClosed = false
            if (packet != null && context.channel().isActive) {
                println("Out: ${packet}")
                context.writeAndFlush(packet).addListeners(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE, ChannelFutureListener.CLOSE)
            } else {
                context.flush()
                context.close()
            }
        }
    }

    fun closeInternal() {
        notClosed = false
    }

    companion object {
        private val log = LogManager.getLogger(Connection::class.java)
    }
}