package com.valaphee.netcode.mcbe.world.inventory

import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.V1_16_010
import com.valaphee.netcode.mcbe.network.V1_16_201
import com.valaphee.netcode.mcbe.network.V1_16_210
import com.valaphee.netcode.mcbe.network.V1_17_011
import com.valaphee.netcode.mcbe.network.V1_17_041
import com.valaphee.netcode.mcbe.world.item.ItemStack
import com.valaphee.netcode.mcbe.world.item.readItemStack
import com.valaphee.netcode.mcbe.world.item.writeItemStack
import com.valaphee.netcode.util.Int2ObjectOpenHashBiMapVersioned
import com.valaphee.netcode.util.LazyList

data class InventoryRequest(
    val requestId: Int,
    val actions: List<InventoryAction>,
    val filteredTexts: List<String>
)

fun PacketBuffer.readInventoryRequest(version: Int) = InventoryRequest(readVarInt(), LazyList(readVarUInt()) {
    when (InventoryAction.Type[version, readUnsignedByte().toInt()]!!) {
        InventoryAction.Type.Move -> MoveInventoryAction(readUnsignedByte().toInt(), WindowSlotType.values()[readUnsignedByte().toInt()], readUnsignedByte().toInt(), readVarInt(), WindowSlotType.values()[readUnsignedByte().toInt()], readUnsignedByte().toInt(), readVarInt())
        InventoryAction.Type.Place -> PlaceInventoryAction(readUnsignedByte().toInt(), WindowSlotType.values()[readUnsignedByte().toInt()], readUnsignedByte().toInt(), readVarInt(), WindowSlotType.values()[readUnsignedByte().toInt()], readUnsignedByte().toInt(), readVarInt())
        InventoryAction.Type.Swap -> SwapInventoryAction(WindowSlotType.values()[readUnsignedByte().toInt()], readUnsignedByte().toInt(), readVarInt(), WindowSlotType.values()[readUnsignedByte().toInt()], readUnsignedByte().toInt(), readVarInt())
        InventoryAction.Type.Drop -> DropInventoryAction(readUnsignedByte().toInt(), WindowSlotType.values()[readUnsignedByte().toInt()], readUnsignedByte().toInt(), readVarInt(), readBoolean())
        InventoryAction.Type.Destroy -> DestroyInventoryAction(readUnsignedByte().toInt(), WindowSlotType.values()[readUnsignedByte().toInt()], readUnsignedByte().toInt(), readVarInt())
        InventoryAction.Type.Consume -> ConsumeInventoryAction(readUnsignedByte().toInt(), WindowSlotType.values()[readUnsignedByte().toInt()], readUnsignedByte().toInt(), readVarInt())
        InventoryAction.Type.Create -> CreateInventoryAction(readUnsignedByte().toInt())
        InventoryAction.Type.LabTableCombine -> LabTableCombineInventoryAction()
        InventoryAction.Type.BeaconPayment -> BeaconPaymentInventoryAction(readVarInt(), readVarInt())
        InventoryAction.Type.MineBlock -> MineBlockInventoryAction(readVarInt(), readVarInt(), readVarInt())
        InventoryAction.Type.CraftRecipe -> CraftRecipeInventoryAction(readVarUInt())
        InventoryAction.Type.CraftRecipeAuto -> CraftRecipeAutoInventoryAction(readVarUInt(), if (version >= V1_17_011) readUnsignedByte().toInt() else 0)
        InventoryAction.Type.CraftCreative -> CraftCreativeInventoryAction(readVarUInt())
        InventoryAction.Type.CraftRecipeOptional -> CraftRecipeOptionalInventoryAction(readVarUInt(), readIntLE())
        InventoryAction.Type.CraftRepairAndDisenchant -> CraftRepairAndDisenchantInventoryAction(readVarUInt(), readVarInt())
        InventoryAction.Type.CraftLoom -> CraftLoomInventoryAction(readString())
        InventoryAction.Type.CraftNonImplementedDeprecated -> CraftNonImplementedDeprecatedInventoryAction()
        InventoryAction.Type.CraftResultsDeprecated -> CraftResultsDeprecatedInventoryAction(LazyList(readVarUInt()) { readItemStack(version, false) }, readUnsignedByte().toInt())
    }
}, LazyList(readVarUInt()) { readString() })

