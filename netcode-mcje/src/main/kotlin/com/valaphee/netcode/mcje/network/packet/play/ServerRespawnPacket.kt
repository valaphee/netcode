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

import com.valaphee.netcode.mc.util.nbt.Tag
import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.PacketReader
import com.valaphee.netcode.mcje.network.ServerPlayPacketHandler
import com.valaphee.netcode.mcje.util.NamespacedKey
import com.valaphee.netcode.mcje.world.GameMode

/**
 * @author Kevin Ludwig
 */
class ServerRespawnPacket(
    val dimension: Tag?,
    val worldName: NamespacedKey?,
    val hashedSeed: Long,
    val gameMode: GameMode,
    val previousGameMode: GameMode,
    val debugGenerator: Boolean,
    val flatGenerator: Boolean,
    val keepMetadata: Boolean
) : Packet<ServerPlayPacketHandler> {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.toNbtOutputStream().use { it.writeTag(dimension) }
        buffer.writeNamespacedKey(worldName!!)
        buffer.writeLong(hashedSeed)
        buffer.writeByte(gameMode.ordinal)
        buffer.writeByte(previousGameMode.ordinal)
        buffer.writeBoolean(debugGenerator)
        buffer.writeBoolean(flatGenerator)
        buffer.writeBoolean(keepMetadata)
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.respawn(this)

    override fun toString() = "ServerRespawnPacket(dimension=$dimension, worldName=$worldName, hashedSeed=$hashedSeed, gameMode=$gameMode, previousGameMode=$previousGameMode, debugGenerator=$debugGenerator, flatGenerator=$flatGenerator, keepMetadata=$keepMetadata)"
}

/**
 * @author Kevin Ludwig
 */
object ServerRespawnPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerRespawnPacket(buffer.toNbtInputStream().use { it.readTag() }, buffer.readNamespacedKey(), buffer.readLong(), checkNotNull(GameMode.byIdOrNull(buffer.readUnsignedByte().toInt())), checkNotNull(GameMode.byIdOrNull(buffer.readUnsignedByte().toInt())), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean())
}
