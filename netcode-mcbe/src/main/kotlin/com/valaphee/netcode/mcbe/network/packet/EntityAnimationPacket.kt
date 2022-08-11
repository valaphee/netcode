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
import com.valaphee.netcode.mcbe.network.V1_14_060
import com.valaphee.netcode.util.Int2ObjectOpenHashBiMapVersioned

/**
 * @author Kevin Ludwig
 */
class EntityAnimationPacket(
    val animation: Animation,
    val runtimeEntityId: Long,
    val rowingTime: Float = 0.0f
) : Packet() {
    enum class Animation {
        NoAction, SwingArm, WakeUp, CriticalHit, MagicCriticalHit, RowRight, RowLeft;

        fun getId(version: Int) = registry.getLastInt(version, this)

        companion object {
            val registry = Int2ObjectOpenHashBiMapVersioned<Animation>().apply {
                put(NoAction        , V1_14_060 to 0x00)
                put(SwingArm        , V1_14_060 to 0x01)
                put(WakeUp          , V1_14_060 to 0x03)
                put(CriticalHit     , V1_14_060 to 0x04)
                put(MagicCriticalHit, V1_14_060 to 0x05)
                put(RowRight        , V1_14_060 to 0x80)
                put(RowLeft         , V1_14_060 to 0x81)
            }

            operator fun get(version: Int, id: Int) = registry.getLast(version, id)
        }
    }

    override val id get() = 0x2C

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(animation.getId(version))
        buffer.writeVarULong(runtimeEntityId)
        if (animation == Animation.RowRight || animation == Animation.RowLeft) buffer.writeFloatLE(rowingTime)
    }

    override fun handle(handler: PacketHandler) = handler.entityAnimation(this)

    override fun toString() = "EntityAnimationPacket(animation=$animation, runtimeEntityId=$runtimeEntityId, rowingTime=$rowingTime)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int): EntityAnimationPacket {
            val animation = checkNotNull(Animation[version, buffer.readVarInt()])
            val runtimeEntityId = buffer.readVarULong()
            val rowingTime = if (animation == Animation.RowRight || animation == Animation.RowLeft) buffer.readFloatLE() else 0.0f
            return EntityAnimationPacket(animation, runtimeEntityId, rowingTime)
        }
    }
}
