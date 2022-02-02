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

package com.valaphee.netcode.mcje.play

import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mc.util.nbt.CompoundTag
import com.valaphee.netcode.mc.util.nbt.NbtInputStream
import com.valaphee.netcode.mc.util.nbt.NbtOutputStream
import com.valaphee.netcode.mcje.Packet
import com.valaphee.netcode.mcje.PacketBuffer
import com.valaphee.netcode.mcje.PacketReader
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream

/**
 * @author Kevin Ludwig
 */
class ServerBlockEntityPacket(
    val position: Int3,
    val type: Type,
    val tag: CompoundTag?
) : Packet<ServerPlayPacketHandler> {
    enum class Type {
        Unused,
        MobSpawner,
        CommandBlock,
        Beacon,
        Head,
        Conduit,
        Banner,
        StructureBlock,
        EndGateway,
        Sign,
        Unused2,
        Bed,
        JigsawBlock,
        Campfire,
        Beehive
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeInt3UnsignedY(position)
        buffer.writeByte(type.ordinal)
        NbtOutputStream(ByteBufOutputStream(buffer)).use { it.writeTag(tag!!) }
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.blockEntity(this)

    override fun toString() = "ServerBlockEntityPacket(position=$position, type=$type, tag=$tag)"
}

/**
 * @author Kevin Ludwig
 */
object ServerBlockEntityPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerBlockEntityPacket(buffer.readInt3UnsignedY(), ServerBlockEntityPacket.Type.values()[buffer.readByte().toInt()], NbtInputStream(ByteBufInputStream(buffer)).use { it.readTag() }?.asCompoundTag())
}
