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
import com.valaphee.netcode.mcje.network.V1_18_2
import com.valaphee.netcode.mcje.network.V1_19_1
import com.valaphee.netcode.mcje.world.map.Decoration
import com.valaphee.netcode.util.LazyList

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
) : Packet<ServerPlayPacketHandler>() {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(mapId)
        buffer.writeByte(scale)
        if (version >= V1_18_2) {
            buffer.writeBoolean(locked)
            if (version < V1_19_1) buffer.writeBoolean(tracking)
        } else {
            buffer.writeBoolean(tracking)
            buffer.writeBoolean(locked)
        }
        buffer.writeVarInt(decorations.size)
        decorations.forEach { (type, positionX, positionY, rotation, label) ->
            buffer.writeVarInt(type.ordinal)
            buffer.writeByte(rotation)
            buffer.writeByte(positionX)
            buffer.writeByte(positionY)
            label?.let {
                buffer.writeBoolean(true)
                buffer.writeComponent(it)
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

    override fun toString() = "ServerMapPacket(mapId=$mapId, scale=$scale, locked=$locked, tracking=$tracking, decorations=$decorations, width=$width, height=$height, offsetX=$offsetX, offsetY=$offsetY, data=<omitted>)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int): ServerMapPacket {
            val mapId = buffer.readVarInt()
            val scale = buffer.readUnsignedByte().toInt()
            val locked: Boolean
            val tracking: Boolean
            if (version >= V1_18_2) {
                locked = buffer.readBoolean()
                tracking = if (version >= V1_19_1) true else buffer.readBoolean()
            } else {
                tracking = buffer.readBoolean()
                locked = buffer.readBoolean()
            }
            val decorations = LazyList(buffer.readVarInt()) { Decoration(Decoration.Type.values()[buffer.readVarInt()], buffer.readByte().toInt(), buffer.readByte().toInt(), buffer.readByte().toInt(), if (buffer.readBoolean()) buffer.readComponent() else null) }
            val width = buffer.readUnsignedByte().toInt()
            val height: Int
            val offsetX: Int
            val offsetY: Int
            val data: ByteArray?
            if (width != 0) {
                height = buffer.readUnsignedByte().toInt()
                offsetX = buffer.readUnsignedByte().toInt()
                offsetY = buffer.readUnsignedByte().toInt()
                data = ByteArray(buffer.readVarInt()) { buffer.readByte() }
            } else {
                height = 0
                offsetX = 0
                offsetY = 0
                data = null
            }
            return ServerMapPacket(mapId, scale, locked, tracking, decorations, width, height, offsetX, offsetY, data)
        }
    }
}
