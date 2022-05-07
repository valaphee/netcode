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
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
class ServerBossBarPacket(
    val entityUid: UUID,
    val action: Action,
    val title: Component?,
    val percentage: Float,
    val color: Color?,
    val overlay: Overlay?,
    val darkenSky: Boolean,
    val playEndMusic: Boolean,
    val showFog: Boolean
) : Packet<ServerPlayPacketHandler>() {
    enum class Action {
        Show, Hide, SetPercentage, SetTitle, SetStyle, SetFlags
    }

    enum class Color {
        Pink, Blue, Red, Green, Yellow, Purple, White
    }

    enum class Overlay {
        None, Notches6, Notches10, Notches12, Notches20
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeUuid(entityUid)
        buffer.writeVarInt(action.ordinal)
        when (action) {
            Action.Show -> {
                buffer.writeComponent(title!!)
                buffer.writeFloat(percentage)
                buffer.writeVarInt(color!!.ordinal)
                buffer.writeVarInt(overlay!!.ordinal)
                buffer.writeByte(if (darkenSky) flagDarkenSky else 0 or if (playEndMusic) flagPlayEndMusic else 0 or if (showFog) flagShowFog else 0)
            }
            Action.Hide -> Unit
            Action.SetPercentage -> buffer.writeFloat(percentage)
            Action.SetTitle -> buffer.writeComponent(title!!)
            Action.SetStyle -> {
                buffer.writeVarInt(color!!.ordinal)
                buffer.writeVarInt(overlay!!.ordinal)
            }
            Action.SetFlags -> buffer.writeByte(if (darkenSky) flagDarkenSky else 0 or if (playEndMusic) flagPlayEndMusic else 0 or if (showFog) flagShowFog else 0)
        }
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.bossBar(this)

    override fun toString() = "ServerBossBarPacket(entityUid=$entityUid, action=$action, title=$title, percentage=$percentage, color=$color, overlay=$overlay, darkenSky=$darkenSky, playEndMusic=$playEndMusic, showFog=$showFog)"

    companion object {
        internal const val flagDarkenSky = 1
        internal const val flagPlayEndMusic = 1 shl 1
        internal const val flagShowFog = 1 shl 2
    }
}

/**
 * @author Kevin Ludwig
 */
object ServerBossBarPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): ServerBossBarPacket {
        val entityUid = buffer.readUuid()
        val action = ServerBossBarPacket.Action.values()[buffer.readVarInt()]
        val title: Component?
        val percentage: Float
        val color: ServerBossBarPacket.Color?
        val overlay: ServerBossBarPacket.Overlay?
        val darkenSky: Boolean
        val playEndMusic: Boolean
        val showFog: Boolean
        when (action) {
            ServerBossBarPacket.Action.Show -> {
                title = buffer.readComponent()
                percentage = buffer.readFloat()
                color = ServerBossBarPacket.Color.values()[buffer.readVarInt()]
                overlay = ServerBossBarPacket.Overlay.values()[buffer.readVarInt()]
                val flagsValue = buffer.readByte().toInt()
                darkenSky = flagsValue and ServerBossBarPacket.flagDarkenSky != 0
                playEndMusic = flagsValue and ServerBossBarPacket.flagPlayEndMusic != 0
                showFog = flagsValue and ServerBossBarPacket.flagShowFog != 0
            }
            ServerBossBarPacket.Action.SetPercentage -> {
                title = null
                percentage = buffer.readFloat()
                color = null
                overlay = null
                darkenSky = false
                playEndMusic = false
                showFog = false
            }
            ServerBossBarPacket.Action.SetTitle -> {
                title = buffer.readComponent()
                percentage = 0.0f
                color = null
                overlay = null
                darkenSky = false
                playEndMusic = false
                showFog = false
            }
            ServerBossBarPacket.Action.SetStyle -> {
                title = null
                percentage = 0.0f
                color = ServerBossBarPacket.Color.values()[buffer.readVarInt()]
                overlay = ServerBossBarPacket.Overlay.values()[buffer.readVarInt()]
                darkenSky = false
                playEndMusic = false
                showFog = false
            }
            ServerBossBarPacket.Action.SetFlags -> {
                title = null
                percentage = 0.0f
                color = null
                overlay = null
                val flagsValue = buffer.readByte().toInt()
                darkenSky = flagsValue and ServerBossBarPacket.flagDarkenSky != 0
                playEndMusic = flagsValue and ServerBossBarPacket.flagPlayEndMusic != 0
                showFog = flagsValue and ServerBossBarPacket.flagShowFog != 0
            }
            else -> {
                title = null
                percentage = 0.0f
                color = null
                overlay = null
                darkenSky = false
                playEndMusic = false
                showFog = false
            }
        }
        return ServerBossBarPacket(entityUid, action, title, percentage, color, overlay, darkenSky, playEndMusic, showFog)
    }
}
