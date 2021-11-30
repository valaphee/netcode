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
