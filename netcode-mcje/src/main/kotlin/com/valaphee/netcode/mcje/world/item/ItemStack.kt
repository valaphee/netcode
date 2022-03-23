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

package com.valaphee.netcode.mcje.world.item

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.module.kotlin.readValue
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.util.NamespacedKey
import com.valaphee.netcode.mcje.util.minecraftKey
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream
import java.io.OutputStream

/**
 * @author Kevin Ludwig
 */
@JsonSerialize(using = ItemStack.Serializer::class)
@JsonDeserialize(using = ItemStack.Deserializer::class)
data class ItemStack(
    val item: NamespacedKey,
    val count: Int = 1,
    val data: Any? = null,
) {
    fun equalsIgnoreCount(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ItemStack

        if (item != other.item) return false
        if (data != other.data) return false

        return true
    }

    object Serializer : JsonSerializer<ItemStack>() {
        override fun serialize(value: ItemStack, generator: JsonGenerator, provider: SerializerProvider) {
            generator.writeStartObject()
            generator.writeStringField("id", value.item.key)
            generator.writeNumberField("Count", value.count)
            value.data?.let { generator.writeObjectField("tag", value.data) }
            generator.writeEndObject()
        }
    }

    object Deserializer : JsonDeserializer<ItemStack>() {
        override fun deserialize(parser: JsonParser, context: DeserializationContext): ItemStack {
            val node = parser.readValueAsTree<JsonNode>()
            return ItemStack(minecraftKey(node["id"].textValue()), node["Count"]?.intValue() ?: 1, context.readTreeAsValue(node["tag"], Any::class.java))
        }
    }
}

fun PacketBuffer.readItemStack() = if (readBoolean()) ItemStack(registries.items[readVarInt()]!!, readByte().toInt(), nbtObjectMapper.readValue(ByteBufInputStream(buffer))) else null

fun PacketBuffer.writeItemStack(value: ItemStack?) {
    value?.let {
        writeBoolean(true)
        writeVarInt(registries.items.getId(it.item))
        writeByte(it.count)
        nbtObjectMapper.writeValue(ByteBufOutputStream(buffer) as OutputStream, it.data)
    } ?: writeBoolean(false)
}
