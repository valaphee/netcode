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

import com.valaphee.foundry.math.Float2
import com.valaphee.foundry.math.Float3
import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.Restrict
import com.valaphee.netcode.mcbe.network.Restriction
import com.valaphee.netcode.mcbe.network.V1_16_100
import com.valaphee.netcode.mcbe.network.V1_16_210
import com.valaphee.netcode.mcbe.network.V1_19_000
import com.valaphee.netcode.mcbe.world.entity.player.User
import com.valaphee.netcode.mcbe.world.inventory.InventoryRequest
import com.valaphee.netcode.mcbe.world.inventory.readInventoryRequest
import com.valaphee.netcode.mcbe.world.inventory.writeInventoryRequest
import com.valaphee.netcode.util.LazyList

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToServer)
class InputPacket(
    val rotation: Float2,
    val position: Float3,
    val move: Float2,
    val headRotationYaw: Float,
    val input: Set<Input>,
    val inputMode: User.InputMode,
    val playMode: PlayMode,
    val inputInteractionModel: InputInteractionModel,
    val virtualRealityGazeDirection: Float3? = null,
    val tick: Long,
    val positionDelta: Float3,
    val inventoryRequest: InventoryRequest?,
    val blockActions: List<BlockAction>?
) : Packet() {
    enum class PlayMode {
        Normal,
        Teaser,
        Screen,
        Viewer,
        VirtualReality,
        PlacementLivingRoom,
        ExitLevel,
        ExitLevelLivingRoom
    }

    enum class InputInteractionModel {
        Touch,
        Crosshair,
        Classic
    }

    enum class Input {
        Ascend,
        Descend,
        NorthJump,
        JumpDown,
        SprintDown,
        ChangeHeight,
        Jumping,
        AutoJumpingInWater,
        Sneaking,
        SneakDown,
        Up,
        Down,
        Left,
        Right,
        UpLeft,
        UpRight,
        WantUp,
        WantDown,
        WantDownSlow,
        WantUpSlow,
        Sprinting,
        AscendScaffolding,
        DescendScaffolding,
        SneakToggleDown,
        PersistSneak,
        StartSprinting,
        StopSprinting,
        StartSneaking,
        StopSneaking,
        StartSwimming,
        StopSwimming,
        StartJumping,
        StartGliding,
        StopGliding,
        PerformItemInteraction,
        PerformBlockActions,
        PerformInventoryRequest
    }

    data class BlockAction(
        val action: PlayerActionPacket.Action,
        val blockPosition: Int3?,
        val blockFace: Int
    )

    override val id get() = 0x90

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeFloat2(rotation)
        buffer.writeFloat3(position)
        buffer.writeFloat2(move)
        buffer.writeFloatLE(headRotationYaw)
        buffer.writeVarULongFlags(input)
        buffer.writeVarUInt(inputMode.ordinal)
        buffer.writeVarUInt(playMode.ordinal)
        if (version >= V1_19_000) buffer.writeVarUInt(inputInteractionModel.ordinal)
        if (playMode == PlayMode.VirtualReality) buffer.writeFloat3(virtualRealityGazeDirection!!)
        if (version >= V1_16_100) {
            buffer.writeVarULong(tick)
            buffer.writeFloat3(positionDelta)
        }
        if (version >= V1_16_210) {
            if (input.equals(Input.PerformItemInteraction)) TODO()
            if (input.equals(Input.PerformInventoryRequest)) buffer.writeInventoryRequest(inventoryRequest!!, version)
            if (input.equals(Input.PerformBlockActions)) blockActions!!.let {
                buffer.writeVarInt(it.size)
                it.forEach {
                    buffer.writeVarInt(it.action.ordinal)
                    when (it.action) {
                        PlayerActionPacket.Action.StartBreak, PlayerActionPacket.Action.AbortBreak, PlayerActionPacket.Action.ContinueBreak, PlayerActionPacket.Action.BlockPredictDestroy, PlayerActionPacket.Action.BlockContinueDestroy -> {
                            buffer.writeBlockPosition(it.blockPosition!!)
                            buffer.writeVarInt(it.blockFace)
                        }
                        else -> Unit
                    }
                }
            }
        }
    }

    override fun handle(handler: PacketHandler) = handler.input(this)

    override fun toString() = "InputPacket(rotation=$rotation, position=$position, move=$move, headRotationYaw=$headRotationYaw, input=$input, inputMode=$inputMode, playMode=$playMode, virtualRealityGazeDirection=$virtualRealityGazeDirection, tick=$tick, positionDelta=$positionDelta)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int): InputPacket {
            val rotation = buffer.readFloat2()
            val position = buffer.readFloat3()
            val move = buffer.readFloat2()
            val headRotationYaw = buffer.readFloatLE()
            val input = buffer.readVarULongFlags<Input>()
            val inputMode = User.InputMode.values()[buffer.readVarUInt()]
            val playMode = PlayMode.values()[buffer.readVarUInt()]
            val inputInteractionModel = if (version >= V1_19_000) InputInteractionModel.values()[buffer.readVarUInt()] else InputInteractionModel.Classic
            val virtualRealityGazeDirection = if (playMode == PlayMode.VirtualReality) buffer.readFloat3() else null
            val tick: Long
            val positionDelta: Float3
            if (version >= V1_16_100) {
                tick = buffer.readVarULong()
                positionDelta = buffer.readFloat3()
            } else {
                tick = 0L
                positionDelta = Float3.Zero
            }
            val inventoryRequest: InventoryRequest?
            val blockActions: List<BlockAction>?
            if (version >= V1_16_210) {
                if (input.equals(Input.PerformItemInteraction)) TODO()
                inventoryRequest = if (input.equals(Input.PerformInventoryRequest)) buffer.readInventoryRequest(version) else null
                blockActions = if (input.equals(Input.PerformBlockActions)) {
                    LazyList(buffer.readVarInt()) {
                        val action = PlayerActionPacket.Action.values()[buffer.readVarInt()];
                        val blockPosition: Int3?
                        val blockFace: Int
                        when (action) {
                            PlayerActionPacket.Action.StartBreak, PlayerActionPacket.Action.AbortBreak, PlayerActionPacket.Action.ContinueBreak, PlayerActionPacket.Action.BlockPredictDestroy, PlayerActionPacket.Action.BlockContinueDestroy -> {
                                blockPosition = buffer.readBlockPosition()
                                blockFace = buffer.readVarInt()
                            }
                            else -> {
                                blockPosition = null
                                blockFace = 0
                            }
                        }
                        BlockAction(action, blockPosition, blockFace)
                    }
                } else null
            } else {
                inventoryRequest = null
                blockActions = null
            }
            return InputPacket(rotation, position, move, headRotationYaw, input, inputMode, playMode, inputInteractionModel, virtualRealityGazeDirection, tick, positionDelta, inventoryRequest, blockActions)
        }
    }
}
