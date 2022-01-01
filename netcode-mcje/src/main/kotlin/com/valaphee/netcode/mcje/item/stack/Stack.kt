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

package com.valaphee.netcode.mcje.item.stack

import com.valaphee.netcode.mc.util.nbt.CompoundTag
import com.valaphee.netcode.mc.util.nbt.NbtInputStream
import com.valaphee.netcode.mc.util.nbt.NbtOutputStream
import com.valaphee.netcode.mcje.PacketBuffer
import com.valaphee.netcode.mcje.util.NamespacedKey
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream

/**
 * @author Kevin Ludwig
 */
data class Stack(
    val itemKey: NamespacedKey,
    var count: Int = 1,
    var tag: CompoundTag? = null,
) {
    fun equalsIgnoreCount(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Stack

        if (itemKey != other.itemKey) return false
        if (tag != other.tag) return false

        return true
    }
}

fun PacketBuffer.readStack() = if (readBoolean()) Stack(registrySet.items[readVarInt()]!!, readByte().toInt(), NbtInputStream(ByteBufInputStream(buffer)).use { it.readTag() }?.asCompoundTag()) else null

fun PacketBuffer.writeStack(value: Stack?) {
    value?.let {
        writeBoolean(true)
        writeVarInt(registrySet.items.getId(it.itemKey))
        writeByte(it.count)
        NbtOutputStream(ByteBufOutputStream(buffer)).use { it.writeTag(value.tag) }
    } ?: writeBoolean(false)
}
