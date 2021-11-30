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

package com.valaphee.netcode.mcbe.pack

import com.valaphee.netcode.mcbe.Packet
import com.valaphee.netcode.mcbe.PacketBuffer
import com.valaphee.netcode.mcbe.PacketHandler
import com.valaphee.netcode.mcbe.PacketReader
import com.valaphee.netcode.mcbe.Restrict
import com.valaphee.netcode.mcbe.Restriction
import com.valaphee.netcode.util.safeList
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToServer)
class PacksResponsePacket(
    val status: Status,
    val packs: List<Pair<UUID, String?>>
) : Packet() {
    enum class Status {
        None, Refused, TransferPacks, HaveAllPacks, Completed
    }

    override val id get() = 0x08

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeByte(status.ordinal)
        buffer.writeShortLE(packs.size)
        packs.forEach { buffer.writeString("${it.first}${it.second?.let { "_$it" } ?: ""}") }
    }

    override fun handle(handler: PacketHandler) = handler.packsResponse(this)

    override fun toString() = "PacksResponsePacket(status=$status, packs=$packs)"
}

/**
 * @author Kevin Ludwig
 */
object PacksResponsePacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = PacksResponsePacket(
        PacksResponsePacket.Status.values()[buffer.readUnsignedByte().toInt()],
        safeList(buffer.readUnsignedShortLE()) {
            val pack = buffer.readString().split("_".toRegex(), 2).toTypedArray()
            UUID.fromString(pack[0]) to if (pack.size == 2) pack[1] else null
        }
    )
}
