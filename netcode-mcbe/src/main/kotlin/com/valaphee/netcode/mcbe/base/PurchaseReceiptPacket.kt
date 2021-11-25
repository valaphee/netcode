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

import com.valaphee.netcode.mcbe.Packet
import com.valaphee.netcode.mcbe.PacketBuffer
import com.valaphee.netcode.mcbe.PacketHandler
import com.valaphee.netcode.mcbe.PacketReader
import com.valaphee.netcode.mcbe.util.safeList

/**
 * @author Kevin Ludwig
 */
class PurchaseReceiptPacket(
    val offerIds: List<String>
) : Packet() {
    override val id get() = 0x5C

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarUInt(offerIds.size)
        offerIds.forEach { buffer.writeString(it) }
    }

    override fun handle(handler: PacketHandler) = handler.purchaseReceipt(this)

    override fun toString() = "PurchaseReceiptPacket(offerIds=$offerIds)"
}

/**
 * @author Kevin Ludwig
 */
object PurchaseReceiptPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = PurchaseReceiptPacket(safeList(buffer.readVarUInt()) { buffer.readString() })
}
