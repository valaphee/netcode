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

import com.valaphee.foundry.math.Int3
import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.PacketReader
import com.valaphee.netcode.mcje.network.ServerPlayPacketHandler
import com.valaphee.netcode.mcje.util.Registry

/**
 * @author Kevin Ludwig
 */
class ServerWorldEventPacket(
    val event: Event,
    val position: Int3,
    val data: Int,
    val relativeVolumeDisabled: Boolean
) : Packet<ServerPlayPacketHandler> {
    enum class Event {
        SoundClick,
        SoundClickFail,
        SoundShoot,
        SoundEyeLaunch,
        SoundFireworkShoot,
        SoundIronDoorOpen,
        SoundWoodenDoorOpen,
        SoundWoodenTrapdoorOpen,
        SoundFenceGateOpen,
        SoundFizz,
        SoundRecord,
        SoundIronDoorClose,
        SoundWoodenDoorClose,
        SoundWoodenTrapdoorClose,
        SoundFenceGateClose,
        SoundGhast,
        SoundGhastShoot,
        SoundEnderdragonShoot,
        SoundBlazeShoot,
        SoundWoodenDoorBump,
        SoundIronDoorBump,
        SoundWoodenDoorCrash,
        SoundWitherBreak,
        SoundWitherSpawn,
        SoundWitherShoot,
        SoundBatTakeoff,
        SoundZombieInfect,
        SoundZombieVillagerConvert,
        SoundEnderdragonDeath,
        SoundAnvilBreak,
        SoundAnvilUse,
        SoundAnvilFall,
        SoundPortal,
        SoundChorusFlowerGrow,
        SoundChorusFlowerDie,
        SoundBrew,
        SoundIronTrapdoorOpen,
        SoundIronTrapdoorClose,
        SoundEndPortalSpawn,
        SoundPhantomBite,
        SoundZombieConvertedToDrowned,
        SoundHuskConvertedToZombie,
        SoundGrindstoneUse,
        SoundBookPageTurn,
        ComposterComposts,
        LavaConvertsBlock,
        RedstoneTorchBurnout,
        EnderEyePlaced,
        ParticleSmoke,
        ParticleBreakBlock,
        ParticleSplash,
        ParticleEyeDespawn,
        ParticleSpawn,
        ParticleBonemeal,
        ParticleDragonBreath,
        ParticleWaterSplash,
        ParticleDragonDestroyBlock,
        ParticleEvaporation,
        EndGatewaySpawn,
        EnderdragonGrowl;

        companion object {
            val registry = Registry<Event>().apply {
                this[1000] = SoundClick
                this[1001] = SoundClickFail
                this[1002] = SoundShoot
                this[1003] = SoundEyeLaunch
                this[1004] = SoundFireworkShoot
                this[1005] = SoundIronDoorOpen
                this[1006] = SoundWoodenDoorOpen
                this[1007] = SoundWoodenTrapdoorOpen
                this[1008] = SoundFenceGateOpen
                this[1009] = SoundFizz
                this[1010] = SoundRecord
                this[1011] = SoundIronDoorClose
                this[1012] = SoundWoodenDoorClose
                this[1013] = SoundWoodenTrapdoorClose
                this[1014] = SoundFenceGateClose
                this[1015] = SoundGhast
                this[1016] = SoundGhastShoot
                this[1017] = SoundEnderdragonShoot
                this[1018] = SoundBlazeShoot
                this[1019] = SoundWoodenDoorBump
                this[1020] = SoundIronDoorBump
                this[1021] = SoundWoodenDoorCrash
                this[1022] = SoundWitherBreak
                this[1023] = SoundWitherSpawn
                this[1024] = SoundWitherShoot
                this[1025] = SoundBatTakeoff
                this[1026] = SoundZombieInfect
                this[1027] = SoundZombieVillagerConvert
                this[1028] = SoundEnderdragonDeath
                this[1029] = SoundAnvilBreak
                this[1030] = SoundAnvilUse
                this[1031] = SoundAnvilFall
                this[1032] = SoundPortal
                this[1033] = SoundChorusFlowerGrow
                this[1034] = SoundChorusFlowerDie
                this[1035] = SoundBrew
                this[1036] = SoundIronTrapdoorOpen
                this[1037] = SoundIronTrapdoorClose
                this[1038] = SoundEndPortalSpawn
                this[1039] = SoundPhantomBite
                this[1040] = SoundZombieConvertedToDrowned
                this[1041] = SoundHuskConvertedToZombie
                this[1042] = SoundGrindstoneUse
                this[1043] = SoundBookPageTurn
                this[1500] = ComposterComposts
                this[1501] = LavaConvertsBlock
                this[1502] = RedstoneTorchBurnout
                this[1503] = EnderEyePlaced
                this[2000] = ParticleSmoke
                this[2001] = ParticleBreakBlock
                this[2002] = ParticleSpawn
                this[2003] = ParticleEyeDespawn
                this[2004] = ParticleSpawn
                this[2005] = ParticleBonemeal
                this[2006] = ParticleDragonBreath
                this[2007] = ParticleWaterSplash
                this[2008] = ParticleDragonDestroyBlock
                this[2009] = ParticleEvaporation
                this[3000] = EndGatewaySpawn
                this[3001] = EnderdragonGrowl
            }
        }
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeInt(Event.registry.getId(event))
        buffer.writeInt3UnsignedY(position)
        buffer.writeInt(data)
        buffer.writeBoolean(relativeVolumeDisabled)
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.worldEvent(this)

    override fun toString() = "ServerWorldEventPacket(event=$event, position=$position, data=$data, relativeVolumeDisabled=$relativeVolumeDisabled)"
}

/**
 * @author Kevin Ludwig
 */
object ServerWorldEventPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerWorldEventPacket(checkNotNull(ServerWorldEventPacket.Event.registry[buffer.readInt()]), buffer.readInt3UnsignedY(), buffer.readInt(), buffer.readBoolean())
}
