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
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.valaphee.netcode.mc.util.getCompoundTagOrNull
import com.valaphee.netcode.mc.util.getIntOrNull
import com.valaphee.netcode.mc.util.getString
import com.valaphee.netcode.mc.util.nbt.CompoundTag
import com.valaphee.netcode.mc.util.nbt.NbtInputStream
import com.valaphee.netcode.mc.util.nbt.NbtOutputStream
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.util.LittleEndianByteBufInputStream
import com.valaphee.netcode.mcbe.util.LittleEndianByteBufOutputStream
import com.valaphee.netcode.mcbe.util.LittleEndianVarIntByteBufInputStream
import com.valaphee.netcode.mcbe.util.LittleEndianVarIntByteBufOutputStream
import io.netty.buffer.Unpooled
import java.util.Base64

/**
 * @author Kevin Ludwig
 */
@JsonSerialize(using = ItemStackSerializer::class)
@JsonDeserialize(using = ItemStackDeserializer::class)
data class ItemStack(
    val itemKey: String,
    var subId: Int = 0,
    var count: Int = 1,
    var tag: CompoundTag? = null,
    var canPlaceOn: Array<String>? = null,
    var canDestroy: Array<String>? = null,
    var blockingTicks: Long = 0,
    var netId: Int = 0,
    var blockStateKey: String? = null
) {
    fun toTag(tag: CompoundTag): CompoundTag {
        tag.setString("Name", itemKey)
        tag.setShort("Damage", subId.toShort())
        tag.setByte("Count", count.toByte())
        this.tag?.let { tag["tag"] = it }
        return tag
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ItemStack

        if (itemKey != other.itemKey) return false
        if (subId != other.subId) return false
        if (count != other.count) return false
        if (tag != other.tag) return false
        /*if (blockStateKey != other.blockStateKey) return false*/

        return true
    }

    fun equalsIgnoreCount(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ItemStack

        if (itemKey != other.itemKey) return false
        if (subId != other.subId) return false
        if (tag != other.tag) return false
        /*if (blockStateKey != other.blockStateKey) return false*/

        return true
    }

    override fun hashCode(): Int {
        var result = itemKey.hashCode()
        result = 31 * result + subId
        result = 31 * result + count
        result = 31 * result + (tag?.hashCode() ?: 0)
        return result
    }
}

/**
 * @author Kevin Ludwig
 */
object ItemStackSerializer : JsonSerializer<ItemStack?>() {
    private val base64Encoder = Base64.getEncoder()

    override fun serialize(value: ItemStack?, generator: JsonGenerator, provider: SerializerProvider) {
        value?.let {
            generator.writeStartObject()
            generator.writeStringField("item", it.itemKey)
            if (it.subId != 0) generator.writeNumberField("subId", it.subId)
            if (it.count != 1) generator.writeNumberField("count", it.count)
            it.tag?.let { tag ->
                var buffer: PacketBuffer? = null
                try {
                    buffer = PacketBuffer(Unpooled.buffer(), true).also { it.toNbtOutputStream().use { it.writeTag(tag) } }
                    val array = ByteArray(buffer.readableBytes())
                    buffer.readBytes(array)
                    generator.writeStringField("tag", base64Encoder.encodeToString(array))
                } finally {
                    buffer?.release()
                }
            }
            it.blockStateKey?.let { generator.writeStringField("blockState", it) }
            generator.writeEndObject()
        } ?: generator.writeNull()
    }
}

/**
 * @author Kevin Ludwig
 */
class ItemStackDeserializer : JsonDeserializer<ItemStack?>() {
    private val base64Decoder = Base64.getDecoder()

    override fun deserialize(parser: JsonParser, context: DeserializationContext): ItemStack? {
        val node = parser.readValueAsTree<JsonNode>()
        return if (node.isNull) null else {
            var tag: CompoundTag? = null
            node["tag"]?.let {
                var buffer: PacketBuffer? = null
                try {
                    buffer = PacketBuffer(Unpooled.wrappedBuffer(base64Decoder.decode(it.asText())), true).also { tag = it.toNbtInputStream().use { it.readTag()?.asCompoundTag() } }
                } finally {
                    buffer?.release()
                }
            }
            ItemStack(checkNotNull(node["item"].asText()), node["subId"]?.asInt() ?: 0, node["count"]?.asInt() ?: 1, tag, blockStateKey = node["blockState"]?.asText())
        }
    }
}

fun CompoundTag.asStack() = ItemStack(getString("Name"), getIntOrNull("Damage") ?: 0, getIntOrNull("Count") ?: 1, getCompoundTagOrNull("tag"))

