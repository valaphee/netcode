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
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaType

/**
 * @author Kevin Ludwig
 */
interface Component

/**
 * @author Kevin Ludwig
 */
abstract class WrapperComponent<T>(
    val value: T
) : Component

/**
 * @author Kevin Ludwig
 */
class ComponentsSerializer : JsonSerializer<List<Component>>() {
    override fun serialize(value: List<Component>, generator: JsonGenerator, serializer: SerializerProvider) {
        generator.writeStartObject()
        value.forEach { generator.writeObjectField(DataTypeResolver.typeByClass(it::class), if (it is WrapperComponent<*>) it.value else it) }
        generator.writeEndObject()
    }
}

/**
 * @author Kevin Ludwig
 */
class ComponentsDeserializer : JsonDeserializer<List<Component>>() {
    override fun deserialize(parser: JsonParser, context: DeserializationContext): List<Component> {
        val node = parser.readValueAsTree<JsonNode>()
        val codec = parser.codec
        return if (node.isObject) mutableListOf<Component>().apply {
            node.fields().forEach {
                val `class` = DataTypeResolver.classByType(it.key)
                add((if (`class`.isSubclassOf(WrapperComponent::class)) `class`.primaryConstructor!!.call(it.value.traverse(codec).readValueAs<Any>(object : TypeReference<Any>() {
                    override fun getType() = `class`.primaryConstructor!!.parameters.first().type.javaType
                })) else it.value.traverse(codec).readValueAs(`class`.java)) as Component)
            }
        } else error("")
    }
}
