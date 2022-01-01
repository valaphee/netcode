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

package com.valaphee.netcode.mcbe.inventory

import com.valaphee.netcode.mcbe.Packet
import com.valaphee.netcode.mcbe.PacketBuffer
import com.valaphee.netcode.mcbe.PacketHandler
import com.valaphee.netcode.mcbe.PacketReader
import com.valaphee.netcode.mcbe.Restrict
import com.valaphee.netcode.mcbe.Restriction
import com.valaphee.netcode.mcbe.item.stack.Stack
import com.valaphee.netcode.mcbe.item.stack.readStack
import com.valaphee.netcode.mcbe.item.stack.readStackPre431
import com.valaphee.netcode.mcbe.item.stack.readStackWithNetIdPre431
import com.valaphee.netcode.mcbe.item.stack.writeStack
import com.valaphee.netcode.mcbe.item.stack.writeStackPre431
import com.valaphee.netcode.mcbe.item.stack.writeStackWithNetIdPre431

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class InventorySlotPacket(
    val windowId: Int,
    val slotId: Int,
    val stack: Stack?
) : Packet() {
    override val id get() = 0x32

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarUInt(windowId)
        buffer.writeVarUInt(slotId)
        if (version >= 431) buffer.writeStack(stack) else if (version >= 407) buffer.writeStackWithNetIdPre431(stack) else buffer.writeStackPre431(stack)
    }

    override fun handle(handler: PacketHandler) = handler.inventorySlot(this)

    override fun toString() = "InventorySlotPacket(windowId=$windowId, slotId=$slotId, stack=$stack)"
}

/**
 * @author Kevin Ludwig
 */
object InventorySlotPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = InventorySlotPacket(buffer.readVarUInt(), buffer.readVarUInt(), if (version >= 431) buffer.readStack() else if (version >= 407) buffer.readStackWithNetIdPre431() else buffer.readStackPre431())
}
