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

package com.valaphee.netcode.mcbe.item.craft

import com.valaphee.netcode.mcbe.Packet
import com.valaphee.netcode.mcbe.PacketBuffer
import com.valaphee.netcode.mcbe.PacketHandler
import com.valaphee.netcode.mcbe.PacketReader
import com.valaphee.netcode.mcbe.item.stack.Stack
import com.valaphee.netcode.mcbe.item.stack.readStack
import com.valaphee.netcode.mcbe.item.stack.readStackPre431
import com.valaphee.netcode.mcbe.item.stack.writeStack
import com.valaphee.netcode.mcbe.item.stack.writeStackPre431
import com.valaphee.netcode.util.safeList
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
class CraftingEventPacket(
    val windowId: Int,
    val type: Type,
    val recipeId: UUID,
    val inputs: List<Stack?>,
    val outputs: List<Stack?>,
) : Packet() {
    enum class Type {
        Inventory, Crafting, Workbench
    }

    override val id get() = 0x35

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeByte(windowId)
        buffer.writeVarInt(type.ordinal)
        buffer.writeUuid(recipeId)
        buffer.writeVarUInt(inputs.size)
        inputs.forEach { if (version >= 431) buffer.writeStack(it) else buffer.writeStackPre431(it) }
        buffer.writeVarUInt(outputs.size)
        outputs.forEach { if (version >= 431) buffer.writeStack(it) else buffer.writeStackPre431(it) }
    }

    override fun handle(handler: PacketHandler) = handler.craftingEvent(this)

    override fun toString() = "CraftingEventPacket(windowId=$windowId, type=$type, recipeId=$recipeId, inputs=$inputs, outputs=$outputs)"
}

/**
 * @author Kevin Ludwig
 */
object CraftingEventPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = CraftingEventPacket(buffer.readUnsignedByte().toInt(), CraftingEventPacket.Type.values()[buffer.readVarInt()], buffer.readUuid(), safeList(buffer.readVarUInt()) { if (version >= 431) buffer.readStack() else buffer.readStackPre431() }, safeList(buffer.readVarUInt()) { if (version >= 431) buffer.readStack() else buffer.readStackPre431() })
}
