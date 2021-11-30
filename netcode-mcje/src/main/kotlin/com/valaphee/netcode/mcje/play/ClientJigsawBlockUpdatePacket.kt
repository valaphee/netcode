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

import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mcje.ClientPlayPacketHandler
import com.valaphee.netcode.mcje.Packet
import com.valaphee.netcode.mcje.PacketBuffer
import com.valaphee.netcode.mcje.PacketReader
import com.valaphee.netcode.mcje.util.NamespacedKey

/**
 * @author Kevin Ludwig
 */
class ClientJigsawBlockUpdatePacket(
    val position: Int3,
    val name: NamespacedKey,
    val target: NamespacedKey?,
    val pool: NamespacedKey,
    val finalState: String,
    val jointType: String?
) : Packet<ClientPlayPacketHandler> {
    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeInt3UnsignedY(position)
        buffer.writeNamespacedKey(name)
        if (version >= 754) buffer.writeNamespacedKey(target!!)
        buffer.writeNamespacedKey(pool)
        buffer.writeString(finalState)
        if (version >= 754) buffer.writeString(jointType!!)
    }

    override fun handle(handler: ClientPlayPacketHandler) = handler.jigsawBlockUpdate(this)

    override fun toString() = "ClientJigsawBlockUpdatePacket(position=$position, name=$name, target=$target, pool=$pool, finalState='$finalState', jointType=$jointType)"
}

/**
 * @author Kevin Ludwig
 */
object ClientJigsawBlockUpdatePacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ClientJigsawBlockUpdatePacket(buffer.readInt3UnsignedY(), buffer.readNamespacedKey(), if (version >= 754) buffer.readNamespacedKey() else null, buffer.readNamespacedKey(), buffer.readString(), if (version >= 754) buffer.readString() else null)
}
