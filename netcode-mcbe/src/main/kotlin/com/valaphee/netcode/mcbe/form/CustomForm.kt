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
class CustomForm(
    title: String,
    @get:JsonProperty("content") val elements: List<Element>
) : Form<Map<String, Any?>>(title) {
    override fun getResponse(response: Any?) = (response as List<*>).mapIndexed { i, answer -> Pair(elements[i].text, elements[i].answer(answer)) }.toMap()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CustomForm

        if (title != other.title) return false
        if (elements != other.elements) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + elements.hashCode()
        return result
    }

    override fun toString() = "CustomForm(title=$title, elements=$elements)"
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
abstract class Element(
    @get:JsonProperty("text") val text: String
) {
    abstract fun answer(answer: Any?): Any?

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Element

        if (text != other.text) return false

        return true
    }

    override fun hashCode() = text.hashCode()
}

class Label(
    text: String
) : Element(text) {
    override fun answer(answer: Any?): Any? = null

    override fun toString() = "Label(text=$text)"
}

class Dropdown(
    text: String,
    @get:JsonProperty("options") val values: List<String>,
    @get:JsonProperty("default") var valueIndex: Int = 0
) : Element(text) {
    override fun answer(answer: Any?): Any {
        valueIndex = answer as Int
        return values[valueIndex]
    }

    override fun toString() = "Dropdown(text=$text, values=$values, valueIndex=$valueIndex)"
}

class Input(
    text: String,
    @get:JsonProperty("placeholder") val placeholder: String,
    @get:JsonProperty("default") var value: String = ""
) : Element(text) {
    override fun answer(answer: Any?): Any {
        value = answer as String
        return value
    }

    override fun toString() = "Input(text=$text, placeholder=$placeholder, value=$value)"
}

class Slider(
    text: String,
    @get:JsonProperty("min") val minimum: Number,
    @get:JsonProperty("max") val maximum: Number,
    @get:JsonProperty("step") val step: Number,
    @get:JsonProperty("default") var value: Number
) : Element(text) {
    override fun answer(answer: Any?): Any {
        value = answer as Number
        return value
    }

    override fun toString() = "Slider(text=$text, minimum=$minimum, maximum=$maximum, step=$step, value=$value)"
}

class StepSlider(
    text: String,
    @get:JsonProperty("steps") val values: List<String>,
    @get:JsonProperty("default") var valueIndex: Int = 0
) : Element(text) {
    override fun answer(answer: Any?): Any {
        valueIndex = answer as Int
        return values[valueIndex]
    }

    override fun toString() = "StepSlider(text=$text, values=$values, valueIndex=$valueIndex)"
}

class Toggle(
    text: String,
    @get:JsonProperty("default") var value: Boolean = false
) : Element(text) {
    override fun answer(answer: Any?): Any {
        value = answer as Boolean
        return value
    }

    override fun toString() = "Toggle(text=$text, value=$value)"
}
