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
import com.valaphee.netcode.mcbe.util.safeList

/**
 * @author Kevin Ludwig
 */
class TextPacket(
    val type: Type,
    val needsTranslation: Boolean,
    val sourceName: String?,
    val message: String,
    val arguments: List<String>?,
    val xboxUserId: String,
    val platformChatId: String
) : Packet() {
    enum class Type {
        Raw, Chat, Translation, PopUp, JukeboxPopUp, Tip, System, Whisper, Announcement, Object, ObjectWhisper
    }

    override val id get() = 0x09

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeByte(type.ordinal)
        buffer.writeBoolean(needsTranslation)
        when (type) {
            Type.Chat, Type.Whisper, Type.Announcement -> {
                buffer.writeString(sourceName!!)
                buffer.writeString(message)
            }
            Type.Raw, Type.Tip, Type.System, Type.Object, Type.ObjectWhisper -> buffer.writeString(message)
            Type.Translation, Type.PopUp, Type.JukeboxPopUp -> {
                buffer.writeString(message)
                arguments!!.let {
                    buffer.writeVarUInt(it.size)
                    it.forEach { buffer.writeString(it) }
                }
            }
        }
        buffer.writeString(xboxUserId)
        buffer.writeString(platformChatId)
    }

    override fun handle(handler: PacketHandler) = handler.text(this)

    override fun toString() = "TextPacket(type=$type, needsTranslation=$needsTranslation, sourceName=$sourceName, message='$message', arguments=$arguments, xboxUserId='$xboxUserId', platformChatId='$platformChatId')"
}

/**
 * @author Kevin Ludwig
 */
object TextPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): TextPacket {
        val type = TextPacket.Type.values()[buffer.readUnsignedByte().toInt()]
        val needsTranslation = buffer.readBoolean()
        var sourceName: String? = null
        val message: String
        var arguments: List<String>? = null
        when (type) {
            TextPacket.Type.Chat, TextPacket.Type.Whisper, TextPacket.Type.Announcement -> {
                sourceName = buffer.readString()
                message = buffer.readString()
            }
            TextPacket.Type.Raw, TextPacket.Type.Tip, TextPacket.Type.System, TextPacket.Type.Object, TextPacket.Type.ObjectWhisper -> message = buffer.readString()
            TextPacket.Type.Translation, TextPacket.Type.PopUp, TextPacket.Type.JukeboxPopUp -> {
                message = buffer.readString()
                arguments = safeList(buffer.readVarUInt()) { buffer.readString() }
            }
        }
        val xboxUserId = buffer.readString()
        val platformChatId = buffer.readString()
        return TextPacket(type, needsTranslation, sourceName, message, arguments, xboxUserId, platformChatId)
    }
}
