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

package com.valaphee.netcode.mc.util

import com.valaphee.foundry.math.Float2
import com.valaphee.foundry.math.Int3
import com.valaphee.foundry.math.MutableFloat3
import com.valaphee.foundry.math.toRad
import java.util.EnumMap
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/**
 * @author Kevin Ludwig
 */
enum class Direction(
    private val noPitch: Boolean = false,
    private val noYaw: Boolean = false,
    private val noRoll: Boolean = false,
    val axis: Int3
) {
    Down(noYaw = true, axis = Int3(0, -1, 0)),
    Up(noYaw = true, axis = Int3(0, 1, 0)),
    North(noRoll = true, axis = Int3(0, 0, -1)),
    South(noRoll = true, axis = Int3(0, 0, 1)),
    West(noPitch = true, axis = Int3(-1, 0, 0)),
    East(noPitch = true, axis = Int3(1, 0, 0));

    fun opposite() = opposite[this]!!

    fun rotatePitch(steps: Int) = if (noPitch) this else when ((if (0 > steps) -steps + 2 else steps) % 4) {
        1 -> clockwisePitch[this]!!
        2 -> opposite[this]!!
        3 -> counterclockwisePitch[this]!!
        else -> this
    }

    fun rotateYaw(steps: Int) = if (noYaw) this else when ((if (0 > steps) -steps + 2 else steps) % 4) {
        1 -> clockwiseYaw[this]!!
        2 -> opposite[this]!!
        3 -> counterclockwiseYaw[this]!!
        else -> this
    }

    fun rotateRoll(steps: Int) = if (noRoll) this else when ((if (0 > steps) -steps + 2 else steps) % 4) {
        1 -> clockwiseRoll[this]!!
        2 -> opposite[this]!!
        3 -> counterclockwiseRoll[this]!!
        else -> this
    }

    companion object {
        private val horizontals = arrayOf(South, West, North, East)
        private val opposite = EnumMap<Direction, Direction>(Direction::class.java).apply {
            this[North] = South
            this[South] = North
            this[East] = West
            this[West] = East
            this[Up] = Down
            this[Down] = Up
        }
        private val clockwisePitch = EnumMap<Direction, Direction>(Direction::class.java).apply {
            this[North] = Up
            this[Down] = North
            this[South] = Down
            this[Up] = South
        }
        private val counterclockwisePitch = EnumMap<Direction, Direction>(Direction::class.java).apply {
            this[North] = Down
            this[Down] = South
            this[South] = Up
            this[Up] = North
        }
        private val clockwiseYaw = EnumMap<Direction, Direction>(Direction::class.java).apply {
            this[East] = North
            this[South] = East
            this[West] = South
            this[North] = West
        }
        private val counterclockwiseYaw = EnumMap<Direction, Direction>(Direction::class.java).apply {
            this[East] = South
            this[South] = West
            this[West] = North
            this[North] = East
        }
        private val clockwiseRoll = EnumMap<Direction, Direction>(Direction::class.java).apply {
            this[Up] = West
            this[West] = Down
            this[Down] = East
            this[East] = Up
        }
        private val counterclockwiseRoll = EnumMap<Direction, Direction>(Direction::class.java).apply {
            this[Up] = East
            this[West] = Up
            this[Down] = West
            this[East] = Down
        }

        fun fromIndex(index: Int) = values()[abs(index % values().size)]

        fun fromHorizontalIndex(index: Int) = horizontals[abs(index % horizontals.size)]
    }
}

fun Float2.toDirectionVector(): MutableFloat3 {
    val pitch = x.toRad()
    val yaw = y.toRad()
    val xz = cos(pitch)
    return MutableFloat3(-xz * sin(yaw), -sin(pitch), xz * cos(yaw)).normalize()
}

fun Float2.toHorizontalDirection() = Direction.fromHorizontalIndex(floor((y * 4.0f / 360.0f) + 0.5) and 0b11)
