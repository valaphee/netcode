/*
 * MIT License
 *
 * Copyright (c) 2021, Valaphee.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.valaphee.netcode.mcbe.command

/**
 * @author Kevin Ludwig
 */
data class Parameter(
    val name: String,
    val optional: Boolean,
    val options: Collection<Option>,
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

    class Structure(
        var name: String,
        var optional: Boolean,
        var options: Collection<Option>,
        var enumeration: Boolean,
        var softEnumeration: Boolean,
        var postfix: Boolean,
        var index: Int
    )
}
