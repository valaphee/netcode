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
import com.valaphee.netcode.mcbe.network.Restrict
import com.valaphee.netcode.mcbe.network.Restriction
import com.valaphee.netcode.mcbe.network.V1_14_060
import com.valaphee.netcode.mcbe.world.entity.player.Appearance
import com.valaphee.netcode.mcbe.world.entity.player.User
import com.valaphee.netcode.mcbe.world.entity.player.readAppearance
import com.valaphee.netcode.mcbe.world.entity.player.writeAppearance
import com.valaphee.netcode.util.LazyList
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class PlayerListPacket(
    val action: Action,
    val entries: List<Entry>
) : Packet() {
    enum class Action {
        Add, Remove
    }

    data class Entry(
        val userId: UUID,
        val uniqueEntityId: Long,
        val userName: String?,
        val xboxUserId: String?,
        val platformChatId: String?,
        val operatingSystem: User.OperatingSystem?,
        val appearance: Appearance?,
        val teacher: Boolean,
        val host: Boolean
    ) {
        constructor(userId: UUID) : this(userId, 0, null, null, null, null, null, false, false)

        override fun toString() = "Entry(userId=$userId, uniqueEntityId=$uniqueEntityId, userName=$userName, xboxUserId=$xboxUserId, platformChatId=$platformChatId, operatingSystem=$operatingSystem, appearance=$appearance, teacher=$teacher, host=$host)"
    }

    override val id get() = 0x3F

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeByte(action.ordinal)
        buffer.writeVarUInt(entries.size)
        entries.forEach {
            buffer.writeUuid(it.userId)
            if (action == Action.Add) {
                buffer.writeVarLong(it.uniqueEntityId)
                buffer.writeString(it.userName!!)
                buffer.writeString(it.xboxUserId!!)
                buffer.writeString(it.platformChatId!!)
                buffer.writeIntLE(if (it.operatingSystem == User.OperatingSystem.Unknown) -1 else it.operatingSystem!!.ordinal)
                buffer.writeAppearance(it.appearance!!, version)
                buffer.writeBoolean(it.teacher)
                buffer.writeBoolean(it.host)
            }
        }
        if (version >= V1_14_060 && action == Action.Add) entries.forEach { buffer.writeBoolean(it.appearance!!.trusted) }
    }

    override fun handle(handler: PacketHandler) = handler.playerList(this)

    override fun toString() = "PlayerListPacket(action=$action, entries=$entries)"


    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int): PlayerListPacket {
            val action = Action.values()[buffer.readUnsignedByte().toInt()]
            val entries = LazyList(buffer.readVarUInt()) {
                when (action) {
                    Action.Add -> Entry(
                        buffer.readUuid(),
                        buffer.readVarLong(),
                        buffer.readString(),
                        buffer.readString(),
                        buffer.readString(),
                        User.OperatingSystem.values().getOrElse(buffer.readIntLE()) { User.OperatingSystem.Unknown },
                        buffer.readAppearance(version),
                        buffer.readBoolean(),
                        buffer.readBoolean()
                    )
                    else -> Entry(buffer.readUuid())
                }
            }.apply { if (version >= V1_14_060 && action == Action.Add) forEach { it.appearance!!.trusted = buffer.readBoolean() } }
            return PlayerListPacket(action, entries)
        }
    }
}
