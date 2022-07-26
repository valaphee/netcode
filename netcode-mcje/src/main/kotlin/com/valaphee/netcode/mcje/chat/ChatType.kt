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

package com.valaphee.netcode.mcje.chat

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @author Kevin Ludwig
 */
class ChatType(
    @get:JsonProperty("chat") val chat: Chat?,
    @get:JsonProperty("narration") val narration: Chat?
) {
    class Chat(
        @get:JsonProperty("decoration") val decoration: Decoration?,
        @get:JsonProperty("priority") val priority: String?
    ) {
        class Decoration(
            @get:JsonProperty("translation_key") val translationKey: String,
            @get:JsonProperty("style") val style: Style,
            @get:JsonProperty("parameters") val parameters: List<Parameter>
        ) {
            class Style(
                @get:JsonProperty("color") val color: String?,
                @get:JsonProperty("italic") val italic: Boolean?
            )

            enum class Parameter {
                @JsonProperty("team_name") TeamName,
                @JsonProperty("sender") Sender,
                @JsonProperty("content") Content
            }
        }
    }
}
