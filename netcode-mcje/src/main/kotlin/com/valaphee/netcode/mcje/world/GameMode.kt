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

package com.valaphee.netcode.mcje.world

/**
 * @author Kevin Ludwig
 */
enum class GameMode(
    val id: Int
) {
    NotSet(-1),
    Survival(0),
    Creative(1),
    Adventure(2),
    Spectator(3);

    companion object {
        private val byId = values().associateBy { it.id }

        fun byIdOrNull(id: Int) = byId[id]
    }
}
