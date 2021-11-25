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

package com.valaphee.netcode.mcbe.base

import com.valaphee.foundry.math.Float3
import com.valaphee.foundry.math.Float4
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
class DebugRendererPacket(
    val type: Type,
    val markerText: String?,
    val markerPosition: Float3?,
    val markerColor: Float4?,
    val markerDuration: Long
) : Packet() {
    enum class Type {
        None,
        ClearDebugMarkers,
        AddDebugMarkerCube
    }

    override val id get() = 0xA4

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeIntLE(type.ordinal)
        if (type == Type.AddDebugMarkerCube) {
            buffer.writeString(markerText!!)
            buffer.writeFloat3(markerPosition!!)
            val (markerColorRed, markerColorGreen, markerColorBlue, markerColorAlpha) = markerColor!!
            buffer.writeFloatLE(markerColorRed)
            buffer.writeFloatLE(markerColorGreen)
            buffer.writeFloatLE(markerColorBlue)
            buffer.writeFloatLE(markerColorAlpha)
            buffer.writeLong(markerDuration)
        }
    }

    override fun handle(handler: PacketHandler) = handler.debugRenderer(this)

    override fun toString() = "DebugRendererPacket(type=$type, markerText=$markerText, markerPosition=$markerPosition, markerColor=$markerColor, markerDuration=$markerDuration)"
}

/**
 * @author Kevin Ludwig
 */
object DebugRendererPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): DebugRendererPacket {
        val type = DebugRendererPacket.Type.values()[buffer.readIntLE()]
        val markerText: String?
        val markerPosition: Float3?
        val markerColor: Float4?
        val markerDuration: Long
        if (type == DebugRendererPacket.Type.AddDebugMarkerCube) {
            markerText = buffer.readString()
            markerPosition = buffer.readFloat3()
            markerColor = Float4(buffer.readFloatLE(), buffer.readFloatLE(), buffer.readFloatLE(), buffer.readFloatLE())
            markerDuration = buffer.readLong()
        } else {
            markerText = null
            markerPosition = null
            markerColor = null
            markerDuration = 0
        }
        return DebugRendererPacket(type, markerText, markerPosition, markerColor, markerDuration)
    }
}
