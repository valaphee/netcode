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

import com.valaphee.netcode.mcbe.network.PacketBuffer
import java.awt.Transparency
import java.awt.color.ColorSpace
import java.awt.image.BufferedImage
import java.awt.image.ComponentColorModel
import java.awt.image.DataBuffer
import java.awt.image.DataBufferByte
import java.io.InputStream
import javax.imageio.ImageIO

/**
 * @author Kevin Ludwig
 */
class AppearanceImage(
    width: Int?,
    height: Int?,
    val data: ByteArray
) {
    val width: Int
    val height: Int

    init {
        if (width != null && height != null) {
            this.width = width
            this.height = height
        }
        else if (data.size.countOneBits() > 1) error("")
        else {
            val sizePow = (data.size shr 2).countTrailingZeroBits()
            if (sizePow % 2 == 0) {
                this.width = 1 shl sizePow / 2
                this.height = this.width
            } else {
                val sizePow2 = (sizePow - 1) / 2
                this.width = 1 shl sizePow2 + 1
                this.height = 1 shl sizePow2
            }
        }
    }

    companion object {
        val Empty = AppearanceImage(0, 0, ByteArray(0))
    }
}

fun PacketBuffer.readAppearanceImage(): AppearanceImage {
    val width = readIntLE()
    val height = readIntLE()
    return AppearanceImage(width, height, readByteArrayOfExpectedLength(width * height * 4))
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

private val colorModel = ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), intArrayOf(8, 8, 8, 8), true, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE)
