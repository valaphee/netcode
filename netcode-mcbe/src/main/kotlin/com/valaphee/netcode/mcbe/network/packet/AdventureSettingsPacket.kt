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

import com.valaphee.netcode.mcbe.command.CommandPermission
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.world.WorldFlag
import com.valaphee.netcode.mcbe.world.entity.player.PlayerFlag
import com.valaphee.netcode.mcbe.world.entity.player.PlayerPermission

/**
 * @author Kevin Ludwig
 */
class AdventureSettingsPacket(
    val playerFlags: Set<PlayerFlag>,
    val commandPermission: CommandPermission,
    val worldFlags: Set<WorldFlag>,
    val playerPermission: PlayerPermission,
    val customFlags: Int,
    val uniqueEntityId: Long
) : Packet() {
    override val id get() = 0x37

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarUIntFlags(playerFlags)
        buffer.writeVarUInt(commandPermission.ordinal)
        buffer.writeVarUIntFlags(worldFlags)
        buffer.writeVarUInt(playerPermission.ordinal)
        buffer.writeVarUInt(customFlags)
        buffer.writeLongLE(uniqueEntityId)
    }

    override fun handle(handler: PacketHandler) = handler.adventureSettings(this)

    override fun toString() = "AdventureSettingsPacket(playerFlags=$playerFlags, commandPermission=$commandPermission, worldFlags=$worldFlags, playerPermission$playerPermission, customFlags=$customFlags, uniqueEntityId=$uniqueEntityId)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = AdventureSettingsPacket(buffer.readVarUIntFlags(), CommandPermission.values()[buffer.readVarUInt()], buffer.readVarUIntFlags(), PlayerPermission.values()[buffer.readVarUInt()], buffer.readVarUInt(), buffer.readLongLE())
    }
}
