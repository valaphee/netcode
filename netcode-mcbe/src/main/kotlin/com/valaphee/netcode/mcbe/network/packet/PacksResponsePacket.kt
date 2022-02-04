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

import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.PacketReader
import com.valaphee.netcode.mcbe.network.Restrict
import com.valaphee.netcode.mcbe.network.Restriction
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
