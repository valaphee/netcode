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
import com.valaphee.netcode.mcbe.network.Restrict
import com.valaphee.netcode.mcbe.network.Restriction
import com.valaphee.netcode.mcbe.network.V1_16_201
import com.valaphee.netcode.mcbe.network.V1_16_210
import com.valaphee.netcode.mcbe.network.V1_16_221
import com.valaphee.netcode.mcbe.network.V1_17_011
import com.valaphee.netcode.mcbe.network.V1_17_041
import com.valaphee.netcode.mcbe.util.Registry
import com.valaphee.netcode.mcbe.world.inventory.WindowSlotType
import com.valaphee.netcode.mcbe.world.item.ItemStack
import com.valaphee.netcode.mcbe.world.item.writeItemStackInstance
import com.valaphee.netcode.mcbe.world.item.writeItemStackPreV1_16_221

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToServer)
class InventoryRequestPacket(
    val requests: List<Request>
) : Packet() {
    interface Action {
        enum class Type {
            Move, Place, Swap, Drop, Destroy, Consume, Create, LabTableCombine, BeaconPayment, MineBlock, CraftRecipe, CraftRecipeAuto, CraftCreative, CraftRecipeOptional, CraftRepairAndDisenchant, CraftLoom, CraftNonImplementedDeprecated, CraftResultsDeprecated;

            companion object {
                internal val registryPreV1_16_201 = Registry<Type>().apply {
                    this[0] = Move
                    this[1] = Place
                    this[2] = Swap
                    this[3] = Drop
                    this[4] = Destroy
                    this[5] = Consume
                    this[6] = Create
                    this[7] = LabTableCombine
                    this[8] = BeaconPayment
                    this[9] = CraftRecipe
                    this[10] = CraftRecipeAuto
                    this[11] = CraftCreative
                    this[12] = CraftNonImplementedDeprecated
                    this[13] = CraftResultsDeprecated
                }
                internal val registryPreV1_16_210 = Registry<Type>().apply {
                    this[0] = Move
                    this[1] = Place
                    this[2] = Swap
                    this[3] = Drop
                    this[4] = Destroy
                    this[5] = Consume
                    this[6] = Create
                    this[7] = LabTableCombine
                    this[8] = BeaconPayment
                    this[9] = CraftRecipe
                    this[10] = CraftRecipeAuto
                    this[11] = CraftCreative
                    this[12] = CraftRecipeOptional
                    this[13] = CraftNonImplementedDeprecated
                    this[14] = CraftResultsDeprecated
                }
                internal val registryPreV1_17_041 = Registry<Type>().apply {
                    this[0] = Move
                    this[1] = Place
                    this[2] = Swap
                    this[3] = Drop
                    this[4] = Destroy
                    this[5] = Consume
                    this[6] = Create
                    this[7] = LabTableCombine
                    this[8] = BeaconPayment
                    this[9] = MineBlock
                    this[10] = CraftRecipe
                    this[11] = CraftRecipeAuto
                    this[12] = CraftCreative
                    this[13] = CraftRecipeOptional
                    this[14] = CraftNonImplementedDeprecated
                    this[15] = CraftResultsDeprecated
                }
                internal val registry = Registry<Type>().apply {
                    this[0] = Move
                    this[1] = Place
                    this[2] = Swap
                    this[3] = Drop
                    this[4] = Destroy
                    this[5] = Consume
                    this[6] = Create
                    this[7] = LabTableCombine
                    this[8] = BeaconPayment
                    this[9] = MineBlock
                    this[10] = CraftRecipe
                    this[11] = CraftRecipeAuto
                    this[12] = CraftCreative
                    this[13] = CraftRecipeOptional
                    this[14] = CraftRepairAndDisenchant
                    this[15] = CraftLoom
                    this[16] = CraftNonImplementedDeprecated
                    this[17] = CraftResultsDeprecated
                }
            }
        }

        val type: Type
    }

    data class MoveAction(
        val count: Int,
        val sourceSlotType: WindowSlotType?,
        val sourceSlotId: Int,
        val sourceNetId: Int,
        val destinationSlotType: WindowSlotType?,
        val destinationSlotId: Int,
        val destinationNetId: Int
    ) : Action {
        override val type get() = Action.Type.Move
    }

    data class PlaceAction(
        val count: Int,
        val sourceSlotType: WindowSlotType?,
        val sourceSlotId: Int,
        val sourceNetId: Int,
        val destinationSlotType: WindowSlotType?,
        val destinationSlotId: Int,
        val destinationNetId: Int
    ) : Action {
        override val type get() = Action.Type.Place
    }

    data class SwapAction(
        val sourceSlotType: WindowSlotType?,
        val sourceSlotId: Int,
        val sourceNetId: Int,
        val destinationSlotType: WindowSlotType?,
        val destinationSlotId: Int,
        val destinationNetId: Int
    ) : Action {
        override val type get() = Action.Type.Swap
    }

    data class DropAction(
        val count: Int,
        val sourceSlotType: WindowSlotType?,
        val sourceSlotId: Int,
        val sourceNetId: Int,
        val random: Boolean
    ) : Action {
        override val type get() = Action.Type.Drop
    }

    data class DestroyAction(
        val count: Int,
        val sourceSlotType: WindowSlotType?,
        val sourceSlotId: Int,
        val sourceNetId: Int,
    ) : Action {
        override val type get() = Action.Type.Destroy
    }

    data class ConsumeAction(
        val count: Int,
        val sourceSlotType: WindowSlotType?,
        val sourceSlotId: Int,
        val sourceNetId: Int,
    ) : Action {
        override val type get() = Action.Type.Consume
    }

    data class CreateAction(
        val slotId: Int
    ) : Action {
        override val type get() = Action.Type.Create
    }

    data class BeaconPaymentAction(
        val primaryEffect: Int,
        val secondaryEffect: Int
    ) : Action {
        override val type get() = Action.Type.BeaconPayment
    }

    data class MineBlockAction(
        val hotbarSlot: Int,
        val durability: Int,
        val netId: Int
    ) : Action {
        override val type get() = Action.Type.MineBlock
    }

    data class CraftRecipeAction(
        val recipeNetId: Int
    ) : Action {
        override val type get() = Action.Type.CraftRecipe
    }

    data class CraftRecipeAutoAction(
        val recipeNetId: Int,
        val count: Int
    ) : Action {
        override val type get() = Action.Type.CraftRecipeAuto
    }

    data class CraftCreativeAction(
        val netId: Int
    ) : Action {
        override val type get() = Action.Type.CraftCreative
    }

    data class CraftRecipeOptionalAction(
        val recipeNetId: Int,
        val filteredText: Int
    ) : Action {
        override val type get() = Action.Type.CraftRecipeOptional
    }

    data class CraftRepairAndDisenchantAction(
        val recipeNetId: Int,
        val repairCost: Int
    ) : Action {
        override val type get() = Action.Type.CraftRepairAndDisenchant
    }

    data class CraftLoomAction(
        val patternId: String
    ) : Action {
        override val type get() = Action.Type.CraftLoom
    }

    data class CraftResultsDeprecatedAction(
        val result: List<ItemStack?>,
        val count: Int
    ) : Action {
        override val type get() = Action.Type.CraftResultsDeprecated
    }

    data class Request(
        val requestId: Int,
        val actions: List<Action>,
        val filteredTexts: List<String>
    )

    override val id get() = 0x93

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarUInt(requests.size)
        val actionTypes = if (version >= V1_17_041) Action.Type.registry else if (version >= V1_16_210) Action.Type.registryPreV1_17_041 else if (version >= V1_16_201) Action.Type.registryPreV1_16_210 else Action.Type.registryPreV1_16_201
        requests.forEach {
            buffer.writeVarInt(it.requestId)
            buffer.writeVarUInt(it.actions.size)
            it.actions.forEach {
                buffer.writeByte(actionTypes.getId(it.type))
                when (it) {
                    is MoveAction -> {
                        buffer.writeByte(it.count)
                        buffer.writeByte(it.sourceSlotType!!.ordinal)
                        buffer.writeByte(it.sourceSlotId)
                        buffer.writeVarInt(it.sourceNetId)
                        buffer.writeByte(it.destinationSlotType!!.ordinal)
                        buffer.writeByte(it.destinationSlotId)
                        buffer.writeVarInt(it.destinationNetId)
                    }
                    is PlaceAction -> {
                        buffer.writeByte(it.count)
                        buffer.writeByte(it.sourceSlotType!!.ordinal)
                        buffer.writeByte(it.sourceSlotId)
                        buffer.writeVarInt(it.sourceNetId)
                        buffer.writeByte(it.destinationSlotType!!.ordinal)
                        buffer.writeByte(it.destinationSlotId)
                        buffer.writeVarInt(it.destinationNetId)
                    }
                    is SwapAction -> {
                        buffer.writeByte(it.sourceSlotType!!.ordinal)
                        buffer.writeByte(it.sourceSlotId)
                        buffer.writeVarInt(it.sourceNetId)
                        buffer.writeByte(it.destinationSlotType!!.ordinal)
                        buffer.writeByte(it.destinationSlotId)
                        buffer.writeVarInt(it.destinationNetId)
                    }
                    is DropAction -> {
                        buffer.writeByte(it.count)
                        buffer.writeByte(it.sourceSlotType!!.ordinal)
                        buffer.writeByte(it.sourceSlotId)
                        buffer.writeVarInt(it.sourceNetId)
                        buffer.writeBoolean(it.random)
                    }
                    is DestroyAction -> {
                        buffer.writeByte(it.count)
                        buffer.writeByte(it.sourceSlotType!!.ordinal)
                        buffer.writeByte(it.sourceSlotId)
                        buffer.writeVarInt(it.sourceNetId)
                    }
                    is ConsumeAction -> {
                        buffer.writeByte(it.count)
                        buffer.writeByte(it.sourceSlotType!!.ordinal)
                        buffer.writeByte(it.sourceSlotId)
                        buffer.writeVarInt(it.sourceNetId)
                    }
                    is CreateAction -> buffer.writeByte(it.slotId)
                    is BeaconPaymentAction -> {
                        buffer.writeVarInt(it.primaryEffect)
                        buffer.writeVarInt(it.secondaryEffect)
                    }
                    is MineBlockAction -> {
                        buffer.writeVarInt(it.hotbarSlot)
                        buffer.writeVarInt(it.durability)
                        buffer.writeVarInt(it.netId)
                    }
                    is CraftRecipeAction -> {
                        buffer.writeVarUInt(it.recipeNetId)
                    }
                    is CraftRecipeAutoAction -> {
                        buffer.writeVarUInt(it.recipeNetId)
                        if (version >= V1_17_011) buffer.writeByte(it.count)
                    }
                    is CraftCreativeAction -> buffer.writeVarUInt(it.netId)
                    is CraftRecipeOptionalAction -> {
                        buffer.writeVarUInt(it.recipeNetId)
                        buffer.writeIntLE(it.filteredText)
                    }
                    is CraftRepairAndDisenchantAction -> {
                        buffer.writeVarUInt(it.recipeNetId)
                        buffer.writeVarInt(it.repairCost)
                    }
                    is CraftLoomAction -> buffer.writeString(it.patternId)
                    is CraftResultsDeprecatedAction -> {
                        buffer.writeVarUInt(it.result.size)
                        it.result.forEach { if (version >= V1_16_221) buffer.writeItemStackInstance(it) else buffer.writeItemStackPreV1_16_221(it) }
                        buffer.writeByte(it.count)
                    }
                }
            }
            if (version >= V1_16_201) {
                buffer.writeVarUInt(it.filteredTexts.size)
                it.filteredTexts.forEach { buffer.writeString(it) }
            }
        }
    }

    override fun handle(handler: PacketHandler) = handler.inventoryRequest(this)

    override fun toString() = "InventoryRequestPacket(requests=$requests)"
}
