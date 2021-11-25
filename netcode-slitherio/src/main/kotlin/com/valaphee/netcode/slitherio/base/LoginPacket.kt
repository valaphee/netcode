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

package com.valaphee.netcode.slitherio.base

import com.valaphee.netcode.slitherio.Packet
import com.valaphee.netcode.slitherio.PacketBuffer
import com.valaphee.netcode.slitherio.PacketHandler
import com.valaphee.netcode.slitherio.PacketReader
import io.netty.buffer.ByteBuf

/**
 * @author Kevin Ludwig
 */
class LoginPacket(
    val protocolVersion: Int,
    val skinId: Int,
    val name: String,
) : Packet() {
    override val id = 's'

    override fun write(buffer: PacketBuffer) {
        buffer.writeByte(protocolVersion)
        buffer.writeByte(skinId)
        buffer.writeByte(name.length)
        buffer.writeBytes(name.toByteArray(Charsets.UTF_8))
    }

    override fun handle(handler: PacketHandler) = handler.login(this)
}

/**
 * @author Kevin Ludwig
 */
object LoginPacketReader : PacketReader {
    override fun read(buffer: ByteBuf) = LoginPacket(buffer.readUnsignedByte().toInt(), buffer.readUnsignedByte().toInt(), ByteArray(buffer.readUnsignedByte().toInt()).apply { buffer.readBytes(this) }.toString(Charsets.UTF_8))
}
