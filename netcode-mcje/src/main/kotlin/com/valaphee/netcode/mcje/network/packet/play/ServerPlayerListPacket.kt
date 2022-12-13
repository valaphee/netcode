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
import com.valaphee.netcode.mcje.network.ServerPlayPacketHandler
import com.valaphee.netcode.mcje.network.V1_19_0
import com.valaphee.netcode.mcje.network.V1_19_3
import com.valaphee.netcode.mcje.world.GameMode
import com.valaphee.netcode.mcje.world.entity.player.GameProfile
import com.valaphee.netcode.util.LazyList
import net.kyori.adventure.text.Component
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
class ServerPlayerListPacket(
    val action: Action,
    val entries: List<Entry>
) : Packet<ServerPlayPacketHandler>() {
    enum class Action {
        Add, InitializeChat, UpdateGameMode, UpdateListed, UpdateLatency, UpdateCustomName, Remove
    }

    data class Entry(
        val gameProfile: GameProfile,
        val gameMode: GameMode? = null,
        val listed: Boolean = false,
        val latency: Int = 0,
        val customName: Component? = null,
        val sessionId: UUID? = null,
        val signature: Signature? = null
    )  {
        data class Signature(
            val expiresAt: Long,
            val publicKey: ByteArray,
            val signature: ByteArray
        )
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        if (version >= V1_19_3 && action != Action.Remove) buffer.writeVarInt(action.ordinal)
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
                    if (version < V1_19_3) {
                        buffer.writeVarInt(it.gameMode!!.ordinal)
                        buffer.writeVarInt(it.latency)
                        it.customName?.let {
                            buffer.writeBoolean(true)
                            buffer.writeComponent(it)
                        } ?: buffer.writeBoolean(false)
                        if (version >= V1_19_0) it.signature?.let {
                            buffer.writeBoolean(true)
                            buffer.writeLong(it.expiresAt)
                            buffer.writeByteArray(it.publicKey)
                            buffer.writeByteArray(it.signature)
                        } ?: buffer.writeBoolean(false)
                    }
                }
                Action.InitializeChat -> {
                    buffer.writeUuid(it.sessionId!!)
                    it.signature?.let {
                        buffer.writeBoolean(true)
                        buffer.writeLong(it.expiresAt)
                        buffer.writeByteArray(it.publicKey)
                        buffer.writeByteArray(it.signature)
                    } ?: buffer.writeBoolean(false)
                }
                Action.UpdateGameMode -> buffer.writeVarInt(it.gameMode!!.ordinal)
                Action.UpdateListed -> buffer.writeBoolean(it.listed)
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

    object UpdateReader : Reader {
        override fun read(buffer: PacketBuffer, version: Int): ServerPlayerListPacket {
            val action = Action.values()[buffer.readVarInt()]
            val entries = LazyList(buffer.readVarInt()) {
                when (action) {
                    Action.Add -> {
                        val gameProfile = GameProfile(buffer.readUuid(), buffer.readString(16), LazyList(buffer.readVarInt()) { GameProfile.Property(buffer.readString(), buffer.readString(), if (buffer.readBoolean()) buffer.readString() else null) })
                        val gameMode: GameMode?
                        val latency: Int
                        val customName: Component?
                        val signature: Entry.Signature?
                        if (version >= V1_19_3) {
                            gameMode = null
                            latency = 0
                            customName = null
                            signature = null
                        } else {
                            gameMode = checkNotNull(GameMode.byIdOrNull(buffer.readVarInt()))
                            latency = buffer.readVarInt()
                            customName = if (buffer.readBoolean()) buffer.readComponent() else null
                            signature = if (version >= V1_19_0 && buffer.readBoolean()) Entry.Signature(buffer.readLong(), buffer.readByteArray(), buffer.readByteArray()) else null
                        }
                        Entry(gameProfile, gameMode, false, latency, customName, null, signature)
                    }
                    Action.InitializeChat ->  Entry(GameProfile(buffer.readUuid(), null), sessionId = buffer.readUuid(), signature = if (buffer.readBoolean()) Entry.Signature(buffer.readLong(), buffer.readByteArray(), buffer.readByteArray()) else null)
                    Action.UpdateGameMode -> Entry(GameProfile(buffer.readUuid(), null), gameMode = checkNotNull(GameMode.byIdOrNull(buffer.readVarInt())))
                    Action.UpdateListed -> Entry(GameProfile(buffer.readUuid(), null), listed = buffer.readBoolean())
                    Action.UpdateLatency -> Entry(GameProfile(buffer.readUuid(), null), latency = buffer.readVarInt())
                    Action.UpdateCustomName -> Entry(GameProfile(buffer.readUuid(), null), customName = if (buffer.readBoolean()) buffer.readComponent() else null)
                    Action.Remove -> Entry(GameProfile(buffer.readUuid(), null))
                }
            }
            return ServerPlayerListPacket(action, entries)
        }
    }

    object RemoveReader : Reader {
        override fun read(buffer: PacketBuffer, version: Int) = ServerPlayerListPacket(Action.Remove, LazyList(buffer.readVarInt()) { Entry(GameProfile(buffer.readUuid(), null)) })
    }
}
