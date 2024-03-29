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
import com.valaphee.netcode.mcbe.network.Restrict
import com.valaphee.netcode.mcbe.network.Restriction
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream
import java.io.InputStream
import java.io.OutputStream

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class WorldGenericEventPacket(
    val event: WorldEventPacket.Event,
    val data: Any?
) : Packet() {
    override val id get() = 0x7C

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(event.getId(version))
        buffer.nbtVarIntNoWrapObjectMapper.writeValue(ByteBufOutputStream(buffer) as OutputStream, data)
    }

    override fun handle(handler: PacketHandler) = handler.worldGenericEvent(this)

    override fun toString() = "WorldGenericEventPacket(event=$event, data=$data)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int): WorldGenericEventPacket{
            val eventId = buffer.readVarInt()
            val event = checkNotNull(WorldEventPacket.Event[version, eventId]) { "No such world event: $eventId" }
            val data = buffer.nbtVarIntNoWrapObjectMapper.readValue<Any?>(ByteBufInputStream(buffer) as InputStream)
            return WorldGenericEventPacket(event, data)
        }
    }
}
