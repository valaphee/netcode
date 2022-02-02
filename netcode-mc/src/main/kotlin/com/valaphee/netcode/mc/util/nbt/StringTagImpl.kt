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

import java.io.DataInput
import java.io.DataOutput
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.util.regex.Pattern

/**
 * @author Kevin Ludwig
 */
internal class StringTagImpl(
    override var name: String,
    private var value: String? = null
) : ArrayTag {
    override val type get() = TagType.String

    override fun toByteArray() = value!!.toByteArray(StandardCharsets.UTF_8)

    override fun toIntArray() = ByteBuffer.wrap(toByteArray()).asIntBuffer().array()

    override fun toLongArray() = ByteBuffer.wrap(toByteArray()).asLongBuffer().array()

    override fun valueToString() = value!!

    override fun read(input: DataInput, depth: Int, remainingBytes: IntArray) {
        value = input.readUTF()
    }

    override fun write(output: DataOutput) {
        output.writeUTF(value!!)
    }

    override fun print(string: StringBuilder) {
        string.append('"')
        val escapedString = StringBuffer()
        val matcher = escapePattern.matcher(value!!)
        while (matcher.find()) matcher.appendReplacement(escapedString, escapes[matcher.group()])
        matcher.appendTail(escapedString)
        string.append(escapedString)
        string.append('"')
    }

    override fun toString() = StringBuilder().apply(this::print).toString()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StringTagImpl

        if (value != other.value) return false

        return true
    }

    override fun hashCode() = value?.hashCode() ?: 0

    companion object {
        private val escapePattern = Pattern.compile("[\\\\\b\t\n\r\"]")
        private val escapes = mutableMapOf(
            "\b" to "\\\\b",
            "\t" to "\\\\t",
            "\n" to "\\\\n",
            "\r" to "\\\\r",
            "\"" to "\\\\\"",
            "'" to "\\\\'",
            "\\" to "\\\\\\\\"
        )
    }
}
