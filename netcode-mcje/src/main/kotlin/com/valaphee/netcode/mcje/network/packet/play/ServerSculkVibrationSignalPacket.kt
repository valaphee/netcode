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

import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.ServerPlayPacketHandler
import com.valaphee.netcode.mcje.util.NamespacedKey

/**
 * @author Kevin Ludwig
 */
class ServerSculkVibrationSignalPacket(
    val sourcePosition: Int3,
    val destinationIdentifier: NamespacedKey,
    val destinationPosition: Int3,
    val destinationEntityId: Int,
    val arrivalTicks: Int
) : Packet<ServerPlayPacketHandler>() {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeBlockPosition(sourcePosition)
        buffer.writeNamespacedKey(destinationIdentifier)
        buffer.writeBlockPosition(destinationPosition)
        buffer.writeVarInt(destinationEntityId)
        buffer.writeVarInt(arrivalTicks)
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.sculkVibrationSignal(this)

    override fun toString() = "ServerSculkVibrationSignalPacket(sourcePosition=$sourcePosition, destinationIdentifier=$destinationIdentifier, destinationPosition=$destinationPosition, destinationEntityId=$destinationEntityId, arrivalTicks=$arrivalTicks)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = ServerSculkVibrationSignalPacket(buffer.readBlockPosition(), buffer.readNamespacedKey(), buffer.readBlockPosition(), buffer.readVarInt(), buffer.readVarInt())
    }
}
