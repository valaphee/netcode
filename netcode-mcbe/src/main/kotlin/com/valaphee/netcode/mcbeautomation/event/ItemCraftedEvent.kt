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

package com.valaphee.netcode.mcbeautomation.event

import com.fasterxml.jackson.annotation.JsonProperty
import com.valaphee.netcode.mcbeautomation.Event

/**
 * @author Kevin Ludwig
 */
data class ItemCraftedEvent(
    @JsonProperty("player") val player: Entity,
    @JsonProperty("item") val item: ItemStack,
    @JsonProperty("count") val count: Int,
    @JsonProperty("usedCraftingTable") val usedCraftingTable: Boolean,
    @JsonProperty("usedSearchBar") val usedSearchBar: Boolean,
    @JsonProperty("startingTabId") val startingTabId: Int,
    @JsonProperty("endingTabId") val endingTabId: Int,
    @JsonProperty("numberOfTabsChanged") val numberOfTabsChanged: Int,
    @JsonProperty("recipeBookShown") val recipeBookOpen: Boolean,
    @JsonProperty("hasCraftableFilterOn") val recipeBookFilterActive: Boolean,
    @JsonProperty("craftedAutomatically") val auto: Boolean
) : Event()
