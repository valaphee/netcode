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

import com.valaphee.netcode.mcbe.PacketBuffer

/**
 * @author Kevin Ludwig
 */
data class Message(
    val success: Boolean,
    val message: String,
    val arguments: Array<String>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Message

        if (success != other.success) return false
        if (message != other.message) return false
        if (!arguments.contentEquals(other.arguments)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = success.hashCode()
        result = 31 * result + message.hashCode()
        result = 31 * result + arguments.contentHashCode()
        return result
    }
}

fun PacketBuffer.readMessage() = Message(readBoolean(), readString(), Array(readVarUInt()) { readString() })

fun PacketBuffer.writeMessage(value: Message) {
    writeBoolean(value.success)
    writeString(value.message)
    writeVarUInt(value.arguments.size)
    value.arguments.forEach { writeString(it) }
}
