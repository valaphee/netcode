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

package com.valaphee.netcode.mcje.world.item

import com.fasterxml.jackson.module.kotlin.readValue
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.util.NamespacedKey
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream
import java.io.OutputStream

/**
 * @author Kevin Ludwig
 */
data class ItemStack(
    val itemKey: NamespacedKey,
    var count: Int = 1,
    var data: Any? = null,
) {
    fun equalsIgnoreCount(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ItemStack

        if (itemKey != other.itemKey) return false
        if (data != other.data) return false

        return true
    }
}

fun PacketBuffer.readStack() = if (readBoolean()) ItemStack(registrySet.items[readVarInt()]!!, readByte().toInt(), nbtObjectMapper.readValue(ByteBufInputStream(buffer))) else null

fun PacketBuffer.writeStack(value: ItemStack?) {
    value?.let {
        writeBoolean(true)
        writeVarInt(registrySet.items.getId(it.itemKey))
        writeByte(it.count)
        nbtObjectMapper.writeValue(ByteBufOutputStream(buffer) as OutputStream, it.data)
    } ?: writeBoolean(false)
}
