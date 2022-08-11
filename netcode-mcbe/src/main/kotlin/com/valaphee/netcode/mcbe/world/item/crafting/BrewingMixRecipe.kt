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

package com.valaphee.netcode.mcbe.world.item.crafting

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeName
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.valaphee.netcode.mcbe.pack.Data
import com.valaphee.netcode.mcbe.util.Registry
import com.valaphee.netcode.mcbe.world.item.ItemStack

/**
 * @author Kevin Ludwig
 */
@JsonTypeName("minecraft:recipe_brewing_mix")
data class BrewingMixRecipe(
    @get:JsonProperty("description") val description: Description,
    @get:JsonProperty("tags") val tags: List<String>,
    @get:JsonProperty("input") @get:JsonSerialize(using = PotionSerializer::class) @get:JsonDeserialize(using = PotionDeserializer::class) val input: ItemStack,
    @get:JsonProperty("reagent") @get:JsonSerialize(using = SingleItemSerializer::class) @get:JsonDeserialize(using = SingleItemDeserializer::class) val reagent: ItemStack,
    @get:JsonProperty("output") @get:JsonSerialize(using = PotionSerializer::class) @get:JsonDeserialize(using = PotionDeserializer::class) val output: ItemStack
) : Data() {
    data class Description(
        @get:JsonProperty("identifier") val key: String
    )

    object PotionSerializer : JsonSerializer<ItemStack>() {
        override fun serialize(value: ItemStack, generator: JsonGenerator, provider: SerializerProvider) {
            generator.writeString(if (value.itemKey == "minecraft:potion") "minecraft:potion_type:${checkNotNull(potionTypes[value.subId])}" else error("Unexpected item in brewing mix recipe: ${value.itemKey}"))
        }
    }

    object PotionDeserializer : JsonDeserializer<ItemStack>() {
        override fun deserialize(parser: JsonParser, context: DeserializationContext): ItemStack {
            val key = parser.readValueAs(String::class.java)
            return if (key.startsWith("minecraft:potion_type:")) ItemStack("minecraft:potion", potionTypes.getId(key.removePrefix("minecraft:potion_type:"))) else error("Unexpected item in brewing mix recipe: $key")
        }
    }
}
