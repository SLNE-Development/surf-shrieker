package dev.slne.surf.shrieker.api.packet

import dev.slne.surf.redis.event.RedisEvent
import dev.slne.surf.shrieker.api.Report
import kotlinx.serialization.Serializable

@Serializable
data class ReportResolvedRedisEvent(
    val report: Report
) : RedisEvent()
