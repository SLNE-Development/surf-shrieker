package dev.slne.surf.shrieker.paper.permission

import dev.slne.surf.api.paper.permission.PermissionRegistry

object PermissionList : PermissionRegistry() {
    private const val BASE = "surf.shrieker"
    private const val BASE_COMMAND = "$BASE.command"

    val REPORT_COMMAND = create("$BASE_COMMAND.report")
}