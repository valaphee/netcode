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

package com.valaphee.netcode.mcje

import com.valaphee.netcode.mcje.play.ServerAbilitiesPacket
import com.valaphee.netcode.mcje.play.ServerActionResponsePacket
import com.valaphee.netcode.mcje.play.ServerBlockBreakAnimationPacket
import com.valaphee.netcode.mcje.play.ServerBlockEntityPacket
import com.valaphee.netcode.mcje.play.ServerBlockEventPacket
import com.valaphee.netcode.mcje.play.ServerBlockUpdatePacket
import com.valaphee.netcode.mcje.play.ServerBlockUpdatesPacket
import com.valaphee.netcode.mcje.play.ServerBookOpenPacket
import com.valaphee.netcode.mcje.play.ServerBorderPacket
import com.valaphee.netcode.mcje.play.ServerBossBarPacket
import com.valaphee.netcode.mcje.play.ServerCameraPacket
import com.valaphee.netcode.mcje.play.ServerChunkLightPacket
import com.valaphee.netcode.mcje.play.ServerChunkPacket
import com.valaphee.netcode.mcje.play.ServerChunkPublishPacket
import com.valaphee.netcode.mcje.play.ServerChunkUnloadPacket
import com.valaphee.netcode.mcje.play.ServerCraftPacket
import com.valaphee.netcode.mcje.play.ServerCustomPayloadPacket
import com.valaphee.netcode.mcje.play.ServerDifficultyPacket
import com.valaphee.netcode.mcje.play.ServerDisconnectPacket
import com.valaphee.netcode.mcje.play.ServerEntitiesRemovePacket
import com.valaphee.netcode.mcje.play.ServerEntityAddPacket
import com.valaphee.netcode.mcje.play.ServerEntityAnimationPacket
import com.valaphee.netcode.mcje.play.ServerEntityAttachPacket
import com.valaphee.netcode.mcje.play.ServerEntityAttributesPacket
import com.valaphee.netcode.mcje.play.ServerEntityEffectApplyPacket
import com.valaphee.netcode.mcje.play.ServerEntityEffectRevokePacket
import com.valaphee.netcode.mcje.play.ServerEntityEquipmentPacket
import com.valaphee.netcode.mcje.play.ServerEntityEventPacket
import com.valaphee.netcode.mcje.play.ServerEntityHeadRotationPacket
import com.valaphee.netcode.mcje.play.ServerEntityLocationPacket
import com.valaphee.netcode.mcje.play.ServerEntityMetadataPacket
import com.valaphee.netcode.mcje.play.ServerEntityMovePacket
import com.valaphee.netcode.mcje.play.ServerEntityMoveRotatePacket
import com.valaphee.netcode.mcje.play.ServerEntityPassengersPacket
import com.valaphee.netcode.mcje.play.ServerEntityRotatePacket
import com.valaphee.netcode.mcje.play.ServerEntitySoundPacket
import com.valaphee.netcode.mcje.play.ServerEntityTeleportPacket
import com.valaphee.netcode.mcje.play.ServerEntityVelocityPacket
import com.valaphee.netcode.mcje.play.ServerExperienceOrbAddPacket
import com.valaphee.netcode.mcje.play.ServerExperiencePacket
import com.valaphee.netcode.mcje.play.ServerExplosionPacket
import com.valaphee.netcode.mcje.play.ServerGameStatePacket
import com.valaphee.netcode.mcje.play.ServerGlobalEntityAddPacket
import com.valaphee.netcode.mcje.play.ServerHealthHungerSaturationPacket
import com.valaphee.netcode.mcje.play.ServerHorseWindowOpenPacket
import com.valaphee.netcode.mcje.play.ServerHotbarPacket
import com.valaphee.netcode.mcje.play.ServerInventoryContentPacket
import com.valaphee.netcode.mcje.play.ServerInventorySlotPacket
import com.valaphee.netcode.mcje.play.ServerItemCooldownPacket
import com.valaphee.netcode.mcje.play.ServerKeepAlivePacket
import com.valaphee.netcode.mcje.play.ServerLocationPacket
import com.valaphee.netcode.mcje.play.ServerLookAtPacket
import com.valaphee.netcode.mcje.play.ServerMapPacket
import com.valaphee.netcode.mcje.play.ServerNamedSoundPacket
import com.valaphee.netcode.mcje.play.ServerObjectAddPacket
import com.valaphee.netcode.mcje.play.ServerObjectivePacket
import com.valaphee.netcode.mcje.play.ServerPaintingAddPacket
import com.valaphee.netcode.mcje.play.ServerParticlePacket
import com.valaphee.netcode.mcje.play.ServerPlayerAddPacket
import com.valaphee.netcode.mcje.play.ServerPlayerCombatEventPacket
import com.valaphee.netcode.mcje.play.ServerPlayerListHeaderFooterPacket
import com.valaphee.netcode.mcje.play.ServerPlayerListPacket
import com.valaphee.netcode.mcje.play.ServerQueryPacket
import com.valaphee.netcode.mcje.play.ServerRecipeBookPacket
import com.valaphee.netcode.mcje.play.ServerRecipesPacket
import com.valaphee.netcode.mcje.play.ServerResourcePackPacket
import com.valaphee.netcode.mcje.play.ServerRespawnPacket
import com.valaphee.netcode.mcje.play.ServerScorePacket
import com.valaphee.netcode.mcje.play.ServerScoreboardDisplayPacket
import com.valaphee.netcode.mcje.play.ServerSculkVibrationSignalPacket
import com.valaphee.netcode.mcje.play.ServerSignUpdatePacket
import com.valaphee.netcode.mcje.play.ServerSoundPacket
import com.valaphee.netcode.mcje.play.ServerSoundStopPacket
import com.valaphee.netcode.mcje.play.ServerSpawnPositionPacket
import com.valaphee.netcode.mcje.play.ServerStackTakePacket
import com.valaphee.netcode.mcje.play.ServerStatisticsPacket
import com.valaphee.netcode.mcje.play.ServerTabCompletePacket
import com.valaphee.netcode.mcje.play.ServerTeamPacket
import com.valaphee.netcode.mcje.play.ServerTextPacket
import com.valaphee.netcode.mcje.play.ServerTimePacket
import com.valaphee.netcode.mcje.play.ServerTitlePacket
import com.valaphee.netcode.mcje.play.ServerTradePacket
import com.valaphee.netcode.mcje.play.ServerVehicleLocationPacket
import com.valaphee.netcode.mcje.play.ServerViewDistancePacket
import com.valaphee.netcode.mcje.play.ServerWindowClosePacket
import com.valaphee.netcode.mcje.play.ServerWindowConfirmPacket
import com.valaphee.netcode.mcje.play.ServerWindowOpenPacket
import com.valaphee.netcode.mcje.play.ServerWindowPropertyPacket
import com.valaphee.netcode.mcje.play.ServerWorldEventPacket
import com.valaphee.netcode.mcje.play.ServerWorldPacket

