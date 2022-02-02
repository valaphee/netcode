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
interface ClientPlayPacketHandler : PacketHandler {
    fun teleportConfirm(packet: ClientTeleportConfirmPacket) = other(packet)

    fun blockQuery(packet: ClientBlockQueryPacket) = other(packet)

    fun difficulty(packet: ClientDifficultyPacket) = other(packet)

    fun text(packet: ClientTextPacket) = other(packet)

    fun status(packet: ClientStatusPacket) = other(packet)

    fun settings(packet: ClientSettingsPacket) = other(packet)

    fun commandSuggest(packet: ClientCommandSuggestPacket) = other(packet)

    fun windowConfirm(packet: ClientWindowConfirmPacket) = other(packet)

    fun windowClickButton(packet: ClientWindowClickButtonPacket) = other(packet)

    fun windowClick(packet: ClientWindowClickPacket) = other(packet)

    fun windowClose(packet: ClientWindowClosePacket) = other(packet)

    fun customPayload(packet: ClientCustomPayloadPacket) = other(packet)

    fun bookEdit(packet: ClientBookEditPacket) = other(packet)

    fun entityQuery(packet: ClientEntityQueryPacket) = other(packet)

    fun itemUseOnEntity(packet: ClientItemUseOnEntityPacket) = other(packet)

    fun generate(packet: ClientGeneratePacket) = other(packet)

    fun keepAlive(packet: ClientKeepAlivePacket) = other(packet)

    fun difficultyLock(packet: ClientDifficultyLockPacket) = other(packet)

    fun position(packet: ClientPositionPacket) = other(packet)

    fun positionRotation(packet: ClientPositionRotationPacket) = other(packet)

    fun rotation(packet: ClientRotationPacket) = other(packet)

    fun location(packet: ClientLocationPacket) = other(packet)

    fun vehicleLocation(packet: ClientVehicleLocationPacket) = other(packet)

    fun steerBoat(packet: ClientSteerBoatPacket) = other(packet)

    fun itemPick(packet: ClientItemPickPacket) = other(packet)

    fun craft(packet: ClientCraftPacket) = other(packet)

    fun abilities(packet: ClientAbilitiesPacket) = other(packet)

    fun action(packet: ClientActionPacket) = other(packet)

    fun playerAction(packet: ClientPlayerActionPacket) = other(packet)

    fun steer(packet: ClientSteerPacket) = other(packet)

    fun recipeBook(packet: ClientRecipeBookPacket) = other(packet)

    fun recipeBookDisplayRecipe(packet: ClientRecipeBookDisplayRecipePacket) = other(packet)

    fun recipeBookState(packet: ClientRecipeBookStatePacket) = other(packet)

    fun itemName(packet: ClientItemNamePacket) = other(packet)

    fun resourcePackStatus(packet: ClientResourcePackStatusPacket) = other(packet)

    fun advancementTab(packet: ClientAdvancementTabPacket) = other(packet)

    fun trade(packet: ClientTradePacket) = other(packet)

    fun beaconUpdate(packet: ClientBeaconUpdatePacket) = other(packet)

    fun hotbar(packet: ClientHotbarPacket) = other(packet)

    fun commandBlockUpdate(packet: ClientCommandBlockUpdatePacket) = other(packet)

    fun commandBlockMinecartUpdate(packet: ClientCommandBlockMinecartUpdatePacket) = other(packet)

    fun creativeInventorySlot(packet: ClientCreativeInventorySlotPacket) = other(packet)

    fun jigsawBlockUpdate(packet: ClientJigsawBlockUpdatePacket) = other(packet)

    fun structureBlockUpdate(packet: ClientStructureBlockUpdatePacket) = other(packet)

    fun signUpdate(packet: ClientSignUpdatePacket) = other(packet)

    fun swingArm(packet: ClientSwingArmPacket) = other(packet)

    fun spectate(packet: ClientSpectatePacket) = other(packet)

    fun blockPlace(packet: ClientBlockPlacePacket) = other(packet)

    fun itemUse(packet: ClientItemUsePacket) = other(packet)
}
