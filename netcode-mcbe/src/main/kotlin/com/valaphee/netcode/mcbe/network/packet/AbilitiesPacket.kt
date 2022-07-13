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
import com.valaphee.netcode.mcbe.world.entity.player.AbilityLayer
import com.valaphee.netcode.mcbe.world.entity.player.Rank
import com.valaphee.netcode.mcbe.world.entity.player.readAbilityLayer
import com.valaphee.netcode.mcbe.world.entity.player.writeAbilityLayer
import com.valaphee.netcode.util.safeList

/**
 * @author Kevin Ludwig
 */
class AbilitiesPacket(
    val uniqueEntityId: Long,
    val rank: Rank,
    val permission: Permission,
    val abilityLayers: List<AbilityLayer>
) : Packet() {
    enum class Type {
        None, Bool, Float
    }

    override val id get() = 0xBB

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeLongLE(uniqueEntityId)
        buffer.writeVarUInt(rank.ordinal)
        buffer.writeVarUInt(permission.ordinal)
        buffer.writeVarUInt(abilityLayers.size)
        abilityLayers.forEach { buffer.writeAbilityLayer(it) }
    }

    override fun handle(handler: PacketHandler) = handler.abilities(this)

    override fun toString() = "AbilitiesPacket(uniqueEntityId=$uniqueEntityId, rank=$rank, permission=$permission, abilityLayers=$abilityLayers)"
}

/**
 * @author Kevin Ludwig
 */
object AbilitiesPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = AbilitiesPacket(buffer.readLongLE(), Rank.values()[buffer.readVarUInt()], Permission.values()[buffer.readVarUInt()], safeList(buffer.readVarUInt()) { buffer.readAbilityLayer() })
}
