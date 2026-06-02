package dev.slne.surf.shrieker.microservice.table

import dev.slne.surf.api.core.messages.adventure.toKey
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.core.dao.id.ULongIdTable

object ReportDataTable : ULongIdTable("shrieker_data") {
    val reportId = long("report_id").references(ReportsTable.id).index()
    val key = varchar("key", 255).transform({ it.toKey() }, { it.toString() }).index()
    val value = text("value")

    init {
        uniqueIndex(reportId, key)
    }
}