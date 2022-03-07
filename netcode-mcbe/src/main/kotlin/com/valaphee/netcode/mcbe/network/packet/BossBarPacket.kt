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

/**
 * @author Kevin Ludwig
 */
class BossBarPacket(
    val uniqueEntityId: Long,
    val action: Action,
    val title: String?,
    val playerUniqueEntityId: Long,
    val percentage: Float,
    val darkenSky: Int,
    val color: Int,
    val overlay: Int
) : Packet() {
    enum class Action {
        Show,
        RegisterPlayer,
        Hide,
        UnregisterPlayer,
        SetPercentage,
        SetTitle,
        SetDarkenSky,
        SetStyle,
        Query
    }

    override val id get() = 0x4A

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarLong(uniqueEntityId)
        buffer.writeVarUInt(action.ordinal)
        when (action) {
            Action.Show -> {
                buffer.writeString(title!!)
                buffer.writeFloatLE(percentage)
                buffer.writeShortLE(darkenSky)
                buffer.writeVarUInt(color)
                buffer.writeVarUInt(overlay)
            }
            Action.RegisterPlayer, Action.UnregisterPlayer -> buffer.writeVarLong(playerUniqueEntityId)
            Action.Hide -> Unit
            Action.SetPercentage -> buffer.writeFloatLE(percentage)
            Action.SetTitle -> buffer.writeString(title!!)
            Action.SetDarkenSky -> {
                buffer.writeShortLE(darkenSky)
                buffer.writeVarUInt(color)
                buffer.writeVarUInt(overlay)
            }
            Action.SetStyle -> {
                buffer.writeVarUInt(color)
                buffer.writeVarUInt(overlay)
            }
            Action.Query -> Unit
        }
    }

    override fun handle(handler: PacketHandler) = handler.bossBar(this)

    override fun toString() = "BossBarPacket(uniqueEntityId=$uniqueEntityId, action=$action, title=$title, playerUniqueEntityId=$playerUniqueEntityId, percentage=$percentage, darkenSky=$darkenSky, color=$color, overlay=$overlay)"
}

/**
 * @author Kevin Ludwig
 */
object BossBarPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): BossBarPacket {
        val uniqueEntityId = buffer.readVarLong()
        val action = BossBarPacket.Action.values()[buffer.readVarUInt()]
        val title: String?
        val percentage: Float
        val darkenSky: Int
        val color: Int
        val overlay: Int
        val playerUniqueEntityId: Long
        when (action) {
            BossBarPacket.Action.Show -> {
                title = buffer.readString()
                percentage = buffer.readFloatLE()
                darkenSky = buffer.readUnsignedShortLE()
                color = buffer.readVarUInt()
                overlay = buffer.readVarUInt()
                playerUniqueEntityId = 0
            }
            BossBarPacket.Action.Hide, BossBarPacket.Action.Query -> {
                title = null
                percentage = 0.0f
                darkenSky = 0
                color = 0
                overlay = 0
                playerUniqueEntityId = 0
            }
            BossBarPacket.Action.RegisterPlayer, BossBarPacket.Action.UnregisterPlayer -> {
                title = null
                percentage = 0.0f
                darkenSky = 0
                color = 0
                overlay = 0
                playerUniqueEntityId = buffer.readVarLong()
            }
            BossBarPacket.Action.SetPercentage -> {
                title = null
                percentage = buffer.readFloatLE()
                darkenSky = 0
                color = 0
                overlay = 0
                playerUniqueEntityId = buffer.readVarLong()
            }
            BossBarPacket.Action.SetTitle -> {
                title = buffer.readString()
                percentage = 0.0f
                darkenSky = 0
                color = 0
                overlay = 0
                playerUniqueEntityId = buffer.readVarLong()
            }
            BossBarPacket.Action.SetDarkenSky -> {
                title = null
                percentage = 0.0f
                darkenSky = buffer.readUnsignedShortLE()
                color = buffer.readVarUInt()
                overlay = buffer.readVarUInt()
                playerUniqueEntityId = 0
            }
            BossBarPacket.Action.SetStyle -> {
                title = null
                percentage = 0.0f
                darkenSky = 0
                color = buffer.readVarUInt()
                overlay = buffer.readVarUInt()
                playerUniqueEntityId = 0
            }
        }
        return BossBarPacket(uniqueEntityId, action, title, playerUniqueEntityId, percentage, darkenSky, color, overlay)
    }
}
