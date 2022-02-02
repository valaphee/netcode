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

package com.valaphee.netcode.mcje.play

import com.valaphee.foundry.math.Double3
import com.valaphee.netcode.mcje.Packet
import com.valaphee.netcode.mcje.PacketBuffer
import com.valaphee.netcode.mcje.PacketReader

/**
 * @author Kevin Ludwig
 */
class ServerLookAtPacket(
    val feetOrEyes: FeetOrEyes,
    val position: Double3,
    val entityId: Int,
    val entityFeetOrEyes: FeetOrEyes?
) : Packet<ServerPlayPacketHandler> {
    enum class FeetOrEyes {
        Feet, Eyes
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(feetOrEyes.ordinal)
        buffer.writeDouble3(position)
        entityFeetOrEyes?.let {
            buffer.writeBoolean(true)
            buffer.writeVarInt(entityId)
            buffer.writeVarInt(it.ordinal)
        } ?: buffer.writeBoolean(false)
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.lookAt(this)

    override fun toString() = "ServerLookAtPacket(feetOrEyes=$feetOrEyes, position=$position, entityId=$entityId, entityFeetOrEyes=$entityFeetOrEyes)"
}

/**
 * @author Kevin Ludwig
 */
object ServerLookAtPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): ServerLookAtPacket {
        val feetOrEyes = ServerLookAtPacket.FeetOrEyes.values()[buffer.readVarInt()]
        val position = buffer.readDouble3()
        val entityId: Int
        val entityFeetOrEyes: ServerLookAtPacket.FeetOrEyes?
        if (buffer.readBoolean()) {
            entityId = buffer.readVarInt()
            entityFeetOrEyes = ServerLookAtPacket.FeetOrEyes.values()[buffer.readVarInt()]
        } else {
            entityId = 0
            entityFeetOrEyes = null
        }
        return ServerLookAtPacket(feetOrEyes, position, entityId, entityFeetOrEyes)
    }
}
