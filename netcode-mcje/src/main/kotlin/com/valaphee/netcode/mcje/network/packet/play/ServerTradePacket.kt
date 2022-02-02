/*
 * Copyright (c) 2021-2022, Valaphee.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.valaphee.netcode.mcje.network.packet.play

import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.PacketReader
import com.valaphee.netcode.mcje.item.stack.Stack
import com.valaphee.netcode.mcje.item.stack.readStack
import com.valaphee.netcode.mcje.item.stack.writeStack
import com.valaphee.netcode.mcje.network.ServerPlayPacketHandler
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
