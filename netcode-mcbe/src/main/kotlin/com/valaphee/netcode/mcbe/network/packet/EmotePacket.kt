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

/**
 * @author Kevin Ludwig
 */
class EmotePacket(
    val runtimeEntityId: Long,
    val emoteId: String,
    val fromServer: Boolean
) : Packet() {

    override val id get() = 0x8A

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarULong(runtimeEntityId)
        buffer.writeString(emoteId)
        buffer.writeBoolean(fromServer)
    }

    override fun handle(handler: PacketHandler) = handler.emote(this)

    override fun toString() = "EmotePacket(runtimeEntityId=$runtimeEntityId, emoteId='$emoteId', fromServer=$fromServer)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = EmotePacket(buffer.readVarULong(), buffer.readString(), buffer.readBoolean())
    }
}
