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

import com.valaphee.netcode.mcje.network.ClientPlayPacketHandler
import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.PacketReader
import com.valaphee.netcode.mcje.network.V1_18_2
import com.valaphee.netcode.mcje.world.entity.player.Hand
import com.valaphee.netcode.mcje.world.item.ItemStack
import com.valaphee.netcode.mcje.world.item.readItemStack
import com.valaphee.netcode.mcje.world.item.writeItemStack

/**
 * @author Kevin Ludwig
 */
class ClientBookEditPacket(
    val itemStack: ItemStack?,
    val sign: Boolean,
    val hand: Hand,
    val pages: List<String>?,
    val title: String?
) : Packet<ClientPlayPacketHandler>() {
    override fun write(buffer: PacketBuffer, version: Int) {
        if (version >= V1_18_2) {
            buffer.writeVarInt(hand.ordinal)
            pages!!.let {
                buffer.writeVarInt(it.size)
                it.forEach { buffer.writeString(it) }
            }
            title?.let {
                buffer.writeBoolean(true)
                buffer.writeString(it)
            } ?: buffer.writeBoolean(false)
        } else {
            buffer.writeItemStack(itemStack)
            buffer.writeBoolean(sign)
            buffer.writeVarInt(hand.ordinal)
        }
    }

    override fun handle(handler: ClientPlayPacketHandler) = handler.bookEdit(this)

    override fun toString() = "ClientBookEditPacket(stack=$itemStack, sign=$sign, hand=$hand, pages=$pages, title=$title)"
}

/**
 * @author Kevin Ludwig
 */
object ClientBookEditPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): ClientBookEditPacket {
        val itemStack: ItemStack?
        val sign: Boolean
        val hand: Hand
        val pages: List<String>?
        val title: String?
        if (version >= V1_18_2) {
            itemStack = null
            sign = false
            hand = Hand.values()[buffer.readVarInt()]
            pages = List(buffer.readVarInt()) { buffer.readString() }
            title = if (buffer.readBoolean()) buffer.readString() else null
        } else {
            itemStack = buffer.readItemStack()
            sign = buffer.readBoolean()
            hand = Hand.values()[buffer.readVarInt()]
            pages = null
            title = null
        }
        return ClientBookEditPacket(itemStack, sign, hand, pages, title)
    }
}
