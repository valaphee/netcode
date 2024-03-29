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

package com.valaphee.netcode.mcbe.world.entity

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeName
import com.valaphee.foundry.math.Float2
import com.valaphee.foundry.math.Float3
import com.valaphee.foundry.math.Int3

/**
 * @author Kevin Ludwig
 */
data class GeometryData(
    @JsonProperty("format_version") val version: String,
    @JsonProperty("minecraft:geometry") val geometries: List<Geometry>,
)

/**
 * @author Kevin Ludwig
 */
@JsonTypeName("minecraft:geometry")
data class Geometry(
    @JsonProperty("description") val description: Description,
    @JsonProperty("bones") val bones: List<Bone>,
) /*: Data*/ {
    data class Description(
        @JsonProperty("identifier") val key: String,
        @JsonProperty("texture_width") val textureWidth: Int,
        @JsonProperty("texture_height") val textureHeight: Int,
        @JsonProperty("visible_bounds_width") val visibleBoundsWidth: Float?,
        @JsonProperty("visible_bounds_height") val visibleBoundsHeight: Float?,
        @JsonProperty("visible_bounds_offset") val visibleBoundsOffset: Float3?,
    )

    data class Bone(
        @JsonProperty("name") val name: String,
        @JsonProperty("parent") val parent: String?,
        @JsonProperty("cubes") val cubes: List<Cube>?,
        @JsonProperty("poly_mesh") val mesh: Mesh?,
        @JsonProperty("pivot") val pivot: Float3?,
        @JsonProperty("rotation") val rotation: Float3?,
        @JsonProperty("mirror") val mirror: Boolean?,
        @JsonProperty("neverRender") val ignore: Boolean?,
        @JsonProperty("locators") val locators: Map<String, Float3>?
    ) {
        data class Cube(
            @JsonProperty("origin") val origin: Float3,
            @JsonProperty("size") val size: Float3,
            @JsonProperty("uv") val uv: Any,
            @JsonProperty("mirror") val mirror: Boolean?,
            @JsonProperty("inflate") val inflate: Float?,
        )

        data class Mesh(
            @JsonProperty("normalized_uvs") val normalizedUvs: Boolean,
            @JsonProperty("positions") val positions: List<Float3>,
            @JsonProperty("normals") val normals: List<Float3>,
            @JsonProperty("uvs") val uvs: List<Float2>,
            @JsonProperty("polys") val indices: List<List<Int3>>
        )
    }
}
