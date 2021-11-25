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

package com.valaphee.netcode.mcbe.world

import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mcbe.Packet
import com.valaphee.netcode.mcbe.PacketBuffer
import com.valaphee.netcode.mcbe.PacketHandler
import com.valaphee.netcode.mcbe.PacketReader
import com.valaphee.netcode.mcbe.Restrict
import com.valaphee.netcode.mcbe.Restriction

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class SpawnPositionPacket(
    val type: Type,
    val blockPosition: Int3,
    val dimensionId: Int,
    val position: Int3,
    val forced: Boolean
) : Packet() {
    enum class Type {
        PlayerSpawn, WorldSpawn
    }

    override val id get() = 0x2B

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(type.ordinal)
        if (version >= 407) {
            buffer.writeInt3UnsignedY(blockPosition)
            buffer.writeVarUInt(dimensionId)
        }
        buffer.writeInt3UnsignedY(position)
        if (version < 407) buffer.writeBoolean(forced)
    }

    override fun handle(handler: PacketHandler) = handler.spawnPosition(this)

    override fun toString() = "SpawnPositionPacket(type=$type, blockPosition=$blockPosition, dimensionId=$dimensionId, position=$position, forced=$forced)"
}

/**
 * @author Kevin Ludwig
 */
object SpawnPositionPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): SpawnPositionPacket {
        val type = SpawnPositionPacket.Type.values()[buffer.readVarInt()]
        val blockPosition: Int3
        val dimensionId: Int
        if (version >= 407) {
            blockPosition = buffer.readInt3UnsignedY()
            dimensionId = buffer.readVarUInt()
        } else {
            blockPosition = Int3.Zero
            dimensionId = 0
        }
        val position = buffer.readInt3UnsignedY()
        val forced = if (version < 407) buffer.readBoolean() else false
        return SpawnPositionPacket(type, blockPosition, dimensionId, position, forced)
    }
}
