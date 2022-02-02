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
class AttributeValue internal constructor(
    val field: AttributeField,
    minimum: Float,
    maximum: Float,
    defaultValue: Float,
    value: Float = defaultValue
) {
    var minimum = minimum
        set(value) {
            field = value
            modified = true
        }
    var maximum = maximum
        set(value) {
            field = value
            modified = true
        }
    var defaultValue = defaultValue
        set(value) {
            field = value
            modified = true
        }
    var value = value
        set(value) {
            field = value
            modified = true
        }
    var modified = true
        private set

    fun flagAsSaved(): Boolean {
        val previousModified = modified
        this.modified = false
        return previousModified
    }
}