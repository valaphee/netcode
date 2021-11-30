/*
 * MIT License
 *
 * Copyright (c) 2021, Valaphee.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
