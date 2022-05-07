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

package com.valaphee.netcode.mcbe.auto

import io.netty.buffer.Unpooled
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOutboundHandlerAdapter
import io.netty.channel.ChannelPromise
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import io.netty.util.ReferenceCountUtil
import java.security.Key
import java.security.KeyPair
import java.security.MessageDigest
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
    salt: ByteArray,
) : ChannelInitializer<Channel>() {
    private val key: ByteArray
    private val iv: ByteArray

    init {
        val secret = KeyAgreement.getInstance("ECDH").apply {
            init(keyPair.private)
            doPhase(otherPublicKey, true)
        }.generateSecret()
        val sha256 = sha256Local.get()
        val sha256Data = Unpooled.directBuffer(salt.size + secret.size)
        try {
            sha256Data.writeBytes(salt)
            sha256Data.writeBytes(secret)
            sha256.update(sha256Data.nioBuffer())
        } finally {
            sha256Data.release()
        }
        key = sha256.digest()
        iv = ByteArray(16)
        System.arraycopy(key, 0, iv, 0, iv.size)
    }

    override fun initChannel(channel: Channel) {
        channel.pipeline()
            .addBefore(PacketCodec.Name, "auto-encryptor", Encryptor())
            .addBefore("auto-encryptor", "auto-decryptor", Decryptor())
    }

    private inner class Encryptor : ChannelOutboundHandlerAdapter() {
        private val aes = Cipher.getInstance("AES/CFB8/NoPadding").apply { init(Cipher.ENCRYPT_MODE, SecretKeySpec(key, "AES"), IvParameterSpec(this@EncryptionInitializer.iv)) }

        override fun write(context: ChannelHandlerContext, message: Any, promise: ChannelPromise) {
            if (message is TextWebSocketFrame) {
                try {
                    val content = message.content()
                    val readableBytes = content.readableBytes()
                    var heapIn = heapInLocal.get()
                    if (heapIn.size < readableBytes) heapInLocal.set(ByteArray(readableBytes).also { heapIn = it })
                    content.duplicate().readBytes(heapIn, 0, readableBytes)
                    var heapOut = heapOutLocal.get()
                    val outputSize = aes.getOutputSize(readableBytes)
                    if (heapOut.size < outputSize) heapOutLocal.set(ByteArray(outputSize).also { heapOut = it })
                    content.duplicate().writerIndex(content.readerIndex()).writeBytes(heapOut, 0, aes.update(heapIn, 0, readableBytes, heapOut))
                    super.write(context, BinaryWebSocketFrame(content.retain()), promise)
                } finally {
                    ReferenceCountUtil.safeRelease(message)
                }
            } else super.write(context, message, promise)
        }
    }

    private inner class Decryptor : ChannelInboundHandlerAdapter() {
        private val aes = Cipher.getInstance("AES/CFB8/NoPadding").apply { init(Cipher.DECRYPT_MODE, SecretKeySpec(key, "AES"), IvParameterSpec(this@EncryptionInitializer.iv)) }

        override fun channelRead(context: ChannelHandlerContext, message: Any) {
            if (message is BinaryWebSocketFrame) {
                try {
                    val content = message.content()
                    val readableBytes = content.readableBytes()
                    var heapIn = heapInLocal.get()
                    if (heapIn.size < readableBytes) heapInLocal.set(ByteArray(readableBytes).also { heapIn = it })
                    content.duplicate().readBytes(heapIn, 0, readableBytes)
                    var heapOut = heapOutLocal.get()
                    val outputSize = aes.getOutputSize(readableBytes)
                    if (heapOut.size < outputSize) heapOutLocal.set(ByteArray(outputSize).also { heapOut = it })
                    content.duplicate().writerIndex(content.readerIndex()).writeBytes(heapOut, 0, aes.update(heapIn, 0, readableBytes, heapOut))
                    super.channelRead(context, TextWebSocketFrame(content.retain()))
                } finally {
                    ReferenceCountUtil.safeRelease(message)
                }
            } else super.channelRead(context, message)
        }
    }

    companion object {
        private val heapInLocal = ThreadLocal.withInitial { ByteArray(0) }
        private val heapOutLocal = ThreadLocal.withInitial { ByteArray(0) }
        private val sha256Local = ThreadLocal.withInitial { MessageDigest.getInstance("SHA-256") }
    }
}
