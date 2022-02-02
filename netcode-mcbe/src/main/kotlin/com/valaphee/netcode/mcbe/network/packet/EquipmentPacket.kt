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

import com.valaphee.netcode.mc.util.nbt.Tag
import com.valaphee.netcode.mcbe.Restrict
import com.valaphee.netcode.mcbe.Restriction
import com.valaphee.netcode.mcbe.world.inventory.WindowType
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.PacketReader

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class EquipmentPacket(
    val windowId: Int,
    val type: WindowType,
    val slotCount: Int,
    val uniqueEntityId: Long,
    val tag: Tag?
) : Packet() {
    override val id get() = 0x51

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeByte(windowId)
        buffer.writeByte(type.id)
        buffer.writeVarInt(slotCount)
        buffer.writeVarLong(uniqueEntityId)
        buffer.toNbtOutputStream().use { it.writeTag(tag) }
    }

    override fun handle(handler: PacketHandler) = handler.equipment(this)

    override fun toString() = "EquipmentPacket(windowId=$windowId, type=$type, slotCount=$slotCount, uniqueEntityId=$uniqueEntityId, tag=$tag)"
}

/**
 * @author Kevin Ludwig
 */
object EquipmentPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = EquipmentPacket(buffer.readByte().toInt(), WindowType.byId(buffer.readByte().toInt()), buffer.readVarInt(), buffer.readVarLong(), buffer.toNbtInputStream().use { it.readTag() })
}
