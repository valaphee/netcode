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
import com.valaphee.netcode.mcje.network.Packet.Reader

/**
 * @author Kevin Ludwig
 */
class ClientResourcePackStatusPacket(
    val status: Status
) : Packet<ClientPlayPacketHandler>() {
    enum class Status {
        Succeed, Declined, Failed, Accepted
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(status.ordinal)
    }

    override fun handle(handler: ClientPlayPacketHandler) = handler.resourcePackStatus(this)

    override fun toString() = "ClientResourcePackStatusPacket(status=$status)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = ClientResourcePackStatusPacket(Status.values()[buffer.readVarInt()])
    }
}
