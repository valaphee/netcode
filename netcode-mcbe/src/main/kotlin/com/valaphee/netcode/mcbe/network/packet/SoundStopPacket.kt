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
import com.valaphee.netcode.mcbe.world.Sound

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class SoundStopPacket(
    val sound: Sound? = null,
    val soundKey: String? = null
) : Packet() {
    constructor(sound: Sound) : this(sound, null)

    constructor(soundName: String) : this(null, soundName)

    override val id get() = 0x57

    override fun write(buffer: PacketBuffer, version: Int) {
        val soundKey = sound?.key ?: soundKey
        soundKey?.let {
            buffer.writeString(it)
            buffer.writeBoolean(false)
        } ?: run {
            buffer.writeString("")
            buffer.writeBoolean(true)
        }
    }

    override fun handle(handler: PacketHandler) = handler.soundStop(this)

    override fun toString() = "SoundStopPacket(sound=$sound, soundKey=$soundKey)"
}

/**
 * @author Kevin Ludwig
 */
object SoundStopPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): SoundStopPacket {
        val _soundKey = buffer.readString()
        val sound: Sound?
        val soundKey: String?
        if (!buffer.readBoolean()) {
            sound = Sound.byKeyOrNull(_soundKey)
            soundKey = _soundKey
        } else {
            sound = null
            soundKey = null
        }
        return SoundStopPacket(sound, soundKey)
    }
}
