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

package com.valaphee.netcode.mcbe.world.item.crafting

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.valaphee.netcode.mcbe.world.item.ItemStack

/**
 * @author Kevin Ludwig
 */
object IngredientSerializer : JsonSerializer<ItemStack>() {
    override fun serialize(value: ItemStack, generator: JsonGenerator, provider: SerializerProvider) {
        generator.writeStartObject()
        generator.writeStringField("item", value.itemKey)
        if (value.subId != -1) generator.writeNumberField("data", value.subId)
        generator.writeNumberField("count", value.count)
        generator.writeEndObject()
    }
}

/**
 * @author Kevin Ludwig
 */
object SingleIngredientSerializer : JsonSerializer<ItemStack>() {
    override fun serialize(value: ItemStack, generator: JsonGenerator, provider: SerializerProvider) {
        generator.writeString("${value.itemKey}${if (value.subId != -1) ":${value.subId}" else ""}")
    }
}

/**
 * @author Kevin Ludwig
 */
object SingleItemSerializer : JsonSerializer<ItemStack>() {
    override fun serialize(value: ItemStack, generator: JsonGenerator, provider: SerializerProvider) {
        generator.writeString("${value.itemKey}${if (value.subId != 0) ":${value.subId}" else ""}")
    }
}

/**
 * @author Kevin Ludwig
 */
object IngredientDeserializer : JsonDeserializer<ItemStack>() {
    override fun deserialize(parser: JsonParser, context: DeserializationContext): ItemStack {
        val node = parser.readValueAsTree<JsonNode>()
        return ItemStack(node["item"].asText(), node["data"]?.asInt() ?: -1, node["count"]?.asInt() ?: 1)
    }
}

/**
 * @author Kevin Ludwig
 */
object SingleIngredientDeserializer : JsonDeserializer<ItemStack>() {
    override fun deserialize(parser: JsonParser, context: DeserializationContext): ItemStack {
        val item = parser.readValueAs(String::class.java)
        val subIdDelimiter = item.lastIndexOf(':')
        return (if (subIdDelimiter != -1) item.substring(subIdDelimiter + 1).toIntOrNull() else null)?.let { ItemStack(item.substring(0, subIdDelimiter), it) } ?: ItemStack(item, -1)
    }
}

/**
 * @author Kevin Ludwig
 */
object SingleItemDeserializer : JsonDeserializer<ItemStack>() {
    override fun deserialize(parser: JsonParser, context: DeserializationContext): ItemStack {
        val item = parser.readValueAs(String::class.java)
        val subIdDelimiter = item.lastIndexOf(':')
        return (if (subIdDelimiter != -1) item.substring(subIdDelimiter + 1).toIntOrNull() else null)?.let { ItemStack(item.substring(0, subIdDelimiter), it) } ?: ItemStack(item)
    }
}
