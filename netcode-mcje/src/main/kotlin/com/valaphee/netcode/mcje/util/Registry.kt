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

package com.valaphee.netcode.mcje.util

import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.Object2IntMap
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap

/**
 * @author Kevin Ludwig
 */

open class Registry<T> : Cloneable {
    var idToValue: Int2ObjectMap<T> = Int2ObjectOpenHashMap()
        private set
    var valueToId: Object2IntMap<T> = Object2IntOpenHashMap()
        private set

    var default: T
        get() = idToValue.defaultReturnValue()
        set(value) {
            idToValue.defaultReturnValue(value)
        }

    operator fun set(id: Int, value: T) {
        idToValue[id] = value
        valueToId[value] = id
    }

    operator fun get(id: Int): T? = idToValue.get(id)

    fun getId(value: T) = valueToId.getInt(value)

    @Suppress("UNCHECKED_CAST")
    public override fun clone() = (super.clone() as Registry<T>).apply {
        idToValue = this@Registry.idToValue
        valueToId = this@Registry.valueToId
    }
}
