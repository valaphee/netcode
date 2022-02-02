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

package com.valaphee.netcode.mcbe.command

import com.valaphee.netcode.mcbe.PacketBuffer
import com.valaphee.netcode.util.safeList

/**
 * @author Kevin Ludwig
 */
data class EnumerationConstraint(
    val value: String,
    val enumeration: Enumeration,
    val constraints: List<Constraint>
) {
    enum class Constraint {
        CheatsEnabled, OperatorPermissions, HostPermissions, Unknown3
    }
}

fun PacketBuffer.readEnumerationConstraint(values: List<String>, enumerations: List<Enumeration>) = EnumerationConstraint(values[buffer.readIntLE()], enumerations[buffer.readIntLE()], safeList(readVarUInt()) { EnumerationConstraint.Constraint.values()[buffer.readByte().toInt()] })

fun PacketBuffer.writeEnumerationConstraint(value: EnumerationConstraint, values: Collection<String>, enumerations: Collection<Enumeration>) {
    writeIntLE(values.indexOf(value.value))
    writeIntLE(enumerations.indexOf(value.enumeration))
    writeVarUInt(value.constraints.size)
    value.constraints.forEach { buffer.writeByte(it.ordinal) }
}
