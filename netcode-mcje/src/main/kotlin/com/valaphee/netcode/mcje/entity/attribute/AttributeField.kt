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

package com.valaphee.netcode.mcje.entity.attribute

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

        fun byKey(key: NamespacedKey) = byKey[key] ?: error(key.toString())

        fun byLegacyKey(key: String) = byLegacyKey[key] ?: error(key)
    }
}
