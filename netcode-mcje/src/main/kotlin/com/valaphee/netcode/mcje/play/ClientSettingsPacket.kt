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

package com.valaphee.netcode.mcje.play

import com.valaphee.netcode.mcje.Packet
import com.valaphee.netcode.mcje.PacketBuffer
import com.valaphee.netcode.mcje.PacketReader
import com.valaphee.netcode.mcje.entity.player.MainHand
import com.valaphee.netcode.mcje.entity.player.SkinPart
import java.util.Locale

/**
 * @author Kevin Ludwig
 */
class ClientSettingsPacket(
    val locale: Locale,
    val viewDistance: Int,
    val chatMode: ChatMode,
    val chatColors: Boolean,
    val skinParts: Set<SkinPart>,
    val mainHand: MainHand,
    val disableTextFiltering: Boolean
) : Packet<ClientPlayPacketHandler> {
    enum class ChatMode {
        Enabled, CommandsOnly, Hidden
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeString(locale.toLanguageTag().replace('-', '_'))
        buffer.writeByte(viewDistance)
        buffer.writeVarInt(chatMode.ordinal)
        buffer.writeBoolean(chatColors)
        buffer.writeByteFlags(skinParts)
        if (version >= 498) buffer.writeVarInt(mainHand.ordinal)
    }

    override fun handle(handler: ClientPlayPacketHandler) = handler.settings(this)

    override fun toString() = "ClientSettingsPacket(locale=$locale, viewDistance=$viewDistance, chatMode=$chatMode, chatColors=$chatColors, skinParts=$skinParts, mainHand=$mainHand, disableTextFiltering=$disableTextFiltering)"
}

/**
 * @author Kevin Ludwig
 */
object ClientSettingsPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ClientSettingsPacket(Locale.forLanguageTag(buffer.readString(16).replace('_', '-')), buffer.readUnsignedByte().toInt(), ClientSettingsPacket.ChatMode.values()[buffer.readVarInt()], buffer.readBoolean(), buffer.readByteFlags(), if (version >= 498) MainHand.values()[buffer.readVarInt()] else MainHand.Right, true)
}
