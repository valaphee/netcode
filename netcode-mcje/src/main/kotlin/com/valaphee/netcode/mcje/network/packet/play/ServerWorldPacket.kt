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
import com.valaphee.netcode.mcje.chat.ChatType
import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.ServerPlayPacketHandler
import com.valaphee.netcode.mcje.network.V1_18_2
import com.valaphee.netcode.mcje.network.V1_19_0
import com.valaphee.netcode.mcje.util.NamespacedKey
import com.valaphee.netcode.mcje.world.Biome
import com.valaphee.netcode.mcje.world.Dimension
import com.valaphee.netcode.mcje.world.GameMode
import com.valaphee.netcode.mcje.world.entity.player.DeathLocation
import com.valaphee.netcode.util.LazyList
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
    val registries: Registries,
    val dimensionName: NamespacedKey?,
    val dimension: Dimension?,
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
    data class Registries(
        @JsonProperty("minecraft:dimension_type") val dimensions: Registry<Dimension>,
        @JsonProperty("minecraft:worldgen/biome") val biomes: Registry<Biome>,
        @JsonProperty("minecraft:chat_type") val chatTypes: Registry<ChatType>?
    ) {
        data class Registry<T>(
            @JsonProperty("type") val key: String,
            @JsonProperty("value") val value: List<Entry<T>>
        ) {
            data class Entry<T>(
                @JsonProperty("id") val id: Int,
                @JsonProperty("name") val key: String,
                @JsonProperty("element") val value: T
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
        buffer.nbtObjectMapper.writeValue(ByteBufOutputStream(buffer) as OutputStream, registries)
        if (version >= V1_19_0) buffer.writeNamespacedKey(dimensionName!!) else buffer.nbtObjectMapper.writeValue(ByteBufOutputStream(buffer) as OutputStream, dimension!!)
        buffer.writeNamespacedKey(worldName)
        buffer.writeLong(hashedSeed)
        buffer.writeVarInt(maximumPlayers)
        buffer.writeVarInt(viewDistance)
        if (version >= V1_18_2) buffer.writeVarInt(simulationDistance)
        buffer.writeBoolean(reducedDebugInfo)
        buffer.writeBoolean(respawnScreen)
        buffer.writeBoolean(debugGenerator)
        buffer.writeBoolean(flatGenerator)
        if (version >= V1_19_0) deathLocation?.let {
            buffer.writeBoolean(true)
            buffer.writeNamespacedKey(it.dimension)
            buffer.writeBlockPosition(it.position)
        } ?: buffer.writeBoolean(false)
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.world(this)

    override fun toString() = "ServerWorldPacket(entityId=$entityId, hardcore=$hardcore, gameMode=$gameMode, previousGameMode=$previousGameMode, worldNames=$worldNames, registries=$registries, dimensionName=$dimensionName, dimension=$dimension, worldName=$worldName, hashedSeed=$hashedSeed, maximumPlayers=$maximumPlayers, viewDistance=$viewDistance, simulationDistance=$simulationDistance, reducedDebugInfo=$reducedDebugInfo, respawnScreen=$respawnScreen, debugGenerator=$debugGenerator, flatGenerator=$flatGenerator, deathLocation=$deathLocation)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int): ServerWorldPacket {
            val entityId = buffer.readInt()
            val hardcore = buffer.readBoolean()
            val gameMode = checkNotNull(GameMode.byIdOrNull(buffer.readByte().toInt()))
            val previousGameMode = checkNotNull(GameMode.byIdOrNull(buffer.readByte().toInt()))
            val worldNames = LazyList(buffer.readVarInt()) { buffer.readNamespacedKey() }
            val registries = buffer.nbtObjectMapper.readValue<Registries>(ByteBufInputStream(buffer))
            val dimensionName: NamespacedKey?
            val dimension: Dimension?
            if (version >= V1_19_0) {
                dimensionName = buffer.readNamespacedKey()
                dimension = registries.dimensions.value.find { it.key == dimensionName.toString() }?.value
            } else {
                dimensionName = null
                dimension = buffer.nbtObjectMapper.readValue(ByteBufInputStream(buffer))
            }
            val worldName = buffer.readNamespacedKey()
            val hashedSeed = buffer.readLong()
            val maximumPlayers = buffer.readVarInt()
            val viewDistance = buffer.readVarInt()
            val simulationDistance = if (version >= V1_18_2) buffer.readVarInt() else 5
            val reducedDebugInfo = buffer.readBoolean()
            val respawnScreen = buffer.readBoolean()
            val debugGenerator = buffer.readBoolean()
            val flatGenerator = buffer.readBoolean()
            val deathLocation = /*if (version >= V1_19_0 && buffer.readBoolean()) DeathLocation(buffer.readNamespacedKey(), buffer.readInt3UnsignedY()) else */null
            return ServerWorldPacket(entityId, hardcore, gameMode, previousGameMode, worldNames, registries, dimensionName, dimension, worldName, hashedSeed, maximumPlayers, viewDistance, simulationDistance, reducedDebugInfo, respawnScreen, debugGenerator, flatGenerator, deathLocation)
        }
    }
}
