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

/**
 * @author Kevin Ludwig
 */
class Content(
    @get:JsonProperty("version") val version: Int,
    @get:JsonProperty("content") val content: List<Entry>
) {
    class Entry(
        @get:JsonProperty("path") val path: String,
        @get:JsonProperty("key") val key: String?
    )
}
