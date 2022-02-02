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

import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.PacketReader

/**
 * @author Kevin Ludwig
 */
class PlayerActionPacket(
    val runtimeEntityId: Long,
    val action: Action,
    val blockPosition: Int3,
    val data: Int
) : Packet() {
    enum class Action {
        StartBreak,
        AbortBreak,
        StopBreak,
        GetUpdatedBlock,
        DropItem,
        StartSleep,
        StopSleep,
        Respawn,
        Jump,
        StartSprinting,
        StopSprinting,
        StartSneak,
        StopSneak,
        DimensionChangeRequestOrCreativeBlockDestroy,
        DimensionChangeSuccess,
        StartGlide,
        StopGlide,
        BuildDenied,
        ContinueBreak,
        ChangeSkin,
        SetEnchantmentSeed,
        StartSwimming,
        StopSwimming,
        StartSpinAttack,
        StopSpinAttack,
        BlockInteract,
        BlockPredictDestroy,
        BlockContinueDestroy
    }

    override val id get() = 0x24

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarULong(runtimeEntityId)
        buffer.writeVarInt(action.ordinal)
        buffer.writeInt3UnsignedY(blockPosition)
        buffer.writeVarUInt(data)
    }

    override fun handle(handler: PacketHandler) = handler.playerAction(this)

    override fun toString() = "PlayerActionPacket(runtimeEntityId=$runtimeEntityId, action=$action, blockPosition=$blockPosition, data=$data)"
}

/**
 * @author Kevin Ludwig
 */
object PlayerActionPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = PlayerActionPacket(buffer.readVarULong(), PlayerActionPacket.Action.values()[buffer.readVarInt()], buffer.readInt3UnsignedY(), buffer.readVarInt())
}
