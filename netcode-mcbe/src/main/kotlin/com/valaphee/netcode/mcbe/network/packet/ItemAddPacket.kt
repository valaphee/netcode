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

import com.valaphee.foundry.math.Float3
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.Restrict
import com.valaphee.netcode.mcbe.network.Restriction
import com.valaphee.netcode.mcbe.world.entity.metadata.Metadata
import com.valaphee.netcode.mcbe.world.item.ItemStack
import com.valaphee.netcode.mcbe.world.item.readItemStack
import com.valaphee.netcode.mcbe.world.item.writeItemStack

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class ItemAddPacket(
    val uniqueEntityId: Long,
    val runtimeEntityId: Long,
    val itemStack: ItemStack?,
    val position: Float3,
    val velocity: Float3,
    val metadata: Metadata,
    val fromFishing: Boolean
) : Packet() {
    override val id get() = 0x0F

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarLong(uniqueEntityId)
        buffer.writeVarULong(runtimeEntityId)
        buffer.writeItemStack(itemStack, version)
        buffer.writeFloat3(position)
        buffer.writeFloat3(velocity)
        metadata.writeToBuffer(buffer)
        buffer.writeBoolean(fromFishing)
    }

    override fun handle(handler: PacketHandler) = handler.itemAdd(this)

    override fun toString() = "ItemAddPacket(uniqueEntityId=$uniqueEntityId, runtimeEntityId=$runtimeEntityId, itemStack=$itemStack, position=$position, velocity=$velocity, metadata=$metadata, fromFishing=$fromFishing)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = ItemAddPacket(buffer.readVarLong(), buffer.readVarULong(), buffer.readItemStack(version), buffer.readFloat3(), buffer.readFloat3(), Metadata().apply { readFromBuffer(buffer) }, buffer.readBoolean())
    }
}
