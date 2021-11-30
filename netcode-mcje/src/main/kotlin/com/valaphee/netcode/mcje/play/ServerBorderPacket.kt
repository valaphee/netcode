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

/**
 * @author Kevin Ludwig
 */
class ServerBorderPacket(
    val action: Action,
    val x: Double,
    val z: Double,
    val oldDiameter: Double,
    val diameter: Double,
    val speed: Long,
    val portalTeleportBoundary: Int,
    val warningTime: Int,
    val warningDistance: Int,
) : Packet<ServerPlayPacketHandler> {
    enum class Action {
        SetSize, LeapSize, SetCenter, Initialize, SetWarningTime, SetWarningDistance
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(action.ordinal)
        when (action) {
            Action.SetSize -> buffer.writeDouble(diameter)
            Action.LeapSize -> {
                buffer.writeDouble(oldDiameter)
                buffer.writeDouble(diameter)
                buffer.writeVarLong(speed)
            }
            Action.SetCenter -> {
                buffer.writeDouble(x)
                buffer.writeDouble(z)
            }
            Action.Initialize -> {
                buffer.writeDouble(x)
                buffer.writeDouble(z)
                buffer.writeDouble(oldDiameter)
                buffer.writeDouble(diameter)
                buffer.writeVarLong(speed)
                buffer.writeVarInt(portalTeleportBoundary)
                buffer.writeVarInt(warningTime)
                buffer.writeVarInt(warningDistance)
            }
            Action.SetWarningTime -> buffer.writeVarInt(warningTime)
            Action.SetWarningDistance -> buffer.writeVarInt(warningDistance)
        }
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.border(this)

    override fun toString() = "ServerBorderPacket(action=$action, x=$x, z=$z, oldDiameter=$oldDiameter, diameter=$diameter, speed=$speed, portalTeleportBoundary=$portalTeleportBoundary, warningTime=$warningTime, warningDistance=$warningDistance)"
}

/**
 * @author Kevin Ludwig
 */
object ServerBorderPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): ServerBorderPacket {
        val action = ServerBorderPacket.Action.values()[buffer.readVarInt()]
        val x: Double
        val z: Double
        val oldDiameter: Double
        val diameter: Double
        val speed: Long
        val portalTeleportBoundary: Int
        val warningTime: Int
        val warningDistance: Int
        when (action) {
            ServerBorderPacket.Action.SetSize -> {
                x = 0.0
                z = 0.0
                oldDiameter = 0.0
                diameter = buffer.readDouble()
                speed = 0
                portalTeleportBoundary = 0
                warningTime = 0
                warningDistance = 0
            }
            ServerBorderPacket.Action.LeapSize -> {
                x = 0.0
                z = 0.0
                oldDiameter = buffer.readDouble()
                diameter = buffer.readDouble()
                speed = buffer.readVarLong()
                portalTeleportBoundary = 0
                warningTime = 0
                warningDistance = 0
            }
            ServerBorderPacket.Action.SetCenter -> {
                x = buffer.readDouble()
                z = buffer.readDouble()
                oldDiameter = 0.0
                diameter = 0.0
                speed = 0
                portalTeleportBoundary = 0
                warningTime = 0
                warningDistance = 0
            }
            ServerBorderPacket.Action.Initialize -> {
                x = buffer.readDouble()
                z = buffer.readDouble()
                oldDiameter = buffer.readDouble()
                diameter = buffer.readDouble()
                speed = buffer.readVarLong()
                portalTeleportBoundary = buffer.readVarInt()
                warningTime = buffer.readVarInt()
                warningDistance = buffer.readVarInt()
            }
            ServerBorderPacket.Action.SetWarningTime -> {
                x = 0.0
                z = 0.0
                oldDiameter = 0.0
                diameter = 0.0
                speed = 0
                portalTeleportBoundary = 0
                warningTime = buffer.readVarInt()
                warningDistance = 0
            }
            ServerBorderPacket.Action.SetWarningDistance -> {
                x = 0.0
                z = 0.0
                oldDiameter = 0.0
                diameter = 0.0
                speed = 0
                portalTeleportBoundary = 0
                warningTime = 0
                warningDistance = buffer.readVarInt()
            }
        }
        return ServerBorderPacket(action, x, z, oldDiameter, diameter, speed, portalTeleportBoundary, warningTime, warningDistance)
    }
}
