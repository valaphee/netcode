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

package com.valaphee.netcode.mcbe.network.packet

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.readValue
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.PacketReader
import com.valaphee.netcode.mcbe.network.Restrict
import com.valaphee.netcode.mcbe.network.Restriction
import com.valaphee.netcode.mcbe.world.inventory.WindowType
import com.valaphee.netcode.mcbe.world.item.ItemStack
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream
import java.io.OutputStream

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class TradePacket(
    val windowId: Int,
    val type: WindowType,
    val experience: Int,
    val level: Int,
    val uniqueEntityId: Long,
    val playerUniqueEntityId: Long,
    val title: String,
    val v2: Boolean,
    val restock: Boolean,
    val data: Data
) : Packet() {
    data class Data(
        @JsonProperty("Recipes") val offers: List<Offer>,
        @JsonProperty("TierExpRequirements") val tierExperienceRequirements: Map<Int, Int>
    ) {
        data class Offer(
            @JsonProperty("buyA") val buyA: ItemStack?,
            @JsonProperty("buyCountA") val buyCountA: Int,
            @JsonProperty("priceMultiplierA") val priceMultiplierA: Float,
            @JsonProperty("sell") val sell: ItemStack?,
            @JsonProperty("buyB") val buyB: ItemStack?,
            @JsonProperty("buyCountB") val buyCountB: Int?,
            @JsonProperty("priceMultiplierB") val priceMultiplierB: Float,
            @JsonProperty("tier") val tier: Int,
            @JsonProperty("uses") val sold: Int,
            @JsonProperty("maxUses") val stock: Int,
            @JsonProperty("traderExp") val experience: Int,
            @JsonProperty("reward") val reward: Boolean,
            @JsonProperty("demand") val demand: Int
        )
    }

    override val id get() = 0x50

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeByte(windowId)
        buffer.writeByte(type.id)
        buffer.writeVarInt(experience)
        buffer.writeVarInt(level)
        buffer.writeVarLong(uniqueEntityId)
        buffer.writeVarLong(playerUniqueEntityId)
        buffer.writeString(title)
        buffer.writeBoolean(v2)
        buffer.writeBoolean(restock)
        buffer.nbtVarIntObjectMapper.writeValue(ByteBufOutputStream(buffer) as OutputStream, data)
    }

    override fun handle(handler: PacketHandler) = handler.trade(this)

    override fun toString() = "TradePacket(windowId=$windowId, type=$type, experience=$experience, level=$level, uniqueEntityId=$uniqueEntityId, playerUniqueEntityId=$playerUniqueEntityId, title='$title', v2=$v2, restock=$restock, data=$data)"
}

/**
 * @author Kevin Ludwig
 */
object TradePacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = TradePacket(
        buffer.readByte().toInt(),
        WindowType.byId(buffer.readByte().toInt()),
        buffer.readVarInt(),
        buffer.readVarInt(),
        buffer.readVarLong(),
        buffer.readVarLong(),
        buffer.readString(),
        buffer.readBoolean(),
        buffer.readBoolean(),
        buffer.nbtVarIntObjectMapper.readValue(ByteBufInputStream(buffer))
    )
}
