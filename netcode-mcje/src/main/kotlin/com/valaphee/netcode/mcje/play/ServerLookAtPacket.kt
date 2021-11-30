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
import com.valaphee.netcode.mcje.Packet
import com.valaphee.netcode.mcje.PacketBuffer
import com.valaphee.netcode.mcje.PacketReader
import com.valaphee.netcode.mcje.ServerPlayPacketHandler

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
