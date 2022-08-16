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

package com.valaphee.netcode.util

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap

/**
 * @author Kevin Ludwig
 */
class Int2ObjectOpenHashBiMap<T> : Int2ObjectOpenHashMap<T>() {
    private val reverse_ = Object2IntOpenHashMap<T>()

    override fun defaultReturnValue(returnValue: T) {
        super.defaultReturnValue(returnValue)
        reverse_.defaultReturnValue(reverse_.getInt(returnValue))
    }

    fun getInt(value: T) = reverse_.getInt(value)

    override fun containsValue(value: T) = reverse_.containsKey(value)

    override fun put(key: Int, value: T): T = super.put(key, value).also { reverse_.put(value, key) }

    override fun remove(key: Int): T = super.remove(key).also { reverse_.removeInt(it) }
}
