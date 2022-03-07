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

import com.valaphee.netcode.mcbe.command.Command
import com.valaphee.netcode.mcbe.command.Enumeration
import com.valaphee.netcode.mcbe.command.EnumerationConstraint
import com.valaphee.netcode.mcbe.command.Parameter
import com.valaphee.netcode.mcbe.command.Permission
import com.valaphee.netcode.mcbe.command.readEnumeration
import com.valaphee.netcode.mcbe.command.readEnumerationConstraint
import com.valaphee.netcode.mcbe.command.writeEnumeration
import com.valaphee.netcode.mcbe.command.writeEnumerationConstraint
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.PacketReader
import com.valaphee.netcode.mcbe.network.Restrict
import com.valaphee.netcode.mcbe.network.Restriction
import com.valaphee.netcode.util.safeList

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class CommandsPacket(
    val commands: List<Command>,
    val constraints: List<EnumerationConstraint>
) : Packet() {
    override val id get() = 0x4C

    override fun write(buffer: PacketBuffer, version: Int) {
        val values = mutableListOf<String>()
        val enumerationsMap = mutableMapOf<String, Enumeration>()
        val softEnumerationsMap = mutableMapOf<String, Enumeration>()
        val postfixes = mutableListOf<String>()
        commands.forEach { command ->
            command.aliases?.let {
                values.addAll(it.values)
                enumerationsMap[it.name] = it
            }
            command.overloads.forEach { overload ->
                overload.forEach { parameter ->
                    parameter.enumeration?.let {
                        if (it.soft) softEnumerationsMap[it.name] = it else {
                            values.addAll(it.values)
                            enumerationsMap[it.name] = it
                        }
                    }
                    parameter.postfix?.let { postfixes.add(it) }
                }
            }
        }
        buffer.writeVarUInt(values.size)
        values.forEach { buffer.writeString(it) }
        buffer.writeVarUInt(postfixes.size)
        postfixes.forEach { buffer.writeString(it) }
        val indexWriter: (Int) -> Unit = when {
            values.size <= 0xFF -> buffer::writeByte
            values.size <= 0xFFFF -> buffer::writeShortLE
            else -> buffer::writeIntLE
        }
        buffer.writeVarUInt(enumerationsMap.values.size)
        enumerationsMap.values.forEach {
            buffer.writeString(it.name)
            buffer.writeVarUInt(it.values.size)
            it.values.forEach { indexWriter(values.indexOf(it)) }
        }
        buffer.writeVarUInt(commands.size)
        commands.forEach { command ->
            buffer.writeString(command.name)
            buffer.writeString(command.description)
            if (version >= 448) buffer.writeShortLEFlags(command.flags) else buffer.writeByteFlags(command.flags)
            buffer.writeByte(command.permission.ordinal)
            buffer.writeIntLE(enumerationsMap.values.indexOf(command.aliases))
            buffer.writeVarUInt(command.overloads.size)
            command.overloads.forEach { overload ->
                buffer.writeVarUInt(overload.size)
                overload.forEach { parameter ->
                    buffer.writeString(parameter.name)
                    buffer.writeIntLE(parameter.postfix?.let { postfixes.indexOf(it) or parameterFlagPostfix } ?: parameter.enumeration?.let { (if (it.soft) softEnumerationsMap.values.indexOf(parameter.enumeration) or parameterFlagSoftEnumeration else enumerationsMap.values.indexOf(parameter.enumeration) or parameterFlagEnumeration) or parameterFlagValid } ?: parameter.type?.let { (if (version >= 419) Parameter.Type.registry else Parameter.Type.registryPre419).getId(it) or parameterFlagValid } ?: error("Unknown type in ${parameter.name}"))
                    buffer.writeBoolean(parameter.optional)
                    buffer.writeByteFlags(parameter.options)
                }
            }
        }
        buffer.writeVarUInt(softEnumerationsMap.values.size)
        softEnumerationsMap.values.forEach { buffer.writeEnumeration(it) }
        buffer.writeVarUInt(constraints.size)
        constraints.forEach { buffer.writeEnumerationConstraint(it, values, enumerationsMap.values) }
    }

    override fun handle(handler: PacketHandler) = handler.commands(this)

    override fun toString() = "CommandsPacket(commands=$commands, constraints=$constraints)"

    companion object {
        internal const val parameterFlagValid = 1 shl 20
        internal const val parameterFlagEnumeration = 1 shl 21
        internal const val parameterFlagPostfix = 1 shl 24
        internal const val parameterFlagSoftEnumeration = 1 shl 26
    }
}

/**
 * @author Kevin Ludwig
 */
object CommandsPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int): CommandsPacket {
        val values = safeList(buffer.readVarUInt()) { buffer.readString() }
        val postfixes = safeList(buffer.readVarUInt()) { buffer.readString() }
        val indexReader: () -> Int = when {
            values.size <= 0xFF -> {
                { buffer.readUnsignedByte().toInt() }
            }
            values.size <= 0xFFFF -> {
                { buffer.readUnsignedShortLE() }
            }
            else -> {
                { buffer.readIntLE() }
            }
        }
        val enumerations = safeList(buffer.readVarUInt()) { Enumeration(buffer.readString(), safeList(buffer.readVarUInt()) { values[indexReader()] }.toMutableSet(), false) }
        val commandBuilders = safeList(buffer.readVarUInt()) {
            val name = buffer.readString()
            val description = buffer.readString()
            val flags: Set<Command.Flag> = if (version >= 448) buffer.readShortLEFlags() else buffer.readByteFlags()
            val permission = Permission.values()[buffer.readByte().toInt()]
            val aliasesIndex = buffer.readIntLE()
            val overloadBuilders = safeList(buffer.readVarUInt()) {
                safeList(buffer.readVarUInt()) {
                    val parameterName = buffer.readString()
                    val type = buffer.readIntLE()
                    val optional = buffer.readBoolean()
                    val options = buffer.readByteFlags<Parameter.Option>()
                    Parameter.Builder(
                        parameterName,
                        optional,
                        options,
                        type and CommandsPacket.parameterFlagEnumeration != 0,
                        type and CommandsPacket.parameterFlagSoftEnumeration != 0,
                        type and CommandsPacket.parameterFlagPostfix != 0,
                        type and 0xFFFF
                    )
                }
            }
            Command.Builder(name, description, flags, permission, aliasesIndex, overloadBuilders)
        }
        val softEnumerations = safeList(buffer.readVarUInt()) { buffer.readEnumeration(true) }
        val constraints = safeList(buffer.readVarUInt()) { buffer.readEnumerationConstraint(values, enumerations) }
        return CommandsPacket(commandBuilders.map {
            val aliasesIndex = it.aliasesIndex
            Command(it.name, it.description, it.flags, it.permission, if (aliasesIndex == -1) null else enumerations[aliasesIndex], it.overloadStructures.map {
                it.map {
                    var postfix: String? = null
                    var enumeration: Enumeration? = null
                    var type: Parameter.Type? = null
                    when {
                        it.postfix -> postfix = postfixes[it.index]
                        it.enumeration -> enumeration = enumerations[it.index]
                        it.softEnumeration -> enumeration = softEnumerations[it.index]
                        else -> type = if (version >= 419) Parameter.Type.registry[it.index] else Parameter.Type.registryPre419[it.index]
                    }
                    Parameter(it.name, it.optional, it.options, enumeration, postfix, type)
                }
            })
        }, constraints)
    }
}
