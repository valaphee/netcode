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
import com.valaphee.netcode.mcje.world.entity.player.Hand
import com.valaphee.netcode.mcje.world.item.ItemStack
import com.valaphee.netcode.mcje.world.item.readStack
import com.valaphee.netcode.mcje.world.item.writeStack

/**
 * @author Kevin Ludwig
 */
class ClientBookEditPacket(
    val itemStack: ItemStack?,
    val sign: Boolean,
    val hand: Hand
) : Packet<ClientPlayPacketHandler> {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeStack(itemStack)
        buffer.writeBoolean(sign)
        buffer.writeVarInt(hand.ordinal)
    }

    override fun handle(handler: ClientPlayPacketHandler) = handler.bookEdit(this)

    override fun toString() = "ClientBookEditPacket(stack=$itemStack, sign=$sign, hand=$hand)"
}

/**
 * @author Kevin Ludwig
 */
object ClientBookEditPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ClientBookEditPacket(buffer.readStack(), buffer.readBoolean(), Hand.values()[buffer.readVarInt()])
}
