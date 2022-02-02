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

package com.valaphee.netcode.mcbe.entity

import com.valaphee.netcode.mcbe.Packet
import com.valaphee.netcode.mcbe.PacketBuffer
import com.valaphee.netcode.mcbe.PacketHandler
import com.valaphee.netcode.mcbe.PacketReader

/**
 * @author Kevin Ludwig
 */
class NpcDialoguePacket(
    val uniqueEntityId: Long,
    val action: Action,
    val dialogue: String,
    val sceneName: String,
    val npcName: String,
    val actionJson: String
) : Packet() {
    enum class Action {
        Open,
        Close
    }

    override val id get() = 0xA9

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeLongLE(uniqueEntityId)
        buffer.writeVarInt(action.ordinal)
        buffer.writeString(dialogue)
        buffer.writeString(sceneName)
        buffer.writeString(npcName)
        buffer.writeString(actionJson)
    }

    override fun handle(handler: PacketHandler) = handler.npcDialogue(this)

    override fun toString() = "NpcDialoguePacket(uniqueEntityId=$uniqueEntityId, action=$action, dialogue='$dialogue', sceneName='$sceneName', npcName='$npcName', actionJson='$actionJson')"
}

/**
 * @author Kevin Ludwig
 */
object NpcDialoguePacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = NpcDialoguePacket(buffer.readLongLE(), NpcDialoguePacket.Action.values()[buffer.readVarInt()], buffer.readString(), buffer.readString(), buffer.readString(), buffer.readString())
}
