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

package com.valaphee.netcode.mcje.network.packet.play

import com.valaphee.netcode.mcje.network.ClientPlayPacketHandler
import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.V1_18_2
import com.valaphee.netcode.mcje.world.entity.player.MainHand
import com.valaphee.netcode.mcje.world.entity.player.SkinPart
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
    val textFilter: Boolean,
    val visibleInStatus: Boolean
) : Packet<ClientPlayPacketHandler>() {
    enum class ChatMode {
        Enabled, CommandsOnly, Hidden
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeString(locale.toLanguageTag().replace('-', '_'))
        buffer.writeByte(viewDistance)
        buffer.writeVarInt(chatMode.ordinal)
        buffer.writeBoolean(chatColors)
        buffer.writeByteFlags(skinParts)
        buffer.writeVarInt(mainHand.ordinal)
        if (version >= V1_18_2) {
            buffer.writeBoolean(textFilter)
            buffer.writeBoolean(visibleInStatus)
        }
    }

    override fun handle(handler: ClientPlayPacketHandler) = handler.settings(this)

    override fun toString() = "ClientSettingsPacket(locale=$locale, viewDistance=$viewDistance, chatMode=$chatMode, chatColors=$chatColors, skinParts=$skinParts, mainHand=$mainHand, filterText=$textFilter, visibleInStatus=$visibleInStatus)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int): ClientSettingsPacket {
            val locale = Locale.forLanguageTag(buffer.readString(16).replace('_', '-'))
            val viewDistance = buffer.readUnsignedByte().toInt()
            val chatMode = ClientSettingsPacket.ChatMode.values()[buffer.readVarInt()]
            val chatColors = buffer.readBoolean()
            val skinParts = buffer.readByteFlags<SkinPart>()
            val mainHand = MainHand.values()[buffer.readVarInt()]
            val textFilter: Boolean
            val present: Boolean
            if (version >= V1_18_2) {
                textFilter = buffer.readBoolean()
                present = buffer.readBoolean()
            } else {
                textFilter = false
                present = false
            }
            return ClientSettingsPacket(locale, viewDistance, chatMode, chatColors, skinParts, mainHand, textFilter, present)
        }
    }
}
