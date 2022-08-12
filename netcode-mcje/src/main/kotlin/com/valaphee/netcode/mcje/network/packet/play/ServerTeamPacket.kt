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

import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.Packet.Reader
import com.valaphee.netcode.mcje.network.ServerPlayPacketHandler
import com.valaphee.netcode.util.LazyList
import net.kyori.adventure.text.Component

/**
 * @author Kevin Ludwig
 */
class ServerTeamPacket(
    val name: String,
    val action: Action,
    val displayName: Component?,
    val friendlyFlags: Byte,
    val nametagVisibility: Rule?,
    val collisionRule: Rule?,
    val styleCode: Int,
    val prefix: Component?,
    val suffix: Component?,
    val userNames: List<String>?
) : Packet<ServerPlayPacketHandler>() {
    enum class Action {
        Create, Remove, Update, AddUserNames, RemoveUserNames
    }

    enum class Rule {
        Always, OtherTeams, OwnTeam, Never
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeString(name)
        buffer.writeByte(action.ordinal)
        when (action) {
            Action.Create -> {
                buffer.writeComponent(displayName!!)
                buffer.writeByte(friendlyFlags.toInt())
                when (nametagVisibility) {
                    Rule.Never -> buffer.writeString("never")
                    Rule.OtherTeams -> buffer.writeString("hideForOtherTeams")
                    Rule.OwnTeam -> buffer.writeString("hideForOwnTeam")
                    else -> buffer.writeString("always")
                }
                when (collisionRule) {
                    Rule.Never -> buffer.writeString("never")
                    Rule.OtherTeams -> buffer.writeString("pushOtherTeams")
                    Rule.OwnTeam -> buffer.writeString("pushOwnTeam")
                    else -> buffer.writeString("always")
                }
                buffer.writeVarInt(styleCode)
                buffer.writeComponent(prefix!!)
                buffer.writeComponent(suffix!!)
                userNames?.let {
                    buffer.writeVarInt(it.size)
                    it.forEach { buffer.writeString(it) }
                }
            }
            Action.Remove -> Unit
            Action.Update -> {
                buffer.writeComponent(displayName!!)
                buffer.writeByte(friendlyFlags.toInt())
                when (nametagVisibility) {
                    Rule.Never -> buffer.writeString("never")
                    Rule.OtherTeams -> buffer.writeString("hideForOtherTeams")
                    Rule.OwnTeam -> buffer.writeString("hideForOwnTeam")
                    else -> buffer.writeString("always")
                }
                when (collisionRule) {
                    Rule.Never -> buffer.writeString("never")
                    Rule.OtherTeams -> buffer.writeString("pushOtherTeams")
                    Rule.OwnTeam -> buffer.writeString("pushOwnTeam")
                    else -> buffer.writeString("always")
                }
                buffer.writeVarInt(styleCode)
                buffer.writeComponent(prefix!!)
                buffer.writeComponent(suffix!!)
            }
            Action.AddUserNames, Action.RemoveUserNames -> userNames?.let {
                buffer.writeVarInt(it.size)
                it.forEach { buffer.writeString(it) }
            }
        }
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.team(this)

    override fun toString() = "ServerTeamPacket(name='$name', action=$action, displayName=$displayName, friendlyFlags=$friendlyFlags, nametagVisibility=$nametagVisibility, collisionRule=$collisionRule, styleCode=$styleCode, prefix=$prefix, suffix=$suffix, userNames=$userNames)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int): ServerTeamPacket {
            val name = buffer.readString(16)
            val action = Action.values()[buffer.readUnsignedByte().toInt()]
            val displayName: Component?
            val friendlyFlags: Byte
            val nametagVisibility: Rule?
            val collisionRule: Rule?
            val styleCode: Int
            val prefix: Component?
            val suffix: Component?
            val userNames: List<String>?
            when (action) {
                Action.Create -> {
                    displayName = buffer.readComponent()
                    friendlyFlags = buffer.readByte()
                    nametagVisibility = when (buffer.readString(32)) {
                        "never" -> Rule.Never
                        "hideForOtherTeams" -> Rule.OtherTeams
                        "hideForOwnTeam" -> Rule.OwnTeam
                        else -> Rule.Always
                    }
                    collisionRule = when (buffer.readString(32)) {
                        "never" -> Rule.Never
                        "pushOtherTeams" -> Rule.OtherTeams
                        "pushOwnTeam" -> Rule.OwnTeam
                        else -> Rule.Always
                    }
                    styleCode = buffer.readVarInt()
                    prefix = buffer.readComponent()
                    suffix = buffer.readComponent()
                    userNames = LazyList(buffer.readVarInt()) { buffer.readString(40) }
                }
                Action.Remove -> {
                    displayName = null
                    friendlyFlags = 0
                    nametagVisibility = null
                    collisionRule = null
                    styleCode = 0
                    prefix = null
                    suffix = null
                    userNames = null
                }
                Action.Update -> {
                    displayName = buffer.readComponent()
                    friendlyFlags = buffer.readByte()
                    nametagVisibility = when (buffer.readString(32)) {
                        "never" -> Rule.Never
                        "hideForOtherTeams" -> Rule.OtherTeams
                        "hideForOwnTeam" -> Rule.OwnTeam
                        else -> Rule.Always
                    }
                    collisionRule = when (buffer.readString(32)) {
                        "never" -> Rule.Never
                        "pushOtherTeams" -> Rule.OtherTeams
                        "pushOwnTeam" -> Rule.OwnTeam
                        else -> Rule.Always
                    }
                    styleCode = buffer.readVarInt()
                    prefix = buffer.readComponent()
                    suffix = buffer.readComponent()
                    userNames = null
                }
                Action.AddUserNames, Action.RemoveUserNames -> {
                    displayName = null
                    friendlyFlags = 0
                    nametagVisibility = null
                    collisionRule = null
                    styleCode = 0
                    prefix = null
                    suffix = null
                    userNames = LazyList(buffer.readVarInt()) { buffer.readString(40) }
                }
            }
            return ServerTeamPacket(name, action, displayName, friendlyFlags, nametagVisibility, collisionRule, styleCode, prefix, suffix, userNames)
        }
    }
}
