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

import com.valaphee.netcode.mc.util.nbt.Tag
import com.valaphee.netcode.mcbe.Packet
import com.valaphee.netcode.mcbe.PacketBuffer
import com.valaphee.netcode.mcbe.PacketHandler
import com.valaphee.netcode.mcbe.PacketReader

/**
 * @author Kevin Ludwig
 */
class EntityPropertiesPacket(
    val tag: Tag?
) : Packet() {
    override val id get() = 0xA5

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.toNbtOutputStream().use { it.writeTag(tag) }
    }

    override fun handle(handler: PacketHandler) = handler.entityProperties(this)

    override fun toString() = "EntityPropertiesPacket(tag=$tag)"
}

/**
 * @author Kevin Ludwig
 */
object EntityPropertiesPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = EntityPropertiesPacket(buffer.toNbtInputStream().use { it.readTag() })
}
