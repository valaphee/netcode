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

package com.valaphee.netcode.mcbe.inventory

/**
 * @author Kevin Ludwig
 */
object WindowId {
    const val DropContents = -100
    const val TradingOutputV2 = -30
    const val TradingInput2V2 = -29
    const val TradingInput1V2 = -28
    const val Beacon = -24
    const val TradingOutput = -23
    const val TradingUseInputs = -22
    const val TradingInput2 = -21
    const val TradingInput1 = -20
    const val EnchantOutput = -17
    const val EnchantMaterial = -16
    const val EnchantInput = -15
    const val AnvilOutput = -13
    const val AnvilResult = -12
    const val AnvilMaterial = -11
    const val ContainerInput = -10
    const val CraftingUseIngredient = -5
    const val CraftingResult = -4
    const val CraftingRemoveIngredient = -3
    const val CraftingAddIngredient = -2
    const val None = -1
    const val Inventory = 0
    const val First = 1
    const val Last = 100
    const val Offhand = 119
    const val Armor = 120
    const val Creative = 121
    const val Hotbar = 122
    const val FixedInventory = 123
    const val Ui = 124
}
