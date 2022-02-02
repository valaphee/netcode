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

package com.valaphee.netcode.mcje.play

import com.valaphee.netcode.mcje.Packet
import com.valaphee.netcode.mcje.PacketBuffer
import com.valaphee.netcode.mcje.PacketReader
import com.valaphee.netcode.mcje.item.stack.Stack
import com.valaphee.netcode.mcje.item.stack.readStack
import com.valaphee.netcode.mcje.item.stack.writeStack

/**
 * @author Kevin Ludwig
 */
class ServerInventorySlotPacket(
    val windowId: Int,
    val slotId: Int,
    val stack: Stack?
) : Packet<ServerPlayPacketHandler> {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeByte(windowId)
        buffer.writeShort(slotId)
        buffer.writeStack(stack)
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.inventorySlot(this)

    override fun toString() = "ServerInventorySlotPacket(windowId=$windowId, slotId=$slotId, stack=$stack)"
}

/**
 * @author Kevin Ludwig
 */
object ServerInventorySlotPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerInventorySlotPacket(buffer.readByte().toInt(), buffer.readShort().toInt(), buffer.readStack())
}
