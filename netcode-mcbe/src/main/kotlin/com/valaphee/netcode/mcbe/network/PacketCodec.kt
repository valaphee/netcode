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

package com.valaphee.netcode.mcbe.network

import com.valaphee.netcode.mcbe.latestProtocolVersion
import com.valaphee.netcode.mcbe.network.packet.AdventureSettingsPacketReader
import com.valaphee.netcode.mcbe.network.packet.AnvilDamagePacketReader
import com.valaphee.netcode.mcbe.network.packet.AppearancePacketReader
import com.valaphee.netcode.mcbe.network.packet.ArmorDamagePacketReader
import com.valaphee.netcode.mcbe.network.packet.BehaviorTreePacketReader
import com.valaphee.netcode.mcbe.network.packet.BiomeDefinitionsPacketReader
import com.valaphee.netcode.mcbe.network.packet.BlockComponentPacketReader
import com.valaphee.netcode.mcbe.network.packet.BlockEntityPacketReader
import com.valaphee.netcode.mcbe.network.packet.BlockEventPacketReader
import com.valaphee.netcode.mcbe.network.packet.BlockPickPacketReader
import com.valaphee.netcode.mcbe.network.packet.BlockUpdatePacketReader
import com.valaphee.netcode.mcbe.network.packet.BlockUpdateSyncedPacketReader
import com.valaphee.netcode.mcbe.network.packet.BlockUpdatesSyncedPacketReader
import com.valaphee.netcode.mcbe.network.packet.BookEditPacketReader
import com.valaphee.netcode.mcbe.network.packet.BossBarPacketReader
import com.valaphee.netcode.mcbe.network.packet.CacheBlobStatusPacketReader
import com.valaphee.netcode.mcbe.network.packet.CacheBlobsPacketReader
import com.valaphee.netcode.mcbe.network.packet.CacheStatusPacketReader
import com.valaphee.netcode.mcbe.network.packet.CameraPacketReader
import com.valaphee.netcode.mcbe.network.packet.CameraShakePacketReader
import com.valaphee.netcode.mcbe.network.packet.ChunkPacketReader
import com.valaphee.netcode.mcbe.network.packet.ChunkPublishPacketReader
import com.valaphee.netcode.mcbe.network.packet.ClientToServerHandshakePacketReader
import com.valaphee.netcode.mcbe.network.packet.CodeBuilderPacketReader
import com.valaphee.netcode.mcbe.network.packet.CommandBlockUpdatePacketReader
import com.valaphee.netcode.mcbe.network.packet.CommandPacketReader
import com.valaphee.netcode.mcbe.network.packet.CommandResponsePacketReader
import com.valaphee.netcode.mcbe.network.packet.CommandSettingsPacketReader
import com.valaphee.netcode.mcbe.network.packet.CommandSoftEnumerationPacketReader
import com.valaphee.netcode.mcbe.network.packet.CommandsPacketReader
import com.valaphee.netcode.mcbe.network.packet.CraftingEventPacketReader
import com.valaphee.netcode.mcbe.network.packet.CreativeInventoryPacketReader
import com.valaphee.netcode.mcbe.network.packet.CustomEventPacketReader
import com.valaphee.netcode.mcbe.network.packet.DebugPacketReader
import com.valaphee.netcode.mcbe.network.packet.DebugRendererPacketReader
import com.valaphee.netcode.mcbe.network.packet.DefaultGameModePacketReader
import com.valaphee.netcode.mcbe.network.packet.DifficultyPacketReader
import com.valaphee.netcode.mcbe.network.packet.DimensionDataPacketReader
import com.valaphee.netcode.mcbe.network.packet.DimensionPacketReader
import com.valaphee.netcode.mcbe.network.packet.DisconnectPacketReader
import com.valaphee.netcode.mcbe.network.packet.EducationUriResourcePacketReader
import com.valaphee.netcode.mcbe.network.packet.EmotePacketReader
import com.valaphee.netcode.mcbe.network.packet.EmotesPacketReader
import com.valaphee.netcode.mcbe.network.packet.EnchantOptionsPacketReader
import com.valaphee.netcode.mcbe.network.packet.EntityAddPacketReader
import com.valaphee.netcode.mcbe.network.packet.EntityAnimatePacketReader
import com.valaphee.netcode.mcbe.network.packet.EntityAnimationPacketReader
import com.valaphee.netcode.mcbe.network.packet.EntityArmorPacketReader
import com.valaphee.netcode.mcbe.network.packet.EntityAttributesPacketReader
import com.valaphee.netcode.mcbe.network.packet.EntityEffectPacketReader
import com.valaphee.netcode.mcbe.network.packet.EntityEquipmentPacketReader
import com.valaphee.netcode.mcbe.network.packet.EntityEventPacketReader
import com.valaphee.netcode.mcbe.network.packet.EntityFallPacketReader
import com.valaphee.netcode.mcbe.network.packet.EntityIdentifiersPacketReader
import com.valaphee.netcode.mcbe.network.packet.EntityLinkPacketReader
import com.valaphee.netcode.mcbe.network.packet.EntityMetadataPacketReader
import com.valaphee.netcode.mcbe.network.packet.EntityMoveRotatePacketReader
import com.valaphee.netcode.mcbe.network.packet.EntityPickPacketReader
import com.valaphee.netcode.mcbe.network.packet.EntityPropertiesPacketReader
import com.valaphee.netcode.mcbe.network.packet.EntityPropertyPacketReader
import com.valaphee.netcode.mcbe.network.packet.EntityRemovePacketReader
import com.valaphee.netcode.mcbe.network.packet.EntityTeleportPacketReader
import com.valaphee.netcode.mcbe.network.packet.EntityVelocityPacketReader
import com.valaphee.netcode.mcbe.network.packet.EquipmentPacketReader
import com.valaphee.netcode.mcbe.network.packet.ExperienceOrbAddPacketReader
import com.valaphee.netcode.mcbe.network.packet.FilterPacketReader
import com.valaphee.netcode.mcbe.network.packet.FogPacketReader
import com.valaphee.netcode.mcbe.network.packet.FormPacketReader
import com.valaphee.netcode.mcbe.network.packet.FormResponsePacketReader
import com.valaphee.netcode.mcbe.network.packet.GameModePacketReader
import com.valaphee.netcode.mcbe.network.packet.GameRulesPacketReader
import com.valaphee.netcode.mcbe.network.packet.HealthPacketReader
import com.valaphee.netcode.mcbe.network.packet.HotbarPacketReader
import com.valaphee.netcode.mcbe.network.packet.InputCorrectPacketReader
import com.valaphee.netcode.mcbe.network.packet.InputPacketReader
import com.valaphee.netcode.mcbe.network.packet.InteractPacketReader
import com.valaphee.netcode.mcbe.network.packet.InventoryContentPacketReader
import com.valaphee.netcode.mcbe.network.packet.InventorySlotPacketReader
import com.valaphee.netcode.mcbe.network.packet.InventoryTransactionPacketReader
import com.valaphee.netcode.mcbe.network.packet.ItemActionPacketReader
import com.valaphee.netcode.mcbe.network.packet.ItemComponentPacketReader
import com.valaphee.netcode.mcbe.network.packet.ItemFrameDropItemPacketReader
import com.valaphee.netcode.mcbe.network.packet.LabTablePacketReader
import com.valaphee.netcode.mcbe.network.packet.LastHurtByPacketReader
import com.valaphee.netcode.mcbe.network.packet.LatencyPacketReader
import com.valaphee.netcode.mcbe.network.packet.LecternUpdatePacketReader
import com.valaphee.netcode.mcbe.network.packet.LocalPlayerAsInitializedPacketReader
import com.valaphee.netcode.mcbe.network.packet.LoginPacketReader
import com.valaphee.netcode.mcbe.network.packet.MapCreateLockedCopyPacketReader
import com.valaphee.netcode.mcbe.network.packet.MapRequestPacketReader
import com.valaphee.netcode.mcbe.network.packet.MultiplayerSettingsPacketReader
import com.valaphee.netcode.mcbe.network.packet.NetworkSettingsPacketReader
import com.valaphee.netcode.mcbe.network.packet.NpcDialoguePacketReader
import com.valaphee.netcode.mcbe.network.packet.NpcRequestPacketReader
import com.valaphee.netcode.mcbe.network.packet.ObjectiveRemovePacketReader
import com.valaphee.netcode.mcbe.network.packet.ObjectiveSetPacketReader
import com.valaphee.netcode.mcbe.network.packet.OnScreenTextureAnimationPacketReader
import com.valaphee.netcode.mcbe.network.packet.PackDataChunkPacketReader
import com.valaphee.netcode.mcbe.network.packet.PackDataChunkRequestPacketReader
import com.valaphee.netcode.mcbe.network.packet.PackDataPacketReader
import com.valaphee.netcode.mcbe.network.packet.PacksPacketReader
import com.valaphee.netcode.mcbe.network.packet.PacksResponsePacketReader
import com.valaphee.netcode.mcbe.network.packet.PacksStackPacketReader
import com.valaphee.netcode.mcbe.network.packet.PaintingAddPacketReader
import com.valaphee.netcode.mcbe.network.packet.ParticlePacketReader
import com.valaphee.netcode.mcbe.network.packet.PhotoItemPacketReader
import com.valaphee.netcode.mcbe.network.packet.PhotoPacketReader
import com.valaphee.netcode.mcbe.network.packet.PhotoRequestPacketReader
import com.valaphee.netcode.mcbe.network.packet.PlayerActionPacketReader
import com.valaphee.netcode.mcbe.network.packet.PlayerAddPacketReader
import com.valaphee.netcode.mcbe.network.packet.PlayerArmorDamagePacketReader
import com.valaphee.netcode.mcbe.network.packet.PlayerGameModePacketReader
import com.valaphee.netcode.mcbe.network.packet.PlayerListPacketReader
import com.valaphee.netcode.mcbe.network.packet.PlayerLocationPacketReader
import com.valaphee.netcode.mcbe.network.packet.PositionTrackingDbClientRequestPacketReader
import com.valaphee.netcode.mcbe.network.packet.PositionTrackingDbServerBroadcastPacketReader
import com.valaphee.netcode.mcbe.network.packet.PurchaseReceiptPacketReader
import com.valaphee.netcode.mcbe.network.packet.RecipesPacketReader
import com.valaphee.netcode.mcbe.network.packet.RespawnPacketReader
import com.valaphee.netcode.mcbe.network.packet.RiderJumpPacketReader
import com.valaphee.netcode.mcbe.network.packet.ScoreboardIdentityPacketReader
import com.valaphee.netcode.mcbe.network.packet.ScoresPacketReader
import com.valaphee.netcode.mcbe.network.packet.ServerSettingsPacketReader
import com.valaphee.netcode.mcbe.network.packet.ServerSettingsRequestPacketReader
import com.valaphee.netcode.mcbe.network.packet.ServerToClientHandshakePacketReader
import com.valaphee.netcode.mcbe.network.packet.SettingsCommandPacketReader
import com.valaphee.netcode.mcbe.network.packet.ShowCreditsPacketReader
import com.valaphee.netcode.mcbe.network.packet.ShowProfilePacketReader
import com.valaphee.netcode.mcbe.network.packet.ShowStoreOfferPacketReader
import com.valaphee.netcode.mcbe.network.packet.SimpleEventPacketReader
import com.valaphee.netcode.mcbe.network.packet.SimulationPacketReader
import com.valaphee.netcode.mcbe.network.packet.SoundEventPacketReader
import com.valaphee.netcode.mcbe.network.packet.SoundEventPacketV1Reader
import com.valaphee.netcode.mcbe.network.packet.SoundEventPacketV2Reader
import com.valaphee.netcode.mcbe.network.packet.SoundPacketReader
import com.valaphee.netcode.mcbe.network.packet.SoundStopPacketReader
import com.valaphee.netcode.mcbe.network.packet.SpawnPositionPacketReader
import com.valaphee.netcode.mcbe.network.packet.StackAddPacketReader
import com.valaphee.netcode.mcbe.network.packet.StackTakePacketReader
import com.valaphee.netcode.mcbe.network.packet.StatusPacketReader
import com.valaphee.netcode.mcbe.network.packet.SteerPacketReader
import com.valaphee.netcode.mcbe.network.packet.StructureBlockUpdatePacketReader
import com.valaphee.netcode.mcbe.network.packet.StructureTemplateDataExportRequestPacketReader
import com.valaphee.netcode.mcbe.network.packet.StructureTemplateDataExportResponsePacketReader
import com.valaphee.netcode.mcbe.network.packet.SubChunkPacketReader
import com.valaphee.netcode.mcbe.network.packet.SubChunkRequestPacketReader
import com.valaphee.netcode.mcbe.network.packet.SubLoginPacketReader
import com.valaphee.netcode.mcbe.network.packet.TextPacketReader
import com.valaphee.netcode.mcbe.network.packet.TickSyncPacketReader
import com.valaphee.netcode.mcbe.network.packet.TimePacketReader
import com.valaphee.netcode.mcbe.network.packet.TitlePacketReader
import com.valaphee.netcode.mcbe.network.packet.TradePacketReader
import com.valaphee.netcode.mcbe.network.packet.TransferPacketReader
import com.valaphee.netcode.mcbe.network.packet.VelocityPredictionPacketReader
import com.valaphee.netcode.mcbe.network.packet.VideoStreamPacketReader
import com.valaphee.netcode.mcbe.network.packet.ViewDistancePacketReader
import com.valaphee.netcode.mcbe.network.packet.ViewDistanceRequestPacketReader
import com.valaphee.netcode.mcbe.network.packet.ViolationPacketReader
import com.valaphee.netcode.mcbe.network.packet.WebSocketPacketReader
import com.valaphee.netcode.mcbe.network.packet.WindowClosePacketReader
import com.valaphee.netcode.mcbe.network.packet.WindowOpenPacketReader
import com.valaphee.netcode.mcbe.network.packet.WindowPropertyPacketReader
import com.valaphee.netcode.mcbe.network.packet.WorldEventPacketReader
import com.valaphee.netcode.mcbe.network.packet.WorldGenericEventPacketReader
import com.valaphee.netcode.mcbe.network.packet.WorldPacketReader
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageCodec
import io.netty.handler.codec.DecoderException
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import kotlin.reflect.full.findAnnotation

