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

package com.valaphee.netcode.mcbeautomation.event

import com.fasterxml.jackson.annotation.JsonProperty
import com.valaphee.netcode.mcbeautomation.Event

/**
 * @author Kevin Ludwig
 */
data class MobKilledEvent(
    @get:JsonProperty("player") val player: Entity,
    @get:JsonProperty("victim") val victim: Entity,
    @get:JsonProperty("killMethodType") val method: Int,
    @get:JsonProperty("isMonster") val monster: Boolean,
    @get:JsonProperty("armorHead") val armorHead: ItemStack,
    @get:JsonProperty("armorTorso") val armorTorso: ItemStack,
    @get:JsonProperty("armorLegs") val armorLegs: ItemStack,
    @get:JsonProperty("armorFeet") val armorFeet: ItemStack,
    @get:JsonProperty("weapon") val weapon: ItemStack,
    @get:JsonProperty("playerIsHiddenFrom") val ambush: Boolean
) : Event()
