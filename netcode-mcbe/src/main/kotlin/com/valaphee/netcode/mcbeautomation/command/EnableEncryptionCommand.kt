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

package com.valaphee.netcode.mcbeautomation.command

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.valaphee.netcode.mcbeautomation.Command
import com.valaphee.netcode.mcbeautomation.CommandResponse

/**
 * @author Kevin Ludwig
 */
data class EnableEncryptionCommand(
    @get:JsonIgnore val publicKey: String,
    @get:JsonIgnore val salt: String
) : Command() {
    override val commandLine get() = """enableencryption "$publicKey" "$salt""""

    override val responseType get() = EnableEncryptionCommandResponse::class
}

/**
 * @author Kevin Ludwig
 */
data class EnableEncryptionCommandResponse(
    @get:JsonProperty("publicKey") val publicKey: String,
    @get:JsonProperty("statusCode") val statusCode: Int,
    @get:JsonProperty("statusMessage") val statusMessage: String
) : CommandResponse()
