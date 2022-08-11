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

/**
 * @author Kevin Ludwig
 */
class BossBarPacket(
    val uniqueEntityId: Long,
    val action: Action,
    val title: String?,
    val uniquePlayerId: Long,
    val percentage: Float,
    val flags: Int,
    val color: Color?,
    val overlay: Overlay?
) : Packet() {
    enum class Action {
        Show, RegisterPlayer, Hide, UnregisterPlayer, SetPercentage, SetTitle, SetFlags, SetStyle, Query
    }

    enum class Color {
        Pink, Blue, Red, Green, Yellow, Purple, White
    }

    enum class Overlay {
        None, Notches6, Notches10, Notches12, Notches20
    }

    override val id get() = 0x4A

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarLong(uniqueEntityId)
        buffer.writeVarUInt(action.ordinal)
        when (action) {
            Action.Show -> {
                buffer.writeString(title!!)
                buffer.writeFloatLE(percentage)
                buffer.writeShortLE(flags)
                buffer.writeVarUInt(color!!.ordinal)
                buffer.writeVarUInt(overlay!!.ordinal)
            }
            Action.RegisterPlayer, Action.UnregisterPlayer, Action.Query -> buffer.writeVarLong(uniquePlayerId)
            Action.Hide -> Unit
            Action.SetPercentage -> buffer.writeFloatLE(percentage)
            Action.SetTitle -> buffer.writeString(title!!)
            Action.SetFlags -> {
                buffer.writeShortLE(flags)
                buffer.writeVarUInt(color!!.ordinal)
                buffer.writeVarUInt(overlay!!.ordinal)
            }
            Action.SetStyle -> {
                buffer.writeVarUInt(color!!.ordinal)
                buffer.writeVarUInt(overlay!!.ordinal)
            }
        }
    }

    override fun handle(handler: PacketHandler) = handler.bossBar(this)

    override fun toString() = "BossBarPacket(uniqueEntityId=$uniqueEntityId, action=$action, title=$title, uniquePlayerId=$uniquePlayerId, percentage=$percentage, flags=$flags, color=$color, overlay=$overlay)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int): BossBarPacket {
            val uniqueBossId = buffer.readVarLong()
            val action = Action.values()[buffer.readVarUInt()]
            val title: String?
            val percentage: Float
            val flags: Int
            val color: Color?
            val overlay: Overlay?
            val uniquePlayerId: Long
            when (action) {
                Action.Show -> {
                    title = buffer.readString()
                    percentage = buffer.readFloatLE()
                    flags = buffer.readUnsignedShortLE()
                    color = Color.values()[buffer.readVarUInt()]
                    overlay = Overlay.values()[buffer.readVarUInt()]
                    uniquePlayerId = 0
                }
                Action.Hide -> {
                    title = null
                    percentage = 0.0f
                    flags = 0
                    color = null
                    overlay = null
                    uniquePlayerId = 0
                }
                Action.RegisterPlayer, Action.UnregisterPlayer, Action.Query -> {
                    title = null
                    percentage = 0.0f
                    flags = 0
                    color = null
                    overlay = null
                    uniquePlayerId = buffer.readVarLong()
                }
                Action.SetPercentage -> {
                    title = null
                    percentage = buffer.readFloatLE()
                    flags = 0
                    color = null
                    overlay = null
                    uniquePlayerId = buffer.readVarLong()
                }
                Action.SetTitle -> {
                    title = buffer.readString()
                    percentage = 0.0f
                    flags = 0
                    color = null
                    overlay = null
                    uniquePlayerId = buffer.readVarLong()
                }
                Action.SetFlags -> {
                    title = null
                    percentage = 0.0f
                    flags = buffer.readUnsignedShortLE()
                    color = Color.values()[buffer.readVarUInt()]
                    overlay = Overlay.values()[buffer.readVarUInt()]
                    uniquePlayerId = 0
                }
                Action.SetStyle -> {
                    title = null
                    percentage = 0.0f
                    flags = 0
                    color = Color.values()[buffer.readVarUInt()]
                    overlay = Overlay.values()[buffer.readVarUInt()]
                    uniquePlayerId = 0
                }
            }
            return BossBarPacket(uniqueBossId, action, title, uniquePlayerId, percentage, flags, color, overlay)
        }
    }
}
