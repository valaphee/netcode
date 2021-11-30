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
import com.valaphee.foundry.math.Float3
import com.valaphee.netcode.mcje.ServerPlayPacketHandler
import com.valaphee.netcode.mcje.Packet
import com.valaphee.netcode.mcje.PacketBuffer
import net.griefergames.breakdown.world.Particle
import net.griefergames.breakdown.world.ParticleType
import net.griefergames.breakdown.world.readParticle
import net.griefergames.breakdown.world.writeParticle

/**
 * @author Kevin Ludwig
 */
class ServerParticlePacket(
    val particleKey: Particle?,
    val longDistance: Boolean,
    val position: Double3,
    val offset: Float3,
    val speed: Float,
    val count: Int
) : Packet<ServerPlayPacketHandler> {
    override fun read(buffer: PacketBuffer, version: Int) {
        val particleType = ParticleType.byId(buffer.readInt())!!
        longDistance = buffer.readBoolean()
        position = buffer.readDouble3()
        offset = buffer.readFloat3()
        speed = buffer.readFloat()
        count = buffer.readInt()
        particle = buffer.readParticle(particleType)
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeInt(particle!!.type.id)
        buffer.writeBoolean(longDistance)
        buffer.writeDouble3(position!!)
        buffer.writeFloat3(offset!!)
        buffer.writeFloat(speed)
        buffer.writeInt(count)
        buffer.writeParticle(particle!!)
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.particle(this)
}
