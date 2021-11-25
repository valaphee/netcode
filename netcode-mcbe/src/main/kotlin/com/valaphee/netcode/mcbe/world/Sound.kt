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

package com.valaphee.netcode.mcbe.world

/**
 * @author Kevin Ludwig
 */
enum class Sound(
    val key: String
) {
    AmbientBasaltDeltasMood("ambient.basalt_deltas.mood"),
    AmbientCave("ambient.cave"),
    AmbientCrimsonForestMood("ambient.crimson_forest.mood"),
    AmbientNetherWastesMood("ambient.nether_wastes.mood"),
    AmbientSoulsandValleyMood("ambient.soulsand_valley.mood"),
    AmbientWarpedForestMood("ambient.warped_forest.mood"),
    AmbientWeatherLightningImpact("ambient.weather.lightning.impact"),
    AmbientWeatherRain("ambient.weather.rain"),
    AmbientWeatherThunder("ambient.weather.thunder"),
    ArmorEquipNetherite("armor.equip_netherite"),
    ArmorEquipChain("armor.equip_chain"),
    ArmorEquipDiamond("armor.equip_diamond"),
    ArmorEquipGeneric("armor.equip_generic"),
    ArmorEquipGold("armor.equip_gold"),
    ArmorEquipIron("armor.equip_iron"),
    ArmorEquipLeather("armor.equip_leather"),
    BeaconActivate("beacon.activate"),
    BeaconAmbient("beacon.ambient"),
    BeaconDeactivate("beacon.deactivate"),
    BeaconPower("beacon.power"),
    BlockBambooBreak("block.bamboo.break"),
    BlockBambooFall("block.bamboo.fall"),
    BlockBambooHit("block.bamboo.hit"),
    BlockBambooPlace("block.bamboo.place"),
    BlockBambooStep("block.bamboo.step"),
    BlockBambooSaplingBreak("block.bamboo_sapling.break"),
    BlockBambooSaplingPlace("block.bamboo_sapling.place"),
    BlockBarrelClose("block.barrel.close"),
    BlockBarrelOpen("block.barrel.open"),
    BlockBeehiveDrip("block.beehive.drip"),
    BlockBeehiveEnter("block.beehive.enter"),
    BlockBeehiveExit("block.beehive.exit"),
    BlockBeehiveShear("block.beehive.shear"),
    BlockBeehiveWork("block.beehive.work"),
    BlockBellHit("block.bell.hit"),
    BlockBlastfurnaceFireCrackle("block.blastfurnace.fire_crackle"),
    BlockCampfireCrackle("block.campfire.crackle"),
    BlockCartographyTableUse("block.cartography_table.use"),
    BlockChorusflowerDeath("block.chorusflower.death"),
    BlockChorusflowerGrow("block.chorusflower.grow"),
    BlockComposterEmpty("block.composter.empty"),
    BlockComposterFill("block.composter.fill"),
    BlockComposterFillSuccess("block.composter.fill_success"),
    BlockComposterReady("block.composter.ready"),
    BlockEndPortalSpawn("block.end_portal.spawn"),
    BlockEndPortalFrameFill("block.end_portal_frame.fill"),
    BlockFalsePermissions("block.false_permissions"),
    BlockFurnaceLit("block.furnace.lit"),
    BlockGrindstoneUse("block.grindstone.use"),
    BlockItemframeAddItem("block.itemframe.add_item"),
    BlockItemframeBreak("block.itemframe.break"),
    BlockItemframePlace("block.itemframe.place"),
    BlockItemframeRemoveItem("block.itemframe.remove_item"),
    BlockItemframeRotateItem("block.itemframe.rotate_item"),
    BlockLanternBreak("block.lantern.break"),
    BlockLanternFall("block.lantern.fall"),
    BlockLanternHit("block.lantern.hit"),
    BlockLanternPlace("block.lantern.place"),
    BlockLanternStep("block.lantern.step"),
    BlockLoomUse("block.loom.use"),
    BlockScaffoldingBreak("block.scaffolding.break"),
    BlockScaffoldingClimb("block.scaffolding.climb"),
    BlockScaffoldingFall("block.scaffolding.fall"),
    BlockScaffoldingHit("block.scaffolding.hit"),
    BlockScaffoldingPlace("block.scaffolding.place"),
    BlockScaffoldingStep("block.scaffolding.step"),
    BlockSmokerSmoke("block.smoker.smoke"),
    BlockStonecutterUse("block.stonecutter.use"),
    BlockSweetBerryBushBreak("block.sweet_berry_bush.break"),
    BlockSweetBerryBushHurt("block.sweet_berry_bush.hurt"),
    BlockSweetBerryBushPick("block.sweet_berry_bush.pick"),
    BlockSweetBerryBushPlace("block.sweet_berry_bush.place"),
    BlockTurtleEggBreak("block.turtle_egg.break"),
    BlockTurtleEggCrack("block.turtle_egg.crack"),
    BlockTurtleEggDrop("block.turtle_egg.drop"),
    BottleDragonbreath("bottle.dragonbreath"),
    BubbleDown("bubble.down"),
    BubbleDowninside("bubble.downinside"),
    BubblePop("bubble.pop"),
    BubbleUp("bubble.up"),
    BubbleUpinside("bubble.upinside"),
    BucketEmptyFish("bucket.empty_fish"),
    BucketEmptyLava("bucket.empty_lava"),
    BucketEmptyWater("bucket.empty_water"),
    BucketFillFish("bucket.fill_fish"),
    BucketFillLava("bucket.fill_lava"),
    BucketFillWater("bucket.fill_water"),
    CameraTakePicture("camera.take_picture"),
    CauldronAdddye("cauldron.adddye"),
    CauldronCleanarmor("cauldron.cleanarmor"),
    CauldronCleanbanner("cauldron.cleanbanner"),
    CauldronDyearmor("cauldron.dyearmor"),
    CauldronExplode("cauldron.explode"),
    CauldronFillpotion("cauldron.fillpotion"),
    CauldronFillwater("cauldron.fillwater"),
    CauldronTakepotion("cauldron.takepotion"),
    CauldronTakewater("cauldron.takewater"),
    ConduitActivate("conduit.activate"),
    ConduitAmbient("conduit.ambient"),
    ConduitAttack("conduit.attack"),
    ConduitDeactivate("conduit.deactivate"),
    ConduitShort("conduit.short"),
    CrossbowLoadingEnd("crossbow.loading.end"),
    CrossbowLoadingMiddle("crossbow.loading.middle"),
    CrossbowLoadingStart("crossbow.loading.start"),
    CrossbowQuickChargeEnd("crossbow.quick_charge.end"),
    CrossbowQuickChargeMiddle("crossbow.quick_charge.middle"),
    CrossbowQuickChargeStart("crossbow.quick_charge.start"),
    CrossbowShoot("crossbow.shoot"),
    DamageFallbig("damage.fallbig"),
    DamageFallsmall("damage.fallsmall"),
    DigAncientDebris("dig.ancient_debris"),
    DigBasalt("dig.basalt"),
    DigBoneBlock("dig.bone_block"),
    DigChain("dig.chain"),
    DigCloth("dig.cloth"),
    DigCoral("dig.coral"),
    DigFungus("dig.fungus"),
    DigGrass("dig.grass"),
    DigGravel("dig.gravel"),
    DigHoneyBlock("dig.honey_block"),
    DigNetherBrick("dig.nether_brick"),
    DigNetherGoldOre("dig.nether_gold_ore"),
    DigNetherSprouts("dig.nether_sprouts"),
    DigNetherWart("dig.nether_wart"),
    DigNetherite("dig.netherite"),
    DigNetherrack("dig.netherrack"),
    DigNylium("dig.nylium"),
    DigRoots("dig.roots"),
    DigSand("dig.sand"),
    DigShroomlight("dig.shroomlight"),
    DigSnow("dig.snow"),
    DigSoulSand("dig.soul_sand"),
    DigSoulSoil("dig.soul_soil"),
    DigStem("dig.stem"),
    DigStone("dig.stone"),
    DigVines("dig.vines"),
    DigWood("dig.wood"),
    ElytraLoop("elytra.loop"),
    EntityZombieConvertedToDrowned("entity.zombie.converted_to_drowned"),
    FallAncientDebris("fall.ancient_debris"),
    FallBasalt("fall.basalt"),
    FallBoneBlock("fall.bone_block"),
    FallChain("fall.chain"),
    FallCloth("fall.cloth"),
    FallCoral("fall.coral"),
    FallEgg("fall.egg"),
    FallGrass("fall.grass"),
    FallGravel("fall.gravel"),
    FallHoneyBlock("fall.honey_block"),
    FallLadder("fall.ladder"),
    FallNetherBrick("fall.nether_brick"),
    FallNetherGoldOre("fall.nether_gold_ore"),
    FallNetherSprouts("fall.nether_sprouts"),
    FallNetherWart("fall.nether_wart"),
    FallNetherite("fall.netherite"),
    FallNetherrack("fall.netherrack"),
    FallNylium("fall.nylium"),
    FallRoots("fall.roots"),
    FallSand("fall.sand"),
    FallShroomlight("fall.shroomlight"),
    FallSlime("fall.slime"),
    FallSnow("fall.snow"),
    FallSoulSand("fall.soul_sand"),
    FallSoulSoil("fall.soul_soil"),
    FallStem("fall.stem"),
    FallStone("fall.stone"),
    FallVines("fall.vines"),
    FallWood("fall.wood"),
    FireFire("fire.fire"),
    FireIgnite("fire.ignite"),
    FireworkBlast("firework.blast"),
    FireworkLargeBlast("firework.large_blast"),
    FireworkLaunch("firework.launch"),
    FireworkShoot("firework.shoot"),
    FireworkTwinkle("firework.twinkle"),
    GamePlayerAttackNodamage("game.player.attack.nodamage"),
    GamePlayerAttackStrong("game.player.attack.strong"),
    GamePlayerDie("game.player.die"),
    GamePlayerHurt("game.player.hurt"),
    HitAncientDebris("hit.ancient_debris"),
    HitAnvil("hit.anvil"),
    HitBasalt("hit.basalt"),
    HitBoneBlock("hit.bone_block"),
    HitChain("hit.chain"),
    HitCloth("hit.cloth"),
    HitCoral("hit.coral"),
    HitGrass("hit.grass"),
    HitGravel("hit.gravel"),
    HitHoneyBlock("hit.honey_block"),
    HitLadder("hit.ladder"),
    HitNetherBrick("hit.nether_brick"),
    HitNetherGoldOre("hit.nether_gold_ore"),
    HitNetherSprouts("hit.nether_sprouts"),
    HitNetherWart("hit.nether_wart"),
    HitNetherite("hit.netherite"),
    HitNetherrack("hit.netherrack"),
    HitNylium("hit.nylium"),
    HitRoots("hit.roots"),
    HitSand("hit.sand"),
    HitShroomlight("hit.shroomlight"),
    HitSlime("hit.slime"),
    HitSnow("hit.snow"),
    HitSoulSand("hit.soul_sand"),
    HitSoulSoil("hit.soul_soil"),
    HitStem("hit.stem"),
    HitStone("hit.stone"),
    HitVines("hit.vines"),
    HitWood("hit.wood"),
    ItemBookPageTurn("item.book.page_turn"),
    ItemBookPut("item.book.put"),
    ItemShieldBlock("item.shield.block"),
    ItemTridentHit("item.trident.hit"),
    ItemTridentHitGround("item.trident.hit_ground"),
    ItemTridentReturn("item.trident.return"),
    ItemTridentRiptide1("item.trident.riptide_1"),
    ItemTridentRiptide2("item.trident.riptide_2"),
    ItemTridentRiptide3("item.trident.riptide_3"),
    ItemTridentThrow("item.trident.throw"),
    ItemTridentThunder("item.trident.thunder"),
    JumpAncientDebris("jump.ancient_debris"),
    JumpBasalt("jump.basalt"),
    JumpBoneBlock("jump.bone_block"),
    JumpChain("jump.chain"),
    JumpCloth("jump.cloth"),
    JumpCoral("jump.coral"),
    JumpGrass("jump.grass"),
    JumpGravel("jump.gravel"),
    JumpHoneyBlock("jump.honey_block"),
    JumpNetherBrick("jump.nether_brick"),
    JumpNetherGoldOre("jump.nether_gold_ore"),
    JumpNetherSprouts("jump.nether_sprouts"),
    JumpNetherWart("jump.nether_wart"),
    JumpNetherite("jump.netherite"),
    JumpNetherrack("jump.netherrack"),
    JumpNylium("jump.nylium"),
    JumpRoots("jump.roots"),
    JumpSand("jump.sand"),
    JumpShroomlight("jump.shroomlight"),
    JumpSlime("jump.slime"),
    JumpSnow("jump.snow"),
    JumpSoulSand("jump.soul_sand"),
    JumpSoulSoil("jump.soul_soil"),
    JumpStem("jump.stem"),
    JumpStone("jump.stone"),
    JumpVines("jump.vines"),
    JumpWood("jump.wood"),
    LandAncientDebris("land.ancient_debris"),
    LandBasalt("land.basalt"),
    LandBoneBlock("land.bone_block"),
    LandChain("land.chain"),
    LandCloth("land.cloth"),
    LandCoral("land.coral"),
    LandGrass("land.grass"),
    LandGravel("land.gravel"),
    LandHoneyBlock("land.honey_block"),
    LandNetherBrick("land.nether_brick"),
    LandNetherGoldOre("land.nether_gold_ore"),
    LandNetherSprouts("land.nether_sprouts"),
    LandNetherWart("land.nether_wart"),
    LandNetherite("land.netherite"),
    LandNetherrack("land.netherrack"),
    LandNylium("land.nylium"),
    LandRoots("land.roots"),
    LandSand("land.sand"),
    LandShroomlight("land.shroomlight"),
    LandSlime("land.slime"),
    LandSnow("land.snow"),
    LandSoulSand("land.soul_sand"),
    LandSoulSoil("land.soul_soil"),
    LandStem("land.stem"),
    LandStone("land.stone"),
    LandVines("land.vines"),
    LandWood("land.wood"),
    LeashknotBreak("leashknot.break"),
    LeashknotPlace("leashknot.place"),
    LiquidLava("liquid.lava"),
    LiquidLavapop("liquid.lavapop"),
    LiquidWater("liquid.water"),
    LodestoneCompassLinkCompassToLodestone("lodestone_compass.link_compass_to_lodestone"),
    DigLodestone("dig.lodestone"),
    MinecartBase("minecart.base"),
    MinecartInside("minecart.inside"),
    MobAgentSpawn("mob.agent.spawn"),
    MobArmorStandBreak("mob.armor_stand.break"),
    MobArmorStandHit("mob.armor_stand.hit"),
    MobArmorStandLand("mob.armor_stand.land"),
    MobArmorStandPlace("mob.armor_stand.place"),
    MobBatDeath("mob.bat.death"),
    MobBatHurt("mob.bat.hurt"),
    MobBatIdle("mob.bat.idle"),
    MobBatTakeoff("mob.bat.takeoff"),
    MobBeeAggressive("mob.bee.aggressive"),
    MobBeeDeath("mob.bee.death"),
    MobBeeHurt("mob.bee.hurt"),
    MobBeeLoop("mob.bee.loop"),
    MobBeePollinate("mob.bee.pollinate"),
    MobBeeSting("mob.bee.sting"),
    MobBlazeBreathe("mob.blaze.breathe"),
    MobBlazeDeath("mob.blaze.death"),
    MobBlazeHit("mob.blaze.hit"),
    MobBlazeShoot("mob.blaze.shoot"),
    MobCatBeg("mob.cat.beg"),
    MobCatEat("mob.cat.eat"),
    MobCatHiss("mob.cat.hiss"),
    MobCatHit("mob.cat.hit"),
    MobCatMeow("mob.cat.meow"),
    MobCatPurr("mob.cat.purr"),
    MobCatPurreow("mob.cat.purreow"),
    MobCatStraymeow("mob.cat.straymeow"),
    MobChickenHurt("mob.chicken.hurt"),
    MobChickenPlop("mob.chicken.plop"),
    MobChickenSay("mob.chicken.say"),
    MobChickenStep("mob.chicken.step"),
    MobCowHurt("mob.cow.hurt"),
    MobCowMilk("mob.cow.milk"),
    MobCowSay("mob.cow.say"),
    MobCowStep("mob.cow.step"),
    MobCreeperDeath("mob.creeper.death"),
    MobCreeperSay("mob.creeper.say"),
    MobDolphinAttack("mob.dolphin.attack"),
    MobDolphinBlowhole("mob.dolphin.blowhole"),
    MobDolphinDeath("mob.dolphin.death"),
    MobDolphinEat("mob.dolphin.eat"),
    MobDolphinHurt("mob.dolphin.hurt"),
    MobDolphinIdle("mob.dolphin.idle"),
    MobDolphinIdleWater("mob.dolphin.idle_water"),
    MobDolphinJump("mob.dolphin.jump"),
    MobDolphinPlay("mob.dolphin.play"),
    MobDolphinSplash("mob.dolphin.splash"),
    MobDolphinSwim("mob.dolphin.swim"),
    MobDrownedDeath("mob.drowned.death"),
    MobDrownedDeathWater("mob.drowned.death_water"),
    MobDrownedHurt("mob.drowned.hurt"),
    MobDrownedHurtWater("mob.drowned.hurt_water"),
    MobDrownedSay("mob.drowned.say"),
    MobDrownedSayWater("mob.drowned.say_water"),
    MobDrownedShoot("mob.drowned.shoot"),
    MobDrownedStep("mob.drowned.step"),
    MobDrownedSwim("mob.drowned.swim"),
    MobElderguardianCurse("mob.elderguardian.curse"),
    MobElderguardianDeath("mob.elderguardian.death"),
    MobElderguardianHit("mob.elderguardian.hit"),
    MobElderguardianIdle("mob.elderguardian.idle"),
    MobEnderdragonDeath("mob.enderdragon.death"),
    MobEnderdragonFlap("mob.enderdragon.flap"),
    MobEnderdragonGrowl("mob.enderdragon.growl"),
    MobEnderdragonHit("mob.enderdragon.hit"),
    MobEndermenDeath("mob.endermen.death"),
    MobEndermenHit("mob.endermen.hit"),
    MobEndermenIdle("mob.endermen.idle"),
    MobEndermenPortal("mob.endermen.portal"),
    MobEndermenScream("mob.endermen.scream"),
    MobEndermenStare("mob.endermen.stare"),
    MobEndermiteHit("mob.endermite.hit"),
    MobEndermiteKill("mob.endermite.kill"),
    MobEndermiteSay("mob.endermite.say"),
    MobEndermiteStep("mob.endermite.step"),
    MobEvocationFangsAttack("mob.evocation_fangs.attack"),
    MobEvocationIllagerAmbient("mob.evocation_illager.ambient"),
    MobEvocationIllagerCastSpell("mob.evocation_illager.cast_spell"),
    MobEvocationIllagerCelebrate("mob.evocation_illager.celebrate"),
    MobEvocationIllagerDeath("mob.evocation_illager.death"),
    MobEvocationIllagerHurt("mob.evocation_illager.hurt"),
    MobEvocationIllagerPrepareAttack("mob.evocation_illager.prepare_attack"),
    MobEvocationIllagerPrepareSummon("mob.evocation_illager.prepare_summon"),
    MobEvocationIllagerPrepareWololo("mob.evocation_illager.prepare_wololo"),
    MobFishFlop("mob.fish.flop"),
    MobFishHurt("mob.fish.hurt"),
    MobFishStep("mob.fish.step"),
    MobFoxAggro("mob.fox.aggro"),
    MobFoxAmbient("mob.fox.ambient"),
    MobFoxBite("mob.fox.bite"),
    MobFoxDeath("mob.fox.death"),
    MobFoxEat("mob.fox.eat"),
    MobFoxHurt("mob.fox.hurt"),
    MobFoxScreech("mob.fox.screech"),
    MobFoxSleep("mob.fox.sleep"),
    MobFoxSniff("mob.fox.sniff"),
    MobFoxSpit("mob.fox.spit"),
    MobGhastAffectionateScream("mob.ghast.affectionate_scream"),
    MobGhastCharge("mob.ghast.charge"),
    MobGhastDeath("mob.ghast.death"),
    MobGhastFireball("mob.ghast.fireball"),
    MobGhastMoan("mob.ghast.moan"),
    MobGhastScream("mob.ghast.scream"),
    MobGuardianAmbient("mob.guardian.ambient"),
    MobGuardianAttackLoop("mob.guardian.attack_loop"),
    MobGuardianDeath("mob.guardian.death"),
    MobGuardianFlop("mob.guardian.flop"),
    MobGuardianHit("mob.guardian.hit"),
    MobGuardianLandDeath("mob.guardian.land_death"),
    MobGuardianLandHit("mob.guardian.land_hit"),
    MobGuardianLandIdle("mob.guardian.land_idle"),
    MobHoglinAmbient("mob.hoglin.ambient"),
    MobHoglinAngry("mob.hoglin.angry"),
    MobHoglinAttack("mob.hoglin.attack"),
    MobHoglinDeath("mob.hoglin.death"),
    MobHoglinHowl("mob.hoglin.howl"),
    MobHoglinHurt("mob.hoglin.hurt"),
    MobHoglinRetreat("mob.hoglin.retreat"),
    MobHoglinStep("mob.hoglin.step"),
    MobHorseAngry("mob.horse.angry"),
    MobHorseArmor("mob.horse.armor"),
    MobHorseBreathe("mob.horse.breathe"),
    MobHorseDeath("mob.horse.death"),
    MobHorseDonkeyAngry("mob.horse.donkey.angry"),
    MobHorseDonkeyDeath("mob.horse.donkey.death"),
    MobHorseDonkeyHit("mob.horse.donkey.hit"),
    MobHorseDonkeyIdle("mob.horse.donkey.idle"),
    MobHorseEat("mob.horse.eat"),
    MobHorseGallop("mob.horse.gallop"),
    MobHorseHit("mob.horse.hit"),
    MobHorseIdle("mob.horse.idle"),
    MobHorseJump("mob.horse.jump"),
    MobHorseLand("mob.horse.land"),
    MobHorseLeather("mob.horse.leather"),
    MobHorseSkeletonDeath("mob.horse.skeleton.death"),
    MobHorseSkeletonHit("mob.horse.skeleton.hit"),
    MobHorseSkeletonIdle("mob.horse.skeleton.idle"),
    MobHorseSoft("mob.horse.soft"),
    MobHorseWood("mob.horse.wood"),
    MobHorseZombieDeath("mob.horse.zombie.death"),
    MobHorseZombieHit("mob.horse.zombie.hit"),
    MobHorseZombieIdle("mob.horse.zombie.idle"),
    MobHuskAmbient("mob.husk.ambient"),
    MobHuskDeath("mob.husk.death"),
    MobHuskHurt("mob.husk.hurt"),
    MobHuskStep("mob.husk.step"),
    MobIrongolemDeath("mob.irongolem.death"),
    MobIrongolemHit("mob.irongolem.hit"),
    MobIrongolemThrow("mob.irongolem.throw"),
    MobIrongolemWalk("mob.irongolem.walk"),
    MobLlamaAngry("mob.llama.angry"),
    MobLlamaDeath("mob.llama.death"),
    MobLlamaEat("mob.llama.eat"),
    MobLlamaHurt("mob.llama.hurt"),
    MobLlamaIdle("mob.llama.idle"),
    MobLlamaSpit("mob.llama.spit"),
    MobLlamaStep("mob.llama.step"),
    MobLlamaSwag("mob.llama.swag"),
    MobMagmacubeBig("mob.magmacube.big"),
    MobMagmacubeJump("mob.magmacube.jump"),
    MobMagmacubeSmall("mob.magmacube.small"),
    MobMooshroomConvert("mob.mooshroom.convert"),
    MobMooshroomEat("mob.mooshroom.eat"),
    MobMooshroomSuspiciousMilk("mob.mooshroom.suspicious_milk"),
    MobOcelotDeath("mob.ocelot.death"),
    MobOcelotIdle("mob.ocelot.idle"),
    MobPandaBite("mob.panda.bite"),
    MobPandaCantBreed("mob.panda.cant_breed"),
    MobPandaDeath("mob.panda.death"),
    MobPandaEat("mob.panda.eat"),
    MobPandaHurt("mob.panda.hurt"),
    MobPandaIdle("mob.panda.idle"),
    MobPandaIdleAggressive("mob.panda.idle.aggressive"),
    MobPandaIdleWorried("mob.panda.idle.worried"),
    MobPandaPresneeze("mob.panda.presneeze"),
    MobPandaSneeze("mob.panda.sneeze"),
    MobPandaStep("mob.panda.step"),
    MobPandaBabyIdle("mob.panda_baby.idle"),
    MobParrotDeath("mob.parrot.death"),
    MobParrotEat("mob.parrot.eat"),
    MobParrotFly("mob.parrot.fly"),
    MobParrotHurt("mob.parrot.hurt"),
    MobParrotIdle("mob.parrot.idle"),
    MobParrotStep("mob.parrot.step"),
    MobPhantomBite("mob.phantom.bite"),
    MobPhantomDeath("mob.phantom.death"),
    MobPhantomHurt("mob.phantom.hurt"),
    MobPhantomIdle("mob.phantom.idle"),
    MobPhantomSwoop("mob.phantom.swoop"),
    MobPigBoost("mob.pig.boost"),
    MobPigDeath("mob.pig.death"),
    MobPigSay("mob.pig.say"),
    MobPigStep("mob.pig.step"),
    MobPiglinAdmiringItem("mob.piglin.admiring_item"),
    MobPiglinAmbient("mob.piglin.ambient"),
    MobPiglinAngry("mob.piglin.angry"),
    MobPiglinCelebrate("mob.piglin.celebrate"),
    MobPiglinConvertedToZombified("mob.piglin.converted_to_zombified"),
    MobPiglinDeath("mob.piglin.death"),
    MobPiglinHurt("mob.piglin.hurt"),
    MobPiglinJealous("mob.piglin.jealous"),
    MobPiglinRetreat("mob.piglin.retreat"),
    MobPiglinStep("mob.piglin.step"),
    MobPiglinBruteAmbient("mob.piglin_brute.ambient"),
    MobPiglinBruteAngry("mob.piglin_brute.angry"),
    MobPiglinBruteConvertedToZombified("mob.piglin_brute.converted_to_zombified"),
    MobPiglinBruteHurt("mob.piglin_brute.hurt"),
    MobPiglinBruteDeath("mob.piglin_brute.death"),
    MobPiglinBruteStep("mob.piglin_brute.step"),
    MobPillagerCelebrate("mob.pillager.celebrate"),
    MobPillagerDeath("mob.pillager.death"),
    MobPillagerHurt("mob.pillager.hurt"),
    MobPillagerIdle("mob.pillager.idle"),
    MobPolarbearDeath("mob.polarbear.death"),
    MobPolarbearHurt("mob.polarbear.hurt"),
    MobPolarbearIdle("mob.polarbear.idle"),
    MobPolarbearStep("mob.polarbear.step"),
    MobPolarbearWarning("mob.polarbear.warning"),
    MobPolarbearBabyIdle("mob.polarbear_baby.idle"),
    MobRabbitDeath("mob.rabbit.death"),
    MobRabbitHop("mob.rabbit.hop"),
    MobRabbitHurt("mob.rabbit.hurt"),
    MobRabbitIdle("mob.rabbit.idle"),
    MobRavagerAmbient("mob.ravager.ambient"),
    MobRavagerBite("mob.ravager.bite"),
    MobRavagerCelebrate("mob.ravager.celebrate"),
    MobRavagerDeath("mob.ravager.death"),
    MobRavagerHurt("mob.ravager.hurt"),
    MobRavagerRoar("mob.ravager.roar"),
    MobRavagerStep("mob.ravager.step"),
    MobRavagerStun("mob.ravager.stun"),
    MobSheepSay("mob.sheep.say"),
    MobSheepShear("mob.sheep.shear"),
    MobSheepStep("mob.sheep.step"),
    MobShulkerAmbient("mob.shulker.ambient"),
    MobShulkerBulletHit("mob.shulker.bullet.hit"),
    MobShulkerClose("mob.shulker.close"),
    MobShulkerCloseHurt("mob.shulker.close.hurt"),
    MobShulkerDeath("mob.shulker.death"),
    MobShulkerHurt("mob.shulker.hurt"),
    MobShulkerOpen("mob.shulker.open"),
    MobShulkerShoot("mob.shulker.shoot"),
    MobShulkerTeleport("mob.shulker.teleport"),
    MobSilverfishHit("mob.silverfish.hit"),
    MobSilverfishKill("mob.silverfish.kill"),
    MobSilverfishSay("mob.silverfish.say"),
    MobSilverfishStep("mob.silverfish.step"),
    MobSkeletonDeath("mob.skeleton.death"),
    MobSkeletonHurt("mob.skeleton.hurt"),
    MobSkeletonSay("mob.skeleton.say"),
    MobSkeletonStep("mob.skeleton.step"),
    MobSlimeAttack("mob.slime.attack"),
    MobSlimeBig("mob.slime.big"),
    MobSlimeDeath("mob.slime.death"),
    MobSlimeHurt("mob.slime.hurt"),
    MobSlimeJump("mob.slime.jump"),
    MobSlimeSmall("mob.slime.small"),
    MobSlimeSquish("mob.slime.squish"),
    MobSnowgolemDeath("mob.snowgolem.death"),
    MobSnowgolemHurt("mob.snowgolem.hurt"),
    MobSnowgolemShoot("mob.snowgolem.shoot"),
    MobSpiderDeath("mob.spider.death"),
    MobSpiderSay("mob.spider.say"),
    MobSpiderStep("mob.spider.step"),
    MobSquidAmbient("mob.squid.ambient"),
    MobSquidDeath("mob.squid.death"),
    MobSquidHurt("mob.squid.hurt"),
    MobStrayAmbient("mob.stray.ambient"),
    MobStrayDeath("mob.stray.death"),
    MobStrayHurt("mob.stray.hurt"),
    MobStrayStep("mob.stray.step"),
    MobStriderDeath("mob.strider.death"),
    MobStriderEat("mob.strider.eat"),
    MobStriderHurt("mob.strider.hurt"),
    MobStriderIdle("mob.strider.idle"),
    MobStriderPanic("mob.strider.panic"),
    MobStriderStep("mob.strider.step"),
    MobStriderStepLava("mob.strider.step_lava"),
    MobStriderTempt("mob.strider.tempt"),
    MobTurtleAmbient("mob.turtle.ambient"),
    MobTurtleDeath("mob.turtle.death"),
    MobTurtleHurt("mob.turtle.hurt"),
    MobTurtleStep("mob.turtle.step"),
    MobTurtleSwim("mob.turtle.swim"),
    MobTurtleBabyBorn("mob.turtle_baby.born"),
    MobTurtleBabyDeath("mob.turtle_baby.death"),
    MobTurtleBabyHurt("mob.turtle_baby.hurt"),
    MobTurtleBabyStep("mob.turtle_baby.step"),
    MobVexAmbient("mob.vex.ambient"),
    MobVexCharge("mob.vex.charge"),
    MobVexDeath("mob.vex.death"),
    MobVexHurt("mob.vex.hurt"),
    MobVillagerDeath("mob.villager.death"),
    MobVillagerHaggle("mob.villager.haggle"),
    MobVillagerHit("mob.villager.hit"),
    MobVillagerIdle("mob.villager.idle"),
    MobVillagerNo("mob.villager.no"),
    MobVillagerYes("mob.villager.yes"),
    MobVindicatorCelebrate("mob.vindicator.celebrate"),
    MobVindicatorDeath("mob.vindicator.death"),
    MobVindicatorHurt("mob.vindicator.hurt"),
    MobVindicatorIdle("mob.vindicator.idle"),
    MobWanderingtraderDeath("mob.wanderingtrader.death"),
    MobWanderingtraderDisappeared("mob.wanderingtrader.disappeared"),
    MobWanderingtraderDrinkMilk("mob.wanderingtrader.drink_milk"),
    MobWanderingtraderDrinkPotion("mob.wanderingtrader.drink_potion"),
    MobWanderingtraderHaggle("mob.wanderingtrader.haggle"),
    MobWanderingtraderHurt("mob.wanderingtrader.hurt"),
    MobWanderingtraderIdle("mob.wanderingtrader.idle"),
    MobWanderingtraderNo("mob.wanderingtrader.no"),
    MobWanderingtraderReappeared("mob.wanderingtrader.reappeared"),
    MobWanderingtraderYes("mob.wanderingtrader.yes"),
    MobWitchAmbient("mob.witch.ambient"),
    MobWitchCelebrate("mob.witch.celebrate"),
    MobWitchDeath("mob.witch.death"),
    MobWitchDrink("mob.witch.drink"),
    MobWitchHurt("mob.witch.hurt"),
    MobWitchThrow("mob.witch.throw"),
    MobWitherAmbient("mob.wither.ambient"),
    MobWitherBreakBlock("mob.wither.break_block"),
    MobWitherDeath("mob.wither.death"),
    MobWitherHurt("mob.wither.hurt"),
    MobWitherShoot("mob.wither.shoot"),
    MobWitherSpawn("mob.wither.spawn"),
    MobWolfBark("mob.wolf.bark"),
    MobWolfDeath("mob.wolf.death"),
    MobWolfGrowl("mob.wolf.growl"),
    MobWolfHurt("mob.wolf.hurt"),
    MobWolfPanting("mob.wolf.panting"),
    MobWolfShake("mob.wolf.shake"),
    MobWolfStep("mob.wolf.step"),
    MobWolfWhine("mob.wolf.whine"),
    MobZoglinAngry("mob.zoglin.angry"),
    MobZoglinDeath("mob.zoglin.death"),
    MobZoglinIdle("mob.zoglin.idle"),
    MobZoglinHurt("mob.zoglin.hurt"),
    MobZoglinStep("mob.zoglin.step"),
    MobZoglinAttack("mob.zoglin.attack"),
    MobZombieDeath("mob.zombie.death"),
    MobZombieHurt("mob.zombie.hurt"),
    MobZombieRemedy("mob.zombie.remedy"),
    MobZombieSay("mob.zombie.say"),
    MobZombieStep("mob.zombie.step"),
    MobZombieUnfect("mob.zombie.unfect"),
    MobZombieWood("mob.zombie.wood"),
    MobZombieWoodbreak("mob.zombie.woodbreak"),
    MobZombieVillagerDeath("mob.zombie_villager.death"),
    MobZombieVillagerHurt("mob.zombie_villager.hurt"),
    MobZombieVillagerSay("mob.zombie_villager.say"),
    MobZombiepigZpig("mob.zombiepig.zpig"),
    MobZombiepigZpigangry("mob.zombiepig.zpigangry"),
    MobZombiepigZpigdeath("mob.zombiepig.zpigdeath"),
    MobZombiepigZpighurt("mob.zombiepig.zpighurt"),
    MusicGame("music.game"),
    MusicGameCreative("music.game.creative"),
    MusicGameCredits("music.game.credits"),
    MusicGameCrimsonForest("music.game.crimson_forest"),
    MusicGameEnd("music.game.end"),
    MusicGameEndboss("music.game.endboss"),
    MusicGameNether("music.game.nether"),
    MusicGameNetherWastes("music.game.nether_wastes"),
    MusicGameSoulsandValley("music.game.soulsand_valley"),
    MusicGameWater("music.game.water"),
    MusicMenu("music.menu"),
    NoteBanjo("note.banjo"),
    NoteBass("note.bass"),
    NoteBassattack("note.bassattack"),
    NoteBd("note.bd"),
    NoteBell("note.bell"),
    NoteBit("note.bit"),
    NoteChime("note.chime"),
    NoteCowBell("note.cow_bell"),
    NoteDidgeridoo("note.didgeridoo"),
    NoteFlute("note.flute"),
    NoteGuitar("note.guitar"),
    NoteHarp("note.harp"),
    NoteHat("note.hat"),
    NoteIronXylophone("note.iron_xylophone"),
    NotePling("note.pling"),
    NoteSnare("note.snare"),
    NoteXylophone("note.xylophone"),
    ParticleSoulEscape("particle.soul_escape"),
    PortalPortal("portal.portal"),
    PortalTravel("portal.travel"),
    PortalTrigger("portal.trigger"),
    RaidHorn("raid.horn"),
    RandomAnvilBreak("random.anvil_break"),
    RandomAnvilLand("random.anvil_land"),
    RandomAnvilUse("random.anvil_use"),
    RandomBow("random.bow"),
    RandomBowhit("random.bowhit"),
    RandomBreak("random.break"),
    RandomBurp("random.burp"),
    RandomChestclosed("random.chestclosed"),
    RandomChestopen("random.chestopen"),
    RandomClick("random.click"),
    RandomDoorClose("random.door_close"),
    RandomDoorOpen("random.door_open"),
    RandomDrink("random.drink"),
    RandomDrinkHoney("random.drink_honey"),
    RandomEat("random.eat"),
    RandomEnderchestclosed("random.enderchestclosed"),
    RandomEnderchestopen("random.enderchestopen"),
    RandomExplode("random.explode"),
    RandomFizz("random.fizz"),
    RandomFuse("random.fuse"),
    RandomGlass("random.glass"),
    RandomHurt("random.hurt"),
    RandomLevelup("random.levelup"),
    RandomOrb("random.orb"),
    RandomPop("random.pop"),
    RandomPop2("random.pop2"),
    RandomPotionBrewed("random.potion.brewed"),
    RandomScreenshot("random.screenshot"),
    RandomShulkerboxclosed("random.shulkerboxclosed"),
    RandomShulkerboxopen("random.shulkerboxopen"),
    RandomSplash("random.splash"),
    RandomSwim("random.swim"),
    RandomToast("random.toast"),
    RandomTotem("random.totem"),
    Record11("record.11"),
    Record13("record.13"),
    RecordBlocks("record.blocks"),
    RecordCat("record.cat"),
    RecordChirp("record.chirp"),
    RecordFar("record.far"),
    RecordMall("record.mall"),
    RecordMellohi("record.mellohi"),
    RecordPigstep("record.pigstep"),
    RecordStal("record.stal"),
    RecordStrad("record.strad"),
    RecordWait("record.wait"),
    RecordWard("record.ward"),
    RespawnAnchorAmbient("respawn_anchor.ambient"),
    RespawnAnchorCharge("respawn_anchor.charge"),
    RespawnAnchorDeplete("respawn_anchor.deplete"),
    RespawnAnchorSetSpawn("respawn_anchor.set_spawn"),
    SmithingTableUse("smithing_table.use"),
    StepAncientDebris("step.ancient_debris"),
    StepBasalt("step.basalt"),
    StepBoneBlock("step.bone_block"),
    StepCloth("step.cloth"),
    StepChain("step.chain"),
    StepCoral("step.coral"),
    StepGrass("step.grass"),
    StepGravel("step.gravel"),
    StepHoneyBlock("step.honey_block"),
    StepLadder("step.ladder"),
    StepNetherBrick("step.nether_brick"),
    StepNetherGoldOre("step.nether_gold_ore"),
    StepNetherSprouts("step.nether_sprouts"),
    StepNetherWart("step.nether_wart"),
    StepNetherite("step.netherite"),
    StepNetherrack("step.netherrack"),
    StepNylium("step.nylium"),
    StepRoots("step.roots"),
    StepSand("step.sand"),
    StepShroomlight("step.shroomlight"),
    StepSlime("step.slime"),
    StepSnow("step.snow"),
    StepSoulSand("step.soul_sand"),
    StepSoulSoil("step.soul_soil"),
    StepStem("step.stem"),
    StepStone("step.stone"),
    StepVines("step.vines"),
    StepWood("step.wood"),
    TilePistonIn("tile.piston.in"),
    TilePistonOut("tile.piston.out"),
    UiCartographyTableTakeResult("ui.cartography_table.take_result"),
    UiLoomSelectPattern("ui.loom.select_pattern"),
    UiLoomTakeResult("ui.loom.take_result"),
    UiStonecutterTakeResult("ui.stonecutter.take_result"),
    UseAncientDebris("use.ancient_debris"),
    UseBasalt("use.basalt"),
    UseBoneBlock("use.bone_block"),
    UseChain("use.chain"),
    UseCloth("use.cloth"),
    UseCoral("use.coral"),
    UseGrass("use.grass"),
    UseGravel("use.gravel"),
    UseHoneyBlock("use.honey_block"),
    UseLadder("use.ladder"),
    UseNetherBrick("use.nether_brick"),
    UseNetherGoldOre("use.nether_gold_ore"),
    UseNetherSprouts("use.nether_sprouts"),
    UseNetherWart("use.nether_wart"),
    UseNetherite("use.netherite"),
    UseNetherrack("use.netherrack"),
    UseNylium("use.nylium"),
    UseRoots("use.roots"),
    UseSand("use.sand"),
    UseShroomlight("use.shroomlight"),
    UseSlime("use.slime"),
    UseSnow("use.snow"),
    UseSoulSand("use.soul_sand"),
    UseSoulSoil("use.soul_soil"),
    UseStem("use.stem"),
    UseStone("use.stone"),
    UseVines("use.vines"),
    UseWood("use.wood"),
    VrShutterturn("vr.stutterturn");

    companion object {
        private val byKey = values().associateBy { it.key }

        fun byKeyOrNull(key: String) = byKey[key]
    }
}
