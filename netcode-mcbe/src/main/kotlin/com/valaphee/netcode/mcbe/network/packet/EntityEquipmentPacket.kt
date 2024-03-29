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
import com.valaphee.netcode.mcbe.world.item.ItemStack
import com.valaphee.netcode.mcbe.world.item.readItemStack
import com.valaphee.netcode.mcbe.world.item.writeItemStack

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
        buffer.writeItemStack(itemStack, version)
        buffer.writeByte(slotId)
        buffer.writeByte(hotbarSlot)
        buffer.writeByte(windowId)
    }

    override fun handle(handler: PacketHandler) = handler.entityEquipment(this)

    override fun toString() = "EntityEquipmentPacket(runtimeEntityId=$runtimeEntityId, stack=$itemStack, slotId=$slotId, hotbarSlot=$hotbarSlot, windowId=$windowId)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = EntityEquipmentPacket(buffer.readVarULong(), buffer.readItemStack(version), buffer.readUnsignedByte().toInt(), buffer.readUnsignedByte().toInt(), buffer.readByte().toInt())
    }
}
