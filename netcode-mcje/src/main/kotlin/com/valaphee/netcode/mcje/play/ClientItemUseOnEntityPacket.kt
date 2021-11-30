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
import com.valaphee.netcode.mcje.Packet
import com.valaphee.netcode.mcje.PacketBuffer
import com.valaphee.netcode.mcje.PacketReader
import com.valaphee.netcode.mcje.entity.player.Hand

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
