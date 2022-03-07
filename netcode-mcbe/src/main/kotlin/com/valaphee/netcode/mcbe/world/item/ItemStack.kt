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

package com.valaphee.netcode.mcbe.world.item

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.valaphee.jackson.dataformat.nbt.NbtFactory
import com.valaphee.jackson.dataformat.nbt.NbtGenerator
import com.valaphee.jackson.dataformat.nbt.NbtParser
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.world.block.BlockState
import com.valaphee.netcode.util.safeList
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream
import java.io.OutputStream
import java.util.Base64

/**
 * @author Kevin Ludwig
 */
@JsonSerialize(using = ItemStack.Serializer::class)
@JsonDeserialize(using = ItemStack.Deserializer::class)
data class ItemStack(
    val itemKey: String,
    val subId: Int = 0,
    val count: Int = 1,
    val data: Any? = null,
    val canPlaceOn: List<String>? = null,
    val canDestroy: List<String>? = null,
    val blockingTicks: Long = 0,
    var netId: Int = 0,
    val blockState: BlockState? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ItemStack

        if (itemKey != other.itemKey) return false
        if (subId != other.subId) return false
        if (count != other.count) return false
        if (data != other.data) return false

        return true
    }

    fun equalsIgnoreCount(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ItemStack

        if (itemKey != other.itemKey) return false
        if (subId != other.subId) return false
        if (data != other.data) return false

        return true
    }

    override fun hashCode(): Int {
        var result = itemKey.hashCode()
        result = 31 * result + subId
        result = 31 * result + count
        result = 31 * result + (data?.hashCode() ?: 0)
        return result
    }

    object Serializer : JsonSerializer<ItemStack>() {
        private val base64Encoder = Base64.getEncoder()
        private val objectMapper = ObjectMapper(NbtFactory().apply { enable(NbtFactory.Feature.LittleEndian) }).apply { registerKotlinModule() }

        override fun serialize(value: ItemStack, generator: JsonGenerator, provider: SerializerProvider) {
            generator.writeStartObject()
            when (generator) {
                is NbtGenerator -> {
                    generator.writeStringField("Name", value.itemKey)
                    generator.writeNumberField("Damage", value.subId.toShort())
                    generator.writeFieldName("Count")
                    generator.writeNumber(value.count.toByte())
                    value.data?.let { generator.writeObjectField("tag", value.data) }
                }
                else -> {
                    generator.writeStringField("item", value.itemKey)
                    generator.writeNumberField("data", value.subId)
                    generator.writeNumberField("count", value.count)
                    value.data?.let { generator.writeStringField("netcode:data", base64Encoder.encodeToString(objectMapper.writeValueAsBytes(it))) }
                    value.blockState?.let { generator.writeStringField("netcode:block_state", it.toString()) }
                }
            }
            generator.writeEndObject()
        }
    }

    object Deserializer : JsonDeserializer<ItemStack>() {
        private val base64Decoder = Base64.getDecoder()
        private val objectMapper = ObjectMapper(NbtFactory().apply { enable(NbtFactory.Feature.LittleEndian) }).apply { registerKotlinModule() }

        override fun deserialize(parser: JsonParser, context: DeserializationContext): ItemStack {
            val node = parser.readValueAsTree<JsonNode>()
            return when (parser) {
                is NbtParser -> ItemStack(node["Name"].asText(), node["Damage"]?.asInt() ?: 0, node["Count"]?.asInt() ?: 1)
                else -> ItemStack(node["item"].asText(), node["data"]?.asInt() ?: 0, node["count"]?.asInt() ?: 1, node["netcode:data"]?.let { objectMapper.readValue(base64Decoder.decode(it.asText())) }, blockState = node["netcode:block_state"]?.let  { BlockState(it.asText()) })
            }
        }
    }
}

