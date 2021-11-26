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

import com.valaphee.netcode.slitherio.base.ChallengeResponsePacketReader
import com.valaphee.netcode.slitherio.base.ConnectPacketReader
import com.valaphee.netcode.slitherio.base.LoginPacketReader
import com.valaphee.netcode.slitherio.base.PingPacketReader
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageCodec
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap

/**
 * @author Kevin Ludwig
 */
class PacketCodec(
    private val client: Boolean
) : MessageToMessageCodec<BinaryWebSocketFrame, Packet>() {
    override fun encode(context: ChannelHandlerContext, `in`: Packet, out: MutableList<Any>) {
        out.add(BinaryWebSocketFrame(PacketBuffer(context.alloc().buffer()).apply {
            writeShort(0)
            writeByte(`in`.id.code)
            `in`.write(this)
        }))
    }

    override fun decode(context: ChannelHandlerContext, `in`: BinaryWebSocketFrame, out: MutableList<Any>) {
        val buffer = PacketBuffer(`in`.content())
        readers[buffer.readByte().toInt().toChar()]?.let { out.add(it.read(buffer)) }
    }

    companion object {
        const val NAME = "sltherio-packet-codec"
        private val readers = Char2ObjectOpenHashMap<PacketReader>().apply {
            this['c'] = ConnectPacketReader
            this['Y'] = ChallengeResponsePacketReader
            this['s'] = LoginPacketReader
            this[0xFB.toChar()] = PingPacketReader
        }
    }
}
