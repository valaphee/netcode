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

import java.io.Closeable
import java.io.DataOutput

/**
 * @author Kevin Ludwig
 */
class NbtOutputStream(
    private val output: DataOutput
) : Closeable {
    fun writeTag(tag: Tag?) = if (tag == null || tag.type == TagType.End) output.writeByte(TagType.End.ordinal) else {
        output.writeByte(tag.type.ordinal)
        output.writeUTF(tag.name)
        tag.write(output)
    }

    override fun close() {
        if (output is Closeable) (output as Closeable).close()
    }
}
