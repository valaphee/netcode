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

import com.valaphee.netcode.mcbe.Restrict
import com.valaphee.netcode.mcbe.Restriction
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.PacketReader
import com.valaphee.netcode.mcbe.world.item.Enchantment
import com.valaphee.netcode.util.safeList

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class EnchantOptionsPacket(
    val options: List<Option>
) : Packet() {
    class Slot(
        val enchantment: Enchantment,
        val level: Short
    )

    class Option(
        val cost: Int,
        val primarySlotId: Int,
        val slots1: List<Slot>,
        val slots2: List<Slot>,
        val slots3: List<Slot>,
        val description: String,
        val netId: Int
    )

    override val id get() = 0x92

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarUInt(options.size)
        options.forEach {
            buffer.writeVarUInt(it.cost)
            buffer.writeIntLE(it.primarySlotId)
            buffer.writeVarUInt(it.slots1.size)
            it.slots1.forEach {
                buffer.writeByte(it.enchantment.ordinal)
                buffer.writeByte(it.level.toInt())
            }
            buffer.writeVarUInt(it.slots2.size)
            it.slots2.forEach {
                buffer.writeByte(it.enchantment.ordinal)
                buffer.writeByte(it.level.toInt())
            }
            buffer.writeVarUInt(it.slots3.size)
            it.slots3.forEach {
                buffer.writeByte(it.enchantment.ordinal)
                buffer.writeByte(it.level.toInt())
            }
            buffer.writeString(it.description)
            buffer.writeVarUInt(it.netId)
        }
    }

    override fun handle(handler: PacketHandler) = handler.enchantOptions(this)

    override fun toString() = "EnchantOptionsPacket(options=$options)"
}

/**
 * @author Kevin Ludwig
 */
object EnchantOptionsPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = EnchantOptionsPacket(safeList(buffer.readVarUInt()) {
        EnchantOptionsPacket.Option(
            buffer.readVarUInt(),
            buffer.readIntLE(),
            safeList(buffer.readVarUInt()) { EnchantOptionsPacket.Slot(Enchantment.values()[buffer.readUnsignedByte().toInt()], buffer.readUnsignedByte()) },
            safeList(buffer.readVarUInt()) { EnchantOptionsPacket.Slot(Enchantment.values()[buffer.readUnsignedByte().toInt()], buffer.readUnsignedByte()) },
            safeList(buffer.readVarUInt()) { EnchantOptionsPacket.Slot(Enchantment.values()[buffer.readUnsignedByte().toInt()], buffer.readUnsignedByte()) },
            buffer.readString(),
            buffer.readVarUInt()
        )
    })
}
