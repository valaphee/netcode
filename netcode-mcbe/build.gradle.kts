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

dependencies {
    api(project(":netcode-common"))
    api("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.3")
    api("com.valaphee:foundry-math:1.4.0")
    api("com.valaphee:jackson-dataformat-nbt:1.2.4")
    api("it.unimi.dsi:fastutil:8.5.8")
    api("network.ycc:netty-raknet-client:0.8-SNAPSHOT")
    api("network.ycc:netty-raknet-server:0.8-SNAPSHOT")
    api("org.bitbucket.b_c:jose4j:0.7.12")
    api("org.lz4:lz4-java:1.8.0")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            pom.apply {
                name.set("Minecraft: Bedrock Edition Netcode")
                description.set("Minecraft: Bedrock Edition Netcode")
                url.set("https://valaphee.com")
                scm {
                    connection.set("https://github.com/valaphee/netcode.git")
                    developerConnection.set("https://github.com/valaphee/netcode.git")
                    url.set("https://github.com/valaphee/netcode")
                }
                licenses {
                    license {
                        name.set("Apache License 2.0")
                        url.set("https://raw.githubusercontent.com/valaphee/netcode/master/LICENSE.txt")
                    }
                }
                developers {
                    developer {
                        id.set("valaphee")
                        name.set("Valaphee")
                        email.set("iam@valaphee.com")
                        roles.add("owner")
                    }
                }
            }

            from(components["java"])
        }
    }
}
