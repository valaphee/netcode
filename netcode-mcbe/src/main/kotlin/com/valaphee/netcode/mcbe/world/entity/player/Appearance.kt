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

package com.valaphee.netcode.mcbe.world.entity.player

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonUnwrapped
import com.fasterxml.jackson.module.kotlin.readValue
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.util.safeList

/**
 * @author Kevin Ludwig
 */
data class Appearance constructor(
    @get:JsonProperty("SkinId") val skinId: String,
    @get:JsonProperty("PlayFabId") val playFabId: String,
    @get:JsonProperty("SkinResourcePatch") val skinResourcePatch: Map<String, Any>,
    @get:JsonUnwrapped(prefix = "Skin") val skinImage: AppearanceImage,
    @get:JsonProperty("AnimatedImageData") val animations: List<Animation>,
    @get:JsonUnwrapped(prefix = "Cape") val capeImage: AppearanceImage,
    @get:JsonProperty("SkinGeometryData") val geometryData: String,
    @get:JsonProperty("SkinGeometryDataEngineVersion") val geometryDataEngineVersion: String,
    @get:JsonProperty("SkinAnimationData") val animationData: String,
    @get:JsonProperty("CapeId") val capeId: String,
    @get:JsonIgnore var id: String,
    @get:JsonProperty("ArmSize") val armSize: String,
    @get:JsonProperty("SkinColor") val skinColor: String,
    @get:JsonProperty("PersonaPieces") val personaPieces: List<PersonaPiece>,
    @get:JsonProperty("PieceTintColors") val personaPieceTints: List<PersonaPieceTint>,
    @get:JsonProperty("PremiumSkin") val premiumSkin: Boolean,
    @get:JsonProperty("PersonaSkin") val personaSkin: Boolean,
    @get:JsonProperty("CapeOnClassicSkin") val capeOnClassicSkin: Boolean,
    @get:JsonIgnore val primaryUser: Boolean,
    @get:JsonIgnore var trusted: Boolean,
) {
    data class Animation constructor(
        @get:JsonUnwrapped val image: AppearanceImage,
        @get:JsonProperty("Type") val type: Type,
        @get:JsonProperty("Frames") val frames: Float,
        @get:JsonProperty("AnimationExpression") val expression: Expression = Expression.Linear
    ) {
        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        enum class Type {
            None, Head, Body32, Body128
        }

        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        enum class Expression {
            Linear, Blinking
        }
    }

    data class PersonaPiece(
        @get:JsonProperty("PieceId") val id: String,
        @get:JsonProperty("PieceType") val type: String,
        @get:JsonProperty("PackId") val packId: String,
        @get:JsonProperty("IsDefault") val default: Boolean,
        @get:JsonProperty("ProductId") val productId: String
    )

    data class PersonaPieceTint(
        @get:JsonProperty("PieceType") val type: String,
        @get:JsonProperty("Colors") val colors: List<String>
    )
}

fun PacketBuffer.readAppearancePre390(): Appearance {
    val skinId = readString()
    val skinResourcePatch = jsonObjectMapper.readValue<Map<String, Any>>(readString())
    val skinImage = readAppearanceImage()
    val animations = mutableListOf<Appearance.Animation>().apply { repeat(readIntLE()) { add(Appearance.Animation(readAppearanceImage(), Appearance.Animation.Type.values()[readIntLE()], buffer.readFloatLE())) } }
    val capeImage = readAppearanceImage()
    val geometryData = readString()
    val animationData = readString()
    val premiumSkin = readBoolean()
    val personaSkin = readBoolean()
    val capeOnClassicSkin = readBoolean()
    val capeId = readString()
    val id = readString()
    return Appearance(skinId, "", skinResourcePatch, skinImage, animations, capeImage, geometryData, "", animationData, capeId, id, "", "", emptyList(), emptyList(), premiumSkin, personaSkin, capeOnClassicSkin, false, false)
}

fun PacketBuffer.readAppearancePre419(): Appearance {
    val skinId = readString()
    val skinResourcePatch = jsonObjectMapper.readValue<Map<String, Any>>(readString())
    val skinImage = readAppearanceImage()
    val animations = mutableListOf<Appearance.Animation>().apply { repeat(readIntLE()) { add(Appearance.Animation(readAppearanceImage(), Appearance.Animation.Type.values()[buffer.readIntLE()], buffer.readFloatLE())) } }
    val capeImage = readAppearanceImage()
    val geometryData = readString()
    val animationData = readString()
    val premiumSkin = readBoolean()
    val personaSkin = readBoolean()
    val capeOnClassicSkin = readBoolean()
    val capeId = readString()
    val id = readString()
    val armSize = readString()
    val skinColor = readString()
    val personaPieces = mutableListOf<Appearance.PersonaPiece>().apply { repeat(readIntLE()) { add(Appearance.PersonaPiece(readString(), readString(), readString(), readBoolean(), readString())) } }
    val personaPieceTints = mutableListOf<Appearance.PersonaPieceTint>().apply { repeat(readIntLE()) { add(Appearance.PersonaPieceTint(readString(), safeList(readIntLE()) { readString() })) } }
    return Appearance(skinId, "", skinResourcePatch, skinImage, animations, capeImage, geometryData, "", animationData, capeId, id, armSize, skinColor, personaPieces, personaPieceTints, premiumSkin, personaSkin, capeOnClassicSkin, false, false)
}

