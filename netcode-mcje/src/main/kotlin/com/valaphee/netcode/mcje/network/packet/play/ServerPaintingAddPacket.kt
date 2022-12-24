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

import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.ServerPlayPacketHandler
import com.valaphee.netcode.mcje.util.Direction
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
class ServerPaintingAddPacket(
    val entityId: Int,
    val entityUid: UUID,
    val entityTypeId: Int,
    val position: Int3,
    val direction: Direction
) : Packet<ServerPlayPacketHandler>() {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(entityId)
        buffer.writeUuid(entityUid)
        buffer.writeVarInt(entityTypeId)
        buffer.writeBlockPosition(position)
        buffer.writeByte(direction.horizontalIndex)
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.paintingAdd(this)

    override fun toString() = "ServerPaintingAddPacket(entityId=$entityId, entityUid=$entityUid, entityTypeId=$entityTypeId, position=$position, direction=$direction)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = ServerPaintingAddPacket(buffer.readVarInt(), buffer.readUuid(), buffer.readVarInt(), buffer.readBlockPosition(), Direction.horizontals[buffer.readByte().toInt()])
    }
}
