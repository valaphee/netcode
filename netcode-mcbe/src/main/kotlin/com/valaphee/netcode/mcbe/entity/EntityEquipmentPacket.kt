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

package com.valaphee.netcode.mcbe.entity

import com.valaphee.netcode.mcbe.Packet
import com.valaphee.netcode.mcbe.PacketBuffer
import com.valaphee.netcode.mcbe.PacketHandler
import com.valaphee.netcode.mcbe.PacketReader
import com.valaphee.netcode.mcbe.item.stack.Stack
import com.valaphee.netcode.mcbe.item.stack.readStack
import com.valaphee.netcode.mcbe.item.stack.readStackPre431
import com.valaphee.netcode.mcbe.item.stack.writeStack
import com.valaphee.netcode.mcbe.item.stack.writeStackPre431

/**
 * @author Kevin Ludwig
 */
class EntityEquipmentPacket(
    val runtimeEntityId: Long,
    val stack: Stack?,
    val slotId: Int,
    val hotbarSlot: Int,
    val windowId: Int
) : Packet() {
    override val id get() = 0x1F

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarULong(runtimeEntityId)
        if (version >= 431) buffer.writeStack(stack) else buffer.writeStackPre431(stack)
        buffer.writeByte(slotId)
        buffer.writeByte(hotbarSlot)
        buffer.writeByte(windowId)
    }

    override fun handle(handler: PacketHandler) = handler.entityEquipment(this)

    override fun toString() = "EntityEquipmentPacket(runtimeEntityId=$runtimeEntityId, stack=$stack, slotId=$slotId, hotbarSlot=$hotbarSlot, windowId=$windowId)"
}

/**
 * @author Kevin Ludwig
 */
object EntityEquipmentPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = EntityEquipmentPacket(
        buffer.readVarULong(),
        if (version >= 431) buffer.readStack() else buffer.readStackPre431(),
        buffer.readUnsignedByte().toInt(),
        buffer.readUnsignedByte().toInt(),
        buffer.readByte().toInt()
    )
}
