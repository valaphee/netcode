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

package com.valaphee.netcode.mcbe.entity.stack

import com.valaphee.foundry.math.Float3
import com.valaphee.netcode.mcbe.Packet
import com.valaphee.netcode.mcbe.PacketBuffer
import com.valaphee.netcode.mcbe.PacketHandler
import com.valaphee.netcode.mcbe.PacketReader
import com.valaphee.netcode.mcbe.Restrict
import com.valaphee.netcode.mcbe.Restriction
import com.valaphee.netcode.mcbe.entity.metadata.Metadata
import com.valaphee.netcode.mcbe.item.stack.Stack
import com.valaphee.netcode.mcbe.item.stack.readStack
import com.valaphee.netcode.mcbe.item.stack.readStackPre431
import com.valaphee.netcode.mcbe.item.stack.writeStack
import com.valaphee.netcode.mcbe.item.stack.writeStackPre431

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class StackAddPacket(
    val uniqueEntityId: Long,
    val runtimeEntityId: Long,
    val stack: Stack?,
    val position: Float3,
    val velocity: Float3,
    val metadata: Metadata,
    val fromFishing: Boolean
) : Packet() {
    override val id get() = 0x0F

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarLong(uniqueEntityId)
        buffer.writeVarULong(runtimeEntityId)
        if (version >= 431) buffer.writeStack(stack) else buffer.writeStackPre431(stack)
        buffer.writeFloat3(position)
        buffer.writeFloat3(velocity)
        metadata.writeToBuffer(buffer)
        buffer.writeBoolean(fromFishing)
    }

    override fun handle(handler: PacketHandler) = handler.stackAdd(this)

    override fun toString() = "StackAddPacket(uniqueEntityId=$uniqueEntityId, runtimeEntityId=$runtimeEntityId, stack=$stack, position=$position, velocity=$velocity, metadata=$metadata, fromFishing=$fromFishing)"
}

/**
 * @author Kevin Ludwig
 */
object StackAddPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = StackAddPacket(
        buffer.readVarLong(),
        buffer.readVarULong(),
        if (version >= 431) buffer.readStack() else buffer.readStackPre431(),
        buffer.readFloat3(),
        buffer.readFloat3(),
        Metadata().apply { readFromBuffer(buffer) },
        buffer.readBoolean()
    )
}
