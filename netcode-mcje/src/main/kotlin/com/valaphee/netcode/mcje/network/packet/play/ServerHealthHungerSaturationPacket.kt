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

import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.Packet.Reader
import com.valaphee.netcode.mcje.network.ServerPlayPacketHandler

/**
 * @author Kevin Ludwig
 */
class ServerHealthHungerSaturationPacket(
    val health: Float,
    val hunger: Int,
    val saturation: Float
) : Packet<ServerPlayPacketHandler>() {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeFloat(health)
        buffer.writeVarInt(hunger)
        buffer.writeFloat(saturation)
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.healthHungerSaturation(this)

    override fun toString() = "ServerHealthHungerSaturationPacket(health=$health, hunger=$hunger, saturation=$saturation)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = ServerHealthHungerSaturationPacket(buffer.readFloat(), buffer.readVarInt(), buffer.readFloat())
    }
}
