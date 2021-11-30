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

import com.valaphee.netcode.mc.util.nbt.CompoundTag
import com.valaphee.netcode.mcje.Packet
import com.valaphee.netcode.mcje.PacketBuffer
import com.valaphee.netcode.mcje.PacketReader
import com.valaphee.netcode.mcje.util.NamespacedKey
import com.valaphee.netcode.mcje.world.GameMode

/**
 * @author Kevin Ludwig
 */
class ServerWorldPacket(
    val entityId: Int,
    val hardcore: Boolean,
    val gameMode: GameMode,
    val previousGameMode: GameMode,
    val worldNames: Array<NamespacedKey>,
    val dimensionCodec: CompoundTag?,
    val dimension: CompoundTag?,
    val worldName: NamespacedKey,
    val hashedSeed: Long,
    val maximumPlayers: Int,
    val viewDistance: Int,
    val showCoordinates: Boolean,
    val immediateRespawn: Boolean,
    val debugGenerator: Boolean,
    val flatGenerator: Boolean,
) : Packet<ServerPlayPacketHandler> {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeInt(entityId)
        buffer.writeBoolean(hardcore)
        buffer.writeByte(gameMode.ordinal)
        buffer.writeByte(previousGameMode.ordinal)
        buffer.writeVarInt(worldNames.size)
        worldNames.forEach { buffer.writeNamespacedKey(it) }
        buffer.toNbtOutputStream().use { it.writeTag(dimensionCodec) }
        buffer.toNbtOutputStream().use { it.writeTag(dimension) }
        buffer.writeNamespacedKey(worldName)
        buffer.writeLong(hashedSeed)
        buffer.writeVarInt(maximumPlayers)
        buffer.writeVarInt(viewDistance)
        buffer.writeBoolean(!showCoordinates)
        buffer.writeBoolean(!immediateRespawn)
        buffer.writeBoolean(debugGenerator)
        buffer.writeBoolean(flatGenerator)
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.world(this)

    override fun toString() = "ServerWorldPacket(entityId=$entityId, hardcore=$hardcore, gameMode=$gameMode, previousGameMode=$previousGameMode, worldNames=${worldNames.contentToString()}, dimensionCodec=$dimensionCodec, dimension=$dimension, worldName=$worldName, hashedSeed=$hashedSeed, maximumPlayers=$maximumPlayers, viewDistance=$viewDistance, showCoordinates=$showCoordinates, immediateRespawn=$immediateRespawn, debugGenerator=$debugGenerator, flatGenerator=$flatGenerator)"
}

/**
 * @author Kevin Ludwig
 */
object ServerWorldPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerWorldPacket(buffer.readInt(), buffer.readBoolean(), GameMode.byIdOrNull(buffer.readByte().toInt())!!, GameMode.byIdOrNull(buffer.readByte().toInt())!!, Array(buffer.readVarInt()) { buffer.readNamespacedKey() }, buffer.toNbtInputStream().use { it.readTag()?.asCompoundTag() }, buffer.toNbtInputStream().use { it.readTag()?.asCompoundTag() }, buffer.readNamespacedKey(), buffer.readLong(), buffer.readVarInt(), buffer.readVarInt(), !buffer.readBoolean(), !buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean())
}
