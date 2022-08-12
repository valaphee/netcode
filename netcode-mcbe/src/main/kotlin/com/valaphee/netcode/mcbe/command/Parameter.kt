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

package com.valaphee.netcode.mcbe.command

import com.valaphee.netcode.mcbe.network.V1_14_060
import com.valaphee.netcode.mcbe.network.V1_16_100
import com.valaphee.netcode.mcbe.network.V1_18_030
import com.valaphee.netcode.mcbe.network.V1_19_000
import com.valaphee.netcode.util.Int2ObjectOpenHashBiMapVersioned

/**
 * @author Kevin Ludwig
 */
data class Parameter(
    val name: String,
    val optional: Boolean,
    val options: Set<Option>,
    val enumeration: Enumeration?,
    val postfix: String?,
    val type: Type?
) {
    enum class Option {
        SuppressEnumerationAutoCompletion, HasSemanticConstraint, EnumAsChainedCommand
    }

    enum class Type {
        Integer,
        Float,
        Float1,
        Target,
        String,
        Int3,
        Float3,
        Message,
        Json,
        BlockStates;

        fun getId(version: Int) = registry.getLastInt(version, this)

        companion object {
            private val registry = Int2ObjectOpenHashBiMapVersioned<Type>().apply {
                put(Integer    , V1_14_060 to 0x01                                                         )
                put(Float      , V1_14_060 to 0x02, V1_16_100 to 0x03                                      )
                put(Float1     , V1_14_060 to 0x03, V1_16_100 to 0x04                                      )
                put(Target     , V1_14_060 to 0x06, V1_16_100 to 0x07                   , V1_19_000 to 0x08)
                put(String     , V1_14_060 to 0x1D, V1_16_100 to 0x20, V1_18_030 to 0x26, V1_19_000 to 0x27)
                put(Int3       , V1_14_060 to 0x25, V1_16_100 to 0x28, V1_18_030 to 0x2E, V1_19_000 to 0x2F)
                put(Float3     , V1_14_060 to 0x26, V1_16_100 to 0x29, V1_18_030 to 0x2F, V1_19_000 to 0x30)
                put(Message    , V1_14_060 to 0x29, V1_16_100 to 0x2C, V1_18_030 to 0x32, V1_19_000 to 0x33)
                put(Json       , V1_14_060 to 0x2F, V1_16_100 to 0x32, V1_18_030 to 0x38, V1_19_000 to 0x39)
                put(BlockStates,                    V1_16_100 to 0x3C                                      )
            }

            operator fun get(version: Int, id: Int) = registry.getLast(version, id)
        }
    }
}
