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

import com.valaphee.netcode.mcbe.item.stack.Stack
import com.valaphee.netcode.mcbe.item.stack.readStack
import com.valaphee.netcode.mcbe.item.stack.readStackPre431
import com.valaphee.netcode.mcbe.item.stack.readStackWithNetIdPre431
import com.valaphee.netcode.mcbe.item.stack.writeStack
import com.valaphee.netcode.mcbe.item.stack.writeStackPre431
import com.valaphee.netcode.mcbe.item.stack.writeStackWithNetIdPre431
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.PacketReader
import com.valaphee.netcode.mcbe.network.Restrict
import com.valaphee.netcode.mcbe.network.Restriction
import com.valaphee.netcode.mcbe.util.safeList

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class InventoryContentPacket(
    val windowId: Int,
    val content: List<Stack?>
) : Packet() {
    override val id get() = 0x31

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarUInt(windowId)
        buffer.writeVarUInt(content.size)
        if (version >= 431) content.forEach(buffer::writeStack) else if (version >= 407) content.forEach(buffer::writeStackWithNetIdPre431) else content.forEach(buffer::writeStackPre431)
    }

    override fun handle(handler: PacketHandler) = handler.inventoryContent(this)

    override fun toString() = "InventoryContentPacket(windowId=$windowId, content=$content)"
}

/**
 * @author Kevin Ludwig
 */
object InventoryContentPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = InventoryContentPacket(buffer.readVarUInt(), safeList(buffer.readVarUInt()) { if (version >= 431) buffer.readStack() else if (version >= 407) buffer.readStackWithNetIdPre431() else buffer.readStackPre431() })
}
