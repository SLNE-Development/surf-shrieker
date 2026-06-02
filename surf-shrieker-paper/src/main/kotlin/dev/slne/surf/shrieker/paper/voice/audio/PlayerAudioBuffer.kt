package dev.slne.surf.shrieker.paper.voice.audio

import dev.slne.surf.shrieker.paper.voice.packet.VoicePacket
import java.util.*

class PlayerAudioBuffer {
    private val buffer = ArrayDeque<VoicePacket>()

    @Synchronized
    fun add(packet: VoicePacket) {
        val now = System.currentTimeMillis()
        buffer.addLast(packet)

        val cutoff = now - (10 * 60 * 1000L)
        while (buffer.isNotEmpty() && buffer.peekFirst().timestamp < cutoff) {
            buffer.pollFirst()
        }
    }

    @Synchronized
    fun snapshot(): List<VoicePacket> = buffer.toList()
}