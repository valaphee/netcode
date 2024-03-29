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
import com.valaphee.netcode.util.LazyList

/**
 * @author Kevin Ludwig
 */
data class Message(
    val success: Boolean,
    val message: String,
    val arguments: List<String>
)

fun PacketBuffer.readMessage() = Message(readBoolean(), readString(), LazyList(readVarUInt()) { readString() })

fun PacketBuffer.writeMessage(value: Message) {
    writeBoolean(value.success)
    writeString(value.message)
    writeVarUInt(value.arguments.size)
    value.arguments.forEach { writeString(it) }
}
