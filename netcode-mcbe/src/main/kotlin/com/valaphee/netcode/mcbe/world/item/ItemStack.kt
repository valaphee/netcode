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
import com.valaphee.netcode.mcbe.network.V1_16_100
import com.valaphee.netcode.mcbe.network.V1_16_221
import com.valaphee.netcode.mcbe.world.block.BlockState
import com.valaphee.netcode.util.LazyList
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
    var subId: Int = 0,
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
        private val objectMapper = ObjectMapper(NbtFactory().enable(NbtFactory.Feature.LittleEndian)).registerKotlinModule()

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
        private val objectMapper = ObjectMapper(NbtFactory().enable(NbtFactory.Feature.LittleEndian)).registerKotlinModule()

        override fun deserialize(parser: JsonParser, context: DeserializationContext): ItemStack {
            val node = parser.readValueAsTree<JsonNode>()
            return when (parser) {
                is NbtParser -> ItemStack(node["Name"].textValue(), node["Damage"]?.intValue() ?: 0, node["Count"]?.intValue() ?: 1, context.readTreeAsValue(node["tag"], Any::class.java))
                else -> ItemStack(node["item"].textValue(), node["data"]?.intValue() ?: 0, node["count"]?.intValue() ?: 1, node["netcode:data"]?.let { objectMapper.readValue(base64Decoder.decode(it.textValue())) }, blockState = node["netcode:block_state"]?.let { BlockState(it.textValue()) })
            }
        }
    }
}

fun PacketBuffer.readItemStack(version: Int, withNetId: Boolean = true): ItemStack? {
    val itemId = readVarInt()
    if (itemId == 0) return null
    val itemKey = Item[version, itemId]!!
    if (version >= V1_16_221) {
        val count = readUnsignedShortLE()
        val subId = readVarUInt()
        val netId = if (withNetId && readBoolean()) readVarInt() else 0
        val blockStateId = readVarInt()
        readVarUInt()
        return ItemStack(
            itemKey,
            subId,
            count,
            readShortLE().toInt().let {
                when {
                    it > 0 -> nbtObjectMapper.readValue(ByteBufInputStream(readSlice(it)))
                    it == -1 -> if (readUnsignedByte().toInt() == 1) nbtObjectMapper.readValue<Map<String, Any>>(ByteBufInputStream(this)) else null
                    else -> null
                }
            },
            readIntLE().let { if (it == 0) null else LazyList(it) { readString16() } },
            readIntLE().let { if (it == 0) null else LazyList(it) { readString16() } },
            if (itemKey == "minecraft:shield") readLongLE() else 0,
            netId,
            if (blockStateId != 0) BlockState[version, blockStateId] else null
        )
    } else {
        val countAndSubId = readVarInt()
        return ItemStack(
            itemKey,
            (countAndSubId shr 8).let { if (it == Short.MAX_VALUE.toInt()) -1 else it },
            countAndSubId and 0xFF,
            readShortLE().toInt().let {
                when {
                    it > 0 -> nbtObjectMapper.readValue(ByteBufInputStream(readSlice(it)))
                    it == -1 -> if (readVarUInt() == 1) nbtVarIntObjectMapper.readValue(ByteBufInputStream(this)) else null
                    else -> null
                }
            },
            readVarInt().let { if (it == 0) null else LazyList(it) { readString() } },
            readVarInt().let { if (it == 0) null else LazyList(it) { readString() } },
            if (itemKey == "minecraft:shield") readVarLong() else 0
        )
    }
}

fun PacketBuffer.readItemStackWithNetId(version: Int) = if (version in V1_16_100 until V1_16_221) {
    val netId = readVarInt()
    readItemStack(version)?.apply { this.netId = netId }
} else readItemStack(version)

fun PacketBuffer.readIngredient(version: Int): ItemStack? {
    val itemId = readVarInt()
    if (itemId == 0) return null
    return ItemStack(Item[version, itemId]!!, readVarInt().let { if (it == Short.MAX_VALUE.toInt()) -1 else it }, readVarInt())
}

fun PacketBuffer.writeItemStack(value: ItemStack?, version: Int, withNetId: Boolean = true) {
    value?.let {
        val itemId = Item[version, it.itemKey]
        writeVarInt(itemId)
        if (itemId != 0) {
            if (version >= V1_16_221) {
                writeShortLE(it.count)
                writeVarUInt(it.subId)
                if (withNetId) if (it.netId != 0) {
                    writeBoolean(true)
                    writeVarInt(it.netId)
                } else writeBoolean(false)
                writeVarInt(it.blockState?.getId(version) ?: 0)
                val dataIndex = buffer.writerIndex()
                writeZero(PacketBuffer.MaximumVarUIntLength)
                it.data?.let {
                    writeShortLE(-1)
                    writeByte(1)
                    nbtObjectMapper.writeValue(ByteBufOutputStream(this) as OutputStream, it)
                } ?: writeShortLE(0)
                it.canPlaceOn?.let {
                    writeIntLE(it.size)
                    it.forEach { writeString16(it) }
                } ?: writeIntLE(0)
                it.canDestroy?.let {
                    writeIntLE(it.size)
                    it.forEach { writeString16(it) }
                } ?: writeIntLE(0)
                if (it.itemKey == "minecraft:shield") writeLongLE(it.blockingTicks)
                setMaximumLengthVarUInt(dataIndex, writerIndex() - (dataIndex + PacketBuffer.MaximumVarUIntLength))
            } else {
                writeVarInt(((if (it.subId == -1) Short.MAX_VALUE.toInt() else it.subId) shl 8) or (it.count and 0xFF))
                it.data?.let {
                    writeShortLE(-1)
                    writeVarUInt(1)
                    nbtVarIntObjectMapper.writeValue(ByteBufOutputStream(this) as OutputStream, it)
                } ?: writeShortLE(0)
                it.canPlaceOn?.let {
                    writeVarInt(it.size)
                    it.forEach { writeString(it) }
                } ?: writeVarInt(0)
                it.canDestroy?.let {
                    writeVarInt(it.size)
                    it.forEach { writeString(it) }
                } ?: writeVarInt(0)
                if (it.itemKey == "minecraft:shield") writeVarLong(it.blockingTicks)
            }
        }
    } ?: writeVarInt(0)
}

fun PacketBuffer.writeItemStackWithNetId(value: ItemStack?, version: Int) {
    if (version in V1_16_100 until V1_16_221) writeVarInt(value?.netId ?: 0)
    writeItemStack(value, version)
}

fun PacketBuffer.writeIngredient(value: ItemStack?, version: Int) {
    value?.let {
        val itemId = Item[version, it.itemKey]
        writeVarInt(itemId)
        if (itemId != 0) {
            writeVarInt(if (value.subId == -1) Short.MAX_VALUE.toInt() else value.subId)
            writeVarInt(value.count)
        }
    } ?: writeVarInt(0)
}
