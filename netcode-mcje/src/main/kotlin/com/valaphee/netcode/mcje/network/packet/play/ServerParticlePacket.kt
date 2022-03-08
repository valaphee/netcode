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

import com.valaphee.foundry.math.Double3
import com.valaphee.foundry.math.Float3
import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.PacketReader
import com.valaphee.netcode.mcje.network.ServerPlayPacketHandler
import com.valaphee.netcode.mcje.world.ParticleData
import com.valaphee.netcode.mcje.world.readParticle

/**
 * @author Kevin Ludwig
 */
class ServerParticlePacket(
    val data: ParticleData,
    val longDistance: Boolean,
    val position: Double3,
    val offset: Float3,
    val speed: Float,
    val count: Int
) : Packet<ServerPlayPacketHandler> {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeInt(buffer.registries.particleTypes.getId(data.type))
        buffer.writeBoolean(longDistance)
        buffer.writeDouble3(position)
        buffer.writeFloat3(offset)
        buffer.writeFloat(speed)
        buffer.writeInt(count)
        data.writeToBuffer(buffer)
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.particle(this)
}

/**
 * @author Kevin Ludwig
 */
object ServerParticlePacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): ServerParticlePacket {
        val type = checkNotNull( buffer.registries.particleTypes[buffer.readInt()])
        val longDistance = buffer.readBoolean()
        val position = buffer.readDouble3()
        val offset = buffer.readFloat3()
        val speed = buffer.readFloat()
        val count = buffer.readInt()
        val data = buffer.readParticle(type)
        return ServerParticlePacket(data, longDistance, position, offset, speed, count)
    }
}
