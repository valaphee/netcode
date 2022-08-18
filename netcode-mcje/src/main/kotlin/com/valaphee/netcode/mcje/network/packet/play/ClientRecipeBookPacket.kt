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
import com.valaphee.netcode.mcje.network.V1_14_0
import com.valaphee.netcode.mcje.util.NamespacedKey

/**
 * @author Kevin Ludwig
 */
class ClientRecipeBookPacket(
    val action: Action,
    val recipeId: Int,
    val recipeKey: NamespacedKey?,
    val craftingBookOpen: Boolean,
    val craftingBookFilterActive: Boolean,
    val smeltingBookOpen: Boolean,
    val smeltingBookFilterActive: Boolean,
    val blastingBookOpen: Boolean,
    val blastingBookFilterActive: Boolean,
    val smokingBookOpen: Boolean,
    val smokingBookFilterActive: Boolean
) : Packet<ClientPlayPacketHandler>() {
    enum class Action {
        Display, State
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(action.ordinal)
        when (action) {
            Action.Display -> if (version >= V1_13_0) buffer.writeNamespacedKey(recipeKey!!) else buffer.writeInt(recipeId)
            Action.State -> {
                buffer.writeBoolean(craftingBookOpen)
                buffer.writeBoolean(craftingBookFilterActive)
                if (version >= V1_13_0) {
                    buffer.writeBoolean(smeltingBookOpen)
                    buffer.writeBoolean(smeltingBookFilterActive)
                    if (version >= V1_14_0) {
                        buffer.writeBoolean(blastingBookOpen)
                        buffer.writeBoolean(blastingBookFilterActive)
                        buffer.writeBoolean(smokingBookOpen)
                        buffer.writeBoolean(smokingBookFilterActive)
                    }
                }
            }
        }
    }

    override fun handle(handler: ClientPlayPacketHandler) = handler.recipeBook(this)

    override fun toString() = "ClientRecipeBookPacket(action=$action, recipeId=$recipeId, recipeKey=$recipeKey, craftingBookOpen=$craftingBookOpen, craftingBookFilterActive=$craftingBookFilterActive, smeltingBookOpen=$smeltingBookOpen, smeltingBookFilterActive=$smeltingBookFilterActive, blastingBookOpen=$blastingBookOpen, blastingBookFilterActive=$blastingBookFilterActive, smokingBookOpen=$smokingBookOpen, smokingBookFilterActive=$smokingBookFilterActive)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int): ClientRecipeBookPacket {
            val action = Action.values()[buffer.readVarInt()]
            val recipeId: Int
            val recipeKey: NamespacedKey?
            val craftingBookOpen: Boolean
            val craftingBookFilterActive: Boolean
            val smeltingBookOpen: Boolean
            val smeltingBookFilterActive: Boolean
            val blastingBookOpen: Boolean
            val blastingBookFilterActive: Boolean
            val smokingBookOpen: Boolean
            val smokingBookFilterActive: Boolean
            when (action) {
                Action.Display -> {
                    if (version >= V1_13_0) {
                        recipeId = 0
                        recipeKey = buffer.readNamespacedKey()
                    } else {
                        recipeId = buffer.readInt()
                        recipeKey = null
                    }
                    craftingBookOpen = false
                    craftingBookFilterActive = false
                    smeltingBookOpen = false
                    smeltingBookFilterActive = false
                    blastingBookOpen = false
                    blastingBookFilterActive = false
                    smokingBookOpen = false
                    smokingBookFilterActive = false
                }
                Action.State -> {
                    recipeId = 0
                    recipeKey = null
                    craftingBookOpen = buffer.readBoolean()
                    craftingBookFilterActive = buffer.readBoolean()
                    if (version >= V1_13_0) {
                        smeltingBookOpen = buffer.readBoolean()
                        smeltingBookFilterActive = buffer.readBoolean()
                        if (version >= V1_14_0) {
                            blastingBookOpen = buffer.readBoolean()
                            blastingBookFilterActive = buffer.readBoolean()
                            smokingBookOpen = buffer.readBoolean()
                            smokingBookFilterActive = buffer.readBoolean()
                        } else {
                            blastingBookOpen = false
                            blastingBookFilterActive = false
                            smokingBookOpen = false
                            smokingBookFilterActive = false
                        }
                    } else {
                        smeltingBookOpen = false
                        smeltingBookFilterActive = false
                        blastingBookOpen = false
                        blastingBookFilterActive = false
                        smokingBookOpen = false
                        smokingBookFilterActive = false
                    }
                }
            }
            return ClientRecipeBookPacket(action, recipeId, recipeKey, craftingBookOpen, craftingBookFilterActive, smeltingBookOpen, smeltingBookFilterActive, blastingBookOpen, blastingBookFilterActive, smokingBookOpen, smokingBookFilterActive)
        }
    }
}
