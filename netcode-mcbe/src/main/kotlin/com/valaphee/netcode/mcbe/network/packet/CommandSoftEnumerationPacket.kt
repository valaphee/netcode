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

import com.valaphee.netcode.mcbe.command.Enumeration
import com.valaphee.netcode.mcbe.command.readEnumeration
import com.valaphee.netcode.mcbe.command.writeEnumeration
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.Restrict
import com.valaphee.netcode.mcbe.network.Restriction

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class CommandSoftEnumerationPacket(
    val action: Action,
    val softEnumeration: Enumeration
) : Packet() {
    enum class Action {
        Add, Remove, Update
    }

    override val id get() = 0x72

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeEnumeration(softEnumeration)
        buffer.writeByte(action.ordinal)
    }

    override fun handle(handler: PacketHandler) = handler.commandSoftEnumeration(this)

    override fun toString() = "CommandSoftEnumerationPacket(action=$action, softEnumeration=$softEnumeration)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int): CommandSoftEnumerationPacket {
            val softEnumeration = buffer.readEnumeration(true)
            val action = Action.values()[buffer.readUnsignedByte().toInt()]
            return CommandSoftEnumerationPacket(action, softEnumeration)
        }
    }
}
