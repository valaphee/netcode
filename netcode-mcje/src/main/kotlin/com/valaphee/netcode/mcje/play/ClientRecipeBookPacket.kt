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
