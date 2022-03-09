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
    var bitArray: BitArray,
) {
    constructor(default: Int, version: BitArray.Version) : this(IntArrayList(16).apply { add(default) }, version.bitArray(BlockStorage.XZSize * SubChunk.YSize * BlockStorage.XZSize))

    operator fun get(index: Int) = palette.getInt(bitArray[index])

    operator fun set(index: Int, value: Int) {
        var paletteIndex = palette.indexOf(value)
        if (paletteIndex == -1) {
            paletteIndex = palette.size
            palette.add(value)
            val blocksVersion = bitArray.version
            if (paletteIndex > blocksVersion.maximumEntryValue) {
                blocksVersion.next?.let {
                    val newBlockStorage = it.bitArray(BlockStorage.XZSize * SubChunk.YSize * BlockStorage.XZSize)
                    repeat(BlockStorage.XZSize * SubChunk.YSize * BlockStorage.XZSize) { newBlockStorage[it] = bitArray[it] }
                    bitArray = newBlockStorage
                }
            }
        }
        bitArray[index] = paletteIndex
    }

    val empty get() = bitArray.empty

    fun writeToBuffer(buffer: PacketBuffer, runtime: Boolean) {
        buffer.writeByte((bitArray.version.bitsPerEntry shl 1) or if (runtime) 1 else 0)
        bitArray.data.forEach { buffer.writeIntLE(it) }
        buffer.writeVarInt(palette.size)
        if (runtime) palette.forEach { buffer.writeVarInt(it) }
        else ByteBufOutputStream(buffer).use {
            val stream = it as OutputStream
            palette.forEach { buffer.nbtObjectMapper.writeValue(stream, buffer.registries.blockStates[it]!!) }
        }
    }
}

fun PacketBuffer.readLayer(): Layer {
    val header = readByte().toInt()
    val version = BitArray.Version.byBitsPerEntry(header shr 1)
    val runtime = header and 1 == 1
    val bitArray = version.bitArray(BlockStorage.XZSize * SubChunk.YSize * BlockStorage.XZSize, IntArray(version.bitArrayDataSize(BlockStorage.XZSize * SubChunk.YSize * BlockStorage.XZSize)) { readIntLE() })
    val paletteSize = readVarInt()
    return Layer(IntArrayList().apply {
        if (runtime) repeat(paletteSize) { add(readVarInt()) }
        else ByteBufInputStream(buffer).use {
            val stream = it as InputStream
            repeat(paletteSize) { add(registries.blockStates.getId(nbtObjectMapper.readValue(stream))) }
        }
    }, bitArray)
}