fun PacketBuffer.readStackPre431(): ItemStack? {
    val id = readVarInt()
    if (id == 0) return null
    val itemKey = registries.items[id]!!
    val countAndSubId = readVarInt()
    return ItemStack(
        itemKey,
        (countAndSubId shr 8).let { if (it == Short.MAX_VALUE.toInt()) -1 else it },
        countAndSubId and 0xFF,
        readShortLE().toInt().let {
            when {
                it > 0 -> littleEndianNbtObjectMapper.readValue(ByteBufInputStream(readSlice(it)))
                it == -1 -> if (readVarUInt() == 1) littleEndianVarIntNbtObjectMapper.readValue(ByteBufInputStream(this)) else null
                else -> null
            }
        },
        readVarInt().let { if (it == 0) null else safeList(it) { readString() } },
        readVarInt().let { if (it == 0) null else safeList(it) { readString() } },
        if (itemKey == shieldKey) readVarLong() else 0
    )
}

fun PacketBuffer.readStackWithNetIdPre431(): ItemStack? {
    val netId = readVarInt()
    val stack = readStackPre431()
    stack?.let { it.netId = netId }
    return stack
}

fun PacketBuffer.readStack(): ItemStack? {
    val id = readVarInt()
    if (id == 0) return null
    val itemKey = registries.items[id]!!
    val count = readUnsignedShortLE()
    val subId = readVarUInt()
    val netId = if (readBoolean()) readVarInt() else 0
    val blockStateId = readVarInt()
    readVarUInt()
    return ItemStack(
        itemKey,
        subId,
        count,
        readShortLE().toInt().let {
            when {
                it > 0 -> littleEndianNbtObjectMapper.readValue(ByteBufInputStream(readSlice(it)))
                it == -1 -> if (readUnsignedByte().toInt() == 1) littleEndianNbtObjectMapper.readValue<Map<String, Any>>(ByteBufInputStream(this)) else null
                else -> null
            }
        },
        readIntLE().let { if (it == 0) null else safeList(it) { readString16() } },
        readIntLE().let { if (it == 0) null else safeList(it) { readString16() } },
        if (itemKey == shieldKey) readLongLE() else 0,
        netId,
        if (blockStateId != 0) registries.blockStates[blockStateId]!! else null
    )
}

fun PacketBuffer.readStackInstance(): ItemStack? {
    val id = readVarInt()
    if (id == 0) return null
    val itemKey = registries.items[id]!!
    val count = readUnsignedShortLE()
    val subId = readVarUInt()
    val blockStateId = readVarInt()
    readVarUInt()
    return ItemStack(
        itemKey,
        subId,
        count,
        readShortLE().toInt().let {
            when {
                it > 0 -> littleEndianNbtObjectMapper.readValue(ByteBufInputStream(readSlice(it)))
                it == -1 -> if (readUnsignedByte().toInt() == 1) littleEndianNbtObjectMapper.readValue(ByteBufInputStream(this)) else null
                else -> null
            }
        },
        readIntLE().let { if (it == 0) null else safeList(it) { readString16() } },
        readIntLE().let { if (it == 0) null else safeList(it) { readString16() } },
        if (itemKey == shieldKey) readLongLE() else 0,
        0,
        if (blockStateId != 0) registries.blockStates[blockStateId]!! else null
    )
}

fun PacketBuffer.readIngredient(): ItemStack? {
    val id = readVarInt()
    if (id == 0) return null
    return ItemStack(registries.items[id]!!, readVarInt().let { if (it == Short.MAX_VALUE.toInt()) -1 else it }, readVarInt())
}

fun PacketBuffer.writeStackPre431(value: ItemStack?) {
    value?.let {
        val itemId = registries.items.getId(it.itemKey)
        writeVarInt(itemId)
        if (itemId != 0) {
            writeVarInt(((if (it.subId == -1) Short.MAX_VALUE.toInt() else it.subId) shl 8) or (it.count and 0xFF))
            it.data?.let {
                writeShortLE(-1)
                writeVarUInt(1)
                littleEndianVarIntNbtObjectMapper.writeValue(ByteBufOutputStream(this) as OutputStream, it)
            } ?: writeShortLE(0)
            it.canPlaceOn?.let {
                writeVarInt(it.size)
                it.forEach { writeString(it) }
            } ?: writeVarInt(0)
            it.canDestroy?.let {
                writeVarInt(it.size)
                it.forEach { writeString(it) }
            } ?: writeVarInt(0)
            if (it.itemKey == shieldKey) writeVarLong(it.blockingTicks)
        }
    } ?: writeVarInt(0)
}

