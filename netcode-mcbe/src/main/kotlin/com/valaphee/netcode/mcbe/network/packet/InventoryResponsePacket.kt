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
import com.valaphee.netcode.mcbe.network.V1_16_100
import com.valaphee.netcode.mcbe.network.V1_16_201
import com.valaphee.netcode.mcbe.network.V1_16_210
import com.valaphee.netcode.util.LazyList

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class InventoryResponsePacket(
    val responses: List<Response>
) : Packet() {
    data class Response(
        val status: Status,
        val requestId: Int,
        val windows: List<Window>
    ) {
        enum class Status {
            Ok,
            Error,
            InvalidRequestActionType,
            ActionRequestNotAllowed,
            ScreenHandlerEndRequestFailed,
            ItemRequestActionHandlerCommitFailed,
            InvalidRequestCraftActionType,
            InvalidCraftRequest,
            InvalidCraftRequestScreen,
            InvalidCraftResult,
            InvalidCraftResultIndex,
            InvalidCraftResultItem,
            InvalidItemNetId,
            MissingCreatedOutputContainer,
            FailedToSetCreatedItemOutputSlot,
            RequestAlreadyInProgress,
            FailedToInitSparseContainer,
            ResultTransferFailed,
            ExpectedItemSlotNotFullyConsumed,
            ExpectedAnywhereItemNotFullyConsumed,
            ItemAlreadyConsumedFromSlot,
            ConsumedTooMuchFromSlot,
            MismatchSlotExpectedConsumedItem,
            MismatchSlotExpectedConsumedItemNetIdVariant,
            FailedToMatchExpectedSlotConsumedItem,
            FailedToMatchExpectedAllowedAnywhereConsumedItem,
            ConsumedItemOutOfAllowedSlotRange,
            ConsumedItemNotAllowed,
            PlayerNotInCreativeMode,
            InvalidExperimentalRecipeRequest,
            FailedToCraftCreative,
            FailedToGetLevelRecipe,
            FailedToFindReceiptByNetId,
            MismatchedCraftingSize,
            MissingInputSparseContainer,
            MismatchedRecipeForInputGridItems,
            EmptyCraftResults,
            FailedToEnchant,
            MissingInputItem,
            InsufficientPlayerLevelToEnchant,
            MissingMaterialItem,
            MissingActor,
            UnknownPrimaryEffect,
            PrimaryEffectOutOfRange,
            PrimaryEffectUnavailable,
            SecondaryEffectOutOfRange,
            SecondaryEffectUnavailable,
            DstContainerEqualToCreatedOutputContainer,
            DstContainerAndSlotEqualToSrcContainerAndSlot,
            FailedToValidateSrcSlot,
            FailedToValidateDstSlot,
            InvalidAdjustedAmount,
            InvalidItemSetType,
            InvalidTransferAmount,
            CannotSwapItem,
            CannotPlaceItem,
            UnhandledItemSetType,
            InvalidRemovedAmount,
            InvalidRegion,
            CannotDropItem,
            CannotDestroyItem,
            InvalidSourceContainer,
            ItemNotConsumed,
            InvalidNumCrafts,
            InvalidCraftResultStackSize,
            CannotRemoveItem,
            CannotConsumeItem
        }

        data class Window(
            val windowId: Int,
            val slots: List<Slot>
        ) {
            data class Slot(
                val slotId: Int,
                val hotbarSlot: Int,
                val count: Int,
                val netId: Int,
                val name: String?,
                val damage: Int
            )
        }
    }

    override val id get() = 0x94

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarUInt(responses.size)
        responses.forEach {
            if (version >= V1_16_100) buffer.writeByte(it.status.ordinal) else buffer.writeBoolean(
                when (it.status) {
                    Response.Status.Ok -> true
                    else -> false
                }
            )
            buffer.writeVarInt(it.requestId)
            if (it.status == Response.Status.Ok) {
                buffer.writeVarUInt(it.windows.size)
                it.windows.forEach {
                    buffer.writeByte(it.windowId)
                    buffer.writeVarUInt(it.slots.size)
                    it.slots.forEach {
                        buffer.writeByte(it.slotId)
                        buffer.writeByte(it.hotbarSlot)
                        buffer.writeByte(it.count)
                        buffer.writeVarInt(it.netId)
                        if (version >= V1_16_201) buffer.writeString(it.name!!)
                        if (version >= V1_16_210) buffer.writeVarInt(it.damage)
                    }
                }
            }
        }
    }

    override fun handle(handler: PacketHandler) = handler.inventoryResponse(this)

    override fun toString() = "InventoryResponsePacket(responses=$responses)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = InventoryResponsePacket(LazyList(buffer.readVarUInt()) {
            val status = if (version >= V1_16_100) Response.Status.values()[buffer.readUnsignedByte().toInt()] else if (buffer.readBoolean()) Response.Status.Ok else Response.Status.Error
            val requestId = buffer.readVarInt()
            val windows = if (status == Response.Status.Ok) LazyList(buffer.readVarUInt()) { Response.Window(buffer.readByte().toInt(), LazyList(buffer.readVarUInt()) { Response.Window.Slot(buffer.readUnsignedByte().toInt(), buffer.readUnsignedByte().toInt(), buffer.readUnsignedByte().toInt(), buffer.readVarInt(), if (version >= V1_16_201) buffer.readString16() else null, if (version >= V1_16_210) buffer.readVarInt() else 0) }) } else emptyList()
            Response(status, requestId, windows)

        })
    }
}
