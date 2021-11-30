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

import com.fasterxml.jackson.module.kotlin.readValue
import com.valaphee.netcode.mc.util.text.StyleCode
import com.valaphee.netcode.mcje.Packet
import com.valaphee.netcode.mcje.PacketBuffer
import com.valaphee.netcode.mcje.PacketReader
import com.valaphee.netcode.mcje.ServerPlayPacketHandler
import com.valaphee.netcode.mcje.util.text.Component
import com.valaphee.netcode.util.ByteBufStringReader
import com.valaphee.netcode.util.safeList

/**
 * @author Kevin Ludwig
 */
class ServerTeamPacket(
    val name: String,
    val action: Action,
    val displayName: Component?,
    val friendlyFlags: Byte,
    val nameTagVisibility: Rule?,
    val collisionRule: Rule?,
    val styleCode: StyleCode?,
    val prefix: Component?,
    val suffix: Component?,
    val userNames: List<String>?
) : Packet<ServerPlayPacketHandler> {
    enum class Action {
        Create, Remove, Update, AddUserNames, RemoveUserNames
    }

    enum class Rule {
        Always, OtherTeams, OwnTeam, Never
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeString(name)
        buffer.writeByte(action.ordinal)
        @Suppress("NON_EXHAUSTIVE_WHEN") when (action) {
            Action.Create -> {
                buffer.writeString(buffer.objectMapper.writeValueAsString(displayName))
                buffer.writeByte(friendlyFlags.toInt())
                when (nameTagVisibility) {
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
                buffer.writeVarInt(styleCode!!.ordinal())
                buffer.writeString(buffer.objectMapper.writeValueAsString(prefix!!))
                buffer.writeString(buffer.objectMapper.writeValueAsString(suffix!!))
                userNames?.let {
                    buffer.writeVarInt(it.size)
                    it.forEach { buffer.writeString(it) }
                }
            }
            Action.AddUserNames, Action.RemoveUserNames -> userNames?.let {
                buffer.writeVarInt(it.size)
                it.forEach { buffer.writeString(it) }
            }
            Action.Update -> {
                buffer.writeString(buffer.objectMapper.writeValueAsString(displayName!!))
                buffer.writeByte(friendlyFlags.toInt())
                when (nameTagVisibility) {
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
                buffer.writeVarInt(styleCode!!.ordinal())
                buffer.writeString(buffer.objectMapper.writeValueAsString(prefix!!))
                buffer.writeString(buffer.objectMapper.writeValueAsString(suffix!!))
            }
        }
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.team(this)

    override fun toString() = "ServerTeamPacket(name='$name', action=$action, displayName=$displayName, friendlyFlags=$friendlyFlags, nameTagVisibility=$nameTagVisibility, collisionRule=$collisionRule, styleCode=$styleCode, prefix=$prefix, suffix=$suffix, userNames=$userNames)"
}

/**
 * @author Kevin Ludwig
 */
object ServerTeamPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): ServerTeamPacket {
        val name = buffer.readString(16)
        val action = ServerTeamPacket.Action.values()[buffer.readUnsignedByte().toInt()]
        val displayName: Component?
        val friendlyFlags: Byte
        val nameTagVisibility: ServerTeamPacket.Rule?
        val collisionRule: ServerTeamPacket.Rule?
        val styleCode: StyleCode?
        val prefix: Component?
        val suffix: Component?
        val userNames: List<String>?
        @Suppress("NON_EXHAUSTIVE_WHEN") when (action) {
            ServerTeamPacket.Action.Create -> {
                displayName = buffer.objectMapper.readValue(ByteBufStringReader(buffer, buffer.readVarInt()))
                friendlyFlags = buffer.readByte()
                nameTagVisibility = when (buffer.readString(32)) {
                    "never" -> ServerTeamPacket.Rule.Never
                    "hideForOtherTeams" -> ServerTeamPacket.Rule.OtherTeams
                    "hideForOwnTeam" -> ServerTeamPacket.Rule.OwnTeam
                    else -> ServerTeamPacket.Rule.Always
                }
                collisionRule = when (buffer.readString(32)) {
                    "never" -> ServerTeamPacket.Rule.Never
                    "pushOtherTeams" -> ServerTeamPacket.Rule.OtherTeams
                    "pushOwnTeam" -> ServerTeamPacket.Rule.OwnTeam
                    else -> ServerTeamPacket.Rule.Always
                }
                styleCode = StyleCode.values[buffer.readVarInt()]
                prefix = buffer.objectMapper.readValue(ByteBufStringReader(buffer, buffer.readVarInt()))
                suffix = buffer.objectMapper.readValue(ByteBufStringReader(buffer, buffer.readVarInt()))
                userNames = safeList(buffer.readVarInt()) { buffer.readString(40) }
            }
            ServerTeamPacket.Action.AddUserNames, ServerTeamPacket.Action.RemoveUserNames -> {
                displayName = null
                friendlyFlags = 0
                nameTagVisibility = null
                collisionRule = null
                styleCode = null
                prefix = null
                suffix = null
                userNames = safeList(buffer.readVarInt()) { buffer.readString(40) }
            }
            ServerTeamPacket.Action.Update -> {
                displayName = buffer.objectMapper.readValue(ByteBufStringReader(buffer, buffer.readVarInt()))
                friendlyFlags = buffer.readByte()
                nameTagVisibility = when (buffer.readString(32)) {
                    "never" -> ServerTeamPacket.Rule.Never
                    "hideForOtherTeams" -> ServerTeamPacket.Rule.OtherTeams
                    "hideForOwnTeam" -> ServerTeamPacket.Rule.OwnTeam
                    else -> ServerTeamPacket.Rule.Always
                }
                collisionRule = when (buffer.readString(32)) {
                    "never" -> ServerTeamPacket.Rule.Never
                    "pushOtherTeams" -> ServerTeamPacket.Rule.OtherTeams
                    "pushOwnTeam" -> ServerTeamPacket.Rule.OwnTeam
                    else -> ServerTeamPacket.Rule.Always
                }
                styleCode = StyleCode.values[buffer.readVarInt()]
                prefix = buffer.objectMapper.readValue(ByteBufStringReader(buffer, buffer.readVarInt()))
                suffix = buffer.objectMapper.readValue(ByteBufStringReader(buffer, buffer.readVarInt()))
                userNames = null
            }
            else -> {
                displayName = null
                friendlyFlags = 0
                nameTagVisibility = null
                collisionRule = null
                styleCode = null
                prefix = null
                suffix = null
                userNames = null
            }
        }
        return ServerTeamPacket(name, action, displayName, friendlyFlags, nameTagVisibility, collisionRule, styleCode, prefix, suffix, userNames)
    }
}
