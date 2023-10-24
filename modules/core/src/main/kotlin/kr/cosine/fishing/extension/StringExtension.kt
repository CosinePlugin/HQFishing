package kr.cosine.fishing.extension

import net.md_5.bungee.api.ChatColor

internal fun String.removeColor(): String = ChatColor.stripColor(this)