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

import com.fasterxml.jackson.module.kotlin.readValue
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.PacketReader
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream
import java.io.OutputStream

/**
 * @author Kevin Ludwig
 */
class EntityPropertiesPacket(
    val data: Any?
) : Packet() {
    override val id get() = 0xA5

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.nbtObjectMapper.writeValue(ByteBufOutputStream(buffer) as OutputStream, data)
    }

    override fun handle(handler: PacketHandler) = handler.entityProperties(this)

    override fun toString() = "EntityPropertiesPacket(data=$data)"
}

/**
 * @author Kevin Ludwig
 */
object EntityPropertiesPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = EntityPropertiesPacket(buffer.nbtObjectMapper.readValue(ByteBufInputStream(buffer)))
}
