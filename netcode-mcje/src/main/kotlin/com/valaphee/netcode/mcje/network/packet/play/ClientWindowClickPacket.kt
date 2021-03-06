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

import com.valaphee.netcode.mcje.network.ClientPlayPacketHandler
import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.PacketReader
import com.valaphee.netcode.mcje.world.item.ItemStack
import com.valaphee.netcode.mcje.world.item.readItemStack
import com.valaphee.netcode.mcje.world.item.writeItemStack

/**
 * @author Kevin Ludwig
 */
class ClientWindowClickPacket(
    val windowId: Int,
    var confirmOrStateId: Int, // needed for je-be protocol translation
    val slotId: Int,
    val buttonSpecifier: Int,
    val button: Button,
    val slots: Map<Int, ItemStack?>,
    val clickedSlot: ItemStack?
) : Packet<ClientPlayPacketHandler>() {
    enum class Button {
        Normal, Shift, Number, MiddleClick, QOrNoOp, Paint, DoubleClick
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeByte(windowId)
        if (version >= 758) buffer.writeVarInt(confirmOrStateId)
        buffer.writeShort(slotId)
        buffer.writeByte(buttonSpecifier)
        if (version < 758) buffer.writeShort(confirmOrStateId)
        buffer.writeVarInt(button.ordinal)
        if (version >= 758) {
            buffer.writeVarInt(slots.size)
            slots.forEach {
                buffer.writeShort(it.key)
                buffer.writeItemStack(it.value)
            }
        }
        buffer.writeItemStack(clickedSlot)
    }

    override fun handle(handler: ClientPlayPacketHandler) = handler.windowClick(this)

    override fun toString() = "ClientWindowClickPacket(windowId=$windowId, confirmOrStateId=$confirmOrStateId, slotId=$slotId, buttonSpecifier=$buttonSpecifier, button=$button, slots=$slots, clickedSlot=$clickedSlot)"
}

/**
 * @author Kevin Ludwig
 */
object ClientWindowClickPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): ClientWindowClickPacket {
        val windowId = buffer.readByte().toInt()
        val confirmOrStateId: Int
        val slotId: Int
        val buttonSpecifier: Int
        if (version >= 758) {
            confirmOrStateId = buffer.readVarInt()
            slotId = buffer.readShort().toInt()
            buttonSpecifier = buffer.readByte().toInt()
        } else {
            slotId = buffer.readShort().toInt()
            buttonSpecifier = buffer.readByte().toInt()
            confirmOrStateId = buffer.readShort().toInt()
        }
        val button = ClientWindowClickPacket.Button.values()[buffer.readVarInt()]
        val slots = if (version >= 758) mutableMapOf<Int, ItemStack?>().apply { repeat(buffer.readVarInt()) { this[buffer.readShort().toInt()] = buffer.readItemStack() } } else emptyMap()
        val clickedSlot = buffer.readItemStack()
        return ClientWindowClickPacket(windowId, confirmOrStateId, slotId, buttonSpecifier, button, slots, clickedSlot)
    }
}
