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
import com.valaphee.netcode.mcbe.network.V1_19_020
import com.valaphee.netcode.mcbe.world.Dimension
import com.valaphee.netcode.mcbe.world.map.Decoration
import com.valaphee.netcode.mcbe.world.map.TrackedObject

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class MapPacket(
    val mapId: Long,
    val dimension: Dimension,
    val locked: Boolean,
    val origin: Int3,
    val trackedUniqueEntityIds: LongArray?,
    val scale: Int = 0,
    val trackedObjects: List<TrackedObject>?,
    val decorations: List<Decoration>?,
    val width: Int,
    val height: Int,
    val offsetX: Int,
    val offsetY: Int,
    val data: IntArray?
) : Packet() {
    override val id get() = 0x43

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarLong(mapId)
        var flagsValue = if (data?.isNotEmpty() == true) flagHasColor else 0 or if (decorations?.isNotEmpty() == true && trackedObjects?.isNotEmpty() == true) flagHasDecorationAndTrackedObjects else 0
        buffer.writeVarUInt(flagsValue)
        buffer.writeByte(dimension.ordinal)
        buffer.writeBoolean(locked)
        if (version >= V1_19_020) buffer.writeInt3UnsignedY(origin)
        trackedUniqueEntityIds?.let {
            if (it.isNotEmpty()) {
                buffer.writeVarUInt(it.size)
                it.forEach { buffer.writeVarLong(it) }
                flagsValue = flagsValue or flagHasTrackedEntities
            }
        }
        if (flagsValue and flagHasAll != 0) buffer.writeByte(scale)
        if (flagsValue and flagHasDecorationAndTrackedObjects != 0) {
            trackedObjects?.let {
                buffer.writeVarUInt(it.size)
                it.forEach {
                    buffer.writeIntLE(it.type.ordinal)
                    when (it.type) {
                        TrackedObject.Type.Entity -> buffer.writeVarLong(it.uniqueEntityId!!)
                        TrackedObject.Type.Block -> buffer.writeInt3UnsignedY(it.blockPosition!!)
                    }
                }
            } ?: buffer.writeVarUInt(0)
            decorations?.let {
                buffer.writeVarUInt(it.size)
                it.forEach {
                    buffer.writeByte(it.type.ordinal)
                    buffer.writeByte(it.rotation.toInt())
                    buffer.writeByte(it.positionX.toInt())
                    buffer.writeByte(it.positionY.toInt())
                    buffer.writeString(it.label)
                    buffer.writeVarUInt(it.color)
                }
            } ?: buffer.writeVarUInt(0)
        }
        if (flagsValue and flagHasColor != 0) data!!.let {
            buffer.writeVarInt(width)
            buffer.writeVarInt(height)
            buffer.writeVarInt(offsetX)
            buffer.writeVarInt(offsetY)
            buffer.writeVarUInt(it.size)
            it.forEach(buffer::writeVarUInt)
        }
    }

    override fun handle(handler: PacketHandler) = handler.map(this)

    override fun toString() = "MapPacket(mapId=$mapId, dimension=$dimension, locked=$locked, origin=$origin trackedUniqueEntityIds=${trackedUniqueEntityIds?.contentToString()}, scale=$scale, trackedObjects=$trackedObjects, decorations=$decorations, width=$width, height=$height, offsetX=$offsetX, offsetY=$offsetY, data=<omitted>)"

    companion object {
        internal const val flagHasColor = 1 shl 1
        internal const val flagHasDecorationAndTrackedObjects = 1 shl 2
        internal const val flagHasTrackedEntities = 1 shl 3
        internal const val flagHasAll = flagHasColor or flagHasDecorationAndTrackedObjects or flagHasTrackedEntities
    }
}
