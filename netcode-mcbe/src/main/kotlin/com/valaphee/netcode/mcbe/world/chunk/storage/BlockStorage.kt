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

package com.valaphee.netcode.mcbe.world.chunk.storage

/**
 * @author Kevin Ludwig
 */
class BlockStorage(
    val default: Int,
    val subChunks: List<SubChunk>
) {
    val subChunkCount: Int
        get() {
            var subChunkCount = subChunks.size - 1
            while (subChunkCount >= 0 && subChunks[subChunkCount].empty) subChunkCount--
            return ++subChunkCount
        }

    constructor(default: Int, subChunkCount: Int = 16) : this(default, List(subChunkCount) { CompactSubChunk(default, BitArray.Version.V1) })

    operator fun get(x: Int, y: Int, z: Int) = if (x in 0 until XZSize && y in 0 until subChunks.size * SubChunk.YSize && z in 0 until XZSize) subChunks[y shr YShift][x, y and YMask, z] else default

    operator fun get(x: Int, y: Int, z: Int, layer: Int) = if (x in 0 until XZSize && y in 0 until subChunks.size * SubChunk.YSize && z in 0 until XZSize) subChunks[y shr YShift][x, y and YMask, z, layer] else default

    operator fun set(x: Int, y: Int, z: Int, value: Int) {
        if (!(x in 0 until XZSize && y in 0 until subChunks.size * SubChunk.YSize && z in 0 until XZSize)) return

        subChunks[y shr YShift][x, y and YMask, z] = value
    }

    operator fun set(x: Int, y: Int, z: Int, value: Int, layer: Int) {
        if (!(x in 0 until XZSize && y in 0 until subChunks.size * SubChunk.YSize && z in 0 until XZSize)) return

        subChunks[y shr YShift][x, y and YMask, z, value] = layer
    }

    companion object {
        const val XZSize = 16
        const val YShift = 4
        const val YMask = 15
    }
}
