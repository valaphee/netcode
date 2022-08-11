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

import com.valaphee.foundry.math.Float2
import com.valaphee.foundry.math.Float3
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.V1_16_100

/**
 * @author Kevin Ludwig
 */
class PlayerLocationPacket(
    val runtimeEntityId: Long,
    val position: Float3,
    val rotation: Float2,
    val headRotationYaw: Float,
    val mode: Mode,
    val onGround: Boolean,
    val vehicleRuntimeEntityId: Long,
    val teleportationCause: TeleportationCause = TeleportationCause.Unknown,
    val entityTypeId: Int,
    val tick: Long = 0
) : Packet() {
    enum class Mode {
        Normal, Reset, Teleport, Vehicle
    }

    enum class TeleportationCause {
        Unknown, Projectile, ChorusFruit, Command, Behavior
    }

    override val id get() = 0x13

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarULong(runtimeEntityId)
        buffer.writeFloat3(position)
        buffer.writeFloat2(rotation)
        buffer.writeFloatLE(headRotationYaw)
        buffer.writeByte(mode.ordinal)
        buffer.writeBoolean(onGround)
        buffer.writeVarULong(vehicleRuntimeEntityId)
        if (mode == Mode.Teleport) {
            buffer.writeIntLE(teleportationCause.ordinal)
            buffer.writeIntLE(entityTypeId)
        }
        if (version >= V1_16_100) buffer.writeVarULong(tick)
    }

    override fun handle(handler: PacketHandler) = handler.playerLocation(this)

    override fun toString() = "PlayerLocationPacket(runtimeEntityId=$runtimeEntityId, position=$position, rotation=$rotation, headRotationYaw=$headRotationYaw, mode=$mode, onGround=$onGround, drivingRuntimeEntityId=$vehicleRuntimeEntityId, teleportationCause=$teleportationCause, entityTypeId=$entityTypeId, tick=$tick)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int): PlayerLocationPacket {
            val runtimeEntityId = buffer.readVarULong()
            val position = buffer.readFloat3()
            val rotation = buffer.readFloat2()
            val headRotationYaw = buffer.readFloatLE()
            val mode = PlayerLocationPacket.Mode.values()[buffer.readUnsignedByte().toInt()]
            val onGround = buffer.readBoolean()
            val drivingRuntimeEntityId = buffer.readVarULong()
            val teleportationCause: TeleportationCause
            val entityTypeId: Int
            if (mode == Mode.Teleport) {
                teleportationCause = PlayerLocationPacket.TeleportationCause.values()[buffer.readIntLE()]
                entityTypeId = buffer.readIntLE()
            } else {
                teleportationCause = TeleportationCause.Unknown
                entityTypeId = 0
            }
            val tick = if (version >= V1_16_100) buffer.readVarULong() else 0
            return PlayerLocationPacket(runtimeEntityId, position, rotation, headRotationYaw, mode, onGround, drivingRuntimeEntityId, teleportationCause, entityTypeId, tick)
        }
    }
}
