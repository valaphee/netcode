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

package com.valaphee.netcode.mcje.play

import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mcje.Packet
import com.valaphee.netcode.mcje.PacketBuffer
import com.valaphee.netcode.mcje.PacketReader

/**
 * @author Kevin Ludwig
 */
class ServerBlockBreakAnimationPacket(
    val entityId: Int,
    val position: Int3,
    val progress: Int
) : Packet<ServerPlayPacketHandler> {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(entityId)
        buffer.writeInt3UnsignedY(position)
        buffer.writeByte(progress)
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.blockBreakAnimation(this)

    override fun toString() = "ServerBlockBreakAnimationPacket(entityId=$entityId, position=$position, progress=$progress)"
}

/**
 * @author Kevin Ludwig
 */
object ServerBlockBreakAnimationPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerBlockBreakAnimationPacket(buffer.readVarInt(), buffer.readInt3UnsignedY(), buffer.readByte().toInt())
}
