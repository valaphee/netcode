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

package com.valaphee.netcode.mcbe.world.block

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.valaphee.netcode.util.Int2ObjectOpenHashBiMapVersioned

/**
 * @author Kevin Ludwig
 */
class BlockState {
    val blockKey: String
    val states: Map<String, Any>

    @JsonCreator
    constructor(
        @JsonProperty("name") blockKey: String,
        @JsonProperty("states") states: Map<String, Any> = emptyMap()
    ) {
        this.blockKey = blockKey
        this.states = states
    }

    constructor(key: String) {
        val statesBegin = key.indexOf('[')
        val statesEnd = key.indexOf(']')
        if (statesBegin == -1 && statesEnd == -1) {
            blockKey = key
            states = emptyMap()
        } else if (statesEnd == key.length - 1) {
            val states = LinkedHashMap<String, Any>()
            key.substring(statesBegin + 1, statesEnd).split(',').forEach {
                val property = it.split('=', limit = 2)
                states[property[0]] = when (val propertyValue = property[1]) {
                    "false" -> false
                    "true" -> true
                    else -> propertyValue.toIntOrNull() ?: propertyValue
                }
            }
            blockKey = key.substring(0, statesBegin)
            this.states = states
        } else throw IllegalArgumentException()
    }

    fun getId(version: Int) = registry.getLastInt(version, this)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BlockState

        if (blockKey != other.blockKey) return false
        if (states != other.states) return false

        return true
    }

    override fun hashCode(): Int {
        var result = blockKey.hashCode()
        result = 31 * result + states.hashCode()
        return result
    }

    override fun toString() = "${blockKey}${if (states.isNotEmpty()) "[${states.entries.joinToString(",")}]" else ""}"

    companion object {
        val registry = Int2ObjectOpenHashBiMapVersioned<BlockState>()

        operator fun get(version: Int, id: Int) = registry.getLast(version, id)
    }
}
