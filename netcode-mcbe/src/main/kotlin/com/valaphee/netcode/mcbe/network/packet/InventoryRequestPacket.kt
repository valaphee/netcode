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
import com.valaphee.netcode.mcbe.network.V1_16_010
import com.valaphee.netcode.mcbe.network.V1_16_201
import com.valaphee.netcode.mcbe.network.V1_16_210
import com.valaphee.netcode.mcbe.network.V1_17_011
import com.valaphee.netcode.mcbe.network.V1_17_041
import com.valaphee.netcode.mcbe.network.packet.InventoryRequestPacket.Action.Type.BeaconPayment
import com.valaphee.netcode.mcbe.network.packet.InventoryRequestPacket.Action.Type.Consume
import com.valaphee.netcode.mcbe.network.packet.InventoryRequestPacket.Action.Type.CraftCreative
import com.valaphee.netcode.mcbe.network.packet.InventoryRequestPacket.Action.Type.CraftLoom
import com.valaphee.netcode.mcbe.network.packet.InventoryRequestPacket.Action.Type.CraftNonImplementedDeprecated
import com.valaphee.netcode.mcbe.network.packet.InventoryRequestPacket.Action.Type.CraftRecipe
import com.valaphee.netcode.mcbe.network.packet.InventoryRequestPacket.Action.Type.CraftRecipeAuto
import com.valaphee.netcode.mcbe.network.packet.InventoryRequestPacket.Action.Type.CraftRecipeOptional
import com.valaphee.netcode.mcbe.network.packet.InventoryRequestPacket.Action.Type.CraftRepairAndDisenchant
import com.valaphee.netcode.mcbe.network.packet.InventoryRequestPacket.Action.Type.CraftResultsDeprecated
import com.valaphee.netcode.mcbe.network.packet.InventoryRequestPacket.Action.Type.Create
import com.valaphee.netcode.mcbe.network.packet.InventoryRequestPacket.Action.Type.Destroy
import com.valaphee.netcode.mcbe.network.packet.InventoryRequestPacket.Action.Type.Drop
import com.valaphee.netcode.mcbe.network.packet.InventoryRequestPacket.Action.Type.LabTableCombine
import com.valaphee.netcode.mcbe.network.packet.InventoryRequestPacket.Action.Type.MineBlock
import com.valaphee.netcode.mcbe.network.packet.InventoryRequestPacket.Action.Type.Move
import com.valaphee.netcode.mcbe.network.packet.InventoryRequestPacket.Action.Type.Place
import com.valaphee.netcode.mcbe.network.packet.InventoryRequestPacket.Action.Type.Swap
import com.valaphee.netcode.mcbe.world.inventory.WindowSlotType
import com.valaphee.netcode.mcbe.world.item.ItemStack
import com.valaphee.netcode.mcbe.world.item.readItemStack
import com.valaphee.netcode.mcbe.world.item.writeItemStack
import com.valaphee.netcode.util.Int2ObjectOpenHashBiMapVersioned
import com.valaphee.netcode.util.LazyList

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToServer)
class InventoryRequestPacket(
    val requests: List<Request>
) : Packet() {
    data class Request(
        val requestId: Int,
        val actions: List<Action>,
        val filteredTexts: List<String>
    )

    sealed interface Action {
        enum class Type {
            Move, Place, Swap, Drop, Destroy, Consume, Create, LabTableCombine, BeaconPayment, MineBlock, CraftRecipe, CraftRecipeAuto, CraftCreative, CraftRecipeOptional, CraftRepairAndDisenchant, CraftLoom, CraftNonImplementedDeprecated, CraftResultsDeprecated;

            fun getId(version: Int) = registry.getLastInt(version, this)

            companion object {
                private val registry = Int2ObjectOpenHashBiMapVersioned<Type>().apply {
                    put(Move                         , V1_16_010 to  0                                                   )
                    put(Place                        , V1_16_010 to  1                                                   )
                    put(Swap                         , V1_16_010 to  2                                                   )
                    put(Drop                         , V1_16_010 to  3                                                   )
                    put(Destroy                      , V1_16_010 to  4                                                   )
                    put(Consume                      , V1_16_010 to  5                                                   )
                    put(Create                       , V1_16_010 to  6                                                   )
                    put(LabTableCombine              , V1_16_010 to  7                                                   )
                    put(BeaconPayment                , V1_16_010 to  8                                                   )
                    put(MineBlock                    ,                                   V1_16_210 to  9                 )
                    put(CraftRecipe                  , V1_16_010 to  9                 , V1_16_210 to 10                 )
                    put(CraftRecipeAuto              , V1_16_010 to 10                 , V1_16_210 to 11                 )
                    put(CraftCreative                , V1_16_010 to 11                 , V1_16_210 to 12                 )
                    put(CraftRecipeOptional          ,                  V1_16_201 to 12, V1_16_210 to 13                 )
                    put(CraftRepairAndDisenchant     ,                                                    V1_17_041 to 14)
                    put(CraftLoom                    ,                                                    V1_17_041 to 15)
                    put(CraftNonImplementedDeprecated, V1_16_010 to 12, V1_16_201 to 13, V1_16_210 to 14, V1_17_041 to 16)
                    put(CraftResultsDeprecated       , V1_16_010 to 13, V1_16_201 to 14, V1_16_210 to 15, V1_17_041 to 17)
                }

                operator fun get(version: Int, id: Int) = registry.getLast(version, id)
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
        override val type get() = Move
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
        override val type get() = Place
    }

    data class SwapAction(
        val sourceSlotType: WindowSlotType?,
        val sourceSlotId: Int,
        val sourceNetId: Int,
        val destinationSlotType: WindowSlotType?,
        val destinationSlotId: Int,
        val destinationNetId: Int
    ) : Action {
        override val type get() = Swap
    }

    data class DropAction(
        val count: Int,
        val sourceSlotType: WindowSlotType?,
        val sourceSlotId: Int,
        val sourceNetId: Int,
        val random: Boolean
    ) : Action {
        override val type get() = Drop
    }

    data class DestroyAction(
        val count: Int,
        val sourceSlotType: WindowSlotType?,
        val sourceSlotId: Int,
        val sourceNetId: Int,
    ) : Action {
        override val type get() = Destroy
    }

    data class ConsumeAction(
        val count: Int,
        val sourceSlotType: WindowSlotType?,
        val sourceSlotId: Int,
        val sourceNetId: Int,
    ) : Action {
        override val type get() = Consume
    }

    data class CreateAction(
        val slotId: Int
    ) : Action {
        override val type get() = Create
    }

    class LabTableCombineAction : Action {
        override val type get() = LabTableCombine

        override fun toString() = "LabTableCombineAction()"
    }

    data class BeaconPaymentAction(
        val primaryEffect: Int,
        val secondaryEffect: Int
    ) : Action {
        override val type get() = BeaconPayment
    }

    data class MineBlockAction(
        val hotbarSlot: Int,
        val durability: Int,
        val netId: Int
    ) : Action {
        override val type get() = MineBlock
    }

    data class CraftRecipeAction(
        val recipeNetId: Int
    ) : Action {
        override val type get() = CraftRecipe
    }

    data class CraftRecipeAutoAction(
        val recipeNetId: Int,
        val count: Int
    ) : Action {
        override val type get() = CraftRecipeAuto
    }

    data class CraftCreativeAction(
        val netId: Int
    ) : Action {
        override val type get() = CraftCreative
    }

    data class CraftRecipeOptionalAction(
        val recipeNetId: Int,
        val filteredText: Int
    ) : Action {
        override val type get() = CraftRecipeOptional
    }

    data class CraftRepairAndDisenchantAction(
        val recipeNetId: Int,
        val repairCost: Int
    ) : Action {
        override val type get() = CraftRepairAndDisenchant
    }

    data class CraftLoomAction(
        val patternId: String
    ) : Action {
        override val type get() = CraftLoom
    }

    class CraftNonImplementedDeprecatedAction : Action {
        override val type get() = CraftNonImplementedDeprecated

        override fun toString() = "CraftNonImplementedDeprecatedAction()"
    }

    data class CraftResultsDeprecatedAction(
        val result: List<ItemStack?>,
        val count: Int
    ) : Action {
        override val type get() = CraftResultsDeprecated
    }

    override val id get() = 0x93

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarUInt(requests.size)
        requests.forEach {
            buffer.writeVarInt(it.requestId)
            buffer.writeVarUInt(it.actions.size)
            it.actions.forEach {
                buffer.writeByte(it.type.getId(version))
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
                    is LabTableCombineAction -> Unit
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
                    is CraftNonImplementedDeprecatedAction -> Unit
                    is CraftResultsDeprecatedAction -> {
                        buffer.writeVarUInt(it.result.size)
                        it.result.forEach { buffer.writeItemStack(it, version, false) }
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

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = InventoryRequestPacket(LazyList(buffer.readVarUInt()) {
            Request(buffer.readVarInt(), LazyList(buffer.readVarUInt()) {
                when (Action.Type[version, buffer.readUnsignedByte().toInt()]!!) {
                    Move -> MoveAction(buffer.readUnsignedByte().toInt(), WindowSlotType.values()[buffer.readUnsignedByte().toInt()], buffer.readUnsignedByte().toInt(), buffer.readVarInt(), WindowSlotType.values()[buffer.readUnsignedByte().toInt()], buffer.readUnsignedByte().toInt(), buffer.readVarInt())
                    Place -> PlaceAction(buffer.readUnsignedByte().toInt(), WindowSlotType.values()[buffer.readUnsignedByte().toInt()], buffer.readUnsignedByte().toInt(), buffer.readVarInt(), WindowSlotType.values()[buffer.readUnsignedByte().toInt()], buffer.readUnsignedByte().toInt(), buffer.readVarInt())
                    Swap -> SwapAction(WindowSlotType.values()[buffer.readUnsignedByte().toInt()], buffer.readUnsignedByte().toInt(), buffer.readVarInt(), WindowSlotType.values()[buffer.readUnsignedByte().toInt()], buffer.readUnsignedByte().toInt(), buffer.readVarInt())
                    Drop -> DropAction(buffer.readUnsignedByte().toInt(), WindowSlotType.values()[buffer.readUnsignedByte().toInt()], buffer.readUnsignedByte().toInt(), buffer.readVarInt(), buffer.readBoolean())
                    Destroy -> DestroyAction(buffer.readUnsignedByte().toInt(), WindowSlotType.values()[buffer.readUnsignedByte().toInt()], buffer.readUnsignedByte().toInt(), buffer.readVarInt())
                    Consume -> ConsumeAction(buffer.readUnsignedByte().toInt(), WindowSlotType.values()[buffer.readUnsignedByte().toInt()], buffer.readUnsignedByte().toInt(), buffer.readVarInt())
                    Create -> CreateAction(buffer.readUnsignedByte().toInt())
                    LabTableCombine -> LabTableCombineAction()
                    BeaconPayment -> BeaconPaymentAction(buffer.readVarInt(), buffer.readVarInt())
                    MineBlock -> MineBlockAction(buffer.readVarInt(), buffer.readVarInt(), buffer.readVarInt())
                    CraftRecipe -> CraftRecipeAction(buffer.readVarUInt())
                    CraftRecipeAuto -> CraftRecipeAutoAction(buffer.readVarUInt(), if (version >= V1_17_011) buffer.readUnsignedByte().toInt() else 0)
                    CraftCreative -> CraftCreativeAction(buffer.readVarUInt())
                    CraftRecipeOptional -> CraftRecipeOptionalAction(buffer.readVarUInt(), buffer.readIntLE())
                    CraftRepairAndDisenchant -> CraftRepairAndDisenchantAction(buffer.readVarUInt(), buffer.readVarInt())
                    CraftLoom -> CraftLoomAction(buffer.readString())
                    CraftNonImplementedDeprecated -> CraftNonImplementedDeprecatedAction()
                    CraftResultsDeprecated -> CraftResultsDeprecatedAction(LazyList(buffer.readVarUInt()) { buffer.readItemStack(version, false) }, buffer.readUnsignedByte().toInt())
                }
            }, LazyList(buffer.readVarUInt()) { buffer.readString() })
        })
    }
}
