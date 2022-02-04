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

import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.PacketReader
import com.valaphee.netcode.mcbe.network.Restrict
import com.valaphee.netcode.mcbe.network.Restriction
import com.valaphee.netcode.util.safeList

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToServer)
class SubChunkRequestPacket(
    val dimension: Int,
    val position: Int3,
    val requestCount: Int,
    val requests: List<Int3>
) : Packet() {
    override val id get() = 0xAF

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(dimension)
        buffer.writeInt3(position)
        if (version >= 486) {
            buffer.writeIntLE(requestCount)
            buffer.writeShortLE(requests.size)
            requests.forEach {
                buffer.writeByte(it.x)
                buffer.writeByte(it.y)
                buffer.writeByte(it.z)
            }
        }
    }

    override fun handle(handler: PacketHandler) = handler.subChunkRequest(this)

    override fun toString() = "SubChunkRequestPacket(dimension=$dimension, position=$position)"
}

/**
 * @author Kevin Ludwig
 */
object SubChunkRequestPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): SubChunkRequestPacket {
        val dimension = buffer.readVarInt()
        val position = buffer.readInt3()
        val requestCount: Int
        val requests: List<Int3>
        if (version >= 486) {
            requestCount = buffer.readIntLE()
            requests = safeList(buffer.readUnsignedShortLE()) { Int3(buffer.readByte().toInt(), buffer.readByte().toInt(), buffer.readByte().toInt()) }
        } else {
            requestCount = 0
            requests = emptyList()
        }
        return SubChunkRequestPacket(dimension, position, requestCount, requests)
    }
}
