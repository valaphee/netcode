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
interface CompoundTag : Tag {
    override val isCompound get() = true

    override fun asCompoundTag() = this

    fun toMap(): Map<String, Tag>

    fun has(name: String): Boolean

    operator fun get(name: String): Tag?

    operator fun set(name: String, tag: Tag)

    fun setByte(name: String, value: Byte)

    fun setShort(name: String, value: Short)

    fun setInt(name: String, value: Int)

    fun setLong(name: String, value: Long)

    fun setFloat(name: String, value: Float)

    fun setDouble(name: String, value: Double)

    fun setByteArray(name: String, value: ByteArray)

    fun setString(name: String, value: String)

    fun setIntArray(name: String, value: IntArray)

    fun setLongArray(name: String, value: LongArray)

    fun remove(name: String): Tag?

    val size: Int

    val isEmpty: Boolean
}
