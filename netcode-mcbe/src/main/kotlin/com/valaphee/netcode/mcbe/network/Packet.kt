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

package com.valaphee.netcode.mcbe.network

/**
 * @author Kevin Ludwig
 */
abstract class Packet {
    interface Reader {
        fun read(buffer: PacketBuffer, version: Int): Packet
    }

    abstract val id: Int
    var senderId = 0
    var clientId = 0

    abstract fun write(buffer: PacketBuffer, version: Int)

    abstract fun handle(handler: PacketHandler)

    companion object {
        const val IdMask = 0x3FF
        const val SenderIdShift = 10
        const val SenderIdMask = 0x3
        const val ClientIdShift = 12
        const val ClientIdMask = 0x3
    }
}
