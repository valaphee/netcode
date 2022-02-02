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

package com.valaphee.netcode.mcbe.command

import com.valaphee.netcode.mcbe.network.PacketBuffer
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
data class Origin(
    val where: Where,
    val requestId: UUID,
    val purpose: String,
    val eventId: Long
) {
    enum class Where {
        Player,
        Block,
        MinecartBlock,
        DeveloperConsole,
        Test,
        AutomationPlayer,
        ClientAutomation,
        DedicatedServer,
        Entity,
        Virtual,
        GameArgument,
        EntityServer,
        PreCompiled,
        GameMasterEntityServer,
        Script
    }
}

fun PacketBuffer.readOrigin(): Origin {
    val where = Origin.Where.values()[readVarUInt()]
    return Origin(where, readUuid(), readString(), if (Origin.Where.DeveloperConsole == where || Origin.Where.Test == where) readVarLong() else 0)
}

fun PacketBuffer.writeOrigin(value: Origin) {
    writeVarUInt(value.where.ordinal)
    writeUuid(value.requestId)
    writeString(value.purpose)
    if (value.where == Origin.Where.DeveloperConsole || value.where == Origin.Where.Test) writeVarLong(value.eventId)
}
