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

package com.valaphee.netcode.mcbe.world.scoreboard

import com.valaphee.netcode.mcbe.Packet
import com.valaphee.netcode.mcbe.PacketBuffer
import com.valaphee.netcode.mcbe.PacketHandler
import com.valaphee.netcode.mcbe.PacketReader
import com.valaphee.netcode.mcbe.Restrict
import com.valaphee.netcode.mcbe.Restriction

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class ObjectiveSetPacket(
    val displaySlot: String,
    val name: String,
    val displayName: String,
    val criteria: String,
    val sortOrder: SortOrder
) : Packet() {
    enum class SortOrder {
        Ascending, Descending
    }

    override val id get() = 0x6B

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeString(displaySlot)
        buffer.writeString(name)
        buffer.writeString(displayName)
        buffer.writeString(criteria)
        buffer.writeVarInt(sortOrder.ordinal)
    }

    override fun handle(handler: PacketHandler) = handler.objectiveSet(this)

    override fun toString() = "ObjectiveSetPacket(displaySlot='$displaySlot', name='$name', displayName='$displayName', criteria='$criteria', sortOrder=$sortOrder)"
}

/**
 * @author Kevin Ludwig
 */
object ObjectiveSetPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ObjectiveSetPacket(buffer.readString(), buffer.readString(), buffer.readString(), buffer.readString(), ObjectiveSetPacket.SortOrder.values()[buffer.readVarInt()])
}
