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

package com.valaphee.netcode.mcbe.util

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.valaphee.netcode.mcbe.world.entity.player.AuthExtra
import com.valaphee.netcode.mcbe.world.entity.player.User
import org.jose4j.jwa.AlgorithmConstraints
import org.jose4j.jws.JsonWebSignature
import org.jose4j.jwt.consumer.InvalidJwtException
import org.jose4j.jwt.consumer.JwtConsumerBuilder
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PublicKey
import java.security.interfaces.ECPublicKey
import java.security.spec.ECGenParameterSpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

fun authJws(keyPair: KeyPair, authExtra: AuthExtra): String = objectMapper.writeValueAsString(
    mapOf(
        "chain" to listOf(
            JsonWebSignature().apply {
                setHeader("alg", "ES384")
                setHeader("x5u", base64Encoder.encodeToString(keyPair.public.encoded))
                val iat = System.currentTimeMillis()
                payload = objectMapper.writeValueAsString(mapOf("nbf" to iat - 60, "iat" to iat, "exp" to iat + 86_400, "identityPublicKey" to base64Encoder.encodeToString(keyPair.public.encoded), "extraData" to authExtra))
                key = keyPair.private
            }.compactSerialization
        )
    )
)

fun authJws(keyPair: KeyPair, authJws: String): String {
    val authJwsChain = objectMapper.readValue<Map<*, *>>(authJws)["chain"] as List<*>
    val authJwtContext = JwtConsumerBuilder().setJwsAlgorithmConstraints(AlgorithmConstraints.ConstraintType.PERMIT, "ES384").apply { setSkipSignatureVerification() }.build().process(authJwsChain.first() as String)
    return objectMapper.writeValueAsString(
        mapOf(
            "chain" to listOf(JsonWebSignature().apply {
                setHeader("alg", "ES384")
                setHeader("x5u", Base64.getEncoder().encodeToString(keyPair.public.encoded))
                val authJwsHeaders = authJwtContext.joseObjects.single().headers
                payload = objectMapper.writeValueAsString(mapOf("nbf" to authJwsHeaders.getStringHeaderValue("nbf"), "exp" to authJwsHeaders.getStringHeaderValue("exp"), "certificateAuthority" to true, "identityPublicKey" to authJwsHeaders.getStringHeaderValue("x5u")))
                key = keyPair.private
            }.compactSerialization) + authJwsChain
        )
    )
}

fun parseAuthJws(authJws: String): Triple<Boolean, PublicKey, AuthExtra> {
    val authJwsChain = objectMapper.readValue<Map<*, *>>(authJws)["chain"] as List<*>
    var verified = false
    var authJwsPayloadJson: Map<*, *>? = null
    var verificationKey: PublicKey? = null
    for (authJwsItem in authJwsChain) {
        val authJwtConsumerBuilder = JwtConsumerBuilder().setJwsAlgorithmConstraints(AlgorithmConstraints.ConstraintType.PERMIT, "ES384").apply {
            if (verified && verificationKey != null) setVerificationKey(verificationKey) else verificationKey?.let {
                setVerificationKey(it)
                if (verificationKey == key) verified = true
            } ?: setSkipSignatureVerification()
        }
        val authJwtClaims = try {
            authJwtConsumerBuilder.build().processToClaims(authJwsItem as String)
        } catch (_: InvalidJwtException) {
            verified = false
            authJwtConsumerBuilder.setSkipSignatureVerification()
            authJwtConsumerBuilder.build().processToClaims(authJwsItem as String)
        }
        authJwsPayloadJson = objectMapper.readValue<Map<*, *>>(authJwtClaims.rawJson)
        verificationKey = generatePublicKey(authJwsPayloadJson["identityPublicKey"] as String)
    }
    val authJwtConsumerBuilder = JwtConsumerBuilder().setJwsAlgorithmConstraints(AlgorithmConstraints.ConstraintType.PERMIT, "ES384").apply { setVerificationKey(verificationKey) }
    try {
        authJwtConsumerBuilder.build().processToClaims(authJwsChain.first() as String)
    } catch (_: InvalidJwtException) {
        verified = false
    }
    return Triple(verified, verificationKey!!, objectMapper.convertValue(authJwsPayloadJson!!["extraData"] as Map<*, *>))
}

fun userJws(keyPair: KeyPair, user: User): String = JsonWebSignature().apply {
    setHeader("alg", "ES384")
    setHeader("x5u", base64Encoder.encodeToString(keyPair.public.encoded))
    payload = objectMapper.writeValueAsString(user)
    key = keyPair.private
}.compactSerialization

fun parseUserJws(userJws: String, verificationKey: PublicKey?): Pair<Boolean, User> {
    val userJwtConsumerBuilder = JwtConsumerBuilder().setJwsAlgorithmConstraints(AlgorithmConstraints.ConstraintType.PERMIT, "ES384")
    verificationKey?.let { userJwtConsumerBuilder.setVerificationKey(verificationKey) } ?: userJwtConsumerBuilder.setSkipSignatureVerification()
    var verified = false
    val userJwtClaims = try {
        userJwtConsumerBuilder.build().processToClaims(userJws).also { verified = verificationKey != null }
    } catch (_: InvalidJwtException) {
        userJwtConsumerBuilder.setSkipSignatureVerification()
        userJwtConsumerBuilder.build().processToClaims(userJws)
    }
    return verified to objectMapper.readValue(userJwtClaims.rawJson)
}

fun serverToClientHandshakeJws(keyPair: KeyPair, salt: ByteArray): String = JsonWebSignature().apply {
    setHeader("alg", "ES384")
    setHeader("x5u", base64Encoder.encodeToString(keyPair.public.encoded))
    setHeader("typ", "JWT")
    payload = objectMapper.writeValueAsString(mapOf("salt" to base64Encoder.encodeToString(salt)))
    key = keyPair.private
}.compactSerialization

fun parseServerToClientHandshakeJws(serverToClientHandshakeJws: String): Pair<PublicKey, ByteArray> {
    val jwtContext = JwtConsumerBuilder().setJwsAlgorithmConstraints(AlgorithmConstraints.ConstraintType.PERMIT, "ES384").apply { setSkipSignatureVerification() }.build().process(serverToClientHandshakeJws)
    return generatePublicKey(jwtContext.joseObjects.single().headers.getStringHeaderValue("x5u")) to base64Decoder.decode(objectMapper.readValue<Map<*, *>>(jwtContext.jwtClaims.rawJson)["salt"] as String)
}

private val objectMapper = jacksonObjectMapper().apply { disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES) }
private val base64Encoder = Base64.getEncoder()
private val base64Decoder = Base64.getDecoder()
private val keyFactory = KeyFactory.getInstance("EC")
private val key = generatePublicKey("MHYwEAYHKoZIzj0CAQYFK4EEACIDYgAE8ELkixyLcwlZryUQcu1TvPOmI2B7vX83ndnWRUaXm74wFfa5f/lwQNTfrLVHa2PmenpGI6JhIMUJaWZrjmMj90NoKNFSNBuKdm8rYiXsfaz3K36x/1U26HpG0ZxK/V1V")
private val keyPairGenerator = KeyPairGenerator.getInstance("EC")

fun generatePublicKey(base64: String) = keyFactory.generatePublic(X509EncodedKeySpec(base64Decoder.decode(base64))) as ECPublicKey

fun generateKeyPair(): KeyPair = keyPairGenerator.apply { initialize(ECGenParameterSpec("secp384r1")) }.generateKeyPair()
