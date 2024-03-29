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
import com.valaphee.netcode.mcbe.network.Restrict
import com.valaphee.netcode.mcbe.network.Restriction

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToServer)
class CommandBlockUpdatePacket(
    val block: Boolean,
    val blockPosition: Int3?,
    val runtimeEntityId: Long,
    val name: String,
    val command: String,
    val lastOutput: String,
    val mode: Mode?,
    val powered: Boolean,
    val conditionMet: Boolean,
    val outputTracked: Boolean,
    val executeOnFirstTick: Boolean,
    val tickDelay: Long = 0
) : Packet() {
    enum class Mode {
        Impulse, Repeat, Chain
    }

    override val id get() = 0x4E

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeBoolean(block)
        if (block) {
            buffer.writeBlockPosition(blockPosition!!)
            buffer.writeVarUInt(mode!!.ordinal)
            buffer.writeBoolean(powered)
            buffer.writeBoolean(conditionMet)
        } else buffer.writeVarULong(runtimeEntityId)
        buffer.writeString(command)
        buffer.writeString(lastOutput)
        buffer.writeString(name)
        buffer.writeBoolean(outputTracked)
        buffer.writeIntLE(tickDelay.toInt())
        buffer.writeBoolean(executeOnFirstTick)
    }

    override fun handle(handler: PacketHandler) = handler.commandBlockUpdate(this)

    override fun toString() = "CommandBlockUpdatePacket(block=$block, blockPosition=$blockPosition, runtimeEntityId=$runtimeEntityId, name='$name', command='$command', lastOutput='$lastOutput', mode=$mode, powered=$powered, conditionMet=$conditionMet, outputTracked=$outputTracked, executeOnFirstTick=$executeOnFirstTick, tickDelay=$tickDelay)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int): CommandBlockUpdatePacket {
            val block = buffer.readBoolean()
            val blockPosition: Int3?
            val runtimeEntityId: Long
            val mode: Mode?
            val powered: Boolean
            val conditionMet: Boolean
            if (block) {
                blockPosition = buffer.readBlockPosition()
                runtimeEntityId = 0L
                mode = Mode.values()[buffer.readVarUInt()]
                powered = buffer.readBoolean()
                conditionMet = buffer.readBoolean()
            } else {
                blockPosition = null
                runtimeEntityId = buffer.readVarULong()
                mode = null
                powered = false
                conditionMet = false
            }
            val command = buffer.readString()
            val lastOutput = buffer.readString()
            val name = buffer.readString()
            val outputTracked = buffer.readBoolean()
            val tickDelay = buffer.readUnsignedIntLE()
            val executeOnFirstTick = buffer.readBoolean()
            return CommandBlockUpdatePacket(block, blockPosition, runtimeEntityId, name, command, lastOutput, mode, powered, conditionMet, outputTracked, executeOnFirstTick, tickDelay)
        }
    }
}
