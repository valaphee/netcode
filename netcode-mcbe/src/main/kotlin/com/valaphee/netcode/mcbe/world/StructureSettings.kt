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

package com.valaphee.netcode.mcbe.world

import com.valaphee.foundry.math.Float3
import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.V1_17_002
import com.valaphee.netcode.mcbe.network.V1_18_030

/**
 * @author Kevin Ludwig
 */
data class StructureSettings(
    val paletteName: String,
    val ignoringEntities: Boolean,
    val ignoringBlocks: Boolean,
    val disableTicking: Boolean,
    val size: Int3,
    val offset: Int3,
    val lastEditedByEntityId: Long,
    val rotation: Rotation,
    val mirror: Mirror,
    val animationMode: AnimationMode,
    val animationTime: Float,
    val integrityValue: Float,
    val integritySeed: Int,
    val pivot: Float3
) {
    enum class Rotation {
        None, Clockwise90, Reverse, Counterclockwise90
    }

    enum class Mirror {
        None, X, Z, XZ
    }

    enum class AnimationMode {
        None, Layer, Blocks
    }
}

fun PacketBuffer.readStructureSettings(version: Int) = StructureSettings(readString(), readBoolean(), readBoolean(), if (version >= V1_18_030) readBoolean() else false, readBlockPosition(), readBlockPosition(), readVarLong(), StructureSettings.Rotation.values()[readUnsignedByte().toInt()], StructureSettings.Mirror.values()[readUnsignedByte().toInt()], if (version >= V1_17_002) StructureSettings.AnimationMode.values()[readUnsignedByte().toInt()] else StructureSettings.AnimationMode.None, if (version >= V1_17_002) readFloatLE() else 0.0f, readFloatLE(), readIntLE(), readFloat3())

fun PacketBuffer.writeStructureSettings(value: StructureSettings, version: Int) {
    writeString(value.paletteName)
    writeBoolean(value.ignoringEntities)
    writeBoolean(value.ignoringBlocks)
    if (version >= V1_18_030) writeBoolean(value.disableTicking)
    writeBlockPosition(value.size)
    writeBlockPosition(value.offset)
    writeVarLong(value.lastEditedByEntityId)
    writeByte(value.rotation.ordinal)
    writeByte(value.mirror.ordinal)
    if (version >= V1_17_002) {
        writeByte(value.animationMode.ordinal)
        writeFloatLE(value.animationTime)
    }
    writeFloatLE(value.integrityValue)
    writeIntLE(value.integritySeed)
    writeFloat3(value.pivot)
}
