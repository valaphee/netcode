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

import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.PacketReader
import com.valaphee.netcode.mcbe.network.Restrict
import com.valaphee.netcode.mcbe.network.Restriction
import com.valaphee.netcode.mcbe.world.inventory.WindowSlotType
import com.valaphee.netcode.mcbe.world.item.ItemStack
import com.valaphee.netcode.mcbe.world.item.readItemStackInstance
import com.valaphee.netcode.mcbe.world.item.readItemStackPre431
import com.valaphee.netcode.mcbe.world.item.writeItemStackInstance
import com.valaphee.netcode.mcbe.world.item.writeItemStackPre431
import com.valaphee.netcode.util.safeList

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToServer)
class InventoryRequestPacket(
    val requests: List<Request>
) : Packet() {
    enum class ActionType {
        Move, Place, Swap, Drop, Destroy, Consume, Create, LabTableCombine, BeaconPayment, MineBlock, CraftRecipe, CraftRecipeAuto, CraftCreative, CraftRecipeOptional, CraftNonImplementedDeprecated, CraftResultsDeprecated
    }

    class Action(
        val result: List<ItemStack?>?,
        val type: ActionType,
        val count: Int,
        val sourceSlotType: WindowSlotType?,
        val sourceSlotId: Int,
        val sourceNetId: Int,
        val random: Boolean,
        val destinationSlotType: WindowSlotType?,
        val destinationSlotId: Int,
        val destinationNetId: Int,
        val slotId: Int,
        val auxInt: Int,
        val auxInt2: Int
    ) {
        override fun toString() = "Action(result=$result, type=$type, count=$count, sourceSlotType=$sourceSlotType, sourceSlotId=$sourceSlotId, sourceNetId=$sourceNetId, random=$random, destinationSlotType=$destinationSlotType, destinationSlotId=$destinationSlotId, destinationNetId=$destinationNetId, slotId=$slotId, auxInt=$auxInt, auxInt2=$auxInt2)"
    }

    class Request(
        val requestId: Int,
        val actions: List<Action>,
        val filteredTexts: List<String>
    ) {
        override fun toString() = "Request(requestId=$requestId, actions=$actions, filteredTexts=$filteredTexts)"
    }

    override val id get() = 0x93

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarUInt(requests.size)
        requests.forEach {
            buffer.writeVarInt(it.requestId)
            buffer.writeVarUInt(it.actions.size)
            it.actions.forEach {
                when (it.type) {
                    ActionType.Move, ActionType.Place -> {
                        buffer.writeByte(it.count)
                        buffer.writeByte(it.sourceSlotType!!.ordinal)
                        buffer.writeByte(it.sourceSlotId)
                        buffer.writeVarInt(it.sourceNetId)
                        buffer.writeByte(it.destinationSlotType!!.ordinal)
                        buffer.writeByte(it.destinationSlotId)
                        buffer.writeVarInt(it.destinationNetId)
                    }
                    ActionType.Swap -> {
                        buffer.writeByte(it.sourceSlotType!!.ordinal)
                        buffer.writeByte(it.sourceSlotId)
                        buffer.writeVarInt(it.sourceNetId)
                        buffer.writeByte(it.destinationSlotType!!.ordinal)
                        buffer.writeByte(it.destinationSlotId)
                        buffer.writeVarInt(it.destinationNetId)
                    }
                    ActionType.Drop -> {
                        buffer.writeByte(it.count)
                        buffer.writeByte(it.sourceSlotType!!.ordinal)
                        buffer.writeByte(it.sourceSlotId)
                        buffer.writeVarInt(it.sourceNetId)
                        buffer.writeBoolean(it.random)
                    }
                    ActionType.Destroy, ActionType.Consume -> {
                        buffer.writeByte(it.count)
                        buffer.writeByte(it.sourceSlotType!!.ordinal)
                        buffer.writeByte(it.sourceSlotId)
                        buffer.writeVarInt(it.sourceNetId)
                    }
                    ActionType.Create -> buffer.writeByte(it.sourceSlotId)
                    ActionType.LabTableCombine -> TODO()
                    ActionType.BeaconPayment -> {
                        buffer.writeVarInt(it.auxInt)
                        buffer.writeVarInt(it.auxInt2)
                    }
                    ActionType.MineBlock -> TODO()
                    ActionType.CraftRecipe, ActionType.CraftCreative -> buffer.writeVarUInt(it.auxInt)
                    ActionType.CraftRecipeAuto -> {
                        buffer.writeVarUInt(it.auxInt)
                        if (version >= 448) buffer.writeByte(it.auxInt2)
                    }
                    ActionType.CraftRecipeOptional -> {
                        buffer.writeVarUInt(it.auxInt)
                        buffer.writeIntLE(it.auxInt2)
                    }
                    ActionType.CraftNonImplementedDeprecated, ActionType.CraftResultsDeprecated -> {
                        it.result!!.let {
                            buffer.writeVarUInt(it.size)
                            it.forEach { if (version >= 431) buffer.writeItemStackInstance(it) else buffer.writeItemStackPre431(it) }
                        }
                        buffer.writeByte(it.count)
                    }
                }
            }
            if (version >= 422) {
                buffer.writeVarUInt(it.filteredTexts.size)
                it.filteredTexts.forEach { buffer.writeString(it) }
            }
        }
    }

    override fun handle(handler: PacketHandler) = handler.inventoryRequest(this)

    override fun toString() = "InventoryRequestPacket(requests=$requests)"
}

