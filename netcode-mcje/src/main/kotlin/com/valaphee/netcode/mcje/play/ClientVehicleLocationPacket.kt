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

package com.valaphee.netcode.mcje.play

import com.valaphee.foundry.math.Double3
import com.valaphee.foundry.math.Float2
import com.valaphee.netcode.mcje.Packet
import com.valaphee.netcode.mcje.PacketBuffer
import com.valaphee.netcode.mcje.PacketReader

/**
 * @author Kevin Ludwig
 */
class ClientVehicleLocationPacket(
    val position: Double3,
    val rotation: Float2
) : Packet<ClientPlayPacketHandler> {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeDouble3(position)
        buffer.writeFloat2(rotation)
    }

    override fun handle(handler: ClientPlayPacketHandler) = handler.vehicleLocation(this)

    override fun toString() = "ClientVehicleLocationPacket(position=$position, rotation=$rotation)"
}

/**
 * @author Kevin Ludwig
 */
object ClientVehicleLocationPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ClientVehicleLocationPacket(buffer.readDouble3(), buffer.readFloat2())
}
