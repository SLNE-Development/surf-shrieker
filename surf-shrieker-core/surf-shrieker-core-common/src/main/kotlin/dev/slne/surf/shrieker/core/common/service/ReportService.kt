package dev.slne.surf.shrieker.core.common.service

import dev.slne.surf.api.core.serializer.java.datetime.datetime.offset.SerializableOffsetDateTime
import dev.slne.surf.api.core.serializer.java.uuid.SerializableUUID
import dev.slne.surf.rabbitmq.api.rpc.RpcService
import dev.slne.surf.shrieker.api.Report
import dev.slne.surf.shrieker.api.ReportData
import dev.slne.surf.shrieker.api.type.ReportType
import java.util.*

@RpcService
interface ReportService {
    suspend fun report(
        reporter: SerializableUUID,
        reported: SerializableUUID,
        type: ReportType,
        data: ReportData,
        reportedAt: SerializableOffsetDateTime
    ): Report

    suspend fun addData(reportId: Long, data: ReportData)

    suspend fun findReportsByReporter(reporter: UUID): List<Report>
    suspend fun findReportsByReported(reported: UUID): List<Report>
}