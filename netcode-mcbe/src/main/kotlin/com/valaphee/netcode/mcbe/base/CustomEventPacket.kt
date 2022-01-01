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

import com.google.gson.JsonElement
import com.google.gson.internal.Streams
import com.google.gson.stream.JsonReader
import com.valaphee.netcode.mcbe.Packet
import com.valaphee.netcode.mcbe.PacketBuffer
import com.valaphee.netcode.mcbe.PacketHandler
import com.valaphee.netcode.mcbe.PacketReader
import com.valaphee.netcode.util.ByteBufStringReader

/**
 * @author Kevin Ludwig
 */
class CustomEventPacket(
    val eventName: String,
    val json: JsonElement
) : Packet() {
    override val id get() = 0x75

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeString(eventName)
        buffer.writeString(json.toString())
    }

    override fun handle(handler: PacketHandler) = handler.customEvent(this)

    override fun toString() = "CustomEventPacket(eventName='$eventName', json=$json)"
}

/**
 * @author Kevin Ludwig
 */
object CustomEventPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = CustomEventPacket(buffer.readString(), Streams.parse(JsonReader(ByteBufStringReader(buffer, buffer.readVarUInt()))))
}
