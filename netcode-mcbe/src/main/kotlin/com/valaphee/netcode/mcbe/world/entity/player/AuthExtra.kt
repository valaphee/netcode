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

package com.valaphee.netcode.mcbe.world.entity.player

import com.google.gson.JsonObject
import com.valaphee.netcode.mc.util.getString
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
data class AuthExtra(
    val userId: UUID,
    val xboxUserId: String,
    val userName: String
) {
    fun toJson(json: JsonObject) {
        json.addProperty("identity", userId.toString())
        json.addProperty("XUID", xboxUserId)
        json.addProperty("displayName", userName)
    }
}

val JsonObject.asAuthExtra get() = AuthExtra(UUID.fromString(getString("identity")), getString("XUID"), getString("displayName"))
