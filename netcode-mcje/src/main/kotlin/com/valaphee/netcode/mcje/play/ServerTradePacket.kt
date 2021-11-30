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
import com.valaphee.netcode.mcje.PacketReader
import com.valaphee.netcode.mcje.ServerPlayPacketHandler
import com.valaphee.netcode.mcje.item.stack.Stack
import com.valaphee.netcode.mcje.item.stack.readStack
import com.valaphee.netcode.mcje.item.stack.writeStack
import com.valaphee.netcode.util.safeList

/**
 * @author Kevin Ludwig
 */
class ServerTradePacket(
    val windowId: Int,
    val offers: MutableList<Offer>,
    val level: Int,
    val experience: Int,
    val regular: Boolean,
    val restock: Boolean
) : Packet<ServerPlayPacketHandler> {
    data class Offer(
        val buyA: Stack?,
        val sell: Stack?,
        val buyB: Stack?,
        val disabled: Boolean,
        val sold: Int,
        val stock: Int,
        val experience: Int,
        val price: Int,
        val priceMultiplier: Float,
        val demand: Int
    )

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(windowId)
        buffer.writeByte(offers.size)
        offers.forEach {
            buffer.writeStack(it.buyA)
            buffer.writeStack(it.sell)
            it.buyB?.let {
                buffer.writeBoolean(true)
                buffer.writeStack(it)
            } ?: buffer.writeBoolean(false)
            buffer.writeBoolean(it.disabled)
            buffer.writeInt(it.sold)
            buffer.writeInt(it.stock)
            buffer.writeInt(it.experience)
            buffer.writeInt(it.price)
            buffer.writeFloat(it.priceMultiplier)
            buffer.writeInt(it.demand)
        }
        buffer.writeVarInt(level)
        buffer.writeVarInt(experience)
        buffer.writeBoolean(regular)
        buffer.writeBoolean(restock)
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.trade(this)

    override fun toString() = "ServerTradePacket(windowId=$windowId, offers=$offers, level=$level, experience=$experience, regular=$regular, restock=$restock)"
}

/**
 * @author Kevin Ludwig
 */
object ServerTradePacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerTradePacket(buffer.readVarInt(), safeList(buffer.readUnsignedByte().toInt()) { ServerTradePacket.Offer(buffer.readStack(), buffer.readStack(), if (buffer.readBoolean()) buffer.readStack() else null, buffer.readBoolean(), buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readFloat(), buffer.readInt()) }, buffer.readVarInt(), buffer.readVarInt(), buffer.readBoolean(), buffer.readBoolean())
}
