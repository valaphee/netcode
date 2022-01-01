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

package com.valaphee.netcode.mcbe.block

import com.valaphee.netcode.mc.util.getListTag
import com.valaphee.netcode.mc.util.getListTagOrNull
import com.valaphee.netcode.mc.util.getString
import com.valaphee.netcode.mc.util.nbt.CompoundTag
import com.valaphee.netcode.mc.util.nbt.TagType
import com.valaphee.netcode.mcbe.pack.block.Block

/**
 * @author Kevin Ludwig
 */
class Block {
    val key: String
    val properties: Map<String, Set<*>>
    val states: List<BlockState>
    val tag: CompoundTag?

    constructor(block: Block) {
        this.key = block.description.key
        properties = block.description.properties ?: emptyMap()
        states = properties.values.reversed().fold(listOf(listOf<Any?>())) { acc, set -> acc.flatMap { list -> set.map { list + it } } }.map { BlockState(this@Block, properties.keys.zip(it.reversed()).toMap()) }
        tag = block.toTag()
    }

    constructor(key: String, states: List<Map<String, Any>>) {
        this.key = key
        properties = emptyMap()
        this.states = states.map { BlockState(this@Block, it) }
        tag = null
    }

    constructor(key: String, tag: CompoundTag?) {
        this.key = key
        this.properties = linkedMapOf<String, LinkedHashSet<*>>().apply {
            tag?.getListTagOrNull("properties")?.toList()?.forEach {
                val compoundTag = it.asCompoundTag()!!
                put(compoundTag.getString("name"), linkedSetOf(compoundTag.getListTag("enum").let {
                    when (it.toList().first().type) {
                        TagType.Byte -> it.toList().map { it.asNumberTag()!!.toInt() == 1 }
                        TagType.Int -> it.toList().map { it.asNumberTag()!!.toInt() }
                        TagType.String -> it.toList().map { it.asArrayTag()!!.valueToString() }
                        else -> TODO("$it")
                    }
                }))
            }
        }
        states = properties.values.reversed().fold(listOf(listOf<Any?>())) { acc, set -> acc.flatMap { list -> set.map { list + it } } }.map { BlockState(this@Block, properties.keys.zip(it.reversed()).toMap()) }
        this.tag = tag
    }
}
