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
class ModelData(
    @get:JsonProperty("format_version") val version: String,
    @get:JsonProperty("minecraft:geometry") val geometry: List<Model>,
)

/**
 * @author Kevin Ludwig
 */
@JsonTypeName("minecraft:geometry")
class Model(
    @get:JsonProperty("description") val description: Description,
    @get:JsonProperty("bones") val bones: List<Bone>,
) /*: Data */ {
    class Description(
        @get:JsonProperty("identifier") val key: String,
        @get:JsonProperty("texture_width") val textureWidth: Int,
        @get:JsonProperty("texture_height") val textureHeight: Int,
        @get:JsonProperty("visible_bounds_width") val visibleBoundsWidth: Float? = null,
        @get:JsonProperty("visible_bounds_height") val visibleBoundsHeight: Float? = null,
        @get:JsonProperty("visible_bounds_offset") val visibleBoundsOffset: Float3? = null,
    )

    class Bone(
        @get:JsonProperty("name") val identifier: String,
        @get:JsonProperty("cubes") val cubes: List<Cube>,
        @get:JsonProperty("pivot") val pivot: Float3? = null,
    ) {
        class Cube(
            @get:JsonProperty("origin") val origin: Float3,
            @get:JsonProperty("size") val size: Float3,
            @get:JsonProperty("uv") val uv: Map<String, Uv>
        ) {
            class Uv(
                @get:JsonProperty("uv") val uv: Float2,
                @get:JsonProperty("uv_size") val uvSize: Float2? = null
            )
        }
    }
}
