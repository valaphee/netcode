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

import com.valaphee.foundry.math.Float3
import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.PacketReader
import com.valaphee.netcode.mcje.network.ServerPlayPacketHandler
import com.valaphee.netcode.util.safeList

/**
 * @author Kevin Ludwig
 */
class ServerExplosionPacket(
    val position: Float3,
    val radius: Float,
    val affectedBlocks: List<Int3>,
    val velocity: Float3
) : Packet<ServerPlayPacketHandler>() {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeFloat3(position)
        buffer.writeFloat(radius)
        if (version >= 758) buffer.writeVarInt(affectedBlocks.size) else buffer.writeInt(affectedBlocks.size)
        affectedBlocks.forEach { (x, y, z) ->
            buffer.writeByte(x)
            buffer.writeByte(y)
            buffer.writeByte(z)
        }
        buffer.writeFloat3(velocity)
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.explosion(this)

    override fun toString() = "ServerExplosionPacket(position=$position, radius=$radius, affectedBlocks=$affectedBlocks, velocity=$velocity)"
}

/**
 * @author Kevin Ludwig
 */
object ServerExplosionPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerExplosionPacket(buffer.readFloat3(), buffer.readFloat(), safeList(if (version >= 758) buffer.readVarInt() else buffer.readInt()) { Int3(buffer.readByte().toInt(), buffer.readByte().toInt(), buffer.readByte().toInt()) }, buffer.readFloat3())
}
