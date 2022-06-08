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

import com.valaphee.netcode.mcbe.command.Permission
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.PacketReader

/**
 * @author Kevin Ludwig
 */
class PermissionsPacket(
    val uniqueEntityId: Long,
    val permission: Permission,
    val customPermissions: Int
) : Packet() {
    override val id get() = 0x00

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeLongLE(uniqueEntityId)
        buffer.writeVarInt(permission.ordinal)
        buffer.writeShortLE(customPermissions)
    }

    override fun handle(handler: PacketHandler) = handler.permissions(this)

    override fun toString() = "PermissionsPacket(uniqueEntityId=$uniqueEntityId, permissions=$permission, customPermissions=$customPermissions)"
}

/**
 * @author Kevin Ludwig
 */
object PermissionsPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = PermissionsPacket(buffer.readLongLE(), Permission.values()[buffer.readVarInt()], buffer.readUnsignedShortLE())
}
