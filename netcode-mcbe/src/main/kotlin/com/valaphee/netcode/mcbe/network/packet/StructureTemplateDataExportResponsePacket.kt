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
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.Restrict
import com.valaphee.netcode.mcbe.network.Restriction
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream
import java.io.OutputStream

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class StructureTemplateDataExportResponsePacket(
    val name: String,
    val save: Boolean,
    val data: Any?,
    val type: Type
) : Packet() {
    enum class Type {
        None, Export, Query
    }

    override val id get() = 0x85

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeString(name)
        buffer.writeBoolean(save)
        if (save) buffer.nbtVarIntObjectMapper.writeValue(ByteBufOutputStream(buffer) as OutputStream, data)
        buffer.writeByte(type.ordinal)
    }

    override fun handle(handler: PacketHandler) = handler.structureTemplateDataExportResponse(this)

    override fun toString() = "StructureTemplateDataExportResponsePacket(name='$name', save=$save, data=$data, type=$type)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int): StructureTemplateDataExportResponsePacket {
            val name = buffer.readString()
            val save = buffer.readBoolean()
            val data = if (save) buffer.nbtVarIntObjectMapper.readValue<Any?>(ByteBufInputStream(buffer)) else null
            val type = Type.values()[buffer.readUnsignedByte().toInt()]
            return StructureTemplateDataExportResponsePacket(name, save, data, type)
        }
    }
}
