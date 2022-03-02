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

package com.valaphee.netcode.mcbe.pack

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize

/**
 * @author Kevin Ludwig
 */
@JsonSerialize(using = Version.Serializer::class)
@JsonDeserialize(using = Version.Deserializer::class)
data class Version(
    val major: Int,
    val minor: Int,
    val patch: Int,
    val build: Int = 0
) : Comparable<Version> {
    override fun compareTo(other: Version): Int {
        var result = Integer.compareUnsigned(major, other.major)
        if (result == 0) {
            result = Integer.compareUnsigned(minor, other.minor)
            if (result == 0) result = Integer.compareUnsigned(patch, other.patch)
        }
        return result
    }

    override fun toString() = "$major.$minor.$patch${if (build != 0) ".$build" else ""}"

    internal object Serializer : JsonSerializer<Version>() {
        override fun serialize(value: Version, generator: JsonGenerator, provider: SerializerProvider) {
            generator.writeArray(intArrayOf(value.major, value.minor, value.patch), 0, 3)
        }
    }

    internal object Deserializer : JsonDeserializer<Version>() {
        override fun deserialize(parser: JsonParser, context: DeserializationContext): Version {
            val array = parser.codec.readValue(parser, IntArray::class.java)
            return Version(array[0], array[1], array[2], if (array.size >= 4) array[3] else 0)
        }
    }
}
