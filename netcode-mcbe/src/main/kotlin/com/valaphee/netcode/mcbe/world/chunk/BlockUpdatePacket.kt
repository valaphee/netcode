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

package com.valaphee.netcode.mcbe.world.chunk

import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mcbe.Packet
import com.valaphee.netcode.mcbe.PacketBuffer
import com.valaphee.netcode.mcbe.PacketHandler
import com.valaphee.netcode.mcbe.PacketReader
import com.valaphee.netcode.mcbe.Restrict
import com.valaphee.netcode.mcbe.Restriction
import java.util.EnumSet

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class BlockUpdatePacket(
    val position: Int3,
    val runtimeId: Int,
    val flags: Set<Flag>,
    val layer: Int
) : Packet() {
    enum class Flag {
        Neighbors, Network, NonVisual, Priority;

        companion object {
            val All: EnumSet<Flag> = EnumSet.of(Neighbors, Network)
            val AllPriority: EnumSet<Flag> = EnumSet.of(Neighbors, Network, Priority)
        }
    }

    override val id get() = 0x15

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeInt3UnsignedY(position)
        buffer.writeVarUInt(runtimeId)
        buffer.writeVarUIntFlags(flags)
        buffer.writeVarUInt(layer)
    }

    override fun handle(handler: PacketHandler) = handler.blockUpdate(this)

    override fun toString() = "BlockUpdatePacket(position=$position, runtimeId=$runtimeId, flags=$flags, layer=$layer)"
}

/**
 * @author Kevin Ludwig
 */
object BlockUpdatePacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = BlockUpdatePacket(buffer.readInt3UnsignedY(), buffer.readVarUInt(), buffer.readVarUIntFlags(), buffer.readVarUInt())
}
