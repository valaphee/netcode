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

import com.valaphee.netcode.mcbe.Restrict
import com.valaphee.netcode.mcbe.Restriction
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.PacketReader
import com.valaphee.netcode.mcbe.world.item.ItemStack
import com.valaphee.netcode.mcbe.world.item.readStack
import com.valaphee.netcode.mcbe.world.item.readStackPre431
import com.valaphee.netcode.mcbe.world.item.readStackWithNetIdPre431
import com.valaphee.netcode.mcbe.world.item.writeStack
import com.valaphee.netcode.mcbe.world.item.writeStackPre431
import com.valaphee.netcode.mcbe.world.item.writeStackWithNetIdPre431

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class InventorySlotPacket(
    val windowId: Int,
    val slotId: Int,
    val itemStack: ItemStack?
) : Packet() {
    override val id get() = 0x32

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarUInt(windowId)
        buffer.writeVarUInt(slotId)
        if (version >= 431) buffer.writeStack(itemStack) else if (version >= 407) buffer.writeStackWithNetIdPre431(itemStack) else buffer.writeStackPre431(itemStack)
    }

    override fun handle(handler: PacketHandler) = handler.inventorySlot(this)

    override fun toString() = "InventorySlotPacket(windowId=$windowId, slotId=$slotId, stack=$itemStack)"
}

/**
 * @author Kevin Ludwig
 */
object InventorySlotPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = InventorySlotPacket(buffer.readVarUInt(), buffer.readVarUInt(), if (version >= 431) buffer.readStack() else if (version >= 407) buffer.readStackWithNetIdPre431() else buffer.readStackPre431())
}
