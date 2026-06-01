import dev.slne.surf.api.gradle.util.registerRequired

plugins {
    id("dev.slne.surf.api.gradle.paper-plugin")
}

surfPaperPluginApi {
    mainClass("dev.slne.surf.shrieker.paper.PaperMain")
    generateLibraryLoader(false)
    foliaSupported(true)

    withSurfRedis()

    authors.add("red")

    bootstrapDependencies {
        registerRequired("surf-rabbitmq-paper")
    }

    serverDependencies {
        registerRequired("LuckPerms")
        registerRequired("surf-rabbitmq-paper")
    }
}

dependencies {
    api(projects.surfShriekerCore.surfShriekerCorePaper)
}