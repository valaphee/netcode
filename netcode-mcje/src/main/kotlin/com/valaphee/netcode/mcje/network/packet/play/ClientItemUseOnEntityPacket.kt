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
import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.PacketReader
import com.valaphee.netcode.mcje.entity.player.Hand
import com.valaphee.netcode.mcje.network.ClientPlayPacketHandler

/**
 * @author Kevin Ludwig
 */
class ClientItemUseOnEntityPacket(
    var entityId: Int,
    var type: Type,
    var position: Float3?,
    var hand: Hand?,
    var sneaking: Boolean
) : Packet<ClientPlayPacketHandler> {
    enum class Type {
        Interact, Attack, InteractAt
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(entityId)
        buffer.writeVarInt(type.ordinal)
        @Suppress("NON_EXHAUSTIVE_WHEN") when (type) {
            Type.InteractAt -> {
                buffer.writeFloat3(position!!)
                if (version >= 498) buffer.writeVarInt(hand!!.ordinal)
            }
            Type.Interact -> if (version >= 498) buffer.writeVarInt(hand!!.ordinal)
        }
        if (version >= 754) buffer.writeBoolean(sneaking)
    }

    override fun handle(handler: ClientPlayPacketHandler) = handler.itemUseOnEntity(this)

    override fun toString() = "ClientItemUseOnEntityPacket(entityId=$entityId, type=$type, position=$position, hand=$hand, sneaking=$sneaking)"
}

/**
 * @author Kevin Ludwig
 */
object ClientItemUseOnEntityPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): ClientItemUseOnEntityPacket {
        val entityId = buffer.readVarInt()
        val type = ClientItemUseOnEntityPacket.Type.values()[buffer.readVarInt()]
        val position: Float3?
        val hand: Hand?
        when (type) {
            ClientItemUseOnEntityPacket.Type.InteractAt -> {
                position = buffer.readFloat3()
                hand = if (version >= 498) Hand.values()[buffer.readVarInt()] else Hand.Main
            }
            ClientItemUseOnEntityPacket.Type.Interact -> {
                position = null
                hand = if (version >= 498) Hand.values()[buffer.readVarInt()] else Hand.Main
            }
            else -> {
                position = null
                hand = null
            }
        }
        val sneaking = if (version >= 754) buffer.readBoolean() else false
        return ClientItemUseOnEntityPacket(entityId, type, position, hand, sneaking)
    }
}
