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
import com.valaphee.netcode.mcbe.world.item.ItemStack
import com.valaphee.netcode.mcbe.world.item.readStack
import com.valaphee.netcode.mcbe.world.item.readStackPre431
import com.valaphee.netcode.mcbe.world.item.writeStack
import com.valaphee.netcode.mcbe.world.item.writeStackPre431

/**
 * @author Kevin Ludwig
 */
class EntityEquipmentPacket(
    val runtimeEntityId: Long,
    val itemStack: ItemStack?,
    val slotId: Int,
    val hotbarSlot: Int,
    val windowId: Int
) : Packet() {
    override val id get() = 0x1F

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarULong(runtimeEntityId)
        if (version >= 431) buffer.writeStack(itemStack) else buffer.writeStackPre431(itemStack)
        buffer.writeByte(slotId)
        buffer.writeByte(hotbarSlot)
        buffer.writeByte(windowId)
    }

    override fun handle(handler: PacketHandler) = handler.entityEquipment(this)

    override fun toString() = "EntityEquipmentPacket(runtimeEntityId=$runtimeEntityId, stack=$itemStack, slotId=$slotId, hotbarSlot=$hotbarSlot, windowId=$windowId)"
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
