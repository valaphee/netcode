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
import com.valaphee.netcode.mcbe.network.V1_19_020
import com.valaphee.netcode.util.safeList

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToServer)
class MapRequestPacket(
    val mapId: Long,
    val pixels: List<Pair<Int, Int>>
) : Packet() {
    override val id get() = 0x44

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarLong(mapId)

        if (version >= V1_19_020) {
            buffer.writeIntLE(pixels.size)
            pixels.forEach {
                buffer.writeIntLE(it.first)
                buffer.writeShortLE(it.second)
            }
        }
    }

    override fun handle(handler: PacketHandler) = handler.mapRequest(this)

    override fun toString() = "MapRequestPacket(mapId=$mapId, pixels=$pixels)"
}

/**
 * @author Kevin Ludwig
 */
object MapRequestPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = MapRequestPacket(buffer.readVarLong(), if (version >= V1_19_020) safeList(buffer.readIntLE()) { buffer.readIntLE() to buffer.readUnsignedShortLE() } else emptyList())
}
