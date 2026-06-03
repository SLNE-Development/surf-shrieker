package dev.slne.surf.shrieker.paper.command

import dev.jorel.commandapi.kotlindsl.commandTree
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.literalArgument
import dev.jorel.commandapi.kotlindsl.longArgument
import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.api.paper.command.executors.playerExecutorSuspend
import dev.slne.surf.shrieker.core.paper.service.ReportServices
import dev.slne.surf.shrieker.paper.permission.PermissionList
import dev.slne.surf.shrieker.paper.util.appendShriekerPrefix
import dev.slne.surf.shrieker.paper.voice.media.AudioMediaPlayer

fun shriekerCommand() = commandTree("shrieker") {
    withPermission(PermissionList.SHRIEKER_COMMAND)
    literalArgument("debugvoice") {
        longArgument("voiceId") {
            playerExecutorSuspend { player, arguments ->
                val voiceId: Long by arguments
                val voiceBytes = ReportServices.voiceLog().getVoiceLog(voiceId)

                if (voiceBytes == null) {
                    player.sendText {
                        appendShriekerPrefix()
                        error("Es konnte kein Voice Log mit der ID $voiceId gefunden werden.")
                    }
                    return@playerExecutorSuspend
                }

                AudioMediaPlayer.playFromBytes(player, voiceBytes)

                player.sendText {
                    appendShriekerPrefix()
                    info("Der Voice Log $voiceId wird abgespielt.")
                }
            }
        }
    }
}