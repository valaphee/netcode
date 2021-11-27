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

package com.valaphee.netcode.mcje.util

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize

fun linkOrMinecraftKey(string: String) = if (string.startsWith('#')) Link(string.substring(1)) else minecraftKey(string)

/**
 * @author Kevin Ludwig
 */
@JsonSerialize(using = LinkOrNamespacedKey.Serializer::class)
@JsonDeserialize(using = LinkOrNamespacedKey.Deserializer::class)
interface LinkOrNamespacedKey {
    fun resolve(resolve: (String) -> NamespacedKey?): NamespacedKey?

    class Serializer : JsonSerializer<Link>() {
        override fun serialize(value: Link, generator: JsonGenerator, provider: SerializerProvider) {
            generator.writeString(value.toString())
        }
    }

    class Deserializer : JsonDeserializer<LinkOrNamespacedKey>() {
        override fun deserialize(parser: JsonParser, context: DeserializationContext) = linkOrMinecraftKey(parser.codec.readValue(parser, String::class.java))
    }
}
