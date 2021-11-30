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

/**
 * @author Kevin Ludwig
 */
class ServerPlayerListHeaderFooterPacket(
    val header: Component,
    val footer: Component
) : Packet<ServerPlayPacketHandler> {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeString(buffer.objectMapper.writeValueAsString(header))
        buffer.writeString(buffer.objectMapper.writeValueAsString(footer))
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.playerListHeaderFooter(this)
}

/**
 * @author Kevin Ludwig
 */
object ServerPlayerListHeaderFooterPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerPlayerListHeaderFooterPacket(buffer.objectMapper.readValue(ByteBufStringReader(buffer, buffer.readVarInt())), buffer.objectMapper.readValue(ByteBufStringReader(buffer, buffer.readVarInt())))
}