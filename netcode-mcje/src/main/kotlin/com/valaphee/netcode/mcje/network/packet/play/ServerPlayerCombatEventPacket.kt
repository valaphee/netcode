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

import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.PacketReader
import com.valaphee.netcode.mcje.network.ServerPlayPacketHandler
import net.kyori.adventure.text.Component

/**
 * @author Kevin Ludwig
 */
class ServerPlayerCombatEventPacket(
    val event: Event,
    val durationOrPlayerEntityId: Int,
    val entityId: Int,
    val message: Component?
) : Packet<ServerPlayPacketHandler> {
    enum class Event {
        Enter, End, Death
    }

    override fun getId(id: Int, version: Int) = if (version >= 758) when (event) {
        Event.End -> 0x33
        Event.Enter -> 0x34
        Event.Death -> 0x35
    } else id

    override fun write(buffer: PacketBuffer, version: Int) {
        if (version < 758) buffer.writeVarInt(event.ordinal)
        when (event) {
            Event.Enter -> Unit
            Event.End -> {
                buffer.writeVarInt(durationOrPlayerEntityId)
                buffer.writeInt(entityId)
            }
            Event.Death -> {
                buffer.writeVarInt(durationOrPlayerEntityId)
                buffer.writeInt(entityId)
                buffer.writeComponent(message!!)
            }
        }
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.playerCombatEvent(this)

    override fun toString() = "ServerPlayerCombatEventPacket(event=$event, durationOrPlayerEntityId=$durationOrPlayerEntityId, entityId=$entityId, message=$message)"
}

/**
 * @author Kevin Ludwig
 */
object ServerPlayerCombatEventPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): ServerPlayerCombatEventPacket {
        val event = ServerPlayerCombatEventPacket.Event.values()[buffer.readVarInt()]
        val durationOrPlayerEntityId: Int
        val entityId: Int
        val message: Component?
        when (event) {
            ServerPlayerCombatEventPacket.Event.Enter -> {
                durationOrPlayerEntityId = 0
                entityId = 0
                message = null
            }
            ServerPlayerCombatEventPacket.Event.End -> {
                durationOrPlayerEntityId = buffer.readVarInt()
                entityId = buffer.readInt()
                message = null
            }
            ServerPlayerCombatEventPacket.Event.Death -> {
                durationOrPlayerEntityId = buffer.readVarInt()
                entityId = buffer.readInt()
                message = buffer.readComponent()
            }
        }
        return ServerPlayerCombatEventPacket(event, durationOrPlayerEntityId, entityId, message)
    }
}

/**
 * @author Kevin Ludwig
 */
object ServerPlayerCombatEventEndPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerPlayerCombatEventPacket(ServerPlayerCombatEventPacket.Event.End, buffer.readVarInt(), buffer.readInt(), null)
}

/**
 * @author Kevin Ludwig
 */
object ServerPlayerCombatEventEnterPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerPlayerCombatEventPacket(ServerPlayerCombatEventPacket.Event.Enter, 0, 0, null)
}

/**
 * @author Kevin Ludwig
 */
object ServerPlayerCombatEventDeathPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerPlayerCombatEventPacket(ServerPlayerCombatEventPacket.Event.Death, buffer.readVarInt(), buffer.readInt(), buffer.readComponent())
}
