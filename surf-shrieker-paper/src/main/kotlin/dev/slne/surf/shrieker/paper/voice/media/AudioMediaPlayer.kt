package dev.slne.surf.shrieker.paper.voice.media

import dev.slne.surf.shrieker.paper.voice.VoicePlugin
import dev.slne.surf.shrieker.paper.voice.packet.VoicePacket
import java.nio.ByteBuffer
import java.util.*

object AudioMediaPlayer {
    fun unpackPackets(bytes: ByteArray): List<VoicePacket> {
        val buf = ByteBuffer.wrap(bytes)
        val packets = mutableListOf<VoicePacket>()

        while (buf.remaining() >= 8 + 16 + 4) {
            val timestamp = buf.getLong()
            val uuidMost = buf.getLong()
            val uuidLeast = buf.getLong()
            val sender = UUID(uuidMost, uuidLeast)
            val opusLen = buf.getInt()

            if (buf.remaining() < opusLen) break

            val opus = ByteArray(opusLen)
            buf.get(opus)

            packets.add(VoicePacket(timestamp, sender, opus))
        }

        return packets
    }

    fun playbackTo(listenerUuid: UUID, logBytes: ByteArray) {
        val api = VoicePlugin.api
        val connection = api.getConnectionOf(listenerUuid) ?: return
        val packets = unpackPackets(logBytes)
        if (packets.isEmpty()) return

        Thread {
            val baseTime = packets.first().timestamp
            val startedAt = System.currentTimeMillis()

            for (packet in packets) {
                val waitMs =
                    (packet.timestamp - baseTime) - (System.currentTimeMillis() - startedAt)
                if (waitMs > 0) Thread.sleep(waitMs)

                TODO()
            }
        }.apply {
            name = "ShriekerMediaPlayer-${listenerUuid.toString().substring(0..7)}"
        }.start()
    }
}

