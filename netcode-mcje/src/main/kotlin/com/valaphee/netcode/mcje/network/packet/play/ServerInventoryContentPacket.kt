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
import com.valaphee.netcode.mcje.network.ServerPlayPacketHandler
import com.valaphee.netcode.mcje.network.V1_18_2
import com.valaphee.netcode.mcje.network.V1_19_0
import com.valaphee.netcode.mcje.world.item.ItemStack
import com.valaphee.netcode.mcje.world.item.readItemStack
import com.valaphee.netcode.mcje.world.item.writeItemStack
import com.valaphee.netcode.util.LazyList

/**
 * @author Kevin Ludwig
 */
class ServerInventoryContentPacket(
    val windowId: Int,
    val stateId: Int,
    val content: List<ItemStack?>,
    val itemStackInHand: ItemStack?
) : Packet<ServerPlayPacketHandler>() {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeByte(windowId)
        if (version >= V1_18_2) buffer.writeVarInt(stateId)
        if (version >= V1_19_0) buffer.writeVarInt(content.size) else buffer.writeShort(content.size)
        content.forEach { buffer.writeItemStack(it, version) }
        if (version >= V1_19_0) buffer.writeItemStack(itemStackInHand, version)
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.inventoryContent(this)

    override fun toString() = "ServerInventoryContentPacket(windowId=$windowId, stateId=$stateId, content=$content, itemStackInHand=$itemStackInHand)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = ServerInventoryContentPacket(buffer.readUnsignedByte().toInt(), if (version >= V1_18_2) buffer.readVarInt() else 0, LazyList(if (version >= V1_19_0) buffer.readVarInt() else buffer.readShort().toInt()) { buffer.readItemStack(version) }, if (version >= V1_19_0) buffer.readItemStack(version) else null)
    }
}
