/*
 * MIT License
 *
 * Copyright (c) 2021, Valaphee.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.valaphee.netcode.mcbe.pack

import com.valaphee.netcode.mcbe.Packet
import com.valaphee.netcode.mcbe.PacketBuffer
import com.valaphee.netcode.mcbe.PacketHandler
import com.valaphee.netcode.mcbe.PacketReader
import com.valaphee.netcode.mcbe.Restrict
import com.valaphee.netcode.mcbe.Restriction
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
