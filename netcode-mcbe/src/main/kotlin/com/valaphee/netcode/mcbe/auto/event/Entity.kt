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

package com.valaphee.netcode.mcbe.auto.event

import com.fasterxml.jackson.annotation.JsonProperty
import com.valaphee.foundry.math.Float3

/**
 * @author Kevin Ludwig
 */
class Entity(
    @get:JsonProperty("id") val id: Long,
    @get:JsonProperty("type") val type: String,
    @get:JsonProperty("name") val name: String,
    @get:JsonProperty("dimension") val dimension: Int,
    @get:JsonProperty("position") val position: Float3,
    @get:JsonProperty("yRot") val yRot: Float,
    @get:JsonProperty("variant") val variant: Int,
    @get:JsonProperty("color") val color: String
) {
    override fun toString() = "Entity(id=$id, type='$type', name='$name', dimension=$dimension, position=$position, yRot=$yRot, variant=$variant, color='$color')"
}
