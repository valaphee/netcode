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

package com.valaphee.netcode.mcbe.entity.player.appearance

import com.google.gson.JsonObject
import com.valaphee.netcode.mc.util.getString
import com.valaphee.netcode.mcbe.PacketBuffer
import java.awt.Transparency
import java.awt.color.ColorSpace
import java.awt.image.BufferedImage
import java.awt.image.ColorModel
import java.awt.image.ComponentColorModel
import java.awt.image.DataBuffer
import java.awt.image.DataBufferByte
import java.io.InputStream
import java.util.Base64
import javax.imageio.ImageIO

/**
 * @author Kevin Ludwig
 */
data class AppearanceImage(
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

        other as AppearanceImage

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

    override fun toString() = "AppearanceImage(width=$width, height=$height)"

    companion object {
        val Empty = AppearanceImage(0, 0, ByteArray(0))
    }
}

fun JsonObject.getAsAppearanceImage(name: String?): AppearanceImage {
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
    return AppearanceImage(width, height, data)
}

fun PacketBuffer.readAppearanceImage(): AppearanceImage {
    val width = readIntLE()
    val height = readIntLE()
    return AppearanceImage(width, height, readByteArrayOfExpectedLength(width * height * bytesPerPixel))
}

fun PacketBuffer.writeAppearanceImage(value: AppearanceImage) {
    writeIntLE(value.width)
    writeIntLE(value.height)
    writeByteArray(value.data)
}

fun InputStream.readAppearanceImage(): AppearanceImage {
    val image = ImageIO.read(this)
    val width = image.width
    val height = image.height
    val convertedImage = BufferedImage(colorModel, colorModel.createCompatibleWritableRaster(width, height), false, null)
    val convertedImageGraphics = convertedImage.createGraphics()
    convertedImageGraphics.drawImage(image, 0, 0, null)
    convertedImageGraphics.dispose()
    return AppearanceImage(width, height, (convertedImage.raster.dataBuffer as DataBufferByte).data)
}

private val base64Decoder = Base64.getDecoder()
private val base64Encoder = Base64.getEncoder()
private const val bytesPerPixel = 4
private val colorModel: ColorModel = ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), intArrayOf(8, 8, 8, 8), true, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE)
