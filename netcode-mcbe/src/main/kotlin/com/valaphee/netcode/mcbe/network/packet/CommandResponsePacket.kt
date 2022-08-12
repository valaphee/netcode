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

import com.valaphee.netcode.mcbe.command.Message
import com.valaphee.netcode.mcbe.command.Origin
import com.valaphee.netcode.mcbe.command.readMessage
import com.valaphee.netcode.mcbe.command.readOrigin
import com.valaphee.netcode.mcbe.command.writeMessage
import com.valaphee.netcode.mcbe.command.writeOrigin
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.Restrict
import com.valaphee.netcode.mcbe.network.Restriction
import com.valaphee.netcode.util.LazyList

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class CommandResponsePacket(
    val origin: Origin,
    val type: Type,
    val successCount: Int,
    val messages: List<Message>,
    val data: String?
) : Packet() {
    enum class Type {
        None, LastOutput, Silent, AllOutput, Data
    }

    override val id get() = 0x4F

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeOrigin(origin)
        buffer.writeByte(type.ordinal)
        buffer.writeVarUInt(successCount)
        messages.let {
            buffer.writeVarUInt(it.size)
            it.forEach { buffer.writeMessage(it) }
        }
        if (type == Type.Data) buffer.writeString(data!!)
    }

    override fun handle(handler: PacketHandler) = handler.commandResponse(this)

    override fun toString() = "CommandResponsePacket(origin=$origin, type=$type, successCount=$successCount, messages=$messages, data=$data)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int): CommandResponsePacket {
            val origin = buffer.readOrigin()
            val type = Type.values()[buffer.readByte().toInt()]
            val successCount = buffer.readVarUInt()
            val messages = LazyList(buffer.readVarUInt()) { buffer.readMessage() }
            val data = if (type == Type.Data) buffer.readString() else null
            return CommandResponsePacket(origin, type, successCount, messages, data)
        }
    }
}
