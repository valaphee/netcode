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

package com.valaphee.netcode.mcbe.world.chunk

import com.fasterxml.jackson.module.kotlin.readValue
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.world.block.BlockState
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream
import it.unimi.dsi.fastutil.ints.IntArrayList
import it.unimi.dsi.fastutil.ints.IntList
import java.io.InputStream
import java.io.OutputStream

/**
 * @author Kevin Ludwig
 */
class Layer(
    var palette: IntList,
    var data: BitArray,
) {
    constructor(default: Int, version: BitArray.Version) : this(IntArrayList(16).apply { add(default) }, version.bitArray(Chunk.XZSize * SubChunk.YSize * Chunk.XZSize))

    operator fun get(index: Int) = palette.getInt(data[index])

    operator fun set(index: Int, value: Int) {
        var paletteIndex = palette.indexOf(value)
        if (paletteIndex == -1) {
            paletteIndex = palette.size
            palette.add(value)
            val blocksVersion = data.version
            if (paletteIndex > blocksVersion.maximumEntryValue) {
                blocksVersion.next?.let {
                    val newBlockStorage = it.bitArray(Chunk.XZSize * SubChunk.YSize * Chunk.XZSize)
                    repeat(Chunk.XZSize * SubChunk.YSize * Chunk.XZSize) { newBlockStorage[it] = data[it] }
                    data = newBlockStorage
                }
            }
        }
        data[index] = paletteIndex
    }

    val empty get() = data.empty

    fun writeToBuffer(buffer: PacketBuffer, version: Int, runtime: Boolean) {
        check(palette.size <= data.version.maximumEntryValue + 1)
        buffer.writeByte((data.version.bitsPerEntry shl 1) or if (runtime) 1 else 0)
        data.data.forEach { buffer.writeIntLE(it) }
        if (data.version != BitArray.Version.V0) buffer.writeVarInt(palette.size)
        if (runtime) palette.forEach { buffer.writeVarInt(it) }
        else ByteBufOutputStream(buffer).use {
            val stream = it as OutputStream
            palette.forEach { buffer.nbtVarIntObjectMapper.writeValue(stream, BlockState[version, it]!!) }
        }
    }
}

fun PacketBuffer.readLayer(version: Int): Layer {
    val header = readByte().toInt()
    val bitArrayVersion = BitArray.Version.byBitsPerEntry(header shr 1)
    val bitArray = bitArrayVersion.bitArray(Chunk.XZSize * SubChunk.YSize * Chunk.XZSize, IntArray(bitArrayVersion.bitArrayDataSize(Chunk.XZSize * SubChunk.YSize * Chunk.XZSize)) { readIntLE() })
    val paletteSize = readVarInt()
    return Layer(IntArrayList().apply {
        if (header and 0b1 != 0) repeat(paletteSize) { add(readVarInt()) }
        else ByteBufInputStream(buffer).use { repeat(paletteSize) { add(nbtVarIntObjectMapper.readValue<BlockState>(it as InputStream).getId(version)) } }
    }, bitArray)
}
