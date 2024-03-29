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

import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.Restrict
import com.valaphee.netcode.mcbe.network.Restriction
import com.valaphee.netcode.mcbe.world.StructureSettings
import com.valaphee.netcode.mcbe.world.readStructureSettings
import com.valaphee.netcode.mcbe.world.writeStructureSettings

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToServer)
class StructureBlockUpdatePacket(
    val position: Int3,
    val name: String,
    val metadata: String,
    val includingPlayers: Boolean,
    val showBoundingBox: Boolean,
    val mode: Mode,
    val settings: StructureSettings,
    val redstoneSaveMode: RedstoneSaveMode,
    val powered: Boolean
) : Packet() {
    enum class Mode {
        Data, Save, Load, Corner, None, Export
    }

    enum class RedstoneSaveMode {
        SavesToMemory, SavesToDisk
    }

    override val id get() = 0x5A

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeBlockPosition(position)
        buffer.writeString(name)
        buffer.writeString(metadata)
        buffer.writeBoolean(includingPlayers)
        buffer.writeBoolean(showBoundingBox)
        buffer.writeVarInt(mode.ordinal)
        buffer.writeStructureSettings(settings, version)
        buffer.writeVarInt(redstoneSaveMode.ordinal)
        buffer.writeBoolean(powered)
    }

    override fun handle(handler: PacketHandler) = handler.structureBlockUpdate(this)

    override fun toString() = "StructureBlockUpdatePacket(position=$position, name='$name', metadata='$metadata', includingPlayers=$includingPlayers, showBoundingBox=$showBoundingBox, mode=$mode, settings=$settings, redstoneSaveMode=$redstoneSaveMode, powered=$powered)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = StructureBlockUpdatePacket(buffer.readBlockPosition(), buffer.readString(), buffer.readString(), buffer.readBoolean(), buffer.readBoolean(), Mode.values()[buffer.readVarInt()], buffer.readStructureSettings(version), RedstoneSaveMode.values()[buffer.readVarInt()], buffer.readBoolean())
    }
}
