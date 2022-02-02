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

package com.valaphee.netcode.mcbe.entity.attribute

/**
 * @author Kevin Ludwig
 */
enum class AttributeField(
    val key: String,
    val minimum: Float,
    val maximum: Float,
    val defaultValue: Float
) {
    Absorption("minecraft:absorption", 0.0f, Float.MAX_VALUE, 0.0f),
    Saturation("minecraft:player.saturation", 0.0f, 20.0f, 5.0f),
    Exhaustion("minecraft:player.exhaustion", 0.0f, 5.0f, 0.41f),
    KnockbackResistance("minecraft:knockback_resistance", 0.0f, 1.0f, 0.0f),
    Health("minecraft:health", 0.0f, 20.0f, 20.0f),
    MovementSpeed("minecraft:movement", 0.0f, Float.MAX_VALUE, 0.1f),
    FollowRange("minecraft:follow_range", 0.0f, 2048.0f, 16.0f),
    Hunger("minecraft:player.hunger", 0.0f, 20.0f, 20.0f),
    AttackDamage("minecraft:attack_damage", 0.0f, Float.MAX_VALUE, 1.0f),
    ExperienceLevel("minecraft:player.level", 0.0f, 24791.0f, 0.0f),
    Experience("minecraft:player.experience", 0.0f, 1.0f, 0.0f),
    Luck("minecraft:luck", -1024.0f, 1024.0f, 0.0f),
    HorseJumpStrength("minecraft:horse.jump_strength", 0.0f, 2.0f, 0.7f);

    fun attributeValue() = AttributeValue(this, minimum, maximum, defaultValue)

    fun inBounds(value: Float) = value.coerceIn(minimum, maximum)

    override fun toString() = key

    companion object {
        private val byKey = values().associateBy { it.key }

        fun byKey(key: String) = byKey[key] ?: error(key)
    }
}
