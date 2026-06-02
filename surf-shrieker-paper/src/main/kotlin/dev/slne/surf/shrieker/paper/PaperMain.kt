package dev.slne.surf.shrieker.paper

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.surf.shrieker.core.paper.ClientShriekerInstance
import dev.slne.surf.shrieker.core.paper.redisApi
import dev.slne.surf.shrieker.paper.command.reportCommand
import dev.slne.surf.shrieker.paper.listener.ShriekerRedisListener
import org.bukkit.plugin.java.JavaPlugin

val plugin get() = JavaPlugin.getPlugin(PaperMain::class.java)

class PaperMain : SuspendingJavaPlugin() {
    override suspend fun onLoadAsync() {
        ClientShriekerInstance.clientLoader.onLoad()
        redisApi.subscribeToEvents(ShriekerRedisListener)

        reportCommand()
    }

    override suspend fun onEnableAsync() {
        ClientShriekerInstance.clientLoader.onEnable()
    }

    override suspend fun onDisableAsync() {
        ClientShriekerInstance.clientLoader.onDisable()
    }
}