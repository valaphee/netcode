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
    @JsonProperty("description") val description: Description,
    @JsonProperty("tags") val tags: List<String>,
    @JsonProperty("input") @JsonSerialize(using = PotionSerializer::class) @JsonDeserialize(using = PotionDeserializer::class) val input: ItemStack,
    @JsonProperty("reagent") @JsonSerialize(using = SingleItemSerializer::class) @JsonDeserialize(using = SingleItemDeserializer::class) val reagent: ItemStack,
    @JsonProperty("output") @JsonSerialize(using = PotionSerializer::class) @JsonDeserialize(using = PotionDeserializer::class) val output: ItemStack
) : Data {
    data class Description(
        @JsonProperty("identifier") val key: String
    )

    object PotionSerializer : JsonSerializer<ItemStack>() {
        override fun serialize(value: ItemStack, generator: JsonGenerator, provider: SerializerProvider) {
            generator.writeString(if (value.item == "minecraft:potion") "minecraft:potion_type:${checkNotNull(potionTypes[value.subId])}" else TODO("$value"))
        }
    }

    object PotionDeserializer : JsonDeserializer<ItemStack>() {
        override fun deserialize(parser: JsonParser, context: DeserializationContext): ItemStack {
            val key = parser.readValueAs(String::class.java)
            return if (key.startsWith("minecraft:potion_type:")) ItemStack("minecraft:potion", potionTypes.getId(key.removePrefix("minecraft:potion_type:"))) else TODO(key)
        }
    }

    companion object {
        val potionTypes = Registry<String>().apply {
            this[ 0] = "water"
            this[ 1] = "mundane"
            this[ 3] = "thick"
            this[ 4] = "awkward"
            this[ 5] = "night_vision"
            this[ 6] = "long_night_vision"
            this[ 7] = "invisibility"
            this[ 8] = "long_invisibility"
            this[ 9] = "leaping"
            this[10] = "long_leaping"
            this[11] = "strong_leaping"
            this[12] = "fire_resistance"
            this[13] = "long_fire_resistance"
            this[14] = "swiftness"
            this[15] = "long_swiftness"
            this[16] = "strong_swiftness"
            this[17] = "slowness"
            this[18] = "long_slowness"
            this[19] = "water_breathing"
            this[20] = "long_water_breathing"
            this[21] = "healing"
            this[22] = "strong_healing"
            this[23] = "harming"
            this[24] = "strong_harming"
            this[25] = "poison"
            this[26] = "long_poison"
            this[27] = "strong_poison"
            this[28] = "regeneration"
            this[29] = "long_regeneration"
            this[30] = "strong_regeneration"
            this[31] = "strength"
            this[32] = "long_strength"
            this[33] = "strong_strength"
            this[34] = "weakness"
            this[35] = "long_weakness"
            this[37] = "turtle_master"
            this[38] = "long_turtle_master"
            this[39] = "strong_turtle_master"
            this[40] = "slow_falling"
            this[41] = "long_slow_falling"
        }
    }
}
