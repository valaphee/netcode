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
import java.io.Closeable
import java.security.MessageDigest

/**
 * @author Kevin Ludwig
 */
interface Hasher : Closeable {
    fun update(buffer: ByteBuf)

    fun digest(): ByteArray
}

fun sha256Hasher(): Hasher = JavaSha256Hasher()

/**
 * @author Kevin Ludwig
 */
class JavaSha256Hasher : Hasher {
    private var digest = MessageDigest.getInstance("SHA-256")

    override fun update(buffer: ByteBuf) {
        digest.update(ByteBufUtil.getBytes(buffer))
    }

    override fun digest() = digest.digest()

    override fun close() {}
}

/**
 * @author Kevin Ludwig
 */
class MbedTlsSha256Hasher : Hasher {
    private var mbedTlsSha256Context = MbedTlsSha256HasherImpl.init()

    override fun update(buffer: ByteBuf) {
        MbedTlsSha256HasherImpl.update(mbedTlsSha256Context, buffer.memoryAddress() + buffer.readerIndex(), buffer.readableBytes())
        buffer.readerIndex(buffer.writerIndex())
    }

    override fun digest() = MbedTlsSha256HasherImpl.digest(mbedTlsSha256Context)

    override fun close() {
        MbedTlsSha256HasherImpl.free(mbedTlsSha256Context)
        mbedTlsSha256Context = 0
    }
}
