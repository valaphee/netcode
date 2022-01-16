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
import com.valaphee.netcode.mcbe.network.Restrict
import com.valaphee.netcode.mcbe.network.Restriction
import com.valaphee.netcode.mcbe.item.stack.Stack
import com.valaphee.netcode.mcbe.item.stack.readStackInstance
import com.valaphee.netcode.mcbe.item.stack.readStackWithNetIdPre431
import com.valaphee.netcode.mcbe.item.stack.writeStackInstance
import com.valaphee.netcode.mcbe.item.stack.writeStackWithNetIdPre431
import com.valaphee.netcode.mcbe.util.safeList

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class CreativeInventoryPacket(
    val content: List<Stack?>
) : Packet() {
    override val id get() = 0x91

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarUInt(content.size)
        content.forEach {
            if (version >= 431) {
                buffer.writeVarUInt(it?.netId ?: 0)
                buffer.writeStackInstance(it)
            } else buffer.writeStackWithNetIdPre431(it)
        }
    }

    override fun handle(handler: PacketHandler) = handler.creativeInventory(this)

    override fun toString() = "CreativeInventoryPacket(content=$content)"
}

/**
 * @author Kevin Ludwig
 */
object CreativeInventoryPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = CreativeInventoryPacket(safeList(buffer.readVarUInt()) {
        if (version >= 431) {
            val netId = buffer.readVarUInt()
            buffer.readStackInstance().also { it?.let { it.netId = netId } }
        } else buffer.readStackWithNetIdPre431()
    })
}
