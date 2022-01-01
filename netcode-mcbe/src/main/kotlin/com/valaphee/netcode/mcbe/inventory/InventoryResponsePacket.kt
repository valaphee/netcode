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

package com.valaphee.netcode.mcbe.inventory

import com.valaphee.netcode.mcbe.Packet
import com.valaphee.netcode.mcbe.PacketBuffer
import com.valaphee.netcode.mcbe.PacketHandler
import com.valaphee.netcode.mcbe.Restrict
import com.valaphee.netcode.mcbe.Restriction

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class InventoryResponsePacket(
    val responses: List<Response>
) : Packet() {
    class Response(
        val status: ResponseStatus,
        val requestId: Int,
        val windows: List<ResponseWindow>
    )

    enum class ResponseStatus {
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

    class ResponseWindow(
        val windowId: Int,
        val slots: List<ResponseWindowSlot>
    )

    class ResponseWindowSlot(
        val slotId: Int,
        val hotbarSlot: Int,
        val count: Int,
        val netId: Int,
        val name: String,
        val damage: Int
    )

    override val id get() = 0x94

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarUInt(responses.size)
        responses.forEach {
            if (version >= 419) buffer.writeByte(it.status.ordinal) else buffer.writeBoolean(
                when (it.status) {
                    ResponseStatus.Ok -> true
                    else -> false
                }
            )
            buffer.writeVarInt(it.requestId)
            if (it.status == ResponseStatus.Ok) {
                buffer.writeVarUInt(it.windows.size)
                it.windows.forEach {
                    buffer.writeByte(it.windowId)
                    buffer.writeVarUInt(it.slots.size)
                    it.slots.forEach {
                        buffer.writeByte(it.slotId)
                        buffer.writeByte(it.hotbarSlot)
                        buffer.writeByte(it.count)
                        buffer.writeVarInt(it.netId)
                        if (version >= 422) {
                            buffer.writeString(it.name)
                            if (version >= 428) buffer.writeVarInt(it.damage)
                        }
                    }
                }
            }
        }
    }

    override fun handle(handler: PacketHandler) = handler.inventoryResponse(this)

    override fun toString() = "InventoryResponsePacket(responses=$responses)"
}
