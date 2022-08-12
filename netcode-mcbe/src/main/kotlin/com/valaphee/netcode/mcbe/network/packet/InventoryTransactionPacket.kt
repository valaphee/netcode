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

import com.valaphee.foundry.math.Float3
import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.V1_16_010
import com.valaphee.netcode.mcbe.network.V1_16_221
import com.valaphee.netcode.mcbe.world.block.BlockState
import com.valaphee.netcode.mcbe.world.inventory.WindowId
import com.valaphee.netcode.mcbe.world.item.ItemStack
import com.valaphee.netcode.mcbe.world.item.readItemStack
import com.valaphee.netcode.mcbe.world.item.writeItemStack
import com.valaphee.netcode.util.LazyList
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

/**
 * @author Kevin Ludwig
 */
class InventoryTransactionPacket(
    val legacyRequestId: Int,
    val legacySlots: List<LegacySlot>?,
    val type: Type,
    val usingNetIds: Boolean,
    val actions: List<Action>,
    val actionId: Int,
    val runtimeEntityId: Long,
    val blockPosition: Int3?,
    val blockFace: Int,
    val hotbarSlot: Int,
    val itemStackInHand: ItemStack?,
    val fromPosition: Float3?,
    val clickPosition: Float3?,
    val headPosition: Float3?,
    val blockState: BlockState?
) : Packet() {
    data class LegacySlot(
        val windowId: Int,
        val slotIds: ByteArray
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as LegacySlot

            if (windowId != other.windowId) return false
            if (!slotIds.contentEquals(other.slotIds)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = windowId
            result = 31 * result + slotIds.contentHashCode()
            return result
        }
    }

    enum class Type {
        Normal, Mismatch, ItemUse, ItemUseOnEntity, ItemRelease
    }

    data class Action(
        val source: Source,
        val slotId: Int,
        val fromItemStack: ItemStack?,
        val toItemStack: ItemStack?,
        val netId: Int
    )

    data class Source(
        val type: Type,
        val windowId: Int,
        val action: Action
    ) {
        enum class Type(
            val id: Int
        ) {
            None(-1),
            Inventory(0),
            Global(1),
            World(2),
            Creative(3),
            UntrackedInteractionUi(100),
            NonImplemented(99999);

            companion object {
                fun byId(id: Int): Type = byId[id]

                private val byId = Int2ObjectOpenHashMap<Type>(values().size).apply { values().forEach { this[it.id] = it } }
            }
        }

        enum class Action {
            DropItem, PickupItem, None
        }
    }

    companion object ActionId {
        const val ItemUseBlock = 0
        const val ItemUseAir = 1
        const val ItemUseDestroy = 2
        const val ItemUseOnEntityInteract = 0
        const val ItemUseOnEntityAttack = 1
        const val ItemReleaseRelease = 0
        const val ItemReleaseConsume = 1
    }

    override val id get() = 0x1E

    override fun write(buffer: PacketBuffer, version: Int) {
        if (version >= V1_16_010) {
            buffer.writeVarInt(legacyRequestId)
            if (legacyRequestId < -1 && (legacyRequestId and 1) == 0) {
                legacySlots!!.let {
                    buffer.writeVarUInt(it.size)
                    it.forEach {
                        buffer.writeByte(it.windowId)
                        buffer.writeByteArray(it.slotIds)
                    }
                }
            }
        }
        buffer.writeVarUInt(type.ordinal)
        if (version in V1_16_010 until V1_16_221) buffer.writeBoolean(usingNetIds)
        buffer.writeVarUInt(actions.size)
        actions.forEach { buffer.writeAction(it, version, usingNetIds) }
        when (type) {
            Type.Normal, Type.Mismatch -> Unit
            Type.ItemUse -> {
                buffer.writeVarUInt(actionId)
                buffer.writeBlockPosition(blockPosition!!)
                buffer.writeVarInt(blockFace)
                buffer.writeVarInt(hotbarSlot)
                buffer.writeItemStack(itemStackInHand, version)
                buffer.writeFloat3(fromPosition!!)
                buffer.writeFloat3(clickPosition!!)
                buffer.writeVarUInt(blockState!!.getId(version))
            }
            Type.ItemUseOnEntity -> {
                buffer.writeVarULong(runtimeEntityId)
                buffer.writeVarUInt(actionId)
                buffer.writeVarInt(hotbarSlot)
                buffer.writeItemStack(itemStackInHand, version)
                buffer.writeFloat3(fromPosition!!)
                buffer.writeFloat3(clickPosition!!)
            }
            Type.ItemRelease -> {
                buffer.writeVarUInt(actionId)
                buffer.writeVarInt(hotbarSlot)
                buffer.writeItemStack(itemStackInHand, version)
                buffer.writeFloat3(headPosition!!)
            }
        }
    }

    override fun handle(handler: PacketHandler) = handler.inventoryTransaction(this)

    override fun toString() = "InventoryTransactionPacket(legacyRequestId=$legacyRequestId, legacySlots=$legacySlots, type=$type, usingNetIds=$usingNetIds, actions=$actions, actionId=$actionId, runtimeEntityId=$runtimeEntityId, blockPosition=$blockPosition, blockFace=$blockFace, hotbarSlot=$hotbarSlot, stackInHand=$itemStackInHand, fromPosition=$fromPosition, clickPosition=$clickPosition, headPosition=$headPosition, blockState=$blockState)"


    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int): InventoryTransactionPacket {
            val legacyRequestId: Int
            val legacySlots: List<LegacySlot>?
            if (version >= V1_16_010) {
                legacyRequestId = buffer.readVarInt()
                legacySlots = if (legacyRequestId < -1 && (legacyRequestId and 1) == 0) LazyList(buffer.readVarUInt()) { LegacySlot(buffer.readByte().toInt(), buffer.readByteArray()) } else null
            } else {
                legacyRequestId = 0
                legacySlots = null
            }
            val type = Type.values()[buffer.readVarUInt()]
            val usingNetIds = if (version in V1_16_010 until V1_16_221) buffer.readBoolean() else false
            val actions = LazyList(buffer.readVarUInt()) { buffer.readAction(version, usingNetIds) }
            val actionId: Int
            val runtimeEntityId: Long
            val position: Int3?
            val blockFace: Int
            val hotbarSlot: Int
            val itemStackInHand: ItemStack?
            val fromPosition: Float3?
            val clickPosition: Float3?
            val headPosition: Float3?
            val blockState: BlockState?
            when (type) {
                Type.ItemUse -> {
                    actionId = buffer.readVarUInt()
                    runtimeEntityId = 0
                    position = buffer.readBlockPosition()
                    blockFace = buffer.readVarInt()
                    hotbarSlot = buffer.readVarInt()
                    itemStackInHand = buffer.readItemStack(version)
                    fromPosition = buffer.readFloat3()
                    clickPosition = buffer.readFloat3()
                    headPosition = null
                    blockState = BlockState[version, buffer.readVarUInt()]
                }
                Type.ItemUseOnEntity -> {
                    runtimeEntityId = buffer.readVarULong()
                    actionId = buffer.readVarUInt()
                    position = null
                    blockFace = 0
                    hotbarSlot = buffer.readVarInt()
                    itemStackInHand = buffer.readItemStack(version)
                    fromPosition = buffer.readFloat3()
                    clickPosition = buffer.readFloat3()
                    headPosition = null
                    blockState = null
                }
                Type.ItemRelease -> {
                    actionId = buffer.readVarUInt()
                    runtimeEntityId = 0
                    position = null
                    blockFace = 0
                    hotbarSlot = buffer.readVarInt()
                    itemStackInHand = buffer.readItemStack(version)
                    fromPosition = null
                    clickPosition = null
                    headPosition = buffer.readFloat3()
                    blockState = null
                }
                else -> {
                    actionId = 0
                    runtimeEntityId = 0
                    position = null
                    blockFace = 0
                    hotbarSlot = 0
                    itemStackInHand = null
                    fromPosition = null
                    clickPosition = null
                    headPosition = null
                    blockState = null
                }
            }
            return InventoryTransactionPacket(legacyRequestId, legacySlots, type, usingNetIds, actions, actionId, runtimeEntityId, position, blockFace, hotbarSlot, itemStackInHand, fromPosition, clickPosition, headPosition, blockState)
        }
    }
}

