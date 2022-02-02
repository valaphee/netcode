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

package com.valaphee.netcode.mcje.network.packet.play

import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.ServerPlayPacketHandler
import com.valaphee.netcode.mcje.world.map.Decoration

/**
 * @author Kevin Ludwig
 */
class ServerMapPacket(
    val mapId: Int,
    val scale: Int,
    val tracking: Boolean,
    val locked: Boolean,
    val decorations: List<Decoration>,
    val width: Int,
    val height: Int,
    val offsetX: Int,
    val offsetY: Int,
    val data: ByteArray?
) : Packet<ServerPlayPacketHandler> {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(mapId)
        buffer.writeByte(scale)
        buffer.writeBoolean(tracking)
        buffer.writeBoolean(locked)
        buffer.writeVarInt(decorations.size)
        decorations.forEach { (type, positionX, positionY, rotation, label) ->
            buffer.writeVarInt(type.ordinal)
            buffer.writeByte(rotation)
            buffer.writeByte(positionX)
            buffer.writeByte(positionY)
            label?.let {
                buffer.writeBoolean(true)
                buffer.writeString(buffer.objectMapper.writeValueAsString(it))
            } ?: buffer.writeBoolean(false)
        }
        buffer.writeByte(width)
        if (width != 0) {
            buffer.writeByte(height)
            buffer.writeByte(offsetX)
            buffer.writeByte(offsetY)
            data!!.let {
                buffer.writeVarInt(it.size)
                it.forEach { buffer.writeByte(it.toInt()) }
            }
        }
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.map(this)
}
