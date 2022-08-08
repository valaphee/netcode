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

package com.valaphee.netcode.mcbe.world

import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.V1_17_002
import kotlin.reflect.jvm.jvmName

/**
 * @author Kevin Ludwig
 */
data class GameRule<T>(
    val name: String,
    val editable: Boolean,
    val value: T
)

fun PacketBuffer.readGameRule(version: Int): GameRule<*> {
    val name = readString()
    val editable = if (version >= V1_17_002) readBoolean() else true
    return when (val type = readVarUInt()) {
        1 -> GameRule(name, editable, readBoolean())
        2 -> GameRule(name, editable, readVarUInt())
        3 -> GameRule(name, editable, readFloatLE())
        else -> error("No such gamerule type: $type (name: $name)")
    }
}

fun PacketBuffer.writeGameRule(value: GameRule<*>, version: Int) {
    writeString(value.name)
    if (version >= V1_17_002) writeBoolean(value.editable)
    when (value.value) {
        is Boolean -> {
            writeVarUInt(1)
            writeBoolean(value.value)
        }
        is Int -> {
            writeVarUInt(2)
            writeVarUInt(value.value)
        }
        is Float -> {
            writeVarUInt(3)
            writeFloatLE(value.value)
        }
        else -> error("Unsupported gamerule type: ${value.value!!::class.jvmName}")
    }
}
