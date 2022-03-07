package com.valaphee.netcode.mcje.network

import com.valaphee.netcode.mcje.network.packet.play.ClientAbilitiesPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientAbilitiesPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientActionPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientActionPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientAdvancementTabPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientAdvancementTabPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientBeaconUpdatePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientBeaconUpdatePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientBlockPlacePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientBlockPlacePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientBlockQueryPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientBlockQueryPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientBookEditPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientBookEditPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientCommandBlockMinecartUpdatePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientCommandBlockMinecartUpdatePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientCommandBlockUpdatePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientCommandBlockUpdatePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientCommandSuggestPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientCommandSuggestPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientCraftPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientCraftPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientCreativeInventorySlotPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientCreativeInventorySlotPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientCustomPayloadPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientCustomPayloadPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientDifficultyPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientDifficultyPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientEntityQueryPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientEntityQueryPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientHotbarPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientHotbarPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientItemNamePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientItemNamePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientItemPickPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientItemPickPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientItemUseOnEntityPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientItemUseOnEntityPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientItemUsePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientItemUsePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientJigsawBlockUpdatePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientJigsawBlockUpdatePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientKeepAlivePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientKeepAlivePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientLocationPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientLocationPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientPlayerActionPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientPlayerActionPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientPositionPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientPositionPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientPositionRotationPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientPositionRotationPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientResourcePackStatusPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientResourcePackStatusPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientRotationPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientRotationPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientSettingsPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientSettingsPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientSignUpdatePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientSignUpdatePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientSpectatePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientSpectatePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientStatusPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientStatusPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientSteerBoatPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientSteerBoatPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientSteerPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientSteerPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientStructureBlockUpdatePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientStructureBlockUpdatePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientSwingArmPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientSwingArmPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientTeleportConfirmPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientTeleportConfirmPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientTextPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientTextPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientTradePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientTradePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientVehicleLocationPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientVehicleLocationPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientWindowClickButtonPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientWindowClickButtonPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientWindowClickPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientWindowClickPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientWindowClosePacket
import com.valaphee.netcode.mcje.network.packet.play.ClientWindowClosePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ClientWindowConfirmPacket
import com.valaphee.netcode.mcje.network.packet.play.ClientWindowConfirmPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerAbilitiesPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerAbilitiesPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerActionResponsePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerActionResponsePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerBlockBreakAnimationPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerBlockBreakAnimationPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerBlockEntityPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerBlockEntityPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerBlockEventPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerBlockEventPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerBlockUpdatePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerBlockUpdatePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerBlockUpdatesPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerBlockUpdatesPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerBookOpenPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerBookOpenPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerBorderPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerBorderPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerBossBarPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerBossBarPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerCameraPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerCameraPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerChunkPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerChunkPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerChunkPublishPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerChunkPublishPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerChunkUnloadPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerChunkUnloadPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerCraftPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerCraftPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerCustomPayloadPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerCustomPayloadPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerDifficultyPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerDifficultyPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerDisconnectPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerDisconnectPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerEntitiesRemovePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntitiesRemovePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityAddPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityAddPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityAnimationPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityAnimationPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityAttachPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityAttachPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityAttributesPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityAttributesPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityEffectApplyPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityEffectApplyPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityEffectRevokePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityEffectRevokePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityEquipmentPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityEquipmentPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityEventPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityEventPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityHeadRotationPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityHeadRotationPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityLocationPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityLocationPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityMetadataPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityMetadataPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityMovePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityMovePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityMoveRotatePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityMoveRotatePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityPassengersPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityPassengersPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityRotatePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityRotatePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerEntitySoundPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntitySoundPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityTeleportPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityTeleportPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityVelocityPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerEntityVelocityPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerExperienceOrbAddPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerExperienceOrbAddPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerExperiencePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerExperiencePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerExplosionPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerExplosionPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerGameStatePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerGameStatePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerHealthHungerSaturationPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerHealthHungerSaturationPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerHorseWindowOpenPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerHorseWindowOpenPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerHotbarPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerHotbarPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerInventoryContentPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerInventoryContentPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerInventorySlotPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerInventorySlotPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerItemCooldownPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerItemCooldownPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerKeepAlivePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerKeepAlivePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerLocationPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerLocationPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerLookAtPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerLookAtPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerNamedSoundPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerNamedSoundPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerObjectAddPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerObjectAddPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerObjectivePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerObjectivePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerPlayerAddPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerPlayerAddPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerPlayerCombatEventPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerPlayerCombatEventPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerPlayerListHeaderFooterPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerPlayerListHeaderFooterPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerPlayerListPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerPlayerListPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerQueryPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerQueryPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerRecipeBookPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerRecipeBookPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerResourcePackPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerResourcePackPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerRespawnPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerRespawnPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerScorePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerScorePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerScoreboardDisplayPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerScoreboardDisplayPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerSignUpdatePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerSignUpdatePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerSoundPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerSoundPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerSoundStopPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerSoundStopPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerSpawnPositionPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerSpawnPositionPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerStackTakePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerStackTakePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerCommandSuggestPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerCommandSuggestPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerTeamPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerTeamPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerTextPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerTextPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerTimePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerTimePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerTitlePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerTitlePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerTradePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerTradePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerVehicleLocationPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerVehicleLocationPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerViewDistancePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerViewDistancePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerWindowClosePacket
import com.valaphee.netcode.mcje.network.packet.play.ServerWindowClosePacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerWindowConfirmPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerWindowConfirmPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerWindowOpenPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerWindowOpenPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerWindowPropertyPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerWindowPropertyPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerWorldEventPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerWorldEventPacketReader
import com.valaphee.netcode.mcje.network.packet.play.ServerWorldPacket
import com.valaphee.netcode.mcje.network.packet.play.ServerWorldPacketReader
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.Object2IntMap
import it.unimi.dsi.fastutil.objects.Object2IntMaps
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import kotlin.reflect.KClass

