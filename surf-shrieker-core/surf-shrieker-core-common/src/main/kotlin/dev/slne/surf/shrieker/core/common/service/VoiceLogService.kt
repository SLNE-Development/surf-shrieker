package dev.slne.surf.shrieker.core.common.service

import dev.slne.surf.rabbitmq.api.rpc.RpcService

@RpcService
interface VoiceLogService {
    suspend fun logVoice(reportId: Long, voiceData: ByteArray): Long
}