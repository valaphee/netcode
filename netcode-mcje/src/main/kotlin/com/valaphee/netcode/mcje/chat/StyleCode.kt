/*
 * Copyright (c) 2019-2022, GrieferGames, Valaphee.
 * All rights reserved.
 */

package com.valaphee.netcode.mcje.chat

/**
 * @author Kevin Ludwig
 */
enum class StyleCode(
    val value: Char
) {
    Black('0'),
    DarkBlue('1'),
    DarkGreen('2'),
    DarkAqua('3'),
    DarkRed('4'),
    DarkPurple('5'),
    Gold('6'),
    Gray('7'),
    DarkGray('8'),
    Blue('9'),
    Green('a'),
    Aqua('b'),
    Red('c'),
    LightPurple('d'),
    Yellow('e'),
    White('f'),
    Obfuscated('k'),
    Bold('l'),
    Strikethrough('m'),
    Underlined('n'),
    Italic('o'),
    Reset('r');

    override fun toString() = String(charArrayOf('§', value))
}
