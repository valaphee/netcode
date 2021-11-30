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

import com.valaphee.netcode.mcje.ClientPlayPacketHandler
import com.valaphee.netcode.mcje.Packet
import com.valaphee.netcode.mcje.PacketBuffer
import com.valaphee.netcode.mcje.PacketReader

/**
 * @author Kevin Ludwig
 */
class ClientRecipeBookStatePacket(
    val recipeBook: RecipeBook,
    val recipeBookOpen: Boolean,
    val recipeBookFilterActive: Boolean
) : Packet<ClientPlayPacketHandler> {
    enum class RecipeBook {
        Crafting, Smelting, Blasting, Smoking
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(recipeBook.ordinal)
        buffer.writeBoolean(recipeBookOpen)
        buffer.writeBoolean(recipeBookFilterActive)
    }

    override fun handle(handler: ClientPlayPacketHandler) = handler.recipeBookState(this)

    override fun toString() = "ClientRecipeBookStatePacket(recipeBook=$recipeBook, recipeBookOpen=$recipeBookOpen, recipeBookFilterActive=$recipeBookFilterActive)"
}

/**
 * @author Kevin Ludwig
 */
object ClientRecipeBookStatePacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ClientRecipeBookStatePacket(ClientRecipeBookStatePacket.RecipeBook.values()[buffer.readVarInt()], buffer.readBoolean(), buffer.readBoolean())
}
