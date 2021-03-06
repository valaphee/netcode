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
import com.valaphee.netcode.mcje.network.ClientPlayPacketHandler
import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.PacketReader
import com.valaphee.netcode.mcje.util.NamespacedKey

/**
 * @author Kevin Ludwig
 */
class ClientJigsawBlockUpdatePacket(
    val position: Int3,
    val name: NamespacedKey,
    val target: NamespacedKey,
    val pool: NamespacedKey,
    val finalState: String,
    val jointType: String
) : Packet<ClientPlayPacketHandler>() {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeInt3UnsignedY(position)
        buffer.writeNamespacedKey(name)
        buffer.writeNamespacedKey(target)
        buffer.writeNamespacedKey(pool)
        buffer.writeString(finalState)
        buffer.writeString(jointType)
    }

    override fun handle(handler: ClientPlayPacketHandler) = handler.jigsawBlockUpdate(this)

    override fun toString() = "ClientJigsawBlockUpdatePacket(position=$position, name=$name, target=$target, pool=$pool, finalState='$finalState', jointType='$jointType')"
}

/**
 * @author Kevin Ludwig
 */
object ClientJigsawBlockUpdatePacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ClientJigsawBlockUpdatePacket(buffer.readInt3UnsignedY(), buffer.readNamespacedKey(), buffer.readNamespacedKey(), buffer.readNamespacedKey(), buffer.readString(), buffer.readString())
}
