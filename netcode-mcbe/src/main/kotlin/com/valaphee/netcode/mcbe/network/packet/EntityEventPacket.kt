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

import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler

/**
 * @author Kevin Ludwig
 */
class EntityEventPacket(
    val runtimeEntityId: Long,
    val event: Event,
    val data: Int = 0
) : Packet() {
    enum class Event {
        Unknown0,
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
        Unknown17,
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
        Unknown40,
        Unknown41,
        Unknown42,
        Unknown43,
        Unknown44,
        Unknown45,
        Unknown46,
        Unknown47,
        Unknown48,
        Unknown49,
        Unknown50,
        Unknown51,
        Unknown52,
        Unknown53,
        Unknown54,
        Unknown55,
        Unknown56,
        EatingItem,
        Unknown58,
        Unknown59,
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
        GrowUp,
        VibrationDetected,
        DrinkMilk
    }

    override val id get() = 0x1B

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarULong(runtimeEntityId)
        buffer.writeByte(event.ordinal)
        buffer.writeVarInt(data)
    }

    override fun handle(handler: PacketHandler) = handler.entityEvent(this)

    override fun toString() = "EntityEventPacket(runtimeEntityId=$runtimeEntityId, event=$event, data=$data)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = EntityEventPacket(buffer.readVarULong(), Event.values()[buffer.readUnsignedByte().toInt()], buffer.readVarInt())
    }
}
