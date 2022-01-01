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
import java.io.Closeable
import javax.crypto.ShortBufferException
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * @author Kevin Ludwig
 */
interface Cipher : Closeable {
    fun cipher(`in`: ByteBuf, out: ByteBuf)
}

fun aesCipher(encrypt: Boolean, key: ByteArray, iv: ByteArray, gcm: Boolean): Cipher = JavaAesCipher(encrypt, key, iv, gcm)

/**
 * @author Kevin Ludwig
 */
class JavaAesCipher(
    encrypt: Boolean,
    key: ByteArray,
    iv: ByteArray,
    gcm: Boolean
) : Cipher {
    private var cipher = javax.crypto.Cipher.getInstance(if (gcm) "AES/CTR/NoPadding" else "AES/CFB8/NoPadding").apply { init(if (encrypt) javax.crypto.Cipher.ENCRYPT_MODE else javax.crypto.Cipher.DECRYPT_MODE, SecretKeySpec(key, "AES"), IvParameterSpec(iv)) }

    @Throws(ShortBufferException::class)
    override fun cipher(`in`: ByteBuf, out: ByteBuf) {
        val readableBytes = `in`.readableBytes()
        var heapIn = heapInLocal.get()
        if (heapIn.size < readableBytes) heapInLocal.set(ByteArray(readableBytes).also { heapIn = it })
        `in`.readBytes(heapIn, 0, readableBytes)
        var heapOut = heapOutLocal.get()
        val outputSize = cipher.getOutputSize(readableBytes)
        if (heapOut.size < outputSize) heapOutLocal.set(ByteArray(outputSize).also { heapOut = it })
        out.writeBytes(heapOut, 0, cipher.update(heapIn, 0, readableBytes, heapOut))
    }

    override fun close() {}

    companion object {
        private val heapInLocal = ThreadLocal.withInitial { ByteArray(0) }
        private val heapOutLocal = ThreadLocal.withInitial { ByteArray(0) }
    }
}

/**
 * @author Kevin Ludwig
 */
class MbedTlsAesCipher(
    encrypt: Boolean,
    key: ByteArray,
    iv: ByteArray
) : Cipher {
    private var cipherContext = MbedTlsAesCipherImpl.init(encrypt, key, iv)

    override fun cipher(`in`: ByteBuf, out: ByteBuf) {
        val length = `in`.readableBytes()
        if (length <= 0) return
        out.ensureWritable(length)
        MbedTlsAesCipherImpl.cipher(cipherContext, `in`.memoryAddress() + `in`.readerIndex(), out.memoryAddress() + out.writerIndex(), length)
        `in`.readerIndex(`in`.writerIndex())
        out.writerIndex(out.writerIndex() + length)
    }

    fun cipher(`in`: Long, out: Long, length: Int) = MbedTlsAesCipherImpl.cipher(cipherContext, `in`, out, length)

    override fun close() {
        MbedTlsAesCipherImpl.free(cipherContext)
        cipherContext = 0
    }
}
