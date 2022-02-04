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

import com.valaphee.netcode.mc.nbt.Tag
import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.PacketReader
import com.valaphee.netcode.mcje.network.ServerPlayPacketHandler
import com.valaphee.netcode.mcje.util.NamespacedKey
import com.valaphee.netcode.mcje.world.GameMode
import com.valaphee.netcode.util.safeList

/**
 * @author Kevin Ludwig
 */
class ServerWorldPacket(
    val entityId: Int,
    val hardcore: Boolean,
    val gameMode: GameMode,
    val previousGameMode: GameMode,
    val worldNames: List<NamespacedKey>,
    val dimensionCodec: Tag?,
    val dimension: Tag?,
    val worldName: NamespacedKey,
    val hashedSeed: Long,
    val maximumPlayers: Int,
    val viewDistance: Int,
    val showCoordinates: Boolean,
    val immediateRespawn: Boolean,
    val debugGenerator: Boolean,
    val flatGenerator: Boolean,
) : Packet<ServerPlayPacketHandler> {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeInt(entityId)
        buffer.writeBoolean(hardcore)
        buffer.writeByte(gameMode.ordinal)
        buffer.writeByte(previousGameMode.ordinal)
        buffer.writeVarInt(worldNames.size)
        worldNames.forEach { buffer.writeNamespacedKey(it) }
        buffer.toNbtOutputStream().use { it.writeTag(dimensionCodec) }
        buffer.toNbtOutputStream().use { it.writeTag(dimension) }
        buffer.writeNamespacedKey(worldName)
        buffer.writeLong(hashedSeed)
        buffer.writeVarInt(maximumPlayers)
        buffer.writeVarInt(viewDistance)
        buffer.writeBoolean(!showCoordinates)
        buffer.writeBoolean(!immediateRespawn)
        buffer.writeBoolean(debugGenerator)
        buffer.writeBoolean(flatGenerator)
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.world(this)

    override fun toString() = "ServerWorldPacket(entityId=$entityId, hardcore=$hardcore, gameMode=$gameMode, previousGameMode=$previousGameMode, worldNames=$worldNames, dimensionCodec=$dimensionCodec, dimension=$dimension, worldName=$worldName, hashedSeed=$hashedSeed, maximumPlayers=$maximumPlayers, viewDistance=$viewDistance, showCoordinates=$showCoordinates, immediateRespawn=$immediateRespawn, debugGenerator=$debugGenerator, flatGenerator=$flatGenerator)"
}

/**
 * @author Kevin Ludwig
 */
object ServerWorldPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerWorldPacket(buffer.readInt(), buffer.readBoolean(), GameMode.byIdOrNull(buffer.readByte().toInt())!!, GameMode.byIdOrNull(buffer.readByte().toInt())!!, safeList(buffer.readVarInt()) { buffer.readNamespacedKey() }, buffer.toNbtInputStream().use { it.readTag() }, buffer.toNbtInputStream().use { it.readTag() }, buffer.readNamespacedKey(), buffer.readLong(), buffer.readVarInt(), buffer.readVarInt(), !buffer.readBoolean(), !buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean())
}