fun PacketBuffer.readAppearancePre428(): Appearance {
    val skinId = readString()
    val skinResourcePatch = jsonObjectMapper.readValue<Map<String, Any>>(readString())
    val skinImage = readAppearanceImage()
    val animations = mutableListOf<Appearance.Animation>().apply { repeat(readIntLE()) { add(Appearance.Animation(readAppearanceImage(), Appearance.Animation.Type.values()[buffer.readIntLE()], buffer.readFloatLE(), Appearance.Animation.Expression.values()[buffer.readIntLE()])) } }
    val capeImage = readAppearanceImage()
    val geometryData = readString()
    val animationData = readString()
    val premiumSkin = readBoolean()
    val personaSkin = readBoolean()
    val capeOnClassicSkin = readBoolean()
    val capeId = readString()
    val id = readString()
    val armSize = readString()
    val skinColor = readString()
    val personaPieces = mutableListOf<Appearance.PersonaPiece>().apply { repeat(readIntLE()) { add(Appearance.PersonaPiece(readString(), readString(), readString(), readBoolean(), readString())) } }
    val personaPieceTints = mutableListOf<Appearance.PersonaPieceTint>().apply { repeat(readIntLE()) { add(Appearance.PersonaPieceTint(readString(), safeList(readIntLE()) { readString() })) } }
    return Appearance(skinId, "", skinResourcePatch, skinImage, animations, capeImage, geometryData, "", animationData, capeId, id, armSize, skinColor, personaPieces, personaPieceTints, premiumSkin, personaSkin, capeOnClassicSkin, false, false)
}

fun PacketBuffer.readAppearancePre465(): Appearance {
    val skinId = readString()
    val playFabId = readString()
    val skinResourcePatch = jsonObjectMapper.readValue<Map<String, Any>>(readString())
    val skinImage = readAppearanceImage()
    val animations = mutableListOf<Appearance.Animation>().apply { repeat(readIntLE()) { add(Appearance.Animation(readAppearanceImage(), Appearance.Animation.Type.values()[buffer.readIntLE()], buffer.readFloatLE(), Appearance.Animation.Expression.values()[buffer.readIntLE()])) } }
    val capeImage = readAppearanceImage()
    val geometryData = readString()
    val animationData = readString()
    val premiumSkin = readBoolean()
    val personaSkin = readBoolean()
    val capeOnClassicSkin = readBoolean()
    val capeId = readString()
    val id = readString()
    val armSize = readString()
    val skinColor = readString()
    val personaPieces = mutableListOf<Appearance.PersonaPiece>().apply { repeat(readIntLE()) { add(Appearance.PersonaPiece(readString(), readString(), readString(), readBoolean(), readString())) } }
    val personaPieceTints = mutableListOf<Appearance.PersonaPieceTint>().apply { repeat(readIntLE()) { add(Appearance.PersonaPieceTint(readString(), safeList(readIntLE()) { readString() })) } }
    return Appearance(skinId, playFabId, skinResourcePatch, skinImage, animations, capeImage, geometryData, "", animationData, capeId, id, armSize, skinColor, personaPieces, personaPieceTints, premiumSkin, personaSkin, capeOnClassicSkin, false, false)
}

fun PacketBuffer.readAppearance() = Appearance(
    readString(),
    readString(),
    jsonObjectMapper.readValue(readString()),
    readAppearanceImage(),
    mutableListOf<Appearance.Animation>().apply { repeat(readIntLE()) { add(Appearance.Animation(readAppearanceImage(), Appearance.Animation.Type.values()[buffer.readIntLE()], buffer.readFloatLE(), Appearance.Animation.Expression.values()[buffer.readIntLE()])) } },
    readAppearanceImage(),
    readString(),
    readString(),
    readString(),
    readString(),
    readString(),
    readString(),
    readString(),
    mutableListOf<Appearance.PersonaPiece>().apply { repeat(readIntLE()) { add(Appearance.PersonaPiece(readString(), readString(), readString(), readBoolean(), readString())) } },
    mutableListOf<Appearance.PersonaPieceTint>().apply { repeat(readIntLE()) { add(Appearance.PersonaPieceTint(readString(), safeList(readIntLE()) { readString() })) } },
    readBoolean(),
    readBoolean(),
    readBoolean(),
    readBoolean(),
    false
)

fun PacketBuffer.writeAppearancePre390(value: Appearance) {
    writeString(value.skinId)
    writeString(jsonObjectMapper.writeValueAsString(value.skinResourcePatch))
    writeAppearanceImage(value.skinImage)
    writeIntLE(value.animations.size)
    value.animations.forEach {
        writeAppearanceImage(it.image)
        writeIntLE(it.type.ordinal)
        writeFloatLE(it.frames)
    }
    writeAppearanceImage(value.capeImage)
    writeString(value.geometryData)
    writeString(value.animationData)
    writeBoolean(value.premiumSkin)
    writeBoolean(value.personaSkin)
    writeBoolean(value.capeOnClassicSkin)
    writeString(value.capeId)
    writeString(value.id)
}

