package dev.slne.surf.shrieker.core.paper.service

import dev.slne.surf.shrieker.core.common.service.ReportBanService
import dev.slne.surf.shrieker.core.common.service.ReportService
import dev.slne.surf.shrieker.core.common.service.VoiceLogService
import dev.slne.surf.shrieker.core.paper.ClientShriekerInstance

object ReportServices {
    private val voiceLogService: VoiceLogService by lazy {
        ClientShriekerInstance.rabbitApi.createRpcService()
    }

    private val reportService: ReportService by lazy {
        ClientShriekerInstance.rabbitApi.createRpcService()
    }

    private val reportBanService: ReportBanService by lazy {
        ClientShriekerInstance.rabbitApi.createRpcService()
    }

    fun voiceLog() = voiceLogService
    fun report() = reportService
    fun reportBan() = reportBanService
}