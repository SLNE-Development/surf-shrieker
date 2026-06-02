package dev.slne.surf.shrieker.paper.command

import dev.jorel.commandapi.kotlindsl.commandTree
import dev.slne.surf.api.core.command.args.awaiting
import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.api.paper.command.executors.playerExecutorSuspend
import dev.slne.surf.core.api.common.player.SurfPlayer
import dev.slne.surf.core.api.paper.command.argument.surfOfflinePlayerArgument
import dev.slne.surf.shrieker.core.paper.service.ReportServices
import dev.slne.surf.shrieker.paper.permission.PermissionList
import dev.slne.surf.shrieker.paper.ui.dialog.reportTypeDialog
import dev.slne.surf.shrieker.paper.util.appendShriekerPrefix

fun reportCommand() = commandTree("report") {
    withPermission(PermissionList.REPORT_COMMAND)

    surfOfflinePlayerArgument("target") {
        playerExecutorSuspend { player, arguments ->
            val target = arguments.awaiting<SurfPlayer>("target")

            if (ReportServices.reportBan().isBanned(player.uniqueId)) {
                player.sendText {
                    appendShriekerPrefix()
                    error("Du kannst keinen Report erstellen, da du von der Reportfunktion ausgeschlossen wurdest.")
                }
                return@playerExecutorSuspend
            }

            player.showDialog(
                reportTypeDialog(
                    playerName = target.username,
                    playerUuid = target.uuid,
                )
            )
        }
    }
}