pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://reposilite.slne.dev/releases")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("dev.slne.surf.api.gradle.settings") version "+"
}
include("surf-shrieker-api")
include("surf-shrieker-core")
include("surf-shrieker-core:surf-shrieker-core-common")
include("surf-shrieker-core:surf-shrieker-core-paper")
include("surf-shrieker-paper")
include("surf-shrieker-microservice")