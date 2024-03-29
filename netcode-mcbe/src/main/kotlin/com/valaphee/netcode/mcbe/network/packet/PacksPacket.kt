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
import com.valaphee.netcode.mcbe.network.V1_16_201
import com.valaphee.netcode.mcbe.network.V1_17_011
import com.valaphee.netcode.util.LazyList
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class PacksPacket(
    val forcedToAccept: Boolean,
    val scriptingEnabled: Boolean,
    val forcingServerPacksEnabled: Boolean,
    val behaviorPacks: List<Pack>,
    val resourcePacks: List<Pack>
) : Packet() {
    data class Pack(
        val id: UUID,
        val version: String,
        val size: Long,
        val encryptionKey: String,
        val subPackName: String,
        val contentId: String,
        val scripting: Boolean,
        val raytracingCapable: Boolean = false
    )

    override val id get() = 0x06

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeBoolean(forcedToAccept)
        buffer.writeBoolean(scriptingEnabled)
        if (version >= V1_17_011) buffer.writeBoolean(forcingServerPacksEnabled)
        buffer.writeShortLE(behaviorPacks.size)
        behaviorPacks.forEach {
            buffer.writeString(it.id.toString())
            buffer.writeString(it.version)
            buffer.writeLongLE(it.size)
            buffer.writeString(it.encryptionKey)
            buffer.writeString(it.subPackName)
            buffer.writeString(it.contentId)
            buffer.writeBoolean(it.scripting)
        }
        buffer.writeShortLE(resourcePacks.size)
        resourcePacks.forEach {
            buffer.writeString(it.id.toString())
            buffer.writeString(it.version)
            buffer.writeLongLE(it.size)
            buffer.writeString(it.encryptionKey)
            buffer.writeString(it.subPackName)
            buffer.writeString(it.contentId)
            buffer.writeBoolean(it.scripting)
            if (version >= V1_16_201) buffer.writeBoolean(it.raytracingCapable)
        }
    }

    override fun handle(handler: PacketHandler) = handler.packs(this)

    override fun toString() = "PacksPacket(forcedToAccept=$forcedToAccept, scriptingEnabled=$scriptingEnabled, forcingServerPacksEnabled=$forcingServerPacksEnabled, behaviorPacks=$behaviorPacks, resourcePacks=$resourcePacks)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = PacksPacket(
            buffer.readBoolean(),
            buffer.readBoolean(),
            if (version >= V1_17_011) buffer.readBoolean() else false,
            LazyList(buffer.readUnsignedShortLE()) { Pack(UUID.fromString(buffer.readString()), buffer.readString(), buffer.readLongLE(), buffer.readString(), buffer.readString(), buffer.readString(), buffer.readBoolean()) },
            LazyList(buffer.readUnsignedShortLE()) { Pack(UUID.fromString(buffer.readString()), buffer.readString(), buffer.readLongLE(), buffer.readString(), buffer.readString(), buffer.readString(), buffer.readBoolean(), if (version >= V1_16_201) buffer.readBoolean() else false) }
        )
    }
}
