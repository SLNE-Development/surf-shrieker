package dev.slne.surf.shrieker.microservice.table

import dev.slne.surf.database.columns.nativeUuid
import dev.slne.surf.database.columns.time.offsetDateTime
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.core.dao.id.ULongIdTable

object ReportBansTable : ULongIdTable("shrieker_bans") {
    val bannedBy = nativeUuid("banned_by")
    val bannedAt = offsetDateTime("banned_at")

    val unbanned = bool("unbanned").default(false)
    val unbannedAt = offsetDateTime("unbanned_at").nullable()
    val unbannedBy = nativeUuid("unbanned_by").nullable()
}