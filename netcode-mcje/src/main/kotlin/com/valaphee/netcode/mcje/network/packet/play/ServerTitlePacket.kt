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

import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.PacketReader
import com.valaphee.netcode.mcje.network.ServerPlayPacketHandler
import net.kyori.adventure.text.Component

/**
 * @author Kevin Ludwig
 */
class ServerTitlePacket(
    val action: Action,
    val text: Component?,
    val fadeInTime: Int,
    val stayTime: Int,
    val fadeOutTime: Int,
) : Packet<ServerPlayPacketHandler> {
    enum class Action {
        SetTitle, SetSubTitle, ActionBar, SetTimings, Clear, Reset
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(action.ordinal)
        when (action) {
            Action.SetTitle, Action.SetSubTitle, Action.ActionBar -> buffer.writeComponent(text!!)
            Action.SetTimings -> {
                buffer.writeInt(fadeInTime)
                buffer.writeInt(stayTime)
                buffer.writeInt(fadeOutTime)
            }
            Action.Clear, Action.Reset -> Unit
        }
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.title(this)

    override fun toString() = "ServerTitlePacket(action=$action, text=$text, fadeInTime=$fadeInTime, stayTime=$stayTime, fadeOutTime=$fadeOutTime)"
}

/**
 * @author Kevin Ludwig
 */
object ServerTitlePacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): ServerTitlePacket {
        val action = ServerTitlePacket.Action.values()[buffer.readVarInt()]
        val text: Component?
        val fadeInTime: Int
        val stayTime: Int
        val fadeOutTime: Int
        when (action) {
            ServerTitlePacket.Action.SetTitle, ServerTitlePacket.Action.SetSubTitle, ServerTitlePacket.Action.ActionBar -> {
                text = buffer.readComponent()
                fadeInTime = 0
                stayTime = 0
                fadeOutTime = 0
            }
            ServerTitlePacket.Action.SetTimings -> {
                text = null
                fadeInTime = buffer.readInt()
                stayTime = buffer.readInt()
                fadeOutTime = buffer.readInt()
            }
            ServerTitlePacket.Action.Clear, ServerTitlePacket.Action.Reset -> {
                text = null
                fadeInTime = 0
                stayTime = 0
                fadeOutTime = 0
            }
        }
        return ServerTitlePacket(action, text, fadeInTime, stayTime, fadeOutTime)
    }
}

/**
 * @author Kevin Ludwig
 */
object ServerTitleClearPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerTitlePacket(ServerTitlePacket.Action.Clear, null, 0, 0, 0)
}

/**
 * @author Kevin Ludwig
 */
object ServerTitleActionBarPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerTitlePacket(ServerTitlePacket.Action.ActionBar, buffer.readComponent(), 0, 0, 0)
}

/**
 * @author Kevin Ludwig
 */
object ServerTitleSetSubTitlePacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerTitlePacket(ServerTitlePacket.Action.SetSubTitle, buffer.readComponent(), 0, 0, 0)
}

/**
 * @author Kevin Ludwig
 */
object ServerTitleSetTitlePacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerTitlePacket(ServerTitlePacket.Action.SetTitle, buffer.readComponent(), 0, 0, 0)
}

/**
 * @author Kevin Ludwig
 */
object ServerTitleSetTimingsPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerTitlePacket(ServerTitlePacket.Action.SetTimings, null, buffer.readInt(), buffer.readInt(), buffer.readInt())
}
