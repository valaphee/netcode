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

import java.util.UUID

/**
 * @author Kevin Ludwig
 */
class AttributeValue(
    private val field: AttributeField,
    value: Double = field.defaultValue
) {
    var value = value
        set(value) {
            field = value
            modified = true
            computeModifiedValue = true
        }
    var modified = true
        private set
    private val _modifiers = mutableMapOf<UUID, AttributeValueModifier>()
    val modifiers: Collection<AttributeValueModifier> get() = _modifiers.values
    private var computeModifiedValue = false
    var modifiedValue = value
        get() {
            if (computeModifiedValue) {
                val absoluteModifiers: MutableCollection<AttributeValueModifier> = HashSet()
                val percentModifiers: MutableCollection<AttributeValueModifier> = HashSet()
                val percentBaseModifiers: MutableCollection<AttributeValueModifier> = HashSet()
                modifiers.forEach {
                    when (it.operation) {
                        AttributeValueModifier.Operation.Absolute -> absoluteModifiers.add(it)
                        AttributeValueModifier.Operation.Percent -> percentModifiers.add(it)
                        AttributeValueModifier.Operation.PercentBase -> percentBaseModifiers.add(it)
                    }
                }
                var absoluteModification = value
                absoluteModifiers.forEach { absoluteModification += it.value }
                var percentModification = absoluteModification
                percentModifiers.forEach { percentModification += absoluteModification * it.value }
                percentBaseModifiers.forEach { percentModification *= 1.0 + it.value }
                field = this.field.inBounds(percentModification)
                computeModifiedValue = false
            }
            return field
        }

    fun getModifier(id: UUID) = _modifiers[id]

    fun applyModifier(modifier: AttributeValueModifier) {
        check(!_modifiers.containsKey(modifier.id)) { "Modifier is already applied to this attribute" }
        _modifiers[modifier.id] = modifier
        modified = true
        computeModifiedValue = true
    }

    fun revokeModifier(modifier: AttributeValueModifier) {
        _modifiers -= modifier.id
        modified = true
        computeModifiedValue = true
    }

    fun revokeAllModifiers() {
        _modifiers.clear()
        modified = true
        computeModifiedValue = true
    }

    fun flagAsSaved(): Boolean {
        val previousModified = modified
        this.modified = false
        return previousModified
    }
}
