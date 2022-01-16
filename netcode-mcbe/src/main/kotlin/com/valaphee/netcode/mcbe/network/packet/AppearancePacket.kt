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

import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.PacketReader
import com.valaphee.netcode.mcbe.world.entity.player.Appearance
import com.valaphee.netcode.mcbe.world.entity.player.readAppearance
import com.valaphee.netcode.mcbe.world.entity.player.readAppearancePre390
import com.valaphee.netcode.mcbe.world.entity.player.readAppearancePre419
import com.valaphee.netcode.mcbe.world.entity.player.readAppearancePre428
import com.valaphee.netcode.mcbe.world.entity.player.readAppearancePre465
import com.valaphee.netcode.mcbe.world.entity.player.writeAppearance
import com.valaphee.netcode.mcbe.world.entity.player.writeAppearancePre390
import com.valaphee.netcode.mcbe.world.entity.player.writeAppearancePre419
import com.valaphee.netcode.mcbe.world.entity.player.writeAppearancePre428
import com.valaphee.netcode.mcbe.world.entity.player.writeAppearancePre465
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
class AppearancePacket(
    val userId: UUID,
    val appearance: Appearance,
    val newName: String,
    val oldName: String
) : Packet() {
    override val id get() = 0x5D

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeUuid(userId)
        if (version >= 465) buffer.writeAppearance(appearance) else if (version >= 428) buffer.writeAppearancePre465(appearance) else if (version >= 419) buffer.writeAppearancePre428(appearance) else if (version >= 390) buffer.writeAppearancePre419(appearance) else buffer.writeAppearancePre390(appearance)
        buffer.writeString(newName)
        buffer.writeString(oldName)
        if (version >= 390) buffer.writeBoolean(appearance.trusted)
    }

    override fun handle(handler: PacketHandler) = handler.appearance(this)

    override fun toString() = "AppearancePacket(userId=$userId, appearance=$appearance, newName='$newName', oldName='$oldName')"
}

/**
 * @author Kevin Ludwig
 */
object AppearancePacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = AppearancePacket(
        buffer.readUuid(),
        if (version >= 465) buffer.readAppearance() else if (version >= 428) buffer.readAppearancePre465() else if (version >= 419) buffer.readAppearancePre428() else if (version >= 390) buffer.readAppearancePre419() else buffer.readAppearancePre390(),
        buffer.readString(),
        buffer.readString()
    ).also { if (version >= 390) it.appearance.trusted = buffer.readBoolean() }
}
