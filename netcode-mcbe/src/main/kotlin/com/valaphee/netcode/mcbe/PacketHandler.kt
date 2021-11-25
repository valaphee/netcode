/*
 * MIT License
 *
 * Copyright (c) 2021, Valaphee.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.valaphee.netcode.mcbe

import com.valaphee.netcode.ProtocolHandler
import com.valaphee.netcode.mcbe.base.BehaviorTreePacket
import com.valaphee.netcode.mcbe.base.BiomeDefinitionsPacket
import com.valaphee.netcode.mcbe.base.BlockComponentPacket
import com.valaphee.netcode.mcbe.base.CacheBlobStatusPacket
import com.valaphee.netcode.mcbe.base.CacheBlobsPacket
import com.valaphee.netcode.mcbe.base.CacheStatusPacket
import com.valaphee.netcode.mcbe.base.ClientToServerHandshakePacket
import com.valaphee.netcode.mcbe.base.CustomEventPacket
import com.valaphee.netcode.mcbe.base.DebugPacket
import com.valaphee.netcode.mcbe.base.DebugRendererPacket
import com.valaphee.netcode.mcbe.base.DisconnectPacket
import com.valaphee.netcode.mcbe.base.EntityIdentifiersPacket
import com.valaphee.netcode.mcbe.base.FilterPacket
import com.valaphee.netcode.mcbe.base.ItemComponentPacket
import com.valaphee.netcode.mcbe.base.LabTablePacket
import com.valaphee.netcode.mcbe.base.LatencyPacket
import com.valaphee.netcode.mcbe.base.LoginPacket
import com.valaphee.netcode.mcbe.base.MultiplayerSettingsPacket
import com.valaphee.netcode.mcbe.base.NetworkSettingsPacket
import com.valaphee.netcode.mcbe.base.OnScreenTextureAnimationPacket
import com.valaphee.netcode.mcbe.base.PositionTrackingDbClientRequestPacket
import com.valaphee.netcode.mcbe.base.PositionTrackingDbServerBroadcastPacket
import com.valaphee.netcode.mcbe.base.ProfilePacket
import com.valaphee.netcode.mcbe.base.PurchaseReceiptPacket
import com.valaphee.netcode.mcbe.base.ServerToClientHandshakePacket
import com.valaphee.netcode.mcbe.base.SimulationPacket
import com.valaphee.netcode.mcbe.base.StatusPacket
import com.valaphee.netcode.mcbe.base.StoreOfferPacket
import com.valaphee.netcode.mcbe.base.SubLoginPacket
import com.valaphee.netcode.mcbe.base.TextPacket
import com.valaphee.netcode.mcbe.base.TitlePacket
import com.valaphee.netcode.mcbe.base.TransferPacket
import com.valaphee.netcode.mcbe.base.VideoStreamPacket
import com.valaphee.netcode.mcbe.base.ViolationPacket
import com.valaphee.netcode.mcbe.base.WebSocketPacket
import com.valaphee.netcode.mcbe.command.CommandPacket
import com.valaphee.netcode.mcbe.command.CommandResponsePacket
import com.valaphee.netcode.mcbe.command.CommandSettingsPacket
import com.valaphee.netcode.mcbe.command.CommandSoftEnumerationPacket
import com.valaphee.netcode.mcbe.command.CommandsPacket
import com.valaphee.netcode.mcbe.command.LocalPlayerAsInitializedPacket
import com.valaphee.netcode.mcbe.command.SettingsCommandPacket
import com.valaphee.netcode.mcbe.entity.CameraPacket
import com.valaphee.netcode.mcbe.entity.EntityAddPacket
import com.valaphee.netcode.mcbe.entity.EntityAnimatePacket
import com.valaphee.netcode.mcbe.entity.EntityAnimationPacket
import com.valaphee.netcode.mcbe.entity.EntityArmorPacket
import com.valaphee.netcode.mcbe.entity.EntityEquipmentPacket
import com.valaphee.netcode.mcbe.entity.EntityEventPacket
import com.valaphee.netcode.mcbe.entity.EntityFallPacket
import com.valaphee.netcode.mcbe.entity.EntityLinkPacket
import com.valaphee.netcode.mcbe.entity.EntityRemovePacket
import com.valaphee.netcode.mcbe.entity.ExperienceOrbAddPacket
import com.valaphee.netcode.mcbe.entity.NpcDialoguePacket
import com.valaphee.netcode.mcbe.entity.NpcRequestPacket
import com.valaphee.netcode.mcbe.entity.PaintingAddPacket
import com.valaphee.netcode.mcbe.entity.attribute.EntityAttributesPacket
import com.valaphee.netcode.mcbe.entity.effect.EntityEffectPacket
import com.valaphee.netcode.mcbe.entity.location.EntityMoveRotatePacket
import com.valaphee.netcode.mcbe.entity.location.EntityTeleportPacket
import com.valaphee.netcode.mcbe.entity.location.EntityVelocityPacket
import com.valaphee.netcode.mcbe.entity.metadata.EntityMetadataPacket
import com.valaphee.netcode.mcbe.entity.player.AdventureSettingsPacket
import com.valaphee.netcode.mcbe.entity.player.ArmorDamagePacket
import com.valaphee.netcode.mcbe.entity.player.BlockPickPacket
import com.valaphee.netcode.mcbe.entity.player.EmotePacket
import com.valaphee.netcode.mcbe.entity.player.EmotesPacket
import com.valaphee.netcode.mcbe.entity.player.EntityPickPacket
import com.valaphee.netcode.mcbe.entity.player.HealthPacket
import com.valaphee.netcode.mcbe.entity.player.InputCorrectPacket
import com.valaphee.netcode.mcbe.entity.player.InputPacket
import com.valaphee.netcode.mcbe.entity.player.InteractPacket
import com.valaphee.netcode.mcbe.entity.player.LastHurtByPacket
import com.valaphee.netcode.mcbe.entity.player.PlayerActionPacket
import com.valaphee.netcode.mcbe.entity.player.PlayerAddPacket
import com.valaphee.netcode.mcbe.entity.player.PlayerArmorDamagePacket
import com.valaphee.netcode.mcbe.entity.player.PlayerGameModePacket
import com.valaphee.netcode.mcbe.entity.player.PlayerLocationPacket
import com.valaphee.netcode.mcbe.entity.player.RiderJumpPacket
import com.valaphee.netcode.mcbe.entity.player.SteerPacket
import com.valaphee.netcode.mcbe.entity.player.VelocityPredictionPacket
import com.valaphee.netcode.mcbe.entity.player.appearance.AppearancePacket
import com.valaphee.netcode.mcbe.entity.player.view.ViewDistancePacket
import com.valaphee.netcode.mcbe.entity.player.view.ViewDistanceRequestPacket
import com.valaphee.netcode.mcbe.entity.stack.StackAddPacket
import com.valaphee.netcode.mcbe.entity.stack.StackTakePacket
import com.valaphee.netcode.mcbe.form.FormPacket
import com.valaphee.netcode.mcbe.form.FormResponsePacket
import com.valaphee.netcode.mcbe.form.ServerSettingsPacket
import com.valaphee.netcode.mcbe.form.ServerSettingsRequestPacket
import com.valaphee.netcode.mcbe.inventory.BookEditPacket
import com.valaphee.netcode.mcbe.inventory.CreativeInventoryPacket
import com.valaphee.netcode.mcbe.inventory.EnchantOptionsPacket
import com.valaphee.netcode.mcbe.inventory.EquipmentPacket
import com.valaphee.netcode.mcbe.inventory.HotbarPacket
import com.valaphee.netcode.mcbe.inventory.InventoryContentPacket
import com.valaphee.netcode.mcbe.inventory.InventoryRequestPacket
import com.valaphee.netcode.mcbe.inventory.InventoryResponsePacket
import com.valaphee.netcode.mcbe.inventory.InventorySlotPacket
import com.valaphee.netcode.mcbe.inventory.InventoryTransactionPacket
import com.valaphee.netcode.mcbe.inventory.PhotoItemPacket
import com.valaphee.netcode.mcbe.inventory.PhotoPacket
import com.valaphee.netcode.mcbe.inventory.TradePacket
import com.valaphee.netcode.mcbe.inventory.WindowClosePacket
import com.valaphee.netcode.mcbe.inventory.WindowOpenPacket
import com.valaphee.netcode.mcbe.inventory.WindowPropertyPacket
import com.valaphee.netcode.mcbe.inventory.item.ItemActionPacket
import com.valaphee.netcode.mcbe.inventory.item.craft.CraftingEventPacket
import com.valaphee.netcode.mcbe.inventory.item.craft.RecipesPacket
import com.valaphee.netcode.mcbe.pack.PackDataChunkPacket
import com.valaphee.netcode.mcbe.pack.PackDataChunkRequestPacket
import com.valaphee.netcode.mcbe.pack.PackDataPacket
import com.valaphee.netcode.mcbe.pack.PacksPacket
import com.valaphee.netcode.mcbe.pack.PacksResponsePacket
import com.valaphee.netcode.mcbe.pack.PacksStackPacket
import com.valaphee.netcode.mcbe.world.BossBarPacket
import com.valaphee.netcode.mcbe.world.CameraShakePacket
import com.valaphee.netcode.mcbe.world.DefaultGameModePacket
import com.valaphee.netcode.mcbe.world.DifficultyPacket
import com.valaphee.netcode.mcbe.world.DimensionPacket
import com.valaphee.netcode.mcbe.world.FogPacket
import com.valaphee.netcode.mcbe.world.GameModePacket
import com.valaphee.netcode.mcbe.world.GameRulesPacket
import com.valaphee.netcode.mcbe.world.ParticlePacket
import com.valaphee.netcode.mcbe.world.PlayerListPacket
import com.valaphee.netcode.mcbe.world.RespawnPacket
import com.valaphee.netcode.mcbe.world.ShowCreditsPacket
import com.valaphee.netcode.mcbe.world.SimpleEventPacket
import com.valaphee.netcode.mcbe.world.SoundEventPacket
import com.valaphee.netcode.mcbe.world.SoundEventPacketV1
import com.valaphee.netcode.mcbe.world.SoundEventPacketV2
import com.valaphee.netcode.mcbe.world.SoundPacket
import com.valaphee.netcode.mcbe.world.SoundStopPacket
import com.valaphee.netcode.mcbe.world.SpawnPositionPacket
import com.valaphee.netcode.mcbe.world.TickSyncPacket
import com.valaphee.netcode.mcbe.world.TimePacket
import com.valaphee.netcode.mcbe.world.WorldEventPacket
import com.valaphee.netcode.mcbe.world.WorldGenericEventPacket
import com.valaphee.netcode.mcbe.world.WorldPacket
import com.valaphee.netcode.mcbe.world.chunk.AnvilDamagePacket
import com.valaphee.netcode.mcbe.world.chunk.BlockEntityPacket
import com.valaphee.netcode.mcbe.world.chunk.BlockEventPacket
import com.valaphee.netcode.mcbe.world.chunk.BlockUpdatePacket
import com.valaphee.netcode.mcbe.world.chunk.BlockUpdateSyncedPacket
import com.valaphee.netcode.mcbe.world.chunk.BlockUpdatesSyncedPacket
import com.valaphee.netcode.mcbe.world.chunk.ChunkPacket
import com.valaphee.netcode.mcbe.world.chunk.ChunkPublishPacket
import com.valaphee.netcode.mcbe.world.chunk.CommandBlockUpdatePacket
import com.valaphee.netcode.mcbe.world.chunk.ItemFrameDropItemPacket
import com.valaphee.netcode.mcbe.world.chunk.LecternUpdatePacket
import com.valaphee.netcode.mcbe.world.chunk.StructureBlockUpdatePacket
import com.valaphee.netcode.mcbe.world.chunk.StructureTemplateDataExportRequestPacket
import com.valaphee.netcode.mcbe.world.chunk.StructureTemplateDataExportResponsePacket
import com.valaphee.netcode.mcbe.world.chunk.SubChunkPacket
import com.valaphee.netcode.mcbe.world.chunk.SubChunkRequestPacket
import com.valaphee.netcode.mcbe.world.map.MapCreateLockedCopyPacket
import com.valaphee.netcode.mcbe.world.map.MapPacket
import com.valaphee.netcode.mcbe.world.map.MapRequestPacket
import com.valaphee.netcode.mcbe.world.scoreboard.ObjectiveRemovePacket
import com.valaphee.netcode.mcbe.world.scoreboard.ObjectiveSetPacket
import com.valaphee.netcode.mcbe.world.scoreboard.ScoreboardIdentityPacket
import com.valaphee.netcode.mcbe.world.scoreboard.ScoresPacket

/**
 * @author Kevin Ludwig
 */
