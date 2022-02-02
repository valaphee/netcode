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
import com.valaphee.netcode.util.safeList

/**
 * @author Kevin Ludwig
 */
class EntityAnimatePacket(
    val animation: String,
    val nextState: String,
    val stopExpression: String,
    val controller: String,
    val blendOutTime: Float,
    val runtimeEntityIds: List<Long>
) : Packet() {
    override val id get() = 0x9E

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeString(animation)
        buffer.writeString(nextState)
        buffer.writeString(stopExpression)
        if (version >= 465) buffer.writeInt(0)
        buffer.writeString(controller)
        buffer.writeFloatLE(blendOutTime)
        buffer.writeVarUInt(runtimeEntityIds.size)
        runtimeEntityIds.forEach { buffer.writeVarULong(it) }
    }

    override fun handle(handler: PacketHandler) = handler.entityAnimate(this)

    override fun toString() = "EntityAnimatePacket(animation='$animation', nextState='$nextState', stopExpression='$stopExpression', controller='$controller', blendOutTime=$blendOutTime, runtimeEntityIds=$runtimeEntityIds)"
}

/**
 * @author Kevin Ludwig
 */
object EntityAnimatePacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = EntityAnimatePacket(
        buffer.readString(),
        buffer.readString(),
        buffer.readString(),
        buffer.readString(),
        buffer.readFloatLE(),
        safeList(buffer.readVarUInt()) { buffer.readVarULong() },
    )
}
