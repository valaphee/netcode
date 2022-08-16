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

package com.valaphee.netcode.mcje.world.chunk

import com.valaphee.netcode.mcje.network.PacketBuffer

/**
 * @author Kevin Ludwig
 */
class SubChunk(
    var blockCount: Int,
    val blocks: Storage,
    val biomes: Storage?
) {
    operator fun get(x: Int, y: Int, z: Int) = blocks[(y shl YShift) or (z shl ZShift) or x]

    operator fun set(x: Int, y: Int, z: Int, value: Int) {
        blocks[(y shl YShift) or (z shl ZShift) or x] = value
    }

    companion object {
        const val YSize = 16
        const val YShift = 8
        const val ZShift = 4
    }
}

fun PacketBuffer.readSubChunk(version: Int) = SubChunk(readUnsignedShort(), readStorage(), if (version >= 758) readStorage() else null)

fun PacketBuffer.writeSubChunk(value: SubChunk, version: Int) {
    writeShort(value.blockCount)
    writeStorage(value.blocks)
    if (version >= 758) writeStorage(value.biomes!!)
}
