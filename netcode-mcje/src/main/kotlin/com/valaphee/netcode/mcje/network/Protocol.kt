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
import com.valaphee.netcode.mcje.network.packet.play.ClientAbilitiesPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientActionPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientActionPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientAdvancementTabPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientAdvancementTabPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientBeaconUpdatePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientBeaconUpdatePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientBlockPlacePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientBlockPlacePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientBlockQueryPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientBlockQueryPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientBookEditPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientBookEditPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientCommandBlockMinecartUpdatePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientCommandBlockMinecartUpdatePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientCommandBlockUpdatePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientCommandBlockUpdatePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientCommandPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientCommandPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientCommandSuggestPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientCommandSuggestPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientCraftPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientCraftPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientCreativeInventorySlotPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientCreativeInventorySlotPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientCustomPayloadPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientCustomPayloadPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientDifficultyLockPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientDifficultyLockPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientDifficultyPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientDifficultyPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientEntityQueryPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientEntityQueryPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientHotbarPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientHotbarPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientItemNamePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientItemNamePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientItemPickPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientItemPickPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientItemUseOnEntityPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientItemUseOnEntityPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientItemUsePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientItemUsePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientJigsawBlockUpdatePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientJigsawBlockUpdatePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientJigsawGeneratePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientJigsawGeneratePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientKeepAlivePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientKeepAlivePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientLocationPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientLocationPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientPlayerActionPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientPlayerActionPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientPlayerChatPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientPlayerChatPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientPlayerChatPreviewPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientPlayerChatPreviewPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientPongPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientPongPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientPositionPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientPositionRotationPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientRecipeBookDisplayRecipePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientRecipeBookDisplayRecipePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientRecipeBookStatePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientRecipeBookStatePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientResourcePackStatusPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientResourcePackStatusPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientRotationPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientSettingsPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientSettingsPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientSignUpdatePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientSignUpdatePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientSpectatePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientSpectatePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientStatusPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientStatusPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientSteerBoatPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientSteerBoatPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientSteerPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientSteerPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientStructureBlockUpdatePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientStructureBlockUpdatePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientSwingArmPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientSwingArmPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientTeleportConfirmPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientTeleportConfirmPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientTradePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientTradePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientVehicleLocationPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientVehicleLocationPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientWindowClickButtonPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientWindowClickButtonPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientWindowClickPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientWindowClickPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientWindowClosePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientWindowClosePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientWindowConfirmPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientWindowConfirmPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerAbilitiesPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerAbilitiesPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerActionConfirmPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerActionConfirmPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerBlockBreakAnimationPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerBlockBreakAnimationPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerBlockEntityPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerBlockEntityPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerBlockEventPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerBlockEventPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerBlockUpdatePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerBlockUpdatePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerBlockUpdatesPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerBlockUpdatesPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerBookOpenPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerBookOpenPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerBorderInitializePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerBorderLerpSizePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerBorderPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerBorderPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerBorderSetCenterPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerBorderSetSizePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerBorderSetWarningDistancePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerBorderSetWarningTimePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerBossBarPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerBossBarPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerCameraPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerCameraPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerChunkPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerChunkPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerChunkPublishPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerChunkPublishPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerChunkUnloadPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerChunkUnloadPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerCommandSuggestPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerCommandSuggestPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerCraftPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerCraftPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerCustomPayloadPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerCustomPayloadPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerDifficultyPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerDifficultyPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerDisconnectPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerDisconnectPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerEntitiesRemovePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntitiesRemovePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityAddPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityAddPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityAnimationPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityAnimationPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityAttachPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityAttachPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityAttributesPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityAttributesPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityEffectApplyPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityEffectApplyPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityEffectRevokePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityEffectRevokePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityEquipmentPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityEquipmentPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityEventPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityEventPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityHeadRotationPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityHeadRotationPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityLocationPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityMetadataPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityMetadataPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityMovePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityMoveRotatePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityMoveRotatePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityPassengersPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityPassengersPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityRotatePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerEntitySoundPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntitySoundPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityTeleportPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityTeleportPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityVelocityPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityVelocityPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerExperienceOrbAddPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerExperienceOrbAddPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerExperiencePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerExperiencePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerExplosionPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerExplosionPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerGameStatePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerGameStatePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerHealthHungerSaturationPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerHealthHungerSaturationPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerHorseWindowOpenPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerHorseWindowOpenPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerHotbarPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerHotbarPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerInventoryContentPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerInventoryContentPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerInventorySlotPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerInventorySlotPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerItemCooldownPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerItemCooldownPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerKeepAlivePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerKeepAlivePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerLocationPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerLocationPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerLookAtPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerLookAtPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerMapPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerMapPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerNamedSoundPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerNamedSoundPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerObjectAddPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerObjectAddPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerObjectivePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerObjectivePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerPaintingAddPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerPaintingAddPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerParticlePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerParticlePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerPingPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerPingPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerPlayerAddPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerPlayerAddPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerPlayerChatPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerPlayerChatPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerPlayerChatPreviewPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerPlayerChatPreviewPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerPlayerCombatEventDeathPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerPlayerCombatEventEndPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerPlayerCombatEventEnterPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerPlayerCombatEventPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerPlayerCombatEventPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerPlayerListHeaderFooterPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerPlayerListHeaderFooterPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerPlayerListPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerPlayerListPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerQueryPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerQueryPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerRecipeBookPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerRecipeBookPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerRecipesPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerRecipesPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerResourcePackPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerResourcePackPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerRespawnPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerRespawnPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerScorePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerScorePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerScoreboardDisplayPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerScoreboardDisplayPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerSignUpdatePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerSignUpdatePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerSimulationDistancePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerSimulationDistancePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerSoundPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerSoundPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerSoundStopPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerSoundStopPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerSpawnPositionPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerSpawnPositionPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerStackTakePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerStackTakePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerSystemChatPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerSystemChatPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerTeamPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerTeamPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerTimePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerTimePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerTitleActionBarPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerTitleClearPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerTitlePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerTitlePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerTitleSetSubTitlePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerTitleSetTimingsPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerTitleSetTitlePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerTradePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerTradePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerVehicleLocationPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerVehicleLocationPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerViewDistancePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerViewDistancePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerWindowClosePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerWindowClosePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerWindowConfirmPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerWindowConfirmPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerWindowOpenPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerWindowOpenPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerWindowPropertyPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerWindowPropertyPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerWorldEventPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerWorldEventPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerWorldPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerWorldPacketReader
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
    val readerById: Int2ObjectMap<PacketReader>,
    val idByReader: Object2IntMap<PacketReader>
) {
    class Builder {
        private val packetsAndReadersByVersion = Int2ObjectOpenHashMap<Pair<Object2IntMap<KClass<out Packet<out PacketHandler>>>, Int2ObjectMap<PacketReader>>>()

        fun register(`class`: KClass<out Packet<out PacketHandler>>?, reader: PacketReader, vararg idByVersion: Pair<Int, Int>) = apply {
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
            .register(ClientTeleportConfirmPacket::class           , ClientTeleportConfirmPacketReader           ,                  V1_09_0 to 0x00, V1_09_1 to 0x00, V1_09_2 to 0x00, V1_09_4 to 0x00, V1_10_0 to 0x00, V1_11_0 to 0x00, V1_11_1 to 0x00, V1_12_0 to 0x00, V1_12_1 to 0x00, V1_12_2 to 0x00, V1_13_0 to 0x00, V1_13_1 to 0x00, V1_13_2 to 0x00, V1_14_0 to 0x00, V1_14_1 to 0x00, V1_14_2 to 0x00, V1_14_3 to 0x00, V1_14_4 to 0x00, V1_15_0 to 0x00, V1_15_1 to 0x00, V1_15_2 to 0x00, V1_16_0 to 0x00, V1_16_1 to 0x00, V1_16_2 to 0x00, V1_16_3 to 0x00, V1_16_4 to 0x00, V1_17_0 to 0x00, V1_17_1 to 0x00, V1_18_2 to 0x00, V1_18_2 to 0x00, V1_19_0 to 0x00, V1_19_1 to 0x00)
            .register(ClientBlockQueryPacket::class                , ClientBlockQueryPacketReader                ,                                                                                                                                                                                            V1_13_0 to 0x01, V1_13_1 to 0x01, V1_13_2 to 0x01, V1_14_0 to 0x01, V1_14_1 to 0x01, V1_14_2 to 0x01, V1_14_3 to 0x01, V1_14_4 to 0x01, V1_15_0 to 0x01, V1_15_1 to 0x01, V1_15_2 to 0x01, V1_16_0 to 0x01, V1_16_1 to 0x01, V1_16_2 to 0x01, V1_16_3 to 0x01, V1_16_4 to 0x01, V1_17_0 to 0x01, V1_17_1 to 0x01, V1_18_2 to 0x01, V1_18_2 to 0x01, V1_19_0 to 0x01, V1_19_1 to 0x01)
            .register(ClientDifficultyPacket::class                , ClientDifficultyPacketReader                ,                                                                                                                                                                                                                                               V1_14_0 to 0x02, V1_14_1 to 0x02, V1_14_2 to 0x02, V1_14_3 to 0x02, V1_14_4 to 0x02, V1_15_0 to 0x02, V1_15_1 to 0x02, V1_15_2 to 0x02, V1_16_0 to 0x02, V1_16_1 to 0x02, V1_16_2 to 0x02, V1_16_3 to 0x02, V1_16_4 to 0x02, V1_17_0 to 0x02, V1_17_1 to 0x02, V1_18_2 to 0x02, V1_18_2 to 0x02, V1_19_0 to 0x02, V1_19_1 to 0x02)
            .register(ClientCommandPacket::class                   , ClientCommandPacketReader                   ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                V1_19_0 to 0x03, V1_19_1 to 0x04)
            .register(ClientPlayerChatPacket::class                , ClientPlayerChatPacketReader                , V1_08_0 to 0x01, V1_09_0 to 0x02, V1_09_1 to 0x02, V1_09_2 to 0x02, V1_09_4 to 0x02, V1_10_0 to 0x02, V1_11_0 to 0x02, V1_11_1 to 0x02, V1_12_0 to 0x03, V1_12_1 to 0x02, V1_12_2 to 0x02, V1_13_0 to 0x02, V1_13_1 to 0x02, V1_13_2 to 0x02, V1_14_0 to 0x03, V1_14_1 to 0x03, V1_14_2 to 0x03, V1_14_3 to 0x03, V1_14_4 to 0x03, V1_15_0 to 0x03, V1_15_1 to 0x03, V1_15_2 to 0x03, V1_16_0 to 0x03, V1_16_1 to 0x03, V1_16_2 to 0x03, V1_16_3 to 0x03, V1_16_4 to 0x03, V1_17_0 to 0x03, V1_17_1 to 0x03, V1_18_2 to 0x03, V1_18_2 to 0x03, V1_19_0 to 0x04, V1_19_1 to 0x05)
            .register(ClientPlayerChatPreviewPacket::class         , ClientPlayerChatPreviewPacketReader         ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                V1_19_0 to 0x05, V1_19_1 to 0x06)
            .register(ClientStatusPacket::class                    , ClientStatusPacketReader                    , V1_08_0 to 0x16, V1_09_0 to 0x03, V1_09_1 to 0x03, V1_09_2 to 0x03, V1_09_4 to 0x03, V1_10_0 to 0x03, V1_11_0 to 0x03, V1_11_1 to 0x03, V1_12_0 to 0x04, V1_12_1 to 0x03, V1_12_2 to 0x03, V1_13_0 to 0x03, V1_13_1 to 0x03, V1_13_2 to 0x03, V1_14_0 to 0x04, V1_14_1 to 0x04, V1_14_2 to 0x04, V1_14_3 to 0x04, V1_14_4 to 0x04, V1_15_0 to 0x04, V1_15_1 to 0x04, V1_15_2 to 0x04, V1_16_0 to 0x04, V1_16_1 to 0x04, V1_16_2 to 0x04, V1_16_3 to 0x04, V1_16_4 to 0x04, V1_17_0 to 0x04, V1_17_1 to 0x04, V1_18_2 to 0x04, V1_18_2 to 0x04, V1_19_0 to 0x06, V1_19_1 to 0x07)
            .register(ClientSettingsPacket::class                  , ClientSettingsPacketReader                  , V1_08_0 to 0x15, V1_09_0 to 0x04, V1_09_1 to 0x04, V1_09_2 to 0x04, V1_09_4 to 0x04, V1_10_0 to 0x04, V1_11_0 to 0x04, V1_11_1 to 0x04, V1_12_0 to 0x05, V1_12_1 to 0x04, V1_12_2 to 0x04, V1_13_0 to 0x04, V1_13_1 to 0x04, V1_13_2 to 0x04, V1_14_0 to 0x05, V1_14_1 to 0x05, V1_14_2 to 0x05, V1_14_3 to 0x05, V1_14_4 to 0x05, V1_15_0 to 0x05, V1_15_1 to 0x05, V1_15_2 to 0x05, V1_16_0 to 0x05, V1_16_1 to 0x05, V1_16_2 to 0x05, V1_16_3 to 0x05, V1_16_4 to 0x05, V1_17_0 to 0x05, V1_17_1 to 0x05, V1_18_2 to 0x05, V1_18_2 to 0x05, V1_19_0 to 0x07, V1_19_1 to 0x08)
            .register(ClientCommandSuggestPacket::class            , ClientCommandSuggestPacketReader            , V1_08_0 to 0x14, V1_09_0 to 0x01, V1_09_1 to 0x01, V1_09_2 to 0x01, V1_09_4 to 0x01, V1_10_0 to 0x01, V1_11_0 to 0x01, V1_11_1 to 0x01, V1_12_0 to 0x02, V1_12_1 to 0x01, V1_12_2 to 0x01, V1_13_0 to 0x05, V1_13_1 to 0x05, V1_13_2 to 0x05, V1_14_0 to 0x06, V1_14_1 to 0x06, V1_14_2 to 0x06, V1_14_3 to 0x06, V1_14_4 to 0x06, V1_15_0 to 0x06, V1_15_1 to 0x06, V1_15_2 to 0x06, V1_16_0 to 0x06, V1_16_1 to 0x06, V1_16_2 to 0x06, V1_16_3 to 0x06, V1_16_4 to 0x06, V1_17_0 to 0x06, V1_17_1 to 0x06, V1_18_2 to 0x06, V1_18_2 to 0x06, V1_19_0 to 0x08, V1_19_1 to 0x09)
            .register(ClientWindowConfirmPacket::class             , ClientWindowConfirmPacketReader             , V1_08_0 to 0x0F, V1_09_0 to 0x05, V1_09_1 to 0x05, V1_09_2 to 0x05, V1_09_4 to 0x05, V1_10_0 to 0x05, V1_11_0 to 0x05, V1_11_1 to 0x05, V1_12_0 to 0x06, V1_12_1 to 0x05, V1_12_2 to 0x05, V1_13_0 to 0x06, V1_13_1 to 0x06, V1_13_2 to 0x06, V1_14_0 to 0x07, V1_14_1 to 0x07, V1_14_2 to 0x07, V1_14_3 to 0x07, V1_14_4 to 0x07, V1_15_0 to 0x07, V1_15_1 to 0x07, V1_15_2 to 0x07, V1_16_0 to 0x07, V1_16_1 to 0x07, V1_16_2 to 0x07, V1_16_3 to 0x07, V1_16_4 to 0x07                                                                                                      )
            .register(ClientWindowClickButtonPacket::class         , ClientWindowClickButtonPacketReader         , V1_08_0 to 0x11, V1_09_0 to 0x06, V1_09_1 to 0x06, V1_09_2 to 0x06, V1_09_4 to 0x06, V1_10_0 to 0x06, V1_11_0 to 0x06, V1_11_1 to 0x06, V1_12_0 to 0x07, V1_12_1 to 0x06, V1_12_2 to 0x06, V1_13_0 to 0x07, V1_13_1 to 0x07, V1_13_2 to 0x07, V1_14_0 to 0x08, V1_14_1 to 0x08, V1_14_2 to 0x08, V1_14_3 to 0x08, V1_14_4 to 0x08, V1_15_0 to 0x08, V1_15_1 to 0x08, V1_15_2 to 0x08, V1_16_0 to 0x08, V1_16_1 to 0x08, V1_16_2 to 0x08, V1_16_3 to 0x08, V1_16_4 to 0x08, V1_17_0 to 0x07, V1_17_1 to 0x07, V1_18_2 to 0x07, V1_18_2 to 0x07, V1_19_0 to 0x09, V1_19_1 to 0x0A)
            .register(ClientWindowClickPacket::class               , ClientWindowClickPacketReader               , V1_08_0 to 0x0E, V1_09_0 to 0x07, V1_09_1 to 0x07, V1_09_2 to 0x07, V1_09_4 to 0x07, V1_10_0 to 0x07, V1_11_0 to 0x07, V1_11_1 to 0x07, V1_12_0 to 0x08, V1_12_1 to 0x07, V1_12_2 to 0x07, V1_13_0 to 0x08, V1_13_1 to 0x08, V1_13_2 to 0x08, V1_14_0 to 0x09, V1_14_1 to 0x09, V1_14_2 to 0x09, V1_14_3 to 0x09, V1_14_4 to 0x09, V1_15_0 to 0x09, V1_15_1 to 0x09, V1_15_2 to 0x09, V1_16_0 to 0x09, V1_16_1 to 0x09, V1_16_2 to 0x09, V1_16_3 to 0x09, V1_16_4 to 0x09, V1_17_0 to 0x08, V1_17_1 to 0x08, V1_18_2 to 0x08, V1_18_2 to 0x08, V1_19_0 to 0x0A, V1_19_1 to 0x0B)
            .register(ClientWindowClosePacket::class               , ClientWindowClosePacketReader               , V1_08_0 to 0x0D, V1_09_0 to 0x08, V1_09_1 to 0x08, V1_09_2 to 0x08, V1_09_4 to 0x08, V1_10_0 to 0x08, V1_11_0 to 0x08, V1_11_1 to 0x08, V1_12_0 to 0x09, V1_12_1 to 0x08, V1_12_2 to 0x08, V1_13_0 to 0x09, V1_13_1 to 0x09, V1_13_2 to 0x09, V1_14_0 to 0x0A, V1_14_1 to 0x0A, V1_14_2 to 0x0A, V1_14_3 to 0x0A, V1_14_4 to 0x0A, V1_15_0 to 0x0A, V1_15_1 to 0x0A, V1_15_2 to 0x0A, V1_16_0 to 0x0A, V1_16_1 to 0x0A, V1_16_2 to 0x0A, V1_16_3 to 0x0A, V1_16_4 to 0x0A, V1_17_0 to 0x09, V1_17_1 to 0x09, V1_18_2 to 0x09, V1_18_2 to 0x09, V1_19_0 to 0x0B, V1_19_1 to 0x0C)
            .register(ClientCustomPayloadPacket::class             , ClientCustomPayloadPacketReader             , V1_08_0 to 0x17, V1_09_0 to 0x09, V1_09_1 to 0x09, V1_09_2 to 0x09, V1_09_4 to 0x09, V1_10_0 to 0x09, V1_11_0 to 0x09, V1_11_1 to 0x09, V1_12_0 to 0x0A, V1_12_1 to 0x09, V1_12_2 to 0x09, V1_13_0 to 0x0A, V1_13_1 to 0x0A, V1_13_2 to 0x0A, V1_14_0 to 0x0B, V1_14_1 to 0x0B, V1_14_2 to 0x0B, V1_14_3 to 0x0B, V1_14_4 to 0x0B, V1_15_0 to 0x0B, V1_15_1 to 0x0B, V1_15_2 to 0x0B, V1_16_0 to 0x0B, V1_16_1 to 0x0B, V1_16_2 to 0x0B, V1_16_3 to 0x0B, V1_16_4 to 0x0B, V1_17_0 to 0x0A, V1_17_1 to 0x0A, V1_18_2 to 0x0A, V1_18_2 to 0x0A, V1_19_0 to 0x0C, V1_19_1 to 0x0D)
            .register(ClientBookEditPacket::class                  , ClientBookEditPacketReader                  ,                                                                                                                                                                                            V1_13_0 to 0x0B, V1_13_1 to 0x0B, V1_13_2 to 0x0B, V1_14_0 to 0x0C, V1_14_1 to 0x0C, V1_14_2 to 0x0C, V1_14_3 to 0x0C, V1_14_4 to 0x0C, V1_15_0 to 0x0C, V1_15_1 to 0x0C, V1_15_2 to 0x0C, V1_16_0 to 0x0C, V1_16_1 to 0x0C, V1_16_2 to 0x0C, V1_16_3 to 0x0C, V1_16_4 to 0x0C, V1_17_0 to 0x0B, V1_17_1 to 0x0B, V1_18_2 to 0x0B, V1_18_2 to 0x0B, V1_19_0 to 0x0D, V1_19_1 to 0x0E)
            .register(ClientEntityQueryPacket::class               , ClientEntityQueryPacketReader               ,                                                                                                                                                                                            V1_13_0 to 0x0C, V1_13_1 to 0x0C, V1_13_2 to 0x0C, V1_14_0 to 0x0D, V1_14_1 to 0x0D, V1_14_2 to 0x0D, V1_14_3 to 0x0D, V1_14_4 to 0x0D, V1_15_0 to 0x0D, V1_15_1 to 0x0D, V1_15_2 to 0x0D, V1_16_0 to 0x0D, V1_16_1 to 0x0D, V1_16_2 to 0x0D, V1_16_3 to 0x0D, V1_16_4 to 0x0D, V1_17_0 to 0x0C, V1_17_1 to 0x0C, V1_18_2 to 0x0C, V1_18_2 to 0x0C, V1_19_0 to 0x0E, V1_19_1 to 0x0F)
            .register(ClientItemUseOnEntityPacket::class           , ClientItemUseOnEntityPacketReader           , V1_08_0 to 0x02, V1_09_0 to 0x0A, V1_09_1 to 0x0A, V1_09_2 to 0x0A, V1_09_4 to 0x0A, V1_10_0 to 0x0A, V1_11_0 to 0x0A, V1_11_1 to 0x0A, V1_12_0 to 0x0B, V1_12_1 to 0x0A, V1_12_2 to 0x0A, V1_13_0 to 0x0D, V1_13_1 to 0x0D, V1_13_2 to 0x0D, V1_14_0 to 0x0E, V1_14_1 to 0x0E, V1_14_2 to 0x0E, V1_14_3 to 0x0E, V1_14_4 to 0x0E, V1_15_0 to 0x0E, V1_15_1 to 0x0E, V1_15_2 to 0x0E, V1_16_0 to 0x0E, V1_16_1 to 0x0E, V1_16_2 to 0x0E, V1_16_3 to 0x0E, V1_16_4 to 0x0E, V1_17_0 to 0x0D, V1_17_1 to 0x0D, V1_18_2 to 0x0D, V1_18_2 to 0x0D, V1_19_0 to 0x0F, V1_19_1 to 0x10)
            .register(ClientJigsawGeneratePacket::class            , ClientJigsawGeneratePacketReader            ,                                                                                                                                                                                                                                                                                                                                                                                       V1_16_0 to 0x0F, V1_16_1 to 0x0F, V1_16_2 to 0x0F, V1_16_3 to 0x0F, V1_16_4 to 0x0F, V1_17_0 to 0x0E, V1_17_1 to 0x0E, V1_18_2 to 0x0E, V1_18_2 to 0x0E, V1_19_0 to 0x10, V1_19_1 to 0x11)
            .register(ClientKeepAlivePacket::class                 , ClientKeepAlivePacketReader                 , V1_08_0 to 0x00, V1_09_0 to 0x0B, V1_09_1 to 0x0B, V1_09_2 to 0x0B, V1_09_4 to 0x0B, V1_10_0 to 0x0B, V1_11_0 to 0x0B, V1_11_1 to 0x0B, V1_12_0 to 0x0C, V1_12_1 to 0x0B, V1_12_2 to 0x0B, V1_13_0 to 0x0E, V1_13_1 to 0x0E, V1_13_2 to 0x0E, V1_14_0 to 0x0F, V1_14_1 to 0x0F, V1_14_2 to 0x0F, V1_14_3 to 0x0F, V1_14_4 to 0x0F, V1_15_0 to 0x0F, V1_15_1 to 0x0F, V1_15_2 to 0x0F, V1_16_0 to 0x10, V1_16_1 to 0x10, V1_16_2 to 0x10, V1_16_3 to 0x10, V1_16_4 to 0x10, V1_17_0 to 0x0F, V1_17_1 to 0x0F, V1_18_2 to 0x0F, V1_18_2 to 0x0F, V1_19_0 to 0x11, V1_19_1 to 0x12)
            .register(ClientDifficultyLockPacket::class            , ClientDifficultyLockPacketReader            ,                                                                                                                                                                                                                                               V1_14_0 to 0x10, V1_14_1 to 0x10, V1_14_2 to 0x10, V1_14_3 to 0x10, V1_14_4 to 0x10, V1_15_0 to 0x10, V1_15_1 to 0x10, V1_15_2 to 0x10, V1_16_0 to 0x11, V1_16_1 to 0x11, V1_16_2 to 0x11, V1_16_3 to 0x11, V1_16_4 to 0x11, V1_17_0 to 0x10, V1_17_1 to 0x10, V1_18_2 to 0x10, V1_18_2 to 0x10, V1_19_0 to 0x12, V1_19_1 to 0x13)
            .register(ClientLocationPacket::class                  , ClientPositionPacketReader                  , V1_08_0 to 0x04, V1_09_0 to 0x0C, V1_09_1 to 0x0C, V1_09_2 to 0x0C, V1_09_4 to 0x0C, V1_10_0 to 0x0C, V1_11_0 to 0x0C, V1_11_1 to 0x0C, V1_12_0 to 0x0E, V1_12_1 to 0x0D, V1_12_2 to 0x0D, V1_13_0 to 0x10, V1_13_1 to 0x10, V1_13_2 to 0x10, V1_14_0 to 0x11, V1_14_1 to 0x11, V1_14_2 to 0x11, V1_14_3 to 0x11, V1_14_4 to 0x11, V1_15_0 to 0x11, V1_15_1 to 0x11, V1_15_2 to 0x11, V1_16_0 to 0x12, V1_16_1 to 0x12, V1_16_2 to 0x12, V1_16_3 to 0x12, V1_16_4 to 0x12, V1_17_0 to 0x11, V1_17_1 to 0x11, V1_18_2 to 0x11, V1_18_2 to 0x11, V1_19_0 to 0x13, V1_19_1 to 0x14)
            .register(null                                         , ClientPositionRotationPacketReader          , V1_08_0 to 0x06, V1_09_0 to 0x0D, V1_09_1 to 0x0D, V1_09_2 to 0x0D, V1_09_4 to 0x0D, V1_10_0 to 0x0D, V1_11_0 to 0x0D, V1_11_1 to 0x0D, V1_12_0 to 0x0F, V1_12_1 to 0x0E, V1_12_2 to 0x0E, V1_13_0 to 0x11, V1_13_1 to 0x11, V1_13_2 to 0x11, V1_14_0 to 0x12, V1_14_1 to 0x12, V1_14_2 to 0x12, V1_14_3 to 0x12, V1_14_4 to 0x12, V1_15_0 to 0x12, V1_15_1 to 0x12, V1_15_2 to 0x12, V1_16_0 to 0x13, V1_16_1 to 0x13, V1_16_2 to 0x13, V1_16_3 to 0x13, V1_16_4 to 0x13, V1_17_0 to 0x12, V1_17_1 to 0x12, V1_18_2 to 0x12, V1_18_2 to 0x12, V1_19_0 to 0x14, V1_19_1 to 0x15)
            .register(null                                         , ClientRotationPacketReader                  , V1_08_0 to 0x05, V1_09_0 to 0x0E, V1_09_1 to 0x0E, V1_09_2 to 0x0E, V1_09_4 to 0x0E, V1_10_0 to 0x0E, V1_11_0 to 0x0E, V1_11_1 to 0x0E, V1_12_0 to 0x10, V1_12_1 to 0x0F, V1_12_2 to 0x0F, V1_13_0 to 0x12, V1_13_1 to 0x12, V1_13_2 to 0x12, V1_14_0 to 0x13, V1_14_1 to 0x13, V1_14_2 to 0x13, V1_14_3 to 0x13, V1_14_4 to 0x13, V1_15_0 to 0x13, V1_15_1 to 0x13, V1_15_2 to 0x13, V1_16_0 to 0x14, V1_16_1 to 0x14, V1_16_2 to 0x14, V1_16_3 to 0x14, V1_16_4 to 0x14, V1_17_0 to 0x13, V1_17_1 to 0x13, V1_18_2 to 0x13, V1_18_2 to 0x13, V1_19_0 to 0x15, V1_19_1 to 0x16)
            .register(null                                         , ClientLocationPacketReader                  , V1_08_0 to 0x03, V1_09_0 to 0x0F, V1_09_1 to 0x0F, V1_09_2 to 0x0F, V1_09_4 to 0x0F, V1_10_0 to 0x0F, V1_11_0 to 0x0F, V1_11_1 to 0x0F, V1_12_0 to 0x0D, V1_12_1 to 0x0C, V1_12_2 to 0x0C, V1_13_0 to 0x0F, V1_13_1 to 0x0F, V1_13_2 to 0x0F, V1_14_0 to 0x14, V1_14_1 to 0x14, V1_14_2 to 0x14, V1_14_3 to 0x14, V1_14_4 to 0x14, V1_15_0 to 0x14, V1_15_1 to 0x14, V1_15_2 to 0x14, V1_16_0 to 0x15, V1_16_1 to 0x15, V1_16_2 to 0x15, V1_16_3 to 0x15, V1_16_4 to 0x15, V1_17_0 to 0x14, V1_17_1 to 0x14, V1_18_2 to 0x14, V1_18_2 to 0x14, V1_19_0 to 0x16, V1_19_1 to 0x17)
            .register(ClientVehicleLocationPacket::class           , ClientVehicleLocationPacketReader           ,                  V1_09_0 to 0x10, V1_09_1 to 0x10, V1_09_2 to 0x10, V1_09_4 to 0x10, V1_10_0 to 0x10, V1_11_0 to 0x10, V1_11_1 to 0x10, V1_12_0 to 0x11, V1_12_1 to 0x10, V1_12_2 to 0x10, V1_13_0 to 0x13, V1_13_1 to 0x13, V1_13_2 to 0x13, V1_14_0 to 0x15, V1_14_1 to 0x15, V1_14_2 to 0x15, V1_14_3 to 0x15, V1_14_4 to 0x15, V1_15_0 to 0x15, V1_15_1 to 0x15, V1_15_2 to 0x15, V1_16_0 to 0x16, V1_16_1 to 0x16, V1_16_2 to 0x16, V1_16_3 to 0x16, V1_16_4 to 0x16, V1_17_0 to 0x15, V1_17_1 to 0x15, V1_18_2 to 0x15, V1_18_2 to 0x15, V1_19_0 to 0x17, V1_19_1 to 0x18)
            .register(ClientSteerBoatPacket::class                 , ClientSteerBoatPacketReader                 ,                  V1_09_0 to 0x11, V1_09_1 to 0x11, V1_09_2 to 0x11, V1_09_4 to 0x11, V1_10_0 to 0x11, V1_11_0 to 0x11, V1_11_1 to 0x11, V1_12_0 to 0x12, V1_12_1 to 0x11, V1_12_2 to 0x11, V1_13_0 to 0x14, V1_13_1 to 0x14, V1_13_2 to 0x14, V1_14_0 to 0x16, V1_14_1 to 0x16, V1_14_2 to 0x16, V1_14_3 to 0x16, V1_14_4 to 0x16, V1_15_0 to 0x16, V1_15_1 to 0x16, V1_15_2 to 0x16, V1_16_0 to 0x17, V1_16_1 to 0x17, V1_16_2 to 0x17, V1_16_3 to 0x17, V1_16_4 to 0x17, V1_17_0 to 0x16, V1_17_1 to 0x16, V1_18_2 to 0x16, V1_18_2 to 0x16, V1_19_0 to 0x18, V1_19_1 to 0x19)
            .register(ClientItemPickPacket::class                  , ClientItemPickPacketReader                  ,                                                                                                                                                                                            V1_13_0 to 0x15, V1_13_1 to 0x15, V1_13_2 to 0x15, V1_14_0 to 0x17, V1_14_1 to 0x17, V1_14_2 to 0x17, V1_14_3 to 0x17, V1_14_4 to 0x17, V1_15_0 to 0x17, V1_15_1 to 0x17, V1_15_2 to 0x17, V1_16_0 to 0x18, V1_16_1 to 0x18, V1_16_2 to 0x18, V1_16_3 to 0x18, V1_16_4 to 0x18, V1_17_0 to 0x17, V1_17_1 to 0x17, V1_18_2 to 0x17, V1_18_2 to 0x17, V1_19_0 to 0x19, V1_19_1 to 0x1A)
            .register(ClientCraftPacket::class                     , ClientCraftPacketReader                     ,                                                                                                                                         V1_12_0 to 0x12, V1_12_1 to 0x12, V1_12_2 to 0x12, V1_13_0 to 0x16, V1_13_1 to 0x16, V1_13_2 to 0x16, V1_14_0 to 0x18, V1_14_1 to 0x18, V1_14_2 to 0x18, V1_14_3 to 0x18, V1_14_4 to 0x18, V1_15_0 to 0x18, V1_15_1 to 0x18, V1_15_2 to 0x18, V1_16_0 to 0x19, V1_16_1 to 0x19, V1_16_2 to 0x19, V1_16_3 to 0x19, V1_16_4 to 0x19, V1_17_0 to 0x18, V1_17_1 to 0x18, V1_18_2 to 0x18, V1_18_2 to 0x18, V1_19_0 to 0x1A, V1_19_1 to 0x1B)
            .register(ClientAbilitiesPacket::class                 , ClientAbilitiesPacketReader                 , V1_08_0 to 0x13, V1_09_0 to 0x12, V1_09_1 to 0x12, V1_09_2 to 0x12, V1_09_4 to 0x12, V1_10_0 to 0x12, V1_11_0 to 0x12, V1_11_1 to 0x12, V1_12_0 to 0x13, V1_12_1 to 0x13, V1_12_2 to 0x13, V1_13_0 to 0x17, V1_13_1 to 0x17, V1_13_2 to 0x17, V1_14_0 to 0x19, V1_14_1 to 0x19, V1_14_2 to 0x19, V1_14_3 to 0x19, V1_14_4 to 0x19, V1_15_0 to 0x19, V1_15_1 to 0x19, V1_15_2 to 0x19, V1_16_0 to 0x1A, V1_16_1 to 0x1A, V1_16_2 to 0x1A, V1_16_3 to 0x1A, V1_16_4 to 0x1A, V1_17_0 to 0x19, V1_17_1 to 0x19, V1_18_2 to 0x19, V1_18_2 to 0x19, V1_19_0 to 0x21, V1_19_1 to 0x22)
            .register(ClientActionPacket::class                    , ClientActionPacketReader                    , V1_08_0 to 0x07, V1_09_0 to 0x13, V1_09_1 to 0x13, V1_09_2 to 0x13, V1_09_4 to 0x13, V1_10_0 to 0x13, V1_11_0 to 0x13, V1_11_1 to 0x13, V1_12_0 to 0x14, V1_12_1 to 0x14, V1_12_2 to 0x14, V1_13_0 to 0x18, V1_13_1 to 0x18, V1_13_2 to 0x18, V1_14_0 to 0x1A, V1_14_1 to 0x1A, V1_14_2 to 0x1A, V1_14_3 to 0x1A, V1_14_4 to 0x1A, V1_15_0 to 0x1A, V1_15_1 to 0x1A, V1_15_2 to 0x1A, V1_16_0 to 0x1B, V1_16_1 to 0x1B, V1_16_2 to 0x1B, V1_16_3 to 0x1B, V1_16_4 to 0x1B, V1_17_0 to 0x1A, V1_17_1 to 0x1A, V1_18_2 to 0x1A, V1_18_2 to 0x1A, V1_19_0 to 0x1C, V1_19_1 to 0x1D)
            .register(ClientPlayerActionPacket::class              , ClientPlayerActionPacketReader              , V1_08_0 to 0x0B, V1_09_0 to 0x14, V1_09_1 to 0x14, V1_09_2 to 0x14, V1_09_4 to 0x14, V1_10_0 to 0x14, V1_11_0 to 0x14, V1_11_1 to 0x14, V1_12_0 to 0x15, V1_12_1 to 0x15, V1_12_2 to 0x15, V1_13_0 to 0x19, V1_13_1 to 0x19, V1_13_2 to 0x19, V1_14_0 to 0x1B, V1_14_1 to 0x1B, V1_14_2 to 0x1B, V1_14_3 to 0x1B, V1_14_4 to 0x1B, V1_15_0 to 0x1B, V1_15_1 to 0x1B, V1_15_2 to 0x1B, V1_16_0 to 0x1C, V1_16_1 to 0x1C, V1_16_2 to 0x1C, V1_16_3 to 0x1C, V1_16_4 to 0x1C, V1_17_0 to 0x1B, V1_17_1 to 0x1B, V1_18_2 to 0x1B, V1_18_2 to 0x1B, V1_19_0 to 0x1D, V1_19_1 to 0x1E)
            .register(ClientSteerPacket::class                     , ClientSteerPacketReader                     , V1_08_0 to 0x0C, V1_09_0 to 0x15, V1_09_1 to 0x15, V1_09_2 to 0x15, V1_09_4 to 0x15, V1_10_0 to 0x15, V1_11_0 to 0x15, V1_11_1 to 0x15, V1_12_0 to 0x16, V1_12_1 to 0x16, V1_12_2 to 0x16, V1_13_0 to 0x1A, V1_13_1 to 0x1A, V1_13_2 to 0x1A, V1_14_0 to 0x1C, V1_14_1 to 0x1C, V1_14_2 to 0x1C, V1_14_3 to 0x1C, V1_14_4 to 0x1C, V1_15_0 to 0x1C, V1_15_1 to 0x1C, V1_15_2 to 0x1C, V1_16_0 to 0x1D, V1_16_1 to 0x1D, V1_16_2 to 0x1D, V1_16_3 to 0x1D, V1_16_4 to 0x1D, V1_17_0 to 0x1C, V1_17_1 to 0x1C, V1_18_2 to 0x1C, V1_18_2 to 0x1C, V1_19_0 to 0x1E, V1_19_1 to 0x1F)
            .register(ClientPongPacket::class                      , ClientPongPacketReader                      ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                            V1_17_0 to 0x1D, V1_17_1 to 0x1D, V1_18_0 to 0x1D, V1_18_2 to 0x1D, V1_19_0 to 0x1F, V1_19_1 to 0x20)
            .register(ClientRecipeBookStatePacket::class           , ClientRecipeBookStatePacketReader           ,                                                                                                                                                                                                                                                                                                                                                                                                                         V1_16_2 to 0x1E, V1_16_3 to 0x1E, V1_16_4 to 0x1E, V1_17_0 to 0x1E, V1_17_1 to 0x1E, V1_18_2 to 0x1E, V1_18_2 to 0x1E, V1_19_0 to 0x20, V1_19_1 to 0x21)
            .register(ClientRecipeBookDisplayRecipePacket::class   , ClientRecipeBookDisplayRecipePacketReader   ,                                                                                                                                                                                                                                                                                                                                                                                                                         V1_16_2 to 0x1F, V1_16_3 to 0x1F, V1_16_4 to 0x1F, V1_17_0 to 0x1F, V1_17_1 to 0x1F, V1_18_2 to 0x1F, V1_18_2 to 0x1F, V1_19_0 to 0x21, V1_19_1 to 0x22)
            .register(ClientItemNamePacket::class                  , ClientItemNamePacketReader                  ,                                                                                                                                                                                            V1_13_0 to 0x1C, V1_13_1 to 0x1C, V1_13_2 to 0x1C, V1_14_0 to 0x1E, V1_14_1 to 0x1E, V1_14_2 to 0x1E, V1_14_3 to 0x1E, V1_14_4 to 0x1E, V1_15_0 to 0x1E, V1_15_1 to 0x1E, V1_15_2 to 0x1E, V1_16_0 to 0x1F, V1_16_1 to 0x1F, V1_16_2 to 0x20, V1_16_3 to 0x20, V1_16_4 to 0x20, V1_17_0 to 0x20, V1_17_1 to 0x20, V1_18_2 to 0x20, V1_18_2 to 0x20, V1_19_0 to 0x22, V1_19_1 to 0x23)
            .register(ClientResourcePackStatusPacket::class        , ClientResourcePackStatusPacketReader        , V1_08_0 to 0x19, V1_09_0 to 0x16, V1_09_1 to 0x16, V1_09_2 to 0x16, V1_09_4 to 0x16, V1_10_0 to 0x16, V1_11_0 to 0x16, V1_11_1 to 0x16, V1_12_0 to 0x18, V1_12_1 to 0x18, V1_12_2 to 0x18, V1_13_0 to 0x1D, V1_13_1 to 0x1D, V1_13_2 to 0x1D, V1_14_0 to 0x1F, V1_14_1 to 0x1F, V1_14_2 to 0x1F, V1_14_3 to 0x1F, V1_14_4 to 0x1F, V1_15_0 to 0x1F, V1_15_1 to 0x1F, V1_15_2 to 0x1F, V1_16_0 to 0x20, V1_16_1 to 0x20, V1_16_2 to 0x21, V1_16_3 to 0x21, V1_16_4 to 0x21, V1_17_0 to 0x21, V1_17_1 to 0x21, V1_18_2 to 0x21, V1_18_2 to 0x21, V1_19_0 to 0x23, V1_19_1 to 0x24)
            .register(ClientAdvancementTabPacket::class            , ClientAdvancementTabPacketReader            ,                                                                                                                                         V1_12_0 to 0x19, V1_12_1 to 0x19, V1_12_2 to 0x19, V1_13_0 to 0x1E, V1_13_1 to 0x1E, V1_13_2 to 0x1E, V1_14_0 to 0x20, V1_14_1 to 0x20, V1_14_2 to 0x20, V1_14_3 to 0x20, V1_14_4 to 0x20, V1_15_0 to 0x20, V1_15_1 to 0x20, V1_15_2 to 0x20, V1_16_0 to 0x21, V1_16_1 to 0x21, V1_16_2 to 0x22, V1_16_3 to 0x22, V1_16_4 to 0x22, V1_17_0 to 0x22, V1_17_1 to 0x22, V1_18_2 to 0x22, V1_18_2 to 0x22, V1_19_0 to 0x24, V1_19_1 to 0x25)
            .register(ClientTradePacket::class                     , ClientTradePacketReader                     ,                                                                                                                                                                                            V1_13_0 to 0x1F, V1_13_1 to 0x1F, V1_13_2 to 0x1F, V1_14_0 to 0x21, V1_14_1 to 0x21, V1_14_2 to 0x21, V1_14_3 to 0x21, V1_14_4 to 0x21, V1_15_0 to 0x21, V1_15_1 to 0x21, V1_15_2 to 0x21, V1_16_0 to 0x22, V1_16_1 to 0x22, V1_16_2 to 0x23, V1_16_3 to 0x23, V1_16_4 to 0x23, V1_17_0 to 0x23, V1_17_1 to 0x23, V1_18_2 to 0x23, V1_18_2 to 0x23, V1_19_0 to 0x25, V1_19_1 to 0x26)
            .register(ClientBeaconUpdatePacket::class              , ClientBeaconUpdatePacketReader              ,                                                                                                                                                                                            V1_13_0 to 0x20, V1_13_1 to 0x20, V1_13_2 to 0x20, V1_14_0 to 0x22, V1_14_1 to 0x22, V1_14_2 to 0x22, V1_14_3 to 0x22, V1_14_4 to 0x22, V1_15_0 to 0x22, V1_15_1 to 0x22, V1_15_2 to 0x22, V1_16_0 to 0x23, V1_16_1 to 0x23, V1_16_2 to 0x24, V1_16_3 to 0x24, V1_16_4 to 0x24, V1_17_0 to 0x24, V1_17_1 to 0x24, V1_18_2 to 0x24, V1_18_2 to 0x24, V1_19_0 to 0x26, V1_19_1 to 0x27)
            .register(ClientHotbarPacket::class                    , ClientHotbarPacketReader                    , V1_08_0 to 0x09, V1_09_0 to 0x17, V1_09_1 to 0x17, V1_09_2 to 0x17, V1_09_4 to 0x17, V1_10_0 to 0x17, V1_11_0 to 0x17, V1_11_1 to 0x17, V1_12_0 to 0x1A, V1_12_1 to 0x1A, V1_12_2 to 0x1A, V1_13_0 to 0x21, V1_13_1 to 0x21, V1_13_2 to 0x21, V1_14_0 to 0x23, V1_14_1 to 0x23, V1_14_2 to 0x23, V1_14_3 to 0x23, V1_14_4 to 0x23, V1_15_0 to 0x23, V1_15_1 to 0x23, V1_15_2 to 0x23, V1_16_0 to 0x24, V1_16_1 to 0x24, V1_16_2 to 0x25, V1_16_3 to 0x25, V1_16_4 to 0x25, V1_17_0 to 0x25, V1_17_1 to 0x25, V1_18_2 to 0x25, V1_18_2 to 0x25, V1_19_0 to 0x27, V1_19_1 to 0x28)
            .register(ClientCommandBlockUpdatePacket::class        , ClientCommandBlockUpdatePacketReader        ,                                                                                                                                                                                            V1_13_0 to 0x22, V1_13_1 to 0x22, V1_13_2 to 0x22, V1_14_0 to 0x24, V1_14_1 to 0x24, V1_14_2 to 0x24, V1_14_3 to 0x24, V1_14_4 to 0x24, V1_15_0 to 0x24, V1_15_1 to 0x24, V1_15_2 to 0x24, V1_16_0 to 0x25, V1_16_1 to 0x25, V1_16_2 to 0x26, V1_16_3 to 0x26, V1_16_4 to 0x26, V1_17_0 to 0x26, V1_17_1 to 0x26, V1_18_2 to 0x26, V1_18_2 to 0x26, V1_19_0 to 0x28, V1_19_1 to 0x29)
            .register(ClientCommandBlockMinecartUpdatePacket::class, ClientCommandBlockMinecartUpdatePacketReader,                                                                                                                                                                                            V1_13_0 to 0x23, V1_13_1 to 0x23, V1_13_2 to 0x23, V1_14_0 to 0x25, V1_14_1 to 0x25, V1_14_2 to 0x25, V1_14_3 to 0x25, V1_14_4 to 0x25, V1_15_0 to 0x25, V1_15_1 to 0x25, V1_15_2 to 0x25, V1_16_0 to 0x26, V1_16_1 to 0x26, V1_16_2 to 0x27, V1_16_3 to 0x27, V1_16_4 to 0x27, V1_17_0 to 0x27, V1_17_1 to 0x27, V1_18_2 to 0x27, V1_18_2 to 0x27, V1_19_0 to 0x29, V1_19_1 to 0x2A)
            .register(ClientCreativeInventorySlotPacket::class     , ClientCreativeInventorySlotPacketReader     , V1_08_0 to 0x10, V1_09_0 to 0x18, V1_09_1 to 0x18, V1_09_2 to 0x18, V1_09_4 to 0x18, V1_10_0 to 0x18, V1_11_0 to 0x18, V1_11_1 to 0x18, V1_12_0 to 0x1B, V1_12_1 to 0x1B, V1_12_2 to 0x1B, V1_13_0 to 0x24, V1_13_1 to 0x24, V1_13_2 to 0x24, V1_14_0 to 0x26, V1_14_1 to 0x26, V1_14_2 to 0x26, V1_14_3 to 0x26, V1_14_4 to 0x26, V1_15_0 to 0x26, V1_15_1 to 0x26, V1_15_2 to 0x26, V1_16_0 to 0x27, V1_16_1 to 0x27, V1_16_2 to 0x28, V1_16_3 to 0x28, V1_16_4 to 0x28, V1_17_0 to 0x28, V1_17_1 to 0x28, V1_18_2 to 0x28, V1_18_2 to 0x28, V1_19_0 to 0x2A, V1_19_1 to 0x2B)
            .register(ClientJigsawBlockUpdatePacket::class         , ClientJigsawBlockUpdatePacketReader         ,                                                                                                                                                                                                                                               V1_14_0 to 0x27, V1_14_1 to 0x27, V1_14_2 to 0x27, V1_14_3 to 0x27, V1_14_4 to 0x27, V1_15_0 to 0x27, V1_15_1 to 0x27, V1_15_2 to 0x27, V1_16_0 to 0x28, V1_16_1 to 0x28, V1_16_2 to 0x29, V1_16_3 to 0x29, V1_16_4 to 0x29, V1_17_0 to 0x29, V1_17_1 to 0x29, V1_18_2 to 0x29, V1_18_2 to 0x29, V1_19_0 to 0x2B, V1_19_1 to 0x2C)
            .register(ClientStructureBlockUpdatePacket::class      , ClientStructureBlockUpdatePacketReader      ,                                                                                                                                                                                            V1_13_0 to 0x25, V1_13_1 to 0x25, V1_13_2 to 0x25, V1_14_0 to 0x28, V1_14_1 to 0x28, V1_14_2 to 0x28, V1_14_3 to 0x28, V1_14_4 to 0x28, V1_15_0 to 0x28, V1_15_1 to 0x28, V1_15_2 to 0x28, V1_16_0 to 0x29, V1_16_1 to 0x29, V1_16_2 to 0x2A, V1_16_3 to 0x2A, V1_16_4 to 0x2A, V1_17_0 to 0x2A, V1_17_1 to 0x2A, V1_18_2 to 0x2A, V1_18_2 to 0x2A, V1_19_0 to 0x2C, V1_19_1 to 0x2D)
            .register(ClientSignUpdatePacket::class                , ClientSignUpdatePacketReader                , V1_08_0 to 0x12, V1_09_0 to 0x19, V1_09_1 to 0x19, V1_09_2 to 0x19, V1_09_4 to 0x19, V1_10_0 to 0x19, V1_11_0 to 0x19, V1_11_1 to 0x19, V1_12_0 to 0x1C, V1_12_1 to 0x1C, V1_12_2 to 0x1C, V1_13_0 to 0x26, V1_13_1 to 0x26, V1_13_2 to 0x26, V1_14_0 to 0x29, V1_14_1 to 0x29, V1_14_2 to 0x29, V1_14_3 to 0x29, V1_14_4 to 0x29, V1_15_0 to 0x29, V1_15_1 to 0x29, V1_15_2 to 0x29, V1_16_0 to 0x2A, V1_16_1 to 0x2A, V1_16_2 to 0x2B, V1_16_3 to 0x2B, V1_16_4 to 0x2B, V1_17_0 to 0x2B, V1_17_1 to 0x2B, V1_18_2 to 0x2B, V1_18_2 to 0x2B, V1_19_0 to 0x2D, V1_19_1 to 0x2E)
            .register(ClientSwingArmPacket::class                  , ClientSwingArmPacketReader                  , V1_08_0 to 0x0A, V1_09_0 to 0x1A, V1_09_1 to 0x1A, V1_09_2 to 0x1A, V1_09_4 to 0x1A, V1_10_0 to 0x1A, V1_11_0 to 0x1A, V1_11_1 to 0x1A, V1_12_0 to 0x1D, V1_12_1 to 0x1D, V1_12_2 to 0x1D, V1_13_0 to 0x27, V1_13_1 to 0x27, V1_13_2 to 0x27, V1_14_0 to 0x2A, V1_14_1 to 0x2A, V1_14_2 to 0x2A, V1_14_3 to 0x2A, V1_14_4 to 0x2A, V1_15_0 to 0x2A, V1_15_1 to 0x2A, V1_15_2 to 0x2A, V1_16_0 to 0x2B, V1_16_1 to 0x2B, V1_16_2 to 0x2C, V1_16_3 to 0x2C, V1_16_4 to 0x2C, V1_17_0 to 0x2C, V1_17_1 to 0x2C, V1_18_2 to 0x2C, V1_18_2 to 0x2C, V1_19_0 to 0x2E, V1_19_1 to 0x2F)
            .register(ClientSpectatePacket::class                  , ClientSpectatePacketReader                  , V1_08_0 to 0x18, V1_09_0 to 0x1B, V1_09_1 to 0x1B, V1_09_2 to 0x1B, V1_09_4 to 0x1B, V1_10_0 to 0x1B, V1_11_0 to 0x1B, V1_11_1 to 0x1B, V1_12_0 to 0x1E, V1_12_1 to 0x1E, V1_12_2 to 0x1E, V1_13_0 to 0x28, V1_13_1 to 0x28, V1_13_2 to 0x28, V1_14_0 to 0x2B, V1_14_1 to 0x2B, V1_14_2 to 0x2B, V1_14_3 to 0x2B, V1_14_4 to 0x2B, V1_15_0 to 0x2B, V1_15_1 to 0x2B, V1_15_2 to 0x2B, V1_16_0 to 0x2C, V1_16_1 to 0x2C, V1_16_2 to 0x2D, V1_16_3 to 0x2D, V1_16_4 to 0x2D, V1_17_0 to 0x2D, V1_17_1 to 0x2D, V1_18_2 to 0x2D, V1_18_2 to 0x2D, V1_19_0 to 0x2F, V1_19_1 to 0x30)
            .register(ClientBlockPlacePacket::class                , ClientBlockPlacePacketReader                , V1_08_0 to 0x08, V1_09_0 to 0x1C, V1_09_1 to 0x1C, V1_09_2 to 0x1C, V1_09_4 to 0x1C, V1_10_0 to 0x1C, V1_11_0 to 0x1C, V1_11_1 to 0x1C, V1_12_0 to 0x1F, V1_12_1 to 0x1F, V1_12_2 to 0x1F, V1_13_0 to 0x29, V1_13_1 to 0x29, V1_13_2 to 0x29, V1_14_0 to 0x2C, V1_14_1 to 0x2C, V1_14_2 to 0x2C, V1_14_3 to 0x2C, V1_14_4 to 0x2C, V1_15_0 to 0x2C, V1_15_1 to 0x2C, V1_15_2 to 0x2C, V1_16_0 to 0x2D, V1_16_1 to 0x2D, V1_16_2 to 0x2E, V1_16_3 to 0x2E, V1_16_4 to 0x2E, V1_17_0 to 0x2E, V1_17_1 to 0x2E, V1_18_2 to 0x2E, V1_18_2 to 0x2E, V1_19_0 to 0x30, V1_19_1 to 0x31)
            .register(ClientItemUsePacket::class                   , ClientItemUsePacketReader                   ,                  V1_09_0 to 0x1D, V1_09_1 to 0x1D, V1_09_2 to 0x1D, V1_09_4 to 0x1D, V1_10_0 to 0x1D, V1_11_0 to 0x1D, V1_11_1 to 0x1D, V1_12_0 to 0x20, V1_12_1 to 0x20, V1_12_2 to 0x20, V1_13_0 to 0x2A, V1_13_1 to 0x2A, V1_13_2 to 0x2A, V1_14_0 to 0x2D, V1_14_1 to 0x2D, V1_14_2 to 0x2D, V1_14_3 to 0x2D, V1_14_4 to 0x2D, V1_15_0 to 0x2D, V1_15_1 to 0x2D, V1_15_2 to 0x2D, V1_16_0 to 0x2E, V1_16_1 to 0x2E, V1_16_2 to 0x2F, V1_16_3 to 0x2F, V1_16_4 to 0x2F, V1_17_0 to 0x2F, V1_17_1 to 0x2F, V1_18_2 to 0x2F, V1_18_2 to 0x2F, V1_19_0 to 0x31, V1_19_1 to 0x32)
            .build(),
        Protocol.Builder()
            .register(ServerObjectAddPacket::class             , ServerObjectAddPacketReader               , V1_16_4 to 0x00, V1_18_2 to 0x00                                  )
            .register(ServerExperienceOrbAddPacket::class      , ServerExperienceOrbAddPacketReader        , V1_16_4 to 0x01, V1_18_2 to 0x01, V1_19_0 to 0x01, V1_19_1 to 0x01)
            .register(ServerEntityAddPacket::class             , ServerEntityAddPacketReader               , V1_16_4 to 0x02, V1_18_2 to 0x02, V1_19_0 to 0x00, V1_19_1 to 0x00)
            .register(ServerPaintingAddPacket::class           , ServerPaintingAddPacketReader             , V1_16_4 to 0x03, V1_18_2 to 0x03                                  )
            .register(ServerPlayerAddPacket::class             , ServerPlayerAddPacketReader               , V1_16_4 to 0x04, V1_18_2 to 0x04, V1_19_0 to 0x02, V1_19_1 to 0x02)
            .register(ServerEntityAnimationPacket::class       , ServerEntityAnimationPacketReader         , V1_16_4 to 0x05, V1_18_2 to 0x06, V1_19_0 to 0x03, V1_19_1 to 0x03)
            .register(ServerActionConfirmPacket::class         , ServerActionConfirmPacketReader           , V1_16_4 to 0x07, V1_18_2 to 0x08, V1_19_0 to 0x05, V1_19_1 to 0x05)
            .register(ServerBlockBreakAnimationPacket::class   , ServerBlockBreakAnimationPacketReader     , V1_16_4 to 0x08, V1_18_2 to 0x09, V1_19_0 to 0x06, V1_19_1 to 0x06)
            .register(ServerBlockEntityPacket::class           , ServerBlockEntityPacketReader             , V1_16_4 to 0x09, V1_18_2 to 0x0A, V1_19_0 to 0x07, V1_19_1 to 0x07)
            .register(ServerBlockEventPacket::class            , ServerBlockEventPacketReader              , V1_16_4 to 0x0A, V1_18_2 to 0x0B, V1_19_0 to 0x08, V1_19_1 to 0x08)
            .register(ServerBlockUpdatePacket::class           , ServerBlockUpdatePacketReader             , V1_16_4 to 0x0B, V1_18_2 to 0x0C, V1_19_0 to 0x09, V1_19_1 to 0x09)
            .register(ServerBossBarPacket::class               , ServerBossBarPacketReader                 , V1_16_4 to 0x0C, V1_18_2 to 0x0D, V1_19_0 to 0x0A, V1_19_1 to 0x0A)
            .register(ServerDifficultyPacket::class            , ServerDifficultyPacketReader              , V1_16_4 to 0x0D, V1_18_2 to 0x0E, V1_19_0 to 0x0B, V1_19_1 to 0x0B)
            .register(ServerPlayerChatPreviewPacket::class     , ServerPlayerChatPreviewPacketReader       ,                                   V1_19_0 to 0x0C, V1_19_1 to 0x0C)
            .register(null                                     , ServerTitleClearPacketReader              ,                  V1_18_2 to 0x10, V1_19_0 to 0x0D, V1_19_1 to 0x0D)
            .register(ServerCommandSuggestPacket::class        , ServerCommandSuggestPacketReader          , V1_16_4 to 0x0F, V1_18_2 to 0x11, V1_19_0 to 0x0E, V1_19_1 to 0x0E)
            .register(ServerWindowConfirmPacket::class         , ServerWindowConfirmPacketReader           , V1_16_4 to 0x11                                                   )
            .register(ServerWindowClosePacket::class           , ServerWindowClosePacketReader             , V1_16_4 to 0x12, V1_18_2 to 0x13, V1_19_0 to 0x10, V1_19_1 to 0x10)
            .register(ServerInventoryContentPacket::class      , ServerInventoryContentPacketReader        , V1_16_4 to 0x13, V1_18_2 to 0x14, V1_19_0 to 0x11, V1_19_1 to 0x11)
            .register(ServerWindowPropertyPacket::class        , ServerWindowPropertyPacketReader          , V1_16_4 to 0x14, V1_18_2 to 0x15, V1_19_0 to 0x12, V1_19_1 to 0x12)
            .register(ServerInventorySlotPacket::class         , ServerInventorySlotPacketReader           , V1_16_4 to 0x15, V1_18_2 to 0x16, V1_19_0 to 0x13, V1_19_1 to 0x13)
            .register(ServerItemCooldownPacket::class          , ServerItemCooldownPacketReader            , V1_16_4 to 0x16, V1_18_2 to 0x17, V1_19_0 to 0x14, V1_19_1 to 0x14)

            .register(ServerCustomPayloadPacket::class         , ServerCustomPayloadPacketReader           , V1_16_4 to 0x17, V1_18_2 to 0x18, V1_19_0 to 0x15, V1_19_1 to 0x16)
            .register(ServerNamedSoundPacket::class            , ServerNamedSoundPacketReader              , V1_16_4 to 0x18, V1_18_2 to 0x19, V1_19_0 to 0x16, V1_19_1 to 0x17)
            //.register(ServerChatDeletePacket::class          , ServerChatDeletePacket                    ,                                                  , V1_19_1 to 0x18)
            .register(ServerDisconnectPacket::class            , ServerDisconnectPacketReader              , V1_16_4 to 0x19, V1_18_2 to 0x1A, V1_19_0 to 0x17, V1_19_1 to 0x19)
            .register(ServerEntityEventPacket::class           , ServerEntityEventPacketReader             , V1_16_4 to 0x1A, V1_18_2 to 0x1B, V1_19_0 to 0x18, V1_19_1 to 0x1A)
            .register(ServerExplosionPacket::class             , ServerExplosionPacketReader               , V1_16_4 to 0x1B, V1_18_2 to 0x1C, V1_19_0 to 0x19, V1_19_1 to 0x1B)
            .register(ServerChunkUnloadPacket::class           , ServerChunkUnloadPacketReader             , V1_16_4 to 0x1C, V1_18_2 to 0x1D, V1_19_0 to 0x1A, V1_19_1 to 0x1C)
            .register(ServerGameStatePacket::class             , ServerGameStatePacketReader               , V1_16_4 to 0x1D, V1_18_2 to 0x1E, V1_19_0 to 0x1B, V1_19_1 to 0x1D)
            .register(ServerHorseWindowOpenPacket::class       , ServerHorseWindowOpenPacketReader         , V1_16_4 to 0x1E, V1_18_2 to 0x1F, V1_19_0 to 0x1C, V1_19_1 to 0x1E)
            .register(null                                     , ServerBorderInitializePacketReader        ,                  V1_18_2 to 0x20, V1_19_0 to 0x1D, V1_19_1 to 0x1F)
            .register(ServerKeepAlivePacket::class             , ServerKeepAlivePacketReader               , V1_16_4 to 0x1F, V1_18_2 to 0x21, V1_19_0 to 0x1E, V1_19_1 to 0x20)
            .register(ServerChunkPacket::class                 , ServerChunkPacketReader                   , V1_16_4 to 0x20, V1_18_2 to 0x22, V1_19_0 to 0x1F, V1_19_1 to 0x21)
            .register(ServerWorldEventPacket::class            , ServerWorldEventPacketReader              , V1_16_4 to 0x21, V1_18_2 to 0x23, V1_19_0 to 0x20, V1_19_1 to 0x22)
            .register(ServerParticlePacket::class              , ServerParticlePacketReader                , V1_16_4 to 0x22, V1_18_2 to 0x24, V1_19_0 to 0x21, V1_19_1 to 0x23)

            .register(ServerWorldPacket::class                 , ServerWorldPacketReader                   , V1_16_4 to 0x24, V1_18_2 to 0x26, V1_19_0 to 0x23, V1_19_1 to 0x25)
            .register(ServerMapPacket::class                   , ServerMapPacketReader                     , V1_16_4 to 0x25, V1_18_2 to 0x27, V1_19_0 to 0x24, V1_19_1 to 0x26)
            .register(ServerTradePacket::class                 , ServerTradePacketReader                   , V1_16_4 to 0x26, V1_18_2 to 0x28, V1_19_0 to 0x25, V1_19_1 to 0x27)
            .register(ServerEntityMoveRotatePacket::class      , ServerEntityMovePacketReader              , V1_16_4 to 0x27, V1_18_2 to 0x29, V1_19_0 to 0x26, V1_19_1 to 0x28)
            .register(null                                     , ServerEntityMoveRotatePacketReader        , V1_16_4 to 0x28, V1_18_2 to 0x2A, V1_19_0 to 0x27, V1_19_1 to 0x29)
            .register(null                                     , ServerEntityRotatePacketReader            , V1_16_4 to 0x29, V1_18_2 to 0x2B, V1_19_0 to 0x28, V1_19_1 to 0x2A)
            .register(null                                     , ServerEntityLocationPacketReader          , V1_16_4 to 0x2A                                                   )
            .register(ServerVehicleLocationPacket::class       , ServerVehicleLocationPacketReader         , V1_16_4 to 0x2B, V1_18_2 to 0x2C, V1_19_0 to 0x29, V1_19_1 to 0x2B)
            .register(ServerBookOpenPacket::class              , ServerBookOpenPacketReader                , V1_16_4 to 0x2C, V1_18_2 to 0x2D, V1_19_0 to 0x2A, V1_19_1 to 0x2C)
            .register(ServerWindowOpenPacket::class            , ServerWindowOpenPacketReader              , V1_16_4 to 0x2D, V1_18_2 to 0x2E, V1_19_0 to 0x2B, V1_19_1 to 0x2D)
            .register(ServerSignUpdatePacket::class            , ServerSignUpdatePacketReader              , V1_16_4 to 0x2E, V1_18_2 to 0x2F, V1_19_0 to 0x2C, V1_19_1 to 0x2E)
            .register(ServerPingPacket::class                  , ServerPingPacketReader                    ,                  V1_18_2 to 0x30, V1_19_0 to 0x2D, V1_19_1 to 0x2F)
            .register(ServerCraftPacket::class                 , ServerCraftPacketReader                   , V1_16_4 to 0x2F, V1_18_2 to 0x31, V1_19_0 to 0x2E, V1_19_1 to 0x30)
            .register(ServerAbilitiesPacket::class             , ServerAbilitiesPacketReader               , V1_16_4 to 0x30, V1_18_2 to 0x32, V1_19_0 to 0x2F, V1_19_1 to 0x31)
            //.register(ServerPlayerChatHeaderPacket::class    , ServerPlayerChatHeaderPacket              ,                                                  , V1_19_1 to 0x32)
            .register(ServerPlayerChatPacket::class            , ServerPlayerChatPacketReader              ,                                   V1_19_0 to 0x30, V1_19_1 to 0x33)
            .register(ServerPlayerCombatEventPacket::class     , ServerPlayerCombatEventPacketReader       , V1_16_4 to 0x31, V1_18_2 to 0x33, V1_19_0 to 0x31, V1_19_1 to 0x34)
            .register(null                                     , ServerPlayerCombatEventEndPacketReader    ,                  V1_18_2 to 0x33, V1_19_0 to 0x31, V1_19_1 to 0x34)
            .register(null                                     , ServerPlayerCombatEventEnterPacketReader  ,                  V1_18_2 to 0x34, V1_19_0 to 0x32, V1_19_1 to 0x35)
            .register(null                                     , ServerPlayerCombatEventDeathPacketReader  ,                  V1_18_2 to 0x35, V1_19_0 to 0x33, V1_19_1 to 0x36)
            .register(ServerPlayerListPacket::class            , ServerPlayerListPacketReader              , V1_16_4 to 0x32, V1_18_2 to 0x36, V1_19_0 to 0x34, V1_19_1 to 0x37)
            .register(ServerLookAtPacket::class                , ServerLookAtPacketReader                  , V1_16_4 to 0x33, V1_18_2 to 0x37, V1_19_0 to 0x35, V1_19_1 to 0x38)
            .register(ServerLocationPacket::class              , ServerLocationPacketReader                , V1_16_4 to 0x34, V1_18_2 to 0x38, V1_19_0 to 0x36, V1_19_1 to 0x39)
            .register(ServerRecipeBookPacket::class            , ServerRecipeBookPacketReader              , V1_16_4 to 0x35, V1_18_2 to 0x39, V1_19_0 to 0x37, V1_19_1 to 0x3A)
            .register(ServerEntitiesRemovePacket::class        , ServerEntitiesRemovePacketReader          , V1_16_4 to 0x36, V1_18_2 to 0x3A, V1_19_0 to 0x38, V1_19_1 to 0x3B)
            .register(ServerEntityEffectRevokePacket::class    , ServerEntityEffectRevokePacketReader      , V1_16_4 to 0x37, V1_18_2 to 0x3B, V1_19_0 to 0x39, V1_19_1 to 0x3C)
            .register(ServerResourcePackPacket::class          , ServerResourcePackPacketReader            , V1_16_4 to 0x38, V1_18_2 to 0x3C, V1_19_0 to 0x3A, V1_19_1 to 0x3D)
            .register(ServerRespawnPacket::class               , ServerRespawnPacketReader                 , V1_16_4 to 0x39, V1_18_2 to 0x3D, V1_19_0 to 0x3B, V1_19_1 to 0x3E)
            .register(ServerEntityHeadRotationPacket::class    , ServerEntityHeadRotationPacketReader      , V1_16_4 to 0x3A, V1_18_2 to 0x3E, V1_19_0 to 0x3C, V1_19_1 to 0x3F)
            .register(ServerBlockUpdatesPacket::class          , ServerBlockUpdatesPacketReader            , V1_16_4 to 0x3B, V1_18_2 to 0x3F, V1_19_0 to 0x3D, V1_19_1 to 0x40)
            .register(null                                     , ServerTitleActionBarPacketReader          ,                  V1_18_2 to 0x41, V1_19_0 to 0x40, V1_19_1 to 0x43)
            .register(ServerBorderPacket::class                , ServerBorderPacketReader                  , V1_16_4 to 0x3D, V1_18_2 to 0x42, V1_19_0 to 0x41, V1_19_1 to 0x44)
            .register(null                                     , ServerBorderSetCenterPacketReader         ,                  V1_18_2 to 0x42, V1_19_0 to 0x41, V1_19_1 to 0x44)
            .register(null                                     , ServerBorderLerpSizePacketReader          ,                  V1_18_2 to 0x43, V1_19_0 to 0x42, V1_19_1 to 0x45)
            .register(null                                     , ServerBorderSetSizePacketReader           ,                  V1_18_2 to 0x44, V1_19_0 to 0x43, V1_19_1 to 0x46)
            .register(null                                     , ServerBorderSetWarningTimePacketReader    ,                  V1_18_2 to 0x45, V1_19_0 to 0x44, V1_19_1 to 0x47)
            .register(null                                     , ServerBorderSetWarningDistancePacketReader,                  V1_18_2 to 0x46, V1_19_0 to 0x45, V1_19_1 to 0x48)
            .register(ServerCameraPacket::class                , ServerCameraPacketReader                  , V1_16_4 to 0x3E, V1_18_2 to 0x47, V1_19_0 to 0x46, V1_19_1 to 0x49)
            .register(ServerHotbarPacket::class                , ServerHotbarPacketReader                  , V1_16_4 to 0x3F, V1_18_2 to 0x48, V1_19_0 to 0x47, V1_19_1 to 0x4A)
            .register(ServerChunkPublishPacket::class          , ServerChunkPublishPacketReader            , V1_16_4 to 0x40, V1_18_2 to 0x49, V1_19_0 to 0x48, V1_19_1 to 0x4B)
            .register(ServerViewDistancePacket::class          , ServerViewDistancePacketReader            , V1_16_4 to 0x41, V1_18_2 to 0x4A, V1_19_0 to 0x49, V1_19_1 to 0x4C)
            .register(ServerSpawnPositionPacket::class         , ServerSpawnPositionPacketReader           , V1_16_4 to 0x42, V1_18_2 to 0x4B, V1_19_0 to 0x4A, V1_19_1 to 0x4D)

            .register(ServerScoreboardDisplayPacket::class     , ServerScoreboardDisplayPacketReader       , V1_16_4 to 0x43, V1_18_2 to 0x4C, V1_19_0 to 0x4C, V1_19_1 to 0x4F)
            .register(ServerEntityMetadataPacket::class        , ServerEntityMetadataPacketReader          , V1_16_4 to 0x44, V1_18_2 to 0x4D, V1_19_0 to 0x4D, V1_19_1 to 0x50)
            .register(ServerEntityAttachPacket::class          , ServerEntityAttachPacketReader            , V1_16_4 to 0x45, V1_18_2 to 0x4E, V1_19_0 to 0x4E, V1_19_1 to 0x51)
            .register(ServerEntityVelocityPacket::class        , ServerEntityVelocityPacketReader          , V1_16_4 to 0x46, V1_18_2 to 0x4F, V1_19_0 to 0x4F, V1_19_1 to 0x52)
            .register(ServerEntityEquipmentPacket::class       , ServerEntityEquipmentPacketReader         , V1_16_4 to 0x47, V1_18_2 to 0x50, V1_19_0 to 0x50, V1_19_1 to 0x53)
            .register(ServerExperiencePacket::class            , ServerExperiencePacketReader              , V1_16_4 to 0x48, V1_18_2 to 0x51, V1_19_0 to 0x51, V1_19_1 to 0x54)
            .register(ServerHealthHungerSaturationPacket::class, ServerHealthHungerSaturationPacketReader  , V1_16_4 to 0x49, V1_18_2 to 0x52, V1_19_0 to 0x52, V1_19_1 to 0x55)
            .register(ServerObjectivePacket::class             , ServerObjectivePacketReader               , V1_16_4 to 0x4A, V1_18_2 to 0x53, V1_19_0 to 0x53, V1_19_1 to 0x56)
            .register(ServerEntityPassengersPacket::class      , ServerEntityPassengersPacketReader        , V1_16_4 to 0x4B, V1_18_2 to 0x54, V1_19_0 to 0x54, V1_19_1 to 0x57)
            .register(ServerTeamPacket::class                  , ServerTeamPacketReader                    , V1_16_4 to 0x4C, V1_18_2 to 0x55, V1_19_0 to 0x55, V1_19_1 to 0x58)
            .register(ServerScorePacket::class                 , ServerScorePacketReader                   , V1_16_4 to 0x4D, V1_18_2 to 0x56, V1_19_0 to 0x56, V1_19_1 to 0x59)
            .register(ServerSimulationDistancePacket::class    , ServerSimulationDistancePacketReader      ,                  V1_18_2 to 0x57, V1_19_0 to 0x57, V1_19_1 to 0x5A)
            .register(null                                     , ServerTitleSetSubTitlePacketReader        ,                  V1_18_2 to 0x58, V1_19_0 to 0x58, V1_19_1 to 0x5B)
            .register(ServerTimePacket::class                  , ServerTimePacketReader                    , V1_16_4 to 0x4E, V1_18_2 to 0x59, V1_19_0 to 0x59, V1_19_1 to 0x5C)
            .register(ServerTitlePacket::class                 , ServerTitlePacketReader                   , V1_16_4 to 0x4F                                                   )
            .register(null                                     , ServerTitleSetTitlePacketReader           ,                  V1_18_2 to 0x5A, V1_19_0 to 0x5A, V1_19_1 to 0x5D)
            .register(null                                     , ServerTitleSetTimingsPacketReader         ,                  V1_18_2 to 0x5B, V1_19_0 to 0x5B, V1_19_1 to 0x5E)
            .register(ServerEntitySoundPacket::class           , ServerEntitySoundPacketReader             , V1_16_4 to 0x50, V1_18_2 to 0x5C, V1_19_0 to 0x5C, V1_19_1 to 0x5F)
            .register(ServerSoundPacket::class                 , ServerSoundPacketReader                   , V1_16_4 to 0x51, V1_18_2 to 0x5D, V1_19_0 to 0x5D, V1_19_1 to 0x60)
            .register(ServerSoundStopPacket::class             , ServerSoundStopPacketReader               , V1_16_4 to 0x52, V1_18_2 to 0x5E, V1_19_0 to 0x5E, V1_19_1 to 0x61)
            .register(ServerSystemChatPacket::class            , ServerSystemChatPacketReader              , V1_16_4 to 0x0E, V1_18_2 to 0x0F, V1_19_0 to 0x5F, V1_19_1 to 0x62)
            .register(ServerPlayerListHeaderFooterPacket::class, ServerPlayerListHeaderFooterPacketReader  , V1_16_4 to 0x53, V1_18_2 to 0x5F, V1_19_0 to 0x60, V1_19_1 to 0x63)
            .register(ServerQueryPacket::class                 , ServerQueryPacketReader                   , V1_16_4 to 0x54, V1_18_2 to 0x60, V1_19_0 to 0x61, V1_19_1 to 0x64)
            .register(ServerStackTakePacket::class             , ServerStackTakePacketReader               , V1_16_4 to 0x55, V1_18_2 to 0x61, V1_19_0 to 0x62, V1_19_1 to 0x65)
            .register(ServerEntityTeleportPacket::class        , ServerEntityTeleportPacketReader          , V1_16_4 to 0x56, V1_18_2 to 0x62, V1_19_0 to 0x63, V1_19_1 to 0x66)
            .register(ServerEntityAttributesPacket::class      , ServerEntityAttributesPacketReader        , V1_16_4 to 0x58, V1_18_2 to 0x64, V1_19_0 to 0x65, V1_19_1 to 0x68)
            .register(ServerEntityEffectApplyPacket::class     , ServerEntityEffectApplyPacketReader       , V1_16_4 to 0x59, V1_18_2 to 0x65, V1_19_0 to 0x66, V1_19_1 to 0x69)
            .register(ServerRecipesPacket::class               , ServerRecipesPacketReader                 , V1_16_4 to 0x5A, V1_18_2 to 0x66, V1_19_0 to 0x67, V1_19_1 to 0x6A)
            .build()
    )
}
