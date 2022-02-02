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

import com.valaphee.foundry.math.Float3
import com.valaphee.netcode.mc.util.Direction
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.PacketReader
import com.valaphee.netcode.mcbe.Restrict
import com.valaphee.netcode.mcbe.Restriction

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class PaintingAddPacket(
    val uniqueEntityId: Long,
    val runtimeEntityId: Long,
    val position: Float3,
    val direction: Direction,
    val painting: Painting
) : Packet() {
    enum class Painting(
        val title: String,
        val width: Int,
        val height: Int
    ) {
        Kebab("Kebab", 1, 1),
        Aztec("Aztec", 1, 1),
        Alban("Alban", 1, 1),
        Aztec2("Aztec2", 1, 1),
        Bomb("Bomb", 1, 1),
        Plant("Plant", 1, 1),
        Wasteland("Wasteland", 1, 1),
        Wanderer("Wanderer", 1, 2),
        Graham("Graham", 1, 2),
        Pool("Pool", 2, 1),
        Courbet("Courbet", 2, 1),
        Sea("Sea", 2, 1),
        Sunset("Sunset", 2, 1),
        Creebet("Creebet", 2, 1),
        Match("Match", 2, 2),
        Bust("Bust", 2, 2),
        Stage("Stage", 2, 2),
        Void("Void", 2, 2),
        SkullAndRoses("SkullAndRoses", 2, 2),
        Wither("Wither", 2, 2),
        Fighters("Fighters", 4, 2),
        Skeleton("Skeleton", 4, 3),
        DonkeyKong("DonkeyKong", 4, 3),
        Pointer("Pointer", 4, 4),
        PigScene("Pigscene", 4, 4),
        FlamingSkull("Flaming Skull", 4, 4);

        companion object {
            fun byTitle(title: String) = checkNotNull(byTitle[title])

            private val byTitle: Map<String, Painting> = HashMap<String, Painting>(values().size).apply { values().forEach { this[it.title] = it } }
        }
    }

    override val id get() = 0x16

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarLong(uniqueEntityId)
        buffer.writeVarULong(runtimeEntityId)
        buffer.writeFloat3(position)
        when (direction) {
            Direction.North -> buffer.writeVarInt(2)
            Direction.South -> buffer.writeVarInt(0)
            Direction.West -> buffer.writeVarInt(1)
            Direction.East -> buffer.writeVarInt(3)
            else -> throw IndexOutOfBoundsException()
        }
        buffer.writeString(painting.title)
    }

    override fun handle(handler: PacketHandler) = handler.paintingAdd(this)

    override fun toString() = "PaintingAddPacket(uniqueEntityId=$uniqueEntityId, runtimeEntityId=$runtimeEntityId, position=$position, direction=$direction, painting=$painting)"
}

/**
 * @author Kevin Ludwig
 */
object PaintingAddPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = PaintingAddPacket(
        buffer.readVarLong(),
        buffer.readVarULong(),
        buffer.readFloat3(),
        when (buffer.readVarInt()) {
            0 -> Direction.South
            1 -> Direction.West
            2 -> Direction.North
            3 -> Direction.East
            else -> throw IndexOutOfBoundsException()
        },
        PaintingAddPacket.Painting.byTitle(buffer.readString())
    )
}
