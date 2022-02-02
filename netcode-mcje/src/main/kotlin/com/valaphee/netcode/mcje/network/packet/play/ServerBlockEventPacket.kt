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

import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.PacketReader
import com.valaphee.netcode.mcje.network.ServerPlayPacketHandler
import com.valaphee.netcode.mcje.util.NamespacedKey

/**
 * @author Kevin Ludwig
 */
class ServerBlockEventPacket(
    val position: Int3,
    val data1: Int,
    val data2: Int,
    val blockKey: NamespacedKey
) : Packet<ServerPlayPacketHandler> {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeInt3UnsignedY(position)
        buffer.writeByte(data1)
        buffer.writeByte(data2)
        buffer.writeVarInt(buffer.registrySet.blocks.getId(blockKey))
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.blockEvent(this)

    override fun toString() = "ServerBlockEventPacket(position=$position, data1=$data1, data2=$data2, blockKey=$blockKey)"
}

/**
 * @author Kevin Ludwig
 */
object ServerBlockEventPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerBlockEventPacket(buffer.readInt3UnsignedY(), buffer.readByte().toInt(), buffer.readByte().toInt(), checkNotNull(buffer.registrySet.blocks[buffer.readVarInt()]))
}
