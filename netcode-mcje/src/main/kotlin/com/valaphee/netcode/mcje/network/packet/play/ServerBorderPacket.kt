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

import com.valaphee.foundry.math.Double2
import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.PacketReader
import com.valaphee.netcode.mcje.network.ServerPlayPacketHandler
import com.valaphee.netcode.mcje.network.V1_18_2

/**
 * @author Kevin Ludwig
 */
class ServerBorderPacket(
    val action: Action,
    val center: Double2?,
    val oldDiameter: Double,
    val diameter: Double,
    val speed: Long,
    val portalTeleportBoundary: Int,
    val warningTime: Int,
    val warningDistance: Int,
) : Packet<ServerPlayPacketHandler>() {
    enum class Action {
        SetSize, LerpSize, SetCenter, Initialize, SetWarningTime, SetWarningDistance
    }

    override val reader get() = when (action) {
        Action.Initialize -> ServerBorderInitializePacketReader
        Action.SetCenter -> ServerBorderSetCenterPacketReader
        Action.LerpSize -> ServerBorderLerpSizePacketReader
        Action.SetSize -> ServerBorderSetSizePacketReader
        Action.SetWarningTime -> ServerBorderSetWarningTimePacketReader
        Action.SetWarningDistance -> ServerBorderSetWarningDistancePacketReader
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        if (version < V1_18_2) buffer.writeVarInt(action.ordinal)
        when (action) {
            Action.SetSize -> buffer.writeDouble(diameter)
            Action.LerpSize -> {
                buffer.writeDouble(oldDiameter)
                buffer.writeDouble(diameter)
                buffer.writeVarLong(speed)
            }
            Action.SetCenter -> {
                buffer.writeDouble(center!!.x)
                buffer.writeDouble(center.y)
            }
            Action.Initialize -> {
                buffer.writeDouble(center!!.x)
                buffer.writeDouble(center.y)
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

    override fun toString() = "ServerBorderPacket(action=$action, center=$center, oldDiameter=$oldDiameter, diameter=$diameter, speed=$speed, portalTeleportBoundary=$portalTeleportBoundary, warningTime=$warningTime, warningDistance=$warningDistance)"
}

/**
 * @author Kevin Ludwig
 */
object ServerBorderPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): ServerBorderPacket {
        val action = ServerBorderPacket.Action.values()[buffer.readVarInt()]
        val center: Double2?
        val oldDiameter: Double
        val diameter: Double
        val speed: Long
        val portalTeleportBoundary: Int
        val warningTime: Int
        val warningDistance: Int
        when (action) {
            ServerBorderPacket.Action.SetSize -> {
                center = null
                oldDiameter = 0.0
                diameter = buffer.readDouble()
                speed = 0
                portalTeleportBoundary = 0
                warningTime = 0
                warningDistance = 0
            }
            ServerBorderPacket.Action.LerpSize -> {
                center = null
                oldDiameter = buffer.readDouble()
                diameter = buffer.readDouble()
                speed = buffer.readVarLong()
                portalTeleportBoundary = 0
                warningTime = 0
                warningDistance = 0
            }
            ServerBorderPacket.Action.SetCenter -> {
                center = Double2(buffer.readDouble(), buffer.readDouble())
                oldDiameter = 0.0
                diameter = 0.0
                speed = 0
                portalTeleportBoundary = 0
                warningTime = 0
                warningDistance = 0
            }
            ServerBorderPacket.Action.Initialize -> {
                center = Double2(buffer.readDouble(), buffer.readDouble())
                oldDiameter = buffer.readDouble()
                diameter = buffer.readDouble()
                speed = buffer.readVarLong()
                portalTeleportBoundary = buffer.readVarInt()
                warningTime = buffer.readVarInt()
                warningDistance = buffer.readVarInt()
            }
            ServerBorderPacket.Action.SetWarningTime -> {
                center = null
                oldDiameter = 0.0
                diameter = 0.0
                speed = 0
                portalTeleportBoundary = 0
                warningTime = buffer.readVarInt()
                warningDistance = 0
            }
            ServerBorderPacket.Action.SetWarningDistance -> {
                center = null
                oldDiameter = 0.0
                diameter = 0.0
                speed = 0
                portalTeleportBoundary = 0
                warningTime = 0
                warningDistance = buffer.readVarInt()
            }
        }
        return ServerBorderPacket(action, center, oldDiameter, diameter, speed, portalTeleportBoundary, warningTime, warningDistance)
    }
}

/**
 * @author Kevin Ludwig
 */
object ServerBorderInitializePacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerBorderPacket(ServerBorderPacket.Action.Initialize, Double2(buffer.readDouble(), buffer.readDouble()), buffer.readDouble(), buffer.readDouble(), buffer.readVarLong(), buffer.readVarInt(), buffer.readVarInt(), buffer.readVarInt())
}

/**
 * @author Kevin Ludwig
 */
object ServerBorderSetCenterPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerBorderPacket(ServerBorderPacket.Action.SetCenter, Double2(buffer.readDouble(), buffer.readDouble()), 0.0, 0.0, 0, 0, 0, 0)
}

/**
 * @author Kevin Ludwig
 */
object ServerBorderLerpSizePacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerBorderPacket(ServerBorderPacket.Action.LerpSize, null, buffer.readDouble(), buffer.readDouble(), buffer.readVarLong(), 0, 0, 0)
}

/**
 * @author Kevin Ludwig
 */
object ServerBorderSetSizePacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerBorderPacket(ServerBorderPacket.Action.SetSize, null, 0.0, buffer.readDouble(), 0, 0, 0, 0)
}

/**
 * @author Kevin Ludwig
 */
object ServerBorderSetWarningTimePacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerBorderPacket(ServerBorderPacket.Action.SetWarningTime, null, 0.0, 0.0, 0, 0, buffer.readVarInt(), 0)
}

/**
 * @author Kevin Ludwig
 */
object ServerBorderSetWarningDistancePacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerBorderPacket(ServerBorderPacket.Action.SetWarningDistance, null, 0.0, 0.0, 0, 0, 0, buffer.readVarInt())
}
