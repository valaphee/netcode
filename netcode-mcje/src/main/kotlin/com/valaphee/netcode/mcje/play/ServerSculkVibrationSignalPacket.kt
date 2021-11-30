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
import com.valaphee.netcode.mcje.Packet
import com.valaphee.netcode.mcje.PacketBuffer
import com.valaphee.netcode.mcje.PacketReader
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
) : Packet<ServerPlayPacketHandler> {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeInt3UnsignedY(sourcePosition)
        buffer.writeNamespacedKey(destinationIdentifier)
        buffer.writeInt3UnsignedY(destinationPosition)
        buffer.writeVarInt(destinationEntityId)
        buffer.writeVarInt(arrivalTicks)
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.sculkVibrationSignal(this)

    override fun toString() = "ServerSculkVibrationSignalPacket(sourcePosition=$sourcePosition, destinationIdentifier=$destinationIdentifier, destinationPosition=$destinationPosition, destinationEntityId=$destinationEntityId, arrivalTicks=$arrivalTicks)"
}

/**
 * @author Kevin Ludwig
 */
object ServerSculkVibrationSignalPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerSculkVibrationSignalPacket(buffer.readInt3UnsignedY(), buffer.readNamespacedKey(), buffer.readInt3UnsignedY(), buffer.readVarInt(), buffer.readVarInt())
}
