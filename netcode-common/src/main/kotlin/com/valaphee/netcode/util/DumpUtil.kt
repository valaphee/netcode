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

package com.valaphee.netcode.util

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufUtil
import java.util.Formatter

fun ByteBuf.dump(displayedOffset: Long, offset: Int, length: Int) = ByteBufUtil.getBytes(this).dump(displayedOffset, offset, length)

fun ByteArray.dump(displayedOffset: Long, offset: Int, length: Int): String {
    var _displayedOffset = displayedOffset
    return StringBuilder().apply {
        Formatter(this).use {
            appendLine("  Offset: 00 01 02 03 04 05 06 07 08 09 0A 0B 0C 0D 0E 0F")
            var i = offset
            while (i < length) {
                it.format("%08X", _displayedOffset)
                append(": ")
                run {
                    var j = i
                    val k = i + 16
                    while (j < k) {
                        if (j < length) it.format("%02X", this[j]) else append("  ")
                        append(' ')
                        j++
                    }
                }
                append(' ')
                for (j in i until i + 16) {
                    if (j < length) {
                        val char = this[j]
                        if (!char.isISOControl()) append(char) else append('.')
                    } else append(" ")
                }
                appendLine()
                i += 16
                _displayedOffset += 16
            }
        }
    }.toString()
}
