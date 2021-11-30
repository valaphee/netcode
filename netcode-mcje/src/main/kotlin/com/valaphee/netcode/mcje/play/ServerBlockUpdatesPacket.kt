/*
 * MIT License
 *
 * Copyright (c) 2021, Valaphee.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.valaphee.netcode.mcje.play

import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mcje.Packet
import com.valaphee.netcode.mcje.PacketBuffer
import com.valaphee.netcode.mcje.PacketReader
import com.valaphee.netcode.mcje.world.chunk.storage.SubChunk

/**
 * @author Kevin Ludwig
 */
class ServerBlockUpdatesPacket(
    val subChunkPosition: Int3,
    val updates: Array<Update>,
) : Packet<ServerPlayPacketHandler> {
    data class Update(
        val position: Int3,
        val id: Int
    )

    override fun write(buffer: PacketBuffer, version: Int) {
        if (version >= 754) {
            buffer.writeLong(((subChunkPosition.x and 0x3FFFFF).toLong() shl 42) or ((subChunkPosition.z and 0x3FFFFF).toLong() shl 20) or (subChunkPosition.y and 0xFFFFF).toLong())
            buffer.writeVarInt(updates.size)
            updates.forEach {
                val position = it.position
                buffer.writeVarLong((it.id and 0xF shl 12).toLong() or ((position.x and 0xF).toLong() shl 8) or ((position.z and 0xF).toLong() shl 4) or (position.y and 0xF).toLong())
            }
        } else {
            buffer.writeInt(subChunkPosition.x)
            buffer.writeInt(subChunkPosition.y)
            buffer.writeVarInt(updates.size)
            updates.forEach {
                buffer.writeByte(((it.position.x and 0xF) shl 4) or (it.position.z and 0xF))
                buffer.writeByte(subChunkPosition.y * SubChunk.YSize + it.position.y)
                buffer.writeVarInt(it.id)
            }
        }
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.blockUpdates(this)

    override fun toString() = "ServerBlockUpdatesPacket(subChunkPosition=$subChunkPosition, updates=${updates.contentToString()})"
}

/**
 * @author Kevin Ludwig
 */
object ServerBlockUpdatesPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): ServerBlockUpdatesPacket {
        val subChunkPosition: Int3
        val updates: Array<ServerBlockUpdatesPacket.Update>
        if (version >= 754) {
            val chunkSectionPosition = buffer.readLong()
            subChunkPosition = Int3((chunkSectionPosition shr 42).toInt(), ((chunkSectionPosition shl 44) shr 44).toInt(), ((chunkSectionPosition shl 22) shr 42).toInt())
            buffer.readBoolean()
            updates = Array(buffer.readVarInt()) {
                val update = buffer.readVarLong()
                ServerBlockUpdatesPacket.Update(Int3((update.toInt() shr 8) and 0xF, update.toInt() and 0xF, (update.toInt() shr 4) and 0xF), (update shr 12).toInt())
            }
        } else {
            subChunkPosition = Int3(buffer.readInt(), 0, buffer.readInt())
            updates = Array(buffer.readVarInt()) {
                val positionXZ = buffer.readByte().toInt()
                ServerBlockUpdatesPacket.Update(Int3((positionXZ shr 4) and 0xF, buffer.readUnsignedByte().toInt(), positionXZ and 0xF), buffer.readVarInt())
            }
        }
        return ServerBlockUpdatesPacket(subChunkPosition, updates)
    }
}
