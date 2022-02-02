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
import com.valaphee.netcode.mc.util.nbt.Tag
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.world.chunk.BlockStorage
import io.netty.buffer.ByteBufUtil
import io.netty.buffer.Unpooled

fun chunkData(borderBlocks: List<Int2>, blockEntities: List<Tag>): ByteArray = PacketBuffer(Unpooled.buffer()).use { buffer ->
    buffer.writeByte(borderBlocks.size)
    borderBlocks.forEach { buffer.writeByte((it.x and 0xF) or ((it.y and 0xF) shl 4)) }
    buffer.toNbtOutputStream().use { stream -> blockEntities.forEach(stream::writeTag) }
    ByteBufUtil.getBytes(buffer)
}

fun chunkData(blockStorage: BlockStorage, biomes: ByteArray, borderBlocks: List<Int2>, blockEntities: List<Tag>): ByteArray = PacketBuffer(Unpooled.buffer()).use { buffer ->
    repeat(blockStorage.subChunkCount) { i -> blockStorage.subChunks[i].writeToBuffer(buffer) }
    buffer.writeBytes(biomes)
    buffer.writeByte(borderBlocks.size)
    borderBlocks.forEach { buffer.writeByte((it.x and 0xF) or ((it.y and 0xF) shl 4)) }
    buffer.toNbtOutputStream().use { stream -> blockEntities.forEach(stream::writeTag) }
    ByteBufUtil.getBytes(buffer)
}
