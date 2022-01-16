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

package com.valaphee.netcode.mcbe.world.block

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import com.valaphee.foundry.math.Float3

/**
 * @author Kevin Ludwig
 */
interface BlockComponent

/**
 * @author Kevin Ludwig
 */
class BlockLightAbsorptionComponent(
    value: Float
) : BlockComponent

/**
 * @author Kevin Ludwig
 */
class BlockLightEmissionComponent(
    value: Float
) : BlockComponent

/**
 * @author Kevin Ludwig
 */
class CreativeCategoryComponent(
    @JsonProperty("category") val category: String,
    @JsonProperty("group") val group: String
) : BlockComponent

/**
 * @author Kevin Ludwig
 */
class DestroyTimeComponent(
    value: Float
) : BlockComponent

/**
 * @author Kevin Ludwig
 */
class EntityCollisionComponent(
    @JsonProperty("origin") val origin: Float3,
    @JsonProperty("size") val size: Float3
) : BlockComponent

/**
 * @author Kevin Ludwig
 */
class ExplosionResistanceComponent(
    value: Float
) : BlockComponent

/**
 * @author Kevin Ludwig
 */
class FrictionComponent(
    value: Float
) : BlockComponent

/**
 * @author Kevin Ludwig
 */
class GeometryComponent(
    value: String
) : BlockComponent

/**
 * @author Kevin Ludwig
 */
class MapColorComponent(
    value: String
) : BlockComponent

/**
 * @author Kevin Ludwig
 */
class MaterialInstancesComponent(
    value: Map<String, Material>,
) : BlockComponent {
    class Material(
        @JsonProperty("texture") val texture: String,
        @JsonProperty("render_method") val renderMethod: RenderMethod,
        @JsonProperty("face_dimming") val faceDimming: Boolean = true,
        @JsonProperty("ambient_occlusion") val ambientOcclusion: Boolean = true
    ) {
        enum class RenderMethod(
            @JsonValue val key: String
        ) {
            AlphaTest("alpha_test"),
            Blend("blend"),
            Opaque("opaque")
        }
    }
}

/**
 * @author Kevin Ludwig
 */
class OnPlayerPlacingComponent(
    @JsonProperty("event") val event: String
) : BlockComponent

/**
 * @author Kevin Ludwig
 */
class PickCollisionComponent(
    @JsonProperty("origin") val origin: Float3,
    @JsonProperty("size") val size: Float3
) : BlockComponent

/**
 * @author Kevin Ludwig
 */
class RotationComponent(
    value: Float3
) : BlockComponent
