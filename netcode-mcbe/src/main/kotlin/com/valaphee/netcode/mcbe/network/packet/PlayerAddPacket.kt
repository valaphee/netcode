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
import com.valaphee.netcode.mcbe.command.CommandPermission
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.Restrict
import com.valaphee.netcode.mcbe.network.Restriction
import com.valaphee.netcode.mcbe.network.V1_18_030
import com.valaphee.netcode.mcbe.network.V1_19_010
import com.valaphee.netcode.mcbe.world.GameMode
import com.valaphee.netcode.mcbe.world.WorldFlag
import com.valaphee.netcode.mcbe.world.entity.Link
import com.valaphee.netcode.mcbe.world.entity.metadata.Metadata
import com.valaphee.netcode.mcbe.world.entity.player.AbilityLayer
import com.valaphee.netcode.mcbe.world.entity.player.PlayerFlag
import com.valaphee.netcode.mcbe.world.entity.player.PlayerPermission
import com.valaphee.netcode.mcbe.world.entity.player.User
import com.valaphee.netcode.mcbe.world.entity.player.readAbilityLayer
import com.valaphee.netcode.mcbe.world.entity.player.writeAbilityLayer
import com.valaphee.netcode.mcbe.world.entity.readLink
import com.valaphee.netcode.mcbe.world.entity.writeLink
import com.valaphee.netcode.mcbe.world.item.ItemStack
import com.valaphee.netcode.mcbe.world.item.readItemStack
import com.valaphee.netcode.mcbe.world.item.writeItemStack
import com.valaphee.netcode.util.LazyList
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
    val gameMode: GameMode?,
    val metadata: Metadata,
    val playerFlags: Set<PlayerFlag>?,
    val commandPermission: CommandPermission,
    val worldFlags: Set<WorldFlag>?,
    val playerPermission: PlayerPermission,
    val customFlags: Int,
    val abilityLayers: List<AbilityLayer>?,
    val links: List<Link>,
    val deviceId: String,
    val operatingSystem: User.OperatingSystem,
) : Packet() {
    override val id get() = 0x0C

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeUuid(userId)
        buffer.writeString(userName)
        if (version < V1_19_010) buffer.writeVarLong(uniqueEntityId)
        buffer.writeVarULong(runtimeEntityId)
        buffer.writeString(platformChatId)
        buffer.writeFloat3(position)
        buffer.writeFloat3(velocity)
        buffer.writeFloat2(rotation)
        buffer.writeFloatLE(headRotationYaw)
        buffer.writeItemStack(itemStackInHand, version)
        if (version >= V1_18_030) buffer.writeVarInt(gameMode!!.ordinal)
        metadata.writeToBuffer(buffer)
        if (version >= V1_19_010) {
            buffer.writeLongLE(uniqueEntityId)
            buffer.writeVarUInt(playerPermission.ordinal)
            buffer.writeVarUInt(commandPermission.ordinal)
            abilityLayers!!.let {
                buffer.writeVarUInt(it.size)
                it.forEach { buffer.writeAbilityLayer(it) }
            }
        } else {
            buffer.writeVarUIntFlags(playerFlags!!)
            buffer.writeVarUInt(commandPermission.ordinal)
            buffer.writeVarUIntFlags(worldFlags!!)
            buffer.writeVarUInt(playerPermission.ordinal)
            buffer.writeVarUInt(customFlags)
            buffer.writeLongLE(uniqueEntityId)
        }
        buffer.writeVarUInt(links.size)
        links.forEach { buffer.writeLink(it, version) }
        buffer.writeString(deviceId)
        buffer.writeIntLE(operatingSystem.ordinal - 1)
    }

    override fun handle(handler: PacketHandler) = handler.playerAdd(this)

    override fun toString() = "PlayerAddPacket(userId=$userId, userName='$userName', uniqueEntityId=$uniqueEntityId, runtimeEntityId=$runtimeEntityId, platformChatId='$platformChatId', position=$position, velocity=$velocity, rotation=$rotation, headRotationYaw=$headRotationYaw, stackInHand=$itemStackInHand, metadata=$metadata, playerFlags=$playerFlags, commandPermission=$commandPermission, worldFlags=$worldFlags, playerPermission$playerPermission, customFlags=$customFlags, abilityLayers=$abilityLayers, links=$links, deviceId='$deviceId', operatingSystem=$operatingSystem)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int): PlayerAddPacket {
            val userId = buffer.readUuid()
            val userName = buffer.readString()
            val uniqueEntityId = if (version >= V1_19_010) 0L else buffer.readVarLong()
            val runtimeEntityId = buffer.readVarULong()
            val platformChatId = buffer.readString()
            val position = buffer.readFloat3()
            val velocity = buffer.readFloat3()
            val rotation = buffer.readFloat2()
            val headRotationYaw = buffer.readFloatLE()
            val itemStackInHand = buffer.readItemStack(version)
            val gameMode = if (version >= V1_18_030) GameMode.values()[buffer.readVarInt()] else null
            val metadata = Metadata().apply { readFromBuffer(buffer) }
            val playerFlags: Set<PlayerFlag>?
            val commandPermission: CommandPermission
            val worldFlags: Set<WorldFlag>?
            val playerPermission: PlayerPermission
            val customFlags: Int
            val abilityLayers: List<AbilityLayer>?
            if (version >= V1_19_010) {
                playerFlags = buffer.readVarUIntFlags()
                commandPermission = CommandPermission.values()[buffer.readVarUInt()]
                worldFlags = buffer.readVarUIntFlags()
                playerPermission = PlayerPermission.values()[buffer.readVarUInt()]
                customFlags = buffer.readVarUInt()
                buffer.readLongLE()
                abilityLayers = null
            } else {
                buffer.readLongLE()
                playerFlags = null
                worldFlags = null
                playerPermission = PlayerPermission.values()[buffer.readVarUInt()]
                commandPermission = CommandPermission.values()[buffer.readVarUInt()]
                customFlags = 0
                abilityLayers = LazyList(buffer.readVarUInt()) { buffer.readAbilityLayer() }
            }
            val links = LazyList(buffer.readVarUInt()) { buffer.readLink(version) }
            val deviceId = buffer.readString()
            val operatingSystem = User.OperatingSystem.values()[buffer.readIntLE() + 1]
            return PlayerAddPacket(userId, userName, uniqueEntityId, runtimeEntityId, platformChatId, position, velocity, rotation, headRotationYaw, itemStackInHand, gameMode, metadata, playerFlags, commandPermission, worldFlags, playerPermission, customFlags, abilityLayers, links, deviceId, operatingSystem)
        }
    }
}
