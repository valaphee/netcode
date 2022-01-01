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
enum class TagType(
    val tag: ((name: kotlin.String) -> Tag)? = null
) {
    End,
    Byte(::ByteTagImpl),
    Short(::ShortTagImpl),
    Int(::IntTagImpl),
    Long(::LongTagImpl),
    Float(::FloatTagImpl),
    Double(::DoubleTagImpl),
    ByteArray(::ByteArrayTagImpl),
    String(::StringTagImpl),
    List(::ListTagImpl),
    Compound(::CompoundTagImpl),
    IntArray(::IntArrayTagImpl),
    LongArray(::LongArrayTagImpl)
}
