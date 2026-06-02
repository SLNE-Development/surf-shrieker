package dev.slne.surf.shrieker.microservice.service

import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.core.statements.api.ExposedBlob
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.r2dbc.insertReturning
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction
import dev.slne.surf.shrieker.core.common.service.VoiceLogService
import dev.slne.surf.shrieker.microservice.table.VoiceChatLogTable
import kotlinx.coroutines.flow.firstOrNull

object ServerVoiceLogService : VoiceLogService {
    override suspend fun logVoice(reportId: Long, voiceData: ByteArray): Long = suspendTransaction {
        VoiceChatLogTable.insertReturning {
            it[this.reportId] = reportId
            it[this.voiceBytes] = ExposedBlob(voiceData)
        }.firstOrNull()?.get(VoiceChatLogTable.id)?.value ?: error("Failed to insert voice log")
    }
}