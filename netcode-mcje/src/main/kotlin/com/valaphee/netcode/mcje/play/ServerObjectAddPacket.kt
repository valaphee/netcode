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
import com.valaphee.netcode.mcje.util.NamespacedKey
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
class ServerObjectAddPacket(
    val entityId: Int,
    val entityUid: UUID?,
    val type: NamespacedKey,
    val position: Double3,
    val rotation: Float2,
    val data: Int,
    val velocity: Double3
) : Packet<ServerPlayPacketHandler> {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(entityId)
        buffer.writeUuid(entityUid!!)
        buffer.writeVarInt(buffer.registrySet.entityTypes.getId(type))
        buffer.writeDouble3(position)
        buffer.writeAngle2(rotation)
        buffer.writeInt(data)
        val (velocityX, velocityY, velocityZ) = velocity.toMutableDouble3().scale(8000.0).toInt3()
        buffer.writeShort(velocityX)
        buffer.writeShort(velocityY)
        buffer.writeShort(velocityZ)
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.objectAdd(this)

    override fun toString() = "ServerObjectAddPacket(entityId=$entityId, entityUid=$entityUid, type=$type, position=$position, rotation=$rotation, data=$data, velocity=$velocity)"
}

/**
 * @author Kevin Ludwig
 */
object ServerObjectAddPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerObjectAddPacket(buffer.readVarInt(), buffer.readUuid(), checkNotNull(buffer.registrySet.entityTypes[buffer.readVarInt()]), buffer.readDouble3(), buffer.readAngle2(), buffer.readInt(), MutableDouble3(buffer.readShort().toDouble(), buffer.readShort().toDouble(), buffer.readShort().toDouble()).scale(1 / 8000.0))
}
