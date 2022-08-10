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
import com.valaphee.netcode.mcbe.network.PacketReader
import com.valaphee.netcode.mcbe.network.V1_19_020

/**
 * @author Kevin Ludwig
 */
class FormResponsePacket(
    val formId: Int,
    val formData: Any?,
    val cancelReason: CancelReason?
) : Packet() {
    enum class CancelReason {
        UserClosed,
        UserBusy
    }

    override val id get() = 0x65

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarUInt(formId)
        if (version >= V1_19_020) {
            formData?.let {
                buffer.writeBoolean(true)
                buffer.writeString(buffer.jsonObjectMapper.writeValueAsString(formData))
            } ?: buffer.writeBoolean(false)
            cancelReason?.let {
                buffer.writeBoolean(true)
                buffer.writeByte(cancelReason.ordinal)
            } ?: buffer.writeBoolean(false)
        } else buffer.writeString(buffer.jsonObjectMapper.writeValueAsString(formData))
    }

    override fun handle(handler: PacketHandler) = handler.formResponse(this)

    override fun toString() = "FormResponsePacket(formId=$formId, formData=$formData, cancelReason=$cancelReason)"
}

/**
 * @author Kevin Ludwig
 */
object FormResponsePacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = FormResponsePacket(buffer.readVarUInt(), if (version < V1_19_020 || buffer.readBoolean()) buffer.jsonObjectMapper.readValue<Any>(buffer.readString()) else null, if (version >= V1_19_020 && buffer.readBoolean()) FormResponsePacket.CancelReason.values()[buffer.readUnsignedByte().toInt()] else null)
}
