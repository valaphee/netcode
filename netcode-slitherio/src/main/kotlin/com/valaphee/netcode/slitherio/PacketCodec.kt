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

import com.valaphee.netcode.slitherio.client.AnglePacket
import com.valaphee.netcode.slitherio.client.ChallengeResponsePacket
import com.valaphee.netcode.slitherio.client.ConnectPacket
import com.valaphee.netcode.slitherio.client.LoginPacket
import com.valaphee.netcode.slitherio.client.PingPacket
import com.valaphee.netcode.slitherio.server.ChallengePacket
import com.valaphee.netcode.slitherio.server.DisconnectPacket
import com.valaphee.netcode.slitherio.server.LeaderboardPacket
import com.valaphee.netcode.slitherio.server.PongPacket
import com.valaphee.netcode.slitherio.server.PreyPacket
import com.valaphee.netcode.slitherio.server.SnakePacket
import com.valaphee.netcode.slitherio.server.WorldPacket
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageCodec
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame

/**
 * @author Kevin Ludwig
 */
class PacketCodec(
    private val client: Boolean
) : MessageToMessageCodec<BinaryWebSocketFrame, Packet>() {
    var loggedIn = false

    override fun encode(context: ChannelHandlerContext, `in`: Packet, out: MutableList<Any>) {
        out.add(BinaryWebSocketFrame(PacketBuffer(context.alloc().buffer()).apply {
            if (client) TODO() else {
                writeShort(0)
                when (`in`) {
                    is ChallengePacket -> {
                        writeByte('6'.code)
                        writeBytes(`in`.challenge.toByteArray(Charsets.UTF_8))
                    }
                    is WorldPacket -> {
                        writeByte('a'.code)
                        writeMedium(`in`.radius)
                        writeShort(`in`.maximumSnakeLength)
                        writeShort(`in`.sectorSize)
                        writeShort(`in`.sectorCountAlongEdge)
                        writeByte((`in`.angularSpeedCoef * 10).toInt())
                        writeShort((`in`.nodeSpeed1 * 100).toInt())
                        writeShort((`in`.nodeSpeed2 * 100).toInt())
                        writeShort((`in`.nodeSpeed3 * 100).toInt())
                        writeShort((`in`.angularSpeed * 1000).toInt())
                        writeShort((`in`.preyTurnSpeed * 1000).toInt())
                        writeShort((`in`.snakeTailSpeedRatio * 1000).toInt())
                        writeByte(`in`.protocolVersion)
                    }
                    is LeaderboardPacket -> {
                        writeByte('l'.code)
                        writeByte(0)
                        writeShort(`in`.rank)
                        writeShort(`in`.playerCount)
                        `in`.entries.forEach {
                            writeShort(it.snakeLength)
                            writeMedium((it.snakeTailFullness * 0xFFFFFF).toInt())
                            writeByte(it.color)
                            writeByte(it.name.length)
                            writeBytes(it.name.toByteArray(Charsets.UTF_8))
                        }
                    }
                    is PongPacket -> writeByte('p'.code)
                    is SnakePacket -> {
                        writeByte('s'.code)
                        writeShort(`in`.snakeId)
                        when (`in`.action) {
                            SnakePacket.Action.Add -> {
                                writeAngle(`in`.wantedAngle)
                                writeByte(0)
                                writeAngle(`in`.angle)
                                writeShort((`in`.speed * 1000).toInt())
                                writeMedium((`in`.tailFullness * 0xFFFFFF).toInt())
                                writeByte(`in`.skin)
                                val (x, y) = `in`.position
                                writeMedium((x * 5).toInt())
                                writeMedium((y * 5).toInt())
                                writeByte(`in`.name.length)
                                writeBytes(`in`.name.toByteArray(Charsets.UTF_8))
                                writeByte(0)
                                val (tailX, tailY) = `in`.tail
                                writeMedium((tailX * 5).toInt())
                                writeMedium((tailY * 5).toInt())
                                `in`.parts.forEach { (x, y) ->
                                    writeByte(((x + 127) * 2).toInt())
                                    writeByte(((y + 127) * 2).toInt())
                                }
                            }
                            SnakePacket.Action.OutOfRange -> writeByte(0)
                            SnakePacket.Action.Died -> writeByte(1)
                        }
                    }
                    is DisconnectPacket -> {
                        writeByte('v'.code)
                        writeByte(`in`.reason.ordinal)
                    }
                    is PreyPacket -> {
                        writeByte('y'.code)
                        writeShort(`in`.preyId)
                        when (`in`.action) {
                            PreyPacket.Action.Add -> {
                                writeByte(`in`.color)
                                writeMedium((`in`.x * 5).toInt())
                                writeMedium((`in`.y * 5).toInt())
                                writeByte((`in`.size * 5).toInt())
                                writeAngle(`in`.wantedAngle)
                                writeAngle(`in`.angle)
                                writeShort((`in`.speed * 1000).toInt())
                            }
                            PreyPacket.Action.Eat -> buffer.writeShort(`in`.snakeId)
                        }
                    }
                }
            }
        }))
    }

    override fun decode(context: ChannelHandlerContext, `in`: BinaryWebSocketFrame, out: MutableList<Any>) {
        val buffer = PacketBuffer(`in`.content())
        val id = buffer.readUnsignedByte().toInt()
        val index = buffer.readerIndex()
        try {
            out.add(if (client) TODO() else {
                if (loggedIn) when (id) {
                    in 0..250 -> AnglePacket(id * 360 / 250f)
                    251 -> PingPacket
                    else -> UnknownPacket(PacketBuffer(buffer.retainedSlice()))
                } else when (id) {
                    'Y'.code -> ChallengeResponsePacket(ByteArray(buffer.readableBytes()).apply { buffer.readBytes(this) }.toString(Charsets.UTF_8))
                    'c'.code -> ConnectPacket
                    's'.code -> LoginPacket(buffer.readUnsignedByte().toInt(), buffer.readUnsignedByte().toInt(), ByteArray(buffer.readUnsignedByte().toInt()).apply { buffer.readBytes(this) }.toString(Charsets.UTF_8))
                    else -> UnknownPacket(PacketBuffer(buffer.retainedSlice()))
                }
            })
        } catch (ex: Exception) {
            throw PacketDecoderException("Packet $id problematic at 0x${buffer.readerIndex().toString(16).uppercase()}", ex, buffer).also { out.add(UnknownPacket(PacketBuffer(buffer.readerIndex(index).retainedSlice()))) }
        }
        if (buffer.readableBytes() > 0) throw PacketDecoderException("Packet $id not fully read", buffer) else Unit
    }

    companion object {
        const val NAME = "sltherio-packet-codec"
    }
}
