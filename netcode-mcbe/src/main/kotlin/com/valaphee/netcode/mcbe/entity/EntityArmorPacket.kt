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

package com.valaphee.netcode.mcbe.entity

import com.valaphee.netcode.mcbe.Packet
import com.valaphee.netcode.mcbe.PacketBuffer
import com.valaphee.netcode.mcbe.PacketHandler
import com.valaphee.netcode.mcbe.PacketReader
import com.valaphee.netcode.mcbe.item.stack.Stack
import com.valaphee.netcode.mcbe.item.stack.readStack
import com.valaphee.netcode.mcbe.item.stack.readStackPre431
import com.valaphee.netcode.mcbe.item.stack.writeStack
import com.valaphee.netcode.mcbe.item.stack.writeStackPre431

/**
 * @author Kevin Ludwig
 */
class EntityArmorPacket(
    val runtimeEntityId: Long,
    val helmet: Stack?,
    val chestplate: Stack?,
    val leggings: Stack?,
    val boots: Stack?
) : Packet() {
    override val id get() = 0x20

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarULong(runtimeEntityId)
        if (version >= 431) buffer.writeStack(helmet) else buffer.writeStackPre431(helmet)
        if (version >= 431) buffer.writeStack(chestplate) else buffer.writeStackPre431(chestplate)
        if (version >= 431) buffer.writeStack(leggings) else buffer.writeStackPre431(leggings)
        if (version >= 431) buffer.writeStack(boots) else buffer.writeStackPre431(boots)
    }

    override fun handle(handler: PacketHandler) = handler.entityArmor(this)

    override fun toString() = "EntityArmorPacket(runtimeEntityId=$runtimeEntityId, helmet=$helmet, chestplate=$chestplate, leggings=$leggings, boots=$boots)"
}

/**
 * @author Kevin Ludwig
 */
object EntityArmorPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = EntityArmorPacket(
        buffer.readVarULong(),
        if (version >= 431) buffer.readStack() else buffer.readStackPre431(),
        if (version >= 431) buffer.readStack() else buffer.readStackPre431(),
        if (version >= 431) buffer.readStack() else buffer.readStackPre431(),
        if (version >= 431) buffer.readStack() else buffer.readStackPre431()
    )
}