/**
 * @author Kevin Ludwig
 */
object InventoryRequestPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = InventoryRequestPacket(safeList(buffer.readVarUInt()) {
        InventoryRequestPacket.Request(buffer.readVarInt(), safeList(buffer.readVarUInt()) {
            when (val actionType = InventoryRequestPacket.ActionType.values()[buffer.readByte().toInt()]) {
                InventoryRequestPacket.ActionType.Move, InventoryRequestPacket.ActionType.Place -> InventoryRequestPacket.Action(null, actionType, buffer.readByte().toInt(), WindowSlotType.values()[buffer.readByte().toInt()], buffer.readByte().toInt(), buffer.readVarInt(), false, WindowSlotType.values()[buffer.readByte().toInt()], buffer.readByte().toInt(), buffer.readVarInt(), 0, 0, 0)
                InventoryRequestPacket.ActionType.Swap -> InventoryRequestPacket.Action(null, actionType, 0, WindowSlotType.values()[buffer.readByte().toInt()], buffer.readByte().toInt(), buffer.readVarInt(), false, WindowSlotType.values()[buffer.readByte().toInt()], buffer.readByte().toInt(), buffer.readVarInt(), 0, 0, 0)
                InventoryRequestPacket.ActionType.Drop -> InventoryRequestPacket.Action(null, actionType, buffer.readByte().toInt(), WindowSlotType.values()[buffer.readByte().toInt()], buffer.readByte().toInt(), buffer.readVarInt(), buffer.readBoolean(), null, 0, 0, 0, 0, 0)
                InventoryRequestPacket.ActionType.Destroy, InventoryRequestPacket.ActionType.Consume -> InventoryRequestPacket.Action(null, actionType, buffer.readByte().toInt(), WindowSlotType.values()[buffer.readByte().toInt()], buffer.readByte().toInt(), buffer.readVarInt(), false, null, 0, 0, 0, 0, 0)
                InventoryRequestPacket.ActionType.Create -> InventoryRequestPacket.Action(null, actionType, 0, null, buffer.readByte().toInt(), 0, false, null, 0, 0, 0, 0, 0)
                InventoryRequestPacket.ActionType.LabTableCombine -> TODO()
                InventoryRequestPacket.ActionType.BeaconPayment -> InventoryRequestPacket.Action(null, actionType, 0, null, 0, 0, false, null, 0, 0, 0, buffer.readVarInt(), buffer.readVarInt())
                InventoryRequestPacket.ActionType.MineBlock -> TODO()
                InventoryRequestPacket.ActionType.CraftRecipe, InventoryRequestPacket.ActionType.CraftCreative -> InventoryRequestPacket.Action(null, actionType, 0, null, 0, 0, false, null, 0, 0, 0, buffer.readVarInt(), 0)
                InventoryRequestPacket.ActionType.CraftRecipeAuto -> InventoryRequestPacket.Action(null, actionType, 0, null, 0, 0, false, null, 0, 0, 0, buffer.readVarInt(), if (version >= 448) buffer.readByte().toInt() else 0)
                InventoryRequestPacket.ActionType.CraftRecipeOptional -> InventoryRequestPacket.Action(null, actionType, 0, null, 0, 0, false, null, 0, 0, 0, buffer.readVarUInt(), buffer.readIntLE())
                InventoryRequestPacket.ActionType.CraftResultsDeprecated -> InventoryRequestPacket.Action(safeList(buffer.readVarUInt()) { if (version >= 431) buffer.readItemStackInstance() else buffer.readItemStackPre431() }, actionType, 0, null, 0, 0, false, null, 0, 0, 0, buffer.readByte().toInt(), 0)
                InventoryRequestPacket.ActionType.CraftNonImplementedDeprecated -> TODO()
            }
        }, if (version >= 422) safeList(buffer.readVarUInt()) { buffer.readString() } else emptyList())
    })
}
