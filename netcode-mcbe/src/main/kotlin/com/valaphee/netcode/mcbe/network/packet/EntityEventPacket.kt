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
        GrowUp;

        companion object {
            val registry = Registry<Event>().apply {
                this[0x01] = JumpAnimation
                this[0x02] = HurtAnimation
                this[0x03] = DeathAnimation
                this[0x04] = AttackStart
                this[0x05] = AttackStop
                this[0x06] = TameFail
                this[0x07] = TameSuccess
                this[0x08] = WolfShakeWet
                this[0x09] = UseItem
                this[0x0A] = EatBlockAnimation
                this[0x0B] = FishHookBubble
                this[0x0C] = FishHookPosition
                this[0x0D] = FishHookHook
                this[0x0E] = FishHookLured
                this[0x0F] = SquidInkCloud
                this[0x10] = ZombieVillagerCure
                this[0x12] = Respawn
                this[0x13] = IronGolemOfferFlower
                this[0x14] = IronGolemWithdrawFlower
                this[0x15] = LoveParticles
                this[0x16] = VillagerHurt
                this[0x17] = VillagerStopTrading
                this[0x18] = WitchSpellParticles
                this[0x19] = FireworkParticles
                this[0x1A] = InLoveHearts
                this[0x1B] = ExplosionParticles
                this[0x1C] = GuardianAttackAnimation
                this[0x1D] = WitchDrinkPotion
                this[0x1E] = WitchThrowPotion
                this[0x1F] = MinecartTntPrimeFuse
                this[0x20] = PrimeCreeper
                this[0x21] = AirSupply
                this[0x22] = PlayerAddXpLevels
                this[0x23] = ElderGuardianCurse
                this[0x24] = AgentArmSwing
                this[0x25] = EnderDragonDeath
                this[0x26] = DustParticles
                this[0x27] = ArrowShake
                this[0x39] = EatingItem
                this[0x3C] = BabyAnimalFeed
                this[0x3D] = DeathSmokeCloud
                this[0x3E] = CompleteTrade
                this[0x3F] = RemoveLeash
                this[0x40] = Caravan
                this[0x41] = ConsumeTotem
                this[0x42] = CheckTreasureHunterAchievement
                this[0x43] = Spawn
                this[0x44] = DragonFlaming
                this[0x45] = MergeStack
                this[0x46] = StartSwimming
                this[0x47] = BalloonPop
                this[0x48] = FindTreasureBribe
                this[0x49] = SummonAgent
                this[0x4A] = FinishedChargingCrossbow
                this[0x4B] = LandedOnGround
                this[0x4C] = GrowUp
            }
        }
    }

    override val id get() = 0x1B

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarULong(runtimeEntityId)
        buffer.writeByte(Event.registry.getId(event))
        buffer.writeVarInt(data)
    }

    override fun handle(handler: PacketHandler) = handler.entityEvent(this)

    override fun toString() = "EntityEventPacket(runtimeEntityId=$runtimeEntityId, event=$event, data=$data)"
}

/**
 * @author Kevin Ludwig
 */
object EntityEventPacketReader : PacketReader {
    override fun read(buffer: PacketBuffer, version: Int) = EntityEventPacket(buffer.readVarULong(), checkNotNull(EntityEventPacket.Event.registry[buffer.readUnsignedByte().toInt()]), buffer.readVarInt())
}
