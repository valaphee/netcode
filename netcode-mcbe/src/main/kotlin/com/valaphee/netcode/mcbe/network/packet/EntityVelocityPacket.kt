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
class EntityVelocityPacket(
    val runtimeEntityId: Long,
    val velocity: Float3
) : Packet() {
    override val id get() = 0x28

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarULong(runtimeEntityId)
        buffer.writeFloat3(velocity)
    }

    override fun handle(handler: PacketHandler) = handler.entityVelocity(this)

    override fun toString() = "EntityVelocityPacket(runtimeEntityId=$runtimeEntityId, velocity=$velocity)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = EntityVelocityPacket(buffer.readVarULong(), buffer.readFloat3())
    }
}
