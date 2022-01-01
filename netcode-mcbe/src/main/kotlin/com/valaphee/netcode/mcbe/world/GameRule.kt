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

import com.valaphee.netcode.mcbe.PacketBuffer

/**
 * @author Kevin Ludwig
 */
data class GameRule<T>(
    val name: String,
    val editable: Boolean,
    val value: T
)

fun PacketBuffer.readGameRulePre440(): GameRule<*> {
    val name = readString()
    return when (readVarUInt()) {
        1 -> GameRule(name, true, readBoolean())
        2 -> GameRule(name, true, readVarUInt())
        3 -> GameRule(name, true, readFloatLE())
        else -> TODO()
    }
}

fun PacketBuffer.readGameRule(): GameRule<*> {
    val name = readString()
    val editable = readBoolean()
    return when (readVarUInt()) {
        1 -> GameRule(name, editable, readBoolean())
        2 -> GameRule(name, editable, readVarUInt())
        3 -> GameRule(name, editable, readFloatLE())
        else -> TODO()
    }
}

fun PacketBuffer.writeGameRulePre440(value: GameRule<*>) {
    writeString(value.name)
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
        else -> TODO()
    }
}

fun PacketBuffer.writeGameRule(value: GameRule<*>) {
    writeString(value.name)
    writeBoolean(value.editable)
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
        else -> TODO()
    }
}
