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
import com.valaphee.netcode.mcje.util.NamespacedKey
import com.valaphee.netcode.util.safeList

/**
 * @author Kevin Ludwig
 */
class ServerRecipeBookPacket(
    val action: Action,
    val craftingRecipeBookOpen: Boolean,
    val craftingRecipeBookFilterActive: Boolean,
    val smeltingRecipeBookOpen: Boolean,
    val smeltingRecipeBookFilterActive: Boolean,
    val blastingRecipeBookOpen: Boolean,
    val blastingRecipeBookFilterActive: Boolean,
    val smokingRecipeBookOpen: Boolean,
    val smokingRecipeBookFilterActive: Boolean,
    val recipeIds: List<NamespacedKey>?,
    val availableRecipeIds: List<NamespacedKey>?
) : Packet<ServerPlayPacketHandler> {
    enum class Action {
        Initialize, Add, Remove
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(action.ordinal)
        buffer.writeBoolean(craftingRecipeBookOpen)
        buffer.writeBoolean(craftingRecipeBookFilterActive)
        buffer.writeBoolean(smeltingRecipeBookOpen)
        buffer.writeBoolean(smeltingRecipeBookFilterActive)
        buffer.writeBoolean(blastingRecipeBookOpen)
        buffer.writeBoolean(blastingRecipeBookFilterActive)
        buffer.writeBoolean(smokingRecipeBookOpen)
        buffer.writeBoolean(smokingRecipeBookFilterActive)
        if (version >= 754) {
            buffer.writeVarInt(recipeIds!!.size)
            recipeIds.forEach { buffer.writeNamespacedKey(it) }
            if (action == Action.Initialize) {
                buffer.writeVarInt(availableRecipeIds!!.size)
                availableRecipeIds.forEach { buffer.writeNamespacedKey(it) }
            }
        }
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.recipeBook(this)

    override fun toString() = "ServerRecipeBookPacket(action=$action, craftingRecipeBookOpen=$craftingRecipeBookOpen, craftingRecipeBookFilterActive=$craftingRecipeBookFilterActive, smeltingRecipeBookOpen=$smeltingRecipeBookOpen, smeltingRecipeBookFilterActive=$smeltingRecipeBookFilterActive, blastingRecipeBookOpen=$blastingRecipeBookOpen, blastingRecipeBookFilterActive=$blastingRecipeBookFilterActive, smokingRecipeBookOpen=$smokingRecipeBookOpen, smokingRecipeBookFilterActive=$smokingRecipeBookFilterActive, recipeIds=$recipeIds, availableRecipeIds=$availableRecipeIds)"
}

/**
 * @author Kevin Ludwig
 */
object ServerRecipeBookPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): ServerRecipeBookPacket {
        val action = ServerRecipeBookPacket.Action.values()[buffer.readVarInt()]
        val craftingRecipeBookOpen = buffer.readBoolean()
        val craftingRecipeBookFilterActive = buffer.readBoolean()
        val smeltingRecipeBookOpen = buffer.readBoolean()
        val smeltingRecipeBookFilterActive = buffer.readBoolean()
        val blastingRecipeBookOpen = buffer.readBoolean()
        val blastingRecipeBookFilterActive = buffer.readBoolean()
        val smokingRecipeBookOpen = buffer.readBoolean()
        val smokingRecipeBookFilterActive = buffer.readBoolean()
        val recipeIds: List<NamespacedKey>?
        val availableRecipeIds: List<NamespacedKey>?
        if (version >= 754) {
            recipeIds = safeList(buffer.readVarInt()) { buffer.readNamespacedKey() }
            availableRecipeIds = if (action == ServerRecipeBookPacket.Action.Initialize) safeList(buffer.readVarInt()) { buffer.readNamespacedKey() } else null
        } else {
            recipeIds = null
            availableRecipeIds = null
        }
        return ServerRecipeBookPacket(action, craftingRecipeBookOpen, craftingRecipeBookFilterActive, smeltingRecipeBookOpen, smeltingRecipeBookFilterActive, blastingRecipeBookOpen, blastingRecipeBookFilterActive, smokingRecipeBookOpen, smokingRecipeBookFilterActive, recipeIds, availableRecipeIds)
    }
}
