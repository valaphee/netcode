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

package com.valaphee.netcode.mcbe.network.packet

import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.util.LazyList

/**
 * @author Kevin Ludwig
 */
class DeathPacket(
    val cause: String,
    val messages: List<String>
) : Packet() {
    override val id get() = 0xBD

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeString(cause)
        buffer.writeVarUInt(messages.size)
        messages.forEach { buffer.writeString(it) }
    }

    override fun handle(handler: PacketHandler) = handler.death(this)

    override fun toString() = "DeathPacket(cause='$cause', messages=$messages)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = DeathPacket(buffer.readString(), LazyList(buffer.readVarUInt()) { buffer.readString() })
    }
}
