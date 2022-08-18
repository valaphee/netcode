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
import com.valaphee.netcode.mcje.network.V1_13_0
import com.valaphee.netcode.mcje.util.NamespacedKey

/**
 * @author Kevin Ludwig
 */
class ClientCraftPacket(
    val windowId: Int,
    val recipeId: Int,
    val recipeKey: NamespacedKey?,
    val all: Boolean
) : Packet<ClientPlayPacketHandler>() {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeByte(windowId)
        if (version >= V1_13_0) buffer.writeNamespacedKey(recipeKey!!) else buffer.writeVarInt(recipeId)
        buffer.writeBoolean(all)
    }

    override fun handle(handler: ClientPlayPacketHandler) = handler.craft(this)

    override fun toString() = "ClientCraftPacket(windowId=$windowId, recipeId=$recipeId, recipeKey=$recipeKey, all=$all)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int): ClientCraftPacket {
            val windowId = buffer.readUnsignedByte().toInt()
            val recipeId: Int
            val recipeKey: NamespacedKey?
            if (version >= V1_13_0) {
                recipeId = 0
                recipeKey = buffer.readNamespacedKey()
            } else {
                recipeId = buffer.readVarInt()
                recipeKey = null
            }
            val all = buffer.readBoolean()
            return ClientCraftPacket(windowId, recipeId, recipeKey, all)
        }
    }
}
