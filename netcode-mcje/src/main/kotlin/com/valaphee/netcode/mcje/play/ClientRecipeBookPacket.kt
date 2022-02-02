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

import com.valaphee.netcode.mcje.Packet
import com.valaphee.netcode.mcje.PacketBuffer
import com.valaphee.netcode.mcje.PacketReader
import com.valaphee.netcode.mcje.util.NamespacedKey

/**
 * @author Kevin Ludwig
 */
class ClientRecipeBookPacket(
    val action: Action,
    val recipeId: NamespacedKey?,
    val craftingRecipeBookOpen: Boolean,
    val craftingRecipeBookFilterActive: Boolean,
    val smeltingRecipeBookOpen: Boolean,
    val smeltingRecipeBookFilterActive: Boolean,
    val blastingRecipeBookOpen: Boolean,
    val blastingRecipeBookFilterActive: Boolean,
    val smokingRecipeBookOpen: Boolean,
    val smokingRecipeBookFilterActive: Boolean
) : Packet<ClientPlayPacketHandler> {
    enum class Action {
        DisplayRecipe, State
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(action.ordinal)
        when (action) {
            Action.DisplayRecipe -> buffer.writeNamespacedKey(recipeId!!)
            Action.State -> {
                buffer.writeBoolean(craftingRecipeBookOpen)
                buffer.writeBoolean(craftingRecipeBookFilterActive)
                buffer.writeBoolean(smeltingRecipeBookOpen)
                buffer.writeBoolean(smeltingRecipeBookFilterActive)
                buffer.writeBoolean(blastingRecipeBookOpen)
                buffer.writeBoolean(blastingRecipeBookFilterActive)
                buffer.writeBoolean(smokingRecipeBookOpen)
                buffer.writeBoolean(smokingRecipeBookFilterActive)
            }
        }
    }

    override fun handle(handler: ClientPlayPacketHandler) = handler.recipeBook(this)

    override fun toString() = "ClientRecipeBookPacket(action=$action, recipeId=$recipeId, craftingRecipeBookOpen=$craftingRecipeBookOpen, craftingRecipeBookFilterActive=$craftingRecipeBookFilterActive, smeltingRecipeBookOpen=$smeltingRecipeBookOpen, smeltingRecipeBookFilterActive=$smeltingRecipeBookFilterActive, blastingRecipeBookOpen=$blastingRecipeBookOpen, blastingRecipeBookFilterActive=$blastingRecipeBookFilterActive, smokingRecipeBookOpen=$smokingRecipeBookOpen, smokingRecipeBookFilterActive=$smokingRecipeBookFilterActive)"
}

/**
 * @author Kevin Ludwig
 */
object ClientRecipeBookPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): ClientRecipeBookPacket {
        val action = ClientRecipeBookPacket.Action.values()[buffer.readVarInt()]
        val recipeId: NamespacedKey?
        val craftingRecipeBookOpen: Boolean
        val craftingRecipeBookFilterActive: Boolean
        val smeltingRecipeBookOpen: Boolean
        val smeltingRecipeBookFilterActive: Boolean
        val blastingRecipeBookOpen: Boolean
        val blastingRecipeBookFilterActive: Boolean
        val smokingRecipeBookOpen: Boolean
        val smokingRecipeBookFilterActive: Boolean
        when (action) {
            ClientRecipeBookPacket.Action.DisplayRecipe -> {
                recipeId = buffer.readNamespacedKey()
                craftingRecipeBookOpen = false
                craftingRecipeBookFilterActive = false
                smeltingRecipeBookOpen = false
                smeltingRecipeBookFilterActive = false
                blastingRecipeBookOpen = false
                blastingRecipeBookFilterActive = false
                smokingRecipeBookOpen = false
                smokingRecipeBookFilterActive = false
            }
            ClientRecipeBookPacket.Action.State -> {
                recipeId = null
                craftingRecipeBookOpen = buffer.readBoolean()
                craftingRecipeBookFilterActive = buffer.readBoolean()
                smeltingRecipeBookOpen = buffer.readBoolean()
                smeltingRecipeBookFilterActive = buffer.readBoolean()
                blastingRecipeBookOpen = buffer.readBoolean()
                blastingRecipeBookFilterActive = buffer.readBoolean()
                smokingRecipeBookOpen = buffer.readBoolean()
                smokingRecipeBookFilterActive = buffer.readBoolean()
            }
        }
        return ClientRecipeBookPacket(action, recipeId, craftingRecipeBookOpen, craftingRecipeBookFilterActive, smeltingRecipeBookOpen, smeltingRecipeBookFilterActive, blastingRecipeBookOpen, blastingRecipeBookFilterActive, smokingRecipeBookOpen, smokingRecipeBookFilterActive)
    }
}
