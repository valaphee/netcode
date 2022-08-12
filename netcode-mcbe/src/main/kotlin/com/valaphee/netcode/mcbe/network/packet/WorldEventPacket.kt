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

import com.valaphee.foundry.math.Float3
import com.valaphee.netcode.mcbe.network.Packet
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.network.PacketHandler
import com.valaphee.netcode.mcbe.network.Restrict
import com.valaphee.netcode.mcbe.network.Restriction
import com.valaphee.netcode.mcbe.network.V1_14_060
import com.valaphee.netcode.mcbe.network.V1_16_010
import com.valaphee.netcode.mcbe.network.V1_16_210
import com.valaphee.netcode.mcbe.network.V1_16_221
import com.valaphee.netcode.mcbe.network.V1_17_002
import com.valaphee.netcode.mcbe.network.V1_17_011
import com.valaphee.netcode.mcbe.network.V1_17_034
import com.valaphee.netcode.mcbe.network.V1_17_041
import com.valaphee.netcode.mcbe.network.V1_18_002
import com.valaphee.netcode.mcbe.network.V1_18_030
import com.valaphee.netcode.mcbe.network.V1_19_000
import com.valaphee.netcode.util.Int2ObjectOpenHashBiMapVersioned

/**
 * @author Kevin Ludwig
 */
