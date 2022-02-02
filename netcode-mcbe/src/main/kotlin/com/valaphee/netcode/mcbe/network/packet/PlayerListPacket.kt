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
import com.valaphee.netcode.mcbe.world.entity.player.User
import com.valaphee.netcode.mcbe.world.entity.player.Appearance
import com.valaphee.netcode.mcbe.world.entity.player.readAppearance
import com.valaphee.netcode.mcbe.world.entity.player.readAppearancePre390
import com.valaphee.netcode.mcbe.world.entity.player.readAppearancePre419
import com.valaphee.netcode.mcbe.world.entity.player.readAppearancePre428
import com.valaphee.netcode.mcbe.world.entity.player.readAppearancePre465
import com.valaphee.netcode.mcbe.world.entity.player.writeAppearance
import com.valaphee.netcode.mcbe.world.entity.player.writeAppearancePre390
import com.valaphee.netcode.mcbe.world.entity.player.writeAppearancePre419
import com.valaphee.netcode.mcbe.world.entity.player.writeAppearancePre428
import com.valaphee.netcode.mcbe.world.entity.player.writeAppearancePre465
import com.valaphee.netcode.util.safeList
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
                buffer.writeIntLE(it.operatingSystem!!.ordinal - 1)
                if (version >= 465) buffer.writeAppearance(it.appearance!!) else if (version >= 428) buffer.writeAppearancePre465(it.appearance!!) else if (version >= 419) buffer.writeAppearancePre428(it.appearance!!) else if (version >= 390) buffer.writeAppearancePre419(it.appearance!!) else buffer.writeAppearancePre390(it.appearance!!)
                buffer.writeBoolean(it.teacher)
                buffer.writeBoolean(it.host)
            }
        }
        if (version >= 390 && action == Action.Add) entries.forEach { buffer.writeBoolean(it.appearance!!.trusted) }
    }

    override fun handle(handler: PacketHandler) = handler.playerList(this)

    override fun toString() = "PlayerListPacket(action=$action, entries=$entries)"
}

/**
 * @author Kevin Ludwig
 */
object PlayerListPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): PlayerListPacket {
        val action = PlayerListPacket.Action.values()[buffer.readUnsignedByte().toInt()]
        val entries = safeList(buffer.readVarUInt()) {
            when (action) {
                PlayerListPacket.Action.Add -> PlayerListPacket.Entry(
                    buffer.readUuid(),
                    buffer.readVarLong(),
                    buffer.readString(),
                    buffer.readString(),
                    buffer.readString(),
                    User.OperatingSystem.values()[buffer.readIntLE() + 1],
                    if (version >= 465) buffer.readAppearance() else if (version >= 428) buffer.readAppearancePre465() else if (version >= 419) buffer.readAppearancePre428() else if (version >= 390) buffer.readAppearancePre419() else buffer.readAppearancePre390(),
                    buffer.readBoolean(),
                    buffer.readBoolean()
                )
                else -> PlayerListPacket.Entry(buffer.readUuid())
            }
        }.apply { if (version > 389 && action == PlayerListPacket.Action.Add) forEach { it.appearance!!.trusted = buffer.readBoolean() } }
        return PlayerListPacket(action, entries)
    }
}
