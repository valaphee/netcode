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

package com.valaphee.netcode.mcbe.entity.player

import com.valaphee.foundry.math.Float3
import com.valaphee.netcode.mcbe.Packet
import com.valaphee.netcode.mcbe.PacketBuffer
import com.valaphee.netcode.mcbe.PacketHandler
import com.valaphee.netcode.mcbe.PacketReader

/**
 * @author Kevin Ludwig
 */
class InputCorrectPacket(
    val position: Float3,
    val positionDelta: Float3,
    val onGround: Boolean,
    val tick: Long
) : Packet() {
    override val id get() = 0xA1

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeFloat3(position)
        buffer.writeFloat3(positionDelta)
        buffer.writeBoolean(onGround)
        buffer.writeVarULong(tick)
    }

    override fun handle(handler: PacketHandler) = handler.inputCorrect(this)

    override fun toString() = "InputCorrectPacket(position=$position, positionDelta=$positionDelta, onGround=$onGround, tick=$tick)"
}

/**
 * @author Kevin Ludwig
 */
object InputCorrectPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = InputCorrectPacket(buffer.readFloat3(), buffer.readFloat3(), buffer.readBoolean(), buffer.readVarULong())
}
