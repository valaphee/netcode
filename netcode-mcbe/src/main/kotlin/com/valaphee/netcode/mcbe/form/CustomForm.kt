/*
 * MIT License
 *
 * Copyright (c) 2021, Valaphee.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.valaphee.netcode.mcbe.form

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.google.gson.JsonElement

/**
 * @author Kevin Ludwig
 */
class CustomForm(
    title: String,
    @get:JsonProperty("content") val elements: List<Element>
) : Form<Map<String, Any?>>(title) {
    override fun getResponse(json: JsonElement): Map<String, Any?> = json.asJsonArray.mapIndexed { i, jsonAnswer -> Pair(elements[i].text, elements[i].answer(jsonAnswer)) }.toMap()

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
    abstract fun answer(jsonAnswer: JsonElement): Any?

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
    override fun answer(jsonAnswer: JsonElement): Any? = null

    override fun toString() = "Label(text=$text)"
}

class Dropdown(
    text: String,
    @get:JsonProperty("options") val values: List<String>,
    @get:JsonProperty("default") var valueIndex: Int = 0
) : Element(text) {
    override fun answer(jsonAnswer: JsonElement): Any {
        valueIndex = jsonAnswer.asInt
        return values[valueIndex]
    }

    override fun toString() = "Dropdown(text=$text, values=$values, valueIndex=$valueIndex)"
}

class Input(
    text: String,
    @get:JsonProperty("placeholder") val placeholder: String,
    @get:JsonProperty("default") var value: String = ""
) : Element(text) {
    override fun answer(jsonAnswer: JsonElement): Any {
        value = jsonAnswer.asString
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
    override fun answer(jsonAnswer: JsonElement): Any {
        value = jsonAnswer.asDouble
        return value
    }

    override fun toString() = "Slider(text=$text, minimum=$minimum, maximum=$maximum, step=$step, value=$value)"
}

class StepSlider(
    text: String,
    @get:JsonProperty("steps") val values: List<String>,
    @get:JsonProperty("default") var valueIndex: Int = 0
) : Element(text) {
    override fun answer(jsonAnswer: JsonElement): Any {
        valueIndex = jsonAnswer.asInt
        return values[valueIndex]
    }

    override fun toString() = "StepSlider(text=$text, values=$values, valueIndex=$valueIndex)"
}

class Toggle(
    text: String,
    @get:JsonProperty("default") var value: Boolean = false
) : Element(text) {
    override fun answer(jsonAnswer: JsonElement): Any {
        value = jsonAnswer.asBoolean
        return value
    }

    override fun toString() = "Toggle(text=$text, value=$value)"
}
