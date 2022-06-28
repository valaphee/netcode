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

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.readValue
import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.PacketReader
import com.valaphee.netcode.mcje.network.ServerPlayPacketHandler
import com.valaphee.netcode.mcje.util.NamespacedKey
import com.valaphee.netcode.mcje.world.Biome
import com.valaphee.netcode.mcje.world.Dimension
import com.valaphee.netcode.mcje.world.GameMode
import com.valaphee.netcode.mcje.world.entity.player.DeathLocation
import com.valaphee.netcode.util.safeList
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream
import java.io.OutputStream

/**
 * @author Kevin Ludwig
 */
class ServerWorldPacket(
    val entityId: Int,
    val hardcore: Boolean,
    val gameMode: GameMode,
    val previousGameMode: GameMode,
    val worldNames: List<NamespacedKey>,
    val dimensionCodec: DimensionCodec,
    val dimension: Dimension,
    val worldName: NamespacedKey,
    val hashedSeed: Long,
    val maximumPlayers: Int,
    val viewDistance: Int,
    val simulationDistance: Int,
    val reducedDebugInfo: Boolean,
    val respawnScreen: Boolean,
    val debugGenerator: Boolean,
    val flatGenerator: Boolean,
    val deathLocation: DeathLocation?
) : Packet<ServerPlayPacketHandler>() {
    data class DimensionCodec(
        @get:JsonProperty("minecraft:dimension_type") val dimensions: Registry<Dimension>,
        @get:JsonProperty("minecraft:worldgen/biome") val biomes: Registry<Biome>
    ) {
        data class Registry<T>(
            @get:JsonProperty("type") val key: String,
            @get:JsonProperty("value") val value: List<Entry<T>>
        ) {
            data class Entry<T>(
                @get:JsonProperty("id") val id: Int,
                @get:JsonProperty("name") val key: String,
                @get:JsonProperty("element") val value: T
            )
        }
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeInt(entityId)
        buffer.writeBoolean(hardcore)
        buffer.writeByte(gameMode.id)
        buffer.writeByte(previousGameMode.id)
        buffer.writeVarInt(worldNames.size)
        worldNames.forEach { buffer.writeNamespacedKey(it) }
        buffer.nbtObjectMapper.writeValue(ByteBufOutputStream(buffer) as OutputStream, dimensionCodec)
        buffer.nbtObjectMapper.writeValue(ByteBufOutputStream(buffer) as OutputStream, dimension)
        buffer.writeNamespacedKey(worldName)
        buffer.writeLong(hashedSeed)
        buffer.writeVarInt(maximumPlayers)
        buffer.writeVarInt(viewDistance)
        if (version >= 758) buffer.writeVarInt(simulationDistance)
        buffer.writeBoolean(reducedDebugInfo)
        buffer.writeBoolean(respawnScreen)
        buffer.writeBoolean(debugGenerator)
        buffer.writeBoolean(flatGenerator)
        if (version >= 759) deathLocation?.let {
            buffer.writeBoolean(true)
            buffer.writeNamespacedKey(it.dimension)
            buffer.writeInt3UnsignedY(it.position)
        } ?: buffer.writeBoolean(false)
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.world(this)

    override fun toString() = "ServerWorldPacket(entityId=$entityId, hardcore=$hardcore, gameMode=$gameMode, previousGameMode=$previousGameMode, worldNames=$worldNames, dimensionCodec=$dimensionCodec, dimension=$dimension, worldName=$worldName, hashedSeed=$hashedSeed, maximumPlayers=$maximumPlayers, viewDistance=$viewDistance, simulationDistance=$simulationDistance, reducedDebugInfo=$reducedDebugInfo, respawnScreen=$respawnScreen, debugGenerator=$debugGenerator, flatGenerator=$flatGenerator, deathLocation=$deathLocation)"
}

/**
 * @author Kevin Ludwig
 */
object ServerWorldPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerWorldPacket(buffer.readInt(), buffer.readBoolean(), checkNotNull(GameMode.byIdOrNull(buffer.readByte().toInt())), checkNotNull(GameMode.byIdOrNull(buffer.readByte().toInt())), safeList(buffer.readVarInt()) { buffer.readNamespacedKey() }, buffer.nbtObjectMapper.readValue(ByteBufInputStream(buffer)), buffer.nbtObjectMapper.readValue(ByteBufInputStream(buffer)), buffer.readNamespacedKey(), buffer.readLong(), buffer.readVarInt(), buffer.readVarInt(), if (version >= 758) buffer.readVarInt() else 0, buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean(), if (version >= 759 && buffer.readBoolean()) DeathLocation(buffer.readNamespacedKey(), buffer.readInt3UnsignedY()) else null)
}
