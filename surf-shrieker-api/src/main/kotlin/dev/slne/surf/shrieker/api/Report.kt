package dev.slne.surf.shrieker.api

import dev.slne.surf.api.core.serializer.adventure.key.SerializableKey
import dev.slne.surf.api.core.serializer.java.datetime.datetime.offset.SerializableOffsetDateTime
import dev.slne.surf.api.core.serializer.java.uuid.SerializableUUID
import dev.slne.surf.core.api.common.SurfCoreApi
import dev.slne.surf.core.api.common.player.SurfPlayer
import dev.slne.surf.shrieker.api.type.ReportType
import kotlinx.serialization.Serializable

typealias ReportData = MutableMap<SerializableKey, String>

@Serializable
data class Report(
    val reporter: SerializableUUID,
    val reported: SerializableUUID,
    val reportedAt: SerializableOffsetDateTime,

    val type: ReportType,
    val reportData: ReportData,

    val resolved: Boolean,
    val resolvedResult: Boolean?,
    val resolvedBy: SerializableUUID?,
    val resolvedAt: SerializableOffsetDateTime?,

    val resolvedText: String?,
    val resolvedNotified: Boolean,

    val internalId: Long,
) {
    private var reportedUser: SurfPlayer? = null
    private var reporterUser: SurfPlayer? = null
    private var resolvedByUser: SurfPlayer? = null

    suspend fun reporterUser() =
        reportedUser ?: SurfCoreApi.getOfflinePlayer(reporter).also { reportedUser = it }
        ?: error("Reporter player not found")

    suspend fun reportedUser() =
        reporterUser ?: SurfCoreApi.getOfflinePlayer(reported).also { reporterUser = it }
        ?: error("Reported player not found")

    suspend fun resolvedByUser() =
        resolvedByUser ?: resolvedBy?.let { SurfCoreApi.getOfflinePlayer(it) }
            .also { resolvedByUser = it }
}