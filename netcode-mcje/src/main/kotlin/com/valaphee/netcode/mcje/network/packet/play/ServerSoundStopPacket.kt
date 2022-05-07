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

import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.PacketReader
import com.valaphee.netcode.mcje.network.ServerPlayPacketHandler
import com.valaphee.netcode.mcje.util.NamespacedKey
import com.valaphee.netcode.mcje.world.SoundCategory

/**
 * @author Kevin Ludwig
 */
class ServerSoundStopPacket(
    val category: SoundCategory?,
    val soundKey: NamespacedKey?
) : Packet<ServerPlayPacketHandler>() {
    override fun write(buffer: PacketBuffer, version: Int) {
        val flagsIndex = buffer.writerIndex()
        var flagsValue = 0
        category?.let {
            buffer.writeVarInt(it.ordinal)
            flagsValue = flagsValue or flagHasCategory
        }
        soundKey?.let {
            buffer.writeNamespacedKey(it)
            flagsValue = flagsValue or flagHasSound
        }
        buffer.setByte(flagsIndex, flagsValue)
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.soundStop(this)

    override fun toString() = "ServerSoundStopPacket(category=$category, soundKey=$soundKey)"

    companion object {
        internal const val flagHasCategory = 1
        internal const val flagHasSound = 1 shl 1
    }
}

/**
 * @author Kevin Ludwig
 */
object ServerSoundStopPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): ServerSoundStopPacket {
        val flagsValue = buffer.readByte().toInt()
        val category = if (flagsValue and ServerSoundStopPacket.flagHasCategory != 0) SoundCategory.values()[buffer.readVarInt()] else null
        val soundKey = if (flagsValue and ServerSoundStopPacket.flagHasSound != 0) buffer.readNamespacedKey() else null
        return ServerSoundStopPacket(category, soundKey)
    }
}