fun PacketBuffer.writeAppearancePre419(value: Appearance) {
    writeAppearancePre390(value)
    writeString(value.armSize)
    writeString(value.skinColor)
    writeIntLE(value.personaPieces.size)
    value.personaPieces.forEach {
        writeString(it.id)
        writeString(it.type)
        writeString(it.packId)
        writeBoolean(it.default)
        writeString(it.productId)
    }
    writeIntLE(value.personaPieceTints.size)
    value.personaPieceTints.forEach {
        writeString(it.type)
        val personaPieceTintColors = it.colors
        writeIntLE(personaPieceTintColors.size)
        personaPieceTintColors.forEach { writeString(it) }
    }
}

fun PacketBuffer.writeAppearancePre428(value: Appearance) {
    writeString(value.skinId)
    writeString(jsonObjectMapper.writeValueAsString(value.skinResourcePatch))
    writeAppearanceImage(value.skinImage)
    writeIntLE(value.animations.size)
    value.animations.forEach {
        writeAppearanceImage(it.image)
        writeIntLE(it.type.ordinal)
        writeFloatLE(it.frames)
        writeIntLE(it.expression.ordinal)
    }
    writeAppearanceImage(value.capeImage)
    writeString(value.geometryData)
    writeString(value.animationData)
    writeBoolean(value.premiumSkin)
    writeBoolean(value.personaSkin)
    writeBoolean(value.capeOnClassicSkin)
    writeString(value.capeId)
    writeString(value.id)
    writeString(value.armSize)
    writeString(value.skinColor)
    writeIntLE(value.personaPieces.size)
    value.personaPieces.forEach {
        writeString(it.id)
        writeString(it.type)
        writeString(it.packId)
        writeBoolean(it.default)
        writeString(it.productId)
    }
    writeIntLE(value.personaPieceTints.size)
    value.personaPieceTints.forEach {
        writeString(it.type)
        val personaPieceTintColors = it.colors
        writeIntLE(personaPieceTintColors.size)
        personaPieceTintColors.forEach { writeString(it) }
    }
}

fun PacketBuffer.writeAppearancePre465(value: Appearance) {
    writeString(value.skinId)
    writeString(value.playFabId)
    writeString(jsonObjectMapper.writeValueAsString(value.skinResourcePatch))
    writeAppearanceImage(value.skinImage)
    writeIntLE(value.animations.size)
    value.animations.forEach {
        writeAppearanceImage(it.image)
        writeIntLE(it.type.ordinal)
        writeFloatLE(it.frames)
        writeIntLE(it.expression.ordinal)
    }
    writeAppearanceImage(value.capeImage)
    writeString(value.geometryData)
    writeString(value.animationData)
    writeBoolean(value.premiumSkin)
    writeBoolean(value.personaSkin)
    writeBoolean(value.capeOnClassicSkin)
    writeString(value.capeId)
    writeString(value.id)
    writeString(value.armSize)
    writeString(value.skinColor)
    writeIntLE(value.personaPieces.size)
    value.personaPieces.forEach {
        writeString(it.id)
        writeString(it.type)
        writeString(it.packId)
        writeBoolean(it.default)
        writeString(it.productId)
    }
    writeIntLE(value.personaPieceTints.size)
    value.personaPieceTints.forEach {
        writeString(it.type)
        val personaPieceTintColors = it.colors
        writeIntLE(personaPieceTintColors.size)
        personaPieceTintColors.forEach { writeString(it) }
    }
}

fun PacketBuffer.writeAppearance(value: Appearance) {
    writeString(value.skinId)
    writeString(value.playFabId)
    writeString(jsonObjectMapper.writeValueAsString(value.skinResourcePatch))
    writeAppearanceImage(value.skinImage)
    writeIntLE(value.animations.size)
    value.animations.forEach {
        writeAppearanceImage(it.image)
        writeIntLE(it.type.ordinal)
        writeFloatLE(it.frames)
        writeIntLE(it.expression.ordinal)
    }
    writeAppearanceImage(value.capeImage)
    writeString(value.geometryData)
    writeString(value.geometryDataEngineVersion)
    writeString(value.animationData)
    writeString(value.capeId)
    writeString(value.id)
    writeString(value.armSize)
    writeString(value.skinColor)
    writeIntLE(value.personaPieces.size)
    value.personaPieces.forEach {
        writeString(it.id)
        writeString(it.type)
        writeString(it.packId)
        writeBoolean(it.default)
        writeString(it.productId)
    }
    writeIntLE(value.personaPieceTints.size)
    value.personaPieceTints.forEach {
        writeString(it.type)
        val personaPieceTintColors = it.colors
        writeIntLE(personaPieceTintColors.size)
        personaPieceTintColors.forEach { writeString(it) }
    }
    writeBoolean(value.premiumSkin)
    writeBoolean(value.personaSkin)
    writeBoolean(value.capeOnClassicSkin)
    writeBoolean(value.primaryUser)
}
