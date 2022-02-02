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
class Modal(
    title: String,
    @get:JsonProperty("content") val content: String,
    @get:JsonProperty("button1") val yesButtonText: String,
    @get:JsonProperty("button2") val noButtonText: String
) : Form<Boolean>(title) {
    override fun getResponse(json: JsonElement) = json.asBoolean

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Modal

        if (title != other.title) return false
        if (content != other.content) return false
        if (yesButtonText != other.yesButtonText) return false
        if (noButtonText != other.noButtonText) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + content.hashCode()
        result = 31 * result + yesButtonText.hashCode()
        result = 31 * result + noButtonText.hashCode()
        return result
    }

    override fun toString() = "Modal(title=$title, content=$content, yesButtonText=$yesButtonText, noButtonText=$noButtonText)"
}
