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
import com.fasterxml.jackson.annotation.JsonIgnore
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
    val thirdPartyNameOnly: Boolean?,
    /*@JsonUnwrapped*/@JsonIgnore val appearance: Appearance,
    val platformOfflineId: String,
    val platformOnlineId: String,
    val platformUserId: String?,
    val deviceId: String,
    val deviceModel: String,
    val operatingSystem: OperatingSystem,
    val version: String,
    val locale: Locale,
    val defaultInputMode: InputMode,
    val currentInputMode: InputMode,
    val guiScale: Int,
    val uiProfile: UiProfile?,
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

    val skinId get() = appearance.skinId
    val playFabId get() = appearance.playFabId
    val skinResourcePatch get() = appearance.skinResourcePatch
    val skinWidth get() = appearance.skinImage.width
    val skinHeight get() = appearance.skinImage.height
    val skinData get() = appearance.skinImage.data
    val animations get() = appearance.animations
    val capeWidth get() = appearance.capeImage.width
    val capeHeight get() = appearance.capeImage.height
    val capeData get() = appearance.capeImage.data
    val geometryData get() = appearance.geometryData
    val geometryDataEngineVersion get() = appearance.geometryDataEngineVersion
    val animationData get() = appearance.animationData
    val capeId get() = appearance.capeId
    val armSize get() = appearance.armSize
    val skinColor get() = appearance.skinColor
    val personaPieces get() = appearance.personaPieces
    val personaPieceTints get() = appearance.personaPieceTints
    val premiumSkin get() = appearance.premiumSkin
    val personaSkin get() = appearance.personaSkin
    val capeOnClassicSkin get() = appearance.capeOnClassicSkin

    @JsonCreator
    constructor(
        @get:JsonProperty("SelfSignedId") selfSignedId: UUID,
        @get:JsonProperty("ClientRandomId") clientId: Long,
        @get:JsonProperty("ThirdPartyName") thirdPartyName: String,
        @get:JsonProperty("ThirdPartyNameOnly") thirdPartyNameOnly: Boolean?,
        @get:JsonProperty("SkinId") skinId: String,
        @get:JsonProperty("PlayFabId") playFabId: String?,
        @get:JsonProperty("SkinResourcePatch") skinResourcePatch: ByteArray,
        @get:JsonProperty("SkinImageWidth") skinWidth: Int?,
        @get:JsonProperty("SkinImageHeight") skinHeight: Int?,
        @get:JsonProperty("SkinData") skinData: ByteArray,
        @get:JsonProperty("AnimatedImageData") animations: List<Appearance.Animation>,
        @get:JsonProperty("CapeImageWidth") capeWidth: Int?,
        @get:JsonProperty("CapeImageHeight") capeHeight: Int?,
        @get:JsonProperty("CapeData") capeData: ByteArray,
        @get:JsonProperty("SkinGeometryData") geometryData: ByteArray,
        @get:JsonProperty("SkinGeometryDataEngineVersion") geometryDataEngineVersion: String?,
        @get:JsonProperty("SkinAnimationData") animationData: ByteArray,
        @get:JsonProperty("CapeId") capeId: String,
        @get:JsonProperty("ArmSize") armSize: String = "",
        @get:JsonProperty("SkinColor") skinColor: String = "#0",
        @get:JsonProperty("PersonaPieces") personaPieces: List<Appearance.PersonaPiece> = emptyList(),
        @get:JsonProperty("PieceTintColors") personaPieceTints: List<Appearance.PersonaPieceTint> = emptyList(),
        @get:JsonProperty("PremiumSkin") premiumSkin: Boolean,
        @get:JsonProperty("PersonaSkin") personaSkin: Boolean,
        @get:JsonProperty("CapeOnClassicSkin") capeOnClassicSkin: Boolean,
        @get:JsonProperty("PlatformOfflineId") platformOfflineId: String,
        @get:JsonProperty("PlatformOnlineId") platformOnlineId: String,
        @get:JsonProperty("PlatformUserId") platformUserId: String?,
        @get:JsonProperty("DeviceId") deviceId: String,
        @get:JsonProperty("DeviceModel") deviceModel: String,
        @get:JsonProperty("DeviceOS") operatingSystem: OperatingSystem,
        @get:JsonProperty("GameVersion") version: String,
        @get:JsonProperty("LanguageCode") locale: Locale,
        @get:JsonProperty("DefaultInputMode") defaultInputMode: InputMode,
        @get:JsonProperty("CurrentInputMode") currentInputMode: InputMode,
        @get:JsonProperty("GuiScale") guiScale: Int,
        @get:JsonProperty("UIProfile") uiProfile: UiProfile?,
        @get:JsonProperty("ServerAddress") serverAddress: String
    ) : this(selfSignedId, clientId, thirdPartyName, thirdPartyNameOnly, Appearance(skinId, playFabId, skinResourcePatch, AppearanceImage(skinWidth, skinHeight, skinData), animations, AppearanceImage(capeWidth, capeHeight, capeData), geometryData, geometryDataEngineVersion, animationData, capeId, null, armSize, skinColor, personaPieces, personaPieceTints, premiumSkin, personaSkin, capeOnClassicSkin, false, false), platformOfflineId, platformOnlineId, platformUserId, deviceId, deviceModel, operatingSystem, version, locale, defaultInputMode, currentInputMode, guiScale, uiProfile, serverAddress)
}
