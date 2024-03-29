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
import com.valaphee.netcode.mcbe.network.Restrict
import com.valaphee.netcode.mcbe.network.Restriction
import com.valaphee.netcode.mcbe.network.V1_16_010
import com.valaphee.netcode.mcbe.network.V1_16_100
import com.valaphee.netcode.mcbe.network.V1_16_210
import com.valaphee.netcode.mcbe.network.V1_17_002
import com.valaphee.netcode.mcbe.network.V1_17_034
import com.valaphee.netcode.mcbe.network.V1_18_002
import com.valaphee.netcode.mcbe.network.V1_18_030
import com.valaphee.netcode.mcbe.network.V1_19_000
import com.valaphee.netcode.mcbe.network.V1_19_010
import com.valaphee.netcode.mcbe.network.V1_19_020
import com.valaphee.netcode.mcbe.world.Difficulty
import com.valaphee.netcode.mcbe.world.Dimension
import com.valaphee.netcode.mcbe.world.Experiment
import com.valaphee.netcode.mcbe.world.GameMode
import com.valaphee.netcode.mcbe.world.GameRule
import com.valaphee.netcode.mcbe.world.block.Block
import com.valaphee.netcode.mcbe.world.entity.player.PlayerPermission
import com.valaphee.netcode.mcbe.world.item.Item
import com.valaphee.netcode.mcbe.world.readExperiment
import com.valaphee.netcode.mcbe.world.readGameRule
import com.valaphee.netcode.mcbe.world.writeExperiment
import com.valaphee.netcode.mcbe.world.writeGameRule
import com.valaphee.netcode.util.LazyList
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class WorldPacket(
    val uniqueEntityId: Long,
    val runtimeEntityId: Long,
    val gameMode: GameMode,
    var position: Float3,
    var rotation: Float2,
    val seed: Long,
    val biomeType: BiomeType,
    val biomeName: String,
    val dimension: Dimension,
    val generatorId: Int,
    val defaultGameMode: GameMode,
    val difficulty: Difficulty,
    var defaultSpawn: Int3,
    val achievementsDisabled: Boolean,
    val worldEditor: Boolean,
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
    val defaultPlayerPermission: PlayerPermission,
    val serverChunkTickRange: Int,
    val behaviorPackLocked: Boolean,
    val resourcePackLocked: Boolean,
    val fromLockedWorldTemplate: Boolean,
    val usingMsaGamertagsOnly: Boolean,
    val fromWorldTemplate: Boolean,
    val worldTemplateOptionLocked: Boolean,
    val onlySpawningV1Villagers: Boolean,
    val disablePersona: Boolean,
    val disableCustomSkin: Boolean,
    val version: String,
    val limitedWorldRadius: Int,
    val limitedWorldHeight: Int,
    val v2Nether: Boolean,
    val experimentalGameplay: Boolean,
    val chatRestriction: ChatRestrictionLevel,
    val disablePlayerInteraction: Boolean,
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
    val blocksChecksum: Long,
    val worldTemplateId: UUID?,
    val clientSideGeneration: Boolean
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

    enum class ChatRestrictionLevel {
        None, Dropped, Disabled
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
        if (version >= V1_18_030) buffer.writeLongLE(seed) else buffer.writeVarInt(seed.toInt())
        if (version >= V1_16_010) {
            buffer.writeShortLE(biomeType.ordinal)
            buffer.writeString(biomeName)
        }
        buffer.writeVarInt(dimension.ordinal)
        buffer.writeVarInt(generatorId)
        buffer.writeVarInt(defaultGameMode.ordinal)
        buffer.writeVarInt(difficulty.ordinal)
        buffer.writeBlockPosition(defaultSpawn)
        buffer.writeBoolean(achievementsDisabled)
        if (version >= V1_19_010) buffer.writeBoolean(worldEditor)
        buffer.writeVarInt(time)
        if (version >= V1_16_010) {
            buffer.writeVarInt(educationEditionOffer.ordinal)
            if (version < V1_16_100) buffer.writeByte(educationModeId)
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
        gameRules.forEach { buffer.writeGameRule(it, version) }
        if (version >= V1_16_100) {
            experiments.let {
                buffer.writeIntLE(it.size)
                it.forEach(buffer::writeExperiment)
            }
            buffer.writeBoolean(experimentsPreviouslyToggled)
        }
        buffer.writeBoolean(bonusChestEnabled)
        buffer.writeBoolean(startingWithMap)
        buffer.writeVarInt(defaultPlayerPermission.ordinal)
        buffer.writeIntLE(serverChunkTickRange)
        buffer.writeBoolean(behaviorPackLocked)
        buffer.writeBoolean(resourcePackLocked)
        buffer.writeBoolean(fromLockedWorldTemplate)
        buffer.writeBoolean(usingMsaGamertagsOnly)
        buffer.writeBoolean(fromWorldTemplate)
        buffer.writeBoolean(worldTemplateOptionLocked)
        buffer.writeBoolean(onlySpawningV1Villagers)
        if (version >= V1_19_020) {
            buffer.writeBoolean(disablePersona)
            buffer.writeBoolean(disableCustomSkin)
        }
        buffer.writeString(this.version)
        if (version >= V1_16_010) {
            buffer.writeIntLE(limitedWorldRadius)
            buffer.writeIntLE(limitedWorldHeight)
            buffer.writeBoolean(v2Nether)
        }
        if (version >= V1_17_034) {
            buffer.writeString("")
            buffer.writeString("")
        }
        if (version >= V1_16_010) {
            buffer.writeBoolean(experimentalGameplay)
            if (version >= V1_16_100 && experimentalGameplay) buffer.writeBoolean(true)
        }
        if (version >= V1_19_020) {
            buffer.writeByte(chatRestriction.ordinal)
            buffer.writeBoolean(disablePlayerInteraction)
        }
        buffer.writeString(worldId)
        buffer.writeString(worldName)
        buffer.writeString(premiumWorldTemplateId)
        buffer.writeBoolean(trial)
        if (version >= V1_16_100) buffer.writeVarInt(movementAuthoritative.ordinal) else buffer.writeBoolean(movementAuthoritative == AuthoritativeMovement.Server)
        if (version >= V1_16_210) {
            buffer.writeVarInt(movementRewindHistory)
            buffer.writeBoolean(blockBreakingServerAuthoritative)
        }
        buffer.writeLongLE(tick)
        buffer.writeVarInt(enchantmentSeed)
        if (version >= V1_16_100) {
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
            if (version >= V1_16_100) buffer.writeBoolean(item.componentBased)
        }
        buffer.writeString(multiplayerCorrelationId)
        if (version >= V1_16_010) buffer.writeBoolean(inventoriesServerAuthoritative)
        if (version >= V1_17_002) buffer.writeString(engineVersion)
        if (version >= V1_18_002) buffer.writeLongLE(blocksChecksum)
        if (version >= V1_19_000) {
            buffer.nbtVarIntObjectMapper.writeValue(ByteBufOutputStream(buffer) as OutputStream, null)
            buffer.writeUuid(worldTemplateId!!)
        }
        if (version >= V1_19_020) buffer.writeBoolean(clientSideGeneration)
    }

    override fun handle(handler: PacketHandler) = handler.world(this)

    override fun toString() = "WorldPacket(uniqueEntityId=$uniqueEntityId, runtimeEntityId=$runtimeEntityId, gameMode=$gameMode, position=$position, rotation=$rotation, seed=$seed, biomeType=$biomeType, biomeName='$biomeName', dimension=$dimension, generatorId=$generatorId, defaultGameMode=$defaultGameMode, difficulty=$difficulty, defaultSpawn=$defaultSpawn, achievementsDisabled=$achievementsDisabled, worldEditor=$worldEditor, time=$time, educationEditionOffer=$educationEditionOffer, educationModeId=$educationModeId, educationFeaturesEnabled=$educationFeaturesEnabled, educationProductId='$educationProductId', rainLevel=$rainLevel, thunderLevel=$thunderLevel, platformLockedContentConfirmed=$platformLockedContentConfirmed, multiplayerGame=$multiplayerGame, broadcastingToLan=$broadcastingToLan, xboxLiveBroadcastMode=$xboxLiveBroadcastMode, platformBroadcastMode=$platformBroadcastMode, commandsEnabled=$commandsEnabled, resourcePacksRequired=$resourcePacksRequired, gameRules=$gameRules, experiments=$experiments, experimentsPreviouslyToggled=$experimentsPreviouslyToggled, bonusChestEnabled=$bonusChestEnabled, startingWithMap=$startingWithMap, defaultplayerPermission$defaultPlayerPermission, serverChunkTickRange=$serverChunkTickRange, behaviorPackLocked=$behaviorPackLocked, resourcePackLocked=$resourcePackLocked, fromLockedWorldTemplate=$fromLockedWorldTemplate, usingMsaGamertagsOnly=$usingMsaGamertagsOnly, fromWorldTemplate=$fromWorldTemplate, worldTemplateOptionLocked=$worldTemplateOptionLocked, onlySpawningV1Villagers=$onlySpawningV1Villagers, disablePersona=$disablePersona, disableCustomSkin=$disableCustomSkin, version='$version', limitedWorldRadius=$limitedWorldRadius, limitedWorldHeight=$limitedWorldHeight, v2Nether=$v2Nether, experimentalGameplay=$experimentalGameplay, chatRestriction=$chatRestriction, disablePlayerInteraction=$disablePlayerInteraction, worldId='$worldId', worldName='$worldName', premiumWorldTemplateId='$premiumWorldTemplateId', trial=$trial, movementAuthoritative=$movementAuthoritative, movementRewindHistory=$movementRewindHistory, blockBreakingServerAuthoritative=$blockBreakingServerAuthoritative, tick=$tick, enchantmentSeed=$enchantmentSeed, blocksData=$blocksData, blocks=$blocks, items=$items, multiplayerCorrelationId='$multiplayerCorrelationId', inventoriesServerAuthoritative=$inventoriesServerAuthoritative, engineVersion='$engineVersion', blocksChecksum=$blocksChecksum, worldTemplateId=$worldTemplateId, clientSideGeneration=$clientSideGeneration)"


    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int): WorldPacket {
            val uniqueEntityId = buffer.readVarLong()
            val runtimeEntityId = buffer.readVarULong()
            val gameMode = GameMode.values()[buffer.readVarInt()]
            val position = buffer.readFloat3()
            val rotation = buffer.readFloat2()
            val seed = if (version >= V1_18_030) buffer.readLongLE() else buffer.readVarInt().toLong()
            val biomeType: WorldPacket.BiomeType
            val biomeName: String
            if (version >= V1_16_010) {
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
            val defaultSpawn = buffer.readBlockPosition()
            val achievementsDisabled = buffer.readBoolean()
            val worldEditor = if (version >= 534) buffer.readBoolean() else false
            val time = buffer.readVarInt()
            val educationEditionOffer: WorldPacket.EducationEditionOffer
            val educationModeId: Int
            val educationFeaturesEnabled: Boolean
            val educationProductId: String?
            if (version >= V1_16_010) {
                educationEditionOffer = WorldPacket.EducationEditionOffer.values()[buffer.readVarInt()]
                educationModeId = if (version < V1_16_100) buffer.readByte().toInt() else 0
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
            val gameRules = LazyList(buffer.readVarUInt()) { buffer.readGameRule(version) }
            val experiments: List<Experiment>
            val experimentsPreviouslyToggled: Boolean
            if (version >= V1_16_100) {
                experiments = LazyList(buffer.readIntLE()) { buffer.readExperiment() }
                experimentsPreviouslyToggled = buffer.readBoolean()
            } else {
                experiments = emptyList()
                experimentsPreviouslyToggled = false
            }
            val bonusChestEnabled = buffer.readBoolean()
            val startingWithMap = buffer.readBoolean()
            val defaultPlayerPermission = PlayerPermission.values()[buffer.readVarInt()]
            val serverChunkTickRange = buffer.readIntLE()
            val behaviorPackLocked = buffer.readBoolean()
            val resourcePackLocked = buffer.readBoolean()
            val fromLockedWorldTemplate = buffer.readBoolean()
            val usingMsaGamertagsOnly = buffer.readBoolean()
            val fromWorldTemplate = buffer.readBoolean()
            val worldTemplateOptionLocked = buffer.readBoolean()
            val onlySpawningV1Villagers = buffer.readBoolean()
            val disablePersona: Boolean
            val disableCustomSkin: Boolean
            if (version >= V1_19_020) {
                disablePersona = buffer.readBoolean()
                disableCustomSkin = buffer.readBoolean()
            } else {
                disablePersona = false
                disableCustomSkin = false
            }
            val version0 = buffer.readString()
            val limitedWorldRadius: Int
            val limitedWorldHeight: Int
            val v2Nether: Boolean
            if (version >= V1_16_010) {
                limitedWorldRadius = buffer.readIntLE()
                limitedWorldHeight = buffer.readIntLE()
                v2Nether = buffer.readBoolean()
            } else {
                limitedWorldRadius = 0
                limitedWorldHeight = 0
                v2Nether = false
            }
            if (version >= V1_17_034) {
                buffer.readString()
                buffer.readString()
            }
            val experimentalGameplay = if (version >= V1_16_010) if (version >= V1_16_100) if (buffer.readBoolean()) buffer.readBoolean() else false else buffer.readBoolean() else false
            val chatRestrictionLevel: WorldPacket.ChatRestrictionLevel
            val disablePlayerInteraction: Boolean
            if (version >= V1_19_020) {
                chatRestrictionLevel = WorldPacket.ChatRestrictionLevel.values()[buffer.readUnsignedByte().toInt()]
                disablePlayerInteraction = buffer.readBoolean()
            } else {
                chatRestrictionLevel = WorldPacket.ChatRestrictionLevel.None
                disablePlayerInteraction = false
            }
            val worldId = buffer.readString()
            val worldName = buffer.readString()
            val premiumWorldTemplateId = buffer.readString()
            val trial = buffer.readBoolean()
            val movementAuthoritative = when {
                version >= V1_16_100 -> WorldPacket.AuthoritativeMovement.values()[buffer.readVarInt()]
                buffer.readBoolean() -> WorldPacket.AuthoritativeMovement.Server
                else -> WorldPacket.AuthoritativeMovement.Client
            }
            val movementRewindHistory: Int
            val blockBreakingServerAuthoritative: Boolean
            if (version >= V1_16_210) {
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
            if (version >= V1_16_100) {
                blocksData = null
                val nbtObjectReader = buffer.nbtVarIntObjectMapper/*.readerFor(Block::class.java).withAttribute("version", version)*/
                blocks = LazyList(buffer.readVarUInt()) {
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
                    this[id.toInt()] = Item(key, version >= V1_16_100 && buffer.readBoolean())
                }
            }
            val multiplayerCorrelationId = buffer.readString()
            val inventoriesServerAuthoritative = buffer.readBoolean()
            val engineVersion = if (version >= V1_17_002) buffer.readString() else ""
            val blocksChecksum = if (version >= V1_18_002) buffer.readLongLE() else 0
            val worldTemplateId: UUID?
            if (version >= V1_19_000) {
                buffer.nbtVarIntObjectMapper.readValue<Any?>(ByteBufInputStream(buffer))
                worldTemplateId = buffer.readUuid()
            } else {
                worldTemplateId = null
            }
            val clientSideGeneration = if (version >= V1_19_020) buffer.readBoolean() else false
            return WorldPacket(uniqueEntityId, runtimeEntityId, gameMode, position, rotation, seed, biomeType, biomeName, dimension, generatorId, defaultGameMode, difficulty, defaultSpawn, achievementsDisabled, worldEditor, time, educationEditionOffer, educationModeId, educationFeaturesEnabled, educationProductId, rainLevel, thunderLevel, platformLockedContentConfirmed, multiplayerGame, broadcastingToLan, xboxLiveBroadcastMode, platformBroadcastMode, commandsEnabled, resourcePacksRequired, gameRules, experiments, experimentsPreviouslyToggled, bonusChestEnabled, startingWithMap, defaultPlayerPermission, serverChunkTickRange, behaviorPackLocked, resourcePackLocked, fromLockedWorldTemplate, usingMsaGamertagsOnly, fromWorldTemplate, worldTemplateOptionLocked, onlySpawningV1Villagers, disablePersona, disableCustomSkin, version0, limitedWorldRadius, limitedWorldHeight, v2Nether, experimentalGameplay, chatRestrictionLevel, disablePlayerInteraction, worldId, worldName, premiumWorldTemplateId, trial, movementAuthoritative, movementRewindHistory, blockBreakingServerAuthoritative, tick, enchantmentSeed, blocksData, blocks, items, multiplayerCorrelationId, inventoriesServerAuthoritative, engineVersion, blocksChecksum, worldTemplateId, clientSideGeneration)
        }
    }
}
