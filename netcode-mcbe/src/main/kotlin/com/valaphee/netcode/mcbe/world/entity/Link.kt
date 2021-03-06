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

package com.valaphee.netcode.mcbe.world.entity

import com.valaphee.netcode.mcbe.network.PacketBuffer

/**
 * @author Kevin Ludwig
 */
data class Link(
    val type: Type,
    val fromUniqueEntityId: Long,
    val toUniqueEntityId: Long,
    val immediate: Boolean,
    val driversAction: Boolean = false
) {
    enum class Type {
        Remove, Driver, Passenger
    }
}

fun PacketBuffer.readLinkPre407(): Link {
    val fromUniqueEntityId = readVarLong()
    val toUniqueEntityId = readVarLong()
    val type = Link.Type.values()[readUnsignedByte().toInt()]
    return Link(type, fromUniqueEntityId, toUniqueEntityId, readBoolean())
}

fun PacketBuffer.readLink(): Link {
    val fromUniqueEntityId = readVarLong()
    val toUniqueEntityId = readVarLong()
    val type = Link.Type.values()[readUnsignedByte().toInt()]
    return Link(type, fromUniqueEntityId, toUniqueEntityId, readBoolean(), readBoolean())
}

fun PacketBuffer.writeLinkPre407(value: Link) {
    writeVarLong(value.fromUniqueEntityId)
    writeVarLong(value.toUniqueEntityId)
    writeByte(value.type.ordinal)
    writeBoolean(value.immediate)
}

fun PacketBuffer.writeLink(value: Link) {
    writeLinkPre407(value)
    writeBoolean(value.driversAction)
}
