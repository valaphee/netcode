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

package com.valaphee.netcode.mcbe.network

import com.valaphee.netcode.mcbe.network.packet.AbilitiesPacket
import com.valaphee.netcode.mcbe.network.packet.AbilityPacket
import com.valaphee.netcode.mcbe.network.packet.AdventureSettingsPacket
import com.valaphee.netcode.mcbe.network.packet.AdventureSettingsWithoutAbilitiesPacket
import com.valaphee.netcode.mcbe.network.packet.AnvilDamagePacket
import com.valaphee.netcode.mcbe.network.packet.AppearancePacket
import com.valaphee.netcode.mcbe.network.packet.ArmorDamagePacket
import com.valaphee.netcode.mcbe.network.packet.AutomationPacket
import com.valaphee.netcode.mcbe.network.packet.BehaviorTreePacket
import com.valaphee.netcode.mcbe.network.packet.BiomeDefinitionsPacket
import com.valaphee.netcode.mcbe.network.packet.BlockComponentPacket
import com.valaphee.netcode.mcbe.network.packet.BlockEntityPacket
import com.valaphee.netcode.mcbe.network.packet.BlockEventPacket
import com.valaphee.netcode.mcbe.network.packet.BlockPickPacket
import com.valaphee.netcode.mcbe.network.packet.BlockUpdatePacket
import com.valaphee.netcode.mcbe.network.packet.BlockUpdateSyncedPacket
import com.valaphee.netcode.mcbe.network.packet.BlockUpdatesSyncedPacket
import com.valaphee.netcode.mcbe.network.packet.BookEditPacket
import com.valaphee.netcode.mcbe.network.packet.BossBarPacket
import com.valaphee.netcode.mcbe.network.packet.CacheBlobStatusPacket
import com.valaphee.netcode.mcbe.network.packet.CacheBlobsPacket
import com.valaphee.netcode.mcbe.network.packet.CacheStatusPacket
import com.valaphee.netcode.mcbe.network.packet.CameraPacket
import com.valaphee.netcode.mcbe.network.packet.CameraShakePacket
import com.valaphee.netcode.mcbe.network.packet.ChunkPacket
import com.valaphee.netcode.mcbe.network.packet.ChunkPublishPacket
import com.valaphee.netcode.mcbe.network.packet.ClientToServerHandshakePacket
import com.valaphee.netcode.mcbe.network.packet.CodeBuilderPacket
import com.valaphee.netcode.mcbe.network.packet.CommandBlockUpdatePacket
import com.valaphee.netcode.mcbe.network.packet.CommandPacket
import com.valaphee.netcode.mcbe.network.packet.CommandResponsePacket
import com.valaphee.netcode.mcbe.network.packet.CommandSettingsPacket
import com.valaphee.netcode.mcbe.network.packet.CommandSoftEnumerationPacket
import com.valaphee.netcode.mcbe.network.packet.CommandsPacket
import com.valaphee.netcode.mcbe.network.packet.CraftingEventPacket
import com.valaphee.netcode.mcbe.network.packet.CreativeInventoryPacket
import com.valaphee.netcode.mcbe.network.packet.CustomEventPacket
import com.valaphee.netcode.mcbe.network.packet.DeathPacket
import com.valaphee.netcode.mcbe.network.packet.DebugOverlayPacket
import com.valaphee.netcode.mcbe.network.packet.DebugPacket
import com.valaphee.netcode.mcbe.network.packet.DefaultGameModePacket
import com.valaphee.netcode.mcbe.network.packet.DifficultyPacket
import com.valaphee.netcode.mcbe.network.packet.DimensionPacket
import com.valaphee.netcode.mcbe.network.packet.DimensionsPacket
import com.valaphee.netcode.mcbe.network.packet.DisconnectPacket
import com.valaphee.netcode.mcbe.network.packet.EditorPacket
import com.valaphee.netcode.mcbe.network.packet.EducationUriResourcePacket
import com.valaphee.netcode.mcbe.network.packet.EmotePacket
import com.valaphee.netcode.mcbe.network.packet.EmotesPacket
import com.valaphee.netcode.mcbe.network.packet.EnchantOptionsPacket
import com.valaphee.netcode.mcbe.network.packet.EntityAddPacket
import com.valaphee.netcode.mcbe.network.packet.EntityAnimatePacket
import com.valaphee.netcode.mcbe.network.packet.EntityAnimationPacket
import com.valaphee.netcode.mcbe.network.packet.EntityArmorPacket
import com.valaphee.netcode.mcbe.network.packet.EntityAttributesPacket
import com.valaphee.netcode.mcbe.network.packet.EntityEffectPacket
import com.valaphee.netcode.mcbe.network.packet.EntityEquipmentPacket
import com.valaphee.netcode.mcbe.network.packet.EntityEventPacket
import com.valaphee.netcode.mcbe.network.packet.EntityFallPacket
import com.valaphee.netcode.mcbe.network.packet.EntityIdentifiersPacket
import com.valaphee.netcode.mcbe.network.packet.EntityLinkPacket
import com.valaphee.netcode.mcbe.network.packet.EntityMetadataPacket
import com.valaphee.netcode.mcbe.network.packet.EntityMoveRotatePacket
import com.valaphee.netcode.mcbe.network.packet.EntityPickPacket
import com.valaphee.netcode.mcbe.network.packet.EntityPropertiesPacket
import com.valaphee.netcode.mcbe.network.packet.EntityPropertyPacket
import com.valaphee.netcode.mcbe.network.packet.EntityRemovePacket
import com.valaphee.netcode.mcbe.network.packet.EntityTeleportPacket
import com.valaphee.netcode.mcbe.network.packet.EntityVelocityPacket
import com.valaphee.netcode.mcbe.network.packet.EquipmentPacket
import com.valaphee.netcode.mcbe.network.packet.ExperienceOrbAddPacket
import com.valaphee.netcode.mcbe.network.packet.FilterPacket
import com.valaphee.netcode.mcbe.network.packet.FogPacket
import com.valaphee.netcode.mcbe.network.packet.FormPacket
import com.valaphee.netcode.mcbe.network.packet.FormResponsePacket
import com.valaphee.netcode.mcbe.network.packet.GameModePacket
import com.valaphee.netcode.mcbe.network.packet.GameRulesPacket
import com.valaphee.netcode.mcbe.network.packet.HealthPacket
import com.valaphee.netcode.mcbe.network.packet.HotbarPacket
import com.valaphee.netcode.mcbe.network.packet.InputCorrectPacket
import com.valaphee.netcode.mcbe.network.packet.InputPacket
import com.valaphee.netcode.mcbe.network.packet.InteractPacket
import com.valaphee.netcode.mcbe.network.packet.InventoryContentPacket
import com.valaphee.netcode.mcbe.network.packet.InventoryRequestPacket
import com.valaphee.netcode.mcbe.network.packet.InventoryResponsePacket
import com.valaphee.netcode.mcbe.network.packet.InventorySlotPacket
import com.valaphee.netcode.mcbe.network.packet.InventoryTransactionPacket
import com.valaphee.netcode.mcbe.network.packet.ItemActionPacket
import com.valaphee.netcode.mcbe.network.packet.ItemAddPacket
import com.valaphee.netcode.mcbe.network.packet.ItemComponentPacket
import com.valaphee.netcode.mcbe.network.packet.ItemCooldownPacket
import com.valaphee.netcode.mcbe.network.packet.ItemFrameDropItemPacket
import com.valaphee.netcode.mcbe.network.packet.ItemTakePacket
import com.valaphee.netcode.mcbe.network.packet.LabTablePacket
import com.valaphee.netcode.mcbe.network.packet.LastHurtByPacket
import com.valaphee.netcode.mcbe.network.packet.LatencyPacket
import com.valaphee.netcode.mcbe.network.packet.LecternUpdatePacket
import com.valaphee.netcode.mcbe.network.packet.LessonProgressPacket
import com.valaphee.netcode.mcbe.network.packet.LocalPlayerAsInitializedPacket
import com.valaphee.netcode.mcbe.network.packet.LoginPacket
import com.valaphee.netcode.mcbe.network.packet.MapCreateLockedCopyPacket
import com.valaphee.netcode.mcbe.network.packet.MapPacket
import com.valaphee.netcode.mcbe.network.packet.MapRequestPacket
import com.valaphee.netcode.mcbe.network.packet.MultiplayerSettingsPacket
import com.valaphee.netcode.mcbe.network.packet.NetworkSettingsPacket
import com.valaphee.netcode.mcbe.network.packet.NpcDialoguePacket
import com.valaphee.netcode.mcbe.network.packet.NpcRequestPacket
import com.valaphee.netcode.mcbe.network.packet.ObjectiveRemovePacket
import com.valaphee.netcode.mcbe.network.packet.ObjectiveSetPacket
import com.valaphee.netcode.mcbe.network.packet.OnScreenTextureAnimationPacket
import com.valaphee.netcode.mcbe.network.packet.PackDataChunkPacket
import com.valaphee.netcode.mcbe.network.packet.PackDataChunkRequestPacket
import com.valaphee.netcode.mcbe.network.packet.PackDataPacket
import com.valaphee.netcode.mcbe.network.packet.PacksPacket
import com.valaphee.netcode.mcbe.network.packet.PacksResponsePacket
import com.valaphee.netcode.mcbe.network.packet.PacksStackPacket
import com.valaphee.netcode.mcbe.network.packet.PaintingAddPacket
import com.valaphee.netcode.mcbe.network.packet.ParticlePacket
import com.valaphee.netcode.mcbe.network.packet.PermissionsPacket
import com.valaphee.netcode.mcbe.network.packet.PhotoItemPacket
import com.valaphee.netcode.mcbe.network.packet.PhotoPacket
import com.valaphee.netcode.mcbe.network.packet.PhotoRequestPacket
import com.valaphee.netcode.mcbe.network.packet.PlayerActionPacket
import com.valaphee.netcode.mcbe.network.packet.PlayerAddPacket
import com.valaphee.netcode.mcbe.network.packet.PlayerArmorDamagePacket
import com.valaphee.netcode.mcbe.network.packet.PlayerGameModePacket
import com.valaphee.netcode.mcbe.network.packet.PlayerListPacket
import com.valaphee.netcode.mcbe.network.packet.PlayerLocationPacket
import com.valaphee.netcode.mcbe.network.packet.PositionTrackingDbClientRequestPacket
import com.valaphee.netcode.mcbe.network.packet.PositionTrackingDbServerBroadcastPacket
import com.valaphee.netcode.mcbe.network.packet.PurchaseReceiptPacket
import com.valaphee.netcode.mcbe.network.packet.RecipesPacket
import com.valaphee.netcode.mcbe.network.packet.RespawnPacket
import com.valaphee.netcode.mcbe.network.packet.RiderJumpPacket
import com.valaphee.netcode.mcbe.network.packet.ScoreboardIdentityPacket
import com.valaphee.netcode.mcbe.network.packet.ScoresPacket
import com.valaphee.netcode.mcbe.network.packet.ScriptMessagePacket
import com.valaphee.netcode.mcbe.network.packet.ServerSettingsPacket
import com.valaphee.netcode.mcbe.network.packet.ServerSettingsRequestPacket
import com.valaphee.netcode.mcbe.network.packet.ServerToClientHandshakePacket
import com.valaphee.netcode.mcbe.network.packet.SettingsCommandPacket
import com.valaphee.netcode.mcbe.network.packet.ShowCreditsPacket
import com.valaphee.netcode.mcbe.network.packet.ShowProfilePacket
import com.valaphee.netcode.mcbe.network.packet.ShowStoreOfferPacket
import com.valaphee.netcode.mcbe.network.packet.SimpleEventPacket
import com.valaphee.netcode.mcbe.network.packet.SimulationPacket
import com.valaphee.netcode.mcbe.network.packet.SoundEventPacket
import com.valaphee.netcode.mcbe.network.packet.SoundEventPacketV1
import com.valaphee.netcode.mcbe.network.packet.SoundEventPacketV2
import com.valaphee.netcode.mcbe.network.packet.SoundPacket
import com.valaphee.netcode.mcbe.network.packet.SoundStopPacket
import com.valaphee.netcode.mcbe.network.packet.SpawnPositionPacket
import com.valaphee.netcode.mcbe.network.packet.StatusPacket
import com.valaphee.netcode.mcbe.network.packet.SteerPacket
import com.valaphee.netcode.mcbe.network.packet.StructureBlockUpdatePacket
import com.valaphee.netcode.mcbe.network.packet.StructureTemplateDataExportRequestPacket
import com.valaphee.netcode.mcbe.network.packet.StructureTemplateDataExportResponsePacket
import com.valaphee.netcode.mcbe.network.packet.SubChunkPacket
import com.valaphee.netcode.mcbe.network.packet.SubChunkRequestPacket
import com.valaphee.netcode.mcbe.network.packet.SubLoginPacket
import com.valaphee.netcode.mcbe.network.packet.TextPacket
import com.valaphee.netcode.mcbe.network.packet.TickSyncPacket
import com.valaphee.netcode.mcbe.network.packet.TickingAreasLoadStatusPacket
import com.valaphee.netcode.mcbe.network.packet.TimePacket
import com.valaphee.netcode.mcbe.network.packet.TitlePacket
import com.valaphee.netcode.mcbe.network.packet.ToastPacket
import com.valaphee.netcode.mcbe.network.packet.TradePacket
import com.valaphee.netcode.mcbe.network.packet.TransferPacket
import com.valaphee.netcode.mcbe.network.packet.VelocityPredictionPacket
import com.valaphee.netcode.mcbe.network.packet.VideoStreamPacket
import com.valaphee.netcode.mcbe.network.packet.ViewDistancePacket
import com.valaphee.netcode.mcbe.network.packet.ViewDistanceRequestPacket
import com.valaphee.netcode.mcbe.network.packet.ViolationPacket
import com.valaphee.netcode.mcbe.network.packet.VolumeEntityAddPacket
import com.valaphee.netcode.mcbe.network.packet.VolumeEntityRemovePacket
import com.valaphee.netcode.mcbe.network.packet.WindowClosePacket
import com.valaphee.netcode.mcbe.network.packet.WindowOpenPacket
import com.valaphee.netcode.mcbe.network.packet.WindowPropertyPacket
import com.valaphee.netcode.mcbe.network.packet.WorldEventPacket
import com.valaphee.netcode.mcbe.network.packet.WorldGenericEventPacket
import com.valaphee.netcode.mcbe.network.packet.WorldPacket
import com.valaphee.netcode.network.ProtocolHandler

