/*
 * MIT License
 *
 * Copyright (c) 2021, Valaphee.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.valaphee.netcode.mcje.play

import com.valaphee.netcode.mcje.Packet
import com.valaphee.netcode.mcje.PacketBuffer
import com.valaphee.netcode.mcje.ServerPlayPacketHandler
import com.valaphee.netcode.mcje.item.stack.Stack
import com.valaphee.netcode.mcje.item.stack.writeStack

/**
 * @author Kevin Ludwig
 */
class ServerEntityEquipmentPacket(
    val entityId: Int = 0,
    val equipments: List<Equipment>
) : Packet<ServerPlayPacketHandler> {
    enum class Slot {
        MainHand, OffHand, Boots, Leggings, Chestplate, Helmet
    }

    class Equipment(
        var slot: Slot,
        var stack: Stack?
    )

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(entityId)
        if (version >= 754) {
            equipments.forEachIndexed { i, equipment ->
                buffer.writeByte(equipment.slot.ordinal or (if (i == equipments.size - 1) 0 else 0b1000_0000))
                buffer.writeStack(equipment.stack)
            }
        } else {
            val equipment = equipments.first()
            if (version >= 498) buffer.writeVarInt(equipment.slot.ordinal) else buffer.writeShort(equipment.slot.ordinal)
            buffer.writeStack(equipment.stack)
        }
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.entityEquipment(this)
}
