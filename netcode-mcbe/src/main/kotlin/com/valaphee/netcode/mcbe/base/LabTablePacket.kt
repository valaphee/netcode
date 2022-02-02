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

package com.valaphee.netcode.mcbe.base

import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mcbe.Packet
import com.valaphee.netcode.mcbe.PacketBuffer
import com.valaphee.netcode.mcbe.PacketHandler
import com.valaphee.netcode.mcbe.PacketReader

/**
 * @author Kevin Ludwig
 */
class LabTablePacket(
    val action: Action,
    val position: Int3,
    val reactionType: ReactionType
) : Packet() {
    enum class Action {
        Combine, React, Reset
    }

    enum class ReactionType {
        None,
        IceBomb,
        Bleach,
        ElephantToothpaste,
        Fertilizer,
        HeatBlock,
        MagnesiumSalts,
        MiscFire,
        MiscExplosion,
        MiscLava,
        MiscMystical,
        MiscSmoke,
        MiscLargeSmoke
    }

    override val id get() = 0x6D

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeByte(action.ordinal)
        buffer.writeInt3(position)
        buffer.writeByte(reactionType.ordinal)
    }

    override fun handle(handler: PacketHandler) = handler.labTable(this)

    override fun toString() = "LabTablePacket(action=$action, position=$position, reactionType=$reactionType)"
}

/**
 * @author Kevin Ludwig
 */
object LabTablePacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = LabTablePacket(LabTablePacket.Action.values()[buffer.readByte().toInt()], buffer.readInt3(), LabTablePacket.ReactionType.values()[buffer.readByte().toInt()])
}
