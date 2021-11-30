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

package com.valaphee.netcode.mcje.status

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.valaphee.netcode.mcje.util.text.Component
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class Status(
    @get:JsonProperty("version") val version: Version,
    /*@get:JsonProperty("modinfo") val mods: Mods?,*/
    @get:JsonProperty("players") val players: Players,
    @get:JsonProperty("description") val description: Component,
    @get:JsonProperty("favicon") val favicon: String?
) {
    class Version(
        @get:JsonProperty("name") val name: String,
        @get:JsonProperty("protocol") val protocol: Int
    )

    class Players(
        @get:JsonProperty("max") val maximum: Int,
        @get:JsonProperty("online") val current: Int,
        @get:JsonProperty("sample") val sample: List<Sample>?
    ) {
        data class Sample(
            @get:JsonProperty("id") val id: UUID,
            @get:JsonProperty("name") val name: String
        )
    }

    class Mods(
        @get:JsonProperty("type") val type: String,
        @get:JsonProperty("modlist") val mods: List<Mod>?
    ) {
        data class Mod(
            @get:JsonProperty("modid") val name: String,
            @get:JsonProperty("version") val version: String
        )
    }
}
