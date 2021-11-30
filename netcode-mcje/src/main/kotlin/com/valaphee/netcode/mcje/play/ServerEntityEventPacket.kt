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
class ServerEntityEventPacket(
    val entityId: Int,
    val event: Event
) : Packet<ServerPlayPacketHandler> {
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
    override fun read(buffer: PacketBuffer, version: Int) = ServerEntityEventPacket(buffer.readInt(), ServerEntityEventPacket.Event.values()[buffer.readUnsignedByte().toInt()])
}
