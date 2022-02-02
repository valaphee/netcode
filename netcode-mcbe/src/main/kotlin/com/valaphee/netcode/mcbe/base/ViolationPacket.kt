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

package com.valaphee.netcode.mcbe.base

import com.valaphee.netcode.mcbe.Packet
import com.valaphee.netcode.mcbe.PacketBuffer
import com.valaphee.netcode.mcbe.PacketHandler
import com.valaphee.netcode.mcbe.PacketReader

/**
 * @author Kevin Ludwig
 */
class ViolationPacket(
    val type: Type,
    val severity: Severity,
    val packetId: Int,
    val context: String
) : Packet() {
    enum class Type {
        Unknown, MalformedPacket
    }

    enum class Severity {
        Information, Warning, Error, Severe
    }

    override val id get() = 0x9C

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(type.ordinal - 1)
        buffer.writeVarInt(severity.ordinal)
        buffer.writeVarInt(packetId)
        buffer.writeString(context)
    }

    override fun handle(handler: PacketHandler) = handler.violation(this)

    override fun toString() = "ViolationPacket(type=$type, severity=$severity, packetId=$packetId, context='$context')"
}

/**
 * @author Kevin Ludwig
 */
object ViolationPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ViolationPacket(ViolationPacket.Type.values()[buffer.readVarInt() + 1], ViolationPacket.Severity.values()[buffer.readVarInt()], buffer.readVarInt(), buffer.readString())
}
