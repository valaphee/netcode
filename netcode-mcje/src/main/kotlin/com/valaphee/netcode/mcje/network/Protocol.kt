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

package com.valaphee.netcode.mcje.network

import com.valaphee.netcode.mcje.network.packet.play.ClientAbilitiesPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientActionPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientAdvancementTabPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientBeaconUpdatePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientBlockPlacePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientBlockQueryPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientBookEditPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientChatAcknowledgePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientChatPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientChatPreviewPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientCommandBlockMinecartUpdatePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientCommandBlockUpdatePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientCommandPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientCommandSuggestPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientCraftPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientCreativeInventorySlotPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientCustomPayloadPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientDifficultyLockPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientDifficultyPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientEntityQueryPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientHotbarPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientItemNamePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientItemPickPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientItemUseOnEntityPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientItemUsePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientJigsawBlockUpdatePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientJigsawGeneratePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientKeepAlivePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientLocationPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientPlayerActionPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientPongPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientRecipeBookDisplayPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientRecipeBookPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientRecipeBookStatePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientResourcePackStatusPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientSettingsPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientSignUpdatePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientSpectatePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientStatusPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientSteerBoatPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientSteerPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientStructureBlockUpdatePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientSwingArmPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientTeleportConfirmPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientTradePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientVehicleLocationPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientWindowClickButtonPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientWindowClickPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientWindowClosePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientWindowConfirmPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerAbilitiesPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerActionConfirmPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerAdvancementTabPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerAdvancementsPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerBlockBreakAnimationPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerBlockEntityPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerBlockEventPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerBlockUpdatePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerBlockUpdatesPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerBookOpenPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerBorderPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerBossBarPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerCameraPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerChatDeletePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerChunkPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerChunkPublishPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerChunkUnloadPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerCommandSuggestPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerCraftPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerCustomPayloadPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerDifficultyPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerDisconnectPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntitiesRemovePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityAddPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityAnimationPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityAttachPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityAttributesPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityEffectApplyPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityEffectRevokePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityEquipmentPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityEventPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityHeadRotationPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityLocationPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityMetadataPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityPassengersPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntitySoundPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityTeleportPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityVelocityPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerExperienceOrbAddPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerExperiencePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerExplosionPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerGameStatePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerHealthHungerSaturationPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerHorseWindowOpenPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerHotbarPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerInventoryContentPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerInventorySlotPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerItemCooldownPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerItemTakePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerKeepAlivePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerLocationPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerLookAtPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerMapPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerNamedSoundPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerObjectAddPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerObjectivePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerPaintingAddPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerParticlePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerPingPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerPlayerAddPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerPlayerChatHeaderPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerPlayerChatPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerPlayerChatPreviewPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerPlayerChatPreviewSettingsPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerPlayerCombatEventPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerPlayerListHeaderFooterPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerPlayerListPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerQueryPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerRecipeBookPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerRecipesPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerResourcePackPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerRespawnPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerScorePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerScoreboardDisplayPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerServerDataPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerSignUpdatePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerSimulationDistancePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerSoundPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerSoundStopPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerSpawnPositionPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerStatisticsPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerSystemChatPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerTagsPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerTeamPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerTimePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerTitlePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerTradePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerVehicleLocationPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerViewDistancePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerWindowClosePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerWindowConfirmPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerWindowOpenPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerWindowPropertyPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerWorldEventPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerWorldPacket
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.Object2IntMap
import it.unimi.dsi.fastutil.objects.Object2IntMaps
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import kotlin.reflect.KClass

/**
 * @author Kevin Ludwig
 */
class Protocol(
    val idByPacket: Object2IntMap<KClass<out Packet<out PacketHandler>>>,
    val readerById: Int2ObjectMap<Packet.Reader>,
    val idByReader: Object2IntMap<Packet.Reader>
) {
    class Builder {
        private val packetsAndReadersByVersion = Int2ObjectOpenHashMap<Pair<Object2IntMap<KClass<out Packet<out PacketHandler>>>, Int2ObjectMap<Packet.Reader>>>()

        fun register(`class`: KClass<out Packet<out PacketHandler>>?, reader: Packet.Reader, vararg idByVersion: Pair<Int, Int>) = apply {
            idByVersion.forEach {
                val packetsAndReaders = packetsAndReadersByVersion.getOrPut(it.first) { Pair(Object2IntOpenHashMap(), Int2ObjectOpenHashMap()) }
                `class`?.let { `class` -> packetsAndReaders.first[`class`] = it.second }
                packetsAndReaders.second[it.second] = reader
            }
        }

        fun build(): Int2ObjectMap<Protocol> = Int2ObjectMaps.unmodifiable(Int2ObjectOpenHashMap<Protocol>().apply { packetsAndReadersByVersion.forEach { this[it.key] = Protocol(Object2IntMaps.unmodifiable(it.value.first), Int2ObjectMaps.unmodifiable(it.value.second), Object2IntOpenHashMap(it.value.second.map { it.value to  it.key }.toMap()) ) } })
    }
}

/**
 * @author Kevin Ludwig
 */