fun PacketBuffer.writeInventoryRequest(value: InventoryRequest, version: Int) {
    writeVarInt(value.requestId)
    writeVarUInt(value.actions.size)
    value.actions.forEach {
        writeByte(it.type.getId(version))
        when (it) {
            is MoveInventoryAction -> {
                writeByte(it.count)
                writeByte(it.sourceSlotType!!.ordinal)
                writeByte(it.sourceSlotId)
                writeVarInt(it.sourceNetId)
                writeByte(it.destinationSlotType!!.ordinal)
                writeByte(it.destinationSlotId)
                writeVarInt(it.destinationNetId)
            }
            is PlaceInventoryAction -> {
                writeByte(it.count)
                writeByte(it.sourceSlotType!!.ordinal)
                writeByte(it.sourceSlotId)
                writeVarInt(it.sourceNetId)
                writeByte(it.destinationSlotType!!.ordinal)
                writeByte(it.destinationSlotId)
                writeVarInt(it.destinationNetId)
            }
            is SwapInventoryAction -> {
                writeByte(it.sourceSlotType!!.ordinal)
                writeByte(it.sourceSlotId)
                writeVarInt(it.sourceNetId)
                writeByte(it.destinationSlotType!!.ordinal)
                writeByte(it.destinationSlotId)
                writeVarInt(it.destinationNetId)
            }
            is DropInventoryAction -> {
                writeByte(it.count)
                writeByte(it.sourceSlotType!!.ordinal)
                writeByte(it.sourceSlotId)
                writeVarInt(it.sourceNetId)
                writeBoolean(it.random)
            }
            is DestroyInventoryAction -> {
                writeByte(it.count)
                writeByte(it.sourceSlotType!!.ordinal)
                writeByte(it.sourceSlotId)
                writeVarInt(it.sourceNetId)
            }
            is ConsumeInventoryAction -> {
                writeByte(it.count)
                writeByte(it.sourceSlotType!!.ordinal)
                writeByte(it.sourceSlotId)
                writeVarInt(it.sourceNetId)
            }
            is CreateInventoryAction -> writeByte(it.slotId)
            is LabTableCombineInventoryAction -> Unit
            is BeaconPaymentInventoryAction -> {
                writeVarInt(it.primaryEffect)
                writeVarInt(it.secondaryEffect)
            }
            is MineBlockInventoryAction -> {
                writeVarInt(it.hotbarSlot)
                writeVarInt(it.durability)
                writeVarInt(it.netId)
            }
            is CraftRecipeInventoryAction -> {
                writeVarUInt(it.recipeNetId)
            }
            is CraftRecipeAutoInventoryAction -> {
                writeVarUInt(it.recipeNetId)
                if (version >= V1_17_011) writeByte(it.count)
            }
            is CraftCreativeInventoryAction -> writeVarUInt(it.netId)
            is CraftRecipeOptionalInventoryAction -> {
                writeVarUInt(it.recipeNetId)
                writeIntLE(it.filteredText)
            }
            is CraftRepairAndDisenchantInventoryAction -> {
                writeVarUInt(it.recipeNetId)
                writeVarInt(it.repairCost)
            }
            is CraftLoomInventoryAction -> writeString(it.patternId)
            is CraftNonImplementedDeprecatedInventoryAction -> Unit
            is CraftResultsDeprecatedInventoryAction -> {
                writeVarUInt(it.result.size)
                it.result.forEach { writeItemStack(it, version, false) }
                writeByte(it.count)
            }
        }
    }
    if (version >= V1_16_201) {
        writeVarUInt(value.filteredTexts.size)
        value.filteredTexts.forEach { writeString(it) }
    }
}

