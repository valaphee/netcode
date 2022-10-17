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
    JsonSubTypes.Type(TranslatableComponent::class),
    JsonSubTypes.Type(ScoreComponent::class)
)
@JsonSerialize
@JsonDeserialize
sealed class Component

/**
 * @author Kevin Ludwig
 */
data class TextComponent(
    @JsonProperty("text") var text: String
) : Component()

/**
 * @author Kevin Ludwig
 */
data class TranslatableComponent(
    @JsonProperty("translate") var name: String,
    @JsonProperty("with") var arguments: List<Component> = emptyList()
) : Component()

/**
 * @author Kevin Ludwig
 */
data class ScoreComponent(
    @JsonProperty("name") var name: String,
    @JsonProperty("objective") var objective: String,
    @JsonProperty("value") var value: String
) : Component()
