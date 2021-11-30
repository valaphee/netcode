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

import com.valaphee.foundry.math.Float3
import com.valaphee.foundry.math.Int3
import com.valaphee.foundry.math.MutableFloat3
import com.valaphee.netcode.mc.util.Direction
import com.valaphee.netcode.mcje.ClientPlayPacketHandler
import com.valaphee.netcode.mcje.Packet
import com.valaphee.netcode.mcje.PacketBuffer
import com.valaphee.netcode.mcje.PacketReader
import com.valaphee.netcode.mcje.entity.player.Hand
import com.valaphee.netcode.mcje.item.stack.Stack
import com.valaphee.netcode.mcje.item.stack.readStack
import com.valaphee.netcode.mcje.item.stack.writeStack

/**
 * @author Kevin Ludwig
 */
class ClientBlockPlacePacket(
    val hand: Hand,
    val blockPosition: Int3,
    val blockFace: Direction?,
    val itemInHand: Stack?,
    val clickPosition: Float3,
    val insideBlock: Boolean,
) : Packet<ClientPlayPacketHandler> {
    override fun write(buffer: PacketBuffer, version: Int) {
        if (version >= 498) buffer.writeVarInt(hand.ordinal)
        buffer.writeInt3UnsignedY(blockPosition)
        buffer.writeVarInt(blockFace?.ordinal ?: blockFaceNone)
        if (version >= 498) {
            buffer.writeFloat3(clickPosition)
            buffer.writeBoolean(insideBlock)
        } else {
            buffer.writeStack(itemInHand)
            val (clickPositionX, clickPositionY, clickPositionZ) = clickPosition.toMutableFloat3().scale(16.0f).toInt3()
            buffer.writeByte(clickPositionX)
            buffer.writeByte(clickPositionY)
            buffer.writeByte(clickPositionZ)
        }
    }

    override fun handle(handler: ClientPlayPacketHandler) = handler.blockPlace(this)

    override fun toString() = "ClientBlockPlacePacket(hand=$hand, blockPosition=$blockPosition, blockFace=$blockFace, itemInHand=$itemInHand, clickPosition=$clickPosition, insideBlock=$insideBlock)"

    companion object {
        internal const val blockFaceNone = 0xFF
    }
}

/**
 * @author Kevin Ludwig
 */
object ClientBlockPlacePacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): ClientBlockPlacePacket {
        val hand = if (version >= 498) Hand.values()[buffer.readVarInt()] else Hand.Main
        val blockPosition = buffer.readInt3UnsignedY()
        val blockFace = buffer.readVarInt()
        val clickPosition: Float3
        val insideBlock: Boolean
        val itemInHand: Stack?
        if (version >= 498) {
            clickPosition = buffer.readFloat3()
            insideBlock = buffer.readBoolean()
            itemInHand = null
        } else {
            itemInHand = buffer.readStack()
            insideBlock = false
            clickPosition = MutableFloat3(buffer.readByte().toFloat(), buffer.readByte().toFloat(), buffer.readByte().toFloat()).scale(1 / 16.0f)
        }
        return ClientBlockPlacePacket(hand, blockPosition, if (blockFace == ClientBlockPlacePacket.blockFaceNone) null else Direction.values()[blockFace], itemInHand, clickPosition, insideBlock)
    }
}
