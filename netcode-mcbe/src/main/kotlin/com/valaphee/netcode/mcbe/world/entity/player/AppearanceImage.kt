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

import com.fasterxml.jackson.annotation.JsonProperty
import com.valaphee.netcode.mcbe.network.PacketBuffer
import java.awt.Transparency
import java.awt.color.ColorSpace
import java.awt.image.BufferedImage
import java.awt.image.ComponentColorModel
import java.awt.image.DataBuffer
import java.awt.image.DataBufferByte

/**
 * @author Kevin Ludwig
 */
data class AppearanceImage(
    @JsonProperty("ImageWidth") val width: Int?,
    @JsonProperty("ImageHeight") val height: Int?,
    @JsonProperty("Data") val data: ByteArray
) {
    val _width: Int
    val _height: Int

    init {
        if (width != null && height != null) {
            _width = width
            _height = height
        } else if (data.size.countOneBits() > 1) error("Unable to auto-detect appearance image size")
        else {
            val sizePow = (data.size shr 2).countTrailingZeroBits()
            if (sizePow % 2 == 0) {
                _width = 1 shl sizePow / 2
                _height = _width
            } else {
                val sizePow2 = (sizePow - 1) / 2
                _width = 1 shl sizePow2 + 1
                _height = 1 shl sizePow2
            }
        }
    }

    companion object {
        val Empty = AppearanceImage(0, 0, ByteArray(0))

        internal val colorModel = ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), intArrayOf(8, 8, 8, 8), true, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AppearanceImage

        if (width != other.width) return false
        if (height != other.height) return false
        if (!data.contentEquals(other.data)) return false
        if (_width != other._width) return false
        if (_height != other._height) return false

        return true
    }

    override fun hashCode(): Int {
        var result = width ?: 0
        result = 31 * result + (height ?: 0)
        result = 31 * result + data.contentHashCode()
        result = 31 * result + _width
        result = 31 * result + _height
        return result
    }
}

fun BufferedImage.toAppearanceImage() = AppearanceImage(width, height, (BufferedImage(AppearanceImage.colorModel, AppearanceImage.colorModel.createCompatibleWritableRaster(width, height), false, null).apply {
    createGraphics().apply {
        drawImage(this@toAppearanceImage, 0, 0, null)
        dispose()
    }
}.raster.dataBuffer as DataBufferByte).data)

fun PacketBuffer.readAppearanceImage(): AppearanceImage {
    val width = readIntLE()
    val height = readIntLE()
    return AppearanceImage(width, height, readByteArrayOfExpectedLength(width * height * 4))
}

fun PacketBuffer.writeAppearanceImage(value: AppearanceImage) {
    writeIntLE(value._width)
    writeIntLE(value._height)
    writeByteArray(value.data)
}
