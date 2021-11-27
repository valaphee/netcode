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

package com.valaphee.netcode.mcje.util.text

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
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
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

    internal class Serializer(
        `class`: Class<Component>? = null
    ) : StdSerializer<Component>(`class`) {
        override fun serialize(value: Component, generator: JsonGenerator, provider: SerializerProvider) {
            if (value is TextComponent && value.style.empty) generator.writeString(value.text) else generator.writeObject(value)
        }
    }

    internal class Deserializer(
        `class`: Class<*>? = null
    ) : StdDeserializer<Component>(`class`) {
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
    JsonSubTypes.Type(TranslateComponent::class),
    JsonSubTypes.Type(ScoreComponent::class),
    JsonSubTypes.Type(SelectorComponent::class),
    JsonSubTypes.Type(KeyBindComponent::class)
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
    @get:JsonUnwrapped
    override var style = Style(color, bold, italic, underlined, strikethrough, obfuscated, insertion, hoverEvent, clickEvent)
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
class TranslateComponent(
    siblings: MutableList<Component> = mutableListOf(),
    @get:JsonProperty("translate") var key: String,
    @get:JsonProperty("with") var arguments: Array<Component> = emptyArray()
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
class KeyBindComponent(
    siblings: MutableList<Component> = mutableListOf(),
    @get:JsonProperty("keybind") var binding: String
) : BaseComponent(siblings = siblings) {
    override fun print(stringBuilder: StringBuilder) {
        style.print(stringBuilder)
        stringBuilder.append(binding)
        super.print(stringBuilder)
    }
}
