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
import com.valaphee.netcode.mc.util.Direction
import com.valaphee.netcode.mcje.network.ClientPlayPacketHandler
import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.PacketReader
import com.valaphee.netcode.mcje.world.entity.player.Hand

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
