package dev.slne.surf.shrieker.paper.listener

import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.redis.event.OnRedisEvent
import dev.slne.surf.shrieker.api.packet.ReportCreatedRedisEvent
import dev.slne.surf.shrieker.api.packet.ReportResolvedRedisEvent
import dev.slne.surf.shrieker.paper.permission.PermissionList
import dev.slne.surf.shrieker.paper.util.appendShriekerPrefix
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bukkit.Bukkit

object ShriekerRedisListener {
    @OnRedisEvent
    suspend fun onReportCreated(event: ReportCreatedRedisEvent) = withContext(Dispatchers.IO) {
        val report = event.report

        Bukkit.getOnlinePlayers().filter { it.hasPermission(PermissionList.REPORT_NOTIFY) }
            .forEach {
                it.sendText {
                    appendShriekerPrefix()
                    info("Der Spieler ")
                    variableValue(report.reportedUser().username)
                    info(" wurde von ")
                    variableValue(report.reporterUser().username)
                    info(" für ")
                    variableValue(report.type.displayName)
                    info(" gemeldet.")
                }
            }
    }

    @OnRedisEvent
    suspend fun onReportResolved(event: ReportResolvedRedisEvent) = withContext(Dispatchers.IO) {
        val report = event.report
        val player = Bukkit.getPlayer(report.reporter) ?: return@withContext

        player.sendText {
            appendShriekerPrefix()
            info("Dein Report über ")
            variableValue(report.reportedUser().username)
            info(" wurde von einem Teammitglied bearbeitet.")
        }
    }
}