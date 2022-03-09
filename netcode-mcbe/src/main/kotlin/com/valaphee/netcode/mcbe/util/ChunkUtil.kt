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

package com.valaphee.netcode.mcbe.util

import com.valaphee.foundry.math.Int2
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.world.chunk.BlockStorage
import com.valaphee.netcode.mcbe.world.chunk.Layer
import io.netty.buffer.ByteBufOutputStream
import java.io.OutputStream

fun PacketBuffer.writeChunkData(borderBlocks: List<Int2>, blockEntities: List<Any?>) {
    writeByte(borderBlocks.size)
    borderBlocks.forEach { writeByte((it.x and 0xF) or ((it.y and 0xF) shl 4)) }
    blockEntities.forEach { nbtObjectMapper.writeValue(ByteBufOutputStream(this) as OutputStream, it) }
}

fun PacketBuffer.writeChunkDataPre486(blockStorage: BlockStorage, biomes: ByteArray, borderBlocks: List<Int2>, blockEntities: List<Any?>) {
    repeat(blockStorage.subChunkCount) { i -> blockStorage.subChunks[i].writeToBuffer(this, true) }
    writeBytes(biomes)
    writeByte(borderBlocks.size)
    borderBlocks.forEach { writeByte((it.x and 0xF) or ((it.y and 0xF) shl 4)) }
    blockEntities.forEach { nbtObjectMapper.writeValue(ByteBufOutputStream(this) as OutputStream, it) }
}

fun PacketBuffer.writeChunkData(blockStorage: BlockStorage, biomes: Layer, borderBlocks: List<Int2>, blockEntities: List<Any?>) {
    repeat(blockStorage.subChunkCount) { i -> blockStorage.subChunks[i].writeToBuffer(this, true) }
    biomes.writeToBuffer(this, true)
    writeByte(borderBlocks.size)
    borderBlocks.forEach { writeByte((it.x and 0xF) or ((it.y and 0xF) shl 4)) }
    blockEntities.forEach { nbtObjectMapper.writeValue(ByteBufOutputStream(this) as OutputStream, it) }
}
