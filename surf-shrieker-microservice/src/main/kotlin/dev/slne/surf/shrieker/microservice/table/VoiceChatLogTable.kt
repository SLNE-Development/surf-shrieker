package dev.slne.surf.shrieker.microservice.table

import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object VoiceChatLogTable : LongIdTable("shrieker_voice_logs") {
    val reportId = long("report_id").references(ReportsTable.id)
    val voiceBytes = blob("voiceBytes")
}