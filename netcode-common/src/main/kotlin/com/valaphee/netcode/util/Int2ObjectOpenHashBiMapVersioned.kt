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

/**
 * @author Kevin Ludwig
 */
class Int2ObjectOpenHashBiMapVersioned<T> : Int2ObjectOpenHashMap<Int2ObjectOpenHashBiMap<T>>() {
    private val versions by lazy { keys.sortedDescending() }

    fun put(value: T, vararg keyByVersion: Pair<Int, Int>) {
        keyByVersion.forEach { getOrPut(it.first) { Int2ObjectOpenHashBiMap() }[it.second] = value }
    }

    fun getLast(version: Int, key: Int): T? {
        versions.forEach {
            if (version >= it) {
                val map = get(it)
                if (map.containsKey(key)) return map.get(key)
            }
        }
        return null
    }

    fun getLastInt(version: Int, value: T): Int {
        versions.forEach {
            if (version >= it) {
                val map = get(it)
                if (map.containsValue(value)) return map.getInt(value)
            }
        }
        return 0
    }
}
