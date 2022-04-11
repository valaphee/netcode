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
    @get:JsonProperty("category") val category: String,
    @get:JsonProperty("depth") val depth: Float,
    @get:JsonProperty("downfall") val downfall: Float,
    @get:JsonProperty("effects") val effects: Effects,
    @get:JsonProperty("precipitation") val precipitation: String,
    @get:JsonProperty("scale") val scale: Float,
    @get:JsonProperty("temperature") val temperature: Float,
    @get:JsonProperty("temperature_modifier") val temperatureModifier: String?
) {
    data class Effects(
        @get:JsonProperty("additions_sound") val additionsSound: AdditionsSound?,
        @get:JsonProperty("ambient_sound") val ambientSound: String?,
        @get:JsonProperty("fog_color") val fogColor: Int,
        @get:JsonProperty("foliage_color") val foliageColor: Int?,
        @get:JsonProperty("grass_color") val grassColor: Int?,
        @get:JsonProperty("grass_color_modifier") val grassColorModifier: String?,
        @get:JsonProperty("mood_sound") val moodSound: MoodSound?,
        @get:JsonProperty("music") val music: Music?,
        @get:JsonProperty("sky_color") val skyColor: Int,
        @get:JsonProperty("water_color") val waterColor: Int,
        @get:JsonProperty("water_fog_color") val waterFogColor: Int
    ) {
        data class Music(
            @get:JsonProperty("replace_current_music") val replaceCurrentMusic: Boolean,
            @get:JsonProperty("sound") val sound: String,
            @get:JsonProperty("max_delay") val maxDelay: Int,
            @get:JsonProperty("min_delay") val minDelay: Int
        )

        data class AdditionsSound(
            @get:JsonProperty("sound") val sound: String,
            @get:JsonProperty("tick_chance") val tickChance: Double
        )

        data class MoodSound(
            @get:JsonProperty("block_search_extent") val blockSearchExtent: Int,
            @get:JsonProperty("offset") val offset: Double,
            @get:JsonProperty("sound") val sound: String,
            @get:JsonProperty("tick_delay") val tickDelay: Int
        )
    }
}