@Restrict(Restriction.ToClient)
class WorldEventPacket(
    val event: Event,
    val position: Float3,
    val data: Int = 0
) : Packet() {
    enum class Event {
        Undefined,
        SoundClick,
        SoundClickFail,
        SoundLaunch,
        SoundDoorOpen,
        SoundFizz,
        SoundFuse,
        SoundPlayRecording,
        SoundGhastWarning,
        SoundGhastFireball,
        SoundBlazeFireball,
        SoundZombieDoorBump,
        SoundZombieDoorCrash,
        SoundZombieInfected,
        SoundZombieConverted,
        SoundEndermanTeleport,
        SoundAnvilBroken,
        SoundAnvilUsed,
        SoundAnvilLand,
        SoundInfinityArrowPickup,
        SoundTeleportEnderpearl,
        SoundItemframeItemAdd,
        SoundItemframeBreak,
        SoundItemframePlace,
        SoundItemframeItemRemove,
        SoundItemframeItemRotate,
        SoundCamera,
        SoundExperienceOrbPickup,
        SoundTotemUsed,
        SoundArmorStandBreak,
        SoundArmorStandHit,
        SoundArmorStandLand,
        SoundArmorStandPlace,
        SoundPointedDripstoneLand,
        SoundDyeUsed,
        SoundInkSaceUsed,
        ParticleShoot,
        ParticleDestroyBlock,
        ParticlePotionSplash,
        ParticleEyeOfEnderDeath,
        ParticleMobBlockSpawn,
        ParticleCropGrowth,
        ParticleSoundGuardianGhost,
        ParticleDeathSmoke,
        ParticleDenyBlock,
        ParticleGenericSpawn,
        ParticleDragonEgg,
        ParticleCropEaten,
        ParticleCrit,
        ParticleTeleport,
        ParticleCrackBlock,
        ParticleBubbles,
        ParticleEvaporate,
        ParticleDestroyArmorStand,
        ParticleBreakingEgg,
        ParticleDestroyEgg,
        ParticleEvaporateWater,
        ParticleDestroyBlockNoSound,
        ParticleKnockbackRoar,
        ParticleTeleportTrail,
        ParticlePointCloud,
        ParticleExplosion,
        ParticleBlockExplosion,
        ParticleVibrationSignal,
        ParticleDripstoneDrip,
        ParticleFizzEffect,
        ParticleWaxOn,
        ParticleWaxOff,
        ParticleScrape,
        ParticleElectricSpark,
        ParticleTurtleEgg,
        ParticleSculkShriek,
        ParticleSculkCatalystBloom,
        ParticleSculkCharge,
        ParticleSculkChargePop,
        ParticleSonicExplosion,
        StartRaining,
        StartThunderstorm,
        StopRaining,
        StopThunderstorm,
        GlobalPause,
        SimTimeStep,
        SimTimeScale,
        ActivateBlock,
        CauldronExplode,
        CauldronDyeArmor,
        CauldronCleanArmor,
        CauldronFillPotion,
        CauldronTakePotion,
        CauldronFillWater,
        CauldronTakeWater,
        CauldronAddDye,
        CauldronCleanBanner,
        CauldronFlush,
        AgentSpawnEffect,
        CauldronFillLava,
        CauldronTakeLava,
        CauldronFillPowderSnow,
        CauldronTakePowderSnow,
        BlockStartBreak,
        BlockStopBreak,
        BlockUpdateBreak,
        SetData,
        AllPlayersSleeping,
        JumpPrevented,
        SleepingPlayers,
        ParticleBubble,
        ParticleBubbleManual,
        ParticleCritical,
        ParticleBlockForceField,
        ParticleSmoke,
        ParticleExplode,
        ParticleEvaporation,
        ParticleFlame,
        ParticleCandleFlame,
        ParticleLava,
        ParticleLargeSmoke,
        ParticleRedstone,
        ParticleRisingRedDust,
        ParticleItemBreak,
        ParticleSnowballPoof,
        ParticleHugeExplode,
        ParticleHugeExplodeSeed,
        ParticleMobFlame,
        ParticleHeart,
        ParticleTerrain,
        ParticleTownAura,
        ParticlePortal,
        ParticleMobPortal,
        ParticleSplash,
        ParticleSplashManual,
        ParticleWaterWake,
        ParticleDripWater,
        ParticleDripLava,
        ParticleDripHoney,
        ParticleFallingDust,
        ParticleMobSpell,
        ParticleMobSpellAmbient,
        ParticleMobSpellInstantaneous,
        ParticleInk,
        ParticleSlime,
        ParticleRainSplash,
        ParticleVillagerAngry,
        ParticleVillagerHappy,
        ParticleEnchantmentTable,
        ParticleTrackingEmitter,
        ParticleNote,
        ParticleWitchSpell,
        ParticleCarrot,
        ParticleMobAppearance,
        ParticleEndRod,
        ParticleRisingDragonsBreath,
        ParticleSpit,
        ParticleTotem,
        ParticleFood,
        ParticleFireworksStarter,
        ParticleFireworksSpark,
        ParticleFireworksOverlay,
        ParticleBalloonGas,
        ParticleColoredFlame,
        ParticleSparkler,
        ParticleConduit,
        ParticleBubbleColumnUp,
        ParticleBubbleColumnDown,
        ParticleSneeze,
        ParticleShulkerBullet,
        ParticleBleach,
        ParticleDragonDestroyBlock,
        ParticleMyceliumDust,
        ParticleFallingRedDust,
        ParticleCampfireSmoke,
        ParticleTallCampfireSmoke,
        ParticleFallingDragonsBreath,
        ParticleDragonsBreath,
        ParticleBlueFlame,
        ParticleSoul,
        ParticleObsidianTear,
        ParticleStalactiteDripWater,
        ParticleStalactiteDripLava,
        ParticlePortalReverse,
        ParticleSnowflake,
        ParticleSculkSensorRedstone,
        ParticleSporeBlossomShower,
        ParticleSporeBlossomAmbient,
        ParticleWax,
        ParticleElectricSparkOnly,
        ParticleCandleFlameOnly,
        ParticleShriek,
        ParticleSculkSoul,
        ParticleSonicExplosion2;

        fun getId(version: Int) = registry.getLastInt(version, this)

        companion object {
            private val registry = Int2ObjectOpenHashBiMapVersioned<Event>().apply {
                put(SoundClick                   , V1_14_060 to   1000                                          )
                put(SoundClickFail               , V1_14_060 to   1001                                          )
                put(SoundLaunch                  , V1_14_060 to   1002                                          )
                put(SoundDoorOpen                , V1_14_060 to   1003                                          )
                put(SoundFizz                    , V1_14_060 to   1004                                          )
                put(SoundFuse                    , V1_14_060 to   1005                                          )
                put(SoundPlayRecording           , V1_14_060 to   1006                                          )
                put(SoundGhastWarning            , V1_14_060 to   1007                                          )
                put(SoundGhastFireball           , V1_14_060 to   1008                                          )
                put(SoundBlazeFireball           , V1_14_060 to   1009                                          )
                put(SoundZombieDoorBump          , V1_14_060 to   1010                                          )
                put(SoundZombieDoorCrash         , V1_14_060 to   1012                                          )
                put(SoundZombieInfected          , V1_14_060 to   1016                                          )
                put(SoundZombieConverted         , V1_14_060 to   1017                                          )
                put(SoundEndermanTeleport        , V1_14_060 to   1018                                          )
                put(SoundAnvilBroken             , V1_14_060 to   1020                                          )
                put(SoundAnvilUsed               , V1_14_060 to   1021                                          )
                put(SoundAnvilLand               , V1_14_060 to   1022                                          )
                put(SoundInfinityArrowPickup     , V1_14_060 to   1030                                          )
                put(SoundTeleportEnderpearl      , V1_14_060 to   1032                                          )
                put(SoundItemframeItemAdd        , V1_14_060 to   1040                                          )
                put(SoundItemframeBreak          , V1_14_060 to   1041                                          )
                put(SoundItemframePlace          , V1_14_060 to   1042                                          )
                put(SoundItemframeItemRemove     , V1_14_060 to   1043                                          )
                put(SoundItemframeItemRotate     , V1_14_060 to   1044                                          )
                put(SoundCamera                  , V1_16_010 to   1050                                          )
                put(SoundExperienceOrbPickup     , V1_14_060 to   1051                                          )
                put(SoundTotemUsed               , V1_14_060 to   1052                                          )
                put(SoundArmorStandBreak         , V1_14_060 to   1060                                          )
                put(SoundArmorStandHit           , V1_14_060 to   1061                                          )
                put(SoundArmorStandLand          , V1_14_060 to   1062                                          )
                put(SoundArmorStandPlace         , V1_14_060 to   1063                                          )
                put(SoundPointedDripstoneLand    , V1_16_221 to   1064                                          )
                put(SoundDyeUsed                 , V1_16_221 to   1065                                          )
                put(SoundInkSaceUsed             , V1_16_221 to   1066                                          )
                put(ParticleShoot                , V1_14_060 to   2000                                          )
                put(ParticleDestroyBlock         , V1_14_060 to   2001                                          )
                put(ParticlePotionSplash         , V1_14_060 to   2002                                          )
                put(ParticleEyeOfEnderDeath      , V1_14_060 to   2003                                          )
                put(ParticleMobBlockSpawn        , V1_14_060 to   2004                                          )
                put(ParticleCropGrowth           , V1_14_060 to   2005                                          )
                put(ParticleSoundGuardianGhost   , V1_14_060 to   2006                                          )
                put(ParticleDeathSmoke           , V1_14_060 to   2007                                          )
                put(ParticleDenyBlock            , V1_14_060 to   2008                                          )
                put(ParticleGenericSpawn         , V1_14_060 to   2009                                          )
                put(ParticleDragonEgg            , V1_14_060 to   2010                                          )
                put(ParticleCropEaten            , V1_14_060 to   2011                                          )
                put(ParticleCrit                 , V1_14_060 to   2012                                          )
                put(ParticleTeleport             , V1_14_060 to   2013                                          )
                put(ParticleCrackBlock           , V1_14_060 to   2014                                          )
                put(ParticleBubbles              , V1_14_060 to   2015                                          )
                put(ParticleEvaporate            , V1_14_060 to   2016                                          )
                put(ParticleDestroyArmorStand    , V1_14_060 to   2017                                          )
                put(ParticleBreakingEgg          , V1_14_060 to   2018                                          )
                put(ParticleDestroyEgg           , V1_14_060 to   2019                                          )
                put(ParticleEvaporateWater       , V1_14_060 to   2020                                          )
                put(ParticleDestroyBlockNoSound  , V1_14_060 to   2021                                          )
                put(ParticleKnockbackRoar        , V1_14_060 to   2022                                          )
                put(ParticleTeleportTrail        , V1_14_060 to   2023                                          )
                put(ParticlePointCloud           , V1_14_060 to   2024                                          )
                put(ParticleExplosion            , V1_14_060 to   2025                                          )
                put(ParticleBlockExplosion       , V1_14_060 to   2026                                          )
                put(ParticleVibrationSignal      , V1_16_210 to   2027                                          )
                put(ParticleDripstoneDrip        , V1_16_221 to   2028                                          )
                put(ParticleFizzEffect           , V1_16_221 to   2029                                          )
                put(ParticleWaxOn                , V1_16_221 to   2030                                          )
                put(ParticleWaxOff               , V1_16_221 to   2031                                          )
                put(ParticleScrape               , V1_16_221 to   2032                                          )
                put(ParticleElectricSpark        , V1_16_221 to   2033                                          )
                put(ParticleTurtleEgg            , V1_17_034 to   2034                                          )
                put(ParticleSculkShriek          , V1_17_034 to   2035                                          )
                put(ParticleSculkCatalystBloom   , V1_17_041 to   2036                                          )
                put(ParticleSculkCharge          , V1_18_030 to   2037                                          )
                put(ParticleSculkChargePop       , V1_18_030 to   2038                                          )
                put(ParticleSonicExplosion       , V1_19_000 to   2039                                          )
                put(StartRaining                 , V1_14_060 to   3001                                          )
                put(StartThunderstorm            , V1_14_060 to   3002                                          )
                put(StopRaining                  , V1_14_060 to   3003                                          )
                put(StopThunderstorm             , V1_14_060 to   3004                                          )
                put(GlobalPause                  , V1_14_060 to   3005                                          )
                put(SimTimeStep                  , V1_14_060 to   3006                                          )
                put(SimTimeScale                 , V1_14_060 to   3007                                          )
                put(ActivateBlock                , V1_14_060 to   3500                                          )
                put(CauldronExplode              , V1_14_060 to   3501                                          )
                put(CauldronDyeArmor             , V1_14_060 to   3502                                          )
                put(CauldronCleanArmor           , V1_14_060 to   3503                                          )
                put(CauldronFillPotion           , V1_14_060 to   3504                                          )
                put(CauldronTakePotion           , V1_14_060 to   3505                                          )
                put(CauldronFillWater            , V1_14_060 to   3506                                          )
                put(CauldronTakeWater            , V1_14_060 to   3507                                          )
                put(CauldronAddDye               , V1_14_060 to   3508                                          )
                put(CauldronCleanBanner          , V1_14_060 to   3509                                          )
                put(CauldronFlush                , V1_14_060 to   3510                                          )
                put(AgentSpawnEffect             , V1_14_060 to   3511                                          )
                put(CauldronFillLava             , V1_14_060 to   3512                                          )
                put(CauldronTakeLava             , V1_14_060 to   3513                                          )
                put(CauldronFillPowderSnow       , V1_16_210 to   3514                                          )
                put(CauldronTakePowderSnow       , V1_16_210 to   3515                                          )
                put(BlockStartBreak              , V1_16_010 to   3600                                          )
                put(BlockStopBreak               , V1_16_010 to   3601                                          )
                put(BlockUpdateBreak             , V1_16_010 to   3602                                          )
                put(SetData                      , V1_16_010 to   4000                                          )
                put(AllPlayersSleeping           , V1_16_010 to   9800                                          )
                put(SleepingPlayers              , V1_18_002 to   9801                                          )
                put(JumpPrevented                , V1_14_060 to   9810                                          )
                put(ParticleBubble               , V1_14_060 to 0x4001                                          )
                put(ParticleBubbleManual         , V1_14_060 to 0x4002                                          )
                put(ParticleCritical             , V1_14_060 to 0x4003                                          )
                put(ParticleBlockForceField      , V1_14_060 to 0x4004                                          )
                put(ParticleSmoke                , V1_14_060 to 0x4005                                          )
                put(ParticleExplode              , V1_14_060 to 0x4006                                          )
                put(ParticleEvaporation          , V1_14_060 to 0x4007                                          )
                put(ParticleFlame                , V1_14_060 to 0x4008                                          )
                put(ParticleCandleFlame          ,                                           V1_17_011 to 0x4009)
                put(ParticleLava                 , V1_14_060 to 0x4009                     , V1_17_011 to 0x400A)
                put(ParticleLargeSmoke           , V1_14_060 to 0x400A                     , V1_17_011 to 0x400B)
                put(ParticleRedstone             , V1_14_060 to 0x400B                     , V1_17_011 to 0x400C)
                put(ParticleRisingRedDust        , V1_14_060 to 0x400C                     , V1_17_011 to 0x400D)
                put(ParticleItemBreak            , V1_14_060 to 0x400D                     , V1_17_011 to 0x400E)
                put(ParticleSnowballPoof         , V1_14_060 to 0x400E                     , V1_17_011 to 0x400F)
                put(ParticleHugeExplode          , V1_14_060 to 0x400F                     , V1_17_011 to 0x4010)
                put(ParticleHugeExplodeSeed      , V1_14_060 to 0x4010                     , V1_17_011 to 0x4011)
                put(ParticleMobFlame             , V1_14_060 to 0x4011                     , V1_17_011 to 0x4012)
                put(ParticleHeart                , V1_14_060 to 0x4012                     , V1_17_011 to 0x4013)
                put(ParticleTerrain              , V1_14_060 to 0x4013                     , V1_17_011 to 0x4014)
                put(ParticleTownAura             , V1_14_060 to 0x4014                     , V1_17_011 to 0x4015)
                put(ParticlePortal               , V1_14_060 to 0x4015                     , V1_17_011 to 0x4016)
                put(ParticleMobPortal            , V1_14_060 to 0x4016                     , V1_17_011 to 0x4017)
                put(ParticleSplash               , V1_14_060 to 0x4017                     , V1_17_011 to 0x4018)
                put(ParticleSplashManual         , V1_14_060 to 0x4018                     , V1_17_011 to 0x4019)
                put(ParticleWaterWake            , V1_14_060 to 0x4019                     , V1_17_011 to 0x401A)
                put(ParticleDripWater            , V1_14_060 to 0x401A                     , V1_17_011 to 0x401B)
                put(ParticleDripLava             , V1_14_060 to 0x401B                     , V1_17_011 to 0x401C)
                put(ParticleDripHoney            , V1_14_060 to 0x401C                     , V1_17_011 to 0x401D)
                put(ParticleStalactiteDripWater  ,                      V1_16_221 to 0x401D, V1_17_011 to 0x401E)
                put(ParticleStalactiteDripLava   ,                      V1_16_221 to 0x401E, V1_17_011 to 0x401F)
                put(ParticleFallingDust          , V1_14_060 to 0x401D, V1_16_221 to 0x401F, V1_17_011 to 0x4020)
                put(ParticleMobSpell             , V1_14_060 to 0x401E, V1_16_221 to 0x4020, V1_17_011 to 0x4021)
                put(ParticleMobSpellAmbient      , V1_14_060 to 0x401F, V1_16_221 to 0x4021, V1_17_011 to 0x4022)
                put(ParticleMobSpellInstantaneous, V1_14_060 to 0x4020, V1_16_221 to 0x4022, V1_17_011 to 0x4023)
                put(ParticleInk                  , V1_14_060 to 0x4021, V1_16_221 to 0x4023, V1_17_011 to 0x4024)
                put(ParticleSlime                , V1_14_060 to 0x4022, V1_16_221 to 0x4024, V1_17_011 to 0x4025)
                put(ParticleRainSplash           , V1_14_060 to 0x4023, V1_16_221 to 0x4025, V1_17_011 to 0x4026)
                put(ParticleVillagerAngry        , V1_14_060 to 0x4024, V1_16_221 to 0x4026, V1_17_011 to 0x4027)
                put(ParticleVillagerHappy        , V1_14_060 to 0x4025, V1_16_221 to 0x4027, V1_17_011 to 0x4028)
                put(ParticleEnchantmentTable     , V1_14_060 to 0x4026, V1_16_221 to 0x4028, V1_17_011 to 0x4029)
                put(ParticleTrackingEmitter      , V1_14_060 to 0x4027, V1_16_221 to 0x4029, V1_17_011 to 0x402A)
                put(ParticleNote                 , V1_14_060 to 0x4028, V1_16_221 to 0x402A, V1_17_011 to 0x402B)
                put(ParticleWitchSpell           , V1_14_060 to 0x4029, V1_16_221 to 0x402B, V1_17_011 to 0x402C)
                put(ParticleCarrot               , V1_14_060 to 0x402A, V1_16_221 to 0x402C, V1_17_011 to 0x402D)
                put(ParticleMobAppearance        , V1_14_060 to 0x402B, V1_16_221 to 0x402D, V1_17_011 to 0x402E)
                put(ParticleEndRod               , V1_14_060 to 0x402C, V1_16_221 to 0x402E, V1_17_011 to 0x402F)
                put(ParticleRisingDragonsBreath  , V1_14_060 to 0x402D, V1_16_221 to 0x402F, V1_17_011 to 0x4030)
                put(ParticleSpit                 , V1_14_060 to 0x402E, V1_16_221 to 0x4030, V1_17_011 to 0x4031)
                put(ParticleTotem                , V1_14_060 to 0x402F, V1_16_221 to 0x4031, V1_17_011 to 0x4032)
                put(ParticleFood                 , V1_14_060 to 0x4030, V1_16_221 to 0x4032, V1_17_011 to 0x4033)
                put(ParticleFireworksStarter     , V1_14_060 to 0x4031, V1_16_221 to 0x4033, V1_17_011 to 0x4034)
                put(ParticleFireworksSpark       , V1_14_060 to 0x4032, V1_16_221 to 0x4034, V1_17_011 to 0x4035)
                put(ParticleFireworksOverlay     , V1_14_060 to 0x4033, V1_16_221 to 0x4035, V1_17_011 to 0x4036)
                put(ParticleBalloonGas           , V1_14_060 to 0x4034, V1_16_221 to 0x4036, V1_17_011 to 0x4037)
                put(ParticleColoredFlame         , V1_14_060 to 0x4035, V1_16_221 to 0x4037, V1_17_011 to 0x4038)
                put(ParticleSparkler             , V1_14_060 to 0x4036, V1_16_221 to 0x4038, V1_17_011 to 0x4039)
                put(ParticleConduit              , V1_14_060 to 0x4037, V1_16_221 to 0x4039, V1_17_011 to 0x403A)
                put(ParticleBubbleColumnUp       , V1_14_060 to 0x4038, V1_16_221 to 0x403A, V1_17_011 to 0x403B)
                put(ParticleBubbleColumnDown     , V1_14_060 to 0x4039, V1_16_221 to 0x403B, V1_17_011 to 0x403C)
                put(ParticleSneeze               , V1_14_060 to 0x403A, V1_16_221 to 0x403C, V1_17_011 to 0x403D)
                put(ParticleShulkerBullet        , V1_14_060 to 0x403B, V1_16_221 to 0x403D, V1_17_011 to 0x403E)
                put(ParticleBleach               , V1_14_060 to 0x403C, V1_16_221 to 0x403E, V1_17_011 to 0x403F)
                put(ParticleDragonDestroyBlock   , V1_14_060 to 0x403D, V1_16_221 to 0x403F, V1_17_011 to 0x4040)
                put(ParticleMyceliumDust         , V1_14_060 to 0x403E, V1_16_221 to 0x4040, V1_17_011 to 0x4041)
                put(ParticleFallingRedDust       , V1_14_060 to 0x403F, V1_16_221 to 0x4041, V1_17_011 to 0x4042)
                put(ParticleCampfireSmoke        , V1_14_060 to 0x4040, V1_16_221 to 0x4042, V1_17_011 to 0x4043)
                put(ParticleTallCampfireSmoke    , V1_14_060 to 0x4041, V1_16_221 to 0x4043, V1_17_011 to 0x4044)
                put(ParticleFallingDragonsBreath , V1_14_060 to 0x4042, V1_16_221 to 0x4044, V1_17_011 to 0x4045)
                put(ParticleDragonsBreath        , V1_14_060 to 0x4043, V1_16_221 to 0x4045, V1_17_011 to 0x4046)
                put(ParticleBlueFlame            , V1_16_010 to 0x4044, V1_16_221 to 0x4046, V1_17_011 to 0x4047)
                put(ParticleSoul                 , V1_16_010 to 0x4045, V1_16_221 to 0x4047, V1_17_011 to 0x4048)
                put(ParticleObsidianTear         , V1_16_010 to 0x4046, V1_16_221 to 0x4048, V1_17_011 to 0x4049)
                put(ParticlePortalReverse        ,                      V1_17_002 to 0x4049, V1_17_011 to 0x404A)
                put(ParticleSnowflake            ,                      V1_17_002 to 0x404A, V1_17_011 to 0x404B)
                put(ParticleCandleFlameOnly      ,                      V1_17_002 to 0x404B, V1_17_011 to 0x404C)
                put(ParticleSculkSensorRedstone  ,                      V1_17_002 to 0x404C, V1_17_011 to 0x404D)
                put(ParticleSporeBlossomShower   ,                      V1_17_002 to 0x404D, V1_17_011 to 0x404E)
                put(ParticleSporeBlossomAmbient  ,                      V1_17_002 to 0x404E, V1_17_011 to 0x404F)
                put(ParticleWax                  ,                      V1_17_002 to 0x404F, V1_17_011 to 0x4050)
                put(ParticleElectricSparkOnly    ,                      V1_17_002 to 0x4050, V1_17_011 to 0x4051)
                put(ParticleShriek               ,                                           V1_17_034 to 0x4052)
                put(ParticleSculkSoul            ,                                           V1_17_041 to 0x4053)
                put(ParticleSonicExplosion2      ,                                           V1_19_000 to 0x4054)
            }

            operator fun get(version: Int, id: Int) = registry.getLast(version, id)
        }
    }

    override val id get() = 0x19

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeVarInt(event.getId(version))
        buffer.writeFloat3(position)
        buffer.writeVarInt(data)
    }

    override fun handle(handler: PacketHandler) = handler.worldEvent(this)

    override fun toString() = "WorldEventPacket(event=$event, position=$position, data=$data)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = WorldEventPacket(Event[version, buffer.readVarInt()]!!, buffer.readFloat3(), buffer.readVarInt())
    }
}
