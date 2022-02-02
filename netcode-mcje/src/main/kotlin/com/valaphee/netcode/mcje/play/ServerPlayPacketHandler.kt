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

package com.valaphee.netcode.mcje.play

import com.valaphee.netcode.mcje.PacketHandler

/**
 * @author Kevin Ludwig
 */
interface ServerPlayPacketHandler : PacketHandler {
    fun objectAdd(packet: ServerObjectAddPacket) = other(packet)

    fun experienceOrbAdd(packet: ServerExperienceOrbAddPacket) = other(packet)

    fun globalEntityAdd(packet: ServerGlobalEntityAddPacket) = other(packet)

    fun entityAdd(packet: ServerEntityAddPacket) = other(packet)

    /*fun paintingAdd(packet: ServerPaintingAddPacket) = other(packet)*/

    fun playerAdd(packet: ServerPlayerAddPacket) = other(packet)

    fun sculkVibrationSignal(packet: ServerSculkVibrationSignalPacket) = other(packet)

    fun entityAnimation(packet: ServerEntityAnimationPacket) = other(packet)

    /*fun statistics(packet: ServerStatisticsPacket) = other(packet)*/

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

    /*fun particle(packet: ServerParticlePacket) = other(packet)

    fun chunkLight(packet: ServerChunkLightPacket) = other(packet)*/

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

    /*fun recipes(packet: ServerRecipesPacket) = other(packet)*/
}
