package dev.slne.surf.shrieker.paper.command

import dev.jorel.commandapi.kotlindsl.commandTree
import dev.slne.surf.api.core.command.args.awaiting
import dev.slne.surf.api.paper.command.executors.playerExecutorSuspend
import dev.slne.surf.core.api.common.player.SurfPlayer
import dev.slne.surf.core.api.paper.command.argument.surfOfflinePlayerArgument
import dev.slne.surf.shrieker.paper.permission.PermissionList
import dev.slne.surf.shrieker.paper.ui.dialog.reportTypeDialog

fun reportCommand() = commandTree("report") {
    withPermission(PermissionList.REPORT_COMMAND)

    surfOfflinePlayerArgument("target") {
        playerExecutorSuspend { player, arguments ->
            val target = arguments.awaiting<SurfPlayer>("target")

            player.showDialog(
                reportTypeDialog(
                    playerName = target.username,
                    playerUuid = target.uuid,
                )
            )
        }
    }
}