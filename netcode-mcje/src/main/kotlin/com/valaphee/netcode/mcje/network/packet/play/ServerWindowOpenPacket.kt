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

import com.fasterxml.jackson.module.kotlin.readValue
import com.valaphee.netcode.mcje.chat.Component
import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.PacketReader
import com.valaphee.netcode.mcje.network.ServerPlayPacketHandler
import com.valaphee.netcode.mcje.util.NamespacedKey
import com.valaphee.netcode.util.ByteBufStringReader

/**
 * @author Kevin Ludwig
 */
class ServerWindowOpenPacket(
    val windowId: Int,
    val windowTypeKey: NamespacedKey,
    val title: Component
) : Packet<ServerPlayPacketHandler> {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(windowId)
        buffer.writeVarInt(buffer.registrySet.windowTypes.getId(windowTypeKey))
        buffer.writeString(buffer.objectMapper.writeValueAsString(title))
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.windowOpen(this)

    override fun toString() = "ServerWindowOpenPacket(windowId=$windowId, windowTypeKey=$windowTypeKey, title=$title)"
}

/**
 * @author Kevin Ludwig
 */
object ServerWindowOpenPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerWindowOpenPacket(buffer.readVarInt(), checkNotNull(buffer.registrySet.windowTypes[buffer.readVarInt()]), buffer.objectMapper.readValue(ByteBufStringReader(buffer, buffer.readVarInt())))
}
