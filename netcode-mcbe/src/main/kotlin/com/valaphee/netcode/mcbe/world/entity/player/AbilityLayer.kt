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

package com.valaphee.netcode.mcbe.world.entity.player

import com.valaphee.netcode.mcbe.network.PacketBuffer

/**
 * @author Kevin Ludwig
 */
class AbilityLayer(
    val type: Type,
    val abilitiesSet: Set<Ability>,
    val abilityValues: Set<Ability>,
    val flySpeed: Float,
    val walkSpeed: Float
) {
    enum class Type {
        Cache,
        Base,
        Spectator,
        Commands
    }

    enum class Ability {
        Build,
        Mine,
        DoorsAndSwitches,
        AttackPlayers,
        AttackMobs,
        OperatorCommands,
        Teleport,
        Invulnerable,
        Flying,
        AllowFlight,
        Instabuild,
        Lightning,
        FlySpeed,
        WalkSpeed,
        Muted,
        WorldBuilder,
        NoClip
    }
}

fun PacketBuffer.writeAbilityLayer(value: AbilityLayer) {
    writeShortLE(value.type.ordinal)
    writeIntLEFlags(value.abilitiesSet)
    writeIntLEFlags(value.abilityValues)
    writeFloatLE(value.flySpeed)
    writeFloatLE(value.walkSpeed)
}

fun PacketBuffer.readAbilityLayer() = AbilityLayer(AbilityLayer.Type.values()[readShortLE().toInt()], readIntLEFlags(), readIntLEFlags(), readFloatLE(), readFloatLE())
