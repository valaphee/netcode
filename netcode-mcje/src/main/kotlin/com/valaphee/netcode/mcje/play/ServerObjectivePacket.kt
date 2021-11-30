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

import com.fasterxml.jackson.module.kotlin.readValue
import com.valaphee.netcode.mcje.Packet
import com.valaphee.netcode.mcje.PacketBuffer
import com.valaphee.netcode.mcje.PacketReader
import com.valaphee.netcode.mcje.util.text.Component
import com.valaphee.netcode.util.ByteBufStringReader

/**
 * @author Kevin Ludwig
 */
class ServerObjectivePacket(
    val name: String,
    val action: Action,
    val displayName: Component?,
    val type: Type?
) : Packet<ServerPlayPacketHandler> {
    enum class Action {
        Create, Remove, Update
    }

    enum class Type(
        val key: String
    ) {
        Integer("integer"),
        Hearts("hearts");

        companion object {
            private val byKey: Map<String, Type> = values().associateBy { it.key }

            fun byKeyOrNull(key: String) = byKey[key]
        }
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeString(name)
        buffer.writeByte(action.ordinal)
        if (action == Action.Create || action == Action.Update) {
            buffer.writeString(buffer.objectMapper.writeValueAsString(displayName!!))
            if (version >= 498) buffer.writeVarInt(type!!.ordinal) else buffer.writeString(type!!.key)
        }
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.objective(this)

    override fun toString() = "ServerObjectivePacket(name='$name', action=$action, displayName=$displayName, type=$type)"
}

/**
 * @author Kevin Ludwig
 */
object ServerObjectivePacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): ServerObjectivePacket {
        val name = buffer.readString(16)
        val action = ServerObjectivePacket.Action.values()[buffer.readUnsignedByte().toInt()]
        val displayName: Component?
        val type: ServerObjectivePacket.Type?
        if (action == ServerObjectivePacket.Action.Create || action == ServerObjectivePacket.Action.Update) {
            displayName = buffer.objectMapper.readValue(ByteBufStringReader(buffer, buffer.readVarInt()))
            type = if (version >= 498) ServerObjectivePacket.Type.values()[buffer.readVarInt()] else ServerObjectivePacket.Type.byKeyOrNull(buffer.readString())
        } else {
            displayName = null
            type = null
        }
        return ServerObjectivePacket(name, action, displayName, type)
    }
}
