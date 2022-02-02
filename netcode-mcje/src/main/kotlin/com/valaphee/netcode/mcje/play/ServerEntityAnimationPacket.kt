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

package com.valaphee.netcode.mcje.play

import com.valaphee.netcode.mcje.Packet
import com.valaphee.netcode.mcje.PacketBuffer
import com.valaphee.netcode.mcje.PacketReader

/**
 * @author Kevin Ludwig
 */
class ServerEntityAnimationPacket(
    val entityId: Int,
    val animation: Animation
) : Packet<ServerPlayPacketHandler> {
    enum class Animation {
        SwingMainArm, Hit, WakeUp, SwingOffArm, CriticalHit, MagicCriticalHit
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(entityId)
        buffer.writeByte(animation.ordinal)
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.entityAnimation(this)

    override fun toString() = "ServerEntityAnimationPacket(entityId=$entityId, animation=$animation)"
}

/**
 * @author Kevin Ludwig
 */
object ServerEntityAnimationPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerEntityAnimationPacket(buffer.readVarInt(), ServerEntityAnimationPacket.Animation.values()[buffer.readUnsignedByte().toInt()])
}
