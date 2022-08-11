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

/**
 * @author Kevin Ludwig
 */
class AdventureSettingsWithoutAbilitiesPacket(
    val noPvM: Boolean,
    val noMvP: Boolean,
    val immutableWorld: Boolean,
    val showNameTags: Boolean,
    val autoJump: Boolean
) : Packet() {
    override val id get() = 0xBC

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeBoolean(noPvM)
        buffer.writeBoolean(noMvP)
        buffer.writeBoolean(immutableWorld)
        buffer.writeBoolean(showNameTags)
        buffer.writeBoolean(autoJump)
    }

    override fun handle(handler: PacketHandler) = handler.adventureSettingsWithoutAbilities(this)

    override fun toString() = "AdventureSettingsWithoutAbilitiesPacket(noPvM=$noPvM, noMvP=$noMvP, immutableWorld=$immutableWorld, showNameTags=$showNameTags, autoJump=$autoJump)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = AdventureSettingsWithoutAbilitiesPacket(buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean())
    }
}
