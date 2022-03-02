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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonUnwrapped
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.util.regex.Pattern

/**
 * @author Kevin Ludwig
 */
@JsonSerialize(using = Component.Serializer::class)
@JsonDeserialize(using = Component.Deserializer::class)
sealed class Component {
    abstract var style: Style

    abstract operator fun plusAssign(component: Component)

    abstract fun print(stringBuilder: StringBuilder)

    override fun toString() = StringBuilder().apply { print(this) }.toString()

    internal class Serializer : JsonSerializer<Component>() {
        override fun serialize(value: Component, generator: JsonGenerator, provider: SerializerProvider) {
            if (value is TextComponent && value.style.empty) generator.writeString(value.text) else generator.writeObject(value)
        }
    }

    internal class Deserializer : JsonDeserializer<Component>() {
        override fun deserialize(parser: JsonParser, context: DeserializationContext): Component {
            val treeNode = parser.readValueAsTree<TreeNode>()
            return when (treeNode.asToken()) {
                JsonToken.START_OBJECT -> parser.codec.treeToValue(treeNode, BaseComponent::class.java)
                JsonToken.VALUE_STRING -> TextComponent(text = parser.codec.treeToValue(treeNode, String::class.java))
                JsonToken.VALUE_NUMBER_INT -> TextComponent(text = parser.codec.treeToValue(treeNode, Int::class.java).toString())
                JsonToken.VALUE_NUMBER_FLOAT -> TextComponent(text = parser.codec.treeToValue(treeNode, Float::class.java).toString())
                JsonToken.VALUE_TRUE -> TextComponent(text = "true")
                JsonToken.VALUE_FALSE -> TextComponent(text = "false")
                JsonToken.VALUE_NULL -> TextComponent(text = "")
                else -> TODO()
            }
        }
    }
}

/**
 * @author Kevin Ludwig
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes(
    JsonSubTypes.Type(TextComponent::class),
    JsonSubTypes.Type(TranslatableComponent::class),
    JsonSubTypes.Type(ScoreComponent::class),
    JsonSubTypes.Type(SelectorComponent::class),
    JsonSubTypes.Type(KeybindComponent::class)
)
@JsonSerialize
@JsonDeserialize
abstract class BaseComponent(
    @JsonProperty("color") color: String? = null,
    @JsonProperty("bold") bold: Boolean? = null,
    @JsonProperty("italic") italic: Boolean? = null,
    @JsonProperty("underlined") underlined: Boolean? = null,
    @JsonProperty("strikethrough") strikethrough: Boolean? = null,
    @JsonProperty("obfuscated") obfuscated: Boolean? = null,
    @JsonProperty("insertion") insertion: String? = null,
    @JsonProperty("hoverEvent") hoverEvent: HoverEvent? = null,
    @JsonProperty("clickEvent") clickEvent: ClickEvent? = null,
    @get:JsonProperty("extra") val siblings: MutableList<Component>
) : Component() {
    @get:JsonUnwrapped override var style = Style(color, bold, italic, underlined, strikethrough, obfuscated, insertion, hoverEvent, clickEvent)
        set(value) {
            field = value
            siblings.forEach { (it as BaseComponent).style.parent = style }
        }

    override operator fun plusAssign(component: Component) {
        (component as BaseComponent).style.parent = style
        siblings += component
    }

    override fun print(stringBuilder: StringBuilder) {
        siblings.forEach { it.print(stringBuilder) }
    }
}

/**
 * @author Kevin Ludwig
 */
class TextComponent(
    siblings: MutableList<Component> = mutableListOf(),
    @get:JsonProperty("text") var text: String
) : BaseComponent(siblings = siblings) {
    override fun print(stringBuilder: StringBuilder) {
        style.print(stringBuilder)
        stringBuilder.append(text)
        super.print(stringBuilder)
    }
}

/**
 * @author Kevin Ludwig
 */
class TranslatableComponent(
    siblings: MutableList<Component> = mutableListOf(),
    @get:JsonProperty("translate") var key: String,
    @get:JsonProperty("with") var arguments: List<Component> = emptyList()
) : BaseComponent(siblings = siblings) {
    override fun print(stringBuilder: StringBuilder) {
        val value = key
        val matcher = pattern.matcher(value)
        var position = 0
        var i = 0
        while (matcher.find()) {
            val nextPosition = matcher.start()
            if (nextPosition != position) {
                style.print(stringBuilder)
                stringBuilder.append(value, position, nextPosition)
            }
            position = matcher.end()
            when (matcher.group(2)[0]) {
                's', 'd' -> arguments[matcher.group(1)?.toInt()?.minus(1) ?: i++].print(stringBuilder)
                '%' -> {
                    style.print(stringBuilder)
                    stringBuilder.append('%')
                }
            }
        }
        if (value.length != position) {
            style.print(stringBuilder)
            stringBuilder.append(value.substring(position))
        }
        super.print(stringBuilder)
    }

    companion object {
        private val pattern = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)")
    }
}

/**
 * @author Kevin Ludwig
 */
class ScoreComponent(
    siblings: MutableList<Component> = mutableListOf(),
    @get:JsonProperty("name") var name: String,
    @get:JsonProperty("objective") var objective: String
) : BaseComponent(siblings = siblings) {
    override fun print(stringBuilder: StringBuilder) {
        style.print(stringBuilder)
        super.print(stringBuilder)
    }
}

/**
 * @author Kevin Ludwig
 */
class SelectorComponent(
    siblings: MutableList<Component> = mutableListOf(),
    @get:JsonProperty("selector") var pattern: String
) : BaseComponent(siblings = siblings) {
    override fun print(stringBuilder: StringBuilder) {
        style.print(stringBuilder)
        stringBuilder.append(pattern)
        super.print(stringBuilder)
    }
}

/**
 * @author Kevin Ludwig
 */
class KeybindComponent(
    siblings: MutableList<Component> = mutableListOf(),
    @get:JsonProperty("keybind") var binding: String
) : BaseComponent(siblings = siblings) {
    override fun print(stringBuilder: StringBuilder) {
        style.print(stringBuilder)
        stringBuilder.append(binding)
        super.print(stringBuilder)
    }
}
