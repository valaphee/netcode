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

import com.valaphee.netcode.mcbe.util.Registry

/**
 * @author Kevin Ludwig
 */
data class Parameter(
    var name: String, // needed for je-be protocol translation
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

        companion object {
            private val registryPre419 = Registry<Type>().apply {
                this[0x01] = Integer
                this[0x02] = Float
                this[0x03] = Float1
                /*this[0x04] =
                this[0x05] =*/
                this[0x06] = Target
                /*this[0x07] =
                this[0x0E] =*/
                this[0x1D] = String
                this[0x25] = Int3
                this[0x26] = Float3
                this[0x29] = Message
                /*this[0x2B] =*/
                this[0x2F] = Json
                /*this[0x36] =*/
            }
            private val registryPre503 = Registry<Type>().apply {
                this[0x01] = Integer
                this[0x03] = Float
                this[0x04] = Float1
                /*this[0x05] =
                this[0x06] =*/
                this[0x07] = Target
                /*this[0x09] =
                this[0x10] =*/
                this[0x20] = String
                this[0x28] = Int3
                this[0x29] = Float3
                this[0x2C] = Message
                /*this[0x2E] =*/
                this[0x32] = Json
                this[0x3C] = BlockStates
                /*this[0x3F] =*/
            }
            private val registry = Registry<Type>().apply {
                this[0x01] = Integer
                this[0x03] = Float
                this[0x04] = Float1
                /*this[0x05] =
                this[0x06] =*/
                this[0x07] = Target
                /*this[0x09] =
                this[0x10] =*/
                this[0x26] = String
                this[0x2E] = Int3
                this[0x2F] = Float3
                this[0x32] = Message
                /*this[0x34] =*/
                this[0x38] = Json
            }

            fun registryByVersion(version: Int) = if (version >= 503) registry else if (version >= 419) registryPre503 else registryPre419
        }
    }

    class Builder(
        var name: String,
        var optional: Boolean,
        var options: Set<Option>,
        var enumeration: Boolean,
        var softEnumeration: Boolean,
        var postfix: Boolean,
        var index: Int
    )
}
