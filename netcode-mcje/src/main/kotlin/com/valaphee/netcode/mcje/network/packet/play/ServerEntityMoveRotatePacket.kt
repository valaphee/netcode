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
import com.valaphee.foundry.math.Float2
import com.valaphee.foundry.math.MutableDouble3
import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.PacketReader
import com.valaphee.netcode.mcje.network.ServerPlayPacketHandler

/**
 * @author Kevin Ludwig
 */
class ServerEntityMoveRotatePacket(
    val entityId: Int,
    val positionDelta: Double3?,
    val rotation: Float2?,
    val onGround: Boolean
) : Packet<ServerPlayPacketHandler> {
    override fun getId(id: Int, version: Int) = id + (if (positionDelta != null) if (rotation != null) 1 else 0 else if (rotation != null) 2 else if (version >= 758) TODO() else 3)

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(entityId)
        positionDelta?.let { (x, y, z) ->
            buffer.writeShort((x * 4096).toInt())
            buffer.writeShort((y * 4096).toInt())
            buffer.writeShort((z * 4096).toInt())
        }
        rotation?.let { buffer.writeAngle2(it) }
        if (positionDelta != null || rotation != null) buffer.writeBoolean(onGround)
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.entityMoveRotate(this)

    override fun toString() = "ServerEntityMoveRotatePacket(entityId=$entityId, positionDelta=$positionDelta, rotation=$rotation, onGround=$onGround)"
}

/**
 * @author Kevin Ludwig
 */
object ServerEntityMovePacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerEntityMoveRotatePacket(buffer.readVarInt(), MutableDouble3(buffer.readShort().toDouble(), buffer.readShort().toDouble(), buffer.readShort().toDouble()).scale(1 / 4096.0), null, buffer.readBoolean())
}

/**
 * @author Kevin Ludwig
 */
object ServerEntityMoveRotatePacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerEntityMoveRotatePacket(buffer.readVarInt(), MutableDouble3(buffer.readShort().toDouble(), buffer.readShort().toDouble(), buffer.readShort().toDouble()).scale(1 / 4096.0), buffer.readAngle2(), buffer.readBoolean())
}

/**
 * @author Kevin Ludwig
 */
object ServerEntityRotatePacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerEntityMoveRotatePacket(buffer.readVarInt(), null, buffer.readAngle2(), buffer.readBoolean())
}

/**
 * @author Kevin Ludwig
 */
object ServerEntityLocationPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerEntityMoveRotatePacket(buffer.readVarInt(), null, null, false)
}
