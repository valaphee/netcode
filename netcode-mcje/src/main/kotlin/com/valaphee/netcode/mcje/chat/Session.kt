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

package com.valaphee.netcode.mcje.chat

import com.valaphee.netcode.mcje.network.PacketBuffer

data class Session(
    val expiresAt: Long,
    val publicKey: ByteArray,
    val signature: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Session

        if (expiresAt != other.expiresAt) return false
        if (!publicKey.contentEquals(other.publicKey)) return false
        if (!signature.contentEquals(other.signature)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = expiresAt.hashCode()
        result = 31 * result + publicKey.contentHashCode()
        result = 31 * result + signature.contentHashCode()
        return result
    }

    override fun toString() = "Session(expiresAt=$expiresAt)"
}

fun PacketBuffer.readSession() = Session(readLong(), readByteArray(), readByteArray(256))

fun PacketBuffer.writeSession(value: Session) {
    writeLong(value.expiresAt)
    writeByteArray(value.publicKey)
    writeByteArray(value.signature)
}
