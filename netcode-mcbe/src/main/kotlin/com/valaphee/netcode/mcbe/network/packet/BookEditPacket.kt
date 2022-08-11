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
import com.valaphee.netcode.mcbe.network.Restrict
import com.valaphee.netcode.mcbe.network.Restriction

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToServer)
class BookEditPacket(
    val action: Action,
    val slotId: Int,
    val pageNumber: Int,
    val text: String?,
    val otherPageNumber: Int,
    val title: String?,
    val photoName: String?,
    val author: String?,
    val xboxUserId: String?
) : Packet() {
    enum class Action {
        ReplacePage, AddPage, DeletePage, SwapPages, SignBook
    }

    override val id get() = 0x61

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeByte(action.ordinal)
        buffer.writeByte(slotId)
        when (action) {
            Action.ReplacePage, Action.AddPage -> {
                buffer.writeByte(pageNumber)
                buffer.writeString(text!!)
                buffer.writeString(photoName!!)
            }
            Action.DeletePage -> buffer.writeByte(pageNumber)
            Action.SwapPages -> {
                buffer.writeByte(pageNumber)
                buffer.writeByte(otherPageNumber)
            }
            Action.SignBook -> {
                buffer.writeString(title!!)
                buffer.writeString(author!!)
                buffer.writeString(xboxUserId!!)
            }
        }
    }

    override fun handle(handler: PacketHandler) = handler.bookEdit(this)

    override fun toString() = "BookEditPacket(action=$action, slotId=$slotId, pageNumber=$pageNumber, text=$text, otherPageNumber=$otherPageNumber, title=$title, photoName=$photoName, author=$author, xboxUserId=$xboxUserId)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int): BookEditPacket {
            val action = BookEditPacket.Action.values()[buffer.readUnsignedByte().toInt()]
            val slotId = buffer.readUnsignedByte().toInt()
            val pageNumber: Int
            val text: String?
            val otherPageNumber: Int
            val title: String?
            val photoName: String?
            val author: String?
            val xboxUserId: String?
            when (action) {
                Action.ReplacePage, Action.AddPage -> {
                    pageNumber = buffer.readUnsignedByte().toInt()
                    text = buffer.readString()
                    otherPageNumber = 0
                    title = null
                    photoName = buffer.readString()
                    author = null
                    xboxUserId = null
                }
                Action.DeletePage -> {
                    pageNumber = buffer.readUnsignedByte().toInt()
                    text = null
                    otherPageNumber = 0
                    title = null
                    photoName = null
                    author = null
                    xboxUserId = null
                }
                Action.SwapPages -> {
                    pageNumber = buffer.readUnsignedByte().toInt()
                    text = null
                    otherPageNumber = buffer.readUnsignedByte().toInt()
                    title = null
                    photoName = null
                    author = null
                    xboxUserId = null
                }
                Action.SignBook -> {
                    pageNumber = 0
                    text = null
                    otherPageNumber = 0
                    title = buffer.readString()
                    photoName = null
                    author = buffer.readString()
                    xboxUserId = buffer.readString()
                }
            }
            return BookEditPacket(action, slotId, pageNumber, text, otherPageNumber, title, photoName, author, xboxUserId)
        }
    }
}
