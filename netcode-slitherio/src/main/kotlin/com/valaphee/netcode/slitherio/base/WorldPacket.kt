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

package com.valaphee.netcode.slitherio.base

import com.valaphee.netcode.slitherio.Packet
import com.valaphee.netcode.slitherio.PacketBuffer
import com.valaphee.netcode.slitherio.PacketHandler

/**
 * @author Kevin Ludwig
 */
class WorldPacket(
    val radius: Int,
    val maximumSnakeLength: Int,
    val sectorSize: Int,
    val sectorCountAlongEdge: Int,
    val angularSpeed: Float,
    val nodeSpeed1: Float,
    val nodeSpeed2: Float,
    val nodeSpeed3: Float,
    val angularBaseSpeed: Float,
    val angularSpeedPrey: Float,
    val snakeTailRatio: Float,
    val protocolVersion: Int
) : Packet() {
    override val id = 'a'

    override fun write(buffer: PacketBuffer) {
        buffer.writeMedium(radius)
        buffer.writeShort(maximumSnakeLength)
        buffer.writeShort(sectorSize)
        buffer.writeShort(sectorCountAlongEdge)
        buffer.writeByte((angularSpeed * 10).toInt())
        buffer.writeShort((nodeSpeed1 * 100).toInt())
        buffer.writeShort((nodeSpeed2 * 100).toInt())
        buffer.writeShort((nodeSpeed3 * 100).toInt())
        buffer.writeShort((angularBaseSpeed * 1000).toInt())
        buffer.writeShort((angularSpeedPrey * 1000).toInt())
        buffer.writeShort((snakeTailRatio * 1000).toInt())
        buffer.writeByte(protocolVersion)
    }

    override fun handle(handler: PacketHandler) = handler.world(this)
}
