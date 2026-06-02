package dev.slne.surf.shrieker.paper.service

import com.github.shynixn.mccoroutine.folia.regionDispatcher
import dev.slne.surf.api.core.font.toSmallCaps
import dev.slne.surf.api.core.messages.adventure.key
import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.api.core.messages.adventure.showTitle
import dev.slne.surf.api.paper.util.readableString
import dev.slne.surf.shrieker.api.ReportData
import dev.slne.surf.shrieker.api.packet.ReportCreatedRedisEvent
import dev.slne.surf.shrieker.api.state.ReportProcessState
import dev.slne.surf.shrieker.api.type.ReportType
import dev.slne.surf.shrieker.core.paper.redisApi
import dev.slne.surf.shrieker.core.paper.service.ReportServices
import dev.slne.surf.shrieker.paper.plugin
import dev.slne.surf.shrieker.paper.voice.audio.AudioListenerManager
import kotlinx.coroutines.*
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.time.OffsetDateTime
import java.util.*
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

object ReportStatusService {
    suspend fun handleReport(
        reporter: Player,
        reportedUuid: UUID,
        reportedName: String,
        type: ReportType,
        data: ReportData
    ) {
        var currentState = ReportProcessState.PROCESSING
        var animationJob: Job? = null

        supervisorScope {
            runCatching {
                animationJob = launch {
                    var dots = 0
                    while (isActive) {
                        val dotStr = ".".repeat(dots + 1)
                        reporter.showTitle {
                            title {
                                variableValue(
                                    "Report wird erstellt".toSmallCaps(),
                                    TextDecoration.BOLD
                                )
                            }

                            subtitle {
                                info("${currentState.text} $dotStr")
                            }

                            times {
                                fadeIn(0)
                                stay(200)
                                fadeOut(0)
                            }
                        }
                        dots = (dots + 1) % 3
                        delay(500.milliseconds)
                    }
                }

                currentState = ReportProcessState.COLLECTING_ADDITIONAL_DATA
                val additionalData: ReportData = mutableMapOf()

                if (type == ReportType.CHAT) {
                    additionalData[key("chat_start")] =
                        OffsetDateTime.now().minusMinutes(10).toString()
                    additionalData[key("chat_end")] = OffsetDateTime.now().toString()
                }

                delay(1.seconds)
                currentState = ReportProcessState.SAVING_REPORT
                val report = ReportServices.report().report(
                    reporter.uniqueId,
                    reportedUuid,
                    type,
                    (data + additionalData).toMutableMap(),
                    OffsetDateTime.now()
                )

                delay(1.seconds)
                currentState = ReportProcessState.COLLECTING_ADDITIONAL_DATA
                if (type == ReportType.VOICE) {
                    val voiceLog = AudioListenerManager.getLog(reportedUuid)
                    val contextVoiceLog =
                        AudioListenerManager.getLog(reporter.uniqueId, reportedUuid)

                    val voiceId = ReportServices.voiceLog().logVoice(report.internalId, voiceLog)
                    val contextId =
                        ReportServices.voiceLog().logVoice(report.internalId, contextVoiceLog)

                    ReportServices.report().addData(
                        report.internalId,
                        mutableMapOf(
                            key("voice_log_id") to voiceId.toString(),
                            key("voice_context_log_id") to contextId.toString()
                        )
                    )
                }

                val tps = withContext(plugin.regionDispatcher(reporter.location)) {
                    Bukkit.getTPS().contentToString()
                }

                delay(1.seconds)
                currentState = ReportProcessState.SAVE_NOTES
                ReportServices.report().addData(
                    report.internalId,
                    mutableMapOf(
                        key("reporter_location") to reporter.location.readableString(true),
                        key("server_tps") to tps,
                        key("reporter_ping") to reporter.ping.toString(),
                        key("server_players") to Bukkit.getOnlinePlayers().size.toString()
                    )
                )

                delay(1.seconds)
                currentState = ReportProcessState.NOTIFYING_STAFF
                redisApi.publishEvent(ReportCreatedRedisEvent(report))

                delay(250.milliseconds)
                currentState = ReportProcessState.DONE

                animationJob.cancel()
            }.onFailure {
                reporter.clearTitle()
                reporter.sendText {
                    appendInfoPrefix()
                    error("Es ist ein Fehler aufgetreten! Bitte versuche es später erneut.")
                }

                plugin.logger.severe("Failed to create report: ${it.stackTraceToString()}")
            }

            if (animationJob != null && animationJob.isActive) {
                animationJob.cancel()
            }
        }

        reporter.clearTitle()
        reporter.sendText {
            appendInfoPrefix()
            info("Vielen Dank für deinen $type Report! Wir werden uns den Fall so schnell wie möglich anschauen.")
        }
    }
}