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

import com.valaphee.netcode.mcbe.network.packet.AbilitiesPacket
import com.valaphee.netcode.mcbe.network.packet.AbilityPacket
import com.valaphee.netcode.mcbe.network.packet.AdventureSettingsPacket
import com.valaphee.netcode.mcbe.network.packet.AdventureSettingsWithoutAbilitiesPacket
import com.valaphee.netcode.mcbe.network.packet.AnvilDamagePacket
import com.valaphee.netcode.mcbe.network.packet.AppearancePacket
import com.valaphee.netcode.mcbe.network.packet.ArmorDamagePacket
import com.valaphee.netcode.mcbe.network.packet.AutomationPacket
import com.valaphee.netcode.mcbe.network.packet.BehaviorTreePacket
import com.valaphee.netcode.mcbe.network.packet.BiomeDefinitionsPacket
import com.valaphee.netcode.mcbe.network.packet.BlockComponentPacket
import com.valaphee.netcode.mcbe.network.packet.BlockEntityPacket
import com.valaphee.netcode.mcbe.network.packet.BlockEventPacket
import com.valaphee.netcode.mcbe.network.packet.BlockPickPacket
import com.valaphee.netcode.mcbe.network.packet.BlockUpdatePacket
import com.valaphee.netcode.mcbe.network.packet.BlockUpdateSyncedPacket
import com.valaphee.netcode.mcbe.network.packet.BlockUpdatesSyncedPacket
import com.valaphee.netcode.mcbe.network.packet.BookEditPacket
import com.valaphee.netcode.mcbe.network.packet.BossBarPacket
import com.valaphee.netcode.mcbe.network.packet.CacheBlobStatusPacket
import com.valaphee.netcode.mcbe.network.packet.CacheBlobsPacket
import com.valaphee.netcode.mcbe.network.packet.CacheStatusPacket
import com.valaphee.netcode.mcbe.network.packet.CameraPacket
import com.valaphee.netcode.mcbe.network.packet.CameraShakePacket
import com.valaphee.netcode.mcbe.network.packet.ChunkPacket
import com.valaphee.netcode.mcbe.network.packet.ChunkPublishPacket
import com.valaphee.netcode.mcbe.network.packet.ClientToServerHandshakePacket
import com.valaphee.netcode.mcbe.network.packet.CodeBuilderPacket
import com.valaphee.netcode.mcbe.network.packet.CommandBlockUpdatePacket
import com.valaphee.netcode.mcbe.network.packet.CommandPacket
import com.valaphee.netcode.mcbe.network.packet.CommandResponsePacket
import com.valaphee.netcode.mcbe.network.packet.CommandSettingsPacket
import com.valaphee.netcode.mcbe.network.packet.CommandSoftEnumerationPacket
import com.valaphee.netcode.mcbe.network.packet.CommandsPacket
import com.valaphee.netcode.mcbe.network.packet.CraftingEventPacket
import com.valaphee.netcode.mcbe.network.packet.CreativeInventoryPacket
import com.valaphee.netcode.mcbe.network.packet.CustomEventPacket
import com.valaphee.netcode.mcbe.network.packet.DeathPacket
import com.valaphee.netcode.mcbe.network.packet.DebugOverlayPacket
import com.valaphee.netcode.mcbe.network.packet.DebugPacket
import com.valaphee.netcode.mcbe.network.packet.DefaultGameModePacket
import com.valaphee.netcode.mcbe.network.packet.DifficultyPacket
import com.valaphee.netcode.mcbe.network.packet.DimensionPacket
import com.valaphee.netcode.mcbe.network.packet.DimensionsPacket
import com.valaphee.netcode.mcbe.network.packet.DisconnectPacket
import com.valaphee.netcode.mcbe.network.packet.EducationUriResourcePacket
import com.valaphee.netcode.mcbe.network.packet.EmotePacket
import com.valaphee.netcode.mcbe.network.packet.EmotesPacket
import com.valaphee.netcode.mcbe.network.packet.EnchantOptionsPacket
import com.valaphee.netcode.mcbe.network.packet.EntityAddPacket
import com.valaphee.netcode.mcbe.network.packet.EntityAnimatePacket
import com.valaphee.netcode.mcbe.network.packet.EntityAnimationPacket
import com.valaphee.netcode.mcbe.network.packet.EntityArmorPacket
import com.valaphee.netcode.mcbe.network.packet.EntityAttributesPacket
import com.valaphee.netcode.mcbe.network.packet.EntityEffectPacket
import com.valaphee.netcode.mcbe.network.packet.EntityEquipmentPacket
import com.valaphee.netcode.mcbe.network.packet.EntityEventPacket
import com.valaphee.netcode.mcbe.network.packet.EntityFallPacket
import com.valaphee.netcode.mcbe.network.packet.EntityIdentifiersPacket
import com.valaphee.netcode.mcbe.network.packet.EntityLinkPacket
import com.valaphee.netcode.mcbe.network.packet.EntityMetadataPacket
import com.valaphee.netcode.mcbe.network.packet.EntityMoveRotatePacket
import com.valaphee.netcode.mcbe.network.packet.EntityPickPacket
import com.valaphee.netcode.mcbe.network.packet.EntityPropertiesPacket
import com.valaphee.netcode.mcbe.network.packet.EntityPropertyPacket
import com.valaphee.netcode.mcbe.network.packet.EntityRemovePacket
import com.valaphee.netcode.mcbe.network.packet.EntityTeleportPacket
import com.valaphee.netcode.mcbe.network.packet.EntityVelocityPacket
import com.valaphee.netcode.mcbe.network.packet.EquipmentPacket
import com.valaphee.netcode.mcbe.network.packet.ExperienceOrbAddPacket
import com.valaphee.netcode.mcbe.network.packet.FilterPacket
import com.valaphee.netcode.mcbe.network.packet.FogPacket
import com.valaphee.netcode.mcbe.network.packet.FormPacket
import com.valaphee.netcode.mcbe.network.packet.FormResponsePacket
import com.valaphee.netcode.mcbe.network.packet.GameModePacket
import com.valaphee.netcode.mcbe.network.packet.GameRulesPacket
import com.valaphee.netcode.mcbe.network.packet.HealthPacket
import com.valaphee.netcode.mcbe.network.packet.HotbarPacket
import com.valaphee.netcode.mcbe.network.packet.InputCorrectPacket
import com.valaphee.netcode.mcbe.network.packet.InputPacket
import com.valaphee.netcode.mcbe.network.packet.InteractPacket
import com.valaphee.netcode.mcbe.network.packet.InventoryContentPacket
import com.valaphee.netcode.mcbe.network.packet.InventorySlotPacket
import com.valaphee.netcode.mcbe.network.packet.InventoryTransactionPacket
import com.valaphee.netcode.mcbe.network.packet.ItemActionPacket
import com.valaphee.netcode.mcbe.network.packet.ItemAddPacket
import com.valaphee.netcode.mcbe.network.packet.ItemComponentPacket
import com.valaphee.netcode.mcbe.network.packet.ItemFrameDropItemPacket
import com.valaphee.netcode.mcbe.network.packet.ItemTakePacket
import com.valaphee.netcode.mcbe.network.packet.LabTablePacket
import com.valaphee.netcode.mcbe.network.packet.LastHurtByPacket
import com.valaphee.netcode.mcbe.network.packet.LatencyPacket
import com.valaphee.netcode.mcbe.network.packet.LecternUpdatePacket
import com.valaphee.netcode.mcbe.network.packet.LessonProgressPacket
import com.valaphee.netcode.mcbe.network.packet.LocalPlayerAsInitializedPacket
import com.valaphee.netcode.mcbe.network.packet.LoginPacket
import com.valaphee.netcode.mcbe.network.packet.MapCreateLockedCopyPacket
import com.valaphee.netcode.mcbe.network.packet.MapRequestPacket
import com.valaphee.netcode.mcbe.network.packet.MultiplayerSettingsPacket
import com.valaphee.netcode.mcbe.network.packet.NetworkSettingsPacket
import com.valaphee.netcode.mcbe.network.packet.NpcDialoguePacket
import com.valaphee.netcode.mcbe.network.packet.NpcRequestPacket
import com.valaphee.netcode.mcbe.network.packet.ObjectiveRemovePacket
import com.valaphee.netcode.mcbe.network.packet.ObjectiveSetPacket
import com.valaphee.netcode.mcbe.network.packet.OnScreenTextureAnimationPacket
import com.valaphee.netcode.mcbe.network.packet.PackDataChunkPacket
import com.valaphee.netcode.mcbe.network.packet.PackDataChunkRequestPacket
import com.valaphee.netcode.mcbe.network.packet.PackDataPacket
import com.valaphee.netcode.mcbe.network.packet.PacksPacket
import com.valaphee.netcode.mcbe.network.packet.PacksResponsePacket
import com.valaphee.netcode.mcbe.network.packet.PacksStackPacket
import com.valaphee.netcode.mcbe.network.packet.PaintingAddPacket
import com.valaphee.netcode.mcbe.network.packet.ParticlePacket
import com.valaphee.netcode.mcbe.network.packet.PermissionsPacket
import com.valaphee.netcode.mcbe.network.packet.PhotoItemPacket
import com.valaphee.netcode.mcbe.network.packet.PhotoPacket
import com.valaphee.netcode.mcbe.network.packet.PhotoRequestPacket
import com.valaphee.netcode.mcbe.network.packet.PlayerActionPacket
import com.valaphee.netcode.mcbe.network.packet.PlayerAddPacket
import com.valaphee.netcode.mcbe.network.packet.PlayerArmorDamagePacket
import com.valaphee.netcode.mcbe.network.packet.PlayerGameModePacket
import com.valaphee.netcode.mcbe.network.packet.PlayerListPacket
import com.valaphee.netcode.mcbe.network.packet.PlayerLocationPacket
import com.valaphee.netcode.mcbe.network.packet.PositionTrackingDbClientRequestPacket
import com.valaphee.netcode.mcbe.network.packet.PositionTrackingDbServerBroadcastPacket
import com.valaphee.netcode.mcbe.network.packet.PurchaseReceiptPacket
import com.valaphee.netcode.mcbe.network.packet.RecipesPacket
import com.valaphee.netcode.mcbe.network.packet.RespawnPacket
import com.valaphee.netcode.mcbe.network.packet.RiderJumpPacket
import com.valaphee.netcode.mcbe.network.packet.ScoreboardIdentityPacket
import com.valaphee.netcode.mcbe.network.packet.ScoresPacket
import com.valaphee.netcode.mcbe.network.packet.ServerSettingsPacket
import com.valaphee.netcode.mcbe.network.packet.ServerSettingsRequestPacket
import com.valaphee.netcode.mcbe.network.packet.ServerToClientHandshakePacket
import com.valaphee.netcode.mcbe.network.packet.SettingsCommandPacket
import com.valaphee.netcode.mcbe.network.packet.ShowCreditsPacket
import com.valaphee.netcode.mcbe.network.packet.ShowProfilePacket
import com.valaphee.netcode.mcbe.network.packet.ShowStoreOfferPacket
import com.valaphee.netcode.mcbe.network.packet.SimpleEventPacket
import com.valaphee.netcode.mcbe.network.packet.SimulationPacket
import com.valaphee.netcode.mcbe.network.packet.SoundEventPacket
import com.valaphee.netcode.mcbe.network.packet.SoundEventPacketV1
import com.valaphee.netcode.mcbe.network.packet.SoundEventPacketV2
import com.valaphee.netcode.mcbe.network.packet.SoundPacket
import com.valaphee.netcode.mcbe.network.packet.SoundStopPacket
import com.valaphee.netcode.mcbe.network.packet.SpawnPositionPacket
import com.valaphee.netcode.mcbe.network.packet.StatusPacket
import com.valaphee.netcode.mcbe.network.packet.SteerPacket
import com.valaphee.netcode.mcbe.network.packet.StructureBlockUpdatePacket
import com.valaphee.netcode.mcbe.network.packet.StructureTemplateDataExportRequestPacket
import com.valaphee.netcode.mcbe.network.packet.StructureTemplateDataExportResponsePacket
import com.valaphee.netcode.mcbe.network.packet.SubChunkPacket
import com.valaphee.netcode.mcbe.network.packet.SubChunkRequestPacket
import com.valaphee.netcode.mcbe.network.packet.SubLoginPacket
import com.valaphee.netcode.mcbe.network.packet.TextPacket
import com.valaphee.netcode.mcbe.network.packet.TickSyncPacket
import com.valaphee.netcode.mcbe.network.packet.TimePacket
import com.valaphee.netcode.mcbe.network.packet.TitlePacket
import com.valaphee.netcode.mcbe.network.packet.ToastPacket
import com.valaphee.netcode.mcbe.network.packet.TradePacket
import com.valaphee.netcode.mcbe.network.packet.TransferPacket
import com.valaphee.netcode.mcbe.network.packet.VelocityPredictionPacket
import com.valaphee.netcode.mcbe.network.packet.VideoStreamPacket
import com.valaphee.netcode.mcbe.network.packet.ViewDistancePacket
import com.valaphee.netcode.mcbe.network.packet.ViewDistanceRequestPacket
import com.valaphee.netcode.mcbe.network.packet.ViolationPacket
import com.valaphee.netcode.mcbe.network.packet.WindowClosePacket
import com.valaphee.netcode.mcbe.network.packet.WindowOpenPacket
import com.valaphee.netcode.mcbe.network.packet.WindowPropertyPacket
import com.valaphee.netcode.mcbe.network.packet.WorldEventPacket
import com.valaphee.netcode.mcbe.network.packet.WorldGenericEventPacket
import com.valaphee.netcode.mcbe.network.packet.WorldPacket
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
    var version: Int
) : ByteToMessageCodec<Packet>() {
    public override fun encode(context: ChannelHandlerContext?, message: Packet, out: ByteBuf) {
        wrapBuffer(out).apply {
            writeVarUInt(message.id and Packet.IdMask or ((message.senderId and Packet.SenderIdMask) shl Packet.SenderIdShift) or ((message.clientId and Packet.ClientIdMask) shl Packet.ClientIdShift))
            message.write(this, version)
        }
    }

    public override fun decode(context: ChannelHandlerContext?, `in`: ByteBuf, out: MutableList<Any>) {
        val buffer = wrapBuffer(`in`)
        val header = buffer.readVarUInt()
        val id = header and Packet.IdMask
        try {
            (if (client) clientReaders else serverReaders)[id]?.let {
                try {
                    out.add(it.read(buffer, version).apply {
                        senderId = (header shr Packet.SenderIdShift) and Packet.SenderIdMask
                        clientId = (header shr Packet.ClientIdShift) and Packet.ClientIdMask
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
        const val Name = "mcbe-codec"
        private val readers = Int2ObjectOpenHashMap<Packet.Reader>().apply {
            this[0x01] = LoginPacket.Reader
            this[0x02] = StatusPacket.Reader
            this[0x03] = ServerToClientHandshakePacket.Reader
            this[0x04] = ClientToServerHandshakePacket.Reader
            this[0x05] = DisconnectPacket.Reader
            this[0x06] = PacksPacket.Reader
            this[0x07] = PacksStackPacket.Reader
            this[0x08] = PacksResponsePacket.Reader
            this[0x09] = TextPacket.Reader
            this[0x0A] = TimePacket.Reader
            this[0x0B] = WorldPacket.Reader
            this[0x0C] = PlayerAddPacket.Reader
            this[0x0D] = EntityAddPacket.Reader
            this[0x0E] = EntityRemovePacket.Reader
            this[0x0F] = ItemAddPacket.Reader
            this[0x11] = ItemTakePacket.Reader
            this[0x12] = EntityTeleportPacket.Reader
            this[0x13] = PlayerLocationPacket.Reader
            this[0x14] = RiderJumpPacket.Reader
            this[0x15] = BlockUpdatePacket.Reader
            this[0x16] = PaintingAddPacket.Reader
            this[0x17] = TickSyncPacket.Reader
            this[0x18] = SoundEventPacketV1.Reader
            this[0x19] = WorldEventPacket.Reader
            this[0x1A] = BlockEventPacket.Reader
            this[0x1B] = EntityEventPacket.Reader
            this[0x1C] = EntityEffectPacket.Reader
            this[0x1D] = EntityAttributesPacket.Reader
            this[0x1E] = InventoryTransactionPacket.Reader
            this[0x1F] = EntityEquipmentPacket.Reader
            this[0x20] = EntityArmorPacket.Reader
            this[0x21] = InteractPacket.Reader
            this[0x22] = BlockPickPacket.Reader
            this[0x23] = EntityPickPacket.Reader
            this[0x24] = PlayerActionPacket.Reader
            this[0x25] = EntityFallPacket.Reader
            this[0x26] = ArmorDamagePacket.Reader
            this[0x27] = EntityMetadataPacket.Reader
            this[0x28] = EntityVelocityPacket.Reader
            this[0x29] = EntityLinkPacket.Reader
            this[0x2A] = HealthPacket.Reader
            this[0x2B] = SpawnPositionPacket.Reader
            this[0x2C] = EntityAnimationPacket.Reader
            this[0x2D] = RespawnPacket.Reader
            this[0x2E] = WindowOpenPacket.Reader
            this[0x2F] = WindowClosePacket.Reader
            this[0x30] = HotbarPacket.Reader
            this[0x31] = InventoryContentPacket.Reader
            this[0x32] = InventorySlotPacket.Reader
            this[0x33] = WindowPropertyPacket.Reader
            this[0x34] = RecipesPacket.Reader
            this[0x35] = CraftingEventPacket.Reader
            this[0x37] = AdventureSettingsPacket.Reader
            this[0x38] = BlockEntityPacket.Reader
            this[0x39] = SteerPacket.Reader
            this[0x3A] = ChunkPacket.Reader
            this[0x3B] = CommandSettingsPacket.Reader
            this[0x3C] = DifficultyPacket.Reader
            this[0x3D] = DimensionPacket.Reader
            this[0x3E] = GameModePacket.Reader
            this[0x3F] = PlayerListPacket.Reader
            this[0x40] = SimpleEventPacket.Reader
            this[0x42] = ExperienceOrbAddPacket.Reader
            this[0x44] = MapRequestPacket.Reader
            this[0x45] = ViewDistanceRequestPacket.Reader
            this[0x46] = ViewDistancePacket.Reader
            this[0x47] = ItemFrameDropItemPacket.Reader
            this[0x48] = GameRulesPacket.Reader
            this[0x49] = CameraPacket.Reader
            this[0x4A] = BossBarPacket.Reader
            this[0x4B] = ShowCreditsPacket.Reader
            this[0x4C] = CommandsPacket.Reader
            this[0x4D] = CommandPacket.Reader
            this[0x4E] = CommandBlockUpdatePacket.Reader
            this[0x4F] = CommandResponsePacket.Reader
            this[0x50] = TradePacket.Reader
            this[0x51] = EquipmentPacket.Reader
            this[0x52] = PackDataPacket.Reader
            this[0x53] = PackDataChunkPacket.Reader
            this[0x54] = PackDataChunkRequestPacket.Reader
            this[0x55] = TransferPacket.Reader
            this[0x56] = SoundPacket.Reader
            this[0x57] = SoundStopPacket.Reader
            this[0x58] = TitlePacket.Reader
            this[0x59] = BehaviorTreePacket.Reader
            this[0x5A] = StructureBlockUpdatePacket.Reader
            this[0x5B] = ShowStoreOfferPacket.Reader
            this[0x5C] = PurchaseReceiptPacket.Reader
            this[0x5D] = AppearancePacket.Reader
            this[0x5E] = SubLoginPacket.Reader
            this[0x5F] = AutomationPacket.Reader
            this[0x60] = LastHurtByPacket.Reader
            this[0x61] = BookEditPacket.Reader
            this[0x62] = NpcRequestPacket.Reader
            this[0x63] = PhotoPacket.Reader
            this[0x64] = FormPacket.Reader
            this[0x65] = FormResponsePacket.Reader
            this[0x66] = ServerSettingsRequestPacket.Reader
            this[0x67] = ServerSettingsPacket.Reader
            this[0x68] = ShowProfilePacket.Reader
            this[0x69] = DefaultGameModePacket.Reader
            this[0x6A] = ObjectiveRemovePacket.Reader
            this[0x6B] = ObjectiveSetPacket.Reader
            this[0x6C] = ScoresPacket.Reader
            this[0x6D] = LabTablePacket.Reader
            this[0x6E] = BlockUpdateSyncedPacket.Reader
            this[0x6F] = EntityMoveRotatePacket.Reader
            this[0x70] = ScoreboardIdentityPacket.Reader
            this[0x71] = LocalPlayerAsInitializedPacket.Reader
            this[0x72] = CommandSoftEnumerationPacket.Reader
            this[0x73] = LatencyPacket.Reader
            this[0x75] = CustomEventPacket.Reader
            this[0x76] = ParticlePacket.Reader
            this[0x77] = EntityIdentifiersPacket.Reader
            this[0x78] = SoundEventPacketV2.Reader
            this[0x79] = ChunkPublishPacket.Reader
            this[0x7A] = BiomeDefinitionsPacket.Reader
            this[0x7B] = SoundEventPacket.Reader
            this[0x7C] = WorldGenericEventPacket.Reader
            this[0x7D] = LecternUpdatePacket.Reader
            this[0x7E] = VideoStreamPacket.Reader
            this[0x81] = CacheStatusPacket.Reader
            this[0x82] = OnScreenTextureAnimationPacket.Reader
            this[0x83] = MapCreateLockedCopyPacket.Reader
            this[0x84] = StructureTemplateDataExportRequestPacket.Reader
            this[0x85] = StructureTemplateDataExportResponsePacket.Reader
            this[0x86] = BlockComponentPacket.Reader
            this[0x87] = CacheBlobStatusPacket.Reader
            this[0x88] = CacheBlobsPacket.Reader
            this[0x8A] = EmotePacket.Reader
            this[0x8B] = MultiplayerSettingsPacket.Reader
            this[0x8C] = SettingsCommandPacket.Reader
            this[0x8D] = AnvilDamagePacket.Reader
            this[0x8E] = ItemActionPacket.Reader
            this[0x8F] = NetworkSettingsPacket.Reader
            this[0x90] = InputPacket.Reader
            this[0x91] = CreativeInventoryPacket.Reader
            this[0x92] = EnchantOptionsPacket.Reader
            //this[0x93] = InventoryRequestPacket.Reader
            //this[0x94] = InventoryResponsePacket.Reader
            this[0x95] = PlayerArmorDamagePacket.Reader
            this[0x96] = CodeBuilderPacket.Reader
            this[0x97] = PlayerGameModePacket.Reader
            this[0x98] = EmotesPacket.Reader
            this[0x99] = PositionTrackingDbServerBroadcastPacket.Reader
            this[0x9A] = PositionTrackingDbClientRequestPacket.Reader
            this[0x9B] = DebugPacket.Reader
            this[0x9C] = ViolationPacket.Reader
            this[0x9D] = VelocityPredictionPacket.Reader
            this[0x9E] = EntityAnimatePacket.Reader
            this[0x9F] = CameraShakePacket.Reader
            this[0xA0] = FogPacket.Reader
            this[0xA1] = InputCorrectPacket.Reader
            this[0xA2] = ItemComponentPacket.Reader
            this[0xA3] = FilterPacket.Reader
            this[0xA4] = DebugOverlayPacket.Reader
            this[0xA5] = EntityPropertiesPacket.Reader
            //this[0xA6] = VolumeEntityAddPacket.Reader
            //this[0xA7] = VolumeEntityRemovePacket.Reader
            this[0xA8] = SimulationPacket.Reader
            this[0xA9] = NpcDialoguePacket.Reader
            this[0xAA] = EducationUriResourcePacket.Reader
            this[0xAB] = PhotoItemPacket.Reader
            this[0xAC] = BlockUpdatesSyncedPacket.Reader
            this[0xAD] = PhotoRequestPacket.Reader
            this[0xAE] = SubChunkPacket.Reader
            this[0xAF] = SubChunkRequestPacket.Reader
            //this[0xB0] = ItemCooldownPacket.Reader
            //this[0xB1] = ScriptMessagePacket.Reader
            //this[0xB2] = CodeBuilderSourcePacket.Reader
            //this[0xB3] = TickingAreasLoadStatusPacket
            this[0xB4] = DimensionsPacket.Reader
            //this[0xB5] = AgentActionEventPacket
            this[0xB6] = EntityPropertyPacket.Reader
            this[0xB7] = LessonProgressPacket.Reader
            this[0xB8] = AbilityPacket.Reader
            this[0xB9] = PermissionsPacket.Reader
            this[0xBA] = ToastPacket.Reader
            this[0xBB] = AbilitiesPacket.Reader
            this[0xBC] = AdventureSettingsWithoutAbilitiesPacket.Reader
            this[0xBD] = DeathPacket.Reader
            //this[0xBE] = EditorNetworkPacket.Reader
        }
        private val clientReaders = readers.filterValues { it::class.java.getMethod("read", PacketBuffer::class.java, Int::class.java).returnType.kotlin.findAnnotation<Restrict>()?.value?.contains(Restriction.ToClient) ?: true }
        private val serverReaders = readers.filterValues { it::class.java.getMethod("read", PacketBuffer::class.java, Int::class.java).returnType.kotlin.findAnnotation<Restrict>()?.value?.contains(Restriction.ToServer) ?: true }
    }
}
