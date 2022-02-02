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

package com.valaphee.netcode.mcje.util.text

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

/**
 * @author Kevin Ludwig
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "action"
)
@JsonSubTypes(
    JsonSubTypes.Type(HoverEvent.ShowText::class, name = "show_text"),
    JsonSubTypes.Type(HoverEvent.ShowAchievement::class, name = "show_achievement"),
    JsonSubTypes.Type(HoverEvent.ShowItem::class, name = "show_item"),
    JsonSubTypes.Type(HoverEvent.ShowEntity::class, name = "show_entity")
)
abstract class HoverEvent(
    val actionKey: String
) {
    class ShowText : HoverEvent("show_text")

    class ShowAchievement : HoverEvent("show_achievement")

    class ShowItem : HoverEvent("show_item")

    class ShowEntity : HoverEvent("show_entity")
}
