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

package com.valaphee.netcode.mcje

import com.valaphee.netcode.mc.util.Registry
import com.valaphee.netcode.mcje.util.NamespacedKey

/**
 * @author Kevin Ludwig
 */
class RegistrySet(
    val sounds: Registry<NamespacedKey>,
    val effects: Registry<NamespacedKey>,
    val blocks: Registry<NamespacedKey>,
    val enchantments: Registry<NamespacedKey>,
    val entityTypes: Registry<NamespacedKey>,
    val items: Registry<NamespacedKey>,
    val particleTypes: Registry<NamespacedKey>,
    val windowTypes: Registry<NamespacedKey>,
    val recipeTypes: Registry<NamespacedKey>,
    val blockStates: Registry<NamespacedKey>,
)
