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

/**
 * @author Kevin Ludwig
 */
enum class Direction(
    val horizontalIndex: Int = -1
) {
    Down,
    Up,
    North(2),
    South(0),
    West(1),
    East(3);

    companion object {
        val horizontals = values().filter { it.horizontalIndex != -1 }.sortedBy { it.horizontalIndex }
    }
}
