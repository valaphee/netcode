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

package com.valaphee.netcode.mcbe.world.scoreboard

/**
 * @author Kevin Ludwig
 */
data class Score private constructor(
    val scoreboardId: Long,
    val objectiveName: String,
    val value: Int,
    val type: ScorerType,
    val name: String?,
    val entityId: Long
) {
    enum class ScorerType {
        None, Player, Entity, Fake
    }

    constructor(scoreboardId: Long, objectiveName: String) : this(scoreboardId, objectiveName, 0, ScorerType.None, null, 0)

    constructor(scoreboardId: Long, objectiveName: String, value: Int) : this(scoreboardId, objectiveName, value, ScorerType.None, null, 0)

    constructor(scoreboardId: Long, objectiveName: String, value: Int, name: String) : this(scoreboardId, objectiveName, value, ScorerType.Fake, name, 0)

    constructor(scoreboardId: Long, objectiveName: String, value: Int, type: ScorerType, entityId: Long) : this(scoreboardId, objectiveName, value, type, null, entityId)
}
