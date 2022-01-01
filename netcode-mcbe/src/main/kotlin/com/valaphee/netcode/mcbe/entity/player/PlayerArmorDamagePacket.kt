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

import com.valaphee.netcode.mcbe.Packet
import com.valaphee.netcode.mcbe.PacketBuffer
import com.valaphee.netcode.mcbe.PacketHandler
import com.valaphee.netcode.mcbe.PacketReader
import com.valaphee.netcode.mcbe.Restrict
import com.valaphee.netcode.mcbe.Restriction

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class PlayerArmorDamagePacket(
    val damages: Array<Int?>
) : Packet() {
    init {
        require(damages.size == 4)
    }

    override val id get() = 0x95

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.skipBytes(1)
        val writerIndex = buffer.writerIndex()
        var flagsValue = 0
        damages.forEachIndexed { i, damage ->
            damage?.let {
                flagsValue = flagsValue or (1 shl i)
                buffer.writeVarInt(it)
            }
        }
        buffer.setByte(writerIndex, flagsValue)
    }

    override fun handle(handler: PacketHandler) = handler.playerArmorDamage(this)

    override fun toString() = "PlayerArmorDamagePacket(damages=${damages.contentToString()})"
}

/**
 * @author Kevin Ludwig
 */
object PlayerArmorDamagePacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): PlayerArmorDamagePacket {
        val flagsValue = buffer.readByte().toInt()
        val damages = arrayOfNulls<Int>(4)
        repeat(4) { damages[it] = if ((flagsValue and (1 shl it)) != 0) buffer.readVarInt() else null }
        return PlayerArmorDamagePacket(damages)
    }
}
