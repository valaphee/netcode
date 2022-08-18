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

import com.valaphee.foundry.math.Float3
import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mcje.network.ClientPlayPacketHandler
import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.V1_09_0
import com.valaphee.netcode.mcje.network.V1_11_0
import com.valaphee.netcode.mcje.network.V1_14_0
import com.valaphee.netcode.mcje.network.V1_19_0
import com.valaphee.netcode.mcje.util.Direction
import com.valaphee.netcode.mcje.world.entity.player.Hand
import com.valaphee.netcode.mcje.world.item.ItemStack
import com.valaphee.netcode.mcje.world.item.readItemStack
import com.valaphee.netcode.mcje.world.item.writeItemStack

/**
 * @author Kevin Ludwig
 */
class ClientBlockPlacePacket(
    val hand: Hand,
    val blockPosition: Int3,
    val blockFace: Direction?,
    val itemStackInHand: ItemStack?,
    val clickPosition: Float3,
    val insideBlock: Boolean,
    val confirmId: Int
) : Packet<ClientPlayPacketHandler>() {
    override fun write(buffer: PacketBuffer, version: Int) {
        if (version >= V1_14_0) buffer.writeVarInt(hand.ordinal)
        buffer.writeBlockPosition(blockPosition)
        if (version >= V1_09_0) buffer.writeVarInt(blockFace?.ordinal ?: 0xFF) else buffer.writeByte(blockFace?.ordinal ?: 0xFF)
        if (version < V1_14_0) if (version >= V1_09_0) buffer.writeVarInt(hand.ordinal) else buffer.writeItemStack(itemStackInHand, version)
        if (version >= V1_11_0) buffer.writeFloat3(clickPosition) else {
            buffer.writeByte(clickPosition.x.toInt())
            buffer.writeByte(clickPosition.y.toInt())
            buffer.writeByte(clickPosition.z.toInt())
        }
        if (version >= V1_14_0) buffer.writeBoolean(insideBlock)
        if (version >= V1_19_0) buffer.writeVarInt(confirmId)
    }

    override fun handle(handler: ClientPlayPacketHandler) = handler.blockPlace(this)

    override fun toString() = "ClientBlockPlacePacket(hand=$hand, blockPosition=$blockPosition, blockFace=$blockFace, itemStackInHand=$itemStackInHand clickPosition=$clickPosition, insideBlock=$insideBlock, confirmId=$confirmId)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int): ClientBlockPlacePacket {
            var hand = if (version >= V1_14_0) Hand.values()[buffer.readVarInt()] else Hand.Main
            val blockPosition = buffer.readBlockPosition()
            val blockFace = if (version >= V1_09_0) buffer.readVarInt() else buffer.readUnsignedByte().toInt()
            val itemStackInHand = if (version < V1_14_0) if (version >= V1_09_0) {
                hand = Hand.values()[buffer.readVarInt()]
                null
            } else {
                hand = Hand.Main
                buffer.readItemStack(version)
            } else null
            val clickPosition = if (version >= V1_11_0) buffer.readFloat3() else Float3(buffer.readByte().toFloat(), buffer.readByte().toFloat(), buffer.readByte().toFloat())
            val insideBlock = if (version >= V1_14_0) buffer.readBoolean() else false
            val confirmId = if (version >= V1_19_0) buffer.readVarInt() else 0
            return ClientBlockPlacePacket(hand, blockPosition, if (blockFace != 0xFF) Direction.values()[blockFace] else null, itemStackInHand, clickPosition, insideBlock, confirmId)
        }
    }
}
