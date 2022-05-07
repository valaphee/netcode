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

package com.valaphee.netcode.mcje.network.packet.play

import com.valaphee.foundry.math.Float2
import com.valaphee.netcode.mcje.network.ClientPlayPacketHandler
import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.PacketReader

/**
 * @author Kevin Ludwig
 */
class ClientSteerPacket(
    val move: Float2,
    val jumping: Boolean,
    val sneaking: Boolean
) : Packet<ClientPlayPacketHandler>() {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeFloat2(move)
        buffer.writeByte(if (jumping) flagJumping else 0 or if (sneaking) flagSneaking else 0)
    }

    override fun handle(handler: ClientPlayPacketHandler) = handler.steer(this)

    override fun toString() = "ClientSteerPacket(move=$move, jumping=$jumping, sneaking=$sneaking)"

    companion object {
        internal const val flagJumping = 1
        internal const val flagSneaking = 1 shl 1
    }
}

/**
 * @author Kevin Ludwig
 */
object ClientSteerPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): ClientSteerPacket {
        val move = buffer.readFloat2()
        val flagsValue = buffer.readByte().toInt()
        return ClientSteerPacket(move, flagsValue and ClientSteerPacket.flagJumping != 0, flagsValue and ClientSteerPacket.flagSneaking != 0)
    }
}
