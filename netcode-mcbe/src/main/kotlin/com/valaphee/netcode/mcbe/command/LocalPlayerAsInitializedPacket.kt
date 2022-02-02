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

package com.valaphee.netcode.mcbe.command

import com.valaphee.netcode.mcbe.Packet
import com.valaphee.netcode.mcbe.PacketBuffer
import com.valaphee.netcode.mcbe.PacketHandler
import com.valaphee.netcode.mcbe.PacketReader
import com.valaphee.netcode.mcbe.Restrict
import com.valaphee.netcode.mcbe.Restriction

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToServer)
class LocalPlayerAsInitializedPacket(
    val runtimeEntityId: Long
) : Packet() {
    override val id get() = 0x71

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarULong(runtimeEntityId)
    }

    override fun handle(handler: PacketHandler) = handler.localPlayerAsInitialized(this)

    override fun toString() = "LocalPlayerAsInitializedPacket(runtimeEntityId=$runtimeEntityId)"
}

/**
 * @author Kevin Ludwig
 */
object LocalPlayerAsInitializedPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = LocalPlayerAsInitializedPacket(buffer.readVarULong())
}