interface PacketHandler : ProtocolHandler {
    fun other(packet: Packet)

    fun unknown(packet: UnknownPacket) = other(packet)

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

    fun stackAdd(packet: StackAddPacket) = other(packet)

    fun stackTake(packet: StackTakePacket) = other(packet)

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

    fun storeOffer(packet: StoreOfferPacket) = other(packet)

    fun purchaseReceipt(packet: PurchaseReceiptPacket) = other(packet)

    fun appearance(packet: AppearancePacket) = other(packet)

    fun subLogin(packet: SubLoginPacket) = other(packet)

    fun webSocket(packet: WebSocketPacket) = other(packet)

    fun lastHurtBy(packet: LastHurtByPacket) = other(packet)

    fun bookEdit(packet: BookEditPacket) = other(packet)

    fun npcRequest(packet: NpcRequestPacket) = other(packet)

    fun photo(packet: PhotoPacket) = other(packet)

    fun form(packet: FormPacket) = other(packet)

    fun formResponse(packet: FormResponsePacket) = other(packet)

    fun serverSettingsRequest(packet: ServerSettingsRequestPacket) = other(packet)

    fun serverSettings(packet: ServerSettingsPacket) = other(packet)

    fun profile(packet: ProfilePacket) = other(packet)

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

    fun debugRenderer(packet: DebugRendererPacket) = other(packet)

    fun simulation(packet: SimulationPacket) = other(packet)

    fun npcDialogue(packet: NpcDialoguePacket) = other(packet)

    fun photoItem(packet: PhotoItemPacket) = other(packet)

    fun blockUpdatesSynced(packet: BlockUpdatesSyncedPacket) = other(packet)

    fun subChunk(packet: SubChunkPacket) = other(packet)

    fun subChunkRequest(packet: SubChunkRequestPacket) = other(packet)
}
