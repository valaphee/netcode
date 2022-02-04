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
import com.valaphee.netcode.mcbe.Restrict
import com.valaphee.netcode.mcbe.Restriction
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.PacketReader
import com.valaphee.netcode.mcbe.world.inventory.WindowType

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class WindowOpenPacket(
    val windowId: Int,
    val type: WindowType,
    val blockPosition: Int3,
    val uniqueEntityId: Long
) : Packet() {
    override val id get() = 0x2E

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeByte(windowId)
        buffer.writeByte(type.id)
        buffer.writeInt3UnsignedY(blockPosition)
        buffer.writeVarLong(uniqueEntityId)
    }

    override fun handle(handler: PacketHandler) = handler.windowOpen(this)

    override fun toString() = "WindowOpenPacket(windowId=$windowId, type=$type, blockPosition=$blockPosition, uniqueEntityId=$uniqueEntityId)"
}

/**
 * @author Kevin Ludwig
 */
object WindowOpenPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = WindowOpenPacket(buffer.readByte().toInt(), WindowType.byId(buffer.readByte().toInt()), buffer.readInt3UnsignedY(), buffer.readVarLong())
}