/**
 * @author Kevin Ludwig
 */
interface ServerPlayPacketHandler : PacketHandler {
    fun objectAdd(packet: ServerObjectAddPacket) = other(packet)

    fun experienceOrbAdd(packet: ServerExperienceOrbAddPacket) = other(packet)

    fun globalEntityAdd(packet: ServerGlobalEntityAddPacket) = other(packet)

    fun entityAdd(packet: ServerEntityAddPacket) = other(packet)

    fun paintingAdd(packet: ServerPaintingAddPacket) = other(packet)

    fun playerAdd(packet: ServerPlayerAddPacket) = other(packet)

    fun sculkVibrationSignal(packet: ServerSculkVibrationSignalPacket) = other(packet)

    fun entityAnimation(packet: ServerEntityAnimationPacket) = other(packet)

    fun statistics(packet: ServerStatisticsPacket) = other(packet)

    fun actionResponse(packet: ServerActionResponsePacket) = other(packet)

    fun blockBreakAnimation(packet: ServerBlockBreakAnimationPacket) = other(packet)

    fun blockEntity(packet: ServerBlockEntityPacket) = other(packet)

    fun blockEvent(packet: ServerBlockEventPacket) = other(packet)

    fun blockUpdate(packet: ServerBlockUpdatePacket) = other(packet)

    fun bossBar(packet: ServerBossBarPacket) = other(packet)

    fun difficulty(packet: ServerDifficultyPacket) = other(packet)

    fun text(packet: ServerTextPacket) = other(packet)

    fun tabComplete(packet: ServerTabCompletePacket) = other(packet)

    fun windowConfirm(packet: ServerWindowConfirmPacket) = other(packet)

    fun windowClose(packet: ServerWindowClosePacket) = other(packet)

    fun inventoryContent(packet: ServerInventoryContentPacket) = other(packet)

    fun windowProperty(packet: ServerWindowPropertyPacket) = other(packet)

    fun inventorySlot(packet: ServerInventorySlotPacket) = other(packet)

    fun itemCooldown(packet: ServerItemCooldownPacket) = other(packet)

    fun customPayload(packet: ServerCustomPayloadPacket) = other(packet)

    fun namedSound(packet: ServerNamedSoundPacket) = other(packet)

    fun disconnect(packet: ServerDisconnectPacket) = other(packet)

    fun entityEvent(packet: ServerEntityEventPacket) = other(packet)

    fun explosion(packet: ServerExplosionPacket) = other(packet)

