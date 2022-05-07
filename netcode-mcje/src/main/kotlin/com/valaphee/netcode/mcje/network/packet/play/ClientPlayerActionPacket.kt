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

package com.valaphee.netcode.mcje.network.packet.play

import com.valaphee.netcode.mcje.network.ClientPlayPacketHandler
import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.PacketReader

/**
 * @author Kevin Ludwig
 */
class ClientPlayerActionPacket(
    val entityId: Int,
    val action: Action,
    val jumpStrength: Float = 0.0f
) : Packet<ClientPlayPacketHandler>() {
    enum class Action {
        StartSneak,
        StopSneak,
        StopSleep,
        StartSprinting,
        StopSprinting,
        StartJumpWithHorse,
        StopJumpWithHorse,
        OpenHorseInventory,
        ToggleGlide
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(entityId)
        buffer.writeVarInt(action.ordinal)
        buffer.writeVarInt((jumpStrength * 100.0f).toInt())
    }

    override fun handle(handler: ClientPlayPacketHandler) = handler.playerAction(this)

    override fun toString() = "ClientPlayerActionPacket(entityId=$entityId, action=$action, jumpStrength=$jumpStrength)"
}

/**
 * @author Kevin Ludwig
 */
object ClientPlayerActionPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ClientPlayerActionPacket(buffer.readVarInt(), ClientPlayerActionPacket.Action.values()[buffer.readVarInt()], buffer.readVarInt() / 100.0f)
}
