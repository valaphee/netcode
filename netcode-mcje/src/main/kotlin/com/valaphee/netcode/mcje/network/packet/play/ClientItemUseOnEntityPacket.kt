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
import com.valaphee.netcode.mcje.network.ClientPlayPacketHandler
import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.V1_09_0
import com.valaphee.netcode.mcje.network.V1_16_0
import com.valaphee.netcode.mcje.world.entity.player.Hand

/**
 * @author Kevin Ludwig
 */
class ClientItemUseOnEntityPacket(
    var entityId: Int,
    var type: Type,
    var position: Float3?,
    var hand: Hand?,
    var sneaking: Boolean
) : Packet<ClientPlayPacketHandler>() {
    enum class Type {
        Interact, Attack, InteractAt
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(entityId)
        buffer.writeVarInt(type.ordinal)
        when (type) {
            Type.Interact -> if (version >= V1_09_0) buffer.writeVarInt(hand!!.ordinal)
            Type.Attack -> Unit
            Type.InteractAt -> {
                buffer.writeFloat3(position!!)
                if (version >= V1_09_0) buffer.writeVarInt(hand!!.ordinal)
            }
        }
        if (version >= V1_16_0) buffer.writeBoolean(sneaking)
    }

    override fun handle(handler: ClientPlayPacketHandler) = handler.itemUseOnEntity(this)

    override fun toString() = "ClientItemUseOnEntityPacket(entityId=$entityId, type=$type, position=$position, hand=$hand, sneaking=$sneaking)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int): ClientItemUseOnEntityPacket {
            val entityId = buffer.readVarInt()
            val type = Type.values()[buffer.readVarInt()]
            val position: Float3?
            val hand: Hand?
            when (type) {
                Type.InteractAt -> {
                    position = buffer.readFloat3()
                    hand = if (version >= V1_09_0) Hand.values()[buffer.readVarInt()] else Hand.Main
                }
                Type.Interact -> {
                    position = null
                    hand = if (version >= V1_09_0) Hand.values()[buffer.readVarInt()] else Hand.Main
                }
                else -> {
                    position = null
                    hand = null
                }
            }
            val sneaking = if (version >= V1_16_0) buffer.readBoolean() else false
            return ClientItemUseOnEntityPacket(entityId, type, position, hand, sneaking)
        }
    }
}
