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
import com.valaphee.netcode.mcbe.network.V1_16_100
import com.valaphee.netcode.mcbe.network.V1_16_210
import com.valaphee.netcode.mcbe.network.V1_17_034
import com.valaphee.netcode.util.LazyList

/**
 * @author Kevin Ludwig
 */
data class Appearance(
    val skinId: String,
    val playFabId: String?,
    val skinResourcePatch: ByteArray,
    /*@JsonUnwrapped(prefix = "Skin")*/@get:JsonIgnore val skinImage: AppearanceImage,
    val animations: List<Animation>,
    /*@JsonUnwrapped(prefix = "Cape")*/@get:JsonIgnore val capeImage: AppearanceImage,
    val geometryData: ByteArray,
    val geometryDataEngineVersion: String?,
    val animationData: ByteArray,
    val capeId: String,
    @get:JsonIgnore var id: String?,
    val armSize: String,
    val skinColor: String,
    val personaPieces: List<PersonaPiece>,
    val personaPieceTints: List<PersonaPieceTint>,
    val premiumSkin: Boolean,
    val personaSkin: Boolean,
    val capeOnClassicSkin: Boolean,
    @get:JsonIgnore val primaryUser: Boolean,
    @get:JsonIgnore var trusted: Boolean
) {
    data class Animation(
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
        @JsonProperty("PieceId") val id: String,
        @JsonProperty("PieceType") val type: String,
        @JsonProperty("PackId") val packId: String,
        @JsonProperty("IsDefault") val default: Boolean,
        @JsonProperty("ProductId") val productId: String
    )

    data class PersonaPieceTint(
        @JsonProperty("PieceType") val type: String,
        @JsonProperty("Colors") val colors: List<String>
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

fun PacketBuffer.readAppearance(version: Int): Appearance {
    val skinId = readString()
    val playFabId = if (version >= V1_16_210) readString() else ""
    val skinResourcePatch = readByteArray()
    val skinImage = readAppearanceImage()
    val animations = mutableListOf<Appearance.Animation>().apply { repeat(readIntLE()) { add(Appearance.Animation(readAppearanceImage(), Appearance.Animation.Type.values()[readIntLE()], buffer.readFloatLE(), if (version >= V1_16_210) Appearance.Animation.Expression.values()[buffer.readIntLE()] else Appearance.Animation.Expression.Linear)) } }
    val capeImage = readAppearanceImage()
    val geometryData = readByteArray()
    val geometryDataEngineVersion = if (version >= V1_17_034) readString() else ""
    val animationData = readByteArray()
    val capeId: String
    val id: String
    val armSize: String
    val skinColor: String
    val personaPieces: List<Appearance.PersonaPiece>
    val personaPieceTints: List<Appearance.PersonaPieceTint>
    val premiumSkin: Boolean
    val personaSkin: Boolean
    val capeOnClassicSkin: Boolean
    val primaryUser: Boolean
    if (version >= V1_17_034) {
        capeId = readString()
        id = readString()
        armSize = readString()
        skinColor = readString()
        personaPieces = mutableListOf<Appearance.PersonaPiece>().apply { repeat(readIntLE()) { add(Appearance.PersonaPiece(readString(), readString(), readString(), readBoolean(), readString())) } }
        personaPieceTints = mutableListOf<Appearance.PersonaPieceTint>().apply { repeat(readIntLE()) { add(Appearance.PersonaPieceTint(readString(), LazyList(readIntLE()) { readString() })) } }
        premiumSkin = readBoolean()
        personaSkin = readBoolean()
        capeOnClassicSkin = readBoolean()
        primaryUser = readBoolean()
    } else {
        premiumSkin = readBoolean()
        personaSkin = readBoolean()
        capeOnClassicSkin = readBoolean()
        primaryUser = readBoolean()
        capeId = readString()
        id = readString()
        if (version >= V1_16_100) {
            armSize = readString()
            skinColor = readString()
            personaPieces = mutableListOf<Appearance.PersonaPiece>().apply { repeat(readIntLE()) { add(Appearance.PersonaPiece(readString(), readString(), readString(), readBoolean(), readString())) } }
            personaPieceTints = mutableListOf<Appearance.PersonaPieceTint>().apply { repeat(readIntLE()) { add(Appearance.PersonaPieceTint(readString(), LazyList(readIntLE()) { readString() })) } }
        } else {
            armSize = ""
            skinColor = ""
            personaPieces = emptyList()
            personaPieceTints = emptyList()
        }
    }
    return Appearance(skinId, playFabId, skinResourcePatch, skinImage, animations, capeImage, geometryData, geometryDataEngineVersion, animationData, capeId, id, armSize, skinColor, personaPieces, personaPieceTints, premiumSkin, personaSkin, capeOnClassicSkin, primaryUser, false)
}

fun PacketBuffer.writeAppearance(value: Appearance, version: Int) {
    writeString(value.skinId)
    if (version >= V1_16_210) writeString(value.playFabId ?: "")
    writeByteArray(value.skinResourcePatch)
    writeAppearanceImage(value.skinImage)
    writeIntLE(value.animations.size)
    value.animations.forEach {
        writeAppearanceImage(it.image)
        writeIntLE(it.type.ordinal)
        writeFloatLE(it.frames)
        if (version >= V1_16_210) writeIntLE(it.expression.ordinal)
    }
    writeAppearanceImage(value.capeImage)
    writeByteArray(value.geometryData)
    if (version >= V1_17_034) writeString(value.geometryDataEngineVersion ?: "")
    writeByteArray(value.animationData)
    if (version >= V1_17_034) {
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
    } else {
        writeBoolean(value.premiumSkin)
        writeBoolean(value.personaSkin)
        writeBoolean(value.capeOnClassicSkin)
        writeString(value.capeId)
        writeString(value.id!!)
        if (version >= V1_16_100) {
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
    }
}
