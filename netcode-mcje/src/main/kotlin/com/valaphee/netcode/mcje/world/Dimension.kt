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
data class Dimension(
    @JsonProperty("ambient_light") val ambientLight: Float,
    @JsonProperty("bed_works") val bedWorks: Boolean,
    @JsonProperty("coordinate_scale") val coordinateScale: Double,
    @JsonProperty("effects") val effects: String,
    @JsonProperty("has_ceiling") val hasCeiling: Boolean,
    @JsonProperty("has_raids") val hasRaids: Boolean,
    @JsonProperty("has_skylight") val hasSkylight: Boolean,
    @JsonProperty("height") val height: Int?,
    @JsonProperty("infiniburn") val infinburn: String,
    @JsonProperty("logical_height") val logicalHeight: Int,
    @JsonProperty("min_y") val minY: Int?,
    @JsonProperty("natural") val natural: Boolean,
    @JsonProperty("piglin_safe") val piglinSafe: Boolean,
    @JsonProperty("respawn_anchor_works") val respawnAnchorWorks: Boolean,
    @JsonProperty("ultrawarm") val ultrawarm: Boolean
)
