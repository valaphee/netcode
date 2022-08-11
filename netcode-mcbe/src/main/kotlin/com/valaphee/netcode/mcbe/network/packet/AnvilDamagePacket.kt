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

import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler

/**
 * @author Kevin Ludwig
 */
class AnvilDamagePacket(
    val damage: Int,
    val position: Int3
) : Packet() {
    override val id get() = 0x8D

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeByte(damage)
        buffer.writeBlockPosition(position)
    }

    override fun handle(handler: PacketHandler) = handler.anvilDamage(this)

    override fun toString() = "AnvilDamagePacket(damage=$damage, position=$position)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = AnvilDamagePacket(buffer.readByte().toInt(), buffer.readBlockPosition())
    }
}