/**
 * @author Kevin Ludwig
 */
class Protocol(
    val packets: Object2IntMap<KClass<out Packet<out PacketHandler>>>,
    val readers: Int2ObjectMap<PacketReader>
) {
    class Builder {
        private val packetsAndReadersByVersion = Int2ObjectOpenHashMap<Pair<Object2IntMap<KClass<out Packet<out PacketHandler>>>, Int2ObjectMap<PacketReader>>>()

        fun register(`class`: KClass<out Packet<out PacketHandler>>, reader: PacketReader, vararg idByVersion: Pair<Int, Int>) = apply {
            idByVersion.forEach {
                val packetsAndReaders = packetsAndReadersByVersion.getOrPut(it.first) { Pair(Object2IntOpenHashMap(), Int2ObjectOpenHashMap()) }
                packetsAndReaders.first[`class`] = it.second
                packetsAndReaders.second[it.second] = reader
            }
        }

        fun build(): Int2ObjectMap<Protocol> = Int2ObjectMaps.unmodifiable(Int2ObjectOpenHashMap<Protocol>().apply { packetsAndReadersByVersion.forEach { this[it.key] = Protocol(Object2IntMaps.unmodifiable(it.value.first), Int2ObjectMaps.unmodifiable(it.value.second)) } })
    }
}

/**
 * @author Kevin Ludwig
 */
