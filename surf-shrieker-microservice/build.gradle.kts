import dev.slne.surf.microservice.gradle.plugin.rabbit.RabbitModule

plugins {
    id("dev.slne.surf.api.gradle.standalone")
    id("dev.slne.surf.microservice")
}

dependencies {
    api(projects.surfShriekerCore.surfShriekerCoreCommon)
    implementation("dev.slne.surf.core:surf-core-api-common:+")
}

surfStandaloneApi {
    withSurfDatabaseR2dbc("1.3.0", "dev.slne.surf.shrieker.libs")
}

surfMicroservice {
    withMicroserviceApi()
    withRabbitModule(RabbitModule.SERVER_API, true)
}