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
import com.valaphee.netcode.mcbe.Restrict
import com.valaphee.netcode.mcbe.Restriction

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class VideoStreamPacket(
    val url: String,
    val screenshotFrequency: Float,
    val action: Action,
    val width: Int,
    val height: Int
) : Packet() {
    enum class Action {
        Open, Close
    }

    override val id get() = 0x7E

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeString(url)
        buffer.writeFloatLE(screenshotFrequency)
        buffer.writeByte(action.ordinal)
        buffer.writeIntLE(width)
        buffer.writeIntLE(height)
    }

    override fun handle(handler: PacketHandler) = handler.videoStream(this)

    override fun toString() = "VideoStreamPacket(url='$url', screenshotFrequency=$screenshotFrequency, action=$action, width=$width, height=$height)"
}

/**
 * @author Kevin Ludwig
 */
object VideoStreamPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = VideoStreamPacket(buffer.readString(), buffer.readFloatLE(), VideoStreamPacket.Action.values()[buffer.readByte().toInt()], buffer.readIntLE(), buffer.readIntLE())
}