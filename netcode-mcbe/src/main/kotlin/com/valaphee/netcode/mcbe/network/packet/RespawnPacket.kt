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

import com.valaphee.foundry.math.Float3
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler

/**
 * @author Kevin Ludwig
 */
class RespawnPacket(
    val runtimeEntityId: Long,
    val state: State,
    val position: Float3
) : Packet() {
    enum class State {
        ServerSearching, ServerReady, ClientReady
    }

    override val id get() = 0x2D

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeFloat3(position)
        buffer.writeByte(state.ordinal)
        buffer.writeVarULong(runtimeEntityId)
    }

    override fun handle(handler: PacketHandler) = handler.respawn(this)

    override fun toString() = "RespawnPacket(runtimeEntityId=$runtimeEntityId, state=$state, position=$position)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int): RespawnPacket {
            val position = buffer.readFloat3()
            val state = State.values()[buffer.readByte().toInt()]
            val runtimeEntityId = buffer.readVarULong()
            return RespawnPacket(runtimeEntityId, state, position)
        }
    }
}