    fun chunkUnload(packet: ServerChunkUnloadPacket) = other(packet)

    fun gameState(packet: ServerGameStatePacket) = other(packet)

    fun horseWindowOpen(packet: ServerHorseWindowOpenPacket) = other(packet)

    fun keepAlive(packet: ServerKeepAlivePacket) = other(packet)

    fun chunk(packet: ServerChunkPacket) = other(packet)

    fun worldEvent(packet: ServerWorldEventPacket) = other(packet)

    fun particle(packet: ServerParticlePacket) = other(packet)

    fun chunkLight(packet: ServerChunkLightPacket) = other(packet)

    fun world(packet: ServerWorldPacket) = other(packet)

    fun map(packet: ServerMapPacket) = other(packet)

    fun trade(packet: ServerTradePacket) = other(packet)

    fun entityMove(packet: ServerEntityMovePacket) = other(packet)

    fun entityMoveRotate(packet: ServerEntityMoveRotatePacket) = other(packet)

    fun entityRotate(packet: ServerEntityRotatePacket) = other(packet)

    fun entityLocation(packet: ServerEntityLocationPacket) = other(packet)

    fun vehicleLocation(packet: ServerVehicleLocationPacket) = other(packet)

    fun bookOpen(packet: ServerBookOpenPacket) = other(packet)

    fun windowOpen(packet: ServerWindowOpenPacket) = other(packet)

    fun signUpdate(packet: ServerSignUpdatePacket) = other(packet)

    fun craft(packet: ServerCraftPacket) = other(packet)

    fun abilities(packet: ServerAbilitiesPacket) = other(packet)

    fun playerCombatEvent(packet: ServerPlayerCombatEventPacket) = other(packet)

    fun playerList(packet: ServerPlayerListPacket) = other(packet)

    fun lookAt(packet: ServerLookAtPacket) = other(packet)

    fun location(packet: ServerLocationPacket) = other(packet)

    fun recipeBook(packet: ServerRecipeBookPacket) = other(packet)

    fun entitiesRemove(packet: ServerEntitiesRemovePacket) = other(packet)

    fun entityEffectRevoke(packet: ServerEntityEffectRevokePacket) = other(packet)

    fun resourcePack(packet: ServerResourcePackPacket) = other(packet)

    fun respawn(packet: ServerRespawnPacket) = other(packet)

    fun entityHeadRotation(packet: ServerEntityHeadRotationPacket) = other(packet)

    fun blockUpdates(packet: ServerBlockUpdatesPacket) = other(packet)

    fun border(packet: ServerBorderPacket) = other(packet)

    fun camera(packet: ServerCameraPacket) = other(packet)

    fun hotbar(packet: ServerHotbarPacket) = other(packet)

    fun chunkPublish(packet: ServerChunkPublishPacket) = other(packet)

    fun viewDistance(packet: ServerViewDistancePacket) = other(packet)

    fun scoreboardDisplay(packet: ServerScoreboardDisplayPacket) = other(packet)

    fun entityMetadata(packet: ServerEntityMetadataPacket) = other(packet)

    fun entityAttach(packet: ServerEntityAttachPacket) = other(packet)

    fun entityVelocity(packet: ServerEntityVelocityPacket) = other(packet)

    fun entityEquipment(packet: ServerEntityEquipmentPacket) = other(packet)

    fun experience(packet: ServerExperiencePacket) = other(packet)

    fun healthHungerSaturation(packet: ServerHealthHungerSaturationPacket) = other(packet)

    fun objective(packet: ServerObjectivePacket) = other(packet)

    fun entityPassengers(packet: ServerEntityPassengersPacket) = other(packet)

    fun team(packet: ServerTeamPacket) = other(packet)

    fun score(packet: ServerScorePacket) = other(packet)

    fun spawnPosition(packet: ServerSpawnPositionPacket) = other(packet)

    fun time(packet: ServerTimePacket) = other(packet)

    fun title(packet: ServerTitlePacket) = other(packet)

    fun entitySound(packet: ServerEntitySoundPacket) = other(packet)

    fun sound(packet: ServerSoundPacket) = other(packet)

    fun soundStop(packet: ServerSoundStopPacket) = other(packet)

    fun playerListHeaderFooter(packet: ServerPlayerListHeaderFooterPacket) = other(packet)

    fun query(packet: ServerQueryPacket) = other(packet)

    fun stackTake(packet: ServerStackTakePacket) = other(packet)

    fun entityTeleport(packet: ServerEntityTeleportPacket) = other(packet)

    fun entityAttributes(packet: ServerEntityAttributesPacket) = other(packet)

    fun entityEffectApply(packet: ServerEntityEffectApplyPacket) = other(packet)

    fun recipes(packet: ServerRecipesPacket) = other(packet)
}
