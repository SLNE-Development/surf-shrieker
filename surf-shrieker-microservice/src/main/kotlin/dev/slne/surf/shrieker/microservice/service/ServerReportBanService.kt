package dev.slne.surf.shrieker.microservice.service

import dev.slne.surf.api.core.serializer.java.datetime.datetime.offset.SerializableOffsetDateTime
import dev.slne.surf.api.core.serializer.java.uuid.SerializableUUID
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.core.eq
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.r2dbc.insert
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.r2dbc.select
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction
import dev.slne.surf.shrieker.core.common.service.ReportBanService
import dev.slne.surf.shrieker.microservice.table.ReportBansTable
import kotlinx.coroutines.flow.any

object ServerReportBanService : ReportBanService {
    override suspend fun ban(
        player: SerializableUUID,
        bannedBy: SerializableUUID?,
        bannedAt: SerializableOffsetDateTime
    ) = suspendTransaction {
        ReportBansTable.insert {
            it[this.bannedPlayer] = player
            it[this.bannedAt] = bannedAt
            it[this.bannedBy] = bannedBy
        }

        Unit
    }

    override suspend fun isBanned(player: SerializableUUID): Boolean = suspendTransaction {
        ReportBansTable.select(
            ReportBansTable.bannedPlayer,
            ReportBansTable.duration,
            ReportBansTable.unbanned,
            ReportBansTable.bannedAt
        ).where(
            ReportBansTable.bannedPlayer eq player
        ).any {
            if (it[ReportBansTable.unbanned]) {
                return@any false
            }

            val duration = it[ReportBansTable.duration]
                ?: return@any true

            val unbanAt = it[ReportBansTable.bannedAt].plus(duration)

            unbanAt.isAfter(SerializableOffsetDateTime.now())
        }
    }
}