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
import com.valaphee.netcode.mcje.world.GameMode
import com.valaphee.netcode.mcje.world.entity.player.GameProfile
import com.valaphee.netcode.util.safeList
import net.kyori.adventure.text.Component

/**
 * @author Kevin Ludwig
 */
class ServerPlayerListPacket(
    val action: Action,
    val entries: List<Entry>
) : Packet<ServerPlayPacketHandler> {
    enum class Action {
        Add, UpdateGameMode, UpdateLatency, UpdateCustomName, Remove
    }

    data class Entry(
        val gameProfile: GameProfile,
        val gameMode: GameMode?,
        val latency: Int,
        val customName: Component?
    )

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(action.ordinal)
        buffer.writeVarInt(entries.size)
        entries.forEach {
            buffer.writeUuid(it.gameProfile.userId!!)
            when (action) {
                Action.Add -> {
                    buffer.writeString(it.gameProfile.userName!!)
                    buffer.writeVarInt(it.gameProfile.properties!!.size)
                    it.gameProfile.properties.forEach {
                        buffer.writeString(it.name)
                        buffer.writeString(it.value)
                        it.signature?.let {
                            buffer.writeBoolean(true)
                            buffer.writeString(it)
                        } ?: buffer.writeBoolean(false)
                    }
                    buffer.writeVarInt(it.gameMode!!.ordinal)
                    buffer.writeVarInt(it.latency)
                    it.customName?.let {
                        buffer.writeBoolean(true)
                        buffer.writeComponent(it)
                    } ?: buffer.writeBoolean(false)
                }
                Action.UpdateGameMode -> buffer.writeVarInt(it.gameMode!!.ordinal)
                Action.UpdateLatency -> buffer.writeVarInt(it.latency)
                Action.UpdateCustomName -> {
                    it.customName?.let {
                        buffer.writeBoolean(true)
                        buffer.writeComponent(it)
                    } ?: buffer.writeBoolean(false)
                }
                Action.Remove -> Unit
            }
        }
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.playerList(this)

    override fun toString() = "ServerPlayerListPacket(action=$action, entries=$entries)"
}

/**
 * @author Kevin Ludwig
 */
object ServerPlayerListPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): ServerPlayerListPacket {
        val action = ServerPlayerListPacket.Action.values()[buffer.readVarInt()]
        val entries = safeList(buffer.readVarInt()) {
            when (action) {
                ServerPlayerListPacket.Action.Add -> ServerPlayerListPacket.Entry(GameProfile(buffer.readUuid(), buffer.readString(16), safeList(buffer.readVarInt()) { GameProfile.Property(buffer.readString(), buffer.readString(), if (buffer.readBoolean()) buffer.readString() else null) }), checkNotNull(GameMode.byIdOrNull(buffer.readVarInt())), buffer.readVarInt(), if (buffer.readBoolean()) buffer.readComponent() else null)
                ServerPlayerListPacket.Action.UpdateGameMode -> ServerPlayerListPacket.Entry(GameProfile(buffer.readUuid(), null), checkNotNull(GameMode.byIdOrNull(buffer.readVarInt())), 0, null)
                ServerPlayerListPacket.Action.UpdateLatency -> ServerPlayerListPacket.Entry(GameProfile(buffer.readUuid(), null), null, buffer.readVarInt(), null)
                ServerPlayerListPacket.Action.UpdateCustomName -> ServerPlayerListPacket.Entry(GameProfile(buffer.readUuid(), null), null, 0, if (buffer.readBoolean()) buffer.readComponent() else null)
                ServerPlayerListPacket.Action.Remove -> ServerPlayerListPacket.Entry(GameProfile(buffer.readUuid(), null), null, 0, null)
            }
        }
        return ServerPlayerListPacket(action, entries)
    }
}
