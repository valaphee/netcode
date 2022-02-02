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

package com.valaphee.netcode.mcje.play

import com.valaphee.netcode.mcje.Packet
import com.valaphee.netcode.mcje.PacketBuffer
import com.valaphee.netcode.mcje.PacketReader

/**
 * @author Kevin Ludwig
 */
class ClientWindowClickButtonPacket(
    val windowId: Int,
    val buttonId: Int
) : Packet<ClientPlayPacketHandler> {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeByte(windowId)
        buffer.writeByte(buttonId)
    }

    override fun handle(handler: ClientPlayPacketHandler) = handler.windowClickButton(this)

    override fun toString() = "ClientWindowClickButtonPacket(windowId=$windowId, buttonId=$buttonId)"
}

/**
 * @author Kevin Ludwig
 */
object ClientWindowClickButtonPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ClientWindowClickButtonPacket(buffer.readByte().toInt(), buffer.readByte().toInt())
}