/**
 * @author Kevin Ludwig
 */
class PacketCodec(
    var wrapBuffer: (ByteBuf) -> PacketBuffer,
    private val client: Boolean,
    var version: Int = latestProtocolVersion
) : ByteToMessageCodec<Packet>() {
    public override fun encode(context: ChannelHandlerContext?, message: Packet, out: ByteBuf) {
        wrapBuffer(out).apply {
            writeVarUInt(message.id and Packet.idMask or ((message.senderId and Packet.senderIdMask) shl Packet.senderIdShift) or ((message.clientId and Packet.clientIdMask) shl Packet.clientIdShift))
            message.write(this, version)
        }
    }

    public override fun decode(context: ChannelHandlerContext?, `in`: ByteBuf, out: MutableList<Any>) {
        val buffer = wrapBuffer(`in`)
        val header = buffer.readVarUInt()
        val id = header and Packet.idMask
        try {
            (if (client) clientReaders else serverReaders)[id]?.let {
                try {
                    out.add(it.read(buffer, version).apply {
                        senderId = (header shr Packet.senderIdShift) and Packet.senderIdMask
                        clientId = (header shr Packet.clientIdShift) and Packet.clientIdMask
                    })
                } catch (ex: Exception) {
                    throw DecoderException("0x${id.toString(16).uppercase()} problematic at 0x${buffer.readerIndex().toString(16).uppercase()}", ex)
                }
                if (buffer.isReadable) throw DecoderException("0x${id.toString(16).uppercase()} not fully read")
            } ?: throw DecoderException("0x${id.toString(16).uppercase()} unknown")
        } finally {
            if (buffer.isReadable) buffer.skipBytes(buffer.readableBytes())
        }
    }

    companion object {
        const val NAME = "mcbe-packet-codec"
        private val readers = Int2ObjectOpenHashMap<PacketReader>().apply {
            this[0x01] = LoginPacketReader
            this[0x02] = StatusPacketReader
            this[0x03] = ServerToClientHandshakePacketReader
            this[0x04] = ClientToServerHandshakePacketReader
            this[0x05] = DisconnectPacketReader
            this[0x06] = PacksPacketReader
            this[0x07] = PacksStackPacketReader
            this[0x08] = PacksResponsePacketReader
            this[0x09] = TextPacketReader
            this[0x0A] = TimePacketReader
            this[0x0B] = WorldPacketReader
            this[0x0C] = PlayerAddPacketReader
            this[0x0D] = EntityAddPacketReader
            this[0x0E] = EntityRemovePacketReader
            this[0x0F] = StackAddPacketReader
            this[0x11] = StackTakePacketReader
            this[0x12] = EntityTeleportPacketReader
            this[0x13] = PlayerLocationPacketReader
            this[0x14] = RiderJumpPacketReader
            this[0x15] = BlockUpdatePacketReader
            this[0x16] = PaintingAddPacketReader
            this[0x17] = TickSyncPacketReader
            this[0x18] = SoundEventPacketV1Reader
            this[0x19] = WorldEventPacketReader
            this[0x1A] = BlockEventPacketReader
            this[0x1B] = EntityEventPacketReader
            this[0x1C] = EntityEffectPacketReader
            this[0x1D] = EntityAttributesPacketReader
            this[0x1E] = InventoryTransactionPacketReader
            this[0x1F] = EntityEquipmentPacketReader
            this[0x20] = EntityArmorPacketReader
            this[0x21] = InteractPacketReader
            this[0x22] = BlockPickPacketReader
            this[0x23] = EntityPickPacketReader
            this[0x24] = PlayerActionPacketReader
            this[0x25] = EntityFallPacketReader
            this[0x26] = ArmorDamagePacketReader
            this[0x27] = EntityMetadataPacketReader
            this[0x28] = EntityVelocityPacketReader
            this[0x29] = EntityLinkPacketReader
            this[0x2A] = HealthPacketReader
            this[0x2B] = SpawnPositionPacketReader
            this[0x2C] = EntityAnimationPacketReader
            this[0x2D] = RespawnPacketReader
            this[0x2E] = WindowOpenPacketReader
            this[0x2F] = WindowClosePacketReader
            this[0x30] = HotbarPacketReader
            this[0x31] = InventoryContentPacketReader
            this[0x32] = InventorySlotPacketReader
            this[0x33] = WindowPropertyPacketReader
            this[0x34] = RecipesPacketReader
            this[0x35] = CraftingEventPacketReader
            //this[0x36] =
            this[0x37] = AdventureSettingsPacketReader
            this[0x38] = BlockEntityPacketReader
            this[0x39] = SteerPacketReader
            this[0x3A] = ChunkPacketReader
            this[0x3B] = CommandSettingsPacketReader
            this[0x3C] = DifficultyPacketReader
            this[0x3D] = DimensionPacketReader
            this[0x3E] = GameModePacketReader
            this[0x3F] = PlayerListPacketReader
            this[0x40] = SimpleEventPacketReader
            //this[0x41] = EventPacketReader
            this[0x42] = ExperienceOrbAddPacketReader
            //this[0x43] = MapPacketReader
            this[0x44] = MapRequestPacketReader
            this[0x45] = ViewDistanceRequestPacketReader
            this[0x46] = ViewDistancePacketReader
            this[0x47] = ItemFrameDropItemPacketReader
            this[0x48] = GameRulesPacketReader
            this[0x49] = CameraPacketReader
            this[0x4A] = BossBarPacketReader
            this[0x4B] = ShowCreditsPacketReader
            this[0x4C] = CommandsPacketReader
            this[0x4D] = CommandPacketReader
            this[0x4E] = CommandBlockUpdatePacketReader
            this[0x4F] = CommandResponsePacketReader
            this[0x50] = TradePacketReader
            this[0x51] = EquipmentPacketReader
            this[0x52] = PackDataPacketReader
            this[0x53] = PackDataChunkPacketReader
            this[0x54] = PackDataChunkRequestPacketReader
            this[0x55] = TransferPacketReader
            this[0x56] = SoundPacketReader
            this[0x57] = SoundStopPacketReader
            this[0x58] = TitlePacketReader
            this[0x59] = BehaviorTreePacketReader
            this[0x5A] = StructureBlockUpdatePacketReader
            this[0x5B] = ShowStoreOfferPacketReader
            this[0x5C] = PurchaseReceiptPacketReader
            this[0x5D] = AppearancePacketReader
            this[0x5E] = SubLoginPacketReader
            this[0x5F] = WebSocketPacketReader
            this[0x60] = LastHurtByPacketReader
            this[0x61] = BookEditPacketReader
            this[0x62] = NpcRequestPacketReader
            this[0x63] = PhotoPacketReader
            this[0x64] = FormPacketReader
            this[0x65] = FormResponsePacketReader
            this[0x66] = ServerSettingsRequestPacketReader
            this[0x67] = ServerSettingsPacketReader
            this[0x68] = ShowProfilePacketReader
            this[0x69] = DefaultGameModePacketReader
            this[0x6A] = ObjectiveRemovePacketReader
            this[0x6B] = ObjectiveSetPacketReader
            this[0x6C] = ScoresPacketReader
            this[0x6D] = LabTablePacketReader
            this[0x6E] = BlockUpdateSyncedPacketReader
            this[0x6F] = EntityMoveRotatePacketReader
            this[0x70] = ScoreboardIdentityPacketReader
            this[0x71] = LocalPlayerAsInitializedPacketReader
            this[0x72] = CommandSoftEnumerationPacketReader
            this[0x73] = LatencyPacketReader
            //this[0x74] =
            this[0x75] = CustomEventPacketReader
            this[0x76] = ParticlePacketReader
            this[0x77] = EntityIdentifiersPacketReader
            this[0x78] = SoundEventPacketV2Reader
            this[0x79] = ChunkPublishPacketReader
            this[0x7A] = BiomeDefinitionsPacketReader
            this[0x7B] = SoundEventPacketReader
            this[0x7C] = WorldGenericEventPacketReader
            this[0x7D] = LecternUpdatePacketReader
            this[0x7E] = VideoStreamPacketReader
            //this[0x7F] =
            //this[0x80] =
            this[0x81] = CacheStatusPacketReader
            this[0x82] = OnScreenTextureAnimationPacketReader
            this[0x83] = MapCreateLockedCopyPacketReader
            this[0x84] = StructureTemplateDataExportRequestPacketReader
            this[0x85] = StructureTemplateDataExportResponsePacketReader
            this[0x86] = BlockComponentPacketReader
            this[0x87] = CacheBlobStatusPacketReader
            this[0x88] = CacheBlobsPacketReader
            //this[0x89] = EducationSettingsPacket
            this[0x8A] = EmotePacketReader
            this[0x8B] = MultiplayerSettingsPacketReader
            this[0x8C] = SettingsCommandPacketReader
            this[0x8D] = AnvilDamagePacketReader
            this[0x8E] = ItemActionPacketReader
            this[0x8F] = NetworkSettingsPacketReader
            this[0x90] = InputPacketReader
            this[0x91] = CreativeInventoryPacketReader
            this[0x92] = EnchantOptionsPacketReader
            //this[0x93] = InventoryRequestPacketReader
            //this[0x94] = InventoryResponsePacketReader
            this[0x95] = PlayerArmorDamagePacketReader
            this[0x96] = CodeBuilderPacketReader
            this[0x97] = PlayerGameModePacketReader
            this[0x98] = EmotesPacketReader
            this[0x99] = PositionTrackingDbServerBroadcastPacketReader
            this[0x9A] = PositionTrackingDbClientRequestPacketReader
            this[0x9B] = DebugPacketReader
            this[0x9C] = ViolationPacketReader
            this[0x9D] = VelocityPredictionPacketReader
            this[0x9E] = EntityAnimatePacketReader
            this[0x9F] = CameraShakePacketReader
            this[0xA0] = FogPacketReader
            this[0xA1] = InputCorrectPacketReader
            this[0xA2] = ItemComponentPacketReader
            this[0xA3] = FilterPacketReader
            this[0xA4] = DebugRendererPacketReader
            this[0xA5] = EntityPropertiesPacketReader
            //this[0xA6] = VolumeEntityAddPacketReader
            //this[0xA7] = VolumeEntityRemovePacketReader
            this[0xA8] = SimulationPacketReader
            this[0xA9] = NpcDialoguePacketReader
            this[0xAA] = EducationUriResourcePacketReader
            this[0xAB] = PhotoItemPacketReader
            this[0xAC] = BlockUpdatesSyncedPacketReader
            this[0xAD] = PhotoRequestPacketReader
            this[0xAE] = SubChunkPacketReader
            this[0xAF] = SubChunkRequestPacketReader
            //this[0xB0] = ItemCooldownPacketReader
            //this[0xB1] = ScriptMessagePacketReader
            //this[0xB2] = CodeBuilderSourcePacketReader
            //this[0xB3] = TickingAreasLoadStatusPacket
            this[0xB4] = DimensionDataPacketReader
            //this[0xB5] = AgentActionEventPacket
            this[0xB6] = EntityPropertyPacketReader
        }
        private val clientReaders = readers.filterValues { it::class.java.getMethod("read", PacketBuffer::class.java, Int::class.java).returnType.kotlin.findAnnotation<Restrict>()?.value?.contains(Restriction.ToClient) ?: true }
        private val serverReaders = readers.filterValues { it::class.java.getMethod("read", PacketBuffer::class.java, Int::class.java).returnType.kotlin.findAnnotation<Restrict>()?.value?.contains(Restriction.ToServer) ?: true }
    }
}
