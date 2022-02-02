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
import com.valaphee.netcode.mcje.network.PacketReader
import com.valaphee.netcode.mcje.item.stack.Stack
import com.valaphee.netcode.mcje.item.stack.readStack
import com.valaphee.netcode.mcje.item.stack.writeStack
import com.valaphee.netcode.mcje.network.ServerPlayPacketHandler

/**
 * @author Kevin Ludwig
 */
class ServerEntityEquipmentPacket(
    val entityId: Int,
    val equipments: List<Equipment>
) : Packet<ServerPlayPacketHandler> {
    class Equipment(
        var slot: Slot,
        var stack: Stack?
    ) {
        enum class Slot {
            MainHand, OffHand, Boots, Leggings, Chestplate, Helmet
        }
    }

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

    override fun toString() = "ServerEntityEquipmentPacket(entityId=$entityId, equipments=$equipments)"
}

/**
 * @author Kevin Ludwig
 */
object ServerEntityEquipmentPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerEntityEquipmentPacket(buffer.readVarInt(), if (version >= 754) ArrayList<ServerEntityEquipmentPacket.Equipment>().apply {
        var slot: Int
        do {
            slot = buffer.readByte().toInt()
            add(ServerEntityEquipmentPacket.Equipment(ServerEntityEquipmentPacket.Equipment.Slot.values()[slot and 0b0111_1111], buffer.readStack()))
        } while (slot and 0b1000_0000 != 0)
    } else listOf(ServerEntityEquipmentPacket.Equipment(ServerEntityEquipmentPacket.Equipment.Slot.values()[if (version >= 498) buffer.readVarInt() else buffer.readUnsignedShort()], buffer.readStack()))
    )
}
