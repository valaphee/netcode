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

import com.valaphee.foundry.math.Float2
import com.valaphee.foundry.math.Float3
import com.valaphee.netcode.mcbe.command.Permission
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.PacketReader
import com.valaphee.netcode.mcbe.network.Restrict
import com.valaphee.netcode.mcbe.network.Restriction
import com.valaphee.netcode.mcbe.world.WorldFlag
import com.valaphee.netcode.mcbe.world.entity.Link
import com.valaphee.netcode.mcbe.world.entity.metadata.Metadata
import com.valaphee.netcode.mcbe.world.entity.player.PlayerFlag
import com.valaphee.netcode.mcbe.world.entity.player.Rank
import com.valaphee.netcode.mcbe.world.entity.player.User
import com.valaphee.netcode.mcbe.world.entity.readLink
import com.valaphee.netcode.mcbe.world.entity.readLinkPre407
import com.valaphee.netcode.mcbe.world.entity.writeLink
import com.valaphee.netcode.mcbe.world.entity.writeLinkPre407
import com.valaphee.netcode.mcbe.world.item.ItemStack
import com.valaphee.netcode.mcbe.world.item.readItemStack
import com.valaphee.netcode.mcbe.world.item.readItemStackPre431
import com.valaphee.netcode.mcbe.world.item.writeItemStack
import com.valaphee.netcode.mcbe.world.item.writeItemStackPre431
import com.valaphee.netcode.util.safeList
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class PlayerAddPacket(
    val userId: UUID,
    val userName: String,
    val uniqueEntityId: Long,
    val runtimeEntityId: Long,
    val platformChatId: String,
    val position: Float3,
    val velocity: Float3,
    val rotation: Float2,
    val headRotationYaw: Float,
    val itemStackInHand: ItemStack?,
    val metadata: Metadata,
    val playerFlags: Set<PlayerFlag>,
    val permission: Permission,
    val worldFlags: Set<WorldFlag>,
    val rank: Rank,
    val customFlags: Int,
    val links: List<Link>,
    val deviceId: String,
    val operatingSystem: User.OperatingSystem
) : Packet() {
    override val id get() = 0x0C

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeUuid(userId)
        buffer.writeString(userName)
        buffer.writeVarLong(uniqueEntityId)
        buffer.writeVarULong(runtimeEntityId)
        buffer.writeString(platformChatId)
        buffer.writeFloat3(position)
        buffer.writeFloat3(velocity)
        buffer.writeFloat2(rotation)
        buffer.writeFloatLE(headRotationYaw)
        if (version >= 431) buffer.writeItemStack(itemStackInHand) else buffer.writeItemStackPre431(itemStackInHand)
        metadata.writeToBuffer(buffer)
        buffer.writeVarUIntFlags(playerFlags)
        buffer.writeVarUInt(permission.ordinal)
        buffer.writeVarUIntFlags(worldFlags)
        buffer.writeVarUInt(rank.ordinal)
        buffer.writeVarUInt(customFlags)
        buffer.writeLongLE(uniqueEntityId)
        buffer.writeVarUInt(links.size)
        if (version >= 407) links.forEach(buffer::writeLink) else links.forEach(buffer::writeLinkPre407)
        buffer.writeString(deviceId)
        buffer.writeIntLE(operatingSystem.ordinal - 1)
    }

    override fun handle(handler: PacketHandler) = handler.playerAdd(this)

    override fun toString() = "PlayerAddPacket(userId=$userId, userName='$userName', uniqueEntityId=$uniqueEntityId, runtimeEntityId=$runtimeEntityId, platformChatId='$platformChatId', position=$position, velocity=$velocity, rotation=$rotation, headRotationYaw=$headRotationYaw, stackInHand=$itemStackInHand, metadata=$metadata, playerFlags=$playerFlags, permission=$permission, worldFlags=$worldFlags, rank=$rank, customFlags=$customFlags, links=$links, deviceId='$deviceId', operatingSystem=$operatingSystem)"
}

/**
 * @author Kevin Ludwig
 */
object PlayerAddPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = PlayerAddPacket(
        buffer.readUuid(),
        buffer.readString(),
        buffer.readVarLong(),
        buffer.readVarULong(),
        buffer.readString(),
        buffer.readFloat3(),
        buffer.readFloat3(),
        buffer.readFloat2(),
        buffer.readFloatLE(),
        if (version >= 431) buffer.readItemStack() else buffer.readItemStackPre431(),
        Metadata().apply { readFromBuffer(buffer) },
        buffer.readVarUIntFlags(),
        Permission.values()[buffer.readVarUInt()],
        buffer.readVarUIntFlags(),
        Rank.values()[buffer.readVarUInt()],
        buffer.readVarUInt().also { buffer.readLongLE() },
        safeList(buffer.readVarUInt()) { if (version >= 407) buffer.readLink() else buffer.readLinkPre407() },
        buffer.readString(),
        User.OperatingSystem.values()[buffer.readIntLE() + 1],
    )
}
