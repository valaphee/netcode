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
import com.valaphee.netcode.mcbe.Restrict
import com.valaphee.netcode.mcbe.Restriction

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class ObjectiveSetPacket(
    val displaySlot: String,
    val name: String,
    val displayName: String,
    val criteria: String,
    val sortOrder: SortOrder
) : Packet() {
    enum class SortOrder {
        Ascending, Descending
    }

    override val id get() = 0x6B

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeString(displaySlot)
        buffer.writeString(name)
        buffer.writeString(displayName)
        buffer.writeString(criteria)
        buffer.writeVarInt(sortOrder.ordinal)
    }

    override fun handle(handler: PacketHandler) = handler.objectiveSet(this)

    override fun toString() = "ObjectiveSetPacket(displaySlot='$displaySlot', name='$name', displayName='$displayName', criteria='$criteria', sortOrder=$sortOrder)"
}

/**
 * @author Kevin Ludwig
 */
object ObjectiveSetPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ObjectiveSetPacket(buffer.readString(), buffer.readString(), buffer.readString(), buffer.readString(), ObjectiveSetPacket.SortOrder.values()[buffer.readVarInt()])
}
