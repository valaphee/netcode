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
class ClientSignUpdatePacket(
    val position: Int3,
    val text0: String,
    val text1: String,
    val text2: String,
    val text3: String
) : Packet<ClientPlayPacketHandler> {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeInt3UnsignedY(position)
        buffer.writeString(text0)
        buffer.writeString(text1)
        buffer.writeString(text2)
        buffer.writeString(text3)
    }

    override fun handle(handler: ClientPlayPacketHandler) = handler.signUpdate(this)

    override fun toString() = "ClientSignUpdatePacket(position=$position, text0=$text0, text1=$text1, text2=$text2, text3=$text3)"
}

/**
 * @author Kevin Ludwig
 */
object ClientSignUpdatePacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ClientSignUpdatePacket(buffer.readInt3UnsignedY(), buffer.readString(384), buffer.readString(384), buffer.readString(384), buffer.readString(384))
}
