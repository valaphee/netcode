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

import kotlin.collections.set

private val falseTag = ByteTagImpl("", 0.toByte())
private val trueTag = ByteTagImpl("", 1.toByte())

fun ofBool(value: Boolean): NumberTag = if (value) trueTag else falseTag

fun ofByte(value: Byte): NumberTag = ByteTagImpl("", value)

fun ofShort(value: Short): NumberTag = ShortTagImpl("", value)

fun ofInt(value: Int): NumberTag = IntTagImpl("", value)

fun ofLong(value: Long): NumberTag = LongTagImpl("", value)

fun ofFloat(value: Float): NumberTag = FloatTagImpl("", value)

fun ofDouble(value: Double): NumberTag = DoubleTagImpl("", value)

fun listTag(): ListTag = ListTagImpl("")

fun listTag(type: TagType): ListTag = ListTagImpl("", mutableListOf(), type)

fun ofList(value: MutableList<Tag>): ListTag = ListTagImpl("", value, value[0].type)

fun compoundTag(): CompoundTag = CompoundTagImpl("")

fun compoundTag(key: Array<String>, value: Array<Tag>): CompoundTag {
    require(key.size == value.size) { "Length of keys and values differ" }
    return CompoundTagImpl("", LinkedHashMap<String, Tag>().apply { repeat(key.size) { this[key[it]] = value[it] } })
}

fun ofMap(value: MutableMap<String, Tag>): CompoundTag = CompoundTagImpl("", value)

fun ofByteArray(value: ByteArray): ArrayTag = ByteArrayTagImpl("", value)

fun ofIntArray(value: IntArray): ArrayTag = IntArrayTagImpl("", value)

fun ofLongArray(value: LongArray): ArrayTag = LongArrayTagImpl("", value)

fun ofString(value: String): ArrayTag = StringTagImpl("", value)

fun Any?.toTag(): Tag = when (this) {
    is Boolean -> ofBool(this)
    is Byte -> ofByte(this)
    is Short -> ofShort(this)
    is Int -> ofInt(this)
    is Long -> ofLong(this)
    is Float -> ofFloat(this)
    is Double -> ofDouble(this)
    is List<*> -> ofList(map { it.toTag() }.toMutableList())
    is Map<*, *> -> ofMap(map { it.key as String to it.value.toTag() }.toMap().toMutableMap())
    is ByteArray -> ofByteArray(this)
    is IntArray -> ofIntArray(this)
    is LongArray -> ofLongArray(this)
    is String -> ofString(this)
    is Nbt -> toTag()
    else -> TODO("$this")
}
