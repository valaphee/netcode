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
import com.valaphee.netcode.mcbe.network.V1_16_100

/**
 * @author Kevin Ludwig
 */
class WindowClosePacket(
    val windowId: Int,
    val fromServer: Boolean
) : Packet() {
    override val id get() = 0x2F

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeByte(windowId)
        if (version >= V1_16_100) buffer.writeBoolean(fromServer)
    }

    override fun handle(handler: PacketHandler) = handler.windowClose(this)

    override fun toString() = "WindowClosePacket(windowId=$windowId, fromServer=$fromServer)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = WindowClosePacket(buffer.readByte().toInt(), if (version >= V1_16_100) buffer.readBoolean() else false)
    }
}
