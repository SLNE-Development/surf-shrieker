package dev.slne.surf.shrieker.core.paper.service

import dev.slne.surf.shrieker.core.common.service.ReportBanService
import dev.slne.surf.shrieker.core.common.service.ReportService
import dev.slne.surf.shrieker.core.common.service.VoiceLogService

object ReportServices {
    private val voiceLogService: VoiceLogService by lazy {
        TODO("Implement service discovery and retrieval")
    }

    private val reportService: ReportService by lazy {
        TODO("Implement service discovery and retrieval")
    }

    private val reportBanService: ReportBanService by lazy {
        TODO("Implement service discovery and retrieval")
    }

    fun voiceLog() = voiceLogService
    fun report() = reportService
    fun reportBan() = reportBanService
}