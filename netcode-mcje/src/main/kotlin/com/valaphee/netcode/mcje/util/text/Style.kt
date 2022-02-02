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

package com.valaphee.netcode.mcje.util.text

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.valaphee.netcode.mc.util.text.Color
import com.valaphee.netcode.mc.util.text.Format

/**
 * @author Kevin Ludwig
 */
open class Style(
    @get:JsonProperty("color") var _color: String? = null,
    @get:JsonProperty("bold") var _bold: Boolean? = null,
    @get:JsonProperty("italic") var _italic: Boolean? = null,
    @get:JsonProperty("underlined") var _underlined: Boolean? = null,
    @get:JsonProperty("strikethrough") var _strikethrough: Boolean? = null,
    @get:JsonProperty("obfuscated") var _obfuscated: Boolean? = null,
    @get:JsonProperty("insertion") var _insertion: String? = null,
    @get:JsonProperty("hoverEvent") var _hoverEvent: HoverEvent? = null,
    @get:JsonProperty("clickEvent") var _clickEvent: ClickEvent? = null,
    @get:JsonIgnore var parent: Style? = Default
) {
    open val color: Color? @JsonIgnore get() = _color?.let { Color.byKey(it) } ?: parent!!.color
    open val bold: Boolean @JsonIgnore get() = _bold ?: parent!!.bold
    open val italic: Boolean @JsonIgnore get() = _italic ?: parent!!.italic
    open val underlined: Boolean @JsonIgnore get() = _underlined ?: parent!!.underlined
    open val strikethrough: Boolean @JsonIgnore get() = _strikethrough ?: parent!!.strikethrough
    open val obfuscated: Boolean @JsonIgnore get() = _obfuscated ?: parent!!.obfuscated
    open val insertion: String? @JsonIgnore get() = _insertion ?: parent!!.insertion
    open val hoverEvent: HoverEvent? @JsonIgnore get() = _hoverEvent ?: parent!!.hoverEvent
    open val clickEvent: ClickEvent? @JsonIgnore get() = _clickEvent ?: parent!!.clickEvent
    val empty get() = _color == null && _bold == null && _italic == null && _underlined == null && _strikethrough == null && _obfuscated == null && _insertion == null && _hoverEvent == null && _clickEvent == null

    open fun print(stringBuilder: StringBuilder) {
        color?.let { stringBuilder.append(color) }
        if (bold) stringBuilder.append(Format.Bold)
        if (italic) stringBuilder.append(Format.Italic)
        if (underlined) stringBuilder.append(Format.Underlined)
        if (strikethrough) stringBuilder.append(Format.Strikethrough)
        if (obfuscated) stringBuilder.append(Format.Obfuscated)
    }

    override fun toString() = StringBuilder().apply(this::print).toString()

    companion object {
        val Default = object : Style() {
            override val color: Color? get() = null

            override val bold get() = false

            override val italic get() = false

            override val underlined get() = false

            override val strikethrough get() = false

            override val obfuscated get() = false

            override val insertion: String? get() = null

            override val hoverEvent: HoverEvent? get() = null

            override val clickEvent: ClickEvent? get() = null
        }
    }
}
