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
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

/**
 * @author Kevin Ludwig
 */
data class CustomForm(
    override val title: String,
    @get:JsonProperty("content") val elements: List<Element>
) : Form<Map<String, Any?>>() {
    override fun getResponse(response: Any?) = (response as List<*>).mapIndexed { i, answer -> Pair(elements[i].text, elements[i].answer(answer)) }.toMap()
}

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(Label::class, name = "label"),
    JsonSubTypes.Type(Dropdown::class, name = "dropdown"),
    JsonSubTypes.Type(Input::class, name = "input"),
    JsonSubTypes.Type(Slider::class, name = "slider"),
    JsonSubTypes.Type(StepSlider::class, name = "step_slider"),
    JsonSubTypes.Type(Toggle::class, name = "toggle"),
)
sealed interface Element {
    @get:JsonProperty("text") val text: String

    fun answer(answer: Any?): Any?
}

data class Label(
    override val text: String
) : Element {
    override fun answer(answer: Any?): Any? = null
}

data class Dropdown(
    override val text: String,
    @get:JsonProperty("options") val values: List<String>,
    @get:JsonProperty("default") var valueIndex: Int = 0
) : Element {
    override fun answer(answer: Any?): Any {
        valueIndex = answer as Int
        return values[valueIndex]
    }
}

data class Input(
    override val text: String,
    @get:JsonProperty("placeholder") val placeholder: String,
    @get:JsonProperty("default") var value: String = ""
) : Element {
    override fun answer(answer: Any?): Any {
        value = answer as String
        return value
    }
}

data class Slider(
    override val text: String,
    @get:JsonProperty("min") val minimum: Number,
    @get:JsonProperty("max") val maximum: Number,
    @get:JsonProperty("step") val step: Number,
    @get:JsonProperty("default") var value: Number
) : Element {
    override fun answer(answer: Any?): Any {
        value = answer as Number
        return value
    }
}

data class StepSlider(
    override val text: String,
    @get:JsonProperty("steps") val values: List<String>,
    @get:JsonProperty("default") var valueIndex: Int = 0
) : Element {
    override fun answer(answer: Any?): Any {
        valueIndex = answer as Int
        return values[valueIndex]
    }
}

data class Toggle(
    override val text: String,
    @get:JsonProperty("default") var value: Boolean = false
) : Element {
    override fun answer(answer: Any?): Any {
        value = answer as Boolean
        return value
    }
}