enum class Protocols(
    val client: Int2ObjectMap<Protocol>,
    val server: Int2ObjectMap<Protocol>
) {
    Handshake(Int2ObjectMaps.emptyMap(), Int2ObjectMaps.emptyMap()),
    Status(Int2ObjectMaps.emptyMap(), Int2ObjectMaps.emptyMap()),
    Login(Int2ObjectMaps.emptyMap(), Int2ObjectMaps.emptyMap()),
    Play(
        Protocol.Builder()
            .register(ClientTeleportConfirmPacket::class, ClientTeleportConfirmPacketReader, 754 to 0x00)
            .register(ClientBlockQueryPacket::class, ClientBlockQueryPacketReader, 754 to 0x01)
            .register(ClientDifficultyPacket::class, ClientDifficultyPacketReader, 754 to 0x02)
            .register(ClientTextPacket::class, ClientTextPacketReader, 754 to 0x03)
            .register(ClientStatusPacket::class, ClientStatusPacketReader, 754 to 0x04)
            .register(ClientSettingsPacket::class, ClientSettingsPacketReader, 754 to 0x05)
            .register(ClientCommandSuggestPacket::class, ClientCommandSuggestPacketReader, 754 to 0x06)
            .register(ClientWindowConfirmPacket::class, ClientWindowConfirmPacketReader, 754 to 0x07)
            .register(ClientWindowClickButtonPacket::class, ClientWindowClickButtonPacketReader, 754 to 0x08)
            .register(ClientWindowClickPacket::class, ClientWindowClickPacketReader, 754 to 0x09)
            .register(ClientWindowClosePacket::class, ClientWindowClosePacketReader, 754 to 0x0A)
            .register(ClientCustomPayloadPacket::class, ClientCustomPayloadPacketReader, 754 to 0x0B)
            .register(ClientBookEditPacket::class, ClientBookEditPacketReader, 754 to 0x0C)
            .register(ClientEntityQueryPacket::class, ClientEntityQueryPacketReader, 754 to 0x0D)
            .register(ClientItemUseOnEntityPacket::class, ClientItemUseOnEntityPacketReader, 754 to 0x0E)
            .register(ClientKeepAlivePacket::class, ClientKeepAlivePacketReader, 754 to 0x10)
            .register(ClientPositionPacket::class, ClientPositionPacketReader, 754 to 0x12)
            .register(ClientPositionRotationPacket::class, ClientPositionRotationPacketReader, 754 to 0x13)
            .register(ClientRotationPacket::class, ClientRotationPacketReader, 754 to 0x14)
            .register(ClientLocationPacket::class, ClientLocationPacketReader, 754 to 0x15)
            .register(ClientVehicleLocationPacket::class, ClientVehicleLocationPacketReader, 754 to 0x16)
            .register(ClientSteerBoatPacket::class, ClientSteerBoatPacketReader, 754 to 0x17)
            .register(ClientItemPickPacket::class, ClientItemPickPacketReader, 754 to 0x18)
            .register(ClientCraftPacket::class, ClientCraftPacketReader, 754 to 0x19)
            .register(ClientAbilitiesPacket::class, ClientAbilitiesPacketReader, 754 to 0x1A)
            .register(ClientActionPacket::class, ClientActionPacketReader, 754 to 0x1B)
            .register(ClientPlayerActionPacket::class, ClientPlayerActionPacketReader, 754 to 0x1C)
            .register(ClientSteerPacket::class, ClientSteerPacketReader, 754 to 0x1D)
            .register(ClientItemNamePacket::class, ClientItemNamePacketReader, 754 to 0x20)
            .register(ClientResourcePackStatusPacket::class, ClientResourcePackStatusPacketReader, 754 to 0x21)
            .register(ClientAdvancementTabPacket::class, ClientAdvancementTabPacketReader, 754 to 0x22)
            .register(ClientTradePacket::class, ClientTradePacketReader, 754 to 0x23)
            .register(ClientBeaconUpdatePacket::class, ClientBeaconUpdatePacketReader, 754 to 0x24)
            .register(ClientHotbarPacket::class, ClientHotbarPacketReader, 754 to 0x25)
            .register(ClientCommandBlockUpdatePacket::class, ClientCommandBlockUpdatePacketReader, 754 to 0x26)
            .register(ClientCommandBlockMinecartUpdatePacket::class, ClientCommandBlockMinecartUpdatePacketReader, 754 to 0x27)
            .register(ClientCreativeInventorySlotPacket::class, ClientCreativeInventorySlotPacketReader, 754 to 0x28)
            .register(ClientJigsawBlockUpdatePacket::class, ClientJigsawBlockUpdatePacketReader, 754 to 0x29)
            .register(ClientStructureBlockUpdatePacket::class, ClientStructureBlockUpdatePacketReader, 754 to 0x2A)
            .register(ClientSignUpdatePacket::class, ClientSignUpdatePacketReader, 754 to 0x2B)
            .register(ClientSwingArmPacket::class, ClientSwingArmPacketReader, 754 to 0x2C)
            .register(ClientSpectatePacket::class, ClientSpectatePacketReader, 754 to 0x2D)
            .register(ClientBlockPlacePacket::class, ClientBlockPlacePacketReader, 754 to 0x2E)
            .register(ClientItemUsePacket::class, ClientItemUsePacketReader, 754 to 0x2F)
            .build(),
        Protocol.Builder()
            .register(ServerObjectAddPacket::class, ServerObjectAddPacketReader, 754 to 0x00)
            .register(ServerExperienceOrbAddPacket::class, ServerExperienceOrbAddPacketReader, 754 to 0x01)
            .register(ServerEntityAddPacket::class, ServerEntityAddPacketReader, 754 to 0x02)
            .register(ServerPlayerAddPacket::class, ServerPlayerAddPacketReader, 754 to 0x04)
            .register(ServerEntityAnimationPacket::class, ServerEntityAnimationPacketReader, 754 to 0x05)
            .register(ServerActionResponsePacket::class, ServerActionResponsePacketReader, 754 to 0x07)
            .register(ServerBlockBreakAnimationPacket::class, ServerBlockBreakAnimationPacketReader, 754 to 0x08)
            .register(ServerBlockEntityPacket::class, ServerBlockEntityPacketReader, 754 to 0x09)
            .register(ServerBlockEventPacket::class, ServerBlockEventPacketReader, 754 to 0x0A)
            .register(ServerBlockUpdatePacket::class, ServerBlockUpdatePacketReader, 754 to 0x0B)
            .register(ServerBossBarPacket::class, ServerBossBarPacketReader, 754 to 0x0C)
            .register(ServerDifficultyPacket::class, ServerDifficultyPacketReader, 754 to 0x0D)
            .register(ServerTextPacket::class, ServerTextPacketReader, 754 to 0x0E)
            .register(ServerCommandSuggestPacket::class, ServerCommandSuggestPacketReader, 754 to 0x0F)
            .register(ServerWindowConfirmPacket::class, ServerWindowConfirmPacketReader, 754 to 0x11)
            .register(ServerWindowClosePacket::class, ServerWindowClosePacketReader, 754 to 0x12)
            .register(ServerInventoryContentPacket::class, ServerInventoryContentPacketReader, 754 to 0x13)
            .register(ServerWindowPropertyPacket::class, ServerWindowPropertyPacketReader, 754 to 0x14)
            .register(ServerInventorySlotPacket::class, ServerInventorySlotPacketReader, 754 to 0x15)
            .register(ServerItemCooldownPacket::class, ServerItemCooldownPacketReader, 754 to 0x16)
            .register(ServerCustomPayloadPacket::class, ServerCustomPayloadPacketReader, 754 to 0x17)
            .register(ServerNamedSoundPacket::class, ServerNamedSoundPacketReader, 754 to 0x18)
            .register(ServerDisconnectPacket::class, ServerDisconnectPacketReader, 754 to 0x19)
            .register(ServerEntityEventPacket::class, ServerEntityEventPacketReader, 754 to 0x1A)
            .register(ServerExplosionPacket::class, ServerExplosionPacketReader, 754 to 0x1B)
            .register(ServerChunkUnloadPacket::class, ServerChunkUnloadPacketReader, 754 to 0x1C)
            .register(ServerGameStatePacket::class, ServerGameStatePacketReader, 754 to 0x1D)
            .register(ServerHorseWindowOpenPacket::class, ServerHorseWindowOpenPacketReader, 754 to 0x1E)
            .register(ServerKeepAlivePacket::class, ServerKeepAlivePacketReader, 754 to 0x1F)
            .register(ServerChunkPacket::class, ServerChunkPacketReader, 754 to 0x20)
            .register(ServerWorldEventPacket::class, ServerWorldEventPacketReader, 754 to 0x21)
            .register(ServerWorldPacket::class, ServerWorldPacketReader, 754 to 0x24)
            .register(ServerTradePacket::class, ServerTradePacketReader, 754 to 0x26)
            .register(ServerEntityMovePacket::class, ServerEntityMovePacketReader, 754 to 0x27)
            .register(ServerEntityMoveRotatePacket::class, ServerEntityMoveRotatePacketReader, 754 to 0x28)
            .register(ServerEntityRotatePacket::class, ServerEntityRotatePacketReader, 754 to 0x29)
            .register(ServerEntityLocationPacket::class, ServerEntityLocationPacketReader, 754 to 0x2A)
            .register(ServerVehicleLocationPacket::class, ServerVehicleLocationPacketReader, 754 to 0x2B)
            .register(ServerBookOpenPacket::class, ServerBookOpenPacketReader, 754 to 0x2C)
            .register(ServerWindowOpenPacket::class, ServerWindowOpenPacketReader, 754 to 0x2D)
            .register(ServerSignUpdatePacket::class, ServerSignUpdatePacketReader, 754 to 0x2E)
            .register(ServerCraftPacket::class, ServerCraftPacketReader, 754 to 0x2F)
            .register(ServerAbilitiesPacket::class, ServerAbilitiesPacketReader, 754 to 0x30)
            .register(ServerPlayerCombatEventPacket::class, ServerPlayerCombatEventPacketReader, 754 to 0x31)
            .register(ServerPlayerListPacket::class, ServerPlayerListPacketReader, 754 to 0x32)
            .register(ServerLookAtPacket::class, ServerLookAtPacketReader, 754 to 0x33)
            .register(ServerLocationPacket::class, ServerLocationPacketReader, 754 to 0x34)
            .register(ServerRecipeBookPacket::class, ServerRecipeBookPacketReader, 754 to 0x35)
            .register(ServerEntitiesRemovePacket::class, ServerEntitiesRemovePacketReader, 754 to 0x36)
            .register(ServerEntityEffectRevokePacket::class, ServerEntityEffectRevokePacketReader, 754 to 0x37)
            .register(ServerResourcePackPacket::class, ServerResourcePackPacketReader, 754 to 0x38)
            .register(ServerRespawnPacket::class, ServerRespawnPacketReader, 754 to 0x39)
            .register(ServerEntityHeadRotationPacket::class, ServerEntityHeadRotationPacketReader, 754 to 0x3A)
            .register(ServerBlockUpdatesPacket::class, ServerBlockUpdatesPacketReader, 754 to 0x3B)
            .register(ServerBorderPacket::class, ServerBorderPacketReader, 754 to 0x3D)
            .register(ServerCameraPacket::class, ServerCameraPacketReader, 754 to 0x3E)
            .register(ServerHotbarPacket::class, ServerHotbarPacketReader, 754 to 0x3F)
            .register(ServerChunkPublishPacket::class, ServerChunkPublishPacketReader, 754 to 0x40)
            .register(ServerViewDistancePacket::class, ServerViewDistancePacketReader, 754 to 0x41)
            .register(ServerSpawnPositionPacket::class, ServerSpawnPositionPacketReader, 754 to 0x42)
            .register(ServerScoreboardDisplayPacket::class, ServerScoreboardDisplayPacketReader, 754 to 0x43)
            .register(ServerEntityMetadataPacket::class, ServerEntityMetadataPacketReader, 754 to 0x44)
            .register(ServerEntityAttachPacket::class, ServerEntityAttachPacketReader, 754 to 0x45)
            .register(ServerEntityVelocityPacket::class, ServerEntityVelocityPacketReader, 754 to 0x46)
            .register(ServerEntityEquipmentPacket::class, ServerEntityEquipmentPacketReader, 754 to 0x47)
            .register(ServerExperiencePacket::class, ServerExperiencePacketReader, 754 to 0x48)
            .register(ServerHealthHungerSaturationPacket::class, ServerHealthHungerSaturationPacketReader, 754 to 0x49)
            .register(ServerObjectivePacket::class, ServerObjectivePacketReader, 754 to 0x4A)
            .register(ServerEntityPassengersPacket::class, ServerEntityPassengersPacketReader, 754 to 0x4B)
            .register(ServerTeamPacket::class, ServerTeamPacketReader, 754 to 0x4C)
            .register(ServerScorePacket::class, ServerScorePacketReader, 754 to 0x4D)
            .register(ServerTimePacket::class, ServerTimePacketReader, 754 to 0x4E)
            .register(ServerTitlePacket::class, ServerTitlePacketReader, 754 to 0x4F)
            .register(ServerEntitySoundPacket::class, ServerEntitySoundPacketReader, 754 to 0x50)
            .register(ServerSoundPacket::class, ServerSoundPacketReader, 754 to 0x51)
            .register(ServerSoundStopPacket::class, ServerSoundStopPacketReader, 754 to 0x52)
            .register(ServerPlayerListHeaderFooterPacket::class, ServerPlayerListHeaderFooterPacketReader, 754 to 0x53)
            .register(ServerQueryPacket::class, ServerQueryPacketReader, 754 to 0x54)
            .register(ServerStackTakePacket::class, ServerStackTakePacketReader, 754 to 0x55)
            .register(ServerEntityTeleportPacket::class, ServerEntityTeleportPacketReader, 754 to 0x56)
            .register(ServerEntityAttributesPacket::class, ServerEntityAttributesPacketReader, 754 to 0x58)
            .register(ServerEntityEffectApplyPacket::class, ServerEntityEffectApplyPacketReader, 754 to 0x59)
            .build()
    )
}
