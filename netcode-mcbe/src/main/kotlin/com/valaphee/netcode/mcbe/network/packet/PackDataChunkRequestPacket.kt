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
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToServer)
class PackDataChunkRequestPacket(
    val packId: UUID,
    val packVersion: String?,
    val chunkIndex: Long
) : Packet() {
    override val id get() = 0x54

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeString("$packId${packVersion?.let { "_$it" } ?: ""}")
        buffer.writeIntLE(chunkIndex.toInt())
    }

    override fun handle(handler: PacketHandler) = handler.packDataChunkRequest(this)

    override fun toString() = "PackDataChunkRequestPacket(packId=$packId, packVersion=$packVersion, chunkIndex=$chunkIndex)"
}

/**
 * @author Kevin Ludwig
 */
object PackDataChunkRequestPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): PackDataChunkRequestPacket {
        val pack = buffer.readString().split("_".toRegex(), 2).toTypedArray()
        val packId = UUID.fromString(pack[0])
        val packVersion = if (pack.size == 2) pack[1] else null
        val chunkIndex = buffer.readUnsignedIntLE()
        return PackDataChunkRequestPacket(packId, packVersion, chunkIndex)
    }
}
