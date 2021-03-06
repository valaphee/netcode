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
import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.PacketReader

/**
 * @author Kevin Ludwig
 */
class EntityMoveRotatePacket(
    val runtimeEntityId: Long,
    val positionDelta: Int3,
    val position: Float3,
    val rotation: Float2,
    val headYaw: Float,
    val onGround: Boolean,
    val immediate: Boolean,
    val force: Boolean
) : Packet() {
    override val id get() = 0x6F

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarULong(runtimeEntityId)
        val flagsIndex = buffer.writerIndex()
        buffer.writeZero(Short.SIZE_BYTES)
        var flagsValue = 0
        val (rawX, rawY, rawZ) = positionDelta
        val (x, y, z) = position
        if (version >= 419) {
            if (!x.isNaN()) {
                buffer.writeFloatLE(x)
                flagsValue = flagsValue or flagHasX
            }
            if (!y.isNaN()) {
                buffer.writeFloatLE(y)
                flagsValue = flagsValue or flagHasY
            }
            if (!z.isNaN()) {
                buffer.writeFloatLE(z)
                flagsValue = flagsValue or flagHasZ
            }
        } else {
            if (rawX != 0) {
                buffer.writeVarInt(rawX)
                flagsValue = flagsValue or flagHasX
            }
            if (rawY != 0) {
                buffer.writeVarInt(rawY)
                flagsValue = flagsValue or flagHasY
            }
            if (rawZ != 0) {
                buffer.writeVarInt(rawZ)
                flagsValue = flagsValue or flagHasZ
            }
        }
        val (pitch, yaw) = rotation
        if (!pitch.isNaN()) {
            buffer.writeAngle(pitch)
            flagsValue = flagsValue or flagHasPitch
        }
        if (!yaw.isNaN()) {
            buffer.writeAngle(yaw)
            flagsValue = flagsValue or flagHasYaw
        }
        if (!headYaw.isNaN()) {
            buffer.writeAngle(headYaw)
            flagsValue = flagsValue or flagHasHeadYaw
        }
        if (onGround) flagsValue = flagsValue or flagOnGround
        if (immediate) flagsValue = flagsValue or flagImmediate
        if (force) flagsValue = flagsValue or flagForce
        buffer.setShortLE(flagsIndex, flagsValue)
    }

    override fun handle(handler: PacketHandler) = handler.entityMoveRotate(this)

    override fun toString() = "EntityMoveRotatePacket(runtimeEntityId=$runtimeEntityId, positionDelta=$positionDelta, position=$position, rotation=$rotation, headYaw=$headYaw, onGround=$onGround, immediate=$immediate, force=$force)"

    companion object {
        internal const val flagHasX = 1 shl 0
        internal const val flagHasY = 1 shl 1
        internal const val flagHasZ = 1 shl 2
        internal const val flagHasPitch = 1 shl 3
        internal const val flagHasYaw = 1 shl 4
        internal const val flagHasHeadYaw = 1 shl 5
        internal const val flagOnGround = 1 shl 6
        internal const val flagImmediate = 1 shl 7
        internal const val flagForce = 1 shl 8
    }
}

/**
 * @author Kevin Ludwig
 */
object EntityMoveRotatePacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): EntityMoveRotatePacket {
        val runtimeEntityId = buffer.readVarULong()
        val flagsValue = buffer.readShortLE().toInt()
        val position: Float3
        val positionDelta: Int3
        if (version >= 419) {
            position = Float3(
                if (flagsValue and EntityMoveRotatePacket.flagHasX != 0) buffer.readFloatLE() else Float.NaN,
                if (flagsValue and EntityMoveRotatePacket.flagHasY != 0) buffer.readFloatLE() else Float.NaN,
                if (flagsValue and EntityMoveRotatePacket.flagHasZ != 0) buffer.readFloatLE() else Float.NaN
            )
            positionDelta = Int3.Zero
        } else {
            position = Float3.Zero
            positionDelta = Int3(
                if (flagsValue and EntityMoveRotatePacket.flagHasX != 0) buffer.readVarInt() else 0,
                if (flagsValue and EntityMoveRotatePacket.flagHasY != 0) buffer.readVarInt() else 0,
                if (flagsValue and EntityMoveRotatePacket.flagHasZ != 0) buffer.readVarInt() else 0
            )
        }
        val rotation = Float2(
            if (flagsValue and EntityMoveRotatePacket.flagHasPitch != 0) buffer.readAngle() else Float.NaN,
            if (flagsValue and EntityMoveRotatePacket.flagHasYaw != 0) buffer.readAngle() else Float.NaN
        )
        val headRotationYaw = if (flagsValue and EntityMoveRotatePacket.flagHasHeadYaw != 0) buffer.readAngle() else Float.NaN
        val onGround = flagsValue and EntityMoveRotatePacket.flagOnGround != 0
        val immediate = flagsValue and EntityMoveRotatePacket.flagImmediate != 0
        val force = flagsValue and EntityMoveRotatePacket.flagForce != 0
        return EntityMoveRotatePacket(runtimeEntityId, positionDelta, position, rotation, headRotationYaw, onGround, immediate, force)
    }
}
