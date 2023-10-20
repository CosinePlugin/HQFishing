package kr.cosine.fishing.extension

import org.bukkit.plugin.Plugin

internal fun Plugin.runTaskLater(delay: Long = 1L, actionFunction: () -> Unit) {
    server.scheduler.runTaskLater(this, actionFunction, delay)
}

internal fun Plugin.sync(actionFunction: () -> Unit) {
    server.scheduler.runTask(this, actionFunction)
}

internal fun Plugin.async(actionFunction: () -> Unit) {
    server.scheduler.runTaskAsynchronously(this, actionFunction)
}