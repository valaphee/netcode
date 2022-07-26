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

package com.valaphee.netcode.mcje.network.packet.play

import com.fasterxml.jackson.module.kotlin.readValue
import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.PacketReader
import com.valaphee.netcode.mcje.network.ServerPlayPacketHandler
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream
import java.io.OutputStream

/**
 * @author Kevin Ludwig
 */
class ServerBlockEntityPacket(
    val position: Int3,
    val type: Type,
    val data: Any?
) : Packet<ServerPlayPacketHandler>() {
    enum class Type {
        Furnace,
        Chest,
        TrappedChest,
        EnderChest,
        Jukebox,
        Dispenser,
        Dropper,
        Sign,
        MobSpawner,
        Piston,
        BrewingStand,
        EnchantingTable,
        EndPortal,
        Beacon,
        Skull,
        DaylightDetector,
        Hopper,
        Comparator,
        Banner,
        StructureBlock,
        EndGateway,
        CommandBlock,
        ShulkerBox,
        Bed,
        Conduit,
        Barrel,
        Smoker,
        BlastFurnace,
        Lectern,
        Bell,
        Jigsaw,
        Campfire,
        Beehive,
        SculkSensor,
        SculkCatalyst,
        SculkShrieker
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeInt3UnsignedY(position)
        if (version >= 758) buffer.writeVarInt(type.ordinal) else buffer.writeByte(type.ordinal)
        buffer.nbtObjectMapper.writeValue(ByteBufOutputStream(buffer) as OutputStream, data)
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.blockEntity(this)

    override fun toString() = "ServerBlockEntityPacket(position=$position, type=$type, data=$data)"
}

/**
 * @author Kevin Ludwig
 */
object ServerBlockEntityPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerBlockEntityPacket(buffer.readInt3UnsignedY(), ServerBlockEntityPacket.Type.values()[if (version >= 758) buffer.readVarInt() else buffer.readByte().toInt()], buffer.nbtObjectMapper.readValue(ByteBufInputStream(buffer)))
}
