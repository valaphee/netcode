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
    val slotId: Int,
    val buttonSpecifier: Int,
    var confirmId: Int, // needed for je-be protocol translation
    val button: Button,
    val itemStackInSlot: ItemStack? = null
) : Packet<ClientPlayPacketHandler> {
    enum class Button {
        Normal, Shift, Number, MiddleClick, QOrNoOp, Paint, DoubleClick
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeByte(windowId)
        buffer.writeShort(slotId)
        buffer.writeByte(buttonSpecifier)
        buffer.writeShort(confirmId)
        buffer.writeVarInt(button.ordinal)
        buffer.writeItemStack(itemStackInSlot)
    }

    override fun handle(handler: ClientPlayPacketHandler) = handler.windowClick(this)

    override fun toString() = "ClientWindowClickPacket(windowId=$windowId, slotId=$slotId, buttonSpecifier=$buttonSpecifier, confirmId=$confirmId, button=$button, stackInSlot=$itemStackInSlot)"

    companion object {
        const val SlotIdNone = -999
    }
}

/**
 * @author Kevin Ludwig
 */
object ClientWindowClickPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ClientWindowClickPacket(buffer.readByte().toInt(), buffer.readShort().toInt(), buffer.readByte().toInt(), buffer.readShort().toInt(), ClientWindowClickPacket.Button.values()[buffer.readVarInt()], buffer.readItemStack())
}
