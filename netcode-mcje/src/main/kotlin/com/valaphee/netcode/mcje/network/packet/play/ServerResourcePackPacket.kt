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
import com.valaphee.netcode.mcje.network.PacketReader
import com.valaphee.netcode.mcje.network.ServerPlayPacketHandler
import com.valaphee.netcode.mcje.network.V1_18_2
import net.kyori.adventure.text.Component

/**
 * @author Kevin Ludwig
 */
class ServerResourcePackPacket(
    val url: String,
    val hash: String,
    val forced: Boolean,
    val message: Component?
) : Packet<ServerPlayPacketHandler>() {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeString(url)
        buffer.writeString(hash)
        if (version >= V1_18_2) {
            buffer.writeBoolean(forced)
            message?.let {
                buffer.writeBoolean(true)
                buffer.writeComponent(message)
            } ?: buffer.writeBoolean(false)
        }
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.resourcePack(this)

    override fun toString() = "ServerResourcePackPacket(url='$url', hash='$hash', forced=$forced, message=$message)"
}

/**
 * @author Kevin Ludwig
 */
object ServerResourcePackPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): ServerResourcePackPacket {
        val url = buffer.readString()
        val hash = buffer.readString(40)
        val forced: Boolean
        val message: Component?
        if (version >= V1_18_2) {
            forced = buffer.readBoolean()
            message = buffer.readComponent()
        } else {
            forced = false
            message = null
        }
        return ServerResourcePackPacket(url, hash, forced, message)
    }
}
