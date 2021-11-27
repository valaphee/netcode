/*
 * MIT License
 *
 * Copyright (c) 2021, Valaphee.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.griefergames.breakdown.world.map

import com.valaphee.netcode.mcje.util.text.Component

/**
 * @author Kevin Ludwig
 */
data class Decoration(
    val type: Type,
    val positionX: Int,
    val positionY: Int,
    val rotation: Int,
    val label: Component?
) {
    enum class Type {
        WhiteArrow,
        GreenArrow,
        RedArrow,
        BlueArrow,
        WhiteCross,
        RedPointer,
        WhiteCircle,
        SmallWhiteCircle,
        Mansion,
        Temple,
        WhiteBanner,
        OrangeBanner,
        MagentaBanner,
        LightBlueBanner,
        YellowBanner,
        LimeBanner,
        PinkBanner,
        GrayBanner,
        LightGrayBanner,
        CyanBanner,
        PurpleBanner,
        BlueBanner,
        BrownBanner,
        GreenBanner,
        RedBanner,
        BlackBanner,
        TreasureMarker
    }
}
