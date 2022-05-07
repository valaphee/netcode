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

package com.valaphee.netcode.mcje.network.packet.play

import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mcje.network.ClientPlayPacketHandler
import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.PacketReader

/**
 * @author Kevin Ludwig
 */
class ClientCommandBlockUpdatePacket(
    val position: Int3,
    val command: String,
    val mode: Mode,
    val outputTracked: Boolean,
    val conditionMet: Boolean,
    val executeOnFirstTick: Boolean
) : Packet<ClientPlayPacketHandler>() {
    enum class Mode {
        Chain, Repeat, Impulse
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeInt3UnsignedY(position)
        buffer.writeString(command)
        buffer.writeVarInt(mode.ordinal)
        var flagsValue = if (outputTracked) flagOutputTracked else 0
        flagsValue = flagsValue or if (conditionMet) flagConditionMet else 0
        flagsValue = flagsValue or if (executeOnFirstTick) flagExecuteOnFirstTick else 0
        buffer.writeByte(flagsValue)
    }

    override fun handle(handler: ClientPlayPacketHandler) = handler.commandBlockUpdate(this)

    override fun toString() = "ClientCommandBlockUpdatePacket(position=$position, command='$command', mode=$mode, outputTracked=$outputTracked, conditionMet=$conditionMet, executeOnFirstTick=$executeOnFirstTick)"

    companion object {
        internal const val flagOutputTracked = 1
        internal const val flagConditionMet = 1 shl 1
        internal const val flagExecuteOnFirstTick = 1 shl 2
    }
}

/**
 * @author Kevin Ludwig
 */
object ClientCommandBlockUpdatePacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): ClientCommandBlockUpdatePacket {
        val position = buffer.readInt3UnsignedY()
        val command = buffer.readString()
        val mode = ClientCommandBlockUpdatePacket.Mode.values()[buffer.readVarInt()]
        val flags = buffer.readByte().toInt()
        return ClientCommandBlockUpdatePacket(position, command, mode, flags and ClientCommandBlockUpdatePacket.flagOutputTracked != 0, flags and ClientCommandBlockUpdatePacket.flagConditionMet != 0, flags and ClientCommandBlockUpdatePacket.flagExecuteOnFirstTick != 0)
    }
}
