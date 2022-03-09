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
    val item: String,
    var subId: Int = 0, // needed for je-be protocol translation
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

        if (item != other.item) return false
        if (subId != other.subId) return false
        if (count != other.count) return false
        if (data != other.data) return false

        return true
    }

    fun equalsIgnoreCount(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ItemStack

        if (item != other.item) return false
        if (subId != other.subId) return false
        if (data != other.data) return false

        return true
    }

    override fun hashCode(): Int {
        var result = item.hashCode()
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
                    generator.writeStringField("Name", value.item)
                    generator.writeNumberField("Damage", value.subId.toShort())
                    generator.writeFieldName("Count")
                    generator.writeNumber(value.count.toByte())
                    value.data?.let { generator.writeObjectField("tag", value.data) }
                }
                else -> {
                    generator.writeStringField("item", value.item)
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
                else -> ItemStack(node["item"].asText(), node["data"]?.asInt() ?: 0, node["count"]?.asInt() ?: 1, node["netcode:data"]?.let { objectMapper.readValue(base64Decoder.decode(it.asText())) }, blockState = node["netcode:block_state"]?.let { BlockState(it.asText()) })
            }
        }
    }
}

fun PacketBuffer.readItemStackPre431(): ItemStack? {
    val id = readVarInt()
    if (id == 0) return null
    val item = checkNotNull(registries.items[id])
    val countAndSubId = readVarInt()
    return ItemStack(
        item,
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
        if (item == shieldItem) readVarLong() else 0
    )
}

fun PacketBuffer.readItemStackWithNetIdPre431(): ItemStack? {
    val netId = readVarInt()
    val stack = readItemStackPre431()
    stack?.let { it.netId = netId }
    return stack
}

fun PacketBuffer.readItemStack(): ItemStack? {
    val id = readVarInt()
    if (id == 0) return null
    val item = checkNotNull(registries.items[id])
    val count = readUnsignedShortLE()
    val subId = readVarUInt()
    val netId = if (readBoolean()) readVarInt() else 0
    val blockStateId = readVarInt()
    readVarUInt()
    return ItemStack(
        item,
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
        if (item == shieldItem) readLongLE() else 0,
        netId,
        if (blockStateId != 0) checkNotNull(registries.blockStates[blockStateId]) else null
    )
}

fun PacketBuffer.readItemStackInstance(): ItemStack? {
    val id = readVarInt()
    if (id == 0) return null
    val item = checkNotNull(registries.items[id])
    val count = readUnsignedShortLE()
    val subId = readVarUInt()
    val blockStateId = readVarInt()
    readVarUInt()
    return ItemStack(
        item,
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
        if (item == shieldItem) readLongLE() else 0,
        0,
        if (blockStateId != 0) checkNotNull(registries.blockStates[blockStateId]) else null
    )
}

fun PacketBuffer.readIngredient(): ItemStack? {
    val id = readVarInt()
    if (id == 0) return null
    return ItemStack(checkNotNull(registries.items[id]), readVarInt().let { if (it == Short.MAX_VALUE.toInt()) -1 else it }, readVarInt())
}

fun PacketBuffer.writeItemStackPre431(value: ItemStack?) {
    value?.let {
        val item = registries.items.getId(it.item)
        writeVarInt(item)
        if (item != 0) {
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
            if (it.item == shieldItem) writeVarLong(it.blockingTicks)
        }
    } ?: writeVarInt(0)
}

fun PacketBuffer.writeItemStackWithNetIdPre431(value: ItemStack?) {
    writeVarInt(value?.netId ?: 0)
    writeItemStackPre431(value)
}

fun PacketBuffer.writeItemStack(value: ItemStack?) {
    value?.let {
        val item = registries.items.getId(it.item)
        writeVarInt(item)
        if (item != 0) {
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
            if (it.item == shieldItem) writeLongLE(it.blockingTicks)
            setMaximumLengthVarUInt(dataIndex, writerIndex() - (dataIndex + PacketBuffer.MaximumVarUIntLength))
        }
    } ?: writeVarInt(0)
}

fun PacketBuffer.writeItemStackInstance(value: ItemStack?) {
    value?.let {
        val item = registries.items.getId(it.item)
        writeVarInt(item)
        if (item != 0) {
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
            if (it.item == shieldItem) writeLongLE(it.blockingTicks)
            setMaximumLengthVarUInt(dataIndex, writerIndex() - (dataIndex + PacketBuffer.MaximumVarUIntLength))
        }
    } ?: writeVarInt(0)
}

fun PacketBuffer.writeIngredient(value: ItemStack?) {
    value?.let {
        val itemId = registries.items.getId(it.item)
        writeVarInt(itemId)
        if (itemId != 0) {
            writeVarInt(if (value.subId == -1) Short.MAX_VALUE.toInt() else value.subId)
            writeVarInt(value.count)
        }
    } ?: writeVarInt(0)
}

private const val shieldItem = "minecraft:shield"
private val littleEndianNbtObjectMapper = ObjectMapper(NbtFactory().apply { enable(NbtFactory.Feature.LittleEndian) })
private val littleEndianVarIntNbtObjectMapper = ObjectMapper(NbtFactory().apply {
    enable(NbtFactory.Feature.LittleEndian)
    enable(NbtFactory.Feature.VarInt)
})