/**
 * @author Kevin Ludwig
 */
interface PacketHandler : ProtocolHandler {
    fun other(packet: Packet)

    fun login(packet: LoginPacket) = other(packet)

    fun status(packet: StatusPacket) = other(packet)

    fun serverToClientHandshake(packet: ServerToClientHandshakePacket) = other(packet)

    fun clientToServerHandshake(packet: ClientToServerHandshakePacket) = other(packet)

    fun disconnect(packet: DisconnectPacket) = other(packet)

    fun packs(packet: PacksPacket) = other(packet)

    fun packsStack(packet: PacksStackPacket) = other(packet)

    fun packsResponse(packet: PacksResponsePacket) = other(packet)

    fun text(packet: TextPacket) = other(packet)

    fun time(packet: TimePacket) = other(packet)

    fun world(packet: WorldPacket) = other(packet)

    fun playerAdd(packet: PlayerAddPacket) = other(packet)

    fun entityAdd(packet: EntityAddPacket) = other(packet)

    fun entityRemove(packet: EntityRemovePacket) = other(packet)

    fun itemAdd(packet: ItemAddPacket) = other(packet)

    fun itemTake(packet: ItemTakePacket) = other(packet)

    fun entityTeleport(packet: EntityTeleportPacket) = other(packet)

