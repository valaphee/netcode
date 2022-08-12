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

package com.valaphee.netcode.mcbeautomation

import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.netty.buffer.ByteBufInputStream
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageCodec
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import java.util.UUID
import kotlin.reflect.KClass

/**
 * @author Kevin Ludwig
 */
class PacketCodec(
    private val events: Map<String, KClass<out Event>>
) : MessageToMessageCodec<TextWebSocketFrame, Packet>() {
    private val responses = mutableMapOf<UUID, KClass<out CommandResponse>>()

    override fun encode(context: ChannelHandlerContext, message: Packet, out: MutableList<Any>) {
        if (message.header.messagePurpose == Packet.Header.MessagePurpose.CommandRequest && message.body is Command) responses[message.header.requestId!!] = message.body.responseType
        out.add(TextWebSocketFrame(objectMapper.writeValueAsString(message)))
    }

    override fun decode(context: ChannelHandlerContext, `in`: TextWebSocketFrame, out: MutableList<Any>) {
        val packet = objectMapper.readValue<Packet>(ByteBufInputStream(`in`.content()))
        out.add(when (packet.header.messagePurpose) {
            Packet.Header.MessagePurpose.CommandResponse -> responses.remove(packet.header.requestId)?.let { Packet(packet.header, objectMapper.convertValue(packet.body, it.java)) } ?: packet
            Packet.Header.MessagePurpose.Error -> objectMapper.convertValue<Error>(packet.body!!)
            Packet.Header.MessagePurpose.Event -> events[packet.header.eventName]?.let { Packet(packet.header, objectMapper.convertValue(packet.body, it.java)) } ?: packet
            Packet.Header.MessagePurpose.EventSubscribe, Packet.Header.MessagePurpose.EventUnsubscribe -> objectMapper.convertValue<EventRequest>(packet.body!!)
            else -> packet
        })
    }

    companion object {
        const val Name = "automation-codec"

        private val objectMapper = jacksonObjectMapper()
    }
}
