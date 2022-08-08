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

/**
 * @author Kevin Ludwig
 */
class EntityArmorPacket(
    val runtimeEntityId: Long,
    val head: ItemStack?,
    val torso: ItemStack?,
    val legs: ItemStack?,
    val feet: ItemStack?
) : Packet() {
    override val id get() = 0x20

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarULong(runtimeEntityId)
        if (version >= V1_16_221) {
            buffer.writeItemStack(head)
            buffer.writeItemStack(torso)
            buffer.writeItemStack(legs)
            buffer.writeItemStack(feet)
        } else {
            buffer.writeItemStackPreV1_16_221(head)
            buffer.writeItemStackPreV1_16_221(torso)
            buffer.writeItemStackPreV1_16_221(legs)
            buffer.writeItemStackPreV1_16_221(feet)
        }
    }

    override fun handle(handler: PacketHandler) = handler.entityArmor(this)

    override fun toString() = "EntityArmorPacket(runtimeEntityId=$runtimeEntityId, head=$head, torso=$torso, legs=$legs, feet=$feet)"
}

/**
 * @author Kevin Ludwig
 */
object EntityArmorPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): EntityArmorPacket {
        val runtimeEntityId = buffer.readVarULong()
        val head: ItemStack?
        val torso: ItemStack?
        val legs: ItemStack?
        val feet: ItemStack?
        if (version >= V1_16_221) {
            head = buffer.readItemStack()
            torso = buffer.readItemStack()
            legs = buffer.readItemStack()
            feet = buffer.readItemStack()
        } else {
            head = buffer.readItemStackPreV1_16_221()
            torso = buffer.readItemStackPreV1_16_221()
            legs = buffer.readItemStackPreV1_16_221()
            feet = buffer.readItemStackPreV1_16_221()
        }
        return EntityArmorPacket(runtimeEntityId, head, torso, legs, feet)
    }
}
