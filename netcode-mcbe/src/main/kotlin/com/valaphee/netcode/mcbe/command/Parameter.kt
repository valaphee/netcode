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

/**
 * @author Kevin Ludwig
 */
data class Parameter(
    val name: String,
    val optional: Boolean,
    val options: Set<Option>,
    val enumeration: Enumeration?,
    val postfix: String?,
    val type: Int?
) {
    enum class Option {
        SuppressEnumerationAutoCompletion, HasSemanticConstraint, EnumAsChainedCommand
    }

    enum class Type {
        Integer,
        Float,
        Value,
        WildcardInteger,
        Operator,
        Target,
        WildcardTarget,
        FilePath,
        String,
        Int3,
        Float3,
        Message,
        Text,
        Json,
        BlockState,
        Command
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
