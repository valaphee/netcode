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
import com.valaphee.netcode.mcbe.network.PacketReader
import com.valaphee.netcode.mcbe.network.Restrict
import com.valaphee.netcode.mcbe.network.Restriction
import com.valaphee.netcode.mcbe.util.safeList
import com.valaphee.netcode.mcbe.world.Experiment
import com.valaphee.netcode.mcbe.world.readExperiment
import com.valaphee.netcode.mcbe.world.writeExperiment
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class PacksStackPacket(
    val forcedToAccept: Boolean,
    val behaviorPacks: List<Pack>,
    val resourcePacks: List<Pack>,
    val experimental: Boolean,
    val version: String,
    val experiments: List<Experiment>,
    val experimentsPreviouslyToggled: Boolean
) : Packet() {
    data class Pack(
        val id: UUID,
        val version: String,
        val subPackName: String
    )

    override val id get() = 0x07

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeBoolean(forcedToAccept)
        buffer.writeVarUInt(behaviorPacks.size)
        behaviorPacks.forEach {
            buffer.writeString(it.id.toString())
            buffer.writeString(it.version)
            buffer.writeString(it.subPackName)
        }
        buffer.writeVarUInt(resourcePacks.size)
        resourcePacks.forEach {
            buffer.writeString(it.id.toString())
            buffer.writeString(it.version)
            buffer.writeString(it.subPackName)
        }
        if (version < 419) buffer.writeBoolean(experimental)
        buffer.writeString(this.version)
        if (version >= 419) {
            buffer.writeIntLE(experiments.size)
            experiments.forEach { buffer.writeExperiment(it) }
            buffer.writeBoolean(experimentsPreviouslyToggled)
        }
    }

    override fun handle(handler: PacketHandler) = handler.packsStack(this)

    override fun toString() = "PacksStackPacket(forcedToAccept=$forcedToAccept, behaviorPacks=$behaviorPacks, resourcePacks=$resourcePacks, experimental=$experimental, version='$version', experiments=$experiments, experimentsPreviouslyToggled=$experimentsPreviouslyToggled)"
}

/**
 * @author Kevin Ludwig
 */
object PacksStackPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = PacksStackPacket(
        buffer.readBoolean(),
        safeList(buffer.readVarUInt()) { PacksStackPacket.Pack(UUID.fromString(buffer.readString()), buffer.readString(), buffer.readString()) },
        safeList(buffer.readVarUInt()) { PacksStackPacket.Pack(UUID.fromString(buffer.readString()), buffer.readString(), buffer.readString()) },
        if (version < 419) buffer.readBoolean() else false,
        buffer.readString(),
        if (version >= 419) safeList(buffer.readIntLE()) { buffer.readExperiment() } else emptyList(),
        if (version >= 419) buffer.readBoolean() else false
    )
}
