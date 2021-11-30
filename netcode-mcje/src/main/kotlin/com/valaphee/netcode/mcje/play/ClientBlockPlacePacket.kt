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
import com.valaphee.netcode.mc.util.Direction
import com.valaphee.netcode.mcje.Packet
import com.valaphee.netcode.mcje.PacketBuffer
import com.valaphee.netcode.mcje.PacketReader
import com.valaphee.netcode.mcje.entity.player.Hand

/**
 * @author Kevin Ludwig
 */
class ClientBlockPlacePacket(
    val hand: Hand,
    val blockPosition: Int3,
    val blockFace: Direction?,
    val clickPosition: Float3,
    val insideBlock: Boolean,
) : Packet<ClientPlayPacketHandler> {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(hand.ordinal)
        buffer.writeInt3UnsignedY(blockPosition)
        buffer.writeVarInt(blockFace?.ordinal ?: 0xFF)
        buffer.writeFloat3(clickPosition)
        buffer.writeBoolean(insideBlock)
    }

    override fun handle(handler: ClientPlayPacketHandler) = handler.blockPlace(this)

    override fun toString() = "ClientBlockPlacePacket(hand=$hand, blockPosition=$blockPosition, blockFace=$blockFace, clickPosition=$clickPosition, insideBlock=$insideBlock)"

    companion object {
        internal const val blockFaceNone = 0xFF
    }
}

/**
 * @author Kevin Ludwig
 */
object ClientBlockPlacePacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): ClientBlockPlacePacket {
        val hand = Hand.values()[buffer.readVarInt()]
        val blockPosition = buffer.readInt3UnsignedY()
        val blockFace = buffer.readVarInt()
        val clickPosition = buffer.readFloat3()
        val insideBlock = buffer.readBoolean()
        return ClientBlockPlacePacket(hand, blockPosition, if (blockFace == 0xFF) null else Direction.values()[blockFace], clickPosition, insideBlock)
    }
}
