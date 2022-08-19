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

import com.valaphee.foundry.math.Float2
import com.valaphee.netcode.mcje.network.Packet
import com.valaphee.netcode.mcje.network.PacketBuffer
import com.valaphee.netcode.mcje.network.ServerPlayPacketHandler
import com.valaphee.netcode.mcje.util.NamespacedKey
import com.valaphee.netcode.mcje.world.item.ItemStack
import com.valaphee.netcode.mcje.world.item.readItemStack
import com.valaphee.netcode.mcje.world.item.writeItemStack
import com.valaphee.netcode.util.LazyList
import net.kyori.adventure.text.Component

/**
 * @author Kevin Ludwig
 */
class ServerAdvancementsPacket(
    val clean: Boolean,
    val add: Map<NamespacedKey, Advacement>,
    val remove: List<NamespacedKey>,
    val progress: Map<NamespacedKey, Map<NamespacedKey, Long?>>
) : Packet<ServerPlayPacketHandler>() {
    data class Advacement(
        val parent: NamespacedKey?,
        val display: Display?,
        val criteria: List<NamespacedKey>,
        val requirements: List<List<String>>
    ) {
        data class Display(
            val title: Component,
            val description: Component,
            val icon: ItemStack?,
            val frame: Frame,
            val background: NamespacedKey?,
            val toast: Boolean,
            val hidden: Boolean,
            val position: Float2
        ) {
            enum class Frame {
                Task, Challenge, Goal
            }
        }
    }

    override fun write(buffer: PacketBuffer, version: Int) {
        buffer.writeBoolean(clean)
        buffer.writeVarInt(add.size)
        add.forEach {
            buffer.writeNamespacedKey(it.key)
            it.value.parent?.let {
                buffer.writeBoolean(true)
                buffer.writeNamespacedKey(it)
            } ?: buffer.writeBoolean(false)
            it.value.display?.let {
                buffer.writeBoolean(true)
                buffer.writeComponent(it.title)
                buffer.writeComponent(it.description)
                buffer.writeItemStack(it.icon, version)
                it.background?.let {
                    buffer.writeNamespacedKey(it)
                }
                buffer.writeFloat2(it.position)
            } ?: buffer.writeBoolean(false)
            buffer.writeVarInt(it.value.criteria.size)
            it.value.criteria.forEach(buffer::writeNamespacedKey)
            buffer.writeVarInt(it.value.requirements.size)
            it.value.requirements.forEach {
                buffer.writeVarInt(it.size)
                it.forEach(buffer::writeString)
            }
        }
        buffer.writeVarInt(remove.size)
        remove.forEach { buffer.writeNamespacedKey(it) }
        buffer.writeVarInt(progress.size)
        progress.forEach {
            buffer.writeNamespacedKey(it.key)
            buffer.writeVarInt(it.value.size)
            it.value.forEach {
                buffer.writeNamespacedKey(it.key)
                it.value?.let {
                    buffer.writeBoolean(true)
                    buffer.writeLong(it)
                } ?: buffer.writeBoolean(false)
            }
        }
    }

    override fun handle(handler: ServerPlayPacketHandler) = handler.advancements(this)

    override fun toString() = "ServerAdvancementsPacket(clean=$clean, add=$add, remove=$remove, progress=$progress)"

    object Reader : Packet.Reader {
        override fun read(buffer: PacketBuffer, version: Int) = ServerAdvancementsPacket(buffer.readBoolean(), mutableMapOf<NamespacedKey, Advacement>().apply {
            repeat(buffer.readVarInt()) {
                this[buffer.readNamespacedKey()] = Advacement(if (buffer.readBoolean()) buffer.readNamespacedKey() else null, if (buffer.readBoolean()) {
                    val title = buffer.readComponent()
                    val description = buffer.readComponent()
                    val icon = buffer.readItemStack(version)
                    val frame = Advacement.Display.Frame.values()[buffer.readVarInt()]
                    val background = buffer.readNamespacedKey()
                    val toast = false
                    val hidden = false
                    val position = buffer.readFloat2()
                    Advacement.Display(title, description, icon, frame, background, toast, hidden, position)
                } else null, LazyList(buffer.readVarInt()) { buffer.readNamespacedKey() }, LazyList(buffer.readVarInt()) { LazyList(buffer.readVarInt()) { buffer.readString() } })
            }
        }, LazyList(buffer.readVarInt()) { buffer.readNamespacedKey()}, mutableMapOf<NamespacedKey, Map<NamespacedKey, Long?>>().apply { repeat(buffer.readVarInt()) { this[buffer.readNamespacedKey()] = mutableMapOf<NamespacedKey, Long?>().apply { repeat(buffer.readVarInt()) { this[buffer.readNamespacedKey()] = if (buffer.readBoolean()) buffer.readLong() else null } } } })
    }
}
