package dev.slne.surf.shrieker.paper

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.surf.shrieker.paper.command.reportCommand
import org.bukkit.plugin.java.JavaPlugin

val plugin get() = JavaPlugin.getPlugin(PaperMain::class.java)

class PaperMain : SuspendingJavaPlugin() {
    override suspend fun onLoadAsync() {
        reportCommand()
    }

    override suspend fun onEnableAsync() {
        super.onEnableAsync()
    }

    override suspend fun onDisableAsync() {
        super.onDisableAsync()
    }
}