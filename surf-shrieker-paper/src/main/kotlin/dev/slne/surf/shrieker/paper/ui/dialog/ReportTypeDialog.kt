@file:Suppress("UnstableApiUsage")

package dev.slne.surf.shrieker.paper.ui.dialog

import dev.slne.surf.api.core.font.toSmallCaps
import dev.slne.surf.api.core.messages.adventure.appendNewline
import dev.slne.surf.api.core.messages.adventure.buildText
import dev.slne.surf.api.paper.dialog.base
import dev.slne.surf.api.paper.dialog.dialog
import dev.slne.surf.api.paper.dialog.type
import dev.slne.surf.shrieker.api.type.ReportType
import net.kyori.adventure.text.format.TextDecoration
import java.util.*

fun reportTypeDialog(playerName: String, playerUuid: UUID) = dialog {
    base {
        title {
            variableValue("$playerName reporten".toSmallCaps(), TextDecoration.BOLD)
        }

        body {
            plainMessage(300) {
                variableValue("Willkommen im Report-System!", TextDecoration.BOLD)
                appendNewline(3)
                info("Hier kannst du einen Spieler reporten, der gegen die Regeln verstoßen hat.")
                appendNewline()
                info("Wähle nun die Art des Verstoßes aus, um den Report ")
                appendNewline()
                info("Anschließend kannst du weitere Details angeben. Je mehr Informationen du bereitstellst, desto besser können wir den Report bearbeiten.")
            }
        }
    }

    type {
        multiAction {
            ReportType.entries.forEach {
                action {
                    label {
                        append(it)
                        hoverEvent(buildText {
                            spacer(it.description)
                        })
                    }

                    action {
                        customPlayerClick { _, player ->
                            player.showDialog(
                                reportDetailsDialog(
                                    player,
                                    playerName,
                                    playerUuid,
                                    it
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}