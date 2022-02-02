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

package com.valaphee.netcode.mcbe

import io.netty.handler.codec.DecoderException

/**
 * @author Kevin Ludwig
 */
class PacketDecoderException : DecoderException {
    val buffer: PacketBuffer

    constructor(message: String, buffer: PacketBuffer) : super(message) {
        this.buffer = buffer
    }

    constructor(message: String, cause: Throwable, buffer: PacketBuffer) : super(message, cause) {
        this.buffer = buffer
    }
}