    fun playerLocation(packet: PlayerLocationPacket) = other(packet)

    fun riderJump(packet: RiderJumpPacket) = other(packet)

    fun blockUpdate(packet: BlockUpdatePacket) = other(packet)

    fun paintingAdd(packet: PaintingAddPacket) = other(packet)

    fun tickSync(packet: TickSyncPacket) = other(packet)

    fun soundEventV1(packet: SoundEventPacketV1) = other(packet)

    fun worldEvent(packet: WorldEventPacket) = other(packet)

    fun blockEvent(packet: BlockEventPacket) = other(packet)

    fun entityEvent(packet: EntityEventPacket) = other(packet)

    fun entityEffect(packet: EntityEffectPacket) = other(packet)

    fun entityAttributes(packet: EntityAttributesPacket) = other(packet)

    fun inventoryTransaction(packet: InventoryTransactionPacket) = other(packet)

    fun entityEquipment(packet: EntityEquipmentPacket) = other(packet)

    fun entityArmor(packet: EntityArmorPacket) = other(packet)

    fun interact(packet: InteractPacket) = other(packet)

    fun blockPick(packet: BlockPickPacket) = other(packet)

    fun entityPick(packet: EntityPickPacket) = other(packet)

    fun playerAction(packet: PlayerActionPacket) = other(packet)

