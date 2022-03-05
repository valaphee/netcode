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

package com.valaphee.netcode.mcbe.network

import com.valaphee.netcode.mcbe.network.packet.ServerToClientHandshakePacket
import com.valaphee.netcode.mcbe.util.serverToClientHandshakeJws
import io.netty.buffer.ByteBuf
import io.netty.buffer.PooledByteBufAllocator
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOutboundHandlerAdapter
import io.netty.channel.ChannelPromise
import io.netty.handler.codec.DecoderException
import io.netty.util.ReferenceCountUtil
import java.security.Key
import java.security.KeyPair
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyAgreement
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * @author Kevin Ludwig
 */
class EncryptionInitializer(
    keyPair: KeyPair,
    otherPublicKey: Key,
    val gcm: Boolean,
    salt: ByteArray = ByteArray(16).apply(random::nextBytes),
) : ChannelInitializer<Channel>() {
    val serverToClientHandshakePacket: ServerToClientHandshakePacket
    private val key: ByteArray
    private val iv: ByteArray
    private lateinit var keyBuffer: ByteBuf

    init {
        val secret = KeyAgreement.getInstance("ECDH").apply {
            init(keyPair.private)
            doPhase(otherPublicKey, true)
        }.generateSecret()
        val sha256 = sha256Local.get()
        val sha256Data = PooledByteBufAllocator.DEFAULT.directBuffer(salt.size + secret.size)
        try {
            sha256Data.writeBytes(salt)
            sha256Data.writeBytes(secret)
            sha256.update(sha256Data.nioBuffer())
        } finally {
            sha256Data.release()
        }
        serverToClientHandshakePacket = ServerToClientHandshakePacket(serverToClientHandshakeJws(keyPair, salt))
        key = sha256.digest()
        if (gcm) {
            iv = ByteArray(16)
            System.arraycopy(key, 0, iv, 0, 12)
            iv[15] = 2
        } else {
            iv = ByteArray(16)
            System.arraycopy(key, 0, iv, 0, iv.size)
        }
    }

    override fun initChannel(channel: Channel) {
        keyBuffer = channel.alloc().directBuffer(32, 32).writeBytes(key)
        channel.pipeline()
            .addBefore(Compressor.NAME, "mcbe-encryptor", Encryptor())
            .addBefore("mcbe-encryptor", "mcbe-decryptor", Decryptor())
    }

    private inner class Encryptor : ChannelOutboundHandlerAdapter() {
        private val aes = Cipher.getInstance(if (gcm) "AES/CTR/NoPadding" else "AES/CFB8/NoPadding").apply { init(Cipher.ENCRYPT_MODE, SecretKeySpec(key, "AES"), IvParameterSpec(this@EncryptionInitializer.iv)) }
        private var counter = 0L

        override fun write(context: ChannelHandlerContext, message: Any, promise: ChannelPromise) {
            if (message is ByteBuf) {
                try {
                    val readerIndex = message.readerIndex()
                    val sha256 = sha256Local.get()
                    val sha256Data = context.alloc().directBuffer()
                    try {
                        sha256Data.writeLongLE(counter++)
                        sha256Data.writeBytes(message)
                        keyBuffer.markReaderIndex()
                        sha256Data.writeBytes(keyBuffer)
                        keyBuffer.resetReaderIndex()
                        sha256.update(sha256Data.nioBuffer())
                    } finally {
                        sha256Data.release()
                    }
                    message.readerIndex(readerIndex)
                    message.writeBytes(sha256.digest().copyOf(8))

                    val readableBytes = message.readableBytes()
                    var heapIn = heapInLocal.get()
                    if (heapIn.size < readableBytes) heapInLocal.set(ByteArray(readableBytes).also { heapIn = it })
                    message.duplicate().readBytes(heapIn, 0, readableBytes)
                    var heapOut = heapOutLocal.get()
                    val outputSize = aes.getOutputSize(readableBytes)
                    if (heapOut.size < outputSize) heapOutLocal.set(ByteArray(outputSize).also { heapOut = it })
                    message.duplicate().writerIndex(message.readerIndex()).writeBytes(heapOut, 0, aes.update(heapIn, 0, readableBytes, heapOut))

                    super.write(context, message.retain(), promise)
                } finally {
                    ReferenceCountUtil.safeRelease(message)
                }
            } else super.write(context, message, promise)
        }
    }

    private inner class Decryptor : ChannelInboundHandlerAdapter() {
        private val aes = Cipher.getInstance(if (gcm) "AES/CTR/NoPadding" else "AES/CFB8/NoPadding").apply { init(Cipher.DECRYPT_MODE, SecretKeySpec(key, "AES"), IvParameterSpec(this@EncryptionInitializer.iv)) }
        private var count = 0L

        override fun channelRead(context: ChannelHandlerContext, message: Any) {
            if (message is ByteBuf) {
                val `in`: ByteBuf
                if (1 < message.nioBufferCount()) {
                    `in` = context.alloc().directBuffer(message.readableBytes())
                    `in`.writeBytes(message)
                    message.release()
                } else `in` = message
                try {
                    val inReaderIndex = `in`.readerIndex()
                    val inWriterIndex = `in`.writerIndex()

                    val readableBytes = `in`.readableBytes()
                    var heapIn = heapInLocal.get()
                    if (heapIn.size < readableBytes) heapInLocal.set(ByteArray(readableBytes).also { heapIn = it })
                    `in`.duplicate().readBytes(heapIn, 0, readableBytes)
                    var heapOut = heapOutLocal.get()
                    val outputSize = aes.getOutputSize(readableBytes)
                    if (heapOut.size < outputSize) heapOutLocal.set(ByteArray(outputSize).also { heapOut = it })
                    `in`.duplicate().writerIndex(`in`.readerIndex()).writeBytes(heapOut, 0, aes.update(heapIn, 0, readableBytes, heapOut))

                    val sha256 = sha256Local.get()
                    val sha256Data = context.alloc().directBuffer()
                    try {
                        sha256Data.writeLongLE(count++)
                        sha256Data.writeBytes(`in`.readerIndex(inReaderIndex).writerIndex(inWriterIndex - 8))
                        keyBuffer.markReaderIndex()
                        sha256Data.writeBytes(keyBuffer)
                        keyBuffer.resetReaderIndex()
                        sha256.update(sha256Data.nioBuffer())
                    } finally {
                        sha256Data.release()
                    }
                    `in`.writerIndex(inWriterIndex)
                    if (sha256.digest().copyOf(8).any { it != `in`.readByte() }) throw DecoderException("Checksum mismatch")
                    super.channelRead(context, `in`.readerIndex(inReaderIndex).writerIndex(inWriterIndex - 8).retain())
                } finally {
                    ReferenceCountUtil.safeRelease(`in`)
                }
            } else super.channelRead(context, message)
        }
    }

    companion object {
        private val heapInLocal = ThreadLocal.withInitial { ByteArray(0) }
        private val heapOutLocal = ThreadLocal.withInitial { ByteArray(0) }
        private val sha256Local = ThreadLocal.withInitial { MessageDigest.getInstance("SHA-256") }
        private val random = SecureRandom()
    }
}
