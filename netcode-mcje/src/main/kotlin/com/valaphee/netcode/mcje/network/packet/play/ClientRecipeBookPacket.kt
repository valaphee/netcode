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
    val states: Map<Type, State>,
) : Packet<ClientPlayPacketHandler>() {
    enum class Action {
        Display, SetState
    }

    enum class Type {
        Crafting, Smelting, Blasting, Smoking
    }

    data class State(
        val open: Boolean,
        val filterActive: Boolean
    )

    override val reader get() = when (action) {
        Action.Display -> DisplayReader
        Action.SetState -> SetStateReader
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(action.ordinal)
        when (action) {
            Action.Display -> if (version >= V1_13_0) buffer.writeNamespacedKey(recipeKey!!) else buffer.writeInt(recipeId)
            Action.SetState -> {
                buffer.writeBoolean(states[Type.Crafting]!!.open)
                buffer.writeBoolean(states[Type.Crafting]!!.filterActive)
                if (version >= V1_13_0) {
                    buffer.writeBoolean(states[Type.Smelting]!!.open)
                    buffer.writeBoolean(states[Type.Smelting]!!.filterActive)
                    if (version >= V1_14_0) {
                        buffer.writeBoolean(states[Type.Blasting]!!.open)
                        buffer.writeBoolean(states[Type.Blasting]!!.filterActive)
                        buffer.writeBoolean(states[Type.Smoking]!!.open)
                        buffer.writeBoolean(states[Type.Smoking]!!.filterActive)
                    }
                }
            }
        }
    }

    override fun handle(handler: ClientPlayPacketHandler) = handler.recipeBook(this)

    override fun toString() = "ClientRecipeBookPacket(action=$action, recipeId=$recipeId, recipeKey=$recipeKey, states=$states)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int): ClientRecipeBookPacket {
            val action = Action.values()[buffer.readVarInt()]
            val recipeId: Int
            val recipeKey: NamespacedKey?
            val states = mutableMapOf<Type, State>()
            when (action) {
                Action.Display -> {
                    if (version >= V1_13_0) {
                        recipeId = 0
                        recipeKey = buffer.readNamespacedKey()
                    } else {
                        recipeId = buffer.readInt()
                        recipeKey = null
                    }
                }
                Action.SetState -> {
                    recipeId = 0
                    recipeKey = null
                    states[Type.Crafting] = State(buffer.readBoolean(), buffer.readBoolean())
                    if (version >= V1_13_0) {
                        states[Type.Smelting] = State(buffer.readBoolean(), buffer.readBoolean())
                        if (version >= V1_14_0) {
                            states[Type.Blasting] = State(buffer.readBoolean(), buffer.readBoolean())
                            states[Type.Smoking] = State(buffer.readBoolean(), buffer.readBoolean())
                        }
                    }
                }
            }
            return ClientRecipeBookPacket(action, recipeId, recipeKey, states)
        }
    }

    object DisplayReader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = ClientRecipeBookPacket(Action.Display, 0, buffer.readNamespacedKey(), emptyMap())
    }

    object SetStateReader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = ClientRecipeBookPacket(Action.SetState, 0, null, mutableMapOf(Type.values()[buffer.readVarInt()] to State(buffer.readBoolean(), buffer.readBoolean())))
    }
}
