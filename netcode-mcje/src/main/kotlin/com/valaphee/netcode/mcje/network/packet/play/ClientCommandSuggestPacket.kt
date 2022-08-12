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
class ClientCommandSuggestPacket(
    val suggestId: Int,
    val command: String
) : Packet<ClientPlayPacketHandler>() {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(suggestId)
        buffer.writeString(command)
    }

    override fun handle(handler: ClientPlayPacketHandler) = handler.commandSuggest(this)

    override fun toString() = "ClientCommandSuggestPacket(suggestId=$suggestId, command='$command')"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = ClientCommandSuggestPacket(buffer.readVarInt(), buffer.readString(32500))
    }
}
