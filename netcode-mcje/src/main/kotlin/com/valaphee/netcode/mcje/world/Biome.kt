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

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @author Kevin Ludwig
 */
data class Biome(
    @JsonProperty("category") val category: String,
    @JsonProperty("depth") val depth: Float,
    @JsonProperty("downfall") val downfall: Float,
    @JsonProperty("effects") val effects: Effects,
    @JsonProperty("precipitation") val precipitation: String,
    @JsonProperty("scale") val scale: Float,
    @JsonProperty("temperature") val temperature: Float,
    @JsonProperty("temperature_modifier") val temperatureModifier: String?
) {
    data class Effects(
        @JsonProperty("additions_sound") val additionsSound: AdditionsSound?,
        @JsonProperty("ambient_sound") val ambientSound: String?,
        @JsonProperty("fog_color") val fogColor: Int,
        @JsonProperty("foliage_color") val foliageColor: Int?,
        @JsonProperty("grass_color") val grassColor: Int?,
        @JsonProperty("grass_color_modifier") val grassColorModifier: String?,
        @JsonProperty("mood_sound") val moodSound: MoodSound?,
        @JsonProperty("music") val music: Music?,
        @JsonProperty("sky_color") val skyColor: Int,
        @JsonProperty("water_color") val waterColor: Int,
        @JsonProperty("water_fog_color") val waterFogColor: Int
    ) {
        data class Music(
            @JsonProperty("replace_current_music") val replaceCurrentMusic: Boolean,
            @JsonProperty("sound") val sound: String,
            @JsonProperty("max_delay") val maxDelay: Int,
            @JsonProperty("min_delay") val minDelay: Int
        )

        data class AdditionsSound(
            @JsonProperty("sound") val sound: String,
            @JsonProperty("tick_chance") val tickChance: Double
        )

        data class MoodSound(
            @JsonProperty("block_search_extent") val blockSearchExtent: Int,
            @JsonProperty("offset") val offset: Double,
            @JsonProperty("sound") val sound: String,
            @JsonProperty("tick_delay") val tickDelay: Int
        )
    }
}
