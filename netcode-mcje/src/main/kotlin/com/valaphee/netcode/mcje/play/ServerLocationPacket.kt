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

import com.valaphee.foundry.math.Double3
import com.valaphee.foundry.math.Float2
import com.valaphee.netcode.mcje.Packet
import com.valaphee.netcode.mcje.PacketBuffer
import com.valaphee.netcode.mcje.PacketReader

/**
 * @author Kevin Ludwig
 */
class ServerLocationPacket(
    val position: Double3,
    val rotation: Float2,
    val flags: Set<Flag>,
    val teleportId: Int,
    val dismountVehicle: Boolean
) : Packet<ServerPlayPacketHandler> {
    enum class Flag {
        RelativeX, RelativeY, RelativeZ, RelativePitch, RelativeYaw
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeDouble3(position)
        buffer.writeFloat2(rotation)
        buffer.writeByteFlags(flags)
        if (version >= 498) buffer.writeVarInt(teleportId)
        if (version >= Int.MAX_VALUE) buffer.writeBoolean(dismountVehicle)
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.location(this)

    override fun toString() = "ServerLocationPacket(position=$position, rotation=$rotation, flags=$flags, teleportId=$teleportId, dismountVehicle=$dismountVehicle)"
}

/**
 * @author Kevin Ludwig
 */
object ServerLocationPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): ServerLocationPacket {
        val position = buffer.readDouble3()
        val rotation = buffer.readFloat2()
        val flags = buffer.readByteFlags<ServerLocationPacket.Flag>()
        val teleportId = if (version >= 498) buffer.readVarInt() else 0
        val dismountVehicle = if (version >= Int.MAX_VALUE) buffer.readBoolean() else false
        return ServerLocationPacket(position, rotation, flags, teleportId, dismountVehicle)
    }
}
