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

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
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

    companion object {
        @JvmStatic
        @JsonCreator
        fun jsonCreator(
            @JsonProperty("SelfSignedId") selfSignedId: UUID,
            @JsonProperty("ClientRandomId") clientId: Long,
            @JsonProperty("ThirdPartyName") thirdPartyName: String,
            @JsonProperty("ThirdPartyNameOnly") thirdPartyNameOnly: Boolean = false,
            @JsonProperty("SkinId") skinId: String,
            @JsonProperty("PlayFabId") playFabId: String,
            @JsonProperty("SkinResourcePatch") @JsonFormat(shape = JsonFormat.Shape.BINARY) skinResourcePatch: String,
            @JsonProperty("SkinImageWidth") skinWidth: Int?,
            @JsonProperty("SkinImageHeight") skinHeight: Int?,
            @JsonProperty("SkinData") skinData: ByteArray,
            @JsonProperty("AnimatedImageData") animations: List<Appearance.Animation>,
            @JsonProperty("CapeImageWidth") capeWidth: Int?,
            @JsonProperty("CapeImageHeight") capeHeight: Int?,
            @JsonProperty("CapeData") capeData: ByteArray,
            @JsonProperty("SkinGeometryData") geometryData: String,
            @JsonProperty("SkinGeometryDataEngineVersion") geometryDataEngineVersion: String,
            @JsonProperty("SkinAnimationData") animationData: String,
            @JsonProperty("CapeId") capeId: String,
            @JsonProperty("ArmSize") armSize: String,
            @JsonProperty("SkinColor") skinColor: String,
            @JsonProperty("PersonaPieces") personaPieces: List<Appearance.PersonaPiece>,
            @JsonProperty("PieceTintColors") personaPieceTints: List<Appearance.PersonaPieceTint>,
            @JsonProperty("PremiumSkin") premiumSkin: Boolean,
            @JsonProperty("PersonaSkin") personaSkin: Boolean,
            @JsonProperty("CapeOnClassicSkin") capeOnClassicSkin: Boolean,
            @JsonProperty("PlatformOfflineId") platformOfflineId: String,
            @JsonProperty("PlatformOnlineId") platformOnlineId: String,
            @JsonProperty("DeviceId") deviceId: String,
            @JsonProperty("DeviceModel") deviceModel: String,
            @JsonProperty("DeviceOS") operatingSystem: OperatingSystem = OperatingSystem.Unknown,
            @JsonProperty("GameVersion") version: String,
            @JsonProperty("LanguageCode") locale: Locale,
            @JsonProperty("DefaultInputMode") defaultInputMode: InputMode = InputMode.Unknown,
            @JsonProperty("CurrentInputMode") currentInputMode: InputMode = InputMode.Unknown,
            @JsonProperty("GuiScale") guiScale: Int = 0,
            @JsonProperty("UIProfile") uiProfile: UiProfile = UiProfile.Classic,
            @JsonProperty("ServerAddress") serverAddress: String
        ) = User(selfSignedId, clientId, thirdPartyName, thirdPartyNameOnly,  Appearance(skinId, playFabId, skinResourcePatch, AppearanceImage(skinWidth, skinHeight, skinData), animations, AppearanceImage(capeWidth, capeHeight, capeData), geometryData, geometryDataEngineVersion, animationData, capeId, null, armSize, skinColor, personaPieces, personaPieceTints, premiumSkin, personaSkin, capeOnClassicSkin, false, false), platformOfflineId, platformOnlineId, deviceId, deviceModel, operatingSystem, version, locale, defaultInputMode, currentInputMode, guiScale, uiProfile, serverAddress)
    }
}
