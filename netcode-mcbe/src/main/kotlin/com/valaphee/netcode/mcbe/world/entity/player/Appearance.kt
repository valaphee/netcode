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

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.util.safeList

/**
 * @author Kevin Ludwig
 */
data class Appearance constructor(
    val skinId: String,
    val playFabId: String?,
    val skinResourcePatch: ByteArray,
    /*@JsonUnwrapped(prefix = "Skin")*/@JsonIgnore val skinImage: AppearanceImage,
    val animations: List<Animation>,
    /*@JsonUnwrapped(prefix = "Cape")*/@JsonIgnore val capeImage: AppearanceImage,
    val geometryData: ByteArray,
    val geometryDataEngineVersion: String?,
    val animationData: ByteArray,
    val capeId: String,
    @JsonIgnore var id: String?,
    val armSize: String,
    val skinColor: String,
    val personaPieces: List<PersonaPiece>,
    val personaPieceTints: List<PersonaPieceTint>,
    val premiumSkin: Boolean,
    val personaSkin: Boolean,
    val capeOnClassicSkin: Boolean,
    @JsonIgnore val primaryUser: Boolean,
    @JsonIgnore var trusted: Boolean
) {
    data class Animation constructor(
        @JsonIgnore val image: AppearanceImage,
        val type: Type,
        val frames: Float,
        val expression: Expression = Expression.Linear
    ) {
        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        enum class Type {
            None, Head, Body32, Body128
        }

        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        enum class Expression {
            Linear, Blinking
        }

        val imageWidth get() = image.width
        val imageHeight get() = image.height
        val imageData get() = image.data

        @JsonCreator
        constructor(
            @JsonProperty("ImageWidth") imageWidth: Int?,
            @JsonProperty("ImageHeight") imageHeight: Int?,
            @JsonProperty("Image") imageData: ByteArray,
            @JsonProperty("Type") type: Type,
            @JsonProperty("Frames") frames: Float,
            @JsonProperty("AnimationExpression") expression: Expression = Expression.Linear
        ) : this(AppearanceImage(imageWidth, imageHeight, imageData), type, frames, expression)
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

    val skinWidth get() = skinImage.width
    val skinHeight get() = skinImage.height
    val skinData get() = skinImage.data
    val capeWidth get() = capeImage.width
    val capeHeight get() = capeImage.height
    val capeData get() = capeImage.data

    @JsonCreator
    constructor(
        @JsonProperty("SkinId") skinId: String,
        @JsonProperty("PlayFabId") playFabId: String?,
        @JsonProperty("SkinResourcePatch") skinResourcePatch: ByteArray,
        @JsonProperty("SkinImageWidth") skinWidth: Int?,
        @JsonProperty("SkinImageHeight") skinHeight: Int?,
        @JsonProperty("SkinData") skinData: ByteArray,
        @JsonProperty("AnimatedImageData") animations: List<Animation>,
        @JsonProperty("CapeImageWidth") capeWidth: Int?,
        @JsonProperty("CapeImageHeight") capeHeight: Int?,
        @JsonProperty("CapeData") capeData: ByteArray,
        @JsonProperty("SkinGeometryData") geometryData: ByteArray,
        @JsonProperty("SkinGeometryDataEngineVersion") geometryDataEngineVersion: String?,
        @JsonProperty("SkinAnimationData") animationData: ByteArray,
        @JsonProperty("CapeId") capeId: String,
        @JsonProperty("ArmSize") armSize: String = "",
        @JsonProperty("SkinColor") skinColor: String = "#0",
        @JsonProperty("PersonaPieces") personaPieces: List<PersonaPiece> = emptyList(),
        @JsonProperty("PieceTintColors") personaPieceTints: List<PersonaPieceTint> = emptyList(),
        @JsonProperty("PremiumSkin") premiumSkin: Boolean,
        @JsonProperty("PersonaSkin") personaSkin: Boolean,
        @JsonProperty("CapeOnClassicSkin") capeOnClassicSkin: Boolean
    ) : this(skinId, playFabId, skinResourcePatch, AppearanceImage(skinWidth, skinHeight, skinData), animations, AppearanceImage(capeWidth, capeHeight, capeData), geometryData, geometryDataEngineVersion, animationData, capeId, null, armSize, skinColor, personaPieces, personaPieceTints, premiumSkin, personaSkin, capeOnClassicSkin, false, false)
}

fun PacketBuffer.readAppearancePre390(): Appearance {
    val skinId = readString()
    val skinResourcePatch = readByteArray()
    val skinImage = readAppearanceImage()
    val animations = mutableListOf<Appearance.Animation>().apply { repeat(readIntLE()) { add(Appearance.Animation(readAppearanceImage(), Appearance.Animation.Type.values()[readIntLE()], buffer.readFloatLE())) } }
    val capeImage = readAppearanceImage()
    val geometryData = readByteArray()
    val animationData = readByteArray()
    val premiumSkin = readBoolean()
    val personaSkin = readBoolean()
    val capeOnClassicSkin = readBoolean()
    val capeId = readString()
    val id = readString()
    return Appearance(skinId, "", skinResourcePatch, skinImage, animations, capeImage, geometryData, "", animationData, capeId, id, "", "", emptyList(), emptyList(), premiumSkin, personaSkin, capeOnClassicSkin, false, false)
}

fun PacketBuffer.readAppearancePre419(): Appearance {
    val skinId = readString()
    val skinResourcePatch = readByteArray()
    val skinImage = readAppearanceImage()
    val animations = mutableListOf<Appearance.Animation>().apply { repeat(readIntLE()) { add(Appearance.Animation(readAppearanceImage(), Appearance.Animation.Type.values()[buffer.readIntLE()], buffer.readFloatLE())) } }
    val capeImage = readAppearanceImage()
    val geometryData = readByteArray()
    val animationData = readByteArray()
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
    val skinResourcePatch = readByteArray()
    val skinImage = readAppearanceImage()
    val animations = mutableListOf<Appearance.Animation>().apply { repeat(readIntLE()) { add(Appearance.Animation(readAppearanceImage(), Appearance.Animation.Type.values()[buffer.readIntLE()], buffer.readFloatLE(), Appearance.Animation.Expression.values()[buffer.readIntLE()])) } }
    val capeImage = readAppearanceImage()
    val geometryData = readByteArray()
    val animationData = readByteArray()
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
    val skinResourcePatch = readByteArray()
    val skinImage = readAppearanceImage()
    val animations = mutableListOf<Appearance.Animation>().apply { repeat(readIntLE()) { add(Appearance.Animation(readAppearanceImage(), Appearance.Animation.Type.values()[buffer.readIntLE()], buffer.readFloatLE(), Appearance.Animation.Expression.values()[buffer.readIntLE()])) } }
    val capeImage = readAppearanceImage()
    val geometryData = readByteArray()
    val animationData = readByteArray()
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
    readByteArray(),
    readAppearanceImage(),
    mutableListOf<Appearance.Animation>().apply { repeat(readIntLE()) { add(Appearance.Animation(readAppearanceImage(), Appearance.Animation.Type.values()[buffer.readIntLE()], buffer.readFloatLE(), Appearance.Animation.Expression.values()[buffer.readIntLE()])) } },
    readAppearanceImage(),
    readByteArray(),
    readString(),
    readByteArray(),
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
    writeByteArray(value.skinResourcePatch)
    writeAppearanceImage(value.skinImage)
    writeIntLE(value.animations.size)
    value.animations.forEach {
        writeAppearanceImage(it.image)
        writeIntLE(it.type.ordinal)
        writeFloatLE(it.frames)
    }
    writeAppearanceImage(value.capeImage)
    writeByteArray(value.geometryData)
    writeByteArray(value.animationData)
    writeBoolean(value.premiumSkin)
    writeBoolean(value.personaSkin)
    writeBoolean(value.capeOnClassicSkin)
    writeString(value.capeId)
    writeString(value.id!!)
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
    writeByteArray(value.skinResourcePatch)
    writeAppearanceImage(value.skinImage)
    writeIntLE(value.animations.size)
    value.animations.forEach {
        writeAppearanceImage(it.image)
        writeIntLE(it.type.ordinal)
        writeFloatLE(it.frames)
        writeIntLE(it.expression.ordinal)
    }
    writeAppearanceImage(value.capeImage)
    writeByteArray(value.geometryData)
    writeByteArray(value.animationData)
    writeBoolean(value.premiumSkin)
    writeBoolean(value.personaSkin)
    writeBoolean(value.capeOnClassicSkin)
    writeString(value.capeId)
    writeString(value.id!!)
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
    writeString(value.playFabId ?: "")
    writeByteArray(value.skinResourcePatch)
    writeAppearanceImage(value.skinImage)
    writeIntLE(value.animations.size)
    value.animations.forEach {
        writeAppearanceImage(it.image)
        writeIntLE(it.type.ordinal)
        writeFloatLE(it.frames)
        writeIntLE(it.expression.ordinal)
    }
    writeAppearanceImage(value.capeImage)
    writeByteArray(value.geometryData)
    writeByteArray(value.animationData)
    writeBoolean(value.premiumSkin)
    writeBoolean(value.personaSkin)
    writeBoolean(value.capeOnClassicSkin)
    writeString(value.capeId)
    writeString(value.id!!)
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
    writeString(value.playFabId ?: "")
    writeByteArray(value.skinResourcePatch)
    writeAppearanceImage(value.skinImage)
    writeIntLE(value.animations.size)
    value.animations.forEach {
        writeAppearanceImage(it.image)
        writeIntLE(it.type.ordinal)
        writeFloatLE(it.frames)
        writeIntLE(it.expression.ordinal)
    }
    writeAppearanceImage(value.capeImage)
    writeByteArray(value.geometryData)
    writeString(value.geometryDataEngineVersion ?: "")
    writeByteArray(value.animationData)
    writeString(value.capeId)
    writeString(value.id!!)
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
