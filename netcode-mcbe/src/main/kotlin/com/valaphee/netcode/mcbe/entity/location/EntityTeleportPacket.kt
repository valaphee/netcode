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

package com.valaphee.netcode.mcbe.entity.location

import com.valaphee.foundry.math.Float2
import com.valaphee.foundry.math.Float3
import com.valaphee.netcode.mcbe.Packet
import com.valaphee.netcode.mcbe.PacketBuffer
import com.valaphee.netcode.mcbe.PacketHandler
import com.valaphee.netcode.mcbe.PacketReader

/**
 * @author Kevin Ludwig
 */
class EntityTeleportPacket(
    val runtimeEntityId: Long,
    val position: Float3,
    val rotation: Float2,
    val headRotationYaw: Float,
    val onGround: Boolean,
    val immediate: Boolean
) : Packet() {
    override val id get() = 0x12

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarULong(runtimeEntityId)
        var flagsValue = if (onGround) flagOnGround else 0
        if (immediate) flagsValue = flagsValue or flagImmediate
        buffer.writeByte(flagsValue)
        buffer.writeFloat3(position)
        buffer.writeAngle2(rotation)
        buffer.writeAngle(headRotationYaw)
    }

    override fun handle(handler: PacketHandler) = handler.entityTeleport(this)

    override fun toString() = "EntityTeleportPacket(runtimeEntityId=$runtimeEntityId, position=$position, rotation=$rotation, headRotationYaw=$headRotationYaw, onGround=$onGround, immediate=$immediate)"

    companion object {
        internal const val flagOnGround = 1 shl 0
        internal const val flagImmediate = 1 shl 1
    }
}

/**
 * @author Kevin Ludwig
 */
object EntityTeleportPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): EntityTeleportPacket {
        val runtimeEntityId = buffer.readVarULong()
        val flagsValue = buffer.readUnsignedByte().toInt()
        val onGround = flagsValue and EntityTeleportPacket.flagOnGround != 0
        val immediate = flagsValue and EntityTeleportPacket.flagImmediate != 0
        val position = buffer.readFloat3()
        val rotation = buffer.readAngle2()
        val headRotationYaw = buffer.readAngle()
        return EntityTeleportPacket(runtimeEntityId, position, rotation, headRotationYaw, onGround, immediate)
    }
}
