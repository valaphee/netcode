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

/**
 * @author Kevin Ludwig
 */
class DimensionDataPacket(
    val dimensions: List<Dimension>
) : Packet() {
    data class Dimension(
        val id: String,
        val maximumHeight: Int,
        val minimumHeight: Int,
        val generatorType: Int
    )

    override val id get() = 0xB4

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarUInt(dimensions.size)
        dimensions.forEach {
            buffer.writeString(it.id)
            buffer.writeVarInt(it.maximumHeight)
            buffer.writeVarInt(it.minimumHeight)
            buffer.writeVarInt(it.generatorType)
        }
    }

    override fun handle(handler: PacketHandler) = handler.dimensionData(this)

    override fun toString() = "DimensionDataPacket(dimensions=$dimensions)"
}

/**
 * @author Kevin Ludwig
 */
object DimensionDataPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = DimensionDataPacket(List(buffer.readVarUInt()) { DimensionDataPacket.Dimension(buffer.readString(), buffer.readVarInt(), buffer.readVarInt(), buffer.readVarInt()) })
}
