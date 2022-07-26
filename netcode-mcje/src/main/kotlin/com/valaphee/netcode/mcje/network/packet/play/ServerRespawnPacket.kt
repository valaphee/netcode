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

import com.fasterxml.jackson.module.kotlin.readValue
import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.PacketReader
import com.valaphee.netcode.mcje.network.ServerPlayPacketHandler
import com.valaphee.netcode.mcje.util.NamespacedKey
import com.valaphee.netcode.mcje.world.Dimension
import com.valaphee.netcode.mcje.world.GameMode
import com.valaphee.netcode.mcje.world.entity.player.DeathLocation
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream
import java.io.OutputStream

/**
 * @author Kevin Ludwig
 */
class ServerRespawnPacket(
    val dimensionName: NamespacedKey?,
    val dimension: Dimension?,
    val worldName: NamespacedKey,
    val hashedSeed: Long,
    val gameMode: GameMode,
    val previousGameMode: GameMode,
    val debugGenerator: Boolean,
    val flatGenerator: Boolean,
    val keepMetadata: Boolean,
    val deathLocation: DeathLocation?
) : Packet<ServerPlayPacketHandler>() {
    override fun write(buffer: PacketBuffer, version: Int) {
        if (version >= 759) buffer.writeNamespacedKey(dimensionName!!) else buffer.nbtObjectMapper.writeValue(ByteBufOutputStream(buffer) as OutputStream, dimension!!)
        buffer.writeNamespacedKey(worldName)
        buffer.writeLong(hashedSeed)
        buffer.writeByte(gameMode.id)
        buffer.writeByte(previousGameMode.id)
        buffer.writeBoolean(debugGenerator)
        buffer.writeBoolean(flatGenerator)
        buffer.writeBoolean(keepMetadata)
        if (version >= 759) deathLocation?.let {
            buffer.writeBoolean(true)
            buffer.writeNamespacedKey(it.dimension)
            buffer.writeInt3UnsignedY(it.position)
        } ?: buffer.writeBoolean(false)
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.respawn(this)

    override fun toString() = "ServerRespawnPacket(dimension=$dimension, worldName=$worldName, hashedSeed=$hashedSeed, gameMode=$gameMode, previousGameMode=$previousGameMode, debugGenerator=$debugGenerator, flatGenerator=$flatGenerator, keepMetadata=$keepMetadata, deathLocation=$deathLocation)"
}

/**
 * @author Kevin Ludwig
 */
object ServerRespawnPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerRespawnPacket(if (version >= 759) buffer.readNamespacedKey() else null, if (version < 759) buffer.nbtObjectMapper.readValue(ByteBufInputStream(buffer)) else null, buffer.readNamespacedKey(), buffer.readLong(), checkNotNull(GameMode.byIdOrNull(buffer.readByte().toInt())), checkNotNull(GameMode.byIdOrNull(buffer.readByte().toInt())), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean(), if (version >= 759 && buffer.readBoolean()) DeathLocation(buffer.readNamespacedKey(), buffer.readInt3UnsignedY()) else null)
}
