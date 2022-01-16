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

/**
 * @author Kevin Ludwig
 */
class Block(
    @JsonProperty("molangVersion") val molangVersion: Int?,
    @JsonProperty("properties") val properties: List<Property>
) {
    class Property(
        @JsonProperty("name") val name: String,
        @JsonProperty("enum") val enum: Set<*>
    )
}

/**
 * @author Kevin Ludwig
 */
class BlockDef(
    @JsonProperty("description") val description: Description,
    @JsonProperty("events") val events: Map<String, Map<String, Any>>? = null,
    @JsonProperty("components") val components: List<BlockComponent>? = null,
    @JsonProperty("permutations") val permutations: List<Permutation>? = null
) {
    class Description(
        @JsonProperty("identifier") val key: String,
        @JsonProperty("properties") val properties: LinkedHashMap<String, LinkedHashSet<*>>? = null
    )

    class Permutation(
        @JsonProperty("condition") val condition: String,
        @JsonProperty("components") val components: List<BlockComponent>
    )
}
