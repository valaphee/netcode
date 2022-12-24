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
import com.valaphee.netcode.mcje.network.packet.play.ClientChatSessionPacket
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
import com.valaphee.netcode.mcje.network.packet.play.ClientRecipeBookPacket
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

/**
 * @author Kevin Ludwig
 */
interface ClientPlayPacketHandler : PacketHandler {
    fun teleportConfirm(packet: ClientTeleportConfirmPacket) = other(packet)

    fun blockQuery(packet: ClientBlockQueryPacket) = other(packet)

    fun difficulty(packet: ClientDifficultyPacket) = other(packet)

    fun chatAcknowledge(packet: ClientChatAcknowledgePacket) = other(packet)

    fun command(packet: ClientCommandPacket) = other(packet)

    fun chat(packet: ClientChatPacket) = other(packet)

    fun chatPreview(packet: ClientChatPreviewPacket) = other(packet)

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

    fun jigsawGenerate(packet: ClientJigsawGeneratePacket) = other(packet)

    fun keepAlive(packet: ClientKeepAlivePacket) = other(packet)

    fun difficultyLock(packet: ClientDifficultyLockPacket) = other(packet)

    fun location(packet: ClientLocationPacket) = other(packet)

    fun vehicleLocation(packet: ClientVehicleLocationPacket) = other(packet)

    fun steerBoat(packet: ClientSteerBoatPacket) = other(packet)

    fun itemPick(packet: ClientItemPickPacket) = other(packet)

    fun craft(packet: ClientCraftPacket) = other(packet)

    fun abilities(packet: ClientAbilitiesPacket) = other(packet)

    fun action(packet: ClientActionPacket) = other(packet)

    fun playerAction(packet: ClientPlayerActionPacket) = other(packet)

    fun steer(packet: ClientSteerPacket) = other(packet)

    fun pong(packet: ClientPongPacket) = other(packet)

    fun chatSession(packet: ClientChatSessionPacket) = other(packet)

    fun recipeBook(packet: ClientRecipeBookPacket) = other(packet)

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
