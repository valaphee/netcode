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
class StructureTemplateDataExportRequestPacket(
    val name: String,
    val position: Int3,
    val settings: StructureSettings,
    val operation: Operation
) : Packet() {
    enum class Operation {
        None, ExportFromSaveMode, ExportFromLoadMode, QuerySavedStructure
    }

    override val id get() = 0x84

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeString(name)
        buffer.writeBlockPosition(position)
        buffer.writeStructureSettings(settings, version)
        buffer.writeByte(operation.ordinal)
    }

    override fun handle(handler: PacketHandler) = handler.structureTemplateDataExportRequest(this)

    override fun toString() = "StructureTemplateDataExportRequestPacket(name='$name', position=$position, settings=$settings, operation=$operation)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = StructureTemplateDataExportRequestPacket(buffer.readString(), buffer.readBlockPosition(), buffer.readStructureSettings(version), Operation.values()[buffer.readByte().toInt()])
    }
}
