/*
 * Copyright (c) 2022, Valaphee.
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

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
class Packet(
    @get:JsonProperty("header") val header: Header,
    @get:JsonProperty("body") val body: Any?
) {
    class Header(
        @get:JsonProperty("messagePurpose") val messagePurpose: MessagePurpose,
        @get:JsonProperty("requestId") val requestId: UUID?,
        @get:JsonProperty("eventName") val eventName: String?,
        @get:JsonProperty("version") val version: Int,
    ) {
        enum class MessagePurpose {
            @JsonProperty("commandRequest") CommandRequest,
            @JsonProperty("commandResponse") CommandResponse,
            @JsonProperty("error") Error,
            @JsonProperty("event") Event,
            @JsonProperty("subscribe") EventSubscribe,
            @JsonProperty("unsubscribe") EventUnsubscribe,
        }

        override fun toString() = "Header(messagePurpose=$messagePurpose, requestId=$requestId, version=$version, eventName=$eventName)"
    }

    override fun toString() = "Packet(header=$header, body=$body)"
}
