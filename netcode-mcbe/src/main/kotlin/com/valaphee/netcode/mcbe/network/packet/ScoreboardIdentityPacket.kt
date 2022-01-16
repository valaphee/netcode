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
import com.valaphee.netcode.mcbe.network.Restrict
import com.valaphee.netcode.mcbe.network.Restriction
import com.valaphee.netcode.mcbe.util.safeList
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class ScoreboardIdentityPacket(
    val action: Action,
    val entries: List<Entry>
) : Packet() {
    enum class Action {
        Add, Remove
    }

    data class Entry(
        var scoreboardId: Long,
        var userId: UUID?
    )

    override val id get() = 0x70

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeByte(action.ordinal)
        buffer.writeVarUInt(entries.size)
        entries.forEach {
            buffer.writeVarLong(it.scoreboardId)
            if (action == Action.Add) buffer.writeUuid(it.userId!!)
        }
    }

    override fun handle(handler: PacketHandler) = handler.scoreboardIdentity(this)

    override fun toString() = "ScoreboardIdentityPacket(action=$action, entries=$entries)"
}

/**
 * @author Kevin Ludwig
 */
object ScoreboardIdentityPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): ScoreboardIdentityPacket {
        val action = ScoreboardIdentityPacket.Action.values()[buffer.readUnsignedByte().toInt()]
        val entries = safeList(buffer.readVarUInt()) { ScoreboardIdentityPacket.Entry(buffer.readVarLong(), if (action == ScoreboardIdentityPacket.Action.Add) buffer.readUuid() else null) }
        return ScoreboardIdentityPacket(action, entries)
    }
}
