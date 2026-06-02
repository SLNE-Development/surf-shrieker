package dev.slne.surf.shrieker.paper

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import de.maxhenkel.voicechat.api.BukkitVoicechatService
import dev.slne.surf.api.paper.event.register
import dev.slne.surf.api.paper.extensions.pluginManager
import dev.slne.surf.shrieker.core.paper.ClientShriekerInstance
import dev.slne.surf.shrieker.core.paper.redisApi
import dev.slne.surf.shrieker.paper.command.reportCommand
import dev.slne.surf.shrieker.paper.listener.ShriekerRedisListener
import dev.slne.surf.shrieker.paper.listener.VoiceChatListener
import dev.slne.surf.shrieker.paper.voice.VoicePlugin
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

        VoiceChatListener.register()

        server.servicesManager.load(BukkitVoicechatService::class.java)?.registerPlugin(VoicePlugin)
    }

    override suspend fun onDisableAsync() {
        ClientShriekerInstance.clientLoader.onDisable()
    }

    fun hasVoiceChatSupport() = pluginManager.isPluginEnabled("voicechat")
}