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

package com.valaphee.netcode.mcbe.network.packet

import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.PacketReader
import com.valaphee.netcode.mcbe.network.Restrict
import com.valaphee.netcode.mcbe.network.Restriction

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToServer)
class CacheBlobStatusPacket(
    val misses: LongArray,
    val hits: LongArray
) : Packet() {
    override val id get() = 0x87

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarUInt(misses.size)
        buffer.writeVarUInt(hits.size)
        misses.forEach { buffer.writeLongLE(it) }
        hits.forEach { buffer.writeLongLE(it) }
    }

    override fun handle(handler: PacketHandler) = handler.cacheBlobStatus(this)

    override fun toString() = "CacheBlobStatusPacket(misses=${misses.contentToString()}, hits=${hits.contentToString()})"
}

/**
 * @author Kevin Ludwig
 */
object CacheBlobStatusPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): CacheBlobStatusPacket {
        val missCount = buffer.readVarUInt()
        val hitCount = buffer.readVarUInt()
        val length = (missCount + hitCount) * 8
        check(length <= buffer.readableBytes()) { "Length of $length exceeds ${buffer.readableBytes()}" }
        val misses = LongArray(missCount) { buffer.readLongLE() }
        val hits = LongArray(hitCount) { buffer.readLongLE() }
        return CacheBlobStatusPacket(misses, hits)
    }
}
