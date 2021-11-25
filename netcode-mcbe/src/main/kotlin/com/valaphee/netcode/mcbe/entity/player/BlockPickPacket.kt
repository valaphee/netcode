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

package com.valaphee.netcode.mcbe.entity.player

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
class BlockPickPacket(
    var position: Int3,
    var withEntity: Boolean,
    var hotbarSlot: Int
) : Packet() {
    override val id get() = 0x22

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeInt3(position)
        buffer.writeBoolean(withEntity)
        buffer.writeByte(hotbarSlot)
    }

    override fun handle(handler: PacketHandler) = handler.blockPick(this)

    override fun toString() = "BlockPickPacket(position=$position, withEntity=$withEntity, hotbarSlot=$hotbarSlot)"
}

/**
 * @author Kevin Ludwig
 */
object BlockPickPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = BlockPickPacket(buffer.readInt3(), buffer.readBoolean(), buffer.readByte().toInt())
}
