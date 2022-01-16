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

import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.PacketReader

/**
 * @author Kevin Ludwig
 */
class ItemActionPacket(
    val itemId: Short,
    val action: Action
) : Packet() {
    enum class Action {
        None,
        Equip,
        Eat,
        Attack,
        Consume,
        Throw,
        Shoot,
        Place,
        FillBottle,
        FillBucket,
        PourBucket,
        UseTool,
        Interact,
        Retrieve,
        Dyed,
        Traded,
        Count
    }

    override val id get() = 0x8E

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeShortLE(itemId.toInt())
        buffer.writeIntLE(action.ordinal - 1)
    }

    override fun handle(handler: PacketHandler) = handler.itemAction(this)

    override fun toString() = "ItemActionPacket(itemId=$itemId, action=$action)"
}

/**
 * @author Kevin Ludwig
 */
object ItemActionPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ItemActionPacket(buffer.readShortLE(), ItemActionPacket.Action.values()[buffer.readIntLE() + 1])
}
