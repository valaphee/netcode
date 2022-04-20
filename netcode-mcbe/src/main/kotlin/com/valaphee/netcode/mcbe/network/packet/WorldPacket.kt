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

package com.valaphee.netcode.mcbe.network.packet

import com.fasterxml.jackson.module.kotlin.readValue
import com.valaphee.foundry.math.Float2
import com.valaphee.foundry.math.Float3
import com.valaphee.foundry.math.Int3
import com.valaphee.jackson.dataformat.nbt.getStringOrNull
import com.valaphee.netcode.mcbe.network.GamePublishMode
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.PacketReader
import com.valaphee.netcode.mcbe.network.Restrict
import com.valaphee.netcode.mcbe.network.Restriction
import com.valaphee.netcode.mcbe.world.Difficulty
import com.valaphee.netcode.mcbe.world.Dimension
import com.valaphee.netcode.mcbe.world.Experiment
import com.valaphee.netcode.mcbe.world.GameMode
import com.valaphee.netcode.mcbe.world.GameRule
import com.valaphee.netcode.mcbe.world.block.Block
import com.valaphee.netcode.mcbe.world.entity.player.Rank
import com.valaphee.netcode.mcbe.world.item.Item
import com.valaphee.netcode.mcbe.world.readExperiment
import com.valaphee.netcode.mcbe.world.readGameRule
import com.valaphee.netcode.mcbe.world.readGameRulePre440
import com.valaphee.netcode.mcbe.world.writeExperiment
import com.valaphee.netcode.mcbe.world.writeGameRule
import com.valaphee.netcode.mcbe.world.writeGameRulePre440
import com.valaphee.netcode.util.safeList
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import java.io.InputStream
import java.io.OutputStream

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class WorldPacket(
    val uniqueEntityId: Long,
    val runtimeEntityId: Long,
    val gameMode: GameMode,
    var position: Float3, // needed for je-be protocol translation
    var rotation: Float2, // needed for je-be protocol translation
    val seed: Long,
    val biomeType: BiomeType,
    val biomeName: String,
    val dimension: Dimension,
    val generatorId: Int,
    val defaultGameMode: GameMode,
    val difficulty: Difficulty,
    var defaultSpawn: Int3, // needed for je-be protocol translation
    val achievementsDisabled: Boolean,
    val time: Int,
    val educationEditionOffer: EducationEditionOffer,
    val educationModeId: Int,
    val educationFeaturesEnabled: Boolean,
    val educationProductId: String,
    val rainLevel: Float,
    val thunderLevel: Float,
    val platformLockedContentConfirmed: Boolean,
    val multiplayerGame: Boolean,
    val broadcastingToLan: Boolean,
    val xboxLiveBroadcastMode: GamePublishMode,
    val platformBroadcastMode: GamePublishMode,
    val commandsEnabled: Boolean,
    val resourcePacksRequired: Boolean,
    val gameRules: List<GameRule<*>>,
    val experiments: List<Experiment>,
    val experimentsPreviouslyToggled: Boolean,
    val bonusChestEnabled: Boolean,
    val startingWithMap: Boolean,
    val defaultRank: Rank,
    val serverChunkTickRange: Int,
    val behaviorPackLocked: Boolean,
    val resourcePackLocked: Boolean,
    val fromLockedWorldTemplate: Boolean,
    val usingMsaGamertagsOnly: Boolean,
    val fromWorldTemplate: Boolean,
    val worldTemplateOptionLocked: Boolean,
    val onlySpawningV1Villagers: Boolean,
    val version: String,
    val limitedWorldRadius: Int,
    val limitedWorldHeight: Int,
    val v2Nether: Boolean,
    val experimentalGameplay: Boolean,
    val worldId: String,
    val worldName: String,
    val premiumWorldTemplateId: String,
    val trial: Boolean,
    val movementAuthoritative: AuthoritativeMovement,
    val movementRewindHistory: Int,
    val blockBreakingServerAuthoritative: Boolean,
    val tick: Long,
    val enchantmentSeed: Int,
    val blocksData: Any?,
    val blocks: List<Block>?,
    val items: Int2ObjectMap<Item>,
    val multiplayerCorrelationId: String,
    val inventoriesServerAuthoritative: Boolean,
    val engineVersion: String,
    val blocksChecksum: Long
) : Packet() {
    enum class BiomeType {
        Default, UserDefined
    }

    companion object GeneratorId {
        const val FiniteOverworld = 0
        const val Overworld = 1
        const val Superflat = 2
        const val TheNether = 3
        const val TheEnd = 4
    }

    enum class EducationEditionOffer {
        None, EverywhereExceptChina, China
    }

    enum class AuthoritativeMovement {
        Client, Server, ServerWithRewind
    }

    override val id get() = 0x0B

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarLong(uniqueEntityId)
        buffer.writeVarULong(runtimeEntityId)
        buffer.writeVarInt(gameMode.ordinal)
        buffer.writeFloat3(position)
        buffer.writeFloat2(rotation)
        if (version >= 503) buffer.writeLongLE(seed) else buffer.writeVarInt(seed.toInt())
        if (version >= 407) {
            buffer.writeShortLE(biomeType.ordinal)
            buffer.writeString(biomeName)
        }
        buffer.writeVarInt(dimension.ordinal)
        buffer.writeVarInt(generatorId)
        buffer.writeVarInt(defaultGameMode.ordinal)
        buffer.writeVarInt(difficulty.ordinal)
        buffer.writeInt3UnsignedY(defaultSpawn)
        buffer.writeBoolean(achievementsDisabled)
        buffer.writeVarInt(time)
        if (version >= 407) {
            buffer.writeVarInt(educationEditionOffer.ordinal)
            if (version < 419) buffer.writeByte(educationModeId)
            buffer.writeBoolean(educationFeaturesEnabled)
            buffer.writeString(educationProductId)
        } else {
            buffer.writeBoolean(educationFeaturesEnabled)
            buffer.writeVarInt(educationEditionOffer.ordinal)
        }
        buffer.writeFloatLE(rainLevel)
        buffer.writeFloatLE(thunderLevel)
        buffer.writeBoolean(platformLockedContentConfirmed)
        buffer.writeBoolean(multiplayerGame)
        buffer.writeBoolean(broadcastingToLan)
        buffer.writeVarInt(xboxLiveBroadcastMode.ordinal)
        buffer.writeVarInt(platformBroadcastMode.ordinal)
        buffer.writeBoolean(commandsEnabled)
        buffer.writeBoolean(resourcePacksRequired)
        buffer.writeVarUInt(gameRules.size)
        if (version >= 440) gameRules.forEach(buffer::writeGameRule) else gameRules.forEach(buffer::writeGameRulePre440)
        if (version >= 419) {
            experiments.let {
                buffer.writeIntLE(it.size)
                it.forEach(buffer::writeExperiment)
            }
            buffer.writeBoolean(experimentsPreviouslyToggled)
        }
        buffer.writeBoolean(bonusChestEnabled)
        buffer.writeBoolean(startingWithMap)
        buffer.writeVarInt(defaultRank.ordinal)
        buffer.writeIntLE(serverChunkTickRange)
        buffer.writeBoolean(behaviorPackLocked)
        buffer.writeBoolean(resourcePackLocked)
        buffer.writeBoolean(fromLockedWorldTemplate)
        buffer.writeBoolean(usingMsaGamertagsOnly)
        buffer.writeBoolean(fromWorldTemplate)
        buffer.writeBoolean(worldTemplateOptionLocked)
        buffer.writeBoolean(onlySpawningV1Villagers)
        buffer.writeString(this.version)
        if (version >= 407) {
            buffer.writeIntLE(limitedWorldRadius)
            buffer.writeIntLE(limitedWorldHeight)
            if (version >= 465) {
                buffer.writeString("")
                buffer.writeString("")
            }
            buffer.writeBoolean(v2Nether)
            buffer.writeBoolean(experimentalGameplay)
            if (version >= 419 && experimentalGameplay) buffer.writeBoolean(true)
        }
        buffer.writeString(worldId)
        buffer.writeString(worldName)
        buffer.writeString(premiumWorldTemplateId)
        buffer.writeBoolean(trial)
        if (version >= 419) buffer.writeVarInt(movementAuthoritative.ordinal) else buffer.writeBoolean(movementAuthoritative == AuthoritativeMovement.Server)
        if (version >= 428) {
            buffer.writeVarInt(movementRewindHistory)
            buffer.writeBoolean(blockBreakingServerAuthoritative)
        }
        buffer.writeLongLE(tick)
        buffer.writeVarInt(enchantmentSeed)
        if (version >= 419) {
            buffer.writeVarUInt(blocks!!.size)
            val nbtObjectWriter = buffer.nbtVarIntObjectMapper.writerFor(Block::class.java).withAttribute("version", version)
            blocks.forEach {
                buffer.writeString(it.description.key)
                nbtObjectWriter.writeValue(ByteBufOutputStream(buffer) as OutputStream, it)
            }
        } else buffer.nbtVarIntObjectMapper.writeValue(ByteBufOutputStream(buffer) as OutputStream, blocksData)
        buffer.writeVarUInt(items.size)
        items.forEach { (id, item) ->
            buffer.writeString(item.key)
            buffer.writeShortLE(id)
            if (version >= 419) buffer.writeBoolean(item.componentBased)
        }
        buffer.writeString(multiplayerCorrelationId)
        if (version >= 407) buffer.writeBoolean(inventoriesServerAuthoritative)
        if (version >= 440) buffer.writeString(engineVersion)
        if (version >= 475) buffer.writeLongLE(blocksChecksum)
    }

    override fun handle(handler: PacketHandler) = handler.world(this)

    override fun toString() = "WorldPacket(uniqueEntityId=$uniqueEntityId, runtimeEntityId=$runtimeEntityId, gameMode=$gameMode, position=$position, rotation=$rotation, seed=$seed, biomeType=$biomeType, biomeName='$biomeName', dimension=$dimension, generatorId=$generatorId, defaultGameMode=$defaultGameMode, difficulty=$difficulty, defaultSpawn=$defaultSpawn, achievementsDisabled=$achievementsDisabled, time=$time, educationEditionOffer=$educationEditionOffer, educationModeId=$educationModeId, educationFeaturesEnabled=$educationFeaturesEnabled, educationProductId='$educationProductId', rainLevel=$rainLevel, thunderLevel=$thunderLevel, platformLockedContentConfirmed=$platformLockedContentConfirmed, multiplayerGame=$multiplayerGame, broadcastingToLan=$broadcastingToLan, xboxLiveBroadcastMode=$xboxLiveBroadcastMode, platformBroadcastMode=$platformBroadcastMode, commandsEnabled=$commandsEnabled, resourcePacksRequired=$resourcePacksRequired, gameRules=$gameRules, experiments=$experiments, experimentsPreviouslyToggled=$experimentsPreviouslyToggled, bonusChestEnabled=$bonusChestEnabled, startingWithMap=$startingWithMap, defaultRank=$defaultRank, serverChunkTickRange=$serverChunkTickRange, behaviorPackLocked=$behaviorPackLocked, resourcePackLocked=$resourcePackLocked, fromLockedWorldTemplate=$fromLockedWorldTemplate, usingMsaGamertagsOnly=$usingMsaGamertagsOnly, fromWorldTemplate=$fromWorldTemplate, worldTemplateOptionLocked=$worldTemplateOptionLocked, onlySpawningV1Villagers=$onlySpawningV1Villagers, version='$version', limitedWorldRadius=$limitedWorldRadius, limitedWorldHeight=$limitedWorldHeight, v2Nether=$v2Nether, experimentalGameplay=$experimentalGameplay, worldId='$worldId', worldName='$worldName', premiumWorldTemplateId='$premiumWorldTemplateId', trial=$trial, movementAuthoritative=$movementAuthoritative, movementRewindHistory=$movementRewindHistory, blockBreakingServerAuthoritative=$blockBreakingServerAuthoritative, tick=$tick, enchantmentSeed=$enchantmentSeed, blocksData=$blocksData, blocks=$blocks, items=$items, multiplayerCorrelationId='$multiplayerCorrelationId', inventoriesServerAuthoritative=$inventoriesServerAuthoritative, engineVersion='$engineVersion', blocksChecksum=$blocksChecksum)"
}

