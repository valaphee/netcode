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

import com.google.gson.JsonObject
import com.valaphee.netcode.mcbe.util.getBoolOrNull
import com.valaphee.netcode.mcbe.util.getIntOrNull
import com.valaphee.netcode.mcbe.util.getLong
import com.valaphee.netcode.mcbe.util.getString
import java.util.Locale
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
data class User constructor(
    val selfSignedId: UUID,
    val clientId: Long,
    val thirdPartyName: String,
    val thirdPartyNameOnly: Boolean,
    val appearance: Appearance,
    val platformOfflineId: String,
    val platformOnlineId: String,
    val deviceId: String,
    val deviceModel: String,
    val operatingSystem: OperatingSystem,
    val version: String,
    val locale: Locale,
    val defaultInputMode: InputMode,
    val currentInputMode: InputMode,
    val guiScale: Int,
    val uiProfile: UiProfile,
    val serverAddress: String
) {
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

    enum class InputMode {
        Unknown, KeyboardAndMouse, Touchscreen, GamePad, MotionControlled
    }

    enum class UiProfile {
        Classic, Pocket, Unknown2, Unknown3
    }

    fun toJson(json: JsonObject) {
        json.addProperty("SelfSignedId", selfSignedId.toString())
        json.addProperty("ClientRandomId", clientId)
        json.addProperty("ThirdPartyName", thirdPartyName)
        json.addProperty("ThirdPartyNameOnly", thirdPartyNameOnly)
        appearance.toJson(json)
        json.addProperty("PlatformOfflineId", platformOfflineId)
        json.addProperty("PlatformOnlineId", platformOnlineId)
        json.addProperty("DeviceId", deviceId)
        json.addProperty("DeviceModel", deviceModel)
        json.addProperty("DeviceOS", operatingSystem.ordinal)
        json.addProperty("GameVersion", version)
        json.addProperty("LanguageCode", locale.toString())
        json.addProperty("DefaultInputMode", defaultInputMode.ordinal)
        json.addProperty("CurrentInputMode", currentInputMode.ordinal)
        json.addProperty("GuiScale", guiScale)
        json.addProperty("UIProfile", uiProfile.ordinal)
        json.addProperty("ServerAddress", serverAddress)
    }
}

val JsonObject.asUser
    get() = User(
        UUID.fromString(getString("SelfSignedId")),
        getLong("ClientRandomId"),
        getString("ThirdPartyName"),
        getBoolOrNull("ThirdPartyNameOnly") ?: false,
        asAppearance,
        getString("PlatformOfflineId"),
        getString("PlatformOnlineId"),
        getString("DeviceId"),
        getString("DeviceModel"),
        User.OperatingSystem.values()[getIntOrNull("DeviceOS") ?: 0],
        getString("GameVersion"),
        Locale.forLanguageTag(getString("LanguageCode").replace('_', '-')),
        User.InputMode.values()[getIntOrNull("DefaultInputMode") ?: 0],
        User.InputMode.values()[getIntOrNull("CurrentInputMode") ?: 0],
        getIntOrNull("GuiScale") ?: 0,
        User.UiProfile.values()[getIntOrNull("UIProfile") ?: 0],
        getString("ServerAddress")
    )
