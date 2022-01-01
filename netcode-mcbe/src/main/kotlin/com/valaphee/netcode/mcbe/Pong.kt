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

package com.valaphee.netcode.mcbe

import com.valaphee.netcode.mcbe.world.GameMode
import java.util.StringJoiner

/**
 * @author Kevin Ludwig
 */
class Pong(
    val serverId: Long,
    val serverName: String? = null,
    val version: String? = null,
    val protocolVersion: Int,
    val edition: String = "MCPE",
    val nintendoLimited: Boolean = false,
    val gameMode: GameMode? = null,
    val playerCount: Int,
    val maximumPlayerCount: Int,
    val ipv4Port: Int = 19132,
    val ipv6Port: Int = 19133,
    val description: String? = null
) {
    override fun toString() = StringJoiner(";")
        .add(edition)
        .add(description ?: "")
        .add(protocolVersion.toString())
        .add(version ?: "")
        .add(playerCount.toString())
        .add(maximumPlayerCount.toString())
        .add(serverId.toString())
        .add(serverName ?: "")
        .add(gameMode?.key ?: "")
        .add(if (nintendoLimited) "0" else "1")
        .add(ipv4Port.toString())
        .add(ipv6Port.toString())
        .toString()
}
