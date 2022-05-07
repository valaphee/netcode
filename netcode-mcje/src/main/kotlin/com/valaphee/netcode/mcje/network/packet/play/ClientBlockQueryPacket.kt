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

import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mcje.network.ClientPlayPacketHandler
import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.PacketReader

/**
 * @author Kevin Ludwig
 */
class ClientBlockQueryPacket(
    val id: Int,
    val position: Int3,
) : Packet<ClientPlayPacketHandler>() {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(id)
        buffer.writeInt3UnsignedY(position)
    }

    override fun handle(handler: ClientPlayPacketHandler) = handler.blockQuery(this)

    override fun toString() = "ClientBlockQueryPacket(id=$id, position=$position)"
}

/**
 * @author Kevin Ludwig
 */
object ClientBlockQueryPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ClientBlockQueryPacket(buffer.readVarInt(), buffer.readInt3UnsignedY())
}
