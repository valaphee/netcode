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
import com.valaphee.netcode.mcbe.Restrict
import com.valaphee.netcode.mcbe.Restriction
import io.netty.util.AsciiString

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToServer)
class LoginPacket(
    val protocolVersion: Int,
    val authJws: String,
    val userJws: String
) : Packet() {
    override val id get() = 0x01

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeInt(protocolVersion)
        val jwsIndex = buffer.writerIndex()
        buffer.writeZero(PacketBuffer.MaximumVarUIntLength)
        buffer.writeAsciiStringLe(AsciiString(authJws))
        buffer.writeAsciiStringLe(AsciiString(userJws))
        buffer.setMaximumLengthVarUInt(jwsIndex, buffer.writerIndex() - (jwsIndex + PacketBuffer.MaximumVarUIntLength))
    }

    override fun handle(handler: PacketHandler) = handler.login(this)

    override fun toString() = "LoginPacket(protocolVersion=$protocolVersion, authJws=$authJws, userJws=$userJws)"
}

/**
 * @author Kevin Ludwig
 */
object LoginPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): LoginPacket {
        val readerIndex = buffer.readerIndex()
        var protocolVersion = buffer.readInt()
        if (protocolVersion == 0) {
            buffer.readerIndex(readerIndex + 2)
            protocolVersion = buffer.readInt()
        }
        buffer.readVarUInt()
        val authJws = buffer.readAsciiStringLe().toString()
        val userJws = buffer.readAsciiStringLe().toString()
        return LoginPacket(protocolVersion, authJws, userJws)
    }
}
