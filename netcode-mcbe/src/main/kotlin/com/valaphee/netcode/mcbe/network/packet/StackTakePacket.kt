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

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class StackTakePacket(
    val runtimeEntityId: Long,
    val stackRuntimeEntityId: Long
) : Packet() {
    override val id get() = 0x11

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarULong(stackRuntimeEntityId)
        buffer.writeVarULong(runtimeEntityId)
    }

    override fun handle(handler: PacketHandler) = handler.stackTake(this)

    override fun toString() = "StackTakePacket(runtimeEntityId=$runtimeEntityId, stackRuntimeEntityId=$stackRuntimeEntityId)"
}

/**
 * @author Kevin Ludwig
 */
object StackTakePacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = StackTakePacket(buffer.readVarULong(), buffer.readVarULong())
}