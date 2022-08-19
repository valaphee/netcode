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

package com.valaphee.netcode.mcje.network.packet.play

import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.ServerPlayPacketHandler
import com.valaphee.netcode.mcje.world.SoundCategory

/**
 * @author Kevin Ludwig
 */
class ServerStatisticsPacket(
    val statistics: List<Statistic>
) : Packet<ServerPlayPacketHandler>() {
    data class Statistic(
        val categoryId: Int,
        val statisticId: Int,
        val value: Int
    )

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(statistics.size)
        statistics.forEach {
            buffer.writeVarInt(it.categoryId)
            buffer.writeVarInt(it.statisticId)
            buffer.writeVarInt(it.value)
        }
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.statistics(this)

    override fun toString() = "ServerStatisticsPacket(statistics=$statistics)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = ServerSoundPacket(buffer.readVarInt(), SoundCategory.values()[buffer.readVarInt()], buffer.readInt3().toMutableFloat3().scale(1 / 8.0f), buffer.readFloat(), buffer.readFloat())
    }
}
