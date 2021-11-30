/*
 * MIT License
 *
 * Copyright (c) 2021, Valaphee.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.valaphee.netcode.mcje.play

import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mcje.ClientPlayPacketHandler
import com.valaphee.netcode.mcje.Packet
import com.valaphee.netcode.mcje.PacketBuffer
import com.valaphee.netcode.mcje.PacketReader

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
) : Packet<ClientPlayPacketHandler> {
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
