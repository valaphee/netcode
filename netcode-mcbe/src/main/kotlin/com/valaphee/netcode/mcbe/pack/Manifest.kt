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

package com.valaphee.netcode.mcbe.pack

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
data class Manifest(
    @get:JsonProperty("format_version") val version: String,
    @get:JsonProperty("header") val header: Header,
    @get:JsonProperty("modules") val modules: List<Module>?,
    @get:JsonProperty("metadata") val metadata: Metadata?,
    @get:JsonProperty("dependencies") val dependencies: List<Dependency>?,
    @get:JsonProperty("capabilities") val capabilities: List<String>?,
    @get:JsonProperty("subpacks") val subPacks: List<SubPack>?
) {
    data class Header(
        @get:JsonProperty("uuid") val id: UUID,
        @get:JsonProperty("name") val name: String,
        @get:JsonProperty("version") val version: Version,
        @get:JsonProperty("description") val description: String?,
        @get:JsonProperty("platform_locked") val platformLocked: Boolean?,
        @get:JsonProperty("min_engine_version") val minimumEngineVersion: Version?,
        @get:JsonProperty("max_engine_version") val maximumEngineVersion: Version?,
        @get:JsonProperty("pack_scope") val scope: String?,
        @get:JsonProperty("directory_load") val directoryLoad: Boolean?,
        @get:JsonProperty("load_before_game") val loadBeforeGame: Boolean?,
        @get:JsonProperty("lock_template_options") val lockTemplateOptions: Boolean?,
        @get:JsonProperty("population_control") val populationControl: Boolean?
    )

    data class Module(
        @get:JsonProperty("uuid") val id: UUID,
        @get:JsonProperty("version") val version: Version,
        @get:JsonProperty("description") val description: String?,
        @get:JsonProperty("type") val type: Type?,
    ) {
        enum class Type {
            @JsonProperty("invalid") Invalid,
            @JsonProperty("resources") Resource,
            @JsonProperty("data") Data,
            @JsonProperty("plugin") Plugin,
            @JsonProperty("client_data") ClientData,
            @JsonProperty("interface") Interface,
            @JsonProperty("mandatory") Mandatory,
            @JsonProperty("world_template") WorldTemplate
        }
    }

    data class Metadata(
        val authors: List<String>?,
        @get:JsonProperty("license") val license: String?,
        @get:JsonProperty("url") val website: String?
    )

    data class Dependency(
        @get:JsonProperty("uuid") val id: UUID,
        @get:JsonProperty("version") val version: Version
    )

    data class SubPack(
        @get:JsonProperty("folder_name") val path: String? = null,
        @get:JsonProperty("name") val name: String? = null,
        @get:JsonProperty("memory_tier") val memoryTier: Int = 0
    )
}
