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

package com.valaphee.netcode.mcje.network.packet.status

import com.fasterxml.jackson.annotation.JsonProperty
import net.kyori.adventure.text.Component
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
data class Status(
    @JsonProperty("version") val version: Version,
    @JsonProperty("modinfo") val mods: Mods?,
    @JsonProperty("players") val players: Players,
    @JsonProperty("description") val description: Component,
    @JsonProperty("favicon") val favicon: String?
) {
    data class Version(
        @JsonProperty("name") val name: String,
        @JsonProperty("protocol") val protocol: Int
    )

    data class Players(
        @JsonProperty("max") val maximum: Int,
        @JsonProperty("online") val current: Int,
        @JsonProperty("sample") val sample: List<Sample>?
    ) {
        data class Sample(
            @JsonProperty("id") val id: UUID,
            @JsonProperty("name") val name: String
        )
    }

    data class Mods(
        @JsonProperty("type") val type: String,
        @JsonProperty("modlist") val mods: List<Mod>?
    ) {
        data class Mod(
            @JsonProperty("modid") val name: String,
            @JsonProperty("version") val version: String
        )
    }
}
