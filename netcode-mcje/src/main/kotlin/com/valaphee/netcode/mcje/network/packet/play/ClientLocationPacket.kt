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

import com.valaphee.foundry.math.Double3
import com.valaphee.foundry.math.Float2
import com.valaphee.netcode.mcje.network.ClientPlayPacketHandler
import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer

/**
 * @author Kevin Ludwig
 */
class ClientLocationPacket(
    val position: Double3?,
    val rotation: Float2?,
    val onGround: Boolean
) : Packet<ClientPlayPacketHandler>() {
    override val reader get() = if (position != null) if (rotation != null) PositionRotationReader else PositionReader else if (rotation != null) RotationReader else LocationReader

    override fun write(buffer: PacketBuffer, version: Int) {
        position?.let { buffer.writeDouble3(position) }
        rotation?.let { buffer.writeFloat2(rotation) }
        buffer.writeBoolean(onGround)
    }

    override fun handle(handler: ClientPlayPacketHandler) = handler.location(this)

    override fun toString() = "ClientLocationPacket(position=$position, rotation=$rotation, onGround=$onGround)"

    object PositionReader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = ClientLocationPacket(buffer.readDouble3(), null, buffer.readBoolean())
    }

    object PositionRotationReader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = ClientLocationPacket(buffer.readDouble3(), buffer.readFloat2(), buffer.readBoolean())
    }

    object RotationReader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = ClientLocationPacket(null, buffer.readFloat2(), buffer.readBoolean())
    }

    object LocationReader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = ClientLocationPacket(null, null, buffer.readBoolean())
    }
}
