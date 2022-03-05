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

package com.valaphee.netcode.mcbe.pack

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeName
import com.valaphee.foundry.math.Float2
import com.valaphee.foundry.math.Float3

/**
 * @author Kevin Ludwig
 */
@JsonTypeName("minecraft:geometry")
class Model(
    @JsonProperty("description") val description: Description,
    @JsonProperty("bones") val bones: List<Bone>,
) /*: Data */ {
    class Description(
        @JsonProperty("identifier") val key: String,
        @JsonProperty("texture_width") val textureWidth: Int,
        @JsonProperty("texture_height") val textureHeight: Int,
        @JsonProperty("visible_bounds_width") val visibleBoundsWidth: Float? = null,
        @JsonProperty("visible_bounds_height") val visibleBoundsHeight: Float? = null,
        @JsonProperty("visible_bounds_offset") val visibleBoundsOffset: Float3? = null,
    )

    class Bone(
        @JsonProperty("name") val identifier: String,
        @JsonProperty("cubes") val cubes: List<Cube>,
        @JsonProperty("pivot") val pivot: Float3? = null,
    ) {
        class Cube(
            @JsonProperty("origin") val origin: Float3,
            @JsonProperty("size") val size: Float3,
            @JsonProperty("uv") val uv: Map<String, Uv>
        ) {
            class Uv(
                @JsonProperty("uv") val uv: Float2,
                @JsonProperty("uv_size") val uvSize: Float2? = null
            )
        }
    }
}
