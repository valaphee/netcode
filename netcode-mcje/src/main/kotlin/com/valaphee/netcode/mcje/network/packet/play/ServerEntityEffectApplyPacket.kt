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
import com.valaphee.netcode.mcje.util.NamespacedKey

/**
 * @author Kevin Ludwig
 */
class ServerEntityEffectApplyPacket(
    val entityId: Int,
    val effect: NamespacedKey,
    val amplifier: Int,
    val duration: Int,
    val flags: Set<Flag>
) : Packet<ServerPlayPacketHandler> {
    enum class Flag {
        Ambient, ShowParticles, ShowIcon
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(entityId)
        buffer.writeByte(buffer.registrySet.effects.getId(effect))
        buffer.writeByte(amplifier)
        buffer.writeVarInt(duration)
        buffer.writeByteFlags(flags)
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.entityEffectApply(this)

    override fun toString() = "ServerEntityEffectApplyPacket(entityId=$entityId, effect=$effect, amplifier=$amplifier, duration=$duration, flags=$flags)"
}

/**
 * @author Kevin Ludwig
 */
object ServerEntityEffectApplyPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerEntityEffectApplyPacket(buffer.readVarInt(), buffer.registrySet.effects[buffer.readUnsignedByte().toInt()]!!, buffer.readUnsignedByte().toInt(), buffer.readVarInt(), buffer.readByteFlags())
}
