package dev.slne.surf.shrieker.paper.voice.media

import dev.slne.surf.api.core.messages.adventure.buildText
import dev.slne.surf.shrieker.paper.voice.VoicePlugin
import dev.slne.surf.shrieker.paper.voice.packet.VoicePacket
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.entity.Player
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

    fun playFromBytes(player: Player, logBytes: ByteArray) {
        val api = VoicePlugin.api
        val connection = api.getConnectionOf(player.uniqueId) ?: return
        val packets = unpackPackets(logBytes)
        if (packets.isEmpty()) return

        val channel = api.createStaticAudioChannel(
            UUID.randomUUID()
        ) ?: return

        channel.addTarget(connection)

        val decoder = api.createDecoder()
        val allPcm = mutableListOf<Short>()
        val senderTimeline = mutableListOf<Pair<Long, UUID>>()

        val samplesPerFrame = 960
        val frameMs = 20L
        var currentSampleOffset = 0L

        for (i in packets.indices) {
            val packet = packets[i]

            if (i > 0) {
                val gap = packet.timestamp - packets[i - 1].timestamp
                val silentFrames = ((gap / frameMs) - 1).coerceIn(0, 500)
                repeat(silentFrames.toInt()) {
                    repeat(samplesPerFrame) { allPcm.add(0) }
                    currentSampleOffset += samplesPerFrame
                }
            }

            val offsetMs = (currentSampleOffset / 48)
            if (i == 0 || packets[i - 1].sender != packet.sender) {
                senderTimeline.add(Pair(offsetMs, packet.sender))
            }

            val pcm = decoder.decode(packet.opus)
            for (sample in pcm) allPcm.add(sample)
            currentSampleOffset += samplesPerFrame
        }

        decoder.close()

        Thread {
            val startedAt = System.currentTimeMillis()

            for ((offsetMs, senderUuid) in senderTimeline) {
                val waitMs = offsetMs - (System.currentTimeMillis() - startedAt)
                if (waitMs > 0) Thread.sleep(waitMs)

                val senderName = Bukkit.getOfflinePlayer(senderUuid).name ?: senderUuid.toString()

                player.sendActionBar(buildText {
                    variableValue("🔊 $senderName", TextDecoration.BOLD)
                })
            }

            val totalMs = currentSampleOffset / 48
            val remainingMs = totalMs - (System.currentTimeMillis() - startedAt)
            if (remainingMs > 0) Thread.sleep(remainingMs)

            player.sendActionBar(Component.empty())
        }.start()

        val audioPlayer = api.createAudioPlayer(
            channel,
            api.createEncoder(),
            allPcm.toShortArray()
        )

        audioPlayer.setOnStopped { channel.flush() }
        audioPlayer.startPlaying()
    }
}

