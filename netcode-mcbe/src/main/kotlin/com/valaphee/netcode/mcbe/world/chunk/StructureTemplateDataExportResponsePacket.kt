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

package com.valaphee.netcode.mcbe.world.chunk

import com.valaphee.netcode.mc.util.nbt.Tag
import com.valaphee.netcode.mcbe.Packet
import com.valaphee.netcode.mcbe.PacketBuffer
import com.valaphee.netcode.mcbe.PacketHandler
import com.valaphee.netcode.mcbe.PacketReader
import com.valaphee.netcode.mcbe.Restrict
import com.valaphee.netcode.mcbe.Restriction

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class StructureTemplateDataExportResponsePacket(
    val name: String,
    val save: Boolean,
    val tag: Tag?,
    val type: Type
) : Packet() {
    enum class Type {
        None, Export, Query
    }

    override val id get() = 0x85

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeString(name)
        buffer.writeBoolean(save)
        if (save) buffer.toNbtOutputStream().use { it.writeTag(tag) }
        buffer.writeByte(type.ordinal)
    }

    override fun handle(handler: PacketHandler) = handler.structureTemplateDataExportResponse(this)

    override fun toString() = "StructureTemplateDataExportResponsePacket(name='$name', save=$save, tag=$tag, type=$type)"
}

/**
 * @author Kevin Ludwig
 */
object StructureTemplateDataExportResponsePacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): StructureTemplateDataExportResponsePacket {
        val name = buffer.readString()
        val save = buffer.readBoolean()
        val tag = if (save) buffer.toNbtInputStream().use { it.readTag() } else null
        val type = StructureTemplateDataExportResponsePacket.Type.values()[buffer.readUnsignedByte().toInt()]
        return StructureTemplateDataExportResponsePacket(name, save, tag, type)
    }
}
