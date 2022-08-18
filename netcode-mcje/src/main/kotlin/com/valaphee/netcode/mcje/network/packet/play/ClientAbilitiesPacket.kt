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
import com.valaphee.netcode.mcje.network.V1_16_0

/**
 * @author Kevin Ludwig
 */
class ClientAbilitiesPacket(
    val flags: Set<Flag>,
    val flySpeed: Float,
    val walkSpeed: Float
) : Packet<ClientPlayPacketHandler>() {
    enum class Flag {
        Invulnerable, Flying, AllowFlight, CreativeMode
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeByteFlags(flags)
        if (version < V1_16_0) {
            buffer.writeFloat(flySpeed)
            buffer.writeFloat(walkSpeed)
        }
    }

    override fun handle(handler: ClientPlayPacketHandler) = handler.abilities(this)

    override fun toString() = "ClientAbilitiesPacket(flags=$flags, flySpeed=$flySpeed, walkSpeed=$walkSpeed)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int): ClientAbilitiesPacket {
            val flags = buffer.readByteFlags<Flag>()
            val flySpeed: Float
            val walkSpeed: Float
            if (version >= V1_16_0) {
                flySpeed = 0.05f
                walkSpeed = 0.1f
            } else {
                flySpeed = buffer.readFloat()
                walkSpeed = buffer.readFloat()
            }
            return ClientAbilitiesPacket(flags, flySpeed, walkSpeed)
        }
    }
}
