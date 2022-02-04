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

package com.valaphee.netcode.mcbe.pack.block

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import com.valaphee.foundry.math.Float3
import com.valaphee.netcode.mc.nbt.Nbt
import com.valaphee.netcode.mc.nbt.compoundTag
import com.valaphee.netcode.mc.nbt.toTag
import com.valaphee.netcode.mc.util.setBool
import com.valaphee.netcode.mcbe.pack.Component
import com.valaphee.netcode.mcbe.pack.DataType
import com.valaphee.netcode.mcbe.pack.WrapperComponent

/**
 * @author Kevin Ludwig
 */
interface BlockComponent : Component

/**
 * @author Kevin Ludwig
 */
@DataType("minecraft:block_light_absorption")
class BlockLightAbsorptionComponent(
    value: Float
) : BlockComponent, WrapperComponent<Float>(value), Nbt {
    override fun toTag() = compoundTag().apply { setFloat("value", value) }
}

/**
 * @author Kevin Ludwig
 */
@DataType("minecraft:block_light_emission")
class BlockLightEmissionComponent(
    value: Float
) : BlockComponent, WrapperComponent<Float>(value), Nbt {
    override fun toTag() = compoundTag().apply { setFloat("value", value) }
}

/**
 * @author Kevin Ludwig
 */
@DataType("minecraft:creative_category")
class CreativeCategoryComponent(
    @get:JsonProperty("category") val category: String,
    @get:JsonProperty("group") val group: String
) : BlockComponent, Nbt {
    override fun toTag() = compoundTag().apply {
        setString("category", category)
        setString("group", group)
    }
}

/**
 * @author Kevin Ludwig
 */
@DataType("minecraft:destroy_time")
class DestroyTimeComponent(
    value: Float
) : BlockComponent, WrapperComponent<Float>(value), Nbt {
    override fun toTag() = compoundTag().apply { setFloat("value", value) }
}

/**
 * @author Kevin Ludwig
 */
@DataType("minecraft:entity_collision")
class EntityCollisionComponent(
    @get:JsonProperty("origin") val origin: Float3,
    @get:JsonProperty("size") val size: Float3
) : BlockComponent, Nbt {
    override fun toTag() = compoundTag().apply {
        setBool("enabled", true)
        this["origin"] = origin.toMutableFloat3().vector.toList().toTag()
        this["size"] = this@EntityCollisionComponent.size.toMutableFloat3().vector.toList().toTag()
    }
}

/**
 * @author Kevin Ludwig
 */
@DataType("minecraft:explosion_resistance")
class ExplosionResistanceComponent(
    value: Float
) : BlockComponent, WrapperComponent<Float>(value), Nbt {
    override fun toTag() = compoundTag().apply { setFloat("value", value) }
}

/**
 * @author Kevin Ludwig
 */
@DataType("minecraft:friction")
class FrictionComponent(
    value: Float
) : BlockComponent, WrapperComponent<Float>(value), Nbt {
    override fun toTag() = compoundTag().apply { setFloat("value", value) }
}

/**
 * @author Kevin Ludwig
 */
@DataType("minecraft:geometry")
class GeometryComponent(
    value: String
) : BlockComponent, WrapperComponent<String>(value), Nbt {
    override fun toTag() = compoundTag().apply { setString("value", value) }
}

/**
 * @author Kevin Ludwig
 */
@DataType("minecraft:map_color")
class MapColorComponent(
    value: String
) : BlockComponent, WrapperComponent<String>(value), Nbt {
    override fun toTag() = compoundTag().apply { setString("value", value) }
}

/**
 * @author Kevin Ludwig
 */
@DataType("minecraft:material_instances")
class MaterialInstancesComponent(
    value: Map<String, Material>,
) : BlockComponent, WrapperComponent<Map<String, MaterialInstancesComponent.Material>>(value), Nbt {
    class Material(
        @get:JsonProperty("texture") val texture: String,
        @get:JsonProperty("render_method") val renderMethod: RenderMethod,
        @get:JsonProperty("face_dimming") val faceDimming: Boolean = true,
        @get:JsonProperty("ambient_occlusion") val ambientOcclusion: Boolean = true
    ) : Nbt {
        enum class RenderMethod(
            @get:JsonValue val key: String
        ) {
            AlphaTest("alpha_test"),
            Blend("blend"),
            Opaque("opaque")
        }

        override fun toTag() = compoundTag().apply {
            setString("texture", texture)
            setString("render_method", renderMethod.key)
            setBool("face_dimming", faceDimming)
            setBool("ambient_occlusion", ambientOcclusion)
        }
    }

    override fun toTag() = compoundTag().apply {
        this["mappings"] = compoundTag()
        this["materials"] = value.toTag()
    }
}

/**
 * @author Kevin Ludwig
 */
@DataType("minecraft:on_player_placing")
class OnPlayerPlacingComponent(
    @get:JsonProperty("event") val event: String
) : BlockComponent, Nbt {
    override fun toTag() = compoundTag().apply { setString("triggerType", event) }
}

/**
 * @author Kevin Ludwig
 */
@DataType("minecraft:pick_collision")
class PickCollisionComponent(
    @get:JsonProperty("origin") val origin: Float3,
    @get:JsonProperty("size") val size: Float3
) : BlockComponent, Nbt {
    override fun toTag() = compoundTag().apply {
        setBool("enabled", true)
        this["origin"] = origin.toMutableFloat3().vector.toList().toTag()
        this["size"] = this@PickCollisionComponent.size.toMutableFloat3().vector.toList().toTag()
    }
}

/**
 * @author Kevin Ludwig
 */
@DataType("minecraft:rotation")
class RotationComponent(
    value: Float3
) : BlockComponent, WrapperComponent<Float3>(value), Nbt {
    override fun toTag() = compoundTag().apply {
        setFloat("x", value.x)
        setFloat("y", value.y)
        setFloat("z", value.z)
    }
}
