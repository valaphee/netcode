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
import com.valaphee.netcode.mcbe.network.Restrict
import com.valaphee.netcode.mcbe.network.Restriction
import com.valaphee.netcode.mcbe.network.V1_18_030

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class VolumeEntityRemovePacket(
    val entityId: Int,
    val dimension: Int
) : Packet() {
    override val id = 0xA7

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarUInt(entityId)
        if (version >= V1_18_030) buffer.writeVarInt(dimension)
    }

    override fun handle(handler: PacketHandler) = handler.volumeEntityRemove(this)

    override fun toString() = "VolumeEntityRemovePacket(entityId=$entityId, dimension=$dimension)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = VolumeEntityRemovePacket(buffer.readVarUInt(), if (version >= V1_18_030) buffer.readVarInt() else 0)
    }
}
