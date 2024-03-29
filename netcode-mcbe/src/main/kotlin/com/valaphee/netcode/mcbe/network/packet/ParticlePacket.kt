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

import com.fasterxml.jackson.module.kotlin.readValue
import com.valaphee.foundry.math.Float3
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.Restrict
import com.valaphee.netcode.mcbe.network.Restriction
import com.valaphee.netcode.mcbe.network.V1_18_030
import com.valaphee.netcode.mcbe.world.Dimension
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream
import java.io.OutputStream

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class ParticlePacket(
    val dimension: Dimension,
    val uniqueEntityId: Long,
    val position: Float3,
    val particleName: String,
    val data: Any?,
) : Packet() {
    override val id get() = 0x76

    constructor(dimension: Dimension, uniqueEntityId: Long, particleName: String) : this(dimension, uniqueEntityId, Float3.Zero, particleName, null)

    constructor(dimension: Dimension, position: Float3, particleName: String) : this(dimension, -1, position, particleName, null)

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeByte(dimension.ordinal)
        buffer.writeVarLong(uniqueEntityId)
        buffer.writeFloat3(position)
        buffer.writeString(particleName)
        if (version >= V1_18_030) if (data != null) {
            buffer.writeBoolean(true)
            buffer.nbtVarIntObjectMapper.writeValue(ByteBufOutputStream(buffer) as OutputStream, data)
        } else buffer.writeBoolean(false)
    }

    override fun handle(handler: PacketHandler) = handler.particle(this)

    override fun toString() = "ParticlePacket(dimension=$dimension, uniqueEntityId=$uniqueEntityId, position=$position, particleName='$particleName')"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = ParticlePacket(
            Dimension.values()[buffer.readUnsignedByte().toInt()],
            buffer.readVarLong(),
            buffer.readFloat3(),
            buffer.readString(),
            if (version >= V1_18_030 && buffer.readBoolean()) buffer.nbtVarIntObjectMapper.readValue(ByteBufInputStream(buffer)) else null
        )
    }
}
