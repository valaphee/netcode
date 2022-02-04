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
import com.valaphee.netcode.mcbe.network.Restrict
import com.valaphee.netcode.mcbe.network.Restriction

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class CameraShakePacket(
    val intensity: Float,
    val duration: Float,
    val type: Type,
    val action: Action
) : Packet() {
    enum class Type {
        Positional, Rotational
    }

    enum class Action {
        Add, Stop
    }

    override val id get() = 0x9F

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeFloatLE(intensity)
        buffer.writeFloatLE(duration)
        if (version >= 419) buffer.writeByte(type.ordinal)
        if (version >= 428) buffer.writeByte(action.ordinal)
    }

    override fun handle(handler: PacketHandler) = handler.cameraShake(this)

    override fun toString() = "CameraShakePacket(intensity=$intensity, duration=$duration, type=$type, action=$action)"
}

/**
 * @author Kevin Ludwig
 */
object CameraShakePacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = CameraShakePacket(buffer.readFloatLE(), buffer.readFloatLE(), if (version >= 419) CameraShakePacket.Type.values()[buffer.readByte().toInt()] else CameraShakePacket.Type.Positional, if (version >= 428) CameraShakePacket.Action.values()[buffer.readByte().toInt()] else CameraShakePacket.Action.Stop)
}
