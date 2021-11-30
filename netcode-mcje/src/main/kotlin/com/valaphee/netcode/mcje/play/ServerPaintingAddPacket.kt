/*
 * MIT License
 *
 * Copyright (c) 2021, Valaphee.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.valaphee.netcode.mcje.play

import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mc.util.Direction
import com.valaphee.netcode.mcbe.util.math.Direction
import com.valaphee.netcode.mcje.ServerPlayPacketHandler
import com.valaphee.netcode.mcje.Packet
import com.valaphee.netcode.mcje.PacketBuffer
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
class ServerPaintingAddPacket(
    val entityId: Int,
    val entityUid: UUID,
    val painting: Painting,
    val position: Int3,
    val direction: Direction
) : Packet<ServerPlayPacketHandler> {
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
        Pool("Pool", 2, 1),
        Courbet("Courbet", 2, 1),
        Sea("Sea", 2, 1),
        Sunset("Sunset", 2, 1),
        Creebet("Creebet", 2, 1),
        Wanderer("Wanderer", 1, 2),
        Graham("Graham", 1, 2),
        Match("Match", 2, 2),
        Bust("Bust", 2, 2),
        Stage("Stage", 2, 2),
        Void("Void", 2, 2),
        SkullAndRoses("SkullAndRoses", 2, 2),
        Wither("Wither", 2, 2),
        Fighters("Fighters", 4, 2),
        Pointer("Pointer", 4, 4),
        PigScene("Pigscene", 4, 4),
        FlamingSkull("Flaming Skull", 4, 4),
        Skeleton("Skeleton", 4, 3),
        DonkeyKong("DonkeyKong", 4, 3);

        companion object {
            @JvmStatic
            fun byTitle(title: String) = byTitle[title]

            private val byTitle: Map<String, Painting> = HashMap<String, Painting>(values().size).apply { values().forEach { this[it.title] = it } }
        }
    }

    override fun read(buffer: PacketBuffer, version: Int) {
        entityId = buffer.readVarInt()
        if (version >= 498) {
            entityUid = buffer.readUuid()
            painting = Painting.values()[buffer.readVarInt()]
        } else painting = Painting.byTitle(buffer.readString(13))
        position = buffer.readInt3UnsignedY()
        when (buffer.readByte().toInt()) {
            0 -> direction = Direction.South
            1 -> direction = Direction.West
            2 -> direction = Direction.North
            3 -> direction = Direction.East
        }
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(entityId)
        if (version >= 498) {
            buffer.writeUuid(entityUid!!)
            buffer.writeVarInt(painting!!.ordinal)
        } else buffer.writeString(painting!!.title)
        buffer.writeInt3UnsignedY(position!!)
        @Suppress("NON_EXHAUSTIVE_WHEN") when (direction) {
            Direction.North -> buffer.writeByte(2)
            Direction.South -> buffer.writeByte(0)
            Direction.West -> buffer.writeByte(1)
            Direction.East -> buffer.writeByte(3)
        }
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.paintingAdd(this)
}
