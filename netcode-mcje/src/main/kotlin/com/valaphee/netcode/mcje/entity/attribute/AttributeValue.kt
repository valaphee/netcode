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
    private val _modifiers = HashMap<UUID, AttributeValueModifier>()
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

    val modifiers get() = _modifiers.values

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
