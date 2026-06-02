package dev.slne.surf.shrieker.paper.util

import org.bukkit.Location

fun Location.dialogString() =
    "(${x.toInt()}, ${y.toInt()}, ${z.toInt()} in ${world?.name ?: "unknown world"})"