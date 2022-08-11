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

package com.valaphee.netcode.mcbe.network.packet

import com.valaphee.foundry.math.Float3
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler

/**
 * @author Kevin Ludwig
 */
class InteractPacket(
    val action: Action,
    val runtimeEntityId: Long,
    val mousePosition: Float3?
) : Packet() {
    enum class Action {
        None, Interact, Damage, LeaveVehicle, Mouseover, NpcOpen, OpenInventory
    }

    override val id get() = 0x21

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeByte(action.ordinal)
        buffer.writeVarULong(runtimeEntityId)
        if (action == Action.LeaveVehicle || action == Action.Mouseover) buffer.writeFloat3(mousePosition!!)
    }

    override fun handle(handler: PacketHandler) = handler.interact(this)

    override fun toString() = "InteractPacket(action=$action, runtimeEntityId=$runtimeEntityId, mousePosition=$mousePosition)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int): InteractPacket {
            val action = InteractPacket.Action.values()[buffer.readByte().toInt()]
            val runtimeEntityId = buffer.readVarULong()
            val mousePosition = if (action == Action.LeaveVehicle || action == Action.Mouseover) buffer.readFloat3() else null
            return InteractPacket(action, runtimeEntityId, mousePosition)
        }
    }
}
