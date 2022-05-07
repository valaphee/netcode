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

package com.valaphee.netcode.mcbe.network.packet

import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.PacketReader

/**
 * @author Kevin Ludwig
 */
class AbilityPacket(
    val ability: Int,
    val type: Type,
    val boolValue: Boolean,
    val floatValue: Float
) : Packet() {
    enum class Type {
        None, Bool, Float
    }

    override val id get() = 0xB8

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(ability)
        buffer.writeByte(type.ordinal)
        buffer.writeBoolean(boolValue)
        buffer.writeFloatLE(floatValue)
    }

    override fun handle(handler: PacketHandler) = handler.ability(this)

    override fun toString() = "AbilityPacket(ability=$ability, type=$type, boolValue=$boolValue, floatValue=$floatValue)"
}

/**
 * @author Kevin Ludwig
 */
object AbilityPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = AbilityPacket(buffer.readVarInt(), AbilityPacket.Type.values()[buffer.readUnsignedByte().toInt()], buffer.readBoolean(), buffer.readFloatLE())
}
