package dev.slne.surf.shrieker.paper.voice

import de.maxhenkel.voicechat.api.VoicechatApi
import de.maxhenkel.voicechat.api.VoicechatPlugin
import de.maxhenkel.voicechat.api.VoicechatServerApi
import de.maxhenkel.voicechat.api.events.EventRegistration
import de.maxhenkel.voicechat.api.events.VoicechatServerStartedEvent
import dev.slne.surf.api.paper.util.forEachPlayer
import dev.slne.surf.shrieker.paper.plugin
import dev.slne.surf.shrieker.paper.voice.audio.AudioListenerManager

object VoicePlugin : VoicechatPlugin {
    lateinit var api: VoicechatServerApi
    var loaded = false

    override fun getPluginId() = plugin.pluginMeta.name

    override fun initialize(api: VoicechatApi) {
        VoicePlugin.api = api as VoicechatServerApi
        loaded = true
    }

    override fun registerEvents(registration: EventRegistration) {
        registration.registerEvent(VoicechatServerStartedEvent::class.java) { _ ->
            forEachPlayer { player ->
                AudioListenerManager.registerListener(player.uniqueId)
            }
        }
    }
}