fun PacketBuffer.writeStackWithNetIdPre431(value: ItemStack?) {
    writeVarInt(value?.netId ?: 0)
    writeStackPre431(value)
}

fun PacketBuffer.writeStack(value: ItemStack?) {
    value?.let {
        val itemId = registries.items.getId(it.itemKey)
        writeVarInt(itemId)
        if (itemId != 0) {
            writeShortLE(it.count)
            writeVarUInt(it.subId)
            if (it.netId != 0) {
                writeBoolean(true)
                writeVarInt(it.netId)
            } else writeBoolean(false)
            writeVarInt(it.blockState?.let { registries.blockStates.getId(it) } ?: 0)
            val dataIndex = buffer.writerIndex()
            writeZero(PacketBuffer.MaximumVarUIntLength)
            it.data?.let {
                writeShortLE(-1)
                writeByte(1)
                littleEndianNbtObjectMapper.writeValue(ByteBufOutputStream(this) as OutputStream, it)
            } ?: writeShortLE(0)
            it.canPlaceOn?.let {
                writeIntLE(it.size)
                it.forEach { writeString16(it) }
            } ?: writeIntLE(0)
            it.canDestroy?.let {
                writeIntLE(it.size)
                it.forEach { writeString16(it) }
            } ?: writeIntLE(0)
            if (it.itemKey == shieldKey) writeLongLE(it.blockingTicks)
            setMaximumLengthVarUInt(dataIndex, writerIndex() - (dataIndex + PacketBuffer.MaximumVarUIntLength))
        }
    } ?: writeVarInt(0)
}

fun PacketBuffer.writeStackInstance(value: ItemStack?) {
    value?.let {
        val itemId = registries.items.getId(it.itemKey)
        writeVarInt(itemId)
        if (itemId != 0) {
            writeShortLE(it.count)
            writeVarUInt(it.subId)
            writeVarInt(it.blockState?.let { registries.blockStates.getId(it) } ?: 0)
            val dataIndex = buffer.writerIndex()
            writeZero(PacketBuffer.MaximumVarUIntLength)
            it.data?.let {
                writeShortLE(-1)
                writeVarUInt(1)
                littleEndianNbtObjectMapper.writeValue(ByteBufOutputStream(this) as OutputStream, it)
            } ?: writeShortLE(0)
            it.canPlaceOn?.let {
                writeIntLE(it.size)
                it.forEach { writeString16(it) }
            } ?: writeIntLE(0)
            it.canDestroy?.let {
                writeIntLE(it.size)
                it.forEach { writeString16(it) }
            } ?: writeIntLE(0)
            if (it.itemKey == shieldKey) writeLongLE(it.blockingTicks)
            setMaximumLengthVarUInt(dataIndex, writerIndex() - (dataIndex + PacketBuffer.MaximumVarUIntLength))
        }
    } ?: writeVarInt(0)
}

fun PacketBuffer.writeIngredient(value: ItemStack?) {
    value?.let {
        val itemId = registries.items.getId(it.itemKey)
        writeVarInt(itemId)
        if (itemId != 0) {
            writeVarInt(if (value.subId == -1) Short.MAX_VALUE.toInt() else value.subId)
            writeVarInt(value.count)
        }
    } ?: writeVarInt(0)
}

private const val shieldKey = "minecraft:shield"
private val littleEndianNbtObjectMapper = ObjectMapper(NbtFactory().apply { enable(NbtFactory.Feature.LittleEndian) })
private val littleEndianVarIntNbtObjectMapper = ObjectMapper(NbtFactory().apply {
    enable(NbtFactory.Feature.LittleEndian)
    enable(NbtFactory.Feature.VarInt)
})
