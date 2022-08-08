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
import net.kyori.adventure.text.format.Style

/**
 * @author Kevin Ludwig
 */
class ChatType(
    @get:JsonProperty("chat") val chat: Text?,
    @get:JsonProperty("overlay") val overlay: Text?,
    @get:JsonProperty("narration") val narration: Narration?
) {
    class Text(
        @get:JsonProperty("decoration") val decoration: Decoration?,
    )

    class Narration(
        @get:JsonProperty("decoration") val decoration: Decoration?,
        @get:JsonProperty("priority") val priority: Priority?
    ) {
        enum class Priority {
            @JsonProperty("chat") Chat,
            @JsonProperty("system") System,
        }
    }

    class Decoration(
        @get:JsonProperty("translation_key") val translationKey: String,
        @get:JsonProperty("parameters") val parameters: List<Parameter>,
        @get:JsonProperty("style") val style: Style,
    ) {
        enum class Parameter {
            @JsonProperty("team_name") TeamName,
            @JsonProperty("sender") Sender,
            @JsonProperty("content") Content
        }
    }
}