    fun entityFall(packet: EntityFallPacket) = other(packet)

    fun armorDamage(packet: ArmorDamagePacket) = other(packet)

    fun entityMetadata(packet: EntityMetadataPacket) = other(packet)

    fun entityVelocity(packet: EntityVelocityPacket) = other(packet)

    fun entityLink(packet: EntityLinkPacket) = other(packet)

    fun health(packet: HealthPacket) = other(packet)

    fun spawnPosition(packet: SpawnPositionPacket) = other(packet)

    fun entityAnimation(packet: EntityAnimationPacket) = other(packet)

    fun respawn(packet: RespawnPacket) = other(packet)

    fun windowOpen(packet: WindowOpenPacket) = other(packet)

    fun windowClose(packet: WindowClosePacket) = other(packet)

    fun hotbar(packet: HotbarPacket) = other(packet)

    fun inventoryContent(packet: InventoryContentPacket) = other(packet)

    fun inventorySlot(packet: InventorySlotPacket) = other(packet)

    fun windowProperty(packet: WindowPropertyPacket) = other(packet)

    fun recipes(packet: RecipesPacket) = other(packet)

    fun craftingEvent(packet: CraftingEventPacket) = other(packet)

    fun adventureSettings(packet: AdventureSettingsPacket) = other(packet)

