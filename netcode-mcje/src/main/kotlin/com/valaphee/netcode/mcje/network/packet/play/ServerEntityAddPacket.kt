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
import com.valaphee.netcode.mcje.util.NamespacedKey
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
class ServerEntityAddPacket(
    val entityId: Int,
    val entityUid: UUID?,
    val typeKey: NamespacedKey,
    val position: Double3,
    val rotation: Float2,
    val headRotationYaw: Float,
    val velocity: Double3
) : Packet<ServerPlayPacketHandler> {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(entityId)
        buffer.writeUuid(entityUid!!)
        buffer.writeVarInt(buffer.registrySet.entityTypes.getId(typeKey))
        buffer.writeDouble3(position)
        buffer.writeAngle2(rotation)
        buffer.writeAngle(headRotationYaw)
        val (velocityX, velocityY, velocityZ) = velocity.toMutableDouble3().scale(8000.0).toInt3()
        buffer.writeShort(velocityX)
        buffer.writeShort(velocityY)
        buffer.writeShort(velocityZ)
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.entityAdd(this)

    override fun toString() = "ServerEntityAddPacket(entityId=$entityId, entityUid=$entityUid, entityTypeKey=$typeKey, position=$position, rotation=$rotation, headRotationYaw=$headRotationYaw, motion=$velocity)"
}

/**
 * @author Kevin Ludwig
 */
object ServerEntityAddPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerEntityAddPacket(buffer.readVarInt(), buffer.readUuid(), checkNotNull(buffer.registrySet.entityTypes[buffer.readVarInt()]), buffer.readDouble3(), buffer.readAngle2(), buffer.readAngle(), MutableDouble3(buffer.readShort().toDouble(), buffer.readShort().toDouble(), buffer.readShort().toDouble()).scale(1 / 8000.0))
}
