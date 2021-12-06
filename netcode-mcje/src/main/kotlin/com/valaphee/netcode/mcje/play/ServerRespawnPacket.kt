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

import com.valaphee.netcode.mc.util.nbt.Tag
import com.valaphee.netcode.mcje.Packet
import com.valaphee.netcode.mcje.PacketBuffer
import com.valaphee.netcode.mcje.PacketReader
import com.valaphee.netcode.mcje.util.NamespacedKey
import com.valaphee.netcode.mcje.world.GameMode

/**
 * @author Kevin Ludwig
 */
class ServerRespawnPacket(
    val dimension: Tag?,
    val worldName: NamespacedKey?,
    val hashedSeed: Long,
    val gameMode: GameMode,
    val previousGameMode: GameMode,
    val debugGenerator: Boolean,
    val flatGenerator: Boolean,
    val keepMetadata: Boolean
) : Packet<ServerPlayPacketHandler> {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.toNbtOutputStream().use { it.writeTag(dimension) }
        buffer.writeNamespacedKey(worldName!!)
        buffer.writeLong(hashedSeed)
        buffer.writeByte(gameMode.ordinal)
        buffer.writeByte(previousGameMode.ordinal)
        buffer.writeBoolean(debugGenerator)
        buffer.writeBoolean(flatGenerator)
        buffer.writeBoolean(keepMetadata)
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.respawn(this)

    override fun toString() = "ServerRespawnPacket(dimension=$dimension, worldName=$worldName, hashedSeed=$hashedSeed, gameMode=$gameMode, previousGameMode=$previousGameMode, debugGenerator=$debugGenerator, flatGenerator=$flatGenerator, keepMetadata=$keepMetadata)"
}

/**
 * @author Kevin Ludwig
 */
object ServerRespawnPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerRespawnPacket(buffer.toNbtInputStream().use { it.readTag() }, buffer.readNamespacedKey(), buffer.readLong(), checkNotNull(GameMode.byIdOrNull(buffer.readUnsignedByte().toInt())), checkNotNull(GameMode.byIdOrNull(buffer.readUnsignedByte().toInt())), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean())
}
