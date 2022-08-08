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

import com.valaphee.netcode.mcje.network.ClientPlayPacketHandler
import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.PacketReader
import com.valaphee.netcode.mcje.network.V1_12_0
import com.valaphee.netcode.mcje.network.V1_13_0
import com.valaphee.netcode.mcje.util.NamespacedKey

/**
 * @author Kevin Ludwig
 */
class ClientCraftPacket(
    val windowId: Int,
    val recipe: Pair<Int, NamespacedKey?>,
    val all: Boolean
) : Packet<ClientPlayPacketHandler>() {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeByte(windowId)
        if (version in V1_12_0 until V1_13_0) buffer.writeVarInt(recipe.first) else buffer.writeNamespacedKey(recipe.second!!)
        buffer.writeBoolean(all)
    }

    override fun handle(handler: ClientPlayPacketHandler) = handler.craft(this)

    override fun toString() = "ClientCraftPacket(windowId=$windowId, recipe=$recipe, all=$all)"
}

/**
 * @author Kevin Ludwig
 */
object ClientCraftPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ClientCraftPacket(buffer.readUnsignedByte().toInt(), if (version in V1_12_0 until V1_13_0) buffer.readVarInt() to null else 0 to buffer.readNamespacedKey(), buffer.readBoolean())
}
