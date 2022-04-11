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
    @get:JsonProperty("format_version") val version: String,
    @get:JsonProperty("minecraft:geometry") val geometries: List<Geometry>,
)

/**
 * @author Kevin Ludwig
 */
@JsonTypeName("minecraft:geometry")
data class Geometry(
    @get:JsonProperty("description") val description: Description,
    @get:JsonProperty("bones") val bones: List<Bone>,
) /*: Data*/ {
    data class Description(
        @get:JsonProperty("identifier") val key: String,
        @get:JsonProperty("texture_width") val textureWidth: Int,
        @get:JsonProperty("texture_height") val textureHeight: Int,
        @get:JsonProperty("visible_bounds_width") val visibleBoundsWidth: Float?,
        @get:JsonProperty("visible_bounds_height") val visibleBoundsHeight: Float?,
        @get:JsonProperty("visible_bounds_offset") val visibleBoundsOffset: Float3?,
    )

    data class Bone(
        @get:JsonProperty("name") val name: String,
        @get:JsonProperty("parent") val parent: String?,
        @get:JsonProperty("cubes") val cubes: List<Cube>?,
        @get:JsonProperty("poly_mesh") val mesh: Mesh?,
        @get:JsonProperty("pivot") val pivot: Float3?,
        @get:JsonProperty("rotation") val rotation: Float3?,
        @get:JsonProperty("mirror") val mirror: Boolean?,
        @get:JsonProperty("neverRender") val ignore: Boolean?,
        @get:JsonProperty("locators") val locators: Map<String, Float3>?
    ) {
        data class Cube(
            @get:JsonProperty("origin") val origin: Float3,
            @get:JsonProperty("size") val size: Float3,
            @get:JsonProperty("uv") val uv: Any,
            @get:JsonProperty("mirror") val mirror: Boolean?,
            @get:JsonProperty("inflate") val inflate: Float?,
        )

        data class Mesh(
            @get:JsonProperty("normalized_uvs") val normalizedUvs: Boolean,
            @get:JsonProperty("positions") val positions: List<Float3>,
            @get:JsonProperty("normals") val normals: List<Float3>,
            @get:JsonProperty("uvs") val uvs: List<Float2>,
            @get:JsonProperty("polys") val indices: List<List<Int3>>
        )
    }
}
