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
import com.valaphee.netcode.mcbe.Restrict
import com.valaphee.netcode.mcbe.Restriction
import com.valaphee.netcode.mcbe.world.entity.player.User
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.PacketReader

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
    val virtualRealityGazeDirection: Float3? = null,
    val tick: Long,
    val positionDelta: Float3
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

    override val id get() = 0x90

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeFloat2(rotation)
        buffer.writeFloat3(position)
        buffer.writeFloat2(move)
        buffer.writeFloatLE(headRotationYaw)
        buffer.writeVarULongFlags(input)
        buffer.writeVarUInt(inputMode.ordinal)
        buffer.writeVarUInt(playMode.ordinal)
        if (playMode == PlayMode.VirtualReality) buffer.writeFloat3(virtualRealityGazeDirection!!)
        if (version >= 419) {
            buffer.writeVarULong(tick)
            buffer.writeFloat3(positionDelta)
        }
        /*if (version >= 428) {
            if (input.equals(Input.PerformItemInteraction)) {
            }
            if (input.equals(Input.PerformInventoryRequest)) {
            }
            if (input.equals(Input.PerformBlockActions)) {
            }
        }*/
    }

    override fun handle(handler: PacketHandler) = handler.input(this)

    override fun toString() = "InputPacket(rotation=$rotation, position=$position, move=$move, headRotationYaw=$headRotationYaw, input=$input, inputMode=$inputMode, playMode=$playMode, virtualRealityGazeDirection=$virtualRealityGazeDirection, tick=$tick, positionDelta=$positionDelta)"
}

/**
 * @author Kevin Ludwig
 */
object InputPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): InputPacket {
        val rotation = buffer.readFloat2()
        val position = buffer.readFloat3()
        val move = buffer.readFloat2()
        val headRotationYaw = buffer.readFloatLE()
        val input = buffer.readVarULongFlags<InputPacket.Input>()
        val inputMode = User.InputMode.values()[buffer.readVarUInt()]
        val playMode = InputPacket.PlayMode.values()[buffer.readVarUInt()]
        val virtualRealityGazeDirection = if (playMode == InputPacket.PlayMode.VirtualReality) buffer.readFloat3() else null
        val tick: Long
        val positionDelta: Float3
        if (version >= 419) {
            tick = buffer.readVarULong()
            positionDelta = buffer.readFloat3()
        } else {
            tick = 0L
            positionDelta = Float3.Zero
        }
        /*if (version >= 428) {
            if (input.equals(InputPacket.Input.PerformItemInteraction)) {
            }
            if (input.equals(InputPacket.Input.PerformInventoryRequest)) {
            }
            if (input.equals(InputPacket.Input.PerformBlockActions)) {
            }
        }*/
        return InputPacket(rotation, position, move, headRotationYaw, input, inputMode, playMode, virtualRealityGazeDirection, tick, positionDelta)
    }
}
