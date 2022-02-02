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
import com.valaphee.netcode.mcje.network.PacketReader
import com.valaphee.netcode.mcje.network.ServerPlayPacketHandler
import com.valaphee.netcode.util.safeList

/**
 * @author Kevin Ludwig
 */
class ServerBlockUpdatesPacket(
    val subChunkPosition: Int3,
    val trustEdges: Boolean,
    val updates: List<Update>,
) : Packet<ServerPlayPacketHandler> {
    data class Update(
        val position: Int3,
        val id: Int
    )

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeLong(((subChunkPosition.x and 0x3FFFFF).toLong() shl 42) or ((subChunkPosition.z and 0x3FFFFF).toLong() shl 20) or (subChunkPosition.y and 0xFFFFF).toLong())
        buffer.writeBoolean(trustEdges)
        buffer.writeVarInt(updates.size)
        updates.forEach {
            val position = it.position
            buffer.writeVarLong((it.id and 0xF shl 12).toLong() or ((position.x and 0xF).toLong() shl 8) or ((position.z and 0xF).toLong() shl 4) or (position.y and 0xF).toLong())
        }
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.blockUpdates(this)

    override fun toString() = "ServerBlockUpdatesPacket(subChunkPosition=$subChunkPosition, trustEdges=$trustEdges, updates=$updates)"
}

/**
 * @author Kevin Ludwig
 */
object ServerBlockUpdatesPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): ServerBlockUpdatesPacket {
        val chunkSectionPosition = buffer.readLong()
        val trustEdges = buffer.readBoolean()
        val updates = safeList(buffer.readVarInt()) {
            val update = buffer.readVarLong()
            ServerBlockUpdatesPacket.Update(Int3((update.toInt() shr 8) and 0xF, update.toInt() and 0xF, (update.toInt() shr 4) and 0xF), (update shr 12).toInt())
        }
        return ServerBlockUpdatesPacket(Int3((chunkSectionPosition shr 42).toInt(), ((chunkSectionPosition shl 44) shr 44).toInt(), ((chunkSectionPosition shl 22) shr 42).toInt()), trustEdges, updates)
    }
}
