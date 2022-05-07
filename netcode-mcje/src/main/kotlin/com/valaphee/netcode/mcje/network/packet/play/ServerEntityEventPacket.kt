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

import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.PacketReader
import com.valaphee.netcode.mcje.network.ServerPlayPacketHandler
import com.valaphee.netcode.mcje.network.Packet

/**
 * @author Kevin Ludwig
 */
class ServerEntityEventPacket(
    val entityId: Int,
    val event: Event
) : Packet<ServerPlayPacketHandler>() {
    enum class Event {
        TippedArrowParticles,
        JumpingParticlesOrResetSpawnerAnimation,
        HurtAnimation,
        DeathAnimation,
        ArmSwing,
        Unused,
        TameFail,
        TameSuccess,
        WolfShakeWet,
        UseItem,
        EatBlockAnimationOrIgnite,
        IronGolemOfferFlower,
        VillagerLoveParticles,
        VillagerHurt,
        VillagerStopTrading,
        WitchSpellParticles,
        ZombieVillagerCure,
        FireworkParticles,
        LoveParticles,
        ResetSquidRotation,
        ExplosionParticles,
        GuardianAttackAnimation,
        EnableReducedDebugScreen,
        DisableReducedDebugScreen,
        PermissionLevel0,
        PermissionLevel1,
        PermissionLevel2,
        PermissionLevel3,
        PermissionLevel4,
        ShieldBlockSound,
        ShieldBreakSound,
        FishHookPull,
        ArmorStandHit,
        HurtByThorns,
        IronGolemWithdrawFlower,
        ConsumeTotem,
        DrownAnimation,
        BurnAnimation,
        DolphinHappy,
        RavagerStunned,
        OcelotTameFail,
        OcelotTameSuccess,
        VillagerSweat,
        DeathSmokeCloud,
        SweatBerryBushHurtAnimation,
        EatAnimation,
        Teleport,
        EquipmentBreakMainHand,
        EquipmentBreakOffHand,
        EquipmentBreakHelmet,
        EquipmentBreakChestplate,
        EquipmentBreakLeggings,
        EquipmentBreakBoots,
        HoneyBlockSlide,
        HoneyBlockLand,
        PlayerSwapSameItem,
        WolfShakeWetStop
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeInt(entityId)
        buffer.writeByte(event.ordinal)
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.entityEvent(this)

    override fun toString() = "ServerEntityEventPacket(entityId=$entityId, event=$event)"
}

/**
 * @author Kevin Ludwig
 */
object ServerEntityEventPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = ServerEntityEventPacket(buffer.readInt(), ServerEntityEventPacket.Event.values()[buffer.readByte().toInt()])
}
