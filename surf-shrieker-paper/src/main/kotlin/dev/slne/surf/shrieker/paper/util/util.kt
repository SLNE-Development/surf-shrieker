package dev.slne.surf.shrieker.paper.util

import dev.slne.surf.api.core.messages.builder.SurfComponentBuilder
import org.bukkit.Location

fun Location.dialogString() =
    "${x.toInt()}, ${y.toInt()}, ${z.toInt()} in ${world?.name ?: "unknown world"}"

fun SurfComponentBuilder.appendShriekerPrefix() = append {
    spacer("»")
    appendSpace()
    error("Reports")
    appendSpace()
    darkSpacer("|")
    appendSpace()
}