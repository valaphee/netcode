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

package com.valaphee.netcode.mcbe.network.packet

import com.valaphee.foundry.math.Float2
import com.valaphee.foundry.math.Float3
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.PacketReader
import com.valaphee.netcode.mcbe.network.Restrict
import com.valaphee.netcode.mcbe.network.Restriction
import com.valaphee.netcode.mcbe.world.entity.Link
import com.valaphee.netcode.mcbe.world.entity.attribute.Attributes
import com.valaphee.netcode.mcbe.world.entity.metadata.Metadata
import com.valaphee.netcode.mcbe.world.entity.readLink
import com.valaphee.netcode.mcbe.world.entity.readLinkPre407
import com.valaphee.netcode.mcbe.world.entity.writeLink
import com.valaphee.netcode.mcbe.world.entity.writeLinkPre407
import com.valaphee.netcode.util.safeList

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class EntityAddPacket(
    val uniqueEntityId: Long,
    val runtimeEntityId: Long,
    val type: String,
    val position: Float3,
    val velocity: Float3,
    val rotation: Float2,
    val headRotationYaw: Float,
    val bodyRotation: Float,
    val attributes: Attributes,
    val metadata: Metadata,
    val links: List<Link>
) : Packet() {
    override val id get() = 0x0D

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarLong(uniqueEntityId)
        buffer.writeVarULong(runtimeEntityId)
        buffer.writeString(type)
        buffer.writeFloat3(position)
        buffer.writeFloat3(velocity)
        buffer.writeFloat2(rotation)
        buffer.writeFloatLE(headRotationYaw)
        if (version >= 534) buffer.writeFloatLE(bodyRotation)
        attributes.writeToBuffer(buffer, false)
        metadata.writeToBuffer(buffer)
        buffer.writeVarUInt(links.size)
        if (version >= 407) links.forEach(buffer::writeLink) else links.forEach(buffer::writeLinkPre407)
    }

    override fun handle(handler: PacketHandler) = handler.entityAdd(this)

    override fun toString() = "EntityAddPacket(uniqueEntityId=$uniqueEntityId, runtimeEntityId=$runtimeEntityId, type='$type', position=$position, velocity=$velocity, rotation=$rotation, headRotationYaw=$headRotationYaw, bodyRotation=$bodyRotation, attributes=$attributes, metadata=$metadata, links=$links)"
}

/**
 * @author Kevin Ludwig
 */
object EntityAddPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = EntityAddPacket(
        buffer.readVarLong(),
        buffer.readVarULong(),
        buffer.readString(),
        buffer.readFloat3(),
        buffer.readFloat3(),
        buffer.readFloat2(),
        buffer.readFloatLE(),
        if (version >= 534) buffer.readFloatLE() else 0.0f,
        Attributes().apply { readFromBuffer(buffer, false) },
        Metadata().apply { readFromBuffer(buffer) },
        safeList(buffer.readVarUInt()) { if (version >= 407) buffer.readLink() else buffer.readLinkPre407() }
    )
}
