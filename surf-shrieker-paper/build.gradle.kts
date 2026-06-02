import dev.slne.surf.api.gradle.util.registerRequired
import dev.slne.surf.api.gradle.util.registerSoft

plugins {
    id("dev.slne.surf.api.gradle.paper-plugin")
}

surfPaperPluginApi {
    mainClass("dev.slne.surf.shrieker.paper.PaperMain")
    generateLibraryLoader(false)
    foliaSupported(true)

    withSurfRedis()
    withCorePaper()

    authors.add("red")
    serverDependencies {
        registerRequired("LuckPerms")
        registerRequired("surf-rabbitmq-paper")
        registerRequired("surf-chat-paper")
        registerSoft("voicechat")
    }
}

repositories {
    maven("https://maven.maxhenkel.de/repository/public")
}

dependencies {
    api(projects.surfShriekerCore.surfShriekerCorePaper)
    compileOnly("dev.slne.surf.chat:surf-chat-api:+")
    compileOnly("de.maxhenkel.voicechat:voicechat-api:2.6.13")
}