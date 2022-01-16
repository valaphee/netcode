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

import com.valaphee.netcode.mcbe.command.Permission
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.PacketReader
import com.valaphee.netcode.mcbe.world.WorldFlag
import com.valaphee.netcode.mcbe.world.entity.player.PlayerFlag
import com.valaphee.netcode.mcbe.world.entity.player.Rank

/**
 * @author Kevin Ludwig
 */
class AdventureSettingsPacket(
    var uniqueEntityId: Long,
    var playerFlags: Set<PlayerFlag>,
    var permission: Permission,
    var worldFlags: Set<WorldFlag>,
    var rank: Rank,
    var customFlags: Int
) : Packet() {
    override val id get() = 0x37

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarUIntFlags(playerFlags)
        buffer.writeVarUInt(permission.ordinal)
        buffer.writeVarUIntFlags(worldFlags)
        buffer.writeVarUInt(rank.ordinal)
        buffer.writeVarUInt(customFlags)
        buffer.writeLongLE(uniqueEntityId)
    }

    override fun handle(handler: PacketHandler) = handler.adventureSettings(this)

    override fun toString() = "AdventureSettingsPacket(uniqueEntityId=$uniqueEntityId, playerFlags=$playerFlags, permission=$permission, worldFlags=$worldFlags, rank=$rank, customFlags=$customFlags)"
}

/**
 * @author Kevin Ludwig
 */
object AdventureSettingsPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): AdventureSettingsPacket {
        val playerFlags = buffer.readVarUIntFlags<PlayerFlag>()
        val permission = Permission.values()[buffer.readVarUInt()]
        val worldFlags = buffer.readVarUIntFlags<WorldFlag>()
        val rank = Rank.values()[buffer.readVarUInt()]
        val customFlags = buffer.readVarUInt()
        val uniqueEntityId = buffer.readLongLE()
        return AdventureSettingsPacket(uniqueEntityId, playerFlags, permission, worldFlags, rank, customFlags)
    }
}
