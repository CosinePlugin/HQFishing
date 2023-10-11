package kr.cosine.fishing.announce

import org.bukkit.entity.Player

data class FishingChat(
    private val enabled: Boolean,
    val message: String
) {

    fun sendMessage(player: Player, fish: String) {
        if (enabled) {
            player.sendMessage(message.replace("%fish%", fish))
        }
    }
}