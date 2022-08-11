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
import com.valaphee.netcode.mcbe.world.GameRule
import com.valaphee.netcode.mcbe.world.readGameRule
import com.valaphee.netcode.mcbe.world.writeGameRule
import com.valaphee.netcode.util.safeList

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class GameRulesPacket(
    val gameRules: List<GameRule<*>>
) : Packet() {
    override val id get() = 0x48

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarUInt(gameRules.size)
        gameRules.forEach { buffer.writeGameRule(it, version) }
    }

    override fun handle(handler: PacketHandler) = handler.gameRules(this)

    override fun toString() = "GameRulesPacket(gameRules=$gameRules)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = GameRulesPacket(safeList(buffer.readVarUInt()) { buffer.readGameRule(version) })
    }
}
