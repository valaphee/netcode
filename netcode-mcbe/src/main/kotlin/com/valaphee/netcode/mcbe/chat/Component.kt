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

package com.valaphee.netcode.mcbe.chat

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize

/**
 * @author Kevin Ludwig
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes(
    JsonSubTypes.Type(TextComponent::class),
    JsonSubTypes.Type(TranslateComponent::class),
    JsonSubTypes.Type(ScoreComponent::class)
)
@JsonSerialize
@JsonDeserialize
sealed class Component

/**
 * @author Kevin Ludwig
 */
data class TextComponent(
    @get:JsonProperty("text") var text: String
) : Component()

/**
 * @author Kevin Ludwig
 */
data class TranslateComponent(
    @get:JsonProperty("translate") var name: String,
    @get:JsonProperty("with") var arguments: Array<Component> = emptyArray()
) : Component() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TranslateComponent

        if (name != other.name) return false
        if (!arguments.contentEquals(other.arguments)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + arguments.contentHashCode()
        return result
    }
}

/**
 * @author Kevin Ludwig
 */
data class ScoreComponent(
    @get:JsonProperty("name") var name: String,
    @get:JsonProperty("objective") var objective: String,
    @get:JsonProperty("value") var value: String
) : Component()
