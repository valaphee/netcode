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
import com.valaphee.netcode.mcbe.world.item.ItemStack
import com.valaphee.netcode.mcbe.world.item.readItemStack
import com.valaphee.netcode.mcbe.world.item.readItemStackPre431
import com.valaphee.netcode.mcbe.world.item.readItemStackWithNetIdPre431
import com.valaphee.netcode.mcbe.world.item.writeItemStack
import com.valaphee.netcode.mcbe.world.item.writeItemStackPre431
import com.valaphee.netcode.mcbe.world.item.writeItemStackWithNetIdPre431

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
        if (version >= 431) buffer.writeItemStack(itemStack) else if (version >= 407) buffer.writeItemStackWithNetIdPre431(itemStack) else buffer.writeItemStackPre431(itemStack)
    }

    override fun handle(handler: PacketHandler) = handler.inventorySlot(this)

    override fun toString() = "InventorySlotPacket(windowId=$windowId, slotId=$slotId, stack=$itemStack)"
}

/**
 * @author Kevin Ludwig
 */
object InventorySlotPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = InventorySlotPacket(buffer.readVarUInt(), buffer.readVarUInt(), if (version >= 431) buffer.readItemStack() else if (version >= 407) buffer.readItemStackWithNetIdPre431() else buffer.readItemStackPre431())
}
