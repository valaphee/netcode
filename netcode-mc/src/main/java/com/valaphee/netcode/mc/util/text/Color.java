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

package com.valaphee.netcode.mc.util.text;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kevin Ludwig
 */
public enum Color implements StyleCode {
    White('f', "white"),
    Yellow('e', "yellow"),
    LightPurple('d', "light_purple"),
    Red('c', "red"),
    Aqua('b', "aqua"),
    Green('a', "green"),
    Blue('9', "blue"),
    DarkGray('8', "dark_gray"),
    Gray('7', "gray"),
    Gold('6', "gold"),
    DarkPurple('5', "dark_purple"),
    DarkRed('4', "dark_red"),
    DarkAqua('3', "dark_aqua"),
    DarkGreen('2', "dark_green"),
    DarkBlue('1', "dark_blue"),
    Black('0', "black");

    private static final Map<String, Color> byKey;

    static {
        byKey = new HashMap<>(values().length);
        for (final Color color : values()) {
            byKey.put(color.key, color);
        }
    }

    private final char code;
    private final String key;

    @SuppressWarnings("ThisEscapedInObjectConstruction")
    Color(final char code, final String key) {
        this.code = code;
        this.key = key;

        values.add(this);
    }

    public static Color byKey(final String key) {
        return byKey.get(key);
    }

    @Override
    public char getCode() {
        return code;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return new String(new char[]{Prefix, code});
    }
}
