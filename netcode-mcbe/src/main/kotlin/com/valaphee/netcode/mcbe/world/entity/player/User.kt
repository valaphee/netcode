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

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonUnwrapped
import java.util.Locale
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
data class User constructor(
    @get:JsonProperty("SelfSignedId") val selfSignedId: UUID,
    @get:JsonProperty("ClientRandomId") val clientId: Long,
    @get:JsonProperty("ThirdPartyName") val thirdPartyName: String,
    @get:JsonProperty("ThirdPartyNameOnly") val thirdPartyNameOnly: Boolean = false,
    @get:JsonUnwrapped val appearance: Appearance,
    @get:JsonProperty("PlatformOfflineId") val platformOfflineId: String,
    @get:JsonProperty("PlatformOnlineId") val platformOnlineId: String,
    @get:JsonProperty("DeviceId") val deviceId: String,
    @get:JsonProperty("DeviceModel") val deviceModel: String,
    @get:JsonProperty("DeviceOS") val operatingSystem: OperatingSystem = OperatingSystem.Unknown,
    @get:JsonProperty("GameVersion") val version: String,
    @get:JsonProperty("LanguageCode") val locale: Locale,
    @get:JsonProperty("DefaultInputMode") val defaultInputMode: InputMode = InputMode.Unknown,
    @get:JsonProperty("CurrentInputMode") val currentInputMode: InputMode = InputMode.Unknown,
    @get:JsonProperty("GuiScale") val guiScale: Int = 0,
    @get:JsonProperty("UIProfile") val uiProfile: UiProfile = UiProfile.Classic,
    @get:JsonProperty("ServerAddress") val serverAddress: String
) {
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    enum class OperatingSystem {
        Unknown,
        Android,
        Ios,
        Osx,
        FireOs,
        GearVr,
        Hololens,
        Windows10,
        Windows32,
        Dedicated,
        Tv,
        Orbis,
        Nx,
        XboxOne,
        WindowsPhone
    }

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    enum class InputMode {
        Unknown, KeyboardAndMouse, Touchscreen, GamePad, MotionControlled
    }

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    enum class UiProfile {
        Classic, Pocket, Unknown2, Unknown3
    }
}
