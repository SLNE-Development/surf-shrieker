package dev.slne.surf.shrieker.paper.voice.packet

import java.util.*

data class VoicePacket(
    val timestamp: Long,
    val sender: UUID,
    val opus: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VoicePacket

        if (timestamp != other.timestamp) return false
        if (sender != other.sender) return false
        if (!opus.contentEquals(other.opus)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = timestamp.hashCode()
        result = 31 * result + sender.hashCode()
        result = 31 * result + opus.contentHashCode()
        return result
    }
}