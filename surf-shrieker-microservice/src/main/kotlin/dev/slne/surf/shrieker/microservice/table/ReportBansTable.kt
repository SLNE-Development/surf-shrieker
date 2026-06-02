package dev.slne.surf.shrieker.microservice.table

import dev.slne.surf.database.columns.nativeUuid
import dev.slne.surf.database.columns.time.offsetDateTime
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.core.dao.id.ULongIdTable
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.javatime.duration

object ReportBansTable : ULongIdTable("shrieker_bans") {
    val bannedPlayer = nativeUuid("banned_player").index()
    val bannedBy = nativeUuid("banned_by").nullable()
    val bannedAt = offsetDateTime("banned_at")

    val duration = duration("duration").nullable().index()

    val unbanned = bool("unbanned").default(false).index()
    val unbannedAt = offsetDateTime("unbanned_at").nullable()
    val unbannedBy = nativeUuid("unbanned_by").nullable()
}