fun PacketBuffer.readAction(version: Int, withNetId: Boolean) = InventoryTransactionPacket.Action(readSource(), readVarUInt(), readItemStack(version), readItemStack(version), if (withNetId && version < V1_16_221) readVarInt() else 0)

fun PacketBuffer.writeAction(value: InventoryTransactionPacket.Action, version: Int, withNetId: Boolean) {
    writeSource(value.source)
    writeVarUInt(value.slotId)
    writeItemStack(value.fromItemStack, version)
    writeItemStack(value.toItemStack, version)
    if (withNetId && version < V1_16_221) writeVarInt(value.netId)
}

fun PacketBuffer.readSource() = when (val type = InventoryTransactionPacket.Source.Type.byId(readVarUInt())) {
    InventoryTransactionPacket.Source.Type.None, InventoryTransactionPacket.Source.Type.Global, InventoryTransactionPacket.Source.Type.Creative -> InventoryTransactionPacket.Source(type, WindowId.None, InventoryTransactionPacket.Source.Action.None)
    InventoryTransactionPacket.Source.Type.Inventory, InventoryTransactionPacket.Source.Type.UntrackedInteractionUi, InventoryTransactionPacket.Source.Type.NonImplemented -> InventoryTransactionPacket.Source(type, readVarInt(), InventoryTransactionPacket.Source.Action.None)
    InventoryTransactionPacket.Source.Type.World -> InventoryTransactionPacket.Source(InventoryTransactionPacket.Source.Type.World, WindowId.None, InventoryTransactionPacket.Source.Action.values()[readVarUInt()])
}

fun PacketBuffer.writeSource(value: InventoryTransactionPacket.Source) {
    writeVarUInt(value.type.id)
    when (value.type) {
        InventoryTransactionPacket.Source.Type.None, InventoryTransactionPacket.Source.Type.Global, InventoryTransactionPacket.Source.Type.Creative -> Unit
        InventoryTransactionPacket.Source.Type.Inventory, InventoryTransactionPacket.Source.Type.UntrackedInteractionUi, InventoryTransactionPacket.Source.Type.NonImplemented -> writeVarInt(value.windowId)
        InventoryTransactionPacket.Source.Type.World -> writeVarUInt(value.action.ordinal)
    }
}