    fun blockEntity(packet: BlockEntityPacket) = other(packet)

    fun steer(packet: SteerPacket) = other(packet)

    fun chunk(packet: ChunkPacket) = other(packet)

    fun commandSettings(packet: CommandSettingsPacket) = other(packet)

    fun difficulty(packet: DifficultyPacket) = other(packet)

    fun dimension(packet: DimensionPacket) = other(packet)

    fun gameMode(packet: GameModePacket) = other(packet)

    fun playerList(packet: PlayerListPacket) = other(packet)

    fun simpleEvent(packet: SimpleEventPacket) = other(packet)

    fun experienceOrbAdd(packet: ExperienceOrbAddPacket) = other(packet)

    fun map(packet: MapPacket) = other(packet)

    fun mapRequest(packet: MapRequestPacket) = other(packet)

    fun viewDistanceRequest(packet: ViewDistanceRequestPacket) = other(packet)

    fun viewDistance(packet: ViewDistancePacket) = other(packet)

    fun itemFrameDropItem(packet: ItemFrameDropItemPacket) = other(packet)

    fun gameRules(packet: GameRulesPacket) = other(packet)

    fun camera(packet: CameraPacket) = other(packet)

    fun bossBar(packet: BossBarPacket) = other(packet)

    fun showCredits(packet: ShowCreditsPacket) = other(packet)

    fun commands(packet: CommandsPacket) = other(packet)

    fun command(packet: CommandPacket) = other(packet)

    fun commandBlockUpdate(packet: CommandBlockUpdatePacket) = other(packet)

    fun commandResponse(packet: CommandResponsePacket) = other(packet)

