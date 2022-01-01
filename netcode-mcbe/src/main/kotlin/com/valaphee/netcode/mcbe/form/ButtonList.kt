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

package com.valaphee.netcode.mcbe.form

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.JsonElement

/**
 * @author Kevin Ludwig
 */
class ButtonList(
    title: String,
    @get:JsonProperty("content") val content: String,
    @get:JsonProperty("buttons") val buttons: List<Button>
) : Form<String>(title) {
    override fun getResponse(json: JsonElement) = if (json.asInt >= buttons.size) null else buttons[json.asInt].text

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ButtonList

        if (title != other.title) return false
        if (content != other.content) return false
        if (buttons != other.buttons) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + content.hashCode()
        result = 31 * result + buttons.hashCode()
        return result
    }

    override fun toString() = "ButtonList(title=$title, content=$content, buttons=$buttons)"
}

data class Button(
    @get:JsonProperty("text") val text: String
)
