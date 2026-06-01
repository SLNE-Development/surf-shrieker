package dev.slne.surf.shrieker.api.type

import dev.slne.surf.api.core.messages.adventure.buildText
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike

enum class ReportType(val displayName: String) : ComponentLike {
    CHAT("Chat"),
    TROLLING("Trolling"),
    SKIN("Skin"),
    NAME("Name"),
    BOOSTING("Boosting/IRL-Trading"),
    EXPLOITING("Bugusing"),
    CLAN_NAME("Clan Name"),
    VOICE("Voicechat"),;

    override fun asComponent() = buildText {
        red(displayName)
    }
}