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

package com.valaphee.netcode.mcbe.util.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.valaphee.foundry.math.Float4

/**
 * @author Kevin Ludwig
 */
object Float4Serializer : JsonSerializer<Float4>() {
    override fun serialize(value: Float4, generator: JsonGenerator, serializer: SerializerProvider) {
        generator.writeArray(floatArrayOf(value.x, value.y, value.z, value.w), 0, 4)
    }
}

/**
 * @author Kevin Ludwig
 */
object Float4Deserializer : JsonDeserializer<Float4>() {
    override fun deserialize(parser: JsonParser, context: DeserializationContext): Float4 {
        val array = parser.readValueAs(FloatArray::class.java)
        return Float4(array[0], array[1], array[2], array[3])
    }
}
