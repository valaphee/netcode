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
