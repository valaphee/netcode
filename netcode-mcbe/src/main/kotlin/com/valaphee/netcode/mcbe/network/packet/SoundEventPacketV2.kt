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
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.PacketReader
import com.valaphee.netcode.mcbe.world.SoundEvent

/**
 * @author Kevin Ludwig
 */
class SoundEventPacketV2(
    val soundEvent: SoundEvent,
    val position: Float3,
    val extraData: Int,
    val entityTypeKey: String,
    val babySound: Boolean,
    val relativeVolumeDisabled: Boolean
) : Packet() {
    override val id get() = 0x18

    override fun write(buffer: PacketBuffer, version: Int) {
        if (version >= 407) buffer.writeVarUInt(SoundEvent.registry.getId(soundEvent)) else buffer.writeByte(SoundEvent.registry.getId(soundEvent))
        buffer.writeFloat3(position)
        buffer.writeVarInt(extraData)
        buffer.writeString(entityTypeKey)
        buffer.writeBoolean(babySound)
        buffer.writeBoolean(relativeVolumeDisabled)
    }

    override fun handle(handler: PacketHandler) = handler.soundEventV2(this)

    override fun toString() = "SoundEventPacketV2(soundEvent=$soundEvent, position=$position, extraData=$extraData, entityTypeKey='$entityTypeKey', babySound=$babySound, relativeVolumeDisabled=$relativeVolumeDisabled)"
}

/**
 * @author Kevin Ludwig
 */
object SoundEventPacketV2Reader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = SoundEventPacketV2(checkNotNull(SoundEvent.registry[if (version >= 407) buffer.readVarUInt() else buffer.readUnsignedByte().toInt()]), buffer.readFloat3(), buffer.readVarInt(), buffer.readString(), buffer.readBoolean(), buffer.readBoolean())
}
