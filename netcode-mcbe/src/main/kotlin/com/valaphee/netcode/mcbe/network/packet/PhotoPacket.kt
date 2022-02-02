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
import com.valaphee.netcode.mcbe.Restrict
import com.valaphee.netcode.mcbe.Restriction

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class PhotoPacket(
    val name: String,
    val data: ByteArray,
    val bookId: String,
    val type: Type?,
    val sourceType: Type?,
    val ownerId: Long,
    val newName: String?
) : Packet() {
    enum class Type {
        Portfolio, PhotoItem, Book
    }

    override val id get() = 0x63

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeString(name)
        buffer.writeByteArray(data)
        buffer.writeString(bookId)
        if (version >= 465) {
            buffer.writeByte(type!!.ordinal)
            buffer.writeByte(sourceType!!.ordinal)
            buffer.writeLongLE(ownerId)
            buffer.writeString(newName!!)
        }
    }

    override fun handle(handler: PacketHandler) = handler.photo(this)

    override fun toString() = "PhotoPacket(name='$name', data=${data.contentToString()}, bookId='$bookId', type=$type, sourceType=$sourceType, ownerId=$ownerId, newName=$newName)"
}

/**
 * @author Kevin Ludwig
 */
object PhotoPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): PhotoPacket {
        val name = buffer.readString()
        val data = buffer.readByteArray()
        val bookId = buffer.readString()
        val type: PhotoPacket.Type?
        val sourceType: PhotoPacket.Type?
        val ownerId: Long
        val newName: String?
        if (version >= 465) {
            type = PhotoPacket.Type.values()[buffer.readByte().toInt()]
            sourceType = PhotoPacket.Type.values()[buffer.readByte().toInt()]
            ownerId = buffer.readLongLE()
            newName = buffer.readString()
        } else {
            type = null
            sourceType = null
            ownerId = 0
            newName = null
        }
        return PhotoPacket(name, data, bookId, type, sourceType, ownerId, newName)
    }
}
