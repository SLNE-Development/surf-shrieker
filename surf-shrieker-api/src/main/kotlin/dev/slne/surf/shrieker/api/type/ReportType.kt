package dev.slne.surf.shrieker.api.type

import dev.slne.surf.api.core.font.toSmallCaps
import dev.slne.surf.api.core.messages.adventure.buildText
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.format.TextDecoration

enum class ReportType(val displayName: String, val description: String) : ComponentLike {
    CHAT("Chat", "Beleidigungen, Spam, Werbung, etc."),
    TROLLING(
        "Trolling",
        "Absichtliches Stören des Spiels, z.B. durch absichtliches Sterben, Blockieren von Wegen, etc."
    ),
    GRIEF("Griefing", "Zerstören von Bauwerken, Stehlen von Items, etc."),
    SKIN("Skin", "Unangebrachte Skins, z.B. NSFW, rassistisch, etc."),
    NAME("Name", "Unangebrachte Namen, z.B. NSFW, rassistisch, etc."),
    BOOSTING(
        "Boosting/IRL-Trading",
        "Angebot von Boosting oder IRL-Trading, z.B. durch Chatnachrichten, etc."
    ),
    EXPLOITING("Bugusing", "Ausnutzen von Spielfehlern, z.B. durch Chatnachrichten, etc."),
    CLAN_NAME("Clan Name", "Unangebrachte Clan Namen, z.B. NSFW, rassistisch, etc."),
    VOICE(
        "Voicechat",
        "Trolling, Beleidigung im Voicechat, Abspielen von Musik in öffentlichen Bereichen"
    ), ;

    override fun asComponent() = buildText {
        error(displayName.toSmallCaps(), TextDecoration.BOLD)
    }

    companion object {
        const val TROLLING_LOCATION = "TROLLING_LOCATION"
        const val GRIEF_LOCATION = "GRIEF_LOCATION"
        const val GRIEF_ALLOWED = "GRIEF_ALLOWED"
        const val GRIEF_DESCRIPTION = "GRIEF_DESCRIPTION"
        const val EXPLOITING_DESCRIPTION = "EXPLOITING_DESCRIPTION"
        const val VOICE_DESCRIPTION = "VOICE_DESCRIPTION"

        fun dataTypesBy(type: ReportType) = when (type) {
            GRIEF -> listOf(GRIEF_ALLOWED, GRIEF_DESCRIPTION)
            VOICE -> listOf(VOICE_DESCRIPTION)
            EXPLOITING -> listOf(EXPLOITING_DESCRIPTION)
            else -> emptyList()
        }
    }
}