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

import com.fasterxml.jackson.module.kotlin.readValue
import com.valaphee.netcode.mcje.Packet
import com.valaphee.netcode.mcje.PacketBuffer
import com.valaphee.netcode.mcje.PacketReader
import com.valaphee.netcode.mcje.ServerPlayPacketHandler
import com.valaphee.netcode.mcje.util.text.Component
import com.valaphee.netcode.util.ByteBufStringReader
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
) : Packet<ServerPlayPacketHandler> {
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
        @Suppress("NON_EXHAUSTIVE_WHEN") when (action) {
            Action.Show -> {
                buffer.writeString(buffer.objectMapper.writeValueAsString(title))
                buffer.writeFloat(percentage)
                buffer.writeVarInt(color!!.ordinal)
                buffer.writeVarInt(overlay!!.ordinal)
                buffer.writeByte(if (darkenSky) flagDarkenSky else 0 or if (playEndMusic) flagPlayEndMusic else 0 or if (showFog) flagShowFog else 0)
            }
            Action.SetPercentage -> buffer.writeFloat(percentage)
            Action.SetTitle -> buffer.writeString(buffer.objectMapper.writeValueAsString(title))
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
                title = buffer.objectMapper.readValue(ByteBufStringReader(buffer, buffer.readVarInt()))
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
                title = buffer.objectMapper.readValue(ByteBufStringReader(buffer, buffer.readVarInt()))
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
