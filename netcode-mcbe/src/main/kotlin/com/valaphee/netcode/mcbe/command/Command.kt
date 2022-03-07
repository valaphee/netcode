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
data class Command(
    val name: String,
    val description: String,
    val flags: Set<Flag>,
    val permission: Permission,
    var aliases: Enumeration?, // needed for je-be protocol translation
    val overloads: List<List<Parameter>>
) {
    enum class Flag {
        Usage, Visible, Synchronized, Executable, Type, Cheat, Unknown6
    }

    class Builder(
        val name: String,
        val description: String,
        val flags: Set<Flag>,
        val permission: Permission,
        val aliasesIndex: Int,
        val overloadStructures: List<List<Parameter.Builder>>
    )
}
