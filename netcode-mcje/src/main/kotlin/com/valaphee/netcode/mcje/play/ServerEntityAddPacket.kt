/*
 * MIT License
 *
 * Copyright (c) 2021, Valaphee.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.valaphee.netcode.mcje.play

import com.valaphee.foundry.math.Double3
import com.valaphee.foundry.math.Float2
import com.valaphee.foundry.math.MutableDouble3
import com.valaphee.netcode.mcje.Packet
import com.valaphee.netcode.mcje.PacketBuffer
import com.valaphee.netcode.mcje.PacketReader
import com.valaphee.netcode.mcje.entity.metadata.Metadata
import com.valaphee.netcode.mcje.util.NamespacedKey
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
class ServerEntityAddPacket(
    val entityId: Int,
    val entityUid: UUID?,
    val entityTypeKey: NamespacedKey,
    val position: Double3,
    val rotation: Float2,
    val headRotationYaw: Float,
    val motion: Double3,
    val metadata: Metadata?
) : Packet<ServerPlayPacketHandler> {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(entityId)
        if (version >= 498) {
            buffer.writeUuid(entityUid!!)
            buffer.writeVarInt(buffer.registrySet.entityTypes.getId(entityTypeKey))
            buffer.writeDouble3(position)
        } else {
            buffer.writeByte(buffer.registrySet.entityTypes.getId(entityTypeKey))
            buffer.writeInt3(position.toMutableDouble3().scale(32.0).toInt3())
        }
        buffer.writeAngle2(rotation)
        buffer.writeAngle(headRotationYaw)
        val (motionX, motionY, motionZ) = motion.toMutableDouble3().scale(8000.0).toInt3()
        buffer.writeShort(motionX)
        buffer.writeShort(motionY)
        buffer.writeShort(motionZ)
        if (version < 578) metadata!!.writeToBuffer(buffer)
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.entityAdd(this)

    override fun toString() = "ServerEntityAddPacket(entityId=$entityId, entityUid=$entityUid, entityTypeKey=$entityTypeKey, position=$position, rotation=$rotation, headRotationYaw=$headRotationYaw, motion=$motion, metadata=$metadata)"
}

/**
 * @author Kevin Ludwig
 */
object ServerEntityAddPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): ServerEntityAddPacket {
        val entityId = buffer.readVarInt()
        val entityUid: UUID?
        val entityTypeKey: NamespacedKey
        val position: Double3
        if (version >= 498) {
            entityUid = buffer.readUuid()
            entityTypeKey = buffer.registrySet.entityTypes[buffer.readVarInt()]!!
            position = buffer.readDouble3()
        } else {
            entityUid = null
            entityTypeKey =  buffer.registrySet.entityTypes[buffer.readUnsignedByte().toInt()]!!
            position = buffer.readInt3().toMutableDouble3().scale(1 / 32.0)
        }
        val rotation = buffer.readAngle2()
        val headRotationYaw = buffer.readAngle()
        val motion = MutableDouble3(buffer.readShort().toDouble(), buffer.readShort().toDouble(), buffer.readShort().toDouble()).scale(1 / 8000.0)
        val metadata = if (version < 578) Metadata().apply { readFromBuffer(buffer) } else null
        return ServerEntityAddPacket(entityId, entityUid, entityTypeKey, position, rotation, headRotationYaw, motion, metadata)
    }
}
