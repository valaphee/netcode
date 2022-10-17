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
    @JsonProperty("format_version") val version: String,
    @JsonProperty("header") val header: Header,
    @JsonProperty("modules") val modules: List<Module>?,
    @JsonProperty("metadata") val metadata: Metadata?,
    @JsonProperty("dependencies") val dependencies: List<Dependency>?,
    @JsonProperty("capabilities") val capabilities: List<String>?,
    @JsonProperty("subpacks") val subPacks: List<SubPack>?
) {
    data class Header(
        @JsonProperty("uuid") val id: UUID,
        @JsonProperty("name") val name: String,
        @JsonProperty("version") val version: Version,
        @JsonProperty("description") val description: String?,
        @JsonProperty("platform_locked") val platformLocked: Boolean?,
        @JsonProperty("min_engine_version") val minimumEngineVersion: Version?,
        @JsonProperty("max_engine_version") val maximumEngineVersion: Version?,
        @JsonProperty("pack_scope") val scope: String?,
        @JsonProperty("directory_load") val directoryLoad: Boolean?,
        @JsonProperty("load_before_game") val loadBeforeGame: Boolean?,
        @JsonProperty("lock_template_options") val lockTemplateOptions: Boolean?,
        @JsonProperty("population_control") val populationControl: Boolean?
    )

    data class Module(
        @JsonProperty("uuid") val id: UUID,
        @JsonProperty("version") val version: Version,
        @JsonProperty("description") val description: String?,
        @JsonProperty("type") val type: Type?,
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
        @JsonProperty("license") val license: String?,
        @JsonProperty("url") val website: String?
    )

    data class Dependency(
        @JsonProperty("uuid") val id: UUID,
        @JsonProperty("version") val version: Version
    )

    data class SubPack(
        @JsonProperty("folder_name") val path: String? = null,
        @JsonProperty("name") val name: String? = null,
        @JsonProperty("memory_tier") val memoryTier: Int = 0
    )
}
