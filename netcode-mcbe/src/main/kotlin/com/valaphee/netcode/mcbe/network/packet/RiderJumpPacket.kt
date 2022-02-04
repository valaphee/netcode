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

import com.valaphee.netcode.mcbe.Restrict
import com.valaphee.netcode.mcbe.Restriction
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.PacketReader

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToServer)
class RiderJumpPacket(
    val jumpStrength: Int
) : Packet() {
    override val id get() = 0x14

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(jumpStrength)
    }

    override fun handle(handler: PacketHandler) = handler.riderJump(this)

    override fun toString() = "RiderJumpPacket(jumpStrength=$jumpStrength)"
}

/**
 * @author Kevin Ludwig
 */
object RiderJumpPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = RiderJumpPacket(buffer.readVarInt())
}
