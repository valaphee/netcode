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
import com.valaphee.netcode.slitherio.base.ChallengePacket
import com.valaphee.netcode.slitherio.base.ChallengeResponsePacket
import com.valaphee.netcode.slitherio.base.ConnectPacket
import com.valaphee.netcode.slitherio.base.LoginPacket
import com.valaphee.netcode.slitherio.base.PingPacket
import com.valaphee.netcode.slitherio.base.PongPacket
import com.valaphee.netcode.slitherio.base.SnakePacket
import com.valaphee.netcode.slitherio.base.WorldPacket

/**
 * @author Kevin Ludwig
 */
interface PacketHandler : ProtocolHandler {
    fun other(packet: Packet)

    fun connect(packet: ConnectPacket) = other(packet)

    fun challenge(packet: ChallengePacket) = other(packet)

    fun challengeResponse(packet: ChallengeResponsePacket) = other(packet)

    fun login(packet: LoginPacket) = other(packet)

    fun world(packet: WorldPacket) = other(packet)

    fun ping(packet: PingPacket) = other(packet)

    fun pong(packet: PongPacket) = other(packet)

    fun snake(packet: SnakePacket) = other(packet)
}
