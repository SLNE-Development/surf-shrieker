package dev.slne.surf.shrieker.microservice.service

import dev.slne.surf.api.core.serializer.java.datetime.datetime.offset.SerializableOffsetDateTime
import dev.slne.surf.api.core.serializer.java.uuid.SerializableUUID
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.core.ResultRow
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.core.eq
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.r2dbc.batchInsert
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.r2dbc.insertReturning
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.r2dbc.selectAll
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction
import dev.slne.surf.shrieker.api.Report
import dev.slne.surf.shrieker.api.ReportData
import dev.slne.surf.shrieker.api.type.ReportType
import dev.slne.surf.shrieker.core.common.service.ReportService
import dev.slne.surf.shrieker.microservice.table.ReportDataTable
import dev.slne.surf.shrieker.microservice.table.ReportsTable
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import java.util.*

object ServerReportService : ReportService {
    override suspend fun report(
        reporter: SerializableUUID,
        reported: SerializableUUID,
        type: ReportType,
        data: ReportData,
        reportedAt: SerializableOffsetDateTime
    ): Report = suspendTransaction {
        val report = ReportsTable.insertReturning {
            it[this.reporter] = reporter
            it[this.reported] = reported
            it[this.reportedAt] = reportedAt
            it[this.type] = type
        }.firstOrNull()?.createReport(data) ?: error("Failed to create report")

        ReportDataTable.batchInsert(data.entries) { entry ->
            this[ReportDataTable.reportId] = report.internalId
            this[ReportDataTable.key] = entry.key
            this[ReportDataTable.value] = entry.value
        }

        return@suspendTransaction report
    }

    override suspend fun addData(
        reportId: Long,
        data: ReportData
    ) = suspendTransaction {
        ReportDataTable.batchInsert(data.entries) { entry ->
            this[ReportDataTable.reportId] = reportId
            this[ReportDataTable.key] = entry.key
            this[ReportDataTable.value] = entry.value
        }

        Unit
    }

    override suspend fun findReportsByReporter(reporter: UUID): List<Report> = suspendTransaction {
        (ReportsTable innerJoin ReportDataTable)
            .selectAll()
            .where { ReportsTable.reporter eq reporter }
            .toList()
            .groupBy { it[ReportsTable.id].value }
            .values
            .map { rows ->
                val first = rows.first()

                val data = rows.associate {
                    it[ReportDataTable.key] to it[ReportDataTable.value]
                }.toMutableMap()

                first.createReport(data)
            }
    }

    override suspend fun findReportsByReported(reported: UUID): List<Report> = suspendTransaction {
        (ReportsTable innerJoin ReportDataTable)
            .selectAll()
            .where { ReportsTable.reported eq reported }
            .toList()
            .groupBy { it[ReportsTable.id].value }
            .values
            .map { rows ->
                val first = rows.first()

                val data = rows.associate {
                    it[ReportDataTable.key] to it[ReportDataTable.value]
                }.toMutableMap()

                first.createReport(data)
            }
    }

    private fun ResultRow.createReport(data: ReportData) = Report(
        reporter = this[ReportsTable.reporter],
        reported = this[ReportsTable.reported],
        reportedAt = this[ReportsTable.reportedAt],
        type = this[ReportsTable.type],
        reportData = data,
        resolved = this[ReportsTable.resolved],
        resolvedResult = this[ReportsTable.resolvedResult],
        resolvedBy = this[ReportsTable.resolvedBy],
        resolvedAt = this[ReportsTable.resolvedAt],
        internalId = this[ReportsTable.id].value,
        resolvedText = this[ReportsTable.resolvedText],
        resolvedNotified = this[ReportsTable.resolvedNotified],
    )
}