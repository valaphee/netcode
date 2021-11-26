/*
 * MIT License
 *
 * Copyright (c) 2021, Valaphee.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.valaphee.netcode.slitherio

import com.valaphee.netcode.ProtocolHandler
import com.valaphee.netcode.slitherio.client.AnglePacket
import com.valaphee.netcode.slitherio.client.ChallengeResponsePacket
import com.valaphee.netcode.slitherio.client.ConnectPacket
import com.valaphee.netcode.slitherio.client.LoginPacket
import com.valaphee.netcode.slitherio.client.PingPacket
import com.valaphee.netcode.slitherio.server.ChallengePacket
import com.valaphee.netcode.slitherio.server.DisconnectPacket
import com.valaphee.netcode.slitherio.server.LeaderboardPacket
import com.valaphee.netcode.slitherio.server.PongPacket
import com.valaphee.netcode.slitherio.server.PreyPacket
import com.valaphee.netcode.slitherio.server.SnakePacket
import com.valaphee.netcode.slitherio.server.SnakeRotateCCWPacket
import com.valaphee.netcode.slitherio.server.WorldPacket

/**
 * @author Kevin Ludwig
 */
interface PacketHandler : ProtocolHandler {
    fun other(packet: Packet)

    fun unknown(packet: UnknownPacket) = other(packet)

    fun challenge(packet: ChallengePacket) = other(packet)

    fun challengeResponse(packet: ChallengeResponsePacket) = other(packet)

    fun world(packet: WorldPacket) = other(packet)

    fun connect(packet: ConnectPacket) = other(packet)

    fun pong(packet: PongPacket) = other(packet)

    fun login(packet: LoginPacket) = other(packet)

    fun ping(packet: PingPacket) = other(packet)


    fun snake(packet: SnakePacket) = other(packet)

    fun prey(packet: PreyPacket) = other(packet)

    fun leaderboard(packet: LeaderboardPacket) = other(packet)

    fun disconnect(packet: DisconnectPacket) = other(packet)

    fun angle(packet: AnglePacket) = other(packet)

    fun snakeRotateCCW(packet: SnakeRotateCCWPacket) = other(packet)
}
