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

package com.valaphee.netcode.mcje.status

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.valaphee.netcode.mcje.util.text.Component
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class Status(
    @get:JsonProperty("version") val version: Version,
    @get:JsonProperty("modinfo") val mods: Mods?,
    @get:JsonProperty("players") val players: Players,
    @get:JsonProperty("description") val description: Component,
    @get:JsonProperty("favicon") val favicon: String?
) {
    class Version(
        @get:JsonProperty("name") val name: String,
        @get:JsonProperty("protocol") val protocol: Int
    )

    class Players(
        @get:JsonProperty("max") val maximum: Int,
        @get:JsonProperty("online") val current: Int,
        @get:JsonProperty("sample") val sample: List<Sample>?
    ) {
        data class Sample(
            @get:JsonProperty("id") val id: UUID,
            @get:JsonProperty("name") val name: String
        )
    }

    class Mods(
        @get:JsonProperty("type") val type: String,
        @get:JsonProperty("modlist") val mods: List<Mod>?
    ) {
        data class Mod(
            @get:JsonProperty("modid") val name: String,
            @get:JsonProperty("version") val version: String
        )
    }
}
