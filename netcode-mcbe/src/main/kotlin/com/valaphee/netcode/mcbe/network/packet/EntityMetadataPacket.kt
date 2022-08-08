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

import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.PacketReader
import com.valaphee.netcode.mcbe.network.Restrict
import com.valaphee.netcode.mcbe.network.Restriction
import com.valaphee.netcode.mcbe.network.V1_16_201
import com.valaphee.netcode.mcbe.world.entity.metadata.Metadata

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class EntityMetadataPacket(
    val runtimeEntityId: Long,
    val metadata: Metadata,
    val tick: Long = 0
) : Packet() {
    override val id get() = 0x27

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarULong(runtimeEntityId)
        metadata.writeToBuffer(buffer)
        if (version >= V1_16_201) buffer.writeVarULong(tick)
    }

    override fun handle(handler: PacketHandler) = handler.entityMetadata(this)

    override fun toString() = "EntityMetadataPacket(runtimeEntityId=$runtimeEntityId, metadata=$metadata, tick=$tick)"
}

/**
 * @author Kevin Ludwig
 */
object EntityMetadataPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = EntityMetadataPacket(buffer.readVarULong(), Metadata().apply { readFromBuffer(buffer) }, if (version >= V1_16_201) buffer.readVarULong() else 0)
}
