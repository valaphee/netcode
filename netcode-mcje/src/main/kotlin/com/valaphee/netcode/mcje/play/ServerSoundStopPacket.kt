/*
 * MIT License
 *
 * Copyright (c) 2021, Valaphee.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.valaphee.netcode.mcje.play

import com.valaphee.netcode.mcje.Packet
import com.valaphee.netcode.mcje.PacketBuffer
import com.valaphee.netcode.mcje.PacketReader
import com.valaphee.netcode.mcje.ServerPlayPacketHandler
import com.valaphee.netcode.mcje.util.NamespacedKey
import com.valaphee.netcode.mcje.world.SoundCategory

/**
 * @author Kevin Ludwig
 */
class ServerSoundStopPacket(
    val category: SoundCategory?,
    val soundKey: NamespacedKey?
) : Packet<ServerPlayPacketHandler> {
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
