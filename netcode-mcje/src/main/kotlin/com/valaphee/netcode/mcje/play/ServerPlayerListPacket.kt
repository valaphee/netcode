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
import com.valaphee.netcode.mcje.util.text.Component
import com.valaphee.netcode.mcje.world.GameMode
import com.valaphee.netcode.util.ByteBufStringReader
import com.valaphee.netcode.util.safeList
import java.util.UUID

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

    class Entry(
        val userId: UUID,
        val userName: String?,
        val properties: Map<String, Property>?,
        val gameMode: GameMode?,
        val latency: Int,
        val customName: Component?
    ) {
        class Property(
            val value: String,
            val signature: String?
        )
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(action.ordinal)
        buffer.writeVarInt(entries.size)
        entries.forEach {
            @Suppress("NON_EXHAUSTIVE_WHEN") when (action) {
                Action.Add -> {
                    buffer.writeString(it.userName!!)
                    buffer.writeVarInt(it.properties!!.size)
                    it.properties.forEach { (propertyKey, property) ->
                        buffer.writeString(propertyKey)
                        buffer.writeString(property.value)
                        property.signature?.let {
                            buffer.writeBoolean(true)
                            buffer.writeString(it)
                        } ?: buffer.writeBoolean(false)
                    }
                    buffer.writeVarInt(it.gameMode!!.ordinal)
                    buffer.writeVarInt(it.latency)
                    it.customName?.let {
                        buffer.writeBoolean(true)
                        buffer.writeString(buffer.objectMapper.writeValueAsString(it))
                    } ?: buffer.writeBoolean(false)
                }
                Action.UpdateCustomName -> {
                    it.customName?.let {
                        buffer.writeBoolean(true)
                        buffer.writeString(buffer.objectMapper.writeValueAsString(it))
                    } ?: buffer.writeBoolean(false)
                }
                Action.UpdateGameMode -> buffer.writeVarInt(it.gameMode!!.ordinal)
                Action.UpdateLatency -> buffer.writeVarInt(it.latency)
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
                ServerPlayerListPacket.Action.Add -> ServerPlayerListPacket.Entry(buffer.readUuid(), buffer.readString(), mutableMapOf<String, ServerPlayerListPacket.Entry.Property>().apply { repeat(buffer.readVarInt()) { this[buffer.readString()] = ServerPlayerListPacket.Entry.Property(buffer.readString(), if (buffer.readBoolean()) buffer.readString() else null) } }, checkNotNull(GameMode.byIdOrNull(buffer.readVarInt())), buffer.readVarInt(), if (buffer.readBoolean()) buffer.objectMapper.readValue(ByteBufStringReader(buffer, buffer.readVarInt())) else null)
                ServerPlayerListPacket.Action.UpdateGameMode -> ServerPlayerListPacket.Entry(buffer.readUuid(), null, null, checkNotNull(GameMode.byIdOrNull(buffer.readVarInt())), 0, null)
                ServerPlayerListPacket.Action.UpdateLatency -> ServerPlayerListPacket.Entry(buffer.readUuid(), null, null, null, buffer.readVarInt(), null)
                ServerPlayerListPacket.Action.UpdateCustomName -> ServerPlayerListPacket.Entry(buffer.readUuid(), null, null, null, 0, if (buffer.readBoolean()) buffer.objectMapper.readValue(ByteBufStringReader(buffer, buffer.readVarInt())) else null)
                ServerPlayerListPacket.Action.Remove -> ServerPlayerListPacket.Entry(buffer.readUuid(), null, null, null, 0, null)
            }
        }
        return ServerPlayerListPacket(action, entries)
    }
}