enum class Protocols(
    val client: Int2ObjectMap<Protocol>,
    val server: Int2ObjectMap<Protocol>
) {
    Handshake(Int2ObjectMaps.emptyMap(), Int2ObjectMaps.emptyMap()),
    Status(Int2ObjectMaps.emptyMap(), Int2ObjectMaps.emptyMap()),
    Login(Int2ObjectMaps.emptyMap(), Int2ObjectMaps.emptyMap()),
    Play(
        Protocol.Builder()
            .register(ClientTeleportConfirmPacket::class           , ClientTeleportConfirmPacket.Reader           ,                  V1_09_0 to 0x00, V1_09_1 to 0x00, V1_09_2 to 0x00, V1_09_4 to 0x00, V1_10_0 to 0x00, V1_11_0 to 0x00, V1_11_1 to 0x00, V1_12_0 to 0x00, V1_12_1 to 0x00, V1_12_2 to 0x00, V1_13_0 to 0x00, V1_13_1 to 0x00, V1_13_2 to 0x00, V1_14_0 to 0x00, V1_14_1 to 0x00, V1_14_2 to 0x00, V1_14_3 to 0x00, V1_14_4 to 0x00, V1_15_0 to 0x00, V1_15_1 to 0x00, V1_15_2 to 0x00, V1_16_0 to 0x00, V1_16_1 to 0x00, V1_16_2 to 0x00, V1_16_3 to 0x00, V1_16_4 to 0x00, V1_17_0 to 0x00, V1_17_1 to 0x00, V1_18_2 to 0x00, V1_18_2 to 0x00, V1_19_0 to 0x00, V1_19_1 to 0x00, V1_19_3 to 0x00)
            .register(ClientBlockQueryPacket::class                , ClientBlockQueryPacket.Reader                ,                                                                                                                                                                                            V1_13_0 to 0x01, V1_13_1 to 0x01, V1_13_2 to 0x01, V1_14_0 to 0x01, V1_14_1 to 0x01, V1_14_2 to 0x01, V1_14_3 to 0x01, V1_14_4 to 0x01, V1_15_0 to 0x01, V1_15_1 to 0x01, V1_15_2 to 0x01, V1_16_0 to 0x01, V1_16_1 to 0x01, V1_16_2 to 0x01, V1_16_3 to 0x01, V1_16_4 to 0x01, V1_17_0 to 0x01, V1_17_1 to 0x01, V1_18_2 to 0x01, V1_18_2 to 0x01, V1_19_0 to 0x01, V1_19_1 to 0x01, V1_19_3 to 0x01)
            .register(ClientDifficultyPacket::class                , ClientDifficultyPacket.Reader                ,                                                                                                                                                                                                                                               V1_14_0 to 0x02, V1_14_1 to 0x02, V1_14_2 to 0x02, V1_14_3 to 0x02, V1_14_4 to 0x02, V1_15_0 to 0x02, V1_15_1 to 0x02, V1_15_2 to 0x02, V1_16_0 to 0x02, V1_16_1 to 0x02, V1_16_2 to 0x02, V1_16_3 to 0x02, V1_16_4 to 0x02, V1_17_0 to 0x02, V1_17_1 to 0x02, V1_18_2 to 0x02, V1_18_2 to 0x02, V1_19_0 to 0x02, V1_19_1 to 0x02, V1_19_3 to 0x02)
            .register(ClientChatAcknowledgePacket::class           , ClientChatAcknowledgePacket.Reader           ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 V1_19_1 to 0x03, V1_19_3 to 0x03)
            .register(ClientCommandPacket::class                   , ClientCommandPacket.Reader                   ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                V1_19_0 to 0x03, V1_19_1 to 0x04, V1_19_3 to 0x04)
            .register(ClientChatPacket::class                      , ClientChatPacket.Reader                      , V1_08_0 to 0x01, V1_09_0 to 0x02, V1_09_1 to 0x02, V1_09_2 to 0x02, V1_09_4 to 0x02, V1_10_0 to 0x02, V1_11_0 to 0x02, V1_11_1 to 0x02, V1_12_0 to 0x03, V1_12_1 to 0x02, V1_12_2 to 0x02, V1_13_0 to 0x02, V1_13_1 to 0x02, V1_13_2 to 0x02, V1_14_0 to 0x03, V1_14_1 to 0x03, V1_14_2 to 0x03, V1_14_3 to 0x03, V1_14_4 to 0x03, V1_15_0 to 0x03, V1_15_1 to 0x03, V1_15_2 to 0x03, V1_16_0 to 0x03, V1_16_1 to 0x03, V1_16_2 to 0x03, V1_16_3 to 0x03, V1_16_4 to 0x03, V1_17_0 to 0x03, V1_17_1 to 0x03, V1_18_2 to 0x03, V1_18_2 to 0x03, V1_19_0 to 0x04, V1_19_1 to 0x05, V1_19_3 to 0x05)
            .register(ClientChatPreviewPacket::class               , ClientChatPreviewPacket.Reader               ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                V1_19_0 to 0x05, V1_19_1 to 0x06                 )
            .register(ClientStatusPacket::class                    , ClientStatusPacket.Reader                    , V1_08_0 to 0x16, V1_09_0 to 0x03, V1_09_1 to 0x03, V1_09_2 to 0x03, V1_09_4 to 0x03, V1_10_0 to 0x03, V1_11_0 to 0x03, V1_11_1 to 0x03, V1_12_0 to 0x04, V1_12_1 to 0x03, V1_12_2 to 0x03, V1_13_0 to 0x03, V1_13_1 to 0x03, V1_13_2 to 0x03, V1_14_0 to 0x04, V1_14_1 to 0x04, V1_14_2 to 0x04, V1_14_3 to 0x04, V1_14_4 to 0x04, V1_15_0 to 0x04, V1_15_1 to 0x04, V1_15_2 to 0x04, V1_16_0 to 0x04, V1_16_1 to 0x04, V1_16_2 to 0x04, V1_16_3 to 0x04, V1_16_4 to 0x04, V1_17_0 to 0x04, V1_17_1 to 0x04, V1_18_2 to 0x04, V1_18_2 to 0x04, V1_19_0 to 0x06, V1_19_1 to 0x07, V1_19_3 to 0x06)
            .register(ClientSettingsPacket::class                  , ClientSettingsPacket.Reader                  , V1_08_0 to 0x15, V1_09_0 to 0x04, V1_09_1 to 0x04, V1_09_2 to 0x04, V1_09_4 to 0x04, V1_10_0 to 0x04, V1_11_0 to 0x04, V1_11_1 to 0x04, V1_12_0 to 0x05, V1_12_1 to 0x04, V1_12_2 to 0x04, V1_13_0 to 0x04, V1_13_1 to 0x04, V1_13_2 to 0x04, V1_14_0 to 0x05, V1_14_1 to 0x05, V1_14_2 to 0x05, V1_14_3 to 0x05, V1_14_4 to 0x05, V1_15_0 to 0x05, V1_15_1 to 0x05, V1_15_2 to 0x05, V1_16_0 to 0x05, V1_16_1 to 0x05, V1_16_2 to 0x05, V1_16_3 to 0x05, V1_16_4 to 0x05, V1_17_0 to 0x05, V1_17_1 to 0x05, V1_18_2 to 0x05, V1_18_2 to 0x05, V1_19_0 to 0x07, V1_19_1 to 0x08, V1_19_3 to 0x07)
            .register(ClientCommandSuggestPacket::class            , ClientCommandSuggestPacket.Reader            , V1_08_0 to 0x14, V1_09_0 to 0x01, V1_09_1 to 0x01, V1_09_2 to 0x01, V1_09_4 to 0x01, V1_10_0 to 0x01, V1_11_0 to 0x01, V1_11_1 to 0x01, V1_12_0 to 0x02, V1_12_1 to 0x01, V1_12_2 to 0x01, V1_13_0 to 0x05, V1_13_1 to 0x05, V1_13_2 to 0x05, V1_14_0 to 0x06, V1_14_1 to 0x06, V1_14_2 to 0x06, V1_14_3 to 0x06, V1_14_4 to 0x06, V1_15_0 to 0x06, V1_15_1 to 0x06, V1_15_2 to 0x06, V1_16_0 to 0x06, V1_16_1 to 0x06, V1_16_2 to 0x06, V1_16_3 to 0x06, V1_16_4 to 0x06, V1_17_0 to 0x06, V1_17_1 to 0x06, V1_18_2 to 0x06, V1_18_2 to 0x06, V1_19_0 to 0x08, V1_19_1 to 0x09, V1_19_3 to 0x08)
            .register(ClientWindowConfirmPacket::class             , ClientWindowConfirmPacket.Reader             , V1_08_0 to 0x0F, V1_09_0 to 0x05, V1_09_1 to 0x05, V1_09_2 to 0x05, V1_09_4 to 0x05, V1_10_0 to 0x05, V1_11_0 to 0x05, V1_11_1 to 0x05, V1_12_0 to 0x06, V1_12_1 to 0x05, V1_12_2 to 0x05, V1_13_0 to 0x06, V1_13_1 to 0x06, V1_13_2 to 0x06, V1_14_0 to 0x07, V1_14_1 to 0x07, V1_14_2 to 0x07, V1_14_3 to 0x07, V1_14_4 to 0x07, V1_15_0 to 0x07, V1_15_1 to 0x07, V1_15_2 to 0x07, V1_16_0 to 0x07, V1_16_1 to 0x07, V1_16_2 to 0x07, V1_16_3 to 0x07, V1_16_4 to 0x07                                                                                                                       )
            .register(ClientWindowClickButtonPacket::class         , ClientWindowClickButtonPacket.Reader         , V1_08_0 to 0x11, V1_09_0 to 0x06, V1_09_1 to 0x06, V1_09_2 to 0x06, V1_09_4 to 0x06, V1_10_0 to 0x06, V1_11_0 to 0x06, V1_11_1 to 0x06, V1_12_0 to 0x07, V1_12_1 to 0x06, V1_12_2 to 0x06, V1_13_0 to 0x07, V1_13_1 to 0x07, V1_13_2 to 0x07, V1_14_0 to 0x08, V1_14_1 to 0x08, V1_14_2 to 0x08, V1_14_3 to 0x08, V1_14_4 to 0x08, V1_15_0 to 0x08, V1_15_1 to 0x08, V1_15_2 to 0x08, V1_16_0 to 0x08, V1_16_1 to 0x08, V1_16_2 to 0x08, V1_16_3 to 0x08, V1_16_4 to 0x08, V1_17_0 to 0x07, V1_17_1 to 0x07, V1_18_2 to 0x07, V1_18_2 to 0x07, V1_19_0 to 0x09, V1_19_1 to 0x0A, V1_19_3 to 0x09)
            .register(ClientWindowClickPacket::class               , ClientWindowClickPacket.Reader               , V1_08_0 to 0x0E, V1_09_0 to 0x07, V1_09_1 to 0x07, V1_09_2 to 0x07, V1_09_4 to 0x07, V1_10_0 to 0x07, V1_11_0 to 0x07, V1_11_1 to 0x07, V1_12_0 to 0x08, V1_12_1 to 0x07, V1_12_2 to 0x07, V1_13_0 to 0x08, V1_13_1 to 0x08, V1_13_2 to 0x08, V1_14_0 to 0x09, V1_14_1 to 0x09, V1_14_2 to 0x09, V1_14_3 to 0x09, V1_14_4 to 0x09, V1_15_0 to 0x09, V1_15_1 to 0x09, V1_15_2 to 0x09, V1_16_0 to 0x09, V1_16_1 to 0x09, V1_16_2 to 0x09, V1_16_3 to 0x09, V1_16_4 to 0x09, V1_17_0 to 0x08, V1_17_1 to 0x08, V1_18_2 to 0x08, V1_18_2 to 0x08, V1_19_0 to 0x0A, V1_19_1 to 0x0B, V1_19_3 to 0x0A)
            .register(ClientWindowClosePacket::class               , ClientWindowClosePacket.Reader               , V1_08_0 to 0x0D, V1_09_0 to 0x08, V1_09_1 to 0x08, V1_09_2 to 0x08, V1_09_4 to 0x08, V1_10_0 to 0x08, V1_11_0 to 0x08, V1_11_1 to 0x08, V1_12_0 to 0x09, V1_12_1 to 0x08, V1_12_2 to 0x08, V1_13_0 to 0x09, V1_13_1 to 0x09, V1_13_2 to 0x09, V1_14_0 to 0x0A, V1_14_1 to 0x0A, V1_14_2 to 0x0A, V1_14_3 to 0x0A, V1_14_4 to 0x0A, V1_15_0 to 0x0A, V1_15_1 to 0x0A, V1_15_2 to 0x0A, V1_16_0 to 0x0A, V1_16_1 to 0x0A, V1_16_2 to 0x0A, V1_16_3 to 0x0A, V1_16_4 to 0x0A, V1_17_0 to 0x09, V1_17_1 to 0x09, V1_18_2 to 0x09, V1_18_2 to 0x09, V1_19_0 to 0x0B, V1_19_1 to 0x0C, V1_19_3 to 0x0B)
            .register(ClientCustomPayloadPacket::class             , ClientCustomPayloadPacket.Reader             , V1_08_0 to 0x17, V1_09_0 to 0x09, V1_09_1 to 0x09, V1_09_2 to 0x09, V1_09_4 to 0x09, V1_10_0 to 0x09, V1_11_0 to 0x09, V1_11_1 to 0x09, V1_12_0 to 0x0A, V1_12_1 to 0x09, V1_12_2 to 0x09, V1_13_0 to 0x0A, V1_13_1 to 0x0A, V1_13_2 to 0x0A, V1_14_0 to 0x0B, V1_14_1 to 0x0B, V1_14_2 to 0x0B, V1_14_3 to 0x0B, V1_14_4 to 0x0B, V1_15_0 to 0x0B, V1_15_1 to 0x0B, V1_15_2 to 0x0B, V1_16_0 to 0x0B, V1_16_1 to 0x0B, V1_16_2 to 0x0B, V1_16_3 to 0x0B, V1_16_4 to 0x0B, V1_17_0 to 0x0A, V1_17_1 to 0x0A, V1_18_2 to 0x0A, V1_18_2 to 0x0A, V1_19_0 to 0x0C, V1_19_1 to 0x0D, V1_19_3 to 0x0C)
            .register(ClientBookEditPacket::class                  , ClientBookEditPacket.Reader                  ,                                                                                                                                                                                            V1_13_0 to 0x0B, V1_13_1 to 0x0B, V1_13_2 to 0x0B, V1_14_0 to 0x0C, V1_14_1 to 0x0C, V1_14_2 to 0x0C, V1_14_3 to 0x0C, V1_14_4 to 0x0C, V1_15_0 to 0x0C, V1_15_1 to 0x0C, V1_15_2 to 0x0C, V1_16_0 to 0x0C, V1_16_1 to 0x0C, V1_16_2 to 0x0C, V1_16_3 to 0x0C, V1_16_4 to 0x0C, V1_17_0 to 0x0B, V1_17_1 to 0x0B, V1_18_2 to 0x0B, V1_18_2 to 0x0B, V1_19_0 to 0x0D, V1_19_1 to 0x0E, V1_19_3 to 0x0D)
            .register(ClientEntityQueryPacket::class               , ClientEntityQueryPacket.Reader               ,                                                                                                                                                                                            V1_13_0 to 0x0C, V1_13_1 to 0x0C, V1_13_2 to 0x0C, V1_14_0 to 0x0D, V1_14_1 to 0x0D, V1_14_2 to 0x0D, V1_14_3 to 0x0D, V1_14_4 to 0x0D, V1_15_0 to 0x0D, V1_15_1 to 0x0D, V1_15_2 to 0x0D, V1_16_0 to 0x0D, V1_16_1 to 0x0D, V1_16_2 to 0x0D, V1_16_3 to 0x0D, V1_16_4 to 0x0D, V1_17_0 to 0x0C, V1_17_1 to 0x0C, V1_18_2 to 0x0C, V1_18_2 to 0x0C, V1_19_0 to 0x0E, V1_19_1 to 0x0F, V1_19_3 to 0x0E)
            .register(ClientItemUseOnEntityPacket::class           , ClientItemUseOnEntityPacket.Reader           , V1_08_0 to 0x02, V1_09_0 to 0x0A, V1_09_1 to 0x0A, V1_09_2 to 0x0A, V1_09_4 to 0x0A, V1_10_0 to 0x0A, V1_11_0 to 0x0A, V1_11_1 to 0x0A, V1_12_0 to 0x0B, V1_12_1 to 0x0A, V1_12_2 to 0x0A, V1_13_0 to 0x0D, V1_13_1 to 0x0D, V1_13_2 to 0x0D, V1_14_0 to 0x0E, V1_14_1 to 0x0E, V1_14_2 to 0x0E, V1_14_3 to 0x0E, V1_14_4 to 0x0E, V1_15_0 to 0x0E, V1_15_1 to 0x0E, V1_15_2 to 0x0E, V1_16_0 to 0x0E, V1_16_1 to 0x0E, V1_16_2 to 0x0E, V1_16_3 to 0x0E, V1_16_4 to 0x0E, V1_17_0 to 0x0D, V1_17_1 to 0x0D, V1_18_2 to 0x0D, V1_18_2 to 0x0D, V1_19_0 to 0x0F, V1_19_1 to 0x10, V1_19_3 to 0x0F)
            .register(ClientJigsawGeneratePacket::class            , ClientJigsawGeneratePacket.Reader            ,                                                                                                                                                                                                                                                                                                                                                                                       V1_16_0 to 0x0F, V1_16_1 to 0x0F, V1_16_2 to 0x0F, V1_16_3 to 0x0F, V1_16_4 to 0x0F, V1_17_0 to 0x0E, V1_17_1 to 0x0E, V1_18_2 to 0x0E, V1_18_2 to 0x0E, V1_19_0 to 0x10, V1_19_1 to 0x11, V1_19_3 to 0x10)
            .register(ClientKeepAlivePacket::class                 , ClientKeepAlivePacket.Reader                 , V1_08_0 to 0x00, V1_09_0 to 0x0B, V1_09_1 to 0x0B, V1_09_2 to 0x0B, V1_09_4 to 0x0B, V1_10_0 to 0x0B, V1_11_0 to 0x0B, V1_11_1 to 0x0B, V1_12_0 to 0x0C, V1_12_1 to 0x0B, V1_12_2 to 0x0B, V1_13_0 to 0x0E, V1_13_1 to 0x0E, V1_13_2 to 0x0E, V1_14_0 to 0x0F, V1_14_1 to 0x0F, V1_14_2 to 0x0F, V1_14_3 to 0x0F, V1_14_4 to 0x0F, V1_15_0 to 0x0F, V1_15_1 to 0x0F, V1_15_2 to 0x0F, V1_16_0 to 0x10, V1_16_1 to 0x10, V1_16_2 to 0x10, V1_16_3 to 0x10, V1_16_4 to 0x10, V1_17_0 to 0x0F, V1_17_1 to 0x0F, V1_18_2 to 0x0F, V1_18_2 to 0x0F, V1_19_0 to 0x11, V1_19_1 to 0x12, V1_19_3 to 0x11)
            .register(ClientDifficultyLockPacket::class            , ClientDifficultyLockPacket.Reader            ,                                                                                                                                                                                                                                               V1_14_0 to 0x10, V1_14_1 to 0x10, V1_14_2 to 0x10, V1_14_3 to 0x10, V1_14_4 to 0x10, V1_15_0 to 0x10, V1_15_1 to 0x10, V1_15_2 to 0x10, V1_16_0 to 0x11, V1_16_1 to 0x11, V1_16_2 to 0x11, V1_16_3 to 0x11, V1_16_4 to 0x11, V1_17_0 to 0x10, V1_17_1 to 0x10, V1_18_2 to 0x10, V1_18_2 to 0x10, V1_19_0 to 0x12, V1_19_1 to 0x13, V1_19_3 to 0x12)
            .register(ClientLocationPacket::class                  , ClientLocationPacket.PositionReader          , V1_08_0 to 0x04, V1_09_0 to 0x0C, V1_09_1 to 0x0C, V1_09_2 to 0x0C, V1_09_4 to 0x0C, V1_10_0 to 0x0C, V1_11_0 to 0x0C, V1_11_1 to 0x0C, V1_12_0 to 0x0E, V1_12_1 to 0x0D, V1_12_2 to 0x0D, V1_13_0 to 0x10, V1_13_1 to 0x10, V1_13_2 to 0x10, V1_14_0 to 0x11, V1_14_1 to 0x11, V1_14_2 to 0x11, V1_14_3 to 0x11, V1_14_4 to 0x11, V1_15_0 to 0x11, V1_15_1 to 0x11, V1_15_2 to 0x11, V1_16_0 to 0x12, V1_16_1 to 0x12, V1_16_2 to 0x12, V1_16_3 to 0x12, V1_16_4 to 0x12, V1_17_0 to 0x11, V1_17_1 to 0x11, V1_18_2 to 0x11, V1_18_2 to 0x11, V1_19_0 to 0x13, V1_19_1 to 0x14, V1_19_3 to 0x13)
            .register(null                                         , ClientLocationPacket.PositionRotationReader  , V1_08_0 to 0x06, V1_09_0 to 0x0D, V1_09_1 to 0x0D, V1_09_2 to 0x0D, V1_09_4 to 0x0D, V1_10_0 to 0x0D, V1_11_0 to 0x0D, V1_11_1 to 0x0D, V1_12_0 to 0x0F, V1_12_1 to 0x0E, V1_12_2 to 0x0E, V1_13_0 to 0x11, V1_13_1 to 0x11, V1_13_2 to 0x11, V1_14_0 to 0x12, V1_14_1 to 0x12, V1_14_2 to 0x12, V1_14_3 to 0x12, V1_14_4 to 0x12, V1_15_0 to 0x12, V1_15_1 to 0x12, V1_15_2 to 0x12, V1_16_0 to 0x13, V1_16_1 to 0x13, V1_16_2 to 0x13, V1_16_3 to 0x13, V1_16_4 to 0x13, V1_17_0 to 0x12, V1_17_1 to 0x12, V1_18_2 to 0x12, V1_18_2 to 0x12, V1_19_0 to 0x14, V1_19_1 to 0x15, V1_19_3 to 0x14)
            .register(null                                         , ClientLocationPacket.RotationReader          , V1_08_0 to 0x05, V1_09_0 to 0x0E, V1_09_1 to 0x0E, V1_09_2 to 0x0E, V1_09_4 to 0x0E, V1_10_0 to 0x0E, V1_11_0 to 0x0E, V1_11_1 to 0x0E, V1_12_0 to 0x10, V1_12_1 to 0x0F, V1_12_2 to 0x0F, V1_13_0 to 0x12, V1_13_1 to 0x12, V1_13_2 to 0x12, V1_14_0 to 0x13, V1_14_1 to 0x13, V1_14_2 to 0x13, V1_14_3 to 0x13, V1_14_4 to 0x13, V1_15_0 to 0x13, V1_15_1 to 0x13, V1_15_2 to 0x13, V1_16_0 to 0x14, V1_16_1 to 0x14, V1_16_2 to 0x14, V1_16_3 to 0x14, V1_16_4 to 0x14, V1_17_0 to 0x13, V1_17_1 to 0x13, V1_18_2 to 0x13, V1_18_2 to 0x13, V1_19_0 to 0x15, V1_19_1 to 0x16, V1_19_3 to 0x15)
            .register(null                                         , ClientLocationPacket.LocationReader          , V1_08_0 to 0x03, V1_09_0 to 0x0F, V1_09_1 to 0x0F, V1_09_2 to 0x0F, V1_09_4 to 0x0F, V1_10_0 to 0x0F, V1_11_0 to 0x0F, V1_11_1 to 0x0F, V1_12_0 to 0x0D, V1_12_1 to 0x0C, V1_12_2 to 0x0C, V1_13_0 to 0x0F, V1_13_1 to 0x0F, V1_13_2 to 0x0F, V1_14_0 to 0x14, V1_14_1 to 0x14, V1_14_2 to 0x14, V1_14_3 to 0x14, V1_14_4 to 0x14, V1_15_0 to 0x14, V1_15_1 to 0x14, V1_15_2 to 0x14, V1_16_0 to 0x15, V1_16_1 to 0x15, V1_16_2 to 0x15, V1_16_3 to 0x15, V1_16_4 to 0x15, V1_17_0 to 0x14, V1_17_1 to 0x14, V1_18_2 to 0x14, V1_18_2 to 0x14, V1_19_0 to 0x16, V1_19_1 to 0x17, V1_19_3 to 0x16)
            .register(ClientVehicleLocationPacket::class           , ClientVehicleLocationPacket.Reader           ,                  V1_09_0 to 0x10, V1_09_1 to 0x10, V1_09_2 to 0x10, V1_09_4 to 0x10, V1_10_0 to 0x10, V1_11_0 to 0x10, V1_11_1 to 0x10, V1_12_0 to 0x11, V1_12_1 to 0x10, V1_12_2 to 0x10, V1_13_0 to 0x13, V1_13_1 to 0x13, V1_13_2 to 0x13, V1_14_0 to 0x15, V1_14_1 to 0x15, V1_14_2 to 0x15, V1_14_3 to 0x15, V1_14_4 to 0x15, V1_15_0 to 0x15, V1_15_1 to 0x15, V1_15_2 to 0x15, V1_16_0 to 0x16, V1_16_1 to 0x16, V1_16_2 to 0x16, V1_16_3 to 0x16, V1_16_4 to 0x16, V1_17_0 to 0x15, V1_17_1 to 0x15, V1_18_2 to 0x15, V1_18_2 to 0x15, V1_19_0 to 0x17, V1_19_1 to 0x18, V1_19_3 to 0x17)
            .register(ClientSteerBoatPacket::class                 , ClientSteerBoatPacket.Reader                 ,                  V1_09_0 to 0x11, V1_09_1 to 0x11, V1_09_2 to 0x11, V1_09_4 to 0x11, V1_10_0 to 0x11, V1_11_0 to 0x11, V1_11_1 to 0x11, V1_12_0 to 0x12, V1_12_1 to 0x11, V1_12_2 to 0x11, V1_13_0 to 0x14, V1_13_1 to 0x14, V1_13_2 to 0x14, V1_14_0 to 0x16, V1_14_1 to 0x16, V1_14_2 to 0x16, V1_14_3 to 0x16, V1_14_4 to 0x16, V1_15_0 to 0x16, V1_15_1 to 0x16, V1_15_2 to 0x16, V1_16_0 to 0x17, V1_16_1 to 0x17, V1_16_2 to 0x17, V1_16_3 to 0x17, V1_16_4 to 0x17, V1_17_0 to 0x16, V1_17_1 to 0x16, V1_18_2 to 0x16, V1_18_2 to 0x16, V1_19_0 to 0x18, V1_19_1 to 0x19, V1_19_3 to 0x18)
            .register(ClientItemPickPacket::class                  , ClientItemPickPacket.Reader                  ,                                                                                                                                                                                            V1_13_0 to 0x15, V1_13_1 to 0x15, V1_13_2 to 0x15, V1_14_0 to 0x17, V1_14_1 to 0x17, V1_14_2 to 0x17, V1_14_3 to 0x17, V1_14_4 to 0x17, V1_15_0 to 0x17, V1_15_1 to 0x17, V1_15_2 to 0x17, V1_16_0 to 0x18, V1_16_1 to 0x18, V1_16_2 to 0x18, V1_16_3 to 0x18, V1_16_4 to 0x18, V1_17_0 to 0x17, V1_17_1 to 0x17, V1_18_2 to 0x17, V1_18_2 to 0x17, V1_19_0 to 0x19, V1_19_1 to 0x1A, V1_19_3 to 0x19)
            .register(ClientCraftPacket::class                     , ClientCraftPacket.Reader                     ,                                                                                                                                         V1_12_0 to 0x12, V1_12_1 to 0x12, V1_12_2 to 0x12, V1_13_0 to 0x16, V1_13_1 to 0x16, V1_13_2 to 0x16, V1_14_0 to 0x18, V1_14_1 to 0x18, V1_14_2 to 0x18, V1_14_3 to 0x18, V1_14_4 to 0x18, V1_15_0 to 0x18, V1_15_1 to 0x18, V1_15_2 to 0x18, V1_16_0 to 0x19, V1_16_1 to 0x19, V1_16_2 to 0x19, V1_16_3 to 0x19, V1_16_4 to 0x19, V1_17_0 to 0x18, V1_17_1 to 0x18, V1_18_2 to 0x18, V1_18_2 to 0x18, V1_19_0 to 0x1A, V1_19_1 to 0x1B, V1_19_3 to 0x1A)
            .register(ClientAbilitiesPacket::class                 , ClientAbilitiesPacket.Reader                 , V1_08_0 to 0x13, V1_09_0 to 0x12, V1_09_1 to 0x12, V1_09_2 to 0x12, V1_09_4 to 0x12, V1_10_0 to 0x12, V1_11_0 to 0x12, V1_11_1 to 0x12, V1_12_0 to 0x13, V1_12_1 to 0x13, V1_12_2 to 0x13, V1_13_0 to 0x17, V1_13_1 to 0x17, V1_13_2 to 0x17, V1_14_0 to 0x19, V1_14_1 to 0x19, V1_14_2 to 0x19, V1_14_3 to 0x19, V1_14_4 to 0x19, V1_15_0 to 0x19, V1_15_1 to 0x19, V1_15_2 to 0x19, V1_16_0 to 0x1A, V1_16_1 to 0x1A, V1_16_2 to 0x1A, V1_16_3 to 0x1A, V1_16_4 to 0x1A, V1_17_0 to 0x19, V1_17_1 to 0x19, V1_18_2 to 0x19, V1_18_2 to 0x19, V1_19_0 to 0x1B, V1_19_1 to 0x1C, V1_19_3 to 0x1B)
            .register(ClientActionPacket::class                    , ClientActionPacket.Reader                    , V1_08_0 to 0x07, V1_09_0 to 0x13, V1_09_1 to 0x13, V1_09_2 to 0x13, V1_09_4 to 0x13, V1_10_0 to 0x13, V1_11_0 to 0x13, V1_11_1 to 0x13, V1_12_0 to 0x14, V1_12_1 to 0x14, V1_12_2 to 0x14, V1_13_0 to 0x18, V1_13_1 to 0x18, V1_13_2 to 0x18, V1_14_0 to 0x1A, V1_14_1 to 0x1A, V1_14_2 to 0x1A, V1_14_3 to 0x1A, V1_14_4 to 0x1A, V1_15_0 to 0x1A, V1_15_1 to 0x1A, V1_15_2 to 0x1A, V1_16_0 to 0x1B, V1_16_1 to 0x1B, V1_16_2 to 0x1B, V1_16_3 to 0x1B, V1_16_4 to 0x1B, V1_17_0 to 0x1A, V1_17_1 to 0x1A, V1_18_2 to 0x1A, V1_18_2 to 0x1A, V1_19_0 to 0x1C, V1_19_1 to 0x1D, V1_19_3 to 0x1C)
            .register(ClientPlayerActionPacket::class              , ClientPlayerActionPacket.Reader              , V1_08_0 to 0x0B, V1_09_0 to 0x14, V1_09_1 to 0x14, V1_09_2 to 0x14, V1_09_4 to 0x14, V1_10_0 to 0x14, V1_11_0 to 0x14, V1_11_1 to 0x14, V1_12_0 to 0x15, V1_12_1 to 0x15, V1_12_2 to 0x15, V1_13_0 to 0x19, V1_13_1 to 0x19, V1_13_2 to 0x19, V1_14_0 to 0x1B, V1_14_1 to 0x1B, V1_14_2 to 0x1B, V1_14_3 to 0x1B, V1_14_4 to 0x1B, V1_15_0 to 0x1B, V1_15_1 to 0x1B, V1_15_2 to 0x1B, V1_16_0 to 0x1C, V1_16_1 to 0x1C, V1_16_2 to 0x1C, V1_16_3 to 0x1C, V1_16_4 to 0x1C, V1_17_0 to 0x1B, V1_17_1 to 0x1B, V1_18_2 to 0x1B, V1_18_2 to 0x1B, V1_19_0 to 0x1D, V1_19_1 to 0x1E, V1_19_3 to 0x1D)
            .register(ClientSteerPacket::class                     , ClientSteerPacket.Reader                     , V1_08_0 to 0x0C, V1_09_0 to 0x15, V1_09_1 to 0x15, V1_09_2 to 0x15, V1_09_4 to 0x15, V1_10_0 to 0x15, V1_11_0 to 0x15, V1_11_1 to 0x15, V1_12_0 to 0x16, V1_12_1 to 0x16, V1_12_2 to 0x16, V1_13_0 to 0x1A, V1_13_1 to 0x1A, V1_13_2 to 0x1A, V1_14_0 to 0x1C, V1_14_1 to 0x1C, V1_14_2 to 0x1C, V1_14_3 to 0x1C, V1_14_4 to 0x1C, V1_15_0 to 0x1C, V1_15_1 to 0x1C, V1_15_2 to 0x1C, V1_16_0 to 0x1D, V1_16_1 to 0x1D, V1_16_2 to 0x1D, V1_16_3 to 0x1D, V1_16_4 to 0x1D, V1_17_0 to 0x1C, V1_17_1 to 0x1C, V1_18_2 to 0x1C, V1_18_2 to 0x1C, V1_19_0 to 0x1E, V1_19_1 to 0x1F, V1_19_3 to 0x1E)
            .register(ClientPongPacket::class                      , ClientPongPacket.Reader                      ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                            V1_17_0 to 0x1D, V1_17_1 to 0x1D, V1_18_0 to 0x1D, V1_18_2 to 0x1D, V1_19_0 to 0x1F, V1_19_1 to 0x20, V1_19_3 to 0x1F)
            .register(ClientRecipeBookPacket::class                , ClientRecipeBookPacket.Reader                ,                                                                                                                                         V1_12_0 to 0x17, V1_12_1 to 0x17, V1_12_2 to 0x17, V1_13_0 to 0x1B, V1_13_1 to 0x1B, V1_13_2 to 0x1B, V1_14_0 to 0x1D, V1_14_1 to 0x1D, V1_14_2 to 0x1D, V1_14_3 to 0x1D, V1_14_4 to 0x1D, V1_15_0 to 0x1D, V1_15_1 to 0x1D, V1_15_2 to 0x1D, V1_16_0 to 0x1E, V1_16_0 to 0x1E                                                                                                                                                                          )
            .register(ClientRecipeBookStatePacket::class           , ClientRecipeBookStatePacket.Reader           ,                                                                                                                                                                                                                                                                                                                                                                                                                         V1_16_2 to 0x1E, V1_16_3 to 0x1E, V1_16_4 to 0x1E, V1_17_0 to 0x1E, V1_17_1 to 0x1E, V1_18_2 to 0x1E, V1_18_2 to 0x1E, V1_19_0 to 0x20, V1_19_1 to 0x21, V1_19_3 to 0x21)
            .register(ClientRecipeBookDisplayPacket::class         , ClientRecipeBookDisplayPacket.Reader         ,                                                                                                                                                                                                                                                                                                                                                                                                                         V1_16_2 to 0x1F, V1_16_3 to 0x1F, V1_16_4 to 0x1F, V1_17_0 to 0x1F, V1_17_1 to 0x1F, V1_18_2 to 0x1F, V1_18_2 to 0x1F, V1_19_0 to 0x21, V1_19_1 to 0x22, V1_19_3 to 0x22)
            .register(ClientItemNamePacket::class                  , ClientItemNamePacket.Reader                  ,                                                                                                                                                                                            V1_13_0 to 0x1C, V1_13_1 to 0x1C, V1_13_2 to 0x1C, V1_14_0 to 0x1E, V1_14_1 to 0x1E, V1_14_2 to 0x1E, V1_14_3 to 0x1E, V1_14_4 to 0x1E, V1_15_0 to 0x1E, V1_15_1 to 0x1E, V1_15_2 to 0x1E, V1_16_0 to 0x1F, V1_16_1 to 0x1F, V1_16_2 to 0x20, V1_16_3 to 0x20, V1_16_4 to 0x20, V1_17_0 to 0x20, V1_17_1 to 0x20, V1_18_2 to 0x20, V1_18_2 to 0x20, V1_19_0 to 0x22, V1_19_1 to 0x23, V1_19_3 to 0x23)
            .register(ClientResourcePackStatusPacket::class        , ClientResourcePackStatusPacket.Reader        , V1_08_0 to 0x19, V1_09_0 to 0x16, V1_09_1 to 0x16, V1_09_2 to 0x16, V1_09_4 to 0x16, V1_10_0 to 0x16, V1_11_0 to 0x16, V1_11_1 to 0x16, V1_12_0 to 0x18, V1_12_1 to 0x18, V1_12_2 to 0x18, V1_13_0 to 0x1D, V1_13_1 to 0x1D, V1_13_2 to 0x1D, V1_14_0 to 0x1F, V1_14_1 to 0x1F, V1_14_2 to 0x1F, V1_14_3 to 0x1F, V1_14_4 to 0x1F, V1_15_0 to 0x1F, V1_15_1 to 0x1F, V1_15_2 to 0x1F, V1_16_0 to 0x20, V1_16_1 to 0x20, V1_16_2 to 0x21, V1_16_3 to 0x21, V1_16_4 to 0x21, V1_17_0 to 0x21, V1_17_1 to 0x21, V1_18_2 to 0x21, V1_18_2 to 0x21, V1_19_0 to 0x23, V1_19_1 to 0x24, V1_19_3 to 0x24)
            .register(ClientAdvancementTabPacket::class            , ClientAdvancementTabPacket.Reader            ,                                                                                                                                         V1_12_0 to 0x19, V1_12_1 to 0x19, V1_12_2 to 0x19, V1_13_0 to 0x1E, V1_13_1 to 0x1E, V1_13_2 to 0x1E, V1_14_0 to 0x20, V1_14_1 to 0x20, V1_14_2 to 0x20, V1_14_3 to 0x20, V1_14_4 to 0x20, V1_15_0 to 0x20, V1_15_1 to 0x20, V1_15_2 to 0x20, V1_16_0 to 0x21, V1_16_1 to 0x21, V1_16_2 to 0x22, V1_16_3 to 0x22, V1_16_4 to 0x22, V1_17_0 to 0x22, V1_17_1 to 0x22, V1_18_2 to 0x22, V1_18_2 to 0x22, V1_19_0 to 0x24, V1_19_1 to 0x25, V1_19_3 to 0x25)
            .register(ClientTradePacket::class                     , ClientTradePacket.Reader                     ,                                                                                                                                                                                            V1_13_0 to 0x1F, V1_13_1 to 0x1F, V1_13_2 to 0x1F, V1_14_0 to 0x21, V1_14_1 to 0x21, V1_14_2 to 0x21, V1_14_3 to 0x21, V1_14_4 to 0x21, V1_15_0 to 0x21, V1_15_1 to 0x21, V1_15_2 to 0x21, V1_16_0 to 0x22, V1_16_1 to 0x22, V1_16_2 to 0x23, V1_16_3 to 0x23, V1_16_4 to 0x23, V1_17_0 to 0x23, V1_17_1 to 0x23, V1_18_2 to 0x23, V1_18_2 to 0x23, V1_19_0 to 0x25, V1_19_1 to 0x26, V1_19_3 to 0x26)
            .register(ClientBeaconUpdatePacket::class              , ClientBeaconUpdatePacket.Reader              ,                                                                                                                                                                                            V1_13_0 to 0x20, V1_13_1 to 0x20, V1_13_2 to 0x20, V1_14_0 to 0x22, V1_14_1 to 0x22, V1_14_2 to 0x22, V1_14_3 to 0x22, V1_14_4 to 0x22, V1_15_0 to 0x22, V1_15_1 to 0x22, V1_15_2 to 0x22, V1_16_0 to 0x23, V1_16_1 to 0x23, V1_16_2 to 0x24, V1_16_3 to 0x24, V1_16_4 to 0x24, V1_17_0 to 0x24, V1_17_1 to 0x24, V1_18_2 to 0x24, V1_18_2 to 0x24, V1_19_0 to 0x26, V1_19_1 to 0x27, V1_19_3 to 0x27)
            .register(ClientHotbarPacket::class                    , ClientHotbarPacket.Reader                    , V1_08_0 to 0x09, V1_09_0 to 0x17, V1_09_1 to 0x17, V1_09_2 to 0x17, V1_09_4 to 0x17, V1_10_0 to 0x17, V1_11_0 to 0x17, V1_11_1 to 0x17, V1_12_0 to 0x1A, V1_12_1 to 0x1A, V1_12_2 to 0x1A, V1_13_0 to 0x21, V1_13_1 to 0x21, V1_13_2 to 0x21, V1_14_0 to 0x23, V1_14_1 to 0x23, V1_14_2 to 0x23, V1_14_3 to 0x23, V1_14_4 to 0x23, V1_15_0 to 0x23, V1_15_1 to 0x23, V1_15_2 to 0x23, V1_16_0 to 0x24, V1_16_1 to 0x24, V1_16_2 to 0x25, V1_16_3 to 0x25, V1_16_4 to 0x25, V1_17_0 to 0x25, V1_17_1 to 0x25, V1_18_2 to 0x25, V1_18_2 to 0x25, V1_19_0 to 0x27, V1_19_1 to 0x28, V1_19_3 to 0x28)
            .register(ClientCommandBlockUpdatePacket::class        , ClientCommandBlockUpdatePacket.Reader        ,                                                                                                                                                                                            V1_13_0 to 0x22, V1_13_1 to 0x22, V1_13_2 to 0x22, V1_14_0 to 0x24, V1_14_1 to 0x24, V1_14_2 to 0x24, V1_14_3 to 0x24, V1_14_4 to 0x24, V1_15_0 to 0x24, V1_15_1 to 0x24, V1_15_2 to 0x24, V1_16_0 to 0x25, V1_16_1 to 0x25, V1_16_2 to 0x26, V1_16_3 to 0x26, V1_16_4 to 0x26, V1_17_0 to 0x26, V1_17_1 to 0x26, V1_18_2 to 0x26, V1_18_2 to 0x26, V1_19_0 to 0x28, V1_19_1 to 0x29, V1_19_3 to 0x29)
            .register(ClientCommandBlockMinecartUpdatePacket::class, ClientCommandBlockMinecartUpdatePacket.Reader,                                                                                                                                                                                            V1_13_0 to 0x23, V1_13_1 to 0x23, V1_13_2 to 0x23, V1_14_0 to 0x25, V1_14_1 to 0x25, V1_14_2 to 0x25, V1_14_3 to 0x25, V1_14_4 to 0x25, V1_15_0 to 0x25, V1_15_1 to 0x25, V1_15_2 to 0x25, V1_16_0 to 0x26, V1_16_1 to 0x26, V1_16_2 to 0x27, V1_16_3 to 0x27, V1_16_4 to 0x27, V1_17_0 to 0x27, V1_17_1 to 0x27, V1_18_2 to 0x27, V1_18_2 to 0x27, V1_19_0 to 0x29, V1_19_1 to 0x2A, V1_19_3 to 0x2A)
            .register(ClientCreativeInventorySlotPacket::class     , ClientCreativeInventorySlotPacket.Reader     , V1_08_0 to 0x10, V1_09_0 to 0x18, V1_09_1 to 0x18, V1_09_2 to 0x18, V1_09_4 to 0x18, V1_10_0 to 0x18, V1_11_0 to 0x18, V1_11_1 to 0x18, V1_12_0 to 0x1B, V1_12_1 to 0x1B, V1_12_2 to 0x1B, V1_13_0 to 0x24, V1_13_1 to 0x24, V1_13_2 to 0x24, V1_14_0 to 0x26, V1_14_1 to 0x26, V1_14_2 to 0x26, V1_14_3 to 0x26, V1_14_4 to 0x26, V1_15_0 to 0x26, V1_15_1 to 0x26, V1_15_2 to 0x26, V1_16_0 to 0x27, V1_16_1 to 0x27, V1_16_2 to 0x28, V1_16_3 to 0x28, V1_16_4 to 0x28, V1_17_0 to 0x28, V1_17_1 to 0x28, V1_18_2 to 0x28, V1_18_2 to 0x28, V1_19_0 to 0x2A, V1_19_1 to 0x2B, V1_19_3 to 0x2B)
            .register(ClientJigsawBlockUpdatePacket::class         , ClientJigsawBlockUpdatePacket.Reader         ,                                                                                                                                                                                                                                               V1_14_0 to 0x27, V1_14_1 to 0x27, V1_14_2 to 0x27, V1_14_3 to 0x27, V1_14_4 to 0x27, V1_15_0 to 0x27, V1_15_1 to 0x27, V1_15_2 to 0x27, V1_16_0 to 0x28, V1_16_1 to 0x28, V1_16_2 to 0x29, V1_16_3 to 0x29, V1_16_4 to 0x29, V1_17_0 to 0x29, V1_17_1 to 0x29, V1_18_2 to 0x29, V1_18_2 to 0x29, V1_19_0 to 0x2B, V1_19_1 to 0x2C, V1_19_3 to 0x2C)
            .register(ClientStructureBlockUpdatePacket::class      , ClientStructureBlockUpdatePacket.Reader      ,                                                                                                                                                                                            V1_13_0 to 0x25, V1_13_1 to 0x25, V1_13_2 to 0x25, V1_14_0 to 0x28, V1_14_1 to 0x28, V1_14_2 to 0x28, V1_14_3 to 0x28, V1_14_4 to 0x28, V1_15_0 to 0x28, V1_15_1 to 0x28, V1_15_2 to 0x28, V1_16_0 to 0x29, V1_16_1 to 0x29, V1_16_2 to 0x2A, V1_16_3 to 0x2A, V1_16_4 to 0x2A, V1_17_0 to 0x2A, V1_17_1 to 0x2A, V1_18_2 to 0x2A, V1_18_2 to 0x2A, V1_19_0 to 0x2C, V1_19_1 to 0x2D, V1_19_3 to 0x2D)
            .register(ClientSignUpdatePacket::class                , ClientSignUpdatePacket.Reader                , V1_08_0 to 0x12, V1_09_0 to 0x19, V1_09_1 to 0x19, V1_09_2 to 0x19, V1_09_4 to 0x19, V1_10_0 to 0x19, V1_11_0 to 0x19, V1_11_1 to 0x19, V1_12_0 to 0x1C, V1_12_1 to 0x1C, V1_12_2 to 0x1C, V1_13_0 to 0x26, V1_13_1 to 0x26, V1_13_2 to 0x26, V1_14_0 to 0x29, V1_14_1 to 0x29, V1_14_2 to 0x29, V1_14_3 to 0x29, V1_14_4 to 0x29, V1_15_0 to 0x29, V1_15_1 to 0x29, V1_15_2 to 0x29, V1_16_0 to 0x2A, V1_16_1 to 0x2A, V1_16_2 to 0x2B, V1_16_3 to 0x2B, V1_16_4 to 0x2B, V1_17_0 to 0x2B, V1_17_1 to 0x2B, V1_18_2 to 0x2B, V1_18_2 to 0x2B, V1_19_0 to 0x2D, V1_19_1 to 0x2E, V1_19_3 to 0x2E)
            .register(ClientSwingArmPacket::class                  , ClientSwingArmPacket.Reader                  , V1_08_0 to 0x0A, V1_09_0 to 0x1A, V1_09_1 to 0x1A, V1_09_2 to 0x1A, V1_09_4 to 0x1A, V1_10_0 to 0x1A, V1_11_0 to 0x1A, V1_11_1 to 0x1A, V1_12_0 to 0x1D, V1_12_1 to 0x1D, V1_12_2 to 0x1D, V1_13_0 to 0x27, V1_13_1 to 0x27, V1_13_2 to 0x27, V1_14_0 to 0x2A, V1_14_1 to 0x2A, V1_14_2 to 0x2A, V1_14_3 to 0x2A, V1_14_4 to 0x2A, V1_15_0 to 0x2A, V1_15_1 to 0x2A, V1_15_2 to 0x2A, V1_16_0 to 0x2B, V1_16_1 to 0x2B, V1_16_2 to 0x2C, V1_16_3 to 0x2C, V1_16_4 to 0x2C, V1_17_0 to 0x2C, V1_17_1 to 0x2C, V1_18_2 to 0x2C, V1_18_2 to 0x2C, V1_19_0 to 0x2E, V1_19_1 to 0x2F, V1_19_3 to 0x2F)
            .register(ClientSpectatePacket::class                  , ClientSpectatePacket.Reader                  , V1_08_0 to 0x18, V1_09_0 to 0x1B, V1_09_1 to 0x1B, V1_09_2 to 0x1B, V1_09_4 to 0x1B, V1_10_0 to 0x1B, V1_11_0 to 0x1B, V1_11_1 to 0x1B, V1_12_0 to 0x1E, V1_12_1 to 0x1E, V1_12_2 to 0x1E, V1_13_0 to 0x28, V1_13_1 to 0x28, V1_13_2 to 0x28, V1_14_0 to 0x2B, V1_14_1 to 0x2B, V1_14_2 to 0x2B, V1_14_3 to 0x2B, V1_14_4 to 0x2B, V1_15_0 to 0x2B, V1_15_1 to 0x2B, V1_15_2 to 0x2B, V1_16_0 to 0x2C, V1_16_1 to 0x2C, V1_16_2 to 0x2D, V1_16_3 to 0x2D, V1_16_4 to 0x2D, V1_17_0 to 0x2D, V1_17_1 to 0x2D, V1_18_2 to 0x2D, V1_18_2 to 0x2D, V1_19_0 to 0x2F, V1_19_1 to 0x30, V1_19_3 to 0x30)
            .register(ClientBlockPlacePacket::class                , ClientBlockPlacePacket.Reader                , V1_08_0 to 0x08, V1_09_0 to 0x1C, V1_09_1 to 0x1C, V1_09_2 to 0x1C, V1_09_4 to 0x1C, V1_10_0 to 0x1C, V1_11_0 to 0x1C, V1_11_1 to 0x1C, V1_12_0 to 0x1F, V1_12_1 to 0x1F, V1_12_2 to 0x1F, V1_13_0 to 0x29, V1_13_1 to 0x29, V1_13_2 to 0x29, V1_14_0 to 0x2C, V1_14_1 to 0x2C, V1_14_2 to 0x2C, V1_14_3 to 0x2C, V1_14_4 to 0x2C, V1_15_0 to 0x2C, V1_15_1 to 0x2C, V1_15_2 to 0x2C, V1_16_0 to 0x2D, V1_16_1 to 0x2D, V1_16_2 to 0x2E, V1_16_3 to 0x2E, V1_16_4 to 0x2E, V1_17_0 to 0x2E, V1_17_1 to 0x2E, V1_18_2 to 0x2E, V1_18_2 to 0x2E, V1_19_0 to 0x30, V1_19_1 to 0x31, V1_19_3 to 0x31)
            .register(ClientItemUsePacket::class                   , ClientItemUsePacket.Reader                   ,                  V1_09_0 to 0x1D, V1_09_1 to 0x1D, V1_09_2 to 0x1D, V1_09_4 to 0x1D, V1_10_0 to 0x1D, V1_11_0 to 0x1D, V1_11_1 to 0x1D, V1_12_0 to 0x20, V1_12_1 to 0x20, V1_12_2 to 0x20, V1_13_0 to 0x2A, V1_13_1 to 0x2A, V1_13_2 to 0x2A, V1_14_0 to 0x2D, V1_14_1 to 0x2D, V1_14_2 to 0x2D, V1_14_3 to 0x2D, V1_14_4 to 0x2D, V1_15_0 to 0x2D, V1_15_1 to 0x2D, V1_15_2 to 0x2D, V1_16_0 to 0x2E, V1_16_1 to 0x2E, V1_16_2 to 0x2F, V1_16_3 to 0x2F, V1_16_4 to 0x2F, V1_17_0 to 0x2F, V1_17_1 to 0x2F, V1_18_2 to 0x2F, V1_18_2 to 0x2F, V1_19_0 to 0x31, V1_19_1 to 0x32, V1_19_3 to 0x32)
            .build(),
        Protocol.Builder()
            .register(ServerObjectAddPacket::class                , ServerObjectAddPacket.Reader                , V1_16_4 to 0x00, V1_18_2 to 0x00                                  )
            .register(ServerEntityAddPacket::class                , ServerEntityAddPacket.Reader                , V1_16_4 to 0x02, V1_18_2 to 0x02, V1_19_0 to 0x00, V1_19_1 to 0x00)
            .register(ServerExperienceOrbAddPacket::class         , ServerExperienceOrbAddPacket.Reader         , V1_16_4 to 0x01, V1_18_2 to 0x01, V1_19_0 to 0x01, V1_19_1 to 0x01)
            .register(ServerPaintingAddPacket::class              , ServerPaintingAddPacket.Reader              , V1_16_4 to 0x03, V1_18_2 to 0x03                                  )
            .register(ServerPlayerAddPacket::class                , ServerPlayerAddPacket.Reader                , V1_16_4 to 0x04, V1_18_2 to 0x04, V1_19_0 to 0x02, V1_19_1 to 0x02)
            .register(ServerEntityAnimationPacket::class          , ServerEntityAnimationPacket.Reader          , V1_16_4 to 0x05, V1_18_2 to 0x06, V1_19_0 to 0x03, V1_19_1 to 0x03)
            .register(ServerStatisticsPacket::class               , ServerStatisticsPacket.Reader               , V1_16_4 to 0x06, V1_18_2 to 0x07, V1_19_0 to 0x04, V1_19_1 to 0x04)
            .register(ServerActionConfirmPacket::class            , ServerActionConfirmPacket.Reader            , V1_16_4 to 0x07, V1_18_2 to 0x08, V1_19_0 to 0x05, V1_19_1 to 0x05)
            .register(ServerBlockBreakAnimationPacket::class      , ServerBlockBreakAnimationPacket.Reader      , V1_16_4 to 0x08, V1_18_2 to 0x09, V1_19_0 to 0x06, V1_19_1 to 0x06)
            .register(ServerBlockEntityPacket::class              , ServerBlockEntityPacket.Reader              , V1_16_4 to 0x09, V1_18_2 to 0x0A, V1_19_0 to 0x07, V1_19_1 to 0x07)
            .register(ServerBlockEventPacket::class               , ServerBlockEventPacket.Reader               , V1_16_4 to 0x0A, V1_18_2 to 0x0B, V1_19_0 to 0x08, V1_19_1 to 0x08)
            .register(ServerBlockUpdatePacket::class              , ServerBlockUpdatePacket.Reader              , V1_16_4 to 0x0B, V1_18_2 to 0x0C, V1_19_0 to 0x09, V1_19_1 to 0x09)
            .register(ServerBossBarPacket::class                  , ServerBossBarPacket.Reader                  , V1_16_4 to 0x0C, V1_18_2 to 0x0D, V1_19_0 to 0x0A, V1_19_1 to 0x0A)
            .register(ServerDifficultyPacket::class               , ServerDifficultyPacket.Reader               , V1_16_4 to 0x0D, V1_18_2 to 0x0E, V1_19_0 to 0x0B, V1_19_1 to 0x0B)
            .register(ServerPlayerChatPreviewPacket::class        , ServerPlayerChatPreviewPacket.Reader        ,                                   V1_19_0 to 0x0C, V1_19_1 to 0x0C)
            .register(null                                        , ServerTitlePacket.ClearReader               ,                  V1_18_2 to 0x10, V1_19_0 to 0x0D, V1_19_1 to 0x0D)
            .register(ServerCommandSuggestPacket::class           , ServerCommandSuggestPacket.Reader           , V1_16_4 to 0x0F, V1_18_2 to 0x11, V1_19_0 to 0x0E, V1_19_1 to 0x0E)
//          .register(ServerCommandsPacket::class                 , ServerCommandsPacket.Reader                 , V1_16_4 to 0x10, V1_18_2 to 0x12, V1_19_0 to 0x0F, V1_19_1 to 0x0F)
            .register(ServerWindowConfirmPacket::class            , ServerWindowConfirmPacket.Reader            , V1_16_4 to 0x11                                                   )
            .register(ServerWindowClosePacket::class              , ServerWindowClosePacket.Reader              , V1_16_4 to 0x12, V1_18_2 to 0x13, V1_19_0 to 0x10, V1_19_1 to 0x10)
            .register(ServerInventoryContentPacket::class         , ServerInventoryContentPacket.Reader         , V1_16_4 to 0x13, V1_18_2 to 0x14, V1_19_0 to 0x11, V1_19_1 to 0x11)
            .register(ServerWindowPropertyPacket::class           , ServerWindowPropertyPacket.Reader           , V1_16_4 to 0x14, V1_18_2 to 0x15, V1_19_0 to 0x12, V1_19_1 to 0x12)
            .register(ServerInventorySlotPacket::class            , ServerInventorySlotPacket.Reader            , V1_16_4 to 0x15, V1_18_2 to 0x16, V1_19_0 to 0x13, V1_19_1 to 0x13)
            .register(ServerItemCooldownPacket::class             , ServerItemCooldownPacket.Reader             , V1_16_4 to 0x16, V1_18_2 to 0x17, V1_19_0 to 0x14, V1_19_1 to 0x14)
//          .register(::class                                     , .Reader                                     ,                                                    V1_19_1 to 0x15)
            .register(ServerCustomPayloadPacket::class            , ServerCustomPayloadPacket.Reader            , V1_16_4 to 0x17, V1_18_2 to 0x18, V1_19_0 to 0x15, V1_19_1 to 0x16)
            .register(ServerNamedSoundPacket::class               , ServerNamedSoundPacket.Reader               , V1_16_4 to 0x18, V1_18_2 to 0x19, V1_19_0 to 0x16, V1_19_1 to 0x17)
            .register(ServerChatDeletePacket::class               , ServerChatDeletePacket.Reader               ,                                                    V1_19_1 to 0x18)
            .register(ServerDisconnectPacket::class               , ServerDisconnectPacket.Reader               , V1_16_4 to 0x19, V1_18_2 to 0x1A, V1_19_0 to 0x17, V1_19_1 to 0x19)
            .register(ServerEntityEventPacket::class              , ServerEntityEventPacket.Reader              , V1_16_4 to 0x1A, V1_18_2 to 0x1B, V1_19_0 to 0x18, V1_19_1 to 0x1A)
            .register(ServerExplosionPacket::class                , ServerExplosionPacket.Reader                , V1_16_4 to 0x1B, V1_18_2 to 0x1C, V1_19_0 to 0x19, V1_19_1 to 0x1B)
            .register(ServerChunkUnloadPacket::class              , ServerChunkUnloadPacket.Reader              , V1_16_4 to 0x1C, V1_18_2 to 0x1D, V1_19_0 to 0x1A, V1_19_1 to 0x1C)
            .register(ServerGameStatePacket::class                , ServerGameStatePacket.Reader                , V1_16_4 to 0x1D, V1_18_2 to 0x1E, V1_19_0 to 0x1B, V1_19_1 to 0x1D)
            .register(ServerHorseWindowOpenPacket::class          , ServerHorseWindowOpenPacket.Reader          , V1_16_4 to 0x1E, V1_18_2 to 0x1F, V1_19_0 to 0x1C, V1_19_1 to 0x1E)
            .register(null                                        , ServerBorderPacket.InitializeReader         ,                  V1_18_2 to 0x20, V1_19_0 to 0x1D, V1_19_1 to 0x1F)
            .register(ServerKeepAlivePacket::class                , ServerKeepAlivePacket.Reader                , V1_16_4 to 0x1F, V1_18_2 to 0x21, V1_19_0 to 0x1E, V1_19_1 to 0x20)
            .register(ServerChunkPacket::class                    , ServerChunkPacket.Reader                    , V1_16_4 to 0x20, V1_18_2 to 0x22, V1_19_0 to 0x1F, V1_19_1 to 0x21)
            .register(ServerWorldEventPacket::class               , ServerWorldEventPacket.Reader               , V1_16_4 to 0x21, V1_18_2 to 0x23, V1_19_0 to 0x20, V1_19_1 to 0x22)
            .register(ServerParticlePacket::class                 , ServerParticlePacket.Reader                 , V1_16_4 to 0x22, V1_18_2 to 0x24, V1_19_0 to 0x21, V1_19_1 to 0x23)
            //.register(ServerChunkLightPacket::class               , ServerChunkLightPacket.Reader               , V1_16_4 to 0x23, V1_18_2 to 0x25, V1_19_0 to 0x22, V1_19_1 to 0x24)
            .register(ServerWorldPacket::class                    , ServerWorldPacket.Reader                    , V1_16_4 to 0x24, V1_18_2 to 0x26, V1_19_0 to 0x23, V1_19_1 to 0x25)
            .register(ServerMapPacket::class                      , ServerMapPacket.Reader                      , V1_16_4 to 0x25, V1_18_2 to 0x27, V1_19_0 to 0x24, V1_19_1 to 0x26)
            .register(ServerTradePacket::class                    , ServerTradePacket.Reader                    , V1_16_4 to 0x26, V1_18_2 to 0x28, V1_19_0 to 0x25, V1_19_1 to 0x27)
            .register(ServerEntityLocationPacket::class           , ServerEntityLocationPacket.MoveReader       , V1_16_4 to 0x27, V1_18_2 to 0x29, V1_19_0 to 0x26, V1_19_1 to 0x28)
            .register(null                                        , ServerEntityLocationPacket.MoveRotateReader , V1_16_4 to 0x28, V1_18_2 to 0x2A, V1_19_0 to 0x27, V1_19_1 to 0x29)
            .register(null                                        , ServerEntityLocationPacket.RotateReader     , V1_16_4 to 0x29, V1_18_2 to 0x2B, V1_19_0 to 0x28, V1_19_1 to 0x2A)
            .register(null                                        , ServerEntityLocationPacket.LocationReader   , V1_16_4 to 0x2A                                                   )
            .register(ServerVehicleLocationPacket::class          , ServerVehicleLocationPacket.Reader          , V1_16_4 to 0x2B, V1_18_2 to 0x2C, V1_19_0 to 0x29, V1_19_1 to 0x2B)
            .register(ServerBookOpenPacket::class                 , ServerBookOpenPacket.Reader                 , V1_16_4 to 0x2C, V1_18_2 to 0x2D, V1_19_0 to 0x2A, V1_19_1 to 0x2C)
            .register(ServerWindowOpenPacket::class               , ServerWindowOpenPacket.Reader               , V1_16_4 to 0x2D, V1_18_2 to 0x2E, V1_19_0 to 0x2B, V1_19_1 to 0x2D)
            .register(ServerSignUpdatePacket::class               , ServerSignUpdatePacket.Reader               , V1_16_4 to 0x2E, V1_18_2 to 0x2F, V1_19_0 to 0x2C, V1_19_1 to 0x2E)
            .register(ServerPingPacket::class                     , ServerPingPacket.Reader                     ,                  V1_18_2 to 0x30, V1_19_0 to 0x2D, V1_19_1 to 0x2F)
            .register(ServerCraftPacket::class                    , ServerCraftPacket.Reader                    , V1_16_4 to 0x2F, V1_18_2 to 0x31, V1_19_0 to 0x2E, V1_19_1 to 0x30)
            .register(ServerAbilitiesPacket::class                , ServerAbilitiesPacket.Reader                , V1_16_4 to 0x30, V1_18_2 to 0x32, V1_19_0 to 0x2F, V1_19_1 to 0x31)
            .register(ServerPlayerChatHeaderPacket::class         , ServerPlayerChatHeaderPacket.Reader         ,                                                    V1_19_1 to 0x32)
            .register(ServerPlayerChatPacket::class               , ServerPlayerChatPacket.Reader               , V1_16_4 to 0x0E, V1_18_2 to 0x0F, V1_19_0 to 0x30, V1_19_1 to 0x33)
            .register(ServerPlayerCombatEventPacket::class        , ServerPlayerCombatEventPacket.Reader        , V1_16_4 to 0x31, V1_18_2 to 0x33, V1_19_0 to 0x31, V1_19_1 to 0x34)
            .register(null                                        , ServerPlayerCombatEventPacket.EndReader     ,                  V1_18_2 to 0x33, V1_19_0 to 0x31, V1_19_1 to 0x34)
            .register(null                                        , ServerPlayerCombatEventPacket.EnterReader   ,                  V1_18_2 to 0x34, V1_19_0 to 0x32, V1_19_1 to 0x35)
            .register(null                                        , ServerPlayerCombatEventPacket.DeathReader   ,                  V1_18_2 to 0x35, V1_19_0 to 0x33, V1_19_1 to 0x36)
            .register(ServerPlayerListPacket::class               , ServerPlayerListPacket.UpdateReader               , V1_16_4 to 0x32, V1_18_2 to 0x36, V1_19_0 to 0x34, V1_19_1 to 0x37)
            .register(ServerLookAtPacket::class                   , ServerLookAtPacket.Reader                   , V1_16_4 to 0x33, V1_18_2 to 0x37, V1_19_0 to 0x35, V1_19_1 to 0x38)
            .register(ServerLocationPacket::class                 , ServerLocationPacket.Reader                 , V1_16_4 to 0x34, V1_18_2 to 0x38, V1_19_0 to 0x36, V1_19_1 to 0x39)
            .register(ServerRecipeBookPacket::class               , ServerRecipeBookPacket.Reader               , V1_16_4 to 0x35, V1_18_2 to 0x39, V1_19_0 to 0x37, V1_19_1 to 0x3A)
            .register(ServerEntitiesRemovePacket::class           , ServerEntitiesRemovePacket.Reader           , V1_16_4 to 0x36, V1_18_2 to 0x3A, V1_19_0 to 0x38, V1_19_1 to 0x3B)
            .register(ServerEntityEffectRevokePacket::class       , ServerEntityEffectRevokePacket.Reader       , V1_16_4 to 0x37, V1_18_2 to 0x3B, V1_19_0 to 0x39, V1_19_1 to 0x3C)
            .register(ServerResourcePackPacket::class             , ServerResourcePackPacket.Reader             , V1_16_4 to 0x38, V1_18_2 to 0x3C, V1_19_0 to 0x3A, V1_19_1 to 0x3D)
            .register(ServerRespawnPacket::class                  , ServerRespawnPacket.Reader                  , V1_16_4 to 0x39, V1_18_2 to 0x3D, V1_19_0 to 0x3B, V1_19_1 to 0x3E)
            .register(ServerEntityHeadRotationPacket::class       , ServerEntityHeadRotationPacket.Reader       , V1_16_4 to 0x3A, V1_18_2 to 0x3E, V1_19_0 to 0x3C, V1_19_1 to 0x3F)
            .register(ServerBlockUpdatesPacket::class             , ServerBlockUpdatesPacket.Reader             , V1_16_4 to 0x3B, V1_18_2 to 0x3F, V1_19_0 to 0x3D, V1_19_1 to 0x40)
            .register(ServerAdvancementTabPacket::class           , ServerAdvancementTabPacket.Reader           , V1_16_4 to 0x3C, V1_18_2 to 0x40, V1_19_0 to 0x3E, V1_19_1 to 0x41)
            .register(ServerServerDataPacket::class               , ServerServerDataPacket.Reader               ,                                   V1_19_0 to 0x3F, V1_19_1 to 0x42)
            .register(null                                        , ServerTitlePacket.ActionBarReader           ,                  V1_18_2 to 0x41, V1_19_0 to 0x40, V1_19_1 to 0x43)
            .register(ServerBorderPacket::class                   , ServerBorderPacket.Reader                   , V1_16_4 to 0x3D, V1_18_2 to 0x42, V1_19_0 to 0x41, V1_19_1 to 0x44)
            .register(null                                        , ServerBorderPacket.SetCenterReader          ,                  V1_18_2 to 0x42, V1_19_0 to 0x41, V1_19_1 to 0x44)
            .register(null                                        , ServerBorderPacket.LerpSizeReader           ,                  V1_18_2 to 0x43, V1_19_0 to 0x42, V1_19_1 to 0x45)
            .register(null                                        , ServerBorderPacket.SetSizeReader            ,                  V1_18_2 to 0x44, V1_19_0 to 0x43, V1_19_1 to 0x46)
            .register(null                                        , ServerBorderPacket.SetWarningTimeReader     ,                  V1_18_2 to 0x45, V1_19_0 to 0x44, V1_19_1 to 0x47)
            .register(null                                        , ServerBorderPacket.SetWarningDistanceReader ,                  V1_18_2 to 0x46, V1_19_0 to 0x45, V1_19_1 to 0x48)
            .register(ServerCameraPacket::class                   , ServerCameraPacket.Reader                   , V1_16_4 to 0x3E, V1_18_2 to 0x47, V1_19_0 to 0x46, V1_19_1 to 0x49)
            .register(ServerHotbarPacket::class                   , ServerHotbarPacket.Reader                   , V1_16_4 to 0x3F, V1_18_2 to 0x48, V1_19_0 to 0x47, V1_19_1 to 0x4A)
            .register(ServerChunkPublishPacket::class             , ServerChunkPublishPacket.Reader             , V1_16_4 to 0x40, V1_18_2 to 0x49, V1_19_0 to 0x48, V1_19_1 to 0x4B)
            .register(ServerViewDistancePacket::class             , ServerViewDistancePacket.Reader             , V1_16_4 to 0x41, V1_18_2 to 0x4A, V1_19_0 to 0x49, V1_19_1 to 0x4C)
            .register(ServerSpawnPositionPacket::class            , ServerSpawnPositionPacket.Reader            , V1_16_4 to 0x42, V1_18_2 to 0x4B, V1_19_0 to 0x4A, V1_19_1 to 0x4D)
            .register(ServerPlayerChatPreviewSettingsPacket::class, ServerPlayerChatPreviewSettingsPacket.Reader,                                   V1_19_0 to 0x4B, V1_19_1 to 0x4E)
            .register(ServerScoreboardDisplayPacket::class        , ServerScoreboardDisplayPacket.Reader        , V1_16_4 to 0x43, V1_18_2 to 0x4C, V1_19_0 to 0x4C, V1_19_1 to 0x4F)
            .register(ServerEntityMetadataPacket::class           , ServerEntityMetadataPacket.Reader           , V1_16_4 to 0x44, V1_18_2 to 0x4D, V1_19_0 to 0x4D, V1_19_1 to 0x50)
            .register(ServerEntityAttachPacket::class             , ServerEntityAttachPacket.Reader             , V1_16_4 to 0x45, V1_18_2 to 0x4E, V1_19_0 to 0x4E, V1_19_1 to 0x51)
            .register(ServerEntityVelocityPacket::class           , ServerEntityVelocityPacket.Reader           , V1_16_4 to 0x46, V1_18_2 to 0x4F, V1_19_0 to 0x4F, V1_19_1 to 0x52)
            .register(ServerEntityEquipmentPacket::class          , ServerEntityEquipmentPacket.Reader          , V1_16_4 to 0x47, V1_18_2 to 0x50, V1_19_0 to 0x50, V1_19_1 to 0x53)
            .register(ServerExperiencePacket::class               , ServerExperiencePacket.Reader               , V1_16_4 to 0x48, V1_18_2 to 0x51, V1_19_0 to 0x51, V1_19_1 to 0x54)
            .register(ServerHealthHungerSaturationPacket::class   , ServerHealthHungerSaturationPacket.Reader   , V1_16_4 to 0x49, V1_18_2 to 0x52, V1_19_0 to 0x52, V1_19_1 to 0x55)
            .register(ServerObjectivePacket::class                , ServerObjectivePacket.Reader                , V1_16_4 to 0x4A, V1_18_2 to 0x53, V1_19_0 to 0x53, V1_19_1 to 0x56)
            .register(ServerEntityPassengersPacket::class         , ServerEntityPassengersPacket.Reader         , V1_16_4 to 0x4B, V1_18_2 to 0x54, V1_19_0 to 0x54, V1_19_1 to 0x57)
            .register(ServerTeamPacket::class                     , ServerTeamPacket.Reader                     , V1_16_4 to 0x4C, V1_18_2 to 0x55, V1_19_0 to 0x55, V1_19_1 to 0x58)
            .register(ServerScorePacket::class                    , ServerScorePacket.Reader                    , V1_16_4 to 0x4D, V1_18_2 to 0x56, V1_19_0 to 0x56, V1_19_1 to 0x59)
            .register(ServerSimulationDistancePacket::class       , ServerSimulationDistancePacket.Reader       ,                  V1_18_2 to 0x57, V1_19_0 to 0x57, V1_19_1 to 0x5A)
            .register(null                                        , ServerTitlePacket.SetSubTitleReader         ,                  V1_18_2 to 0x58, V1_19_0 to 0x58, V1_19_1 to 0x5B)
            .register(ServerTimePacket::class                     , ServerTimePacket.Reader                     , V1_16_4 to 0x4E, V1_18_2 to 0x59, V1_19_0 to 0x59, V1_19_1 to 0x5C)
            .register(ServerTitlePacket::class                    , ServerTitlePacket.Reader                    , V1_16_4 to 0x4F                                                   )
            .register(null                                        , ServerTitlePacket.SetTitleReader            ,                  V1_18_2 to 0x5A, V1_19_0 to 0x5A, V1_19_1 to 0x5D)
            .register(null                                        , ServerTitlePacket.SetTimingsReader          ,                  V1_18_2 to 0x5B, V1_19_0 to 0x5B, V1_19_1 to 0x5E)
            .register(ServerEntitySoundPacket::class              , ServerEntitySoundPacket.Reader              , V1_16_4 to 0x50, V1_18_2 to 0x5C, V1_19_0 to 0x5C, V1_19_1 to 0x5F)
            .register(ServerSoundPacket::class                    , ServerSoundPacket.Reader                    , V1_16_4 to 0x51, V1_18_2 to 0x5D, V1_19_0 to 0x5D, V1_19_1 to 0x60)
            .register(ServerSoundStopPacket::class                , ServerSoundStopPacket.Reader                , V1_16_4 to 0x52, V1_18_2 to 0x5E, V1_19_0 to 0x5E, V1_19_1 to 0x61)
            .register(ServerSystemChatPacket::class               , ServerSystemChatPacket.Reader               ,                                   V1_19_0 to 0x5F, V1_19_1 to 0x62)
            .register(ServerPlayerListHeaderFooterPacket::class   , ServerPlayerListHeaderFooterPacket.Reader   , V1_16_4 to 0x53, V1_18_2 to 0x5F, V1_19_0 to 0x60, V1_19_1 to 0x63)
            .register(ServerQueryPacket::class                    , ServerQueryPacket.Reader                    , V1_16_4 to 0x54, V1_18_2 to 0x60, V1_19_0 to 0x61, V1_19_1 to 0x64)
            .register(ServerItemTakePacket::class                 , ServerItemTakePacket.Reader                 , V1_16_4 to 0x55, V1_18_2 to 0x61, V1_19_0 to 0x62, V1_19_1 to 0x65)
            .register(ServerEntityTeleportPacket::class           , ServerEntityTeleportPacket.Reader           , V1_16_4 to 0x56, V1_18_2 to 0x62, V1_19_0 to 0x63, V1_19_1 to 0x66)
            .register(ServerAdvancementsPacket::class             , ServerAdvancementsPacket.Reader             , V1_16_4 to 0x57, V1_18_2 to 0x63, V1_19_0 to 0x64, V1_19_1 to 0x67)
            .register(ServerEntityAttributesPacket::class         , ServerEntityAttributesPacket.Reader         , V1_16_4 to 0x58, V1_18_2 to 0x64, V1_19_0 to 0x65, V1_19_1 to 0x68)
            .register(ServerEntityEffectApplyPacket::class        , ServerEntityEffectApplyPacket.Reader        , V1_16_4 to 0x59, V1_18_2 to 0x65, V1_19_0 to 0x66, V1_19_1 to 0x69)
            .register(ServerRecipesPacket::class                  , ServerRecipesPacket.Reader                  , V1_16_4 to 0x5A, V1_18_2 to 0x66, V1_19_0 to 0x67, V1_19_1 to 0x6A)
            .register(ServerTagsPacket::class                     , ServerTagsPacket.Reader                     , V1_16_4 to 0x5B, V1_18_2 to 0x67, V1_19_0 to 0x68, V1_19_1 to 0x6B)
            .build()
    )
}
