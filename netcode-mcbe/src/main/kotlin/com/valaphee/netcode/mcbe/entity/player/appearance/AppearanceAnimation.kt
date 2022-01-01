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

package com.valaphee.netcode.mcbe.entity.player.appearance

/**
 * @author Kevin Ludwig
 */
data class AppearanceAnimation constructor(
    val image: AppearanceImage,
    val type: Type,
    val frames: Float,
    val expression: Expression = Expression.Linear
) {
    enum class Type {
        None, Head, Body32, Body128
    }

    enum class Expression {
        Linear, Blinking
    }
}
