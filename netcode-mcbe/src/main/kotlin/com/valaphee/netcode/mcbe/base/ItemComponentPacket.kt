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

package com.valaphee.netcode.mcbe.base

import com.valaphee.netcode.mc.util.nbt.CompoundTag
import com.valaphee.netcode.mcbe.Packet
import com.valaphee.netcode.mcbe.PacketBuffer
import com.valaphee.netcode.mcbe.PacketHandler
import com.valaphee.netcode.mcbe.PacketReader
import com.valaphee.netcode.mcbe.Restrict
import com.valaphee.netcode.mcbe.Restriction
import com.valaphee.netcode.mcbe.util.safeList

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class ItemComponentPacket(
    val entries: List<Entry>
) : Packet() {
    class Entry(
        val name: String,
        val tag: CompoundTag?
    )

    override val id get() = 0xA2

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarUInt(entries.size)
        entries.forEach {
            buffer.writeString(it.name)
            buffer.toNbtOutputStream().use { stream -> stream.writeTag(it.tag) }
        }
    }

    override fun handle(handler: PacketHandler) = handler.itemComponent(this)

    override fun toString() = "ItemComponentPacket(entries=$entries)"
}

/**
 * @author Kevin Ludwig
 */
object ItemComponentPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ItemComponentPacket(safeList(buffer.readVarUInt()) { ItemComponentPacket.Entry(buffer.readString(), buffer.toNbtInputStream().use { it.readTag()?.asCompoundTag() }) })
}
