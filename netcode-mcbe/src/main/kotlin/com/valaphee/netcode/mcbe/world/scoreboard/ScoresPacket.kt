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

package com.valaphee.netcode.mcbe.world.scoreboard

import com.valaphee.netcode.mcbe.Packet
import com.valaphee.netcode.mcbe.PacketBuffer
import com.valaphee.netcode.mcbe.PacketHandler
import com.valaphee.netcode.mcbe.PacketReader
import com.valaphee.netcode.mcbe.Restrict
import com.valaphee.netcode.mcbe.Restriction
import com.valaphee.netcode.util.safeList

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class ScoresPacket(
    val action: Action,
    val scores: List<Score>
) : Packet() {
    enum class Action {
        Set, Remove
    }

    override val id get() = 0x6C

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeByte(action.ordinal)
        buffer.writeVarUInt(scores.size)
        scores.forEach {
            buffer.writeVarLong(it.scoreboardId)
            buffer.writeString(it.objectiveName)
            buffer.writeIntLE(it.value)
            if (action == Action.Set) {
                buffer.writeByte(it.type.ordinal)
                @Suppress("NON_EXHAUSTIVE_WHEN")
                when (it.type) {
                    Score.ScorerType.Entity, Score.ScorerType.Player -> buffer.writeVarLong(it.entityId)
                    Score.ScorerType.Fake -> buffer.writeString(it.name!!)
                }
            }
        }
    }

    override fun handle(handler: PacketHandler) = handler.scores(this)

    override fun toString() = "ScoresPacket(action=$action, scores=$scores)"
}

/**
 * @author Kevin Ludwig
 */
object ScoresPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): ScoresPacket {
        val action = ScoresPacket.Action.values()[buffer.readUnsignedByte().toInt()]
        val entries = safeList(buffer.readVarUInt()) {
            val scoreboardId = buffer.readVarLong()
            val objectiveId = buffer.readString()
            val score = buffer.readIntLE()
            if (action == ScoresPacket.Action.Set) {
                when (val scorerType = Score.ScorerType.values()[buffer.readUnsignedByte().toInt()]) {
                    Score.ScorerType.Entity, Score.ScorerType.Player -> return@safeList Score(scoreboardId, objectiveId, score, scorerType, buffer.readVarLong())
                    Score.ScorerType.Fake -> return@safeList Score(scoreboardId, objectiveId, score, buffer.readString())
                }
            }
            Score(scoreboardId, objectiveId, score)
        }
        return ScoresPacket(action, entries)
    }
}