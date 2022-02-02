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

import com.valaphee.netcode.mc.util.Registry
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.PacketReader

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

        companion object {
            internal val registry = Registry<Animation>().apply {
                this[0x00] = NoAction
                this[0x01] = SwingArm
                this[0x03] = WakeUp
                this[0x04] = CriticalHit
                this[0x05] = MagicCriticalHit
                this[0x80] = RowRight
                this[0x81] = RowLeft
            }
        }
    }

    override val id get() = 0x2C

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(Animation.registry.getId(animation))
        buffer.writeVarULong(runtimeEntityId)
        if (animation == Animation.RowRight || animation == Animation.RowLeft) buffer.writeFloatLE(rowingTime)
    }

    override fun handle(handler: PacketHandler) = handler.entityAnimation(this)

    override fun toString() = "EntityAnimationPacket(animation=$animation, runtimeEntityId=$runtimeEntityId, rowingTime=$rowingTime)"
}

/**
 * @author Kevin Ludwig
 */
object EntityAnimationPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): EntityAnimationPacket {
        val animation = checkNotNull(EntityAnimationPacket.Animation.registry[buffer.readVarInt()])
        val runtimeEntityId = buffer.readVarULong()
        val rowingTime = if (animation == EntityAnimationPacket.Animation.RowRight || animation == EntityAnimationPacket.Animation.RowLeft) buffer.readFloatLE() else 0.0f
        return EntityAnimationPacket(animation, runtimeEntityId, rowingTime)
    }
}
