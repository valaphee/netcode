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

import com.valaphee.netcode.mcje.ClientPlayPacketHandler
import com.valaphee.netcode.mcje.Packet
import com.valaphee.netcode.mcje.PacketBuffer
import com.valaphee.netcode.mcje.PacketReader
import com.valaphee.netcode.mcje.item.stack.Stack
import com.valaphee.netcode.mcje.item.stack.readStack
import com.valaphee.netcode.mcje.item.stack.writeStack

/**
 * @author Kevin Ludwig
 */
class ClientWindowClickPacket(
    val windowId: Int,
    val slotId: Int,
    val buttonSpecifier: Int,
    val confirmId: Int,
    val button: Button,
    val stackInSlot: Stack?
) : Packet<ClientPlayPacketHandler> {
    enum class Button {
        Normal, Shift, Number, MiddleClick, QOrNoOp, Paint, DoubleClick
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeByte(windowId)
        buffer.writeShort(slotId)
        buffer.writeByte(buttonSpecifier)
        buffer.writeShort(confirmId)
        buffer.writeVarInt(button.ordinal)
        buffer.writeStack(stackInSlot)
    }

    override fun handle(handler: ClientPlayPacketHandler) = handler.windowClick(this)

    override fun toString() = "ClientWindowClickPacket(windowId=$windowId, slotId=$slotId, buttonSpecifier=$buttonSpecifier, confirmId=$confirmId, button=$button, stackInSlot=$stackInSlot)"

    companion object {
        const val SlotIdNone = -999
    }
}

/**
 * @author Kevin Ludwig
 */
object ClientWindowClickPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ClientWindowClickPacket(buffer.readByte().toInt(), buffer.readShort().toInt(), buffer.readByte().toInt(), buffer.readShort().toInt(), ClientWindowClickPacket.Button.values()[buffer.readVarInt()], buffer.readStack())
}
