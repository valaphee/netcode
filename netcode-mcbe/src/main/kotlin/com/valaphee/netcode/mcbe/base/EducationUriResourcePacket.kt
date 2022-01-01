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

package com.valaphee.netcode.mcbe.base

import com.valaphee.netcode.mcbe.Packet
import com.valaphee.netcode.mcbe.PacketBuffer
import com.valaphee.netcode.mcbe.PacketHandler
import com.valaphee.netcode.mcbe.PacketReader

/**
 * @author Kevin Ludwig
 */
class EducationUriResourcePacket(
    val name: String,
    val uri: String
) : Packet() {
    override val id get() = 0xAA

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeString(name)
        buffer.writeString(uri)
    }

    override fun handle(handler: PacketHandler) = handler.educationUriResource(this)

    override fun toString() = "EducationUriResourcePacket(name=$name, uri=$uri)"
}

/**
 * @author Kevin Ludwig
 */
object EducationUriResourcePacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = EducationUriResourcePacket(buffer.readString(), buffer.readString())
}
