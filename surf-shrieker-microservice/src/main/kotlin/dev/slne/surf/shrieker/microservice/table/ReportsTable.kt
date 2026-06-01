package dev.slne.surf.shrieker.microservice.table

import dev.slne.surf.database.columns.nativeUuid
import dev.slne.surf.database.columns.time.offsetDateTime
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import dev.slne.surf.shrieker.api.type.ReportType

object ReportsTable : LongIdTable("shrieker_reports") {
    val reporter = nativeUuid("reporter")
    val reported = nativeUuid("reported")
    val reportedAt = offsetDateTime("reported_at")

    val type = enumeration<ReportType>("type")

    val resolved = bool("resolved").default(false)
    val resolvedResult = bool("resolved_result").nullable()
    val resolvedBy = nativeUuid("resolved_by").nullable()
    val resolvedAt = offsetDateTime("resolved_at").nullable()
}