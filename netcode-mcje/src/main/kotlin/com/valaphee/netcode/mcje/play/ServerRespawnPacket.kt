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

import com.valaphee.netcode.mc.util.getString
import com.valaphee.netcode.mc.util.nbt.CompoundTag
import com.valaphee.netcode.mc.util.nbt.compoundTag
import com.valaphee.netcode.mcje.Packet
import com.valaphee.netcode.mcje.PacketBuffer
import com.valaphee.netcode.mcje.PacketReader
import com.valaphee.netcode.mcje.util.NamespacedKey
import com.valaphee.netcode.mcje.util.minecraftKey
import com.valaphee.netcode.mcje.world.GameMode

/**
 * @author Kevin Ludwig
 */
class ServerRespawnPacket(
    val dimension: NamespacedKey,
    val dimensionTag: CompoundTag?,
    val worldName: NamespacedKey?,
    val hashedSeed: Long,
    val gameMode: GameMode,
    val previousGameMode: GameMode,
    val debugGenerator: Boolean,
    val flatGenerator: Boolean,
    val keepMetadata: Boolean
) : Packet<ServerPlayPacketHandler> {
    override fun write(buffer: PacketBuffer, version: Int) {
        if (version >= 754) {
            buffer.toNbtOutputStream().use { it.writeTag(compoundTag()) }
            buffer.writeNamespacedKey(worldName!!)
            buffer.writeLong(hashedSeed)
        } else buffer.writeInt(buffer.registrySet.dimensions.getId(dimension) - 1)
        buffer.writeByte(gameMode.ordinal)
        if (version >= 754) {
            buffer.writeByte(previousGameMode.ordinal)
            buffer.writeBoolean(debugGenerator)
            buffer.writeBoolean(flatGenerator)
            buffer.writeBoolean(keepMetadata)
        } else buffer.writeString(if (flatGenerator) "flat" else "default")
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.respawn(this)

    override fun toString() = "ServerRespawnPacket(dimension=$dimension, dimensionTag=$dimensionTag, worldName=$worldName, hashedSeed=$hashedSeed, gameMode=$gameMode, previousGameMode=$previousGameMode, debugGenerator=$debugGenerator, flatGenerator=$flatGenerator, keepMetadata=$keepMetadata)"
}

/**
 * @author Kevin Ludwig
 */
object ServerRespawnPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): ServerRespawnPacket {
        val dimension: NamespacedKey
        val dimensionTag: CompoundTag?
        val worldName: NamespacedKey?
        val hashedSeed: Long
        if (version >= 754) {
            dimensionTag = buffer.toNbtInputStream().use { it.readTag()?.asCompoundTag() }!!.also { dimension = minecraftKey(it.getString("effects")) }
            worldName = buffer.readNamespacedKey()
            hashedSeed = buffer.readLong()
        } else {
            dimension = checkNotNull(buffer.registrySet.dimensions[buffer.readInt() + 1])
            dimensionTag = null
            worldName = null
            hashedSeed = 0
        }
        val gameMode = checkNotNull(GameMode.byIdOrNull(buffer.readUnsignedByte().toInt()))
        val previousGameMode: GameMode
        val debugGenerator: Boolean
        val flatGenerator: Boolean
        val keepMetadata: Boolean
        if (version >= 754) {
            previousGameMode = checkNotNull(GameMode.byIdOrNull(buffer.readUnsignedByte().toInt()))
            debugGenerator = buffer.readBoolean()
            flatGenerator = buffer.readBoolean()
            keepMetadata = buffer.readBoolean()
        } else {
            previousGameMode = gameMode
            debugGenerator = false
            flatGenerator = buffer.readString(16) == "flat"
            keepMetadata = false
        }
        return ServerRespawnPacket(dimension, dimensionTag, worldName, hashedSeed, gameMode, previousGameMode, debugGenerator, flatGenerator, keepMetadata)
    }
}
