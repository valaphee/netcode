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
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class PackDataPacket(
    val packId: UUID,
    val packVersion: String?,
    val maximumChunkSize: Long,
    val chunkCount: Long,
    val compressedPackSize: Long,
    val hash: ByteArray,
    val premium: Boolean,
    val type: Type
) : Packet() {
    enum class Type {
        Invalid,
        AddOn,
        Cached,
        CopyProtected,
        Behavior,
        PersonaPiece,
        Resource,
        Skins,
        WorldTemplate
    }

    override val id get() = 0x52

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeString("$packId${packVersion?.let { "_$packVersion" } ?: ""}")
        buffer.writeIntLE(maximumChunkSize.toInt())
        buffer.writeIntLE(chunkCount.toInt())
        buffer.writeLongLE(compressedPackSize)
        buffer.writeByteArray(hash)
        buffer.writeBoolean(premium)
        buffer.writeByte(type.ordinal)
    }

    override fun handle(handler: PacketHandler) = handler.packData(this)

    override fun toString() = "PackDataPacket(packId=$packId, packVersion=$packVersion, maximumChunkSize=$maximumChunkSize, chunkCount=$chunkCount, compressedPackSize=$compressedPackSize, hash=${hash.contentToString()}, premium=$premium, type=$type)"
}

/**
 * @author Kevin Ludwig
 */
object PackDataPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): PackDataPacket {
        val pack = buffer.readString().split("_".toRegex(), 2).toTypedArray()
        val packId = UUID.fromString(pack[0])
        val packVersion = if (pack.size == 2) pack[1] else null
        val maximumChunkSize = buffer.readUnsignedIntLE()
        val chunkCount = buffer.readUnsignedIntLE()
        val compressedPackSize = buffer.readLongLE()
        val hash = buffer.readByteArray()
        val premium = buffer.readBoolean()
        val type = PackDataPacket.Type.values()[buffer.readUnsignedByte().toInt()]
        return PackDataPacket(packId, packVersion, maximumChunkSize, chunkCount, compressedPackSize, hash, premium, type)
    }
}
