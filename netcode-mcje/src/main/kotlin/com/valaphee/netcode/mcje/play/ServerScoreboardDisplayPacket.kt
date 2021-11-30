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

package com.valaphee.netcode.mcje.play

import com.valaphee.netcode.mcje.Packet
import com.valaphee.netcode.mcje.PacketBuffer
import com.valaphee.netcode.mcje.PacketReader

/**
 * @author Kevin Ludwig
 */
class ServerScoreboardDisplayPacket(
    val displaySlot: DisplaySlot,
    val name: String
) : Packet<ServerPlayPacketHandler> {
    enum class DisplaySlot {
        PlayerList,
        Sidebar,
        BelowName,
        BlackSidebar,
        DarkBlueSidebar,
        DarkGreenSidebar,
        DarkAquaSidebar,
        DarkRedSidebar,
        DarkPurpleSidebar,
        GoldSidebar,
        GraySidebar,
        DarkGraySidebar,
        BlueSidebar,
        GreenSidebar,
        AquaSidebar,
        RedSidebar,
        LightPurpleSidebar,
        YellowSidebar,
        WhiteSidebar
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeByte(displaySlot.ordinal)
        buffer.writeString(name)
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.scoreboardDisplay(this)

    override fun toString() = "ServerScoreboardDisplayPacket(displaySlot=$displaySlot, name='$name')"
}

/**
 * @author Kevin Ludwig
 */
object ServerScoreboardDisplayPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerScoreboardDisplayPacket(ServerScoreboardDisplayPacket.DisplaySlot.values()[buffer.readUnsignedByte().toInt()], buffer.readString(16))
}
