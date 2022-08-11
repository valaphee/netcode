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

/**
 * @author Kevin Ludwig
 */
class EntityPropertyPacket(
    val uniqueEntityId: Long,
    val key: String,
    val boolValue: Boolean,
    val stringValue: String,
    val intValue: Int,
    val floatValue: Float
) : Packet() {
    override val id get() = 0xB6

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarLong(uniqueEntityId)
        buffer.writeString(key)
        buffer.writeBoolean(boolValue)
        buffer.writeString(stringValue)
        buffer.writeVarInt(intValue)
        buffer.writeFloatLE(floatValue)
    }

    override fun handle(handler: PacketHandler) = handler.entityProperty(this)

    override fun toString() = "EntityPropertyPacket(uniqueEntityId=$uniqueEntityId, key='$key', boolValue=$boolValue, stringValue='$stringValue', intValue=$intValue, floatValue=$floatValue)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = EntityPropertyPacket(buffer.readVarLong(), buffer.readString(), buffer.readBoolean(), buffer.readString(), buffer.readVarInt(), buffer.readFloatLE())
    }
}
