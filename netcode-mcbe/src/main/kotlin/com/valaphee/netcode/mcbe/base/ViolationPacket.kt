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
