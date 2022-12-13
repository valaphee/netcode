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
import com.valaphee.netcode.mcje.network.V1_19_0

/**
 * @author Kevin Ludwig
 */
class ClientBeaconUpdatePacket(
    val primaryEffectId: Int?,
    val secondaryEffectId: Int?
) : Packet<ClientPlayPacketHandler>() {
    override fun write(buffer: PacketBuffer, version: Int) {
        if (version >= V1_19_0) {
            primaryEffectId?.let {
                buffer.writeBoolean(true)
                buffer.writeVarInt(it)
            } ?: buffer.writeBoolean(false)
            secondaryEffectId?.let {
                buffer.writeBoolean(true)
                buffer.writeVarInt(it)
            } ?: buffer.writeBoolean(false)
        } else {
            buffer.writeVarInt(primaryEffectId!!)
            buffer.writeVarInt(secondaryEffectId!!)
        }
    }

    override fun handle(handler: ClientPlayPacketHandler) = handler.beaconUpdate(this)

    override fun toString() = "ClientBeaconUpdatePacket(primaryEffectId=$primaryEffectId, secondaryEffectId=$secondaryEffectId)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = ClientBeaconUpdatePacket(if (version < V1_19_0 || buffer.readBoolean()) buffer.readVarInt() else null, if (version < V1_19_0 || buffer.readBoolean()) buffer.readVarInt() else null)
    }
}
