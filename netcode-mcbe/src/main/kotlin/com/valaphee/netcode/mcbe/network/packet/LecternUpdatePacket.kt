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
import com.valaphee.netcode.mcbe.Restrict
import com.valaphee.netcode.mcbe.Restriction

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToServer)
class LecternUpdatePacket(
    val page: Int,
    val totalPages: Int,
    val position: Int3,
    val droppingBook: Boolean
) : Packet() {
    override val id get() = 0x7D

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeByte(page)
        buffer.writeByte(totalPages)
        buffer.writeInt3UnsignedY(position)
        buffer.writeBoolean(droppingBook)
    }

    override fun handle(handler: PacketHandler) = handler.lecternUpdate(this)

    override fun toString() = "LecternUpdatePacket(page=$page, totalPages=$totalPages, position=$position, droppingBook=$droppingBook)"
}

/**
 * @author Kevin Ludwig
 */
object LecternUpdatePacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = LecternUpdatePacket(buffer.readUnsignedByte().toInt(), buffer.readUnsignedByte().toInt(), buffer.readInt3UnsignedY(), buffer.readBoolean())
}