    fun trade(packet: TradePacket) = other(packet)

    fun equipment(packet: EquipmentPacket) = other(packet)

    fun packData(packet: PackDataPacket) = other(packet)

    fun packDataChunk(packet: PackDataChunkPacket) = other(packet)

    fun packDataChunkRequest(packet: PackDataChunkRequestPacket) = other(packet)

    fun transfer(packet: TransferPacket) = other(packet)

    fun sound(packet: SoundPacket) = other(packet)

    fun soundStop(packet: SoundStopPacket) = other(packet)

    fun title(packet: TitlePacket) = other(packet)

    fun behaviorTree(packet: BehaviorTreePacket) = other(packet)

    fun structureBlockUpdate(packet: StructureBlockUpdatePacket) = other(packet)

    fun showStoreOffer(packet: ShowStoreOfferPacket) = other(packet)

    fun purchaseReceipt(packet: PurchaseReceiptPacket) = other(packet)

    fun appearance(packet: AppearancePacket) = other(packet)

    fun subLogin(packet: SubLoginPacket) = other(packet)

    fun webSocket(packet: AutomationPacket) = other(packet)

    fun lastHurtBy(packet: LastHurtByPacket) = other(packet)

    fun bookEdit(packet: BookEditPacket) = other(packet)

    fun npcRequest(packet: NpcRequestPacket) = other(packet)

    fun photo(packet: PhotoPacket) = other(packet)

    fun form(packet: FormPacket) = other(packet)

    fun formResponse(packet: FormResponsePacket) = other(packet)

    fun serverSettingsRequest(packet: ServerSettingsRequestPacket) = other(packet)

    fun serverSettings(packet: ServerSettingsPacket) = other(packet)

    fun showProfile(packet: ShowProfilePacket) = other(packet)

    fun defaultGameMode(packet: DefaultGameModePacket) = other(packet)

    fun objectiveRemove(packet: ObjectiveRemovePacket) = other(packet)

    fun objectiveSet(packet: ObjectiveSetPacket) = other(packet)

    fun scores(packet: ScoresPacket) = other(packet)

    fun labTable(packet: LabTablePacket) = other(packet)

    fun blockUpdateSynced(packet: BlockUpdateSyncedPacket) = other(packet)

    fun entityMoveRotate(packet: EntityMoveRotatePacket) = other(packet)

    fun scoreboardIdentity(packet: ScoreboardIdentityPacket) = other(packet)

    fun localPlayerAsInitialized(packet: LocalPlayerAsInitializedPacket) = other(packet)

    fun commandSoftEnumeration(packet: CommandSoftEnumerationPacket) = other(packet)

    fun latency(packet: LatencyPacket) = other(packet)

    fun customEvent(packet: CustomEventPacket) = other(packet)

    fun particle(packet: ParticlePacket) = other(packet)

    fun entityIdentifiers(packet: EntityIdentifiersPacket) = other(packet)

    fun soundEventV2(packet: SoundEventPacketV2) = other(packet)

    fun chunkPublish(packet: ChunkPublishPacket) = other(packet)

    fun biomeDefinitions(packet: BiomeDefinitionsPacket) = other(packet)

    fun soundEvent(packet: SoundEventPacket) = other(packet)

    fun worldGenericEvent(packet: WorldGenericEventPacket) = other(packet)

    fun lecternUpdate(packet: LecternUpdatePacket) = other(packet)

    fun videoStream(packet: VideoStreamPacket) = other(packet)

    fun cacheStatus(packet: CacheStatusPacket) = other(packet)

    fun onScreenTextureAnimation(packet: OnScreenTextureAnimationPacket) = other(packet)

    fun mapCreateLockedCopy(packet: MapCreateLockedCopyPacket) = other(packet)

    fun structureTemplateDataExportRequest(packet: StructureTemplateDataExportRequestPacket) = other(packet)

    fun structureTemplateDataExportResponse(packet: StructureTemplateDataExportResponsePacket) = other(packet)

    fun blockComponent(packet: BlockComponentPacket) = other(packet)

