/*
 * Copyright (c) 2024. ForteScarlet.
 *
 * This file is part of simbot-component-onebot.
 *
 * simbot-component-onebot is free software: you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-onebot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-onebot.
 * If not, see <https://www.gnu.org/licenses/>.
 */

import io.gitlab.arturbosch.detekt.Detekt
import love.forte.gradle.common.core.project.setup
import love.forte.gradle.common.core.repository.Repositories
import util.isCi

plugins {
    idea
    `simbot-onebot-dokka-multi-module`

    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlinxBinaryCompatibilityValidator)
}

group = "love.forte.simbot.component"
version = "0.0.1"

setup(P.ComponentOneBot)

buildscript {
    repositories {
        mavenCentral()
    }
}


logger.info("=== Current version: {} ===", version)

allprojects {
    setup(P.ComponentOneBot)
    repositories {
        mavenCentral()
        maven {
            url = uri(Repositories.Snapshot.URL)
            mavenContent {
                snapshotsOnly()
            }
        }
    }
}

idea {
    module.apply {
        isDownloadSources = true
    }
    project {
        modules.forEach { module ->
            module.apply {
                isDownloadSources = true
            }
        }
    }
}

// detekt
dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:${libs.versions.detekt.get()}")
}

detekt {
    source.setFrom(subprojects.map { it.projectDir.absoluteFile.resolve("src") })
    config.setFrom(rootDir.resolve("config/detekt/detekt.yml"))
    baseline = rootDir.resolve("config/detekt/baseline.xml")
    // buildUponDefaultConfig = true
    parallel = true
    reportsDir = rootProject.layout.buildDirectory.dir("reports/detekt").get().asFile
    if (!isCi) {
        autoCorrect = true
    }
    basePath = projectDir.absolutePath
}

// https://detekt.dev/blog/2019/03/03/configure-detekt-on-root-project/
tasks.withType<Detekt>().configureEach {
    // internal 处理器不管
    exclude("internal-processors/**")

    include("**/src/*Main/kotlin/**/*.kt")
    include("**/src/*Main/kotlin/**/*.java")
    include("**/src/*Main/java/**/*.kt")
    include("**/src/*Main/java/**/*.java")
    include("**/src/main/kotlin/**/*.kt")
    include("**/src/main/kotlin/**/*.java")
    include("**/src/main/java/**/*.kt")
    include("**/src/main/java/**/*.java")

    exclude("**/src/*/resources/")
    exclude("**/build/")
    exclude("**/*Test/kotlin/")
    exclude("**/*Test/java/")
    exclude("**/test/kotlin/")
    exclude("**/test/java/")
}

apiValidation {
    // TODO 都差不多的时候别忘了去掉
    validationDisabled = true
    ignoredPackages.add("*.internal.*")

    this.ignoredProjects.addAll(
        listOf(
            "event-type-resolver-processor",
            "include-component-message-elements-processor",
        )
    )

    // 实验性和内部API可能无法保证二进制兼容
    nonPublicMarkers.addAll(
        listOf(
            "love.forte.simbot.annotations.ExperimentalSimbotAPI",
            "love.forte.simbot.annotations.InternalSimbotAPI",
            "love.forte.simbot.component.onebot.v11.common.api.ApiResultConstructor",
            "love.forte.simbot.component.onebot.v11.common.event.SourceEventConstructor",
        ),
    )

    apiDumpDirectory = "api"
}
