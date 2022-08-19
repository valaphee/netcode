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
import com.valaphee.netcode.mcje.network.ServerPlayPacketHandler
import net.kyori.adventure.text.Component

/**
 * @author Kevin Ludwig
 */
class ServerServerDataPacket(
    val description: Component?,
    val favicon: String?,
    val playerChatPreview: Boolean,
    val enforceSecureChat: Boolean
) : Packet<ServerPlayPacketHandler>() {
    override fun write(buffer: PacketBuffer, version: Int) {
        description?.let {
            buffer.writeBoolean(true)
            buffer.writeComponent(it)
        } ?: buffer.writeBoolean(false)
        favicon?.let {
            buffer.writeBoolean(true)
            buffer.writeString(it)
        } ?: buffer.writeBoolean(false)
        buffer.writeBoolean(playerChatPreview)
        buffer.writeBoolean(enforceSecureChat)
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.serverData(this)

    override fun toString() = "ServerServerDataPacket(description=$description, favicon=$favicon, playerChatPreview=$playerChatPreview, enforceSecureChat=$enforceSecureChat)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = ServerServerDataPacket(if (buffer.readBoolean()) buffer.readComponent() else null, if (buffer.readBoolean()) buffer.readString() else null, buffer.readBoolean(), buffer.readBoolean())
    }
}
