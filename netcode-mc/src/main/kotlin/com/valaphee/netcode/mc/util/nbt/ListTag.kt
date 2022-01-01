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

package com.valaphee.netcode.mc.util.nbt

/**
 * @author Kevin Ludwig
 */
interface ListTag : Tag {
    override val isList get() = true

    override fun asListTag() = this

    fun toArray(): Array<Tag>

    fun toList(): List<Tag>

    fun has(index: Int): Boolean

    operator fun get(index: Int): Tag

    fun put(tag: Tag)

    fun putByte(value: Byte)

    fun putShort(value: Short)

    fun putInt(value: Int)

    fun putLong(value: Long)

    fun putFloat(value: Float)

    fun putDouble(value: Double)

    fun putByteArray(value: ByteArray)

    fun putString(value: String)

    fun putIntArray(value: IntArray)

    fun putLongArray(value: LongArray)

    operator fun set(index: Int, tag: Tag)

    fun setByte(index: Int, value: Byte)

    fun setShort(index: Int, value: Short)

    fun setInt(index: Int, value: Int)

    fun setLong(index: Int, value: Long)

    fun setFloat(index: Int, value: Float)

    fun setDouble(index: Int, value: Double)

    fun setByteArray(index: Int, value: ByteArray)

    fun setString(index: Int, value: String)

    fun setIntArray(index: Int, value: IntArray)

    fun setLongArray(index: Int, value: LongArray)

    fun remove(index: Int): Tag

    val size: Int

    val isEmpty: Boolean
}
