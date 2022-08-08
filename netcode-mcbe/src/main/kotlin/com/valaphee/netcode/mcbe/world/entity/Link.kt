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
import com.valaphee.netcode.mcbe.network.V1_16_010

/**
 * @author Kevin Ludwig
 */
data class Link(
    val fromUniqueEntityId: Long,
    val toUniqueEntityId: Long,
    val type: Type,
    val immediate: Boolean,
    val driversAction: Boolean
) {
    enum class Type {
        Remove, Driver, Passenger
    }
}

fun PacketBuffer.readLink(version: Int) = Link(readVarLong(), readVarLong(), Link.Type.values()[readUnsignedByte().toInt()], readBoolean(), if (version >= V1_16_010) readBoolean() else false)

fun PacketBuffer.writeLink(value: Link, version: Int) {
    writeVarLong(value.fromUniqueEntityId)
    writeVarLong(value.toUniqueEntityId)
    writeByte(value.type.ordinal)
    writeBoolean(value.immediate)
    if (version >= V1_16_010)writeBoolean(value.driversAction)
}
