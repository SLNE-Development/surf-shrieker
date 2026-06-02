package dev.slne.surf.shrieker.paper.listener

import dev.slne.surf.shrieker.paper.voice.VoicePlugin
import dev.slne.surf.shrieker.paper.voice.audio.AudioListenerManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object VoiceChatListener : Listener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        if (!VoicePlugin.loaded) {
            return
        }

        AudioListenerManager.registerListener(event.player.uniqueId)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        AudioListenerManager.unregisterListener(event.player.uniqueId)
    }
}