    fun cacheBlobStatus(packet: CacheBlobStatusPacket) = other(packet)

    fun cacheBlobs(packet: CacheBlobsPacket) = other(packet)

    fun emote(packet: EmotePacket) = other(packet)

    fun multiplayerSettings(packet: MultiplayerSettingsPacket) = other(packet)

    fun settingsCommand(packet: SettingsCommandPacket) = other(packet)

    fun anvilDamage(packet: AnvilDamagePacket) = other(packet)

    fun itemAction(packet: ItemActionPacket) = other(packet)

    fun networkSettings(packet: NetworkSettingsPacket) = other(packet)

    fun input(packet: InputPacket) = other(packet)

    fun creativeInventory(packet: CreativeInventoryPacket) = other(packet)

    fun enchantOptions(packet: EnchantOptionsPacket) = other(packet)

    fun inventoryRequest(packet: InventoryRequestPacket) = other(packet)

    fun inventoryResponse(packet: InventoryResponsePacket) = other(packet)

    fun playerArmorDamage(packet: PlayerArmorDamagePacket) = other(packet)

    fun codeBuilder(packet: CodeBuilderPacket) = other(packet)

    fun playerGameMode(packet: PlayerGameModePacket) = other(packet)

    fun emotes(packet: EmotesPacket) = other(packet)

    fun positionTrackingDbClientRequest(packet: PositionTrackingDbClientRequestPacket) = other(packet)

    fun positionTrackingDbServerBroadcast(packet: PositionTrackingDbServerBroadcastPacket) = other(packet)

    fun debug(packet: DebugPacket) = other(packet)

    fun violation(packet: ViolationPacket) = other(packet)

    fun velocityPrediction(packet: VelocityPredictionPacket) = other(packet)

    fun entityAnimate(packet: EntityAnimatePacket) = other(packet)

    fun cameraShake(packet: CameraShakePacket) = other(packet)

    fun fog(packet: FogPacket) = other(packet)

    fun inputCorrect(packet: InputCorrectPacket) = other(packet)

    fun itemComponent(packet: ItemComponentPacket) = other(packet)

    fun filter(packet: FilterPacket) = other(packet)

    fun debugOverlay(packet: DebugOverlayPacket) = other(packet)

    fun entityProperties(packet: EntityPropertiesPacket) = other(packet)

    fun volumeEntityAdd(packet: VolumeEntityAddPacket) = other(packet)

    fun volumeEntityRemove(packet: VolumeEntityRemovePacket) = other(packet)

    fun simulation(packet: SimulationPacket) = other(packet)

    fun npcDialogue(packet: NpcDialoguePacket) = other(packet)

    fun educationUriResource(packet: EducationUriResourcePacket) = other(packet)

    fun photoItem(packet: PhotoItemPacket) = other(packet)

    fun blockUpdatesSynced(packet: BlockUpdatesSyncedPacket) = other(packet)

    fun photoRequest(packet: PhotoRequestPacket) = other(packet)

    fun subChunk(packet: SubChunkPacket) = other(packet)

    fun subChunkRequest(packet: SubChunkRequestPacket) = other(packet)

    fun itemCooldown(packet: ItemCooldownPacket) = other(packet)

    fun scriptMessage(packet: ScriptMessagePacket) = other(packet)

    fun tickingAreasLoadStatusPacket(packet: TickingAreasLoadStatusPacket) = other(packet)

    fun dimensions(packet: DimensionsPacket) = other(packet)

    fun entityProperty(packet: EntityPropertyPacket) = other(packet)

    fun lessonProgress(packet: LessonProgressPacket) = other(packet)

    fun ability(packet: AbilityPacket) = other(packet)

    fun permissions(packet: PermissionsPacket) = other(packet)

    fun toast(packet: ToastPacket) = other(packet)

    fun abilities(packet: AbilitiesPacket) = other(packet)

    fun adventureSettingsWithoutAbilities(packet: AdventureSettingsWithoutAbilitiesPacket) = other(packet)

    fun death(packet: DeathPacket) = other(packet)

    fun editor(packet: EditorPacket) = other(packet)
}
