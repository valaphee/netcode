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

package com.valaphee.netcode.mcbe.network.packet

import com.valaphee.netcode.mc.util.Registry
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.PacketReader

/**
 * @author Kevin Ludwig
 */
class EntityEventPacket(
    val runtimeEntityId: Long,
    val event: Event,
    val data: Int = 0
) : Packet() {
    enum class Event {
        JumpAnimation,
        HurtAnimation,
        DeathAnimation,
        AttackStart,
        AttackStop,
        TameFail,
        TameSuccess,
        WolfShakeWet,
        UseItem,
        EatBlockAnimation,
        FishHookBubble,
        FishHookPosition,
        FishHookHook,
        FishHookLured,
        SquidInkCloud,
        ZombieVillagerCure,
        Respawn,
        IronGolemOfferFlower,
        IronGolemWithdrawFlower,
        VillagerHurt,
        LoveParticles,
        VillagerStopTrading,
        WitchSpellParticles,
        FireworkParticles,
        InLoveHearts,
        ExplosionParticles,
        GuardianAttackAnimation,
        WitchDrinkPotion,
        WitchThrowPotion,
        MinecartTntPrimeFuse,
        PrimeCreeper,
        AirSupply,
        PlayerAddXpLevels,
        ElderGuardianCurse,
        AgentArmSwing,
        EnderDragonDeath,
        DustParticles,
        ArrowShake,
        EatingItem,
        BabyAnimalFeed,
        DeathSmokeCloud,
        CompleteTrade,
        RemoveLeash,
        Caravan,
        ConsumeTotem,
        CheckTreasureHunterAchievement,
        Spawn,
        DragonFlaming,
        MergeStack,
        StartSwimming,
        BalloonPop,
        FindTreasureBribe,
        SummonAgent,
        FinishedChargingCrossbow,
        LandedOnGround,
        GrowUp
    }

    override val id get() = 0x1B

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarULong(runtimeEntityId)
        buffer.writeByte(events.getId(event))
        buffer.writeVarInt(data)
    }

    override fun handle(handler: PacketHandler) = handler.entityEvent(this)

    override fun toString() = "EntityEventPacket(runtimeEntityId=$runtimeEntityId, event=$event, data=$data)"

    companion object {
        internal val events = Registry<Event>().apply {
            this[0x01] = Event.JumpAnimation
            this[0x02] = Event.HurtAnimation
            this[0x03] = Event.DeathAnimation
            this[0x04] = Event.AttackStart
            this[0x05] = Event.AttackStop
            this[0x06] = Event.TameFail
            this[0x07] = Event.TameSuccess
            this[0x08] = Event.WolfShakeWet
            this[0x09] = Event.UseItem
            this[0x0A] = Event.EatBlockAnimation
            this[0x0B] = Event.FishHookBubble
            this[0x0C] = Event.FishHookPosition
            this[0x0D] = Event.FishHookHook
            this[0x0E] = Event.FishHookLured
            this[0x0F] = Event.SquidInkCloud
            this[0x10] = Event.ZombieVillagerCure
            this[0x12] = Event.Respawn
            this[0x13] = Event.IronGolemOfferFlower
            this[0x14] = Event.IronGolemWithdrawFlower
            this[0x15] = Event.LoveParticles
            this[0x16] = Event.VillagerHurt
            this[0x17] = Event.VillagerStopTrading
            this[0x18] = Event.WitchSpellParticles
            this[0x19] = Event.FireworkParticles
            this[0x1A] = Event.InLoveHearts
            this[0x1B] = Event.ExplosionParticles
            this[0x1C] = Event.GuardianAttackAnimation
            this[0x1D] = Event.WitchDrinkPotion
            this[0x1E] = Event.WitchThrowPotion
            this[0x1F] = Event.MinecartTntPrimeFuse
            this[0x20] = Event.PrimeCreeper
            this[0x21] = Event.AirSupply
            this[0x22] = Event.PlayerAddXpLevels
            this[0x23] = Event.ElderGuardianCurse
            this[0x24] = Event.AgentArmSwing
            this[0x25] = Event.EnderDragonDeath
            this[0x26] = Event.DustParticles
            this[0x27] = Event.ArrowShake
            this[0x39] = Event.EatingItem
            this[0x3C] = Event.BabyAnimalFeed
            this[0x3D] = Event.DeathSmokeCloud
            this[0x3E] = Event.CompleteTrade
            this[0x3F] = Event.RemoveLeash
            this[0x40] = Event.Caravan
            this[0x41] = Event.ConsumeTotem
            this[0x42] = Event.CheckTreasureHunterAchievement
            this[0x43] = Event.Spawn
            this[0x44] = Event.DragonFlaming
            this[0x45] = Event.MergeStack
            this[0x46] = Event.StartSwimming
            this[0x47] = Event.BalloonPop
            this[0x48] = Event.FindTreasureBribe
            this[0x49] = Event.SummonAgent
            this[0x4A] = Event.FinishedChargingCrossbow
            this[0x4B] = Event.LandedOnGround
            this[0x4C] = Event.GrowUp
        }
    }
}

/**
 * @author Kevin Ludwig
 */
object EntityEventPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = EntityEventPacket(buffer.readVarULong(), checkNotNull(EntityEventPacket.events[buffer.readUnsignedByte().toInt()]), buffer.readVarInt())
}
