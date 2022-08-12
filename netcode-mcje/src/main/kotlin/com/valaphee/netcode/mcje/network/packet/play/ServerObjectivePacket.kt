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
import com.valaphee.netcode.mcje.network.Packet.Reader
import com.valaphee.netcode.mcje.network.ServerPlayPacketHandler
import net.kyori.adventure.text.Component

/**
 * @author Kevin Ludwig
 */
class ServerObjectivePacket(
    val name: String,
    val action: Action,
    val displayName: Component?,
    val type: Type?
) : Packet<ServerPlayPacketHandler>() {
    enum class Action {
        Create, Remove, Update
    }

    enum class Type {
        Integer, Hearts
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeString(name)
        buffer.writeByte(action.ordinal)
        if (action == Action.Create || action == Action.Update) {
            buffer.writeComponent(displayName!!)
            buffer.writeVarInt(type!!.ordinal)
        }
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.objective(this)

    override fun toString() = "ServerObjectivePacket(name='$name', action=$action, displayName=$displayName, type=$type)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int): ServerObjectivePacket {
            val name = buffer.readString(16)
            val action = Action.values()[buffer.readUnsignedByte().toInt()]
            val displayName: Component?
            val type: Type?
            if (action == Action.Create || action == Action.Update) {
                displayName = buffer.readComponent()
                type = Type.values()[buffer.readVarInt()]
            } else {
                displayName = null
                type = null
            }
            return ServerObjectivePacket(name, action, displayName, type)
        }
    }
}
