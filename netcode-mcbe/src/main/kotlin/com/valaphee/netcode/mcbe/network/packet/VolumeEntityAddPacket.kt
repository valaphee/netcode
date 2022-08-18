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
import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.Restrict
import com.valaphee.netcode.mcbe.network.Restriction
import com.valaphee.netcode.mcbe.network.V1_17_034
import com.valaphee.netcode.mcbe.network.V1_18_010
import com.valaphee.netcode.mcbe.network.V1_18_030
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream
import java.io.OutputStream

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class VolumeEntityAddPacket(
    val entityId: Int,
    val data: Any?,
    val identifier: String?,
    val instanceName: String?,
    val minimum: Int3?,
    val maximum: Int3?,
    val dimension: Int,
    val engineVersion: String?,
) : Packet() {
    override val id = 0xA6

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarUInt(entityId)
        buffer.nbtVarIntObjectMapper.writeValue(ByteBufOutputStream(buffer) as OutputStream, data)
        if (version >= V1_18_010) {
            buffer.writeString(identifier!!)
            buffer.writeString(instanceName!!)
        }
        if (version >= V1_18_030) {
            buffer.writeBlockPosition(minimum!!)
            buffer.writeBlockPosition(maximum!!)
            buffer.writeVarInt(dimension)
        }
        if (version >= V1_17_034) buffer.writeString(engineVersion!!)
    }

    override fun handle(handler: PacketHandler) = handler.volumeEntityAdd(this)

    override fun toString() = "VolumeEntityAddPacket(entityId=$entityId, data=$data, identifier=$identifier, instanceName=$instanceName, minimum=$minimum, maximum=$maximum, dimension=$dimension, engineVersion=$engineVersion)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = VolumeEntityAddPacket(buffer.readVarUInt(), buffer.nbtVarIntObjectMapper.readValue(ByteBufInputStream(buffer)), if (version >= V1_18_010) buffer.readString() else null, if (version >= V1_18_010) buffer.readString() else null, if (version >= V1_18_030) buffer.readBlockPosition() else null, if (version >= V1_18_030) buffer.readBlockPosition() else null, if (version >= V1_18_030) buffer.readVarInt() else 0, if (version >= V1_17_034) buffer.readString() else null)
    }
}