sealed interface InventoryAction {
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

data class MoveInventoryAction(
    val count: Int,
    val sourceSlotType: WindowSlotType?,
    val sourceSlotId: Int,
    val sourceNetId: Int,
    val destinationSlotType: WindowSlotType?,
    val destinationSlotId: Int,
    val destinationNetId: Int
) : InventoryAction {
    override val type get() = InventoryAction.Type.Move
}

data class PlaceInventoryAction(
    val count: Int,
    val sourceSlotType: WindowSlotType?,
    val sourceSlotId: Int,
    val sourceNetId: Int,
    val destinationSlotType: WindowSlotType?,
    val destinationSlotId: Int,
    val destinationNetId: Int
) : InventoryAction {
    override val type get() = InventoryAction.Type.Place
}

data class SwapInventoryAction(
    val sourceSlotType: WindowSlotType?,
    val sourceSlotId: Int,
    val sourceNetId: Int,
    val destinationSlotType: WindowSlotType?,
    val destinationSlotId: Int,
    val destinationNetId: Int
) : InventoryAction {
    override val type get() = InventoryAction.Type.Swap
}

data class DropInventoryAction(
    val count: Int,
    val sourceSlotType: WindowSlotType?,
    val sourceSlotId: Int,
    val sourceNetId: Int,
    val random: Boolean
) : InventoryAction {
    override val type get() = InventoryAction.Type.Drop
}

data class DestroyInventoryAction(
    val count: Int,
    val sourceSlotType: WindowSlotType?,
    val sourceSlotId: Int,
    val sourceNetId: Int,
) : InventoryAction {
    override val type get() = InventoryAction.Type.Destroy
}

data class ConsumeInventoryAction(
    val count: Int,
    val sourceSlotType: WindowSlotType?,
    val sourceSlotId: Int,
    val sourceNetId: Int,
) : InventoryAction {
    override val type get() = InventoryAction.Type.Consume
}

data class CreateInventoryAction(
    val slotId: Int
) : InventoryAction {
    override val type get() = InventoryAction.Type.Create
}

class LabTableCombineInventoryAction : InventoryAction {
    override val type get() = InventoryAction.Type.LabTableCombine

    override fun toString() = "LabTableCombineAction()"
}

data class BeaconPaymentInventoryAction(
    val primaryEffect: Int,
    val secondaryEffect: Int
) : InventoryAction {
    override val type get() = InventoryAction.Type.BeaconPayment
}

data class MineBlockInventoryAction(
    val hotbarSlot: Int,
    val durability: Int,
    val netId: Int
) : InventoryAction {
    override val type get() = InventoryAction.Type.MineBlock
}

data class CraftRecipeInventoryAction(
    val recipeNetId: Int
) : InventoryAction {
    override val type get() = InventoryAction.Type.CraftRecipe
}

data class CraftRecipeAutoInventoryAction(
    val recipeNetId: Int,
    val count: Int
) : InventoryAction {
    override val type get() = InventoryAction.Type.CraftRecipeAuto
}

data class CraftCreativeInventoryAction(
    val netId: Int
) : InventoryAction {
    override val type get() = InventoryAction.Type.CraftCreative
}

data class CraftRecipeOptionalInventoryAction(
    val recipeNetId: Int,
    val filteredText: Int
) : InventoryAction {
    override val type get() = InventoryAction.Type.CraftRecipeOptional
}

data class CraftRepairAndDisenchantInventoryAction(
    val recipeNetId: Int,
    val repairCost: Int
) : InventoryAction {
    override val type get() = InventoryAction.Type.CraftRepairAndDisenchant
}

data class CraftLoomInventoryAction(
    val patternId: String
) : InventoryAction {
    override val type get() = InventoryAction.Type.CraftLoom
}

class CraftNonImplementedDeprecatedInventoryAction : InventoryAction {
    override val type get() = InventoryAction.Type.CraftNonImplementedDeprecated

    override fun toString() = "CraftNonImplementedDeprecatedAction()"
}

data class CraftResultsDeprecatedInventoryAction(
    val result: List<ItemStack?>,
    val count: Int
) : InventoryAction {
    override val type get() = InventoryAction.Type.CraftResultsDeprecated
}
