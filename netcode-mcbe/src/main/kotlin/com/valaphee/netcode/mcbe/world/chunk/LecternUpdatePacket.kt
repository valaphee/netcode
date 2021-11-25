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

package com.valaphee.netcode.mcbe.world.chunk

import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mcbe.Packet
import com.valaphee.netcode.mcbe.PacketBuffer
import com.valaphee.netcode.mcbe.PacketHandler
import com.valaphee.netcode.mcbe.PacketReader
import com.valaphee.netcode.mcbe.Restrict
import com.valaphee.netcode.mcbe.Restriction

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToServer)
class LecternUpdatePacket(
    val page: Int,
    val totalPages: Int,
    val position: Int3,
    val droppingBook: Boolean
) : Packet() {
    override val id get() = 0x7D

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeByte(page)
        buffer.writeByte(totalPages)
        buffer.writeInt3UnsignedY(position)
        buffer.writeBoolean(droppingBook)
    }

    override fun handle(handler: PacketHandler) = handler.lecternUpdate(this)

    override fun toString() = "LecternUpdatePacket(page=$page, totalPages=$totalPages, position=$position, droppingBook=$droppingBook)"
}

/**
 * @author Kevin Ludwig
 */
object LecternUpdatePacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = LecternUpdatePacket(buffer.readUnsignedByte().toInt(), buffer.readUnsignedByte().toInt(), buffer.readInt3UnsignedY(), buffer.readBoolean())
}
