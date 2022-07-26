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

package com.valaphee.netcode.mcje.world.entity.attribute

import com.valaphee.netcode.mcje.util.NamespacedKey
import com.valaphee.netcode.mcje.util.minecraftKey

/**
 * @author Kevin Ludwig
 */
enum class AttributeField(
    var key: NamespacedKey,
    var legacyKey: String,
    var minimum: Double,
    var maximum: Double,
    var defaultValue: Double
) {
    MaximumHealth(minecraftKey("generic.max_health"), "generic.maxHealth", 0.0, 1024.0, 20.0),
    FollowRange(minecraftKey("generic.follow_range"), "generic.followRange", 0.0, 2048.0, 32.0),
    KnockbackResistance(minecraftKey("generic.knockback_resistance"), "generic.knockbackResistance", 0.0, 1.0, 0.0),
    WalkSpeed(minecraftKey("generic.movement_speed"), "generic.movementSpeed", 0.0, 1024.0, .7),
    AttackDamage(minecraftKey("generic.attack_damage"), "generic.attackDamage", 0.0, 2048.0, 2.0),
    AttackSpeed(minecraftKey("generic.attack_speed"), "generic.attackSpeed", 0.0, 1024.0, 4.0),
    FlySpeed(minecraftKey("generic.flying_speed"), "generic.flyingSpeed", 0.0, 1024.0, .4),
    Armor(minecraftKey("generic.armor"), "generic.armor", 0.0, 30.0, 0.0),
    ArmorToughness(minecraftKey("generic.armor_toughness"), "generic.armorToughness", 0.0, 20.0, 0.0),
    AttackKnockback(minecraftKey("generic.attack_knockback"), "generic.attackKnockback", 0.0, 5.0, 0.0),
    Luck(minecraftKey("generic.luck"), "generic.luck", -1024.0, 1024.0, 0.0),
    HorseJumpStrength(minecraftKey("horse.jump_strength"), "horse.jumpStrength", 0.0, 2.0, .7),
    ZombieSpawnReinforcementsChance(minecraftKey("zombie.spawn_reinforcements"), "zombie.spawnReinforcements", 0.0, 1.0, 0.0);

    fun attributeValue() = AttributeValue(this)

    fun inBounds(value: Double) = value.coerceIn(minimum, maximum)

    override fun toString() = key.toString()

    companion object {
        private val byKey = values().associateBy { it.key }
        private val byLegacyKey = values().associateBy { it.legacyKey }

        fun byKey(key: NamespacedKey) = byKey[key] ?: error("No such attribute field: $key")

        fun byLegacyKey(key: String) = byLegacyKey[key] ?: error("No such attribute field: $key (legacy)")
    }
}
