@file:Suppress("UnstableApiUsage")

package dev.slne.surf.shrieker.paper.ui.dialog

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.api.core.font.toSmallCaps
import dev.slne.surf.api.core.messages.adventure.appendNewline
import dev.slne.surf.api.core.messages.adventure.buildText
import dev.slne.surf.api.core.messages.adventure.key
import dev.slne.surf.api.paper.dialog.base
import dev.slne.surf.api.paper.dialog.dialog
import dev.slne.surf.api.paper.dialog.type
import dev.slne.surf.shrieker.api.type.ReportDetails
import dev.slne.surf.shrieker.api.type.ReportType
import dev.slne.surf.shrieker.paper.plugin
import dev.slne.surf.shrieker.paper.service.ReportStatusService
import dev.slne.surf.shrieker.paper.util.dialogString
import io.papermc.paper.dialog.Dialog
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player
import java.util.*

fun reportDetailsDialog(
    reporter: Player,
    reportedName: String,
    reportedUuid: UUID,
    reportType: ReportType
): Dialog = dialog {
    base {
        title {
            variableValue("$reportedName reporten".toSmallCaps(), TextDecoration.BOLD)
        }

        body {
            plainMessage(300) {
                variableValue("Willkommen im Report-System!", TextDecoration.BOLD)
                appendNewline(3)
                info("Hier kannst du einen Spieler reporten, der gegen die Regeln verstößt.")
                if (ReportDetails.byDataType(reportType).isNotEmpty()) {
                    appendNewline()
                    error("Gebe hier weite Informationen an. Je mehr Informationen du bereitstellst, desto besser können wir den Report bearbeiten.")
                }
            }

            input {
                ReportDetails.byDataType(reportType).forEach {
                    text(it.name) {
                        width(400)
                        multiline(15)
                        maxLength(2000)
                        label {
                            info(it.displayName)
                            hoverEvent(buildText {
                                spacer(it.description)
                            })
                        }

                        if (it == ReportDetails.GRIEF_LOCATION) {
                            initial(reporter.location.dialogString())
                            multiline(1)
                        }
                    }
                }
            }
        }

        type {
            confirmation {
                yes {
                    label {
                        error("Zurück")
                    }

                    action {
                        customPlayerClick { response, player ->
                            player.showDialog(
                                reportTypeDialog(
                                    reportedName,
                                    reportedUuid
                                )
                            )
                        }
                    }
                }

                no {
                    label {
                        info("Report absenden")
                    }

                    action {
                        customPlayerClick { response, player ->
                            val details =
                                ReportDetails.byDataType(reportType).associateWith { reportDetail ->
                                    response.getText(reportDetail.name)
                                }.mapKeys { key(it.key.name.lowercase()) }

                            player.closeDialog()

                            plugin.launch {
                                ReportStatusService.handleReport(
                                    player,
                                    reportedUuid,
                                    reportedName,
                                    reportType,
                                    details.filterValues { it != null }.mapValues { it.value!! }
                                        .toMutableMap()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}