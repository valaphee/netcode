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
import com.valaphee.netcode.mcje.ServerPlayPacketHandler
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
