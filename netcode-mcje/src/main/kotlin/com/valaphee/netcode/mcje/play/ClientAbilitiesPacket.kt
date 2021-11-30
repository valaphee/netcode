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

import com.valaphee.netcode.mcje.ClientPlayPacketHandler
import com.valaphee.netcode.mcje.Packet
import com.valaphee.netcode.mcje.PacketBuffer
import com.valaphee.netcode.mcje.PacketReader

/**
 * @author Kevin Ludwig
 */
class ClientAbilitiesPacket(
    val flags: Set<Flag>,
    val flySpeed: Float,
    val walkSpeed: Float,
) : Packet<ClientPlayPacketHandler> {
    enum class Flag {
        None, Flying
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeByteFlags(flags)
        if (version < 754) {
            buffer.writeFloat(flySpeed)
            buffer.writeFloat(walkSpeed)
        }
    }

    override fun handle(handler: ClientPlayPacketHandler) = handler.abilities(this)

    override fun toString() = "ClientAbilitiesPacket(flags=$flags, flySpeed=$flySpeed, walkSpeed=$walkSpeed)"
}

/**
 * @author Kevin Ludwig
 */
object ClientAbilitiesPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): ClientAbilitiesPacket {
        val flags = buffer.readByteFlags<ClientAbilitiesPacket.Flag>()
        val flySpeed: Float
        val walkSpeed: Float
        if (version < 754) {
            flySpeed = buffer.readFloat()
            walkSpeed = buffer.readFloat()
        } else {
            flySpeed = 0.0f
            walkSpeed = 0.0f
        }
        return ClientAbilitiesPacket(flags, flySpeed, walkSpeed)
    }
}
