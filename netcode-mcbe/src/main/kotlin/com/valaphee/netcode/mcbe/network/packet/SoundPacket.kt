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

import com.valaphee.foundry.math.Float3
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.PacketReader
import com.valaphee.netcode.mcbe.network.Restrict
import com.valaphee.netcode.mcbe.network.Restriction
import com.valaphee.netcode.mcbe.world.Sound

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class SoundPacket(
    val sound: Sound? = null,
    val soundKey: String? = null,
    val position: Float3,
    val volume: Float,
    val pitch: Float
) : Packet() {
    override val id get() = 0x56

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeString(sound?.key ?: soundKey!!)
        buffer.writeInt3UnsignedY(position.toMutableFloat3().scale(8.0f).toInt3())
        buffer.writeFloatLE(volume)
        buffer.writeFloatLE(pitch)
    }

    override fun handle(handler: PacketHandler) = handler.sound(this)

    override fun toString() = "SoundPacket(sound=$sound, soundKey=$soundKey, position=$position, volume=$volume, pitch=$pitch)"
}

/**
 * @author Kevin Ludwig
 */
object SoundPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): SoundPacket {
        val soundKey = buffer.readString()
        val sound = Sound.byKeyOrNull(soundKey)
        val position = buffer.readInt3UnsignedY().toMutableFloat3().scale(1 / 8.0f)
        val volume = buffer.readFloatLE()
        val pitch = buffer.readFloatLE()
        return SoundPacket(sound, soundKey, position, volume, pitch)
    }
}
