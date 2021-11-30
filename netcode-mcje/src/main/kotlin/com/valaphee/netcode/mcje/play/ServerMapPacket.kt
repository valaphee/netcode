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

package com.valaphee.netcode.mcje.play

import com.valaphee.netcode.mcje.Packet
import com.valaphee.netcode.mcje.PacketBuffer
import com.valaphee.netcode.mcje.world.map.Decoration

/**
 * @author Kevin Ludwig
 */
class ServerMapPacket(
    val mapId: Int,
    val scale: Int,
    val tracking: Boolean,
    val locked: Boolean,
    val decorations: List<Decoration>?,
    val width: Int,
    val height: Int,
    val offsetX: Int,
    val offsetY: Int,
    val data: ByteArray? = null
) : Packet<ServerPlayPacketHandler> {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(mapId)
        buffer.writeByte(scale)
        if (version >= Int.MAX_VALUE) {
            buffer.writeBoolean(locked)
            buffer.writeBoolean(tracking)
        } else if (version >= 498) {
            buffer.writeBoolean(tracking)
            buffer.writeBoolean(locked)
        }
        decorations!!.let {
            buffer.writeVarInt(it.size)
            it.forEach { (type, positionX, positionY, rotation, label) ->
                buffer.writeVarInt(type.ordinal)
                buffer.writeByte(rotation)
                buffer.writeByte(positionX)
                buffer.writeByte(positionY)
                label?.let {
                    buffer.writeBoolean(true)
                    buffer.writeString(buffer.objectMapper.writeValueAsString(it))
                } ?: buffer.writeBoolean(false)
            }
        }
        buffer.writeByte(width)
        if (width != 0) {
            buffer.writeByte(height)
            buffer.writeByte(offsetX)
            buffer.writeByte(offsetY)
            data!!.let {
                buffer.writeVarInt(it.size)
                it.forEach { buffer.writeByte(it.toInt()) }
            }
        }
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.map(this)
}
