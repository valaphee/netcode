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
import com.valaphee.netcode.mcbe.network.Restrict
import com.valaphee.netcode.mcbe.network.Restriction
import com.valaphee.netcode.mcbe.world.item.ItemStack
import com.valaphee.netcode.mcbe.world.item.readItemStackWithNetId
import com.valaphee.netcode.mcbe.world.item.writeItemStackWithNetId
import com.valaphee.netcode.util.LazyList

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class InventoryContentPacket(
    val windowId: Int,
    val content: List<ItemStack?>
) : Packet() {
    override val id get() = 0x31

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarUInt(windowId)
        buffer.writeVarUInt(content.size)
        content.forEach { buffer.writeItemStackWithNetId(it, version) }
    }

    override fun handle(handler: PacketHandler) = handler.inventoryContent(this)

    override fun toString() = "InventoryContentPacket(windowId=$windowId, content=$content)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = InventoryContentPacket(buffer.readVarUInt(), LazyList(buffer.readVarUInt()) { buffer.readItemStackWithNetId(version) })
    }
}
