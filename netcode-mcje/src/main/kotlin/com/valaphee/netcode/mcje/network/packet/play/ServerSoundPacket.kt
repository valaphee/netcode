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

import com.valaphee.foundry.math.Float3
import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.PacketReader
import com.valaphee.netcode.mcje.network.ServerPlayPacketHandler
import com.valaphee.netcode.mcje.util.NamespacedKey
import com.valaphee.netcode.mcje.world.SoundCategory

/**
 * @author Kevin Ludwig
 */
class ServerSoundPacket(
    val soundKey: NamespacedKey,
    val category: SoundCategory,
    val position: Float3,
    val volume: Float,
    val pitch: Float
) : Packet<ServerPlayPacketHandler>() {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(buffer.registries.sounds.getId(soundKey))
        buffer.writeVarInt(category.ordinal)
        buffer.writeInt3(position.toMutableFloat3().scale(8.0f).toInt3())
        buffer.writeFloatLE(volume)
        buffer.writeFloatLE(pitch)
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.sound(this)

    override fun toString() = "ServerSoundPacket(soundKey=$soundKey, category=$category, position=$position, volume=$volume, pitch=$pitch)"
}

/**
 * @author Kevin Ludwig
 */
object ServerSoundPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerSoundPacket(checkNotNull(buffer.registries.sounds[buffer.readVarInt()]), SoundCategory.values()[buffer.readVarInt()], buffer.readInt3().toMutableFloat3().scale(1 / 8.0f), buffer.readFloat(), buffer.readFloat())
}
