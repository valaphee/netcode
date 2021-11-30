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

import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mcje.ClientPlayPacketHandler
import com.valaphee.netcode.mcje.Packet
import com.valaphee.netcode.mcje.PacketBuffer

/**
 * @author Kevin Ludwig
 */
class ClientStructureBlockUpdatePacket(
    val position: Int3,
    val action: Action,
    val mode: Mode,
    val name: String,
    val offset: Int3,
    val size: Int3,
    val mirror: Mirror,
    val rotation: Rotation,
    val metadata: String,
    val integrityValue: Float,
    val integritySeed: Long,
    val ignoreEntities: Boolean,
    val showAir: Boolean,
    val showBoundingBox: Boolean
) : Packet<ClientPlayPacketHandler> {
    enum class Action {
        Update, Save, Load, DetectSize
    }

    enum class Mode {
        Save, Load, Corner, Data
    }

    enum class Mirror {
        None, LeftRight, FrontBack
    }

    enum class Rotation {
        None, Clockwise90, Reverse, Counterclockwise90
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeInt3UnsignedY(position)
        buffer.writeVarInt(action.ordinal)
        buffer.writeVarInt(mode.ordinal)
        buffer.writeString(name)
        buffer.writeByte(offset.x)
        buffer.writeByte(offset.y)
        buffer.writeByte(offset.z)
        buffer.writeByte(size.x)
        buffer.writeByte(size.y)
        buffer.writeByte(size.z)
        buffer.writeVarInt(mirror.ordinal)
        buffer.writeVarInt(rotation.ordinal)
        buffer.writeString(metadata)
        buffer.writeFloat(integrityValue)
        buffer.writeVarLong(integritySeed)
        var flagsValue = if (ignoreEntities) flagIgnoreEntities else 0
        flagsValue = flagsValue or if (showAir) flagShowAir else 0
        flagsValue = flagsValue or if (showBoundingBox) flagShowBoundingBox else 0
        buffer.writeByte(flagsValue)
    }

    override fun handle(handler: ClientPlayPacketHandler) = handler.structureBlockUpdate(this)

    override fun toString() = "ClientStructureBlockUpdatePacket(position=$position, action=$action, mode=$mode, name='$name', offset=$offset, size=$size, mirror=$mirror, rotation=$rotation, metadata='$metadata', integrityValue=$integrityValue, integritySeed=$integritySeed, ignoreEntities=$ignoreEntities, showAir=$showAir, showBoundingBox=$showBoundingBox)"

    companion object {
        private const val flagIgnoreEntities = 1
        private const val flagShowAir = 1 shl 1
        private const val flagShowBoundingBox = 1 shl 2
    }
}
