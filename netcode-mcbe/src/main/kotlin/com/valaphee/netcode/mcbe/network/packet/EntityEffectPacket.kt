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
import com.valaphee.netcode.mcbe.network.Restrict
import com.valaphee.netcode.mcbe.network.Restriction
import com.valaphee.netcode.mcbe.world.Effect

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class EntityEffectPacket(
    val runtimeEntityId: Long,
    val action: Action,
    val effect: Effect,
    val amplifier: Int,
    val showParticle: Boolean,
    val duration: Int
) : Packet() {
    enum class Action {
        Apply, Modify, Revoke
    }

    override val id get() = 0x1C

    constructor(runtimeEntityId: Long, action: Action, effect: Effect) : this(runtimeEntityId, action, effect, 0, false, 0)

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarULong(runtimeEntityId)
        buffer.writeByte(action.ordinal + 1)
        buffer.writeVarInt(effect.ordinal + 1)
        buffer.writeVarInt(amplifier)
        buffer.writeBoolean(showParticle)
        buffer.writeVarInt(duration)
    }

    override fun handle(handler: PacketHandler) = handler.entityEffect(this)

    override fun toString() = "EntityEffectPacket(runtimeEntityId=$runtimeEntityId, action=$action, effect=$effect, amplifier=$amplifier, showParticle=$showParticle, duration=$duration)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = EntityEffectPacket(
            buffer.readVarULong(),
            EntityEffectPacket.Action.values()[buffer.readByte().toInt() - 1],
            Effect.values()[buffer.readVarInt() - 1],
            buffer.readVarInt(),
            buffer.readBoolean(),
            buffer.readVarInt()
        )
    }
}
