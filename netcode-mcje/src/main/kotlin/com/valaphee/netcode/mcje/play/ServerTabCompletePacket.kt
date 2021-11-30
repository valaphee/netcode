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
import com.valaphee.netcode.util.safeList

/**
 * @author Kevin Ludwig
 */
class ServerTabCompletePacket(
    val id: Int,
    val start: Int,
    val length: Int,
    val matches: List<Match>
) : Packet<ServerPlayPacketHandler> {
    class Match(
        val match: String,
        val tooltip: Component?
    )

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(id)
        buffer.writeVarInt(start)
        buffer.writeVarInt(length)
        buffer.writeVarInt(matches.size)
        matches.forEach {
            buffer.writeString(it.match)
            it.tooltip?.let {
                buffer.writeBoolean(true)
                buffer.writeString(buffer.objectMapper.writeValueAsString(it))
            } ?: buffer.writeBoolean(false)
        }
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.tabComplete(this)

    override fun toString() = "ServerTabCompletePacket(id=$id, start=$start, length=$length, matches=$matches)"
}

/**
 * @author Kevin Ludwig
 */
object ServerTabCompletePacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerTabCompletePacket(buffer.readVarInt(), buffer.readVarInt(), buffer.readVarInt(), safeList(buffer.readVarInt()) { ServerTabCompletePacket.Match(buffer.readString(), if (buffer.readBoolean()) buffer.objectMapper.readValue(ByteBufStringReader(buffer, buffer.readVarInt())) else null) })
}
