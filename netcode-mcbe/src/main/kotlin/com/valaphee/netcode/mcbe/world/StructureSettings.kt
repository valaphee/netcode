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

fun PacketBuffer.readStructureSettingsPre440() = StructureSettings(readString(), readBoolean(), readBoolean(), true, readInt3UnsignedY(), readInt3UnsignedY(), readVarLong(), StructureSettings.Rotation.values()[readUnsignedByte().toInt()], StructureSettings.Mirror.values()[readUnsignedByte().toInt()], StructureSettings.AnimationMode.None, 0.0f, readFloatLE(), readIntLE(), readFloat3())

fun PacketBuffer.readStructureSettingsPre503() = StructureSettings(readString(), readBoolean(), readBoolean(), true, readInt3UnsignedY(), readInt3UnsignedY(), readVarLong(), StructureSettings.Rotation.values()[readUnsignedByte().toInt()], StructureSettings.Mirror.values()[readUnsignedByte().toInt()], StructureSettings.AnimationMode.values()[readUnsignedByte().toInt()], readFloatLE(), readFloatLE(), readIntLE(), readFloat3())

fun PacketBuffer.readStructureSettings() = StructureSettings(readString(), readBoolean(), readBoolean(), readBoolean(), readInt3UnsignedY(), readInt3UnsignedY(), readVarLong(), StructureSettings.Rotation.values()[readUnsignedByte().toInt()], StructureSettings.Mirror.values()[readUnsignedByte().toInt()], StructureSettings.AnimationMode.values()[readUnsignedByte().toInt()], readFloatLE(), readFloatLE(), readIntLE(), readFloat3())

fun PacketBuffer.writeStructureSettingsPre440(value: StructureSettings) {
    writeString(value.paletteName)
    writeBoolean(value.ignoringEntities)
    writeBoolean(value.ignoringBlocks)
    writeInt3UnsignedY(value.size)
    writeInt3UnsignedY(value.offset)
    writeVarLong(value.lastEditedByEntityId)
    writeByte(value.rotation.ordinal)
    writeByte(value.mirror.ordinal)
    writeFloatLE(value.integrityValue)
    writeIntLE(value.integritySeed)
    writeFloat3(value.pivot)
}

fun PacketBuffer.writeStructureSettingsPre503(value: StructureSettings) {
    writeString(value.paletteName)
    writeBoolean(value.ignoringEntities)
    writeBoolean(value.ignoringBlocks)
    writeInt3UnsignedY(value.size)
    writeInt3UnsignedY(value.offset)
    writeVarLong(value.lastEditedByEntityId)
    writeByte(value.rotation.ordinal)
    writeByte(value.mirror.ordinal)
    writeByte(value.animationMode.ordinal)
    writeFloatLE(value.animationTime)
    writeFloatLE(value.integrityValue)
    writeIntLE(value.integritySeed)
    writeFloat3(value.pivot)
}

fun PacketBuffer.writeStructureSettings(value: StructureSettings) {
    writeString(value.paletteName)
    writeBoolean(value.ignoringEntities)
    writeBoolean(value.ignoringBlocks)
    writeBoolean(value.disableTicking)
    writeInt3UnsignedY(value.size)
    writeInt3UnsignedY(value.offset)
    writeVarLong(value.lastEditedByEntityId)
    writeByte(value.rotation.ordinal)
    writeByte(value.mirror.ordinal)
    writeByte(value.animationMode.ordinal)
    writeFloatLE(value.animationTime)
    writeFloatLE(value.integrityValue)
    writeIntLE(value.integritySeed)
    writeFloat3(value.pivot)
}
