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

package com.valaphee.netcode.mcje.network.packet.play

import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.Packet.Reader
import com.valaphee.netcode.mcje.network.ServerPlayPacketHandler
import com.valaphee.netcode.mcje.network.V1_18_2
import com.valaphee.netcode.mcje.world.item.ItemStack
import com.valaphee.netcode.mcje.world.item.readItemStack
import com.valaphee.netcode.mcje.world.item.writeItemStack

/**
 * @author Kevin Ludwig
 */
class ServerInventorySlotPacket(
    val windowId: Int,
    val stateId: Int,
    val slotId: Int,
    val itemStack: ItemStack?
) : Packet<ServerPlayPacketHandler>() {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeByte(windowId)
        if (version >= V1_18_2) buffer.writeVarInt(stateId)
        buffer.writeShort(slotId)
        buffer.writeItemStack(itemStack)
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.inventorySlot(this)

    override fun toString() = "ServerInventorySlotPacket(windowId=$windowId, stateId=$stateId, slotId=$slotId, stack=$itemStack)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = ServerInventorySlotPacket(buffer.readByte().toInt(), if (version >= V1_18_2) buffer.readVarInt() else 0, buffer.readShort().toInt(), buffer.readItemStack())
    }
}
