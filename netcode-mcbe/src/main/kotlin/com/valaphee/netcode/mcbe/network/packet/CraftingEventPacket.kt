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
import com.valaphee.netcode.mcbe.network.V1_16_221
import com.valaphee.netcode.mcbe.world.item.ItemStack
import com.valaphee.netcode.mcbe.world.item.readItemStack
import com.valaphee.netcode.mcbe.world.item.readItemStackPreV1_16_221
import com.valaphee.netcode.mcbe.world.item.writeItemStack
import com.valaphee.netcode.mcbe.world.item.writeItemStackPreV1_16_221
import com.valaphee.netcode.util.safeList
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
class CraftingEventPacket(
    val windowId: Int,
    val type: Type,
    val recipeId: UUID,
    val inputs: List<ItemStack?>,
    val outputs: List<ItemStack?>,
) : Packet() {
    enum class Type {
        Inventory, Crafting, Workbench
    }

    override val id get() = 0x35

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeByte(windowId)
        buffer.writeVarInt(type.ordinal)
        buffer.writeUuid(recipeId)
        buffer.writeVarUInt(inputs.size)
        inputs.forEach { if (version >= V1_16_221) buffer.writeItemStack(it) else buffer.writeItemStackPreV1_16_221(it) }
        buffer.writeVarUInt(outputs.size)
        outputs.forEach { if (version >= V1_16_221) buffer.writeItemStack(it) else buffer.writeItemStackPreV1_16_221(it) }
    }

    override fun handle(handler: PacketHandler) = handler.craftingEvent(this)

    override fun toString() = "CraftingEventPacket(windowId=$windowId, type=$type, recipeId=$recipeId, inputs=$inputs, outputs=$outputs)"
}

/**
 * @author Kevin Ludwig
 */
object CraftingEventPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = CraftingEventPacket(buffer.readUnsignedByte().toInt(), CraftingEventPacket.Type.values()[buffer.readVarInt()], buffer.readUuid(), safeList(buffer.readVarUInt()) { if (version >= V1_16_221) buffer.readItemStack() else buffer.readItemStackPreV1_16_221() }, safeList(buffer.readVarUInt()) { if (version >= V1_16_221) buffer.readItemStack() else buffer.readItemStackPreV1_16_221() })
}