/**
 * @author Kevin Ludwig
 */
object WorldPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): WorldPacket {
        val uniqueEntityId = buffer.readVarLong()
        val runtimeEntityId = buffer.readVarULong()
        val gameMode = GameMode.values()[buffer.readVarInt()]
        val position = buffer.readFloat3()
        val rotation = buffer.readFloat2()
        val seed = if (version >= 503) buffer.readLongLE() else buffer.readVarInt().toLong()
        val biomeType: WorldPacket.BiomeType
        val biomeName: String
        if (version >= 407) {
            biomeType = WorldPacket.BiomeType.values()[buffer.readShortLE().toInt()]
            biomeName = buffer.readString()
        } else {
            biomeType = WorldPacket.BiomeType.Default
            biomeName = ""
        }
        val dimension = Dimension.values()[buffer.readVarInt()]
        val generatorId = buffer.readVarInt()
        val defaultGameMode = GameMode.values()[buffer.readVarInt()]
        val difficulty = Difficulty.values()[buffer.readVarInt()]
        val defaultSpawn = buffer.readInt3UnsignedY()
        val achievementsDisabled = buffer.readBoolean()
        val time = buffer.readVarInt()
        val educationEditionOffer: WorldPacket.EducationEditionOffer
        val educationModeId: Int
        val educationFeaturesEnabled: Boolean
        val educationProductId: String?
        if (version >= 407) {
            educationEditionOffer = WorldPacket.EducationEditionOffer.values()[buffer.readVarInt()]
            educationModeId = if (version < 419) buffer.readByte().toInt() else 0
            educationFeaturesEnabled = buffer.readBoolean()
            educationProductId = buffer.readString()
        } else {
            educationFeaturesEnabled = buffer.readBoolean()
            educationModeId = 0
            educationEditionOffer = WorldPacket.EducationEditionOffer.values()[buffer.readVarInt()]
            educationProductId = ""
        }
        val rainLevel = buffer.readFloatLE()
        val thunderLevel = buffer.readFloatLE()
        val platformLockedContentConfirmed = buffer.readBoolean()
        val multiplayerGame = buffer.readBoolean()
        val broadcastingToLan = buffer.readBoolean()
        val xboxLiveBroadcastMode = GamePublishMode.values()[buffer.readVarInt()]
        val platformBroadcastMode = GamePublishMode.values()[buffer.readVarInt()]
        val commandsEnabled = buffer.readBoolean()
        val resourcePacksRequired = buffer.readBoolean()
        val gameRules = if (version >= 440) safeList(buffer.readVarUInt()) { buffer.readGameRule() } else safeList(buffer.readVarUInt()) { buffer.readGameRulePre440() }
        val experiments: List<Experiment>
        val experimentsPreviouslyToggled: Boolean
        if (version >= 419) {
            experiments = safeList(buffer.readIntLE()) { buffer.readExperiment() }
            experimentsPreviouslyToggled = buffer.readBoolean()
        } else {
            experiments = emptyList()
            experimentsPreviouslyToggled = false
        }
        val bonusChestEnabled = buffer.readBoolean()
        val startingWithMap = buffer.readBoolean()
        val defaultPlayerPermission = Rank.values()[buffer.readVarInt()]
        val serverChunkTickRange = buffer.readIntLE()
        val behaviorPackLocked = buffer.readBoolean()
        val resourcePackLocked = buffer.readBoolean()
        val fromLockedWorldTemplate = buffer.readBoolean()
        val usingMsaGamertagsOnly = buffer.readBoolean()
        val fromWorldTemplate = buffer.readBoolean()
        val worldTemplateOptionLocked = buffer.readBoolean()
        val onlySpawningV1Villagers = buffer.readBoolean()
        val version0 = buffer.readString()
        val limitedWorldRadius: Int
        val limitedWorldHeight: Int
        val v2Nether: Boolean
        val experimentalGameplay: Boolean
        if (version >= 407) {
            limitedWorldRadius = buffer.readIntLE()
            limitedWorldHeight = buffer.readIntLE()
            v2Nether = buffer.readBoolean()
            if (version >= 465) {
                buffer.readString()
                buffer.readString()
            }
            experimentalGameplay = if (version >= 419) if (buffer.readBoolean()) buffer.readBoolean() else false else buffer.readBoolean()
        } else {
            limitedWorldRadius = 0
            limitedWorldHeight = 0
            v2Nether = false
            experimentalGameplay = false
        }
        val levelId = buffer.readString()
        val worldName = buffer.readString()
        val premiumWorldTemplateId = buffer.readString()
        val trial = buffer.readBoolean()
        val movementAuthoritative = when {
            version >= 419 -> WorldPacket.AuthoritativeMovement.values()[buffer.readVarInt()]
            buffer.readBoolean() -> WorldPacket.AuthoritativeMovement.Server
            else -> WorldPacket.AuthoritativeMovement.Client
        }
        val movementRewindHistory: Int
        val blockBreakingServerAuthoritative: Boolean
        if (version >= 428) {
            movementRewindHistory = buffer.readVarInt()
            blockBreakingServerAuthoritative = buffer.readBoolean()
        } else {
            movementRewindHistory = 0
            blockBreakingServerAuthoritative = false
        }
        val tick = buffer.readLongLE()
        val enchantmentSeed = buffer.readVarInt()
        val blocksData: Any?
        val blocks: List<Block>?
        if (version >= 419) {
            blocksData = null
            val nbtObjectReader = buffer.nbtVarIntObjectMapper/*.readerFor(Block::class.java).withAttribute("version", version)*/
            blocks = safeList(buffer.readVarUInt()) {
                val blockKey = buffer.readString()
                val block = nbtObjectReader.readValue<Map<String, Any?>>(ByteBufInputStream(buffer) as InputStream)
                val blockProperties = (block["properties"] as List<Any?>?)
                Block(Block.Description(blockKey, blockProperties?.associate { (it as Map<String, Any?>).getStringOrNull("name")!! to it["enum"] as List<Any> } ?: emptyMap()))
            }
        } else {
            blocksData = buffer.nbtVarIntObjectMapper.readValue(ByteBufInputStream(buffer))
            blocks = null
        }
        val items = Int2ObjectOpenHashMap<Item>().apply {
            repeat(buffer.readVarUInt()) {
                val key = buffer.readString()
                val id = buffer.readShortLE()
                this[id.toInt()] = Item(key, version >= 419 && buffer.readBoolean())
            }
        }
        val multiplayerCorrelationId = buffer.readString()
        val inventoriesServerAuthoritative = buffer.readBoolean()
        val engineVersion = if (version >= 440) buffer.readString() else ""
        val blocksChecksum = if (version >= 475) buffer.readLongLE() else 0
        return WorldPacket(uniqueEntityId, runtimeEntityId, gameMode, position, rotation, seed, biomeType, biomeName, dimension, generatorId, defaultGameMode, difficulty, defaultSpawn, achievementsDisabled, time, educationEditionOffer, educationModeId, educationFeaturesEnabled, educationProductId, rainLevel, thunderLevel, platformLockedContentConfirmed, multiplayerGame, broadcastingToLan, xboxLiveBroadcastMode, platformBroadcastMode, commandsEnabled, resourcePacksRequired, gameRules, experiments, experimentsPreviouslyToggled, bonusChestEnabled, startingWithMap, defaultPlayerPermission, serverChunkTickRange, behaviorPackLocked, resourcePackLocked, fromLockedWorldTemplate, usingMsaGamertagsOnly, fromWorldTemplate, worldTemplateOptionLocked, onlySpawningV1Villagers, version0, limitedWorldRadius, limitedWorldHeight, v2Nether, experimentalGameplay, levelId, worldName, premiumWorldTemplateId, trial, movementAuthoritative, movementRewindHistory, blockBreakingServerAuthoritative, tick, enchantmentSeed, blocksData, blocks, items, multiplayerCorrelationId, inventoriesServerAuthoritative, engineVersion, blocksChecksum)
    }
}
