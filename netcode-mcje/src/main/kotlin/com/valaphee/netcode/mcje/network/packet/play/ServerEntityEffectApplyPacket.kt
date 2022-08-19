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

import com.fasterxml.jackson.module.kotlin.readValue
import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.ServerPlayPacketHandler
import com.valaphee.netcode.mcje.network.V1_19_0
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream
import java.io.OutputStream

/**
 * @author Kevin Ludwig
 */
class ServerEntityEffectApplyPacket(
    val entityId: Int,
    val effectId: Int,
    val amplifier: Int,
    val duration: Int,
    val flags: Set<Flag>,
    val data: Any?
) : Packet<ServerPlayPacketHandler>() {
    enum class Flag {
        Ambient, ShowParticles, ShowIcon
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(entityId)
        buffer.writeByte(effectId)
        buffer.writeByte(amplifier)
        buffer.writeVarInt(duration)
        buffer.writeByteFlags(flags)
        if (version >= V1_19_0) data?.let {
            buffer.writeBoolean(true)
            buffer.nbtObjectMapper.writeValue(ByteBufOutputStream(buffer) as OutputStream, it)
        } ?: buffer.writeBoolean(false)
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.entityEffectApply(this)

    override fun toString() = "ServerEntityEffectApplyPacket(entityId=$entityId, effectId=$effectId, amplifier=$amplifier, duration=$duration, flags=$flags, data=$data)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = ServerEntityEffectApplyPacket(buffer.readVarInt(), buffer.readUnsignedByte().toInt(), buffer.readUnsignedByte().toInt(), buffer.readVarInt(), buffer.readByteFlags(), if (version >= V1_19_0 && buffer.readBoolean()) buffer.nbtObjectMapper.readValue(ByteBufInputStream(buffer)) else null)
    }
}
