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

package com.valaphee.netcode.mcje.entity.metadata

import com.valaphee.netcode.mcje.PacketBuffer

/**
 * @author Kevin Ludwig
 */
data class VillagerData(
    var type: Type,
    var profession: Profession,
    var level: Int
) {
    enum class Type {
        Desert, Jungle, Plains, Savanna, Snowy, Swamp, Taiga
    }

    enum class Profession {
        Armorer,
        Butcher,
        Cartographer,
        Cleric,
        Farmer,
        Fisherman,
        Fletcher,
        Leatherworker,
        Librarian,
        Nitwit,
        None,
        Mason,
        Shepherd,
        Toolsmith,
        Weaponsmith
    }
}

fun PacketBuffer.readVillagerData() = VillagerData(VillagerData.Type.values()[readVarInt()], VillagerData.Profession.values()[readVarInt()], readVarInt())

fun PacketBuffer.writeVillagerData(value: VillagerData) {
    writeVarInt(value.type.ordinal)
    writeVarInt(value.profession.ordinal)
    writeVarInt(value.level)
}
