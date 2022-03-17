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
import com.valaphee.netcode.mcbe.network.Restrict
import com.valaphee.netcode.mcbe.network.Restriction
import com.valaphee.netcode.util.safeList
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream
import java.io.OutputStream

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class ItemComponentPacket(
    val entries: List<Entry>
) : Packet() {
    data class Entry(
        val name: String,
        val data: Any?
    )

    override val id get() = 0xA2

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarUInt(entries.size)
        entries.forEach {
            buffer.writeString(it.name)
            buffer.nbtVarIntObjectMapper.writeValue(ByteBufOutputStream(buffer) as OutputStream, it.data)
        }
    }

    override fun handle(handler: PacketHandler) = handler.itemComponent(this)

    override fun toString() = "ItemComponentPacket(entries=$entries)"
}

/**
 * @author Kevin Ludwig
 */
object ItemComponentPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ItemComponentPacket(safeList(buffer.readVarUInt()) { ItemComponentPacket.Entry(buffer.readString(), buffer.nbtVarIntObjectMapper.readValue(ByteBufInputStream(buffer))) })
}
