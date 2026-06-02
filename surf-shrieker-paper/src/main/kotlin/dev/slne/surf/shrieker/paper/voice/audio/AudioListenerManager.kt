package dev.slne.surf.shrieker.paper.voice.audio

import de.maxhenkel.voicechat.api.audiolistener.PlayerAudioListener
import dev.slne.surf.shrieker.paper.voice.VoicePlugin
import dev.slne.surf.shrieker.paper.voice.packet.VoicePacket
import java.nio.ByteBuffer
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object AudioListenerManager {
    private val buffers = ConcurrentHashMap<UUID, PlayerAudioBuffer>()
    private val listeners = ConcurrentHashMap<UUID, PlayerAudioListener>()

    fun registerListener(playerUuid: UUID) {
        val api = VoicePlugin.api
        val buf = PlayerAudioBuffer()
        buffers[playerUuid] = buf

        val listener = api.playerAudioListenerBuilder()
            .setPlayer(playerUuid)
            .setPacketListener { packet ->
                val opusData = packet.opusEncodedData
                    ?.takeIf { it.isNotEmpty() }
                    ?: return@setPacketListener

                val sender = packet.sender ?: return@setPacketListener

                buf.add(
                    VoicePacket(
                        timestamp = System.currentTimeMillis(),
                        sender = sender,
                        opus = opusData
                    )
                )
            }
            .build()

        api.registerAudioListener(listener)
        listeners[playerUuid] = listener
    }

    fun unregisterListener(playerUuid: UUID) {
        listeners.remove(playerUuid)?.let { VoicePlugin.api.unregisterAudioListener(it) }
        buffers.remove(playerUuid)
    }

    fun getLog(playerUuid: UUID): ByteArray {
        val packets = buffers[playerUuid]?.snapshot() ?: return byteArrayOf()
        return packPackets(packets)
    }

    fun getLog(hearer: UUID, speaker: UUID): ByteArray {
        val packets = buffers[hearer]?.snapshot()
            ?.filter { it.sender == speaker }
            ?: return byteArrayOf()
        return packPackets(packets)
    }

    private fun packPackets(packets: List<VoicePacket>): ByteArray {
        val totalSize = packets.sumOf { 8 + 16 + 4 + it.opus.size }
        val buf = ByteBuffer.allocate(totalSize)

        for (packet in packets) {
            buf.putLong(packet.timestamp)
            buf.putLong(packet.sender.mostSignificantBits)
            buf.putLong(packet.sender.leastSignificantBits)
            buf.putInt(packet.opus.size)
            buf.put(packet.opus)
        }

        return buf.array()
    }
}