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

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.internal.Streams
import com.google.gson.stream.JsonReader
import com.valaphee.netcode.mcbe.network.PacketBuffer
import com.valaphee.netcode.mcbe.util.ByteBufStringReader
import com.valaphee.netcode.mcbe.util.getBool
import com.valaphee.netcode.mcbe.util.getFloat
import com.valaphee.netcode.mcbe.util.getInt
import com.valaphee.netcode.mcbe.util.getIntOrNull
import com.valaphee.netcode.mcbe.util.getJsonArray
import com.valaphee.netcode.mcbe.util.getJsonArrayOrNull
import com.valaphee.netcode.mcbe.util.getString
import com.valaphee.netcode.mcbe.util.getStringOrNull
import java.awt.Transparency
import java.awt.color.ColorSpace
import java.awt.image.BufferedImage
import java.awt.image.ColorModel
import java.awt.image.ComponentColorModel
import java.awt.image.DataBuffer
import java.awt.image.DataBufferByte
import java.io.InputStream
import java.io.StringReader
import java.nio.charset.StandardCharsets
import java.util.Base64
import javax.imageio.ImageIO

/**
 * @author Kevin Ludwig
 */
data class Appearance constructor(
    val skinId: String,
    val playFabId: String,
    val skinResourcePatch: Map<String, Any>,
    val skinImage: Image,
    val animations: List<Animation>,
    val capeImage: Image,
    val geometryData: String,
    val geometryDataEngineVersion: String,
    val animationData: String,
    val capeId: String,
    var id: String,
    val armSize: String,
    val skinColor: String,
    val personaPieces: List<PersonaPiece>,
    val personaPieceTints: List<PersonaPieceTint>,
    val premiumSkin: Boolean,
    val personaSkin: Boolean,
    val capeOnClassicSkin: Boolean,
    val primaryUser: Boolean,
    var trusted: Boolean,
) {
    data class Image(
        var width: Int,
        var height: Int,
        val data: ByteArray
    ) {
        constructor(image: BufferedImage) : this(
            image.width, image.height, (BufferedImage(colorModel, colorModel.createCompatibleWritableRaster(image.width, image.height), false, null).apply {
                createGraphics().apply {
                    drawImage(image, 0, 0, null)
                    dispose()
                }
            }.raster.dataBuffer as DataBufferByte).data
        )

        fun toJson(json: JsonObject, name: String? = null) {
            json.addProperty("${name ?: ""}ImageWidth", width)
            json.addProperty("${name ?: ""}ImageHeight", height)
            json.addProperty(if (null != name) "${name}Data" else "Image", base64Encoder.encodeToString(data))
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Image

            if (width != other.width) return false
            if (height != other.height) return false
            if (!data.contentEquals(other.data)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = width
            result = 31 * result + height
            result = 31 * result + data.contentHashCode()
            return result
        }

        override fun toString() = "Image(width=$width, height=$height)"

        companion object {
            val Empty = Image(0, 0, ByteArray(0))
        }
    }

    data class Animation constructor(
        val image: Image,
        val type: Type,
        val frames: Float,
        val expression: Expression = Expression.Linear
    ) {
        enum class Type {
            None, Head, Body32, Body128
        }

        enum class Expression {
            Linear, Blinking
        }
    }

    data class PersonaPiece(
        val id: String,
        val type: String,
        val packId: String,
        val default: Boolean,
        val productId: String
    )

    data class PersonaPieceTint(
        val type: String,
        val colors: Array<String>
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as PersonaPieceTint

            if (type != other.type) return false
            if (!colors.contentEquals(other.colors)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = type.hashCode()
            result = 31 * result + colors.contentHashCode()
            return result
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Appearance

        if (id != other.id) return false

        return true
    }

    override fun hashCode() = id.hashCode()

    fun toJson(json: JsonObject): JsonObject {
        json.addProperty("SkinId", skinId)
        json.addProperty("PlayFabId", playFabId)
        val jsonSkinResourcePatch = JsonObject()
        skinResourcePatch.forEach { jsonSkinResourcePatch.add(it.key, gson.toJsonTree(it.value)) }
        json.addProperty("SkinResourcePatch", base64Encoder.encodeToString(jsonSkinResourcePatch.toString().toByteArray(StandardCharsets.UTF_8)))
        skinImage.toJson(json, "Skin")
        json.addProperty("SkinGeometryData", base64Encoder.encodeToString(geometryData.toByteArray(StandardCharsets.UTF_8)))
        json.addProperty("SkinGeometryDataEngineVersion", geometryDataEngineVersion)
        json.addProperty("SkinAnimationData", base64Encoder.encodeToString(animationData.toByteArray(StandardCharsets.UTF_8)))
        json.addProperty("CapeId", capeId)
        capeImage.toJson(json, "Cape")
        json.addProperty("CapeOnClassicSkin", capeOnClassicSkin)
        val jsonAnimations = JsonArray()
        animations.forEach {
            val jsonAnimation = JsonObject()
            jsonAnimation.addProperty("Type", it.type.ordinal)
            jsonAnimation.addProperty("Frames", it.frames)
            it.image.toJson(jsonAnimation)
            jsonAnimation.addProperty("AnimationExpression", it.expression.ordinal)
            jsonAnimations.add(jsonAnimation)
        }
        json.add("AnimatedImageData", jsonAnimations)
        json.addProperty("PremiumSkin", premiumSkin)
        json.addProperty("PersonaSkin", personaSkin)
        json.addProperty("ArmSize", armSize)
        json.addProperty("SkinColor", skinColor)
        val jsonPersonaPieces = JsonArray()
        personaPieces.forEach {
            val jsonPersonaPiece = JsonObject()
            jsonPersonaPiece.addProperty("PieceId", it.id)
            jsonPersonaPiece.addProperty("PieceType", it.type)
            jsonPersonaPiece.addProperty("PackId", it.packId)
            jsonPersonaPiece.addProperty("IsDefault", it.default)
            jsonPersonaPiece.addProperty("ProductId", it.productId)
            jsonPersonaPieces.add(jsonPersonaPiece)
        }
        json.add("PersonaPieces", jsonPersonaPieces)
        val jsonPersonaPieceTints = JsonArray()
        personaPieceTints.forEach {
            val jsonPersonaPieceTint = JsonObject()
            jsonPersonaPieceTint.addProperty("PieceType", it.type)
            val jsonPersonaPieceTintColors = JsonArray()
            it.colors.forEach { jsonPersonaPieceTintColors.add(it) }
            jsonPersonaPieceTint.add("Colors", jsonPersonaPieceTintColors)
            jsonPersonaPieceTints.add(jsonPersonaPieceTint)
        }
        json.add("PieceTintColors", jsonPersonaPieceTints)
        return json
    }
}

fun JsonObject.getAsAppearanceImage(name: String?): Appearance.Image {
    val data = base64Decoder.decode(getString(if (null != name) "${name}Data" else "Image"))
    val width: Int
    val height: Int
    if (has("${name ?: ""}ImageWidth") && has("${name ?: ""}ImageHeight")) {
        width = this["${name ?: ""}ImageWidth"].asInt
        height = this["${name ?: ""}ImageHeight"].asInt
    } else if (data.size.countOneBits() > 1) error("Image size is not power of two") else {
        val sizePow = (data.size shr 2).countTrailingZeroBits()
        if (sizePow % 2 == 0) {
            width = 1 shl sizePow / 2
            height = width
        } else {
            val sizePow2 = (sizePow - 1) / 2
            width = 1 shl sizePow2 + 1
            height = 1 shl sizePow2
        }
    }
    return Appearance.Image(width, height, data)
}

val JsonObject.asAppearance
    get() = Appearance(
        getString("SkinId"),
        getStringOrNull("PlayFabId") ?: "",
        getStringOrNull("SkinResourcePatch")?.let {
            val jsonSkinResourcePatch = Streams.parse(JsonReader(StringReader(String(base64Decoder.decode(it), StandardCharsets.UTF_8))))
            if (jsonSkinResourcePatch.isJsonObject) jsonSkinResourcePatch.asJsonObject.entrySet().associate { it.key to gson.fromJson(it.value, Any::class.java) } else emptyMap()
        } ?: emptyMap(),
        getAsAppearanceImage("Skin"),
        getJsonArray("AnimatedImageData").map {
            val jsonAnimation = it.asJsonObject
            Appearance.Animation(
                jsonAnimation.getAsAppearanceImage(null),
                Appearance.Animation.Type.values()[jsonAnimation.getInt("Type")],
                jsonAnimation.getFloat("Frames"),
                Appearance.Animation.Expression.values()[jsonAnimation.getIntOrNull("AnimationExpression") ?: 0],
            )
        },
        getAsAppearanceImage("Cape"),
        String(base64Decoder.decode(getString("SkinGeometryData")), StandardCharsets.UTF_8),
        getStringOrNull("SkinGeometryDataEngineVersion") ?: "",
        String(base64Decoder.decode(getString("SkinAnimationData")), StandardCharsets.UTF_8),
        getString("CapeId"),
        "",
        getStringOrNull("ArmSize") ?: "",
        getStringOrNull("SkinColor") ?: "#0",
        run {
            val personaPieces = mutableListOf<Appearance.PersonaPiece>()
            getJsonArrayOrNull("PersonaPieces")?.forEach {
                val jsonPersonaPiece = it.asJsonObject
                personaPieces.add(
                    Appearance.PersonaPiece(
                        jsonPersonaPiece.getString("PieceId"),
                        jsonPersonaPiece.getString("PieceType"),
                        jsonPersonaPiece.getString("PackId"),
                        jsonPersonaPiece.getBool("IsDefault"),
                        jsonPersonaPiece.getString("ProductId")
                    )
                )
            }
            personaPieces
        },
        run {
            val personaPieceTints = mutableListOf<Appearance.PersonaPieceTint>()
            getJsonArrayOrNull("PieceTintColors")?.forEach {
                val jsonPersonaPieceTint = it.asJsonObject
                val jsonPersonaPieceTintColors = jsonPersonaPieceTint.getJsonArray("Colors")
                personaPieceTints.add(Appearance.PersonaPieceTint(jsonPersonaPieceTint.getString("PieceType"), Array(jsonPersonaPieceTintColors.size()) { jsonPersonaPieceTintColors[it].asString }))
            }
            personaPieceTints
        },
        getBool("PremiumSkin"),
        getBool("PersonaSkin"),
        getBool("CapeOnClassicSkin"),
        false,
        false
    )

fun PacketBuffer.readAppearanceImage(): Appearance.Image {
    val width = readIntLE()
    val height = readIntLE()
    return Appearance.Image(width, height, readByteArrayOfExpectedLength(width * height * bytesPerPixel))
}

fun PacketBuffer.readAppearancePre390(): Appearance {
    val skinId = readString()
    val skinResourcePatch = Streams.parse(JsonReader(ByteBufStringReader(this, readVarUInt()))).asJsonObject
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
    return Appearance(skinId, "", mutableMapOf<String, Any>().apply { skinResourcePatch.entrySet().forEach { this[it.key] = gson.fromJson(it.value, Any::class.java) } }, skinImage, animations, capeImage, geometryData, "", animationData, capeId, id, "", "", emptyList(), emptyList(), premiumSkin, personaSkin, capeOnClassicSkin, false, false)
}

fun PacketBuffer.readAppearancePre419(): Appearance {
    val skinId = readString()
    val skinResourcePatch = Streams.parse(JsonReader(ByteBufStringReader(this, readVarUInt()))).asJsonObject
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
    val personaPieceTints = mutableListOf<Appearance.PersonaPieceTint>().apply { repeat(readIntLE()) { add(Appearance.PersonaPieceTint(readString(), Array(readIntLE()) { readString() })) } }
    return Appearance(skinId, "", mutableMapOf<String, Any>().apply { skinResourcePatch.entrySet().forEach { this[it.key] = gson.fromJson(it.value, Any::class.java) } }, skinImage, animations, capeImage, geometryData, "", animationData, capeId, id, armSize, skinColor, personaPieces, personaPieceTints, premiumSkin, personaSkin, capeOnClassicSkin, false, false)
}

fun PacketBuffer.readAppearancePre428(): Appearance {
    val skinId = readString()
    val skinResourcePatch = Streams.parse(JsonReader(ByteBufStringReader(this, readVarUInt()))).asJsonObject
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
    val personaPieceTints = mutableListOf<Appearance.PersonaPieceTint>().apply { repeat(readIntLE()) { add(Appearance.PersonaPieceTint(readString(), Array(readIntLE()) { readString() })) } }
    return Appearance(skinId, "", mutableMapOf<String, Any>().apply { skinResourcePatch.entrySet().forEach { this[it.key] = gson.fromJson(it.value, Any::class.java) } }, skinImage, animations, capeImage, geometryData, "", animationData, capeId, id, armSize, skinColor, personaPieces, personaPieceTints, premiumSkin, personaSkin, capeOnClassicSkin, false, false)
}

fun PacketBuffer.readAppearancePre465(): Appearance {
    val skinId = readString()
    val playFabId = readString()
    val skinResourcePatch = Streams.parse(JsonReader(ByteBufStringReader(this, readVarUInt()))).asJsonObject
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
    val personaPieceTints = mutableListOf<Appearance.PersonaPieceTint>().apply { repeat(readIntLE()) { add(Appearance.PersonaPieceTint(readString(), Array(readIntLE()) { readString() })) } }
    return Appearance(skinId, playFabId, mutableMapOf<String, Any>().apply { skinResourcePatch.entrySet().forEach { this[it.key] = gson.fromJson(it.value, Any::class.java) } }, skinImage, animations, capeImage, geometryData, "", animationData, capeId, id, armSize, skinColor, personaPieces, personaPieceTints, premiumSkin, personaSkin, capeOnClassicSkin, false, false)
}

fun PacketBuffer.readAppearance() = Appearance(
    readString(),
    readString(),
    mutableMapOf<String, Any>().apply { Streams.parse(JsonReader(ByteBufStringReader(this@readAppearance, readVarUInt()))).asJsonObject.entrySet().forEach { this[it.key] = gson.fromJson(it.value, Any::class.java) } },
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
    mutableListOf<Appearance.PersonaPieceTint>().apply { repeat(readIntLE()) { add(Appearance.PersonaPieceTint(readString(), Array(readIntLE()) { readString() })) } },
    readBoolean(),
    readBoolean(),
    readBoolean(),
    readBoolean(),
    false
)

fun PacketBuffer.writeAppearanceImage(value: Appearance.Image) {
    writeIntLE(value.width)
    writeIntLE(value.height)
    writeByteArray(value.data)
}

fun PacketBuffer.writeAppearancePre390(value: Appearance) {
    writeString(value.skinId)
    writeString(JsonObject().apply { value.skinResourcePatch.forEach { add(it.key, gson.toJsonTree(it.value)) } }.toString())
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
    writeString(JsonObject().apply { value.skinResourcePatch.forEach { add(it.key, gson.toJsonTree(it.value)) } }.toString())
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
    writeString(JsonObject().apply { value.skinResourcePatch.forEach { add(it.key, gson.toJsonTree(it.value)) } }.toString())
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
    writeString(JsonObject().apply { value.skinResourcePatch.forEach { add(it.key, gson.toJsonTree(it.value)) } }.toString())
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

fun InputStream.readAppearanceImage(): Appearance.Image {
    val image = ImageIO.read(this)
    val width = image.width
    val height = image.height
    val convertedImage = BufferedImage(colorModel, colorModel.createCompatibleWritableRaster(width, height), false, null)
    val convertedImageGraphics = convertedImage.createGraphics()
    convertedImageGraphics.drawImage(image, 0, 0, null)
    convertedImageGraphics.dispose()
    return Appearance.Image(width, height, (convertedImage.raster.dataBuffer as DataBufferByte).data)
}

private val gson = Gson()
private val base64Decoder = Base64.getDecoder()
private val base64Encoder = Base64.getEncoder()
private const val bytesPerPixel = 4
private val colorModel: ColorModel = ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), intArrayOf(8, 8, 8, 8), true, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE)
