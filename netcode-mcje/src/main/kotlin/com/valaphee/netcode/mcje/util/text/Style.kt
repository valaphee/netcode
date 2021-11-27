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