fun PacketBuffer.readStackPre431(): ItemStack? {
    val id = readVarInt()
    if (id == 0) return null
    val itemKey = registrySet.items[id] ?: "minecraft:unknown"
    val countAndSubId = readVarInt()
    return ItemStack(
        itemKey,
        (countAndSubId shr 8).let { if (it == Short.MAX_VALUE.toInt()) -1 else it },
        countAndSubId and 0xFF,
        readShortLE().toInt().let {
            when {
                it > 0 -> NbtInputStream(LittleEndianByteBufInputStream(readSlice(it))).use { it.readTag()?.asCompoundTag() }
                it == -1 -> NbtInputStream(LittleEndianVarIntByteBufInputStream(this)).use { if (readVarUInt() == 1) it.readTag()?.asCompoundTag() else null }
                else -> null
            }
        },
        readVarInt().let { if (it == 0) null else Array(it) { readString() } },
        readVarInt().let { if (it == 0) null else Array(it) { readString() } },
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
    val itemKey = registrySet.items[id] ?: "minecraft:unknown"
    val count = readUnsignedShortLE()
    val subId = readVarUInt()
    val netId = if (readBoolean()) readVarInt() else 0
    val blockRuntimeId = readVarInt()
    readVarUInt()
    return ItemStack(
        itemKey,
        subId,
        count,
        readShortLE().toInt().let {
            when {
                it > 0 -> NbtInputStream(LittleEndianByteBufInputStream(readSlice(it))).use { it.readTag()?.asCompoundTag() }
                it == -1 -> NbtInputStream(LittleEndianByteBufInputStream(this)).use { if (readUnsignedByte().toInt() == 1) it.readTag()?.asCompoundTag() else null }
                else -> null
            }
        },
        readIntLE().let { if (it == 0) null else Array(it) { readString16() } },
        readIntLE().let { if (it == 0) null else Array(it) { readString16() } },
        if (itemKey == shieldKey) readLongLE() else 0,
        netId,
        if (blockRuntimeId != 0) registrySet.blockStates[blockRuntimeId] ?: "minecraft:unknown" else null
    )
}

fun PacketBuffer.readStackInstance(): ItemStack? {
    val id = readVarInt()
    if (id == 0) return null
    val itemKey = registrySet.items[id] ?: "minecraft:unknown"
    val count = readUnsignedShortLE()
    val subId = readVarUInt()
    val blockRuntimeId = readVarInt()
    readVarUInt()
    return ItemStack(
        itemKey,
        subId,
        count,
        readShortLE().toInt().let {
            when {
                it > 0 -> NbtInputStream(LittleEndianByteBufInputStream(readSlice(it))).use { it.readTag()?.asCompoundTag() }
                it == -1 -> NbtInputStream(LittleEndianByteBufInputStream(this)).use { if (readUnsignedByte().toInt() == 1) it.readTag()?.asCompoundTag() else null }
                else -> null
            }
        },
        readIntLE().let { if (it == 0) null else Array(it) { readString16() } },
        readIntLE().let { if (it == 0) null else Array(it) { readString16() } },
        if (itemKey == shieldKey) readLongLE() else 0,
        0,
        if (blockRuntimeId != 0) registrySet.blockStates[blockRuntimeId] ?: "minecraft:unknown" else null
    )
}

fun PacketBuffer.readIngredient(): ItemStack? {
    val id = readVarInt()
    if (id == 0) return null
    return ItemStack(registrySet.items[id] ?: "minecraft:unknown", readVarInt().let { if (it == Short.MAX_VALUE.toInt()) -1 else it }, readVarInt())
}

fun PacketBuffer.writeStackPre431(value: ItemStack?) {
    value?.let {
        writeVarInt(registrySet.items.getId(it.itemKey))
        writeVarInt(((if (it.subId == -1) Short.MAX_VALUE.toInt() else it.subId) shl 8) or (it.count and 0xFF))
        it.tag?.let {
            writeShortLE(-1)
            writeVarUInt(1)
            NbtOutputStream(LittleEndianVarIntByteBufOutputStream(this)).use { stream -> stream.writeTag(it) }
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
    } ?: writeVarInt(0)
}

fun PacketBuffer.writeStackWithNetIdPre431(value: ItemStack?) {
    writeVarInt(value?.netId ?: 0)
    writeStackPre431(value)
}

fun PacketBuffer.writeStack(value: ItemStack?) {
    value?.let {
        writeVarInt(registrySet.items.getId(it.itemKey))
        writeShortLE(it.count)
        writeVarUInt(it.subId)
        if (it.netId != 0) {
            writeBoolean(true)
            writeVarInt(it.netId)
        } else writeBoolean(false)
        writeVarInt(it.blockStateKey?.let { registrySet.blockStates.getId(it) } ?: 0)
        val dataIndex = buffer.writerIndex()
        writeZero(PacketBuffer.MaximumVarUIntLength)
        it.tag?.let {
            writeShortLE(-1)
            writeByte(1)
            NbtOutputStream(LittleEndianByteBufOutputStream(this)).use { stream -> stream.writeTag(it) }
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
    } ?: writeVarInt(0)
}

fun PacketBuffer.writeStackInstance(value: ItemStack?) {
    value?.let {
        writeVarInt(registrySet.items.getId(it.itemKey))
        writeShortLE(it.count)
        writeVarUInt(it.subId)
        writeVarInt(it.blockStateKey?.let { registrySet.blockStates.getId(it) } ?: 0)
        val dataIndex = buffer.writerIndex()
        writeZero(PacketBuffer.MaximumVarUIntLength)
        it.tag?.let {
            writeShortLE(-1)
            writeVarUInt(1)
            NbtOutputStream(LittleEndianByteBufOutputStream(this)).use { stream -> stream.writeTag(it) }
        } ?: writeShortLE(0)
        it.canPlaceOn?.let {
            writeIntLE(it.size)
            it.forEach { writeString16(it) }
        } ?: writeIntLE(0)
        it.canDestroy?.let {
            writeIntLE(it.size)
            it.forEach { writeString16(it) }
        } ?: writeIntLE(0)
        if (it.itemKey == shieldKey) writeVarLong(it.blockingTicks)
        setMaximumLengthVarUInt(dataIndex, writerIndex() - (dataIndex + PacketBuffer.MaximumVarUIntLength))
    } ?: writeIntLE(0)
}

fun PacketBuffer.writeIngredient(value: ItemStack?) {
    value?.let {
        writeVarInt(registrySet.items.getId(it.itemKey))
        writeVarInt(if (value.subId == -1) Short.MAX_VALUE.toInt() else value.subId)
        writeVarInt(value.count)
    } ?: writeVarInt(0)
}

private const val shieldKey = "minecraft:shield"
