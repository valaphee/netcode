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

package com.valaphee.netcode.mcbe.world.chunk

import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mcbe.Packet
import com.valaphee.netcode.mcbe.PacketBuffer
import com.valaphee.netcode.mcbe.PacketHandler
import com.valaphee.netcode.mcbe.PacketReader

/**
 * @author Kevin Ludwig
 */
class BlockEventPacket(
    val position: Int3,
    val data1: Int,
    val data2: Int
) : Packet() {
    override val id get() = 0x1A

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeInt3UnsignedY(position)
        buffer.writeVarInt(data1)
        buffer.writeVarInt(data2)
    }

    override fun handle(handler: PacketHandler) = handler.blockEvent(this)

    override fun toString() = "BlockEventPacket(position=$position, data1=$data1, data2=$data2)"
}

/**
 * @author Kevin Ludwig
 */
object BlockEventPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = BlockEventPacket(buffer.readInt3UnsignedY(), buffer.readVarInt(), buffer.readVarInt